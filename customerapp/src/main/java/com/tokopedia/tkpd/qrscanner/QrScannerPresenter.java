package com.tokopedia.tkpd.qrscanner;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.ovo.model.BarcodeResponseData;
import com.tokopedia.ovo.model.Errors;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.campaign.analytics.CampaignTracking;
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;
import com.tokopedia.tkpd.campaign.data.model.CampaignException;
import com.tokopedia.tkpd.campaign.di.IdentifierWalletQualifier;
import com.tokopedia.tkpd.campaign.domain.barcode.PostBarCodeDataUseCase;
import com.tokopedia.tkpd.deeplink.domain.branchio.BranchIODeeplinkUseCase;
import com.tokopedia.tkpd.deeplink.source.entity.BranchIOAndroidDeepLink;
import com.tokopedia.tokocash.balance.view.BalanceTokoCash;
import com.tokopedia.tokocash.network.exception.WalletException;
import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.core.network.retrofit.utils.AuthUtil.KEY.KEY_BRANCHIO;
import static com.tokopedia.tkpd.campaign.domain.barcode.PostBarCodeDataUseCase.CAMPAIGN_ID;


/**
 * Created by sandeepgoyal on 18/12/17.
 */

public class QrScannerPresenter extends BaseDaggerPresenter<QrScannerContract.View>
        implements QrScannerContract.Presenter {

    private static final String TAG_QR_PAYMENT = "QR";
    private static final String IDENTIFIER = "identifier";
    private static final String GOAL_QR_INQUIRY = "goalQRInquiry";
    private static final String ERRORS = "errors";
    private static final String MESSAGE = "message";
    private static final String QR_ID = "qr_id";
    private static final String OVO_TEXT = "ovo";
    private static final String GPNR_TEXT = "gpnqr";

    private PostBarCodeDataUseCase postBarCodeDataUseCase;
    private BranchIODeeplinkUseCase branchIODeeplinkUseCase;
    private Context context;
    private UserSessionInterface userSession;
    private LocalCacheHandler localCacheHandler;
    private CompositeSubscription compositeSubscription;

    @Inject
    public QrScannerPresenter(PostBarCodeDataUseCase postBarCodeDataUseCase,
                              BranchIODeeplinkUseCase branchIODeeplinkUseCase,
                              @ApplicationContext Context context,
                              @IdentifierWalletQualifier LocalCacheHandler localCacheHandler
    ) {
        this.postBarCodeDataUseCase = postBarCodeDataUseCase;
        this.context = context;
        this.userSession = new UserSession(context);
        this.localCacheHandler = localCacheHandler;
        this.branchIODeeplinkUseCase = branchIODeeplinkUseCase;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onBarCodeScanComplete(String barcodeData) {
        Uri uri = Uri.parse(barcodeData);
        String host = uri.getHost();
        boolean isOvoPayQrEnabled = getView().getRemoteConfigForOvoPay();
        if (host != null && uri.getPathSegments() != null) {
            if (host.equals(QrScannerTypeDef.PAYMENT_QR_CODE)) {
                onScanCompleteGetInfoQrPayment(uri.getPathSegments().get(0));
            } else if (host.equals(QrScannerTypeDef.CAMPAIGN_QR_CODE)) {
                onScanCompleteGetInfoQrCampaign(uri.getPathSegments().get(0));
            } else if (host.contains("tokopedia.link")) {
                onScanBranchIOLink(barcodeData);
            } else if (host.contains("tokopedia")) {
                openActivity(barcodeData);
            } else {
                getView().showErrorGetInfo(context.getString(R.string.msg_dialog_wrong_scan));
            }
        } else if (isOvoPayQrEnabled && barcodeData.toLowerCase().contains(OVO_TEXT)
                || barcodeData.toLowerCase().contains(GPNR_TEXT)) {
            checkBarCode(barcodeData);
        } else {
            getView().showErrorGetInfo(context.getString(R.string.msg_dialog_wrong_scan));
        }
    }

    private void checkBarCode(String barcodeData) {
        GraphqlUseCase graphqlUseCase = new GraphqlUseCase();
        Map<String, Object> variables = new HashMap<>();

        variables.put(QR_ID, barcodeData);
        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.verify_ovo_qr_code),
                BarcodeResponseData.class,
                variables);

        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showErrorGetInfo(context.getString(R.string.msg_dialog_wrong_scan));
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                BarcodeResponseData response = graphqlResponse.getData(BarcodeResponseData.class);
                if (response != null && response.getGoalQRInquiry() != null) {
                    List<Errors> errors = response.getGoalQRInquiry().getErrors();
                    if (errors != null && errors.size() > 0) {
                        Errors error = errors.get(0);
                        if (error != null && !TextUtils.isEmpty(error.getMessage())) {
                            getView().showErrorGetInfo(context.getString(R.string.msg_dialog_wrong_scan));
                        }
                    } else {
                        getView().goToPaymentPage(barcodeData, response);
                    }
                } else {
                    getView().showErrorGetInfo(context.getString(R.string.msg_dialog_wrong_scan));
                }

            }
        });
    }

    private void onScanBranchIOLink(String qrCode) {
        getView().showProgressDialog();
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("url", qrCode);
        requestParams.putString("branch_key", KEY_BRANCHIO);
        branchIODeeplinkUseCase.execute(requestParams, new Subscriber<BranchIOAndroidDeepLink>() {
            @Override
            public void onCompleted() {
                getView().hideProgressDialog();
                getView().finish();
            }

            @Override
            public void onError(Throwable e) {
                getView().hideProgressDialog();
                if (e instanceof CampaignException) {
                    getView().showErrorGetInfo(context.getString(R.string.msg_dialog_wrong_scan));
                    CampaignTracking.eventScanQRCode("fail", context.getString(R.string.msg_dialog_wrong_scan), "");
                } else {
                    getView().showErrorNetwork(e);
                    CampaignTracking.eventScanQRCode("fail", context.getString(R.string.msg_dialog_wrong_scan), "");
                }
            }

            @Override
            public void onNext(BranchIOAndroidDeepLink branchIOAndroidDeepLink) {
                openActivity(Constants.Schemes.APPLINKS + "://" + branchIOAndroidDeepLink.getAndroidDeeplinkPath());
                CampaignTracking.eventScanQRCode("success", "", branchIOAndroidDeepLink.getAndroidDeeplinkPath());
            }
        });
    }

    private void onScanCompleteGetInfoQrPayment(String qrcode) {
        if (isUserLogin()) {
            getAbTagsForContinuingPayment(qrcode);
        } else {
            localCacheHandler.putString(IDENTIFIER, qrcode);
            localCacheHandler.applyEditor();
            getView().interruptToLoginPage();
        }
    }

    private void onScanCompleteGetInfoQrCampaign(final String idCampaign) {
        getView().showProgressDialog();
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(CAMPAIGN_ID, idCampaign);
        postBarCodeDataUseCase.execute(requestParams, new Subscriber<CampaignResponseEntity>() {
            @Override
            public void onCompleted() {
                getView().hideProgressDialog();
                getView().finish();
            }

            @Override
            public void onError(Throwable e) {
                getView().hideProgressDialog();
                if (e instanceof CampaignException) {
                    getView().showErrorGetInfo(context.getString(R.string.msg_dialog_wrong_scan));
                } else {
                    getView().showErrorNetwork(e);
                }
                CampaignTracking.eventScanQRCode("fail", idCampaign, "");
            }

            @Override
            public void onNext(CampaignResponseEntity s) {
                openActivity(s.getUrl());
                CampaignTracking.eventScanQRCode("success", idCampaign, s.getUrl());
            }
        });
    }

    public void openActivity(String url) {
        Uri uri = Uri.parse("" + url);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        getView().startActivity(intent);

    }

    @Override
    public void onScanCompleteAfterLoginQrPayment() {
        if (isUserLogin()) {
            getAbTagsForContinuingPayment(localCacheHandler.getString(IDENTIFIER));
        } else {
            localCacheHandler.putString(IDENTIFIER, "");
            localCacheHandler.applyEditor();
        }
    }

    private boolean isUserLogin() {
        return userSession.isLoggedIn();
    }

    private void getAbTagsForContinuingPayment(final String qrCode) {
        getView().showProgressDialog();
        compositeSubscription.add(
                getView().getBalanceTokoCash()
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<BalanceTokoCash>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (isViewAttached()) {
                                    getView().hideProgressDialog();
                                    getView().showErrorNetwork(e);
                                }
                            }

                            @Override
                            public void onNext(BalanceTokoCash balanceTokoCash) {
                                if (isContinuingPayment(balanceTokoCash.getAbTags())) {
                                    getInfoQrWallet(qrCode);
                                } else {
                                    getView().hideProgressDialog();
                                    getView().showErrorGetInfo(context.getString(R.string.no_available_feature));
                                }
                            }
                        }));
    }

    private boolean isContinuingPayment(List<String> abTags) {
        if (abTags != null) {
            for (String abTag : abTags) {
                if (abTag.equals(TAG_QR_PAYMENT)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void getInfoQrWallet(final String qrcode) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(IDENTIFIER, qrcode);
        compositeSubscription.add(getView().getInfoQrTokoCash(requestParams)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InfoQrTokoCash>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().hideProgressDialog();
                            if (e instanceof WalletException) {
                                getView().showErrorGetInfo(e.getMessage());
                            } else {
                                getView().showErrorNetwork(e);
                            }
                        }
                    }

                    @Override
                    public void onNext(InfoQrTokoCash infoQrTokoCash) {
                        getView().hideProgressDialog();
                        if (infoQrTokoCash != null) {
                            getView().navigateToNominalActivityPage(qrcode, infoQrTokoCash);
                        } else {
                            getView().showErrorGetInfo(context.getString(R.string.msg_dialog_wrong_scan));
                        }
                    }
                })
        );
    }

    @Override
    public void destroyView() {
        if (compositeSubscription != null & compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
        if (postBarCodeDataUseCase != null) postBarCodeDataUseCase.unsubscribe();
    }
}
