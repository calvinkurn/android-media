package com.tokopedia.qrscanner;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.common_wallet.balance.domain.GetWalletBalanceUseCase;
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.qrscanner.branchio.BranchIOAndroidDeepLink;
import com.tokopedia.qrscanner.branchio.BranchIODeeplinkUseCase;
import com.tokopedia.qrscanner.event_redeem.data.EventRedeem;
import com.tokopedia.qrscanner.scanner.domain.usecase.ScannerUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import kotlin.Unit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.core.network.retrofit.utils.AuthUtil.KEY.KEY_BRANCHIO;

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
    private static final String QR_TEXT = "qr";
    private static final String LOGIN_TEXT = "login";
    private static final String EVENT_REDEEM = "tokopedia.com/v1/api/event/custom/redeem/invoice";

    private ScannerUseCase scannerUseCase;
    private BranchIODeeplinkUseCase branchIODeeplinkUseCase;
    private Context context;
    private UserSessionInterface userSession;
    private LocalCacheHandler localCacheHandler;
    private CompositeSubscription compositeSubscription;
    private GetWalletBalanceUseCase getWalletBalanceUseCase;

    @Inject
    public QrScannerPresenter(ScannerUseCase scannerUseCase,
                              BranchIODeeplinkUseCase branchIODeeplinkUseCase,
                              GetWalletBalanceUseCase getWalletBalanceUseCase,
                              @ApplicationContext Context context,
                              @IdentifierWalletQualifier LocalCacheHandler localCacheHandler
    ) {
        this.scannerUseCase = scannerUseCase;
        this.context = context;
        this.userSession = new UserSession(context);
        this.localCacheHandler = localCacheHandler;
        this.branchIODeeplinkUseCase = branchIODeeplinkUseCase;
        this.compositeSubscription = new CompositeSubscription();
        this.getWalletBalanceUseCase = getWalletBalanceUseCase;
    }

    @Override
    public void onBarCodeScanComplete(String barcodeData) {
        QRTracking.eventScanQRCode(userSession.getUserId());
        Uri uri = Uri.parse(barcodeData);
        String host = uri.getHost();
        boolean isOvoPayQrEnabled = getView().getRemoteConfigForOvoPay();
        if (host != null && uri.getPathSegments() != null) {
            if (host.equals(QrScannerTypeDef.CAMPAIGN_QR_CODE)) {
                onScanCompleteGetInfoQrCampaign(uri.getPathSegments().get(0));
            } else if (host.contains("tokopedia.link")) {
                onScanBranchIOLink(barcodeData);
            } else if (host.contains("tokopedia")) {
                openActivity(barcodeData);
            } else if (host.equals("login") &&
                    uri.getPathSegments().get(0) != null &&
                    uri.getPathSegments().get(0).equals("qr")) {
                getView().goToLoginByQr(barcodeData);
            } else {
                getView().showErrorGetInfo(context.getString(R.string.qr_scanner_msg_dialog_wrong_scan));
            }
        } else if (isOvoPayQrEnabled && barcodeData.toLowerCase().contains(OVO_TEXT)
                || barcodeData.toLowerCase().contains(GPNR_TEXT)) {
            checkBarCode(barcodeData);
        } else if (barcodeData.toLowerCase().contains(EVENT_REDEEM)){
            checkEventRedeem(barcodeData);
        } else {
            getView().showErrorGetInfo(context.getString(R.string.qr_scanner_msg_dialog_wrong_scan));
        }
    }

    private void checkBarCode(String barcodeData) {
        GraphqlUseCase graphqlUseCase = new GraphqlUseCase();
        Map<String, Object> variables = new HashMap<>();

        variables.put(QR_ID, barcodeData);
        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.verify_ovo_qr_code),
                JsonObject.class,
                variables);

        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showErrorGetInfo(context.getString(R.string.qr_scanner_msg_dialog_wrong_scan));
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                JsonObject response = graphqlResponse.getData(JsonObject.class);
                JsonArray error;
                if (response != null) {
                    JsonObject object = response.getAsJsonObject(GOAL_QR_INQUIRY);
                    if (object != null) {
                        error = object.getAsJsonArray(ERRORS);
                        if (error != null && error.size() > 0) {
                            JsonObject errorObject = error.get(0).getAsJsonObject();
                            if (errorObject != null && errorObject.get(MESSAGE) != null
                                    && !TextUtils.isEmpty(errorObject.get(MESSAGE).getAsString())) {
                                //error
                                getView().showErrorGetInfo(context.getString(R.string.qr_scanner_msg_dialog_wrong_scan));
                            }
                        } else {
                            getView().goToPaymentPage(barcodeData, response);
                        }
                    }
                } else {
                    getView().showErrorGetInfo(context.getString(R.string.qr_scanner_msg_dialog_wrong_scan));
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
                    getView().showErrorGetInfo(context.getString(R.string.qr_scanner_msg_dialog_wrong_scan));
                    QRTracking.eventScanQRCode("fail", context.getString(R.string.qr_scanner_msg_dialog_wrong_scan), "");
                } else {
                    getView().showErrorNetwork(e);
                    QRTracking.eventScanQRCode("fail", context.getString(R.string.qr_scanner_msg_dialog_wrong_scan), "");
                }
            }

            @Override
            public void onNext(BranchIOAndroidDeepLink branchIOAndroidDeepLink) {
                String path = branchIOAndroidDeepLink.getAndroidDeeplinkPath();
                if (!path.startsWith("tokopedia://")) {
                    path = "tokopedia://" + path;
                }
                openActivity(path);
                QRTracking.eventScanQRCode("success", "", branchIOAndroidDeepLink.getAndroidDeeplinkPath());
            }
        });
    }

    private void onScanCompleteGetInfoQrCampaign(final String idCampaign) {
        getView().showProgressDialog();
        scannerUseCase.setParams(idCampaign, false);
        scannerUseCase.execute(
            verificationResponse -> {
                getView().hideProgressDialog();

                if (!verificationResponse.getData().getUrl().isEmpty()) {
                    openActivity(verificationResponse.getData().getUrl());
                    QRTracking.eventScanQRCode("success", idCampaign, verificationResponse.getData().getUrl());
                    getView().finish();
                    return Unit.INSTANCE;
                } else {
                    getView().showErrorGetInfo(context.getString(R.string.qr_scanner_msg_dialog_wrong_scan));
                    QRTracking.eventScanQRCode("fail", idCampaign, "");
                    return Unit.INSTANCE;
                }
            },
            throwable -> {
                getView().showErrorNetwork(throwable);
                QRTracking.eventScanQRCode("fail", idCampaign, "");
                getView().hideProgressDialog();
                return Unit.INSTANCE;
            }
        );
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
                getWalletBalanceUseCase.createObservable(RequestParams.EMPTY)
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<WalletBalanceModel>() {
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
                            public void onNext(WalletBalanceModel walletBalanceModel) {
                                getView().hideProgressDialog();
                                getView().showErrorGetInfo(context.getString(R.string.qr_scanner_no_available_feature));
                            }
                        }));
    }

    private void checkEventRedeem(String qrCode){
        EventRedeem eventRedeem = new Gson().fromJson(qrCode, EventRedeem.class);
        getView().goToEventRedeemPage(eventRedeem.getUrl());
    }

    @Override
    public void destroyView() {
        if (compositeSubscription != null & compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
        if (scannerUseCase != null) {
            scannerUseCase.cancelJobs();
            scannerUseCase.clearCache();
        }
    }
}
