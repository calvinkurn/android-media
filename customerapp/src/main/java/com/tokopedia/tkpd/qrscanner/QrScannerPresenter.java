package com.tokopedia.tkpd.qrscanner;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.tkpd.campaign.analytics.CampaignTracking;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;
import com.tokopedia.tkpd.campaign.data.model.CampaignException;
import com.tokopedia.tkpd.campaign.di.IdentifierWalletQualifier;
import com.tokopedia.tkpd.campaign.domain.barcode.PostBarCodeDataUseCase;
import com.tokopedia.tkpd.deeplink.domain.branchio.BranchIODeeplinkUseCase;
import com.tokopedia.tkpd.deeplink.source.entity.BranchIOAndroidDeepLink;
import com.tokopedia.tokocash.network.exception.WalletException;
import com.tokopedia.tokocash.qrpayment.domain.GetBalanceTokoCashUseCase;
import com.tokopedia.tokocash.qrpayment.domain.GetInfoQrTokoCashUseCase;
import com.tokopedia.tokocash.qrpayment.presentation.activity.NominalQrPaymentActivity;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;
import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.tkpd.campaign.domain.barcode.PostBarCodeDataUseCase.CAMPAIGN_ID;


/**
 * Created by sandeepgoyal on 18/12/17.
 */

public class QrScannerPresenter extends BaseDaggerPresenter<QrScannerContract.View>
        implements QrScannerContract.Presenter {

    private static final String TAG_QR_PAYMENT = "QR";

    private PostBarCodeDataUseCase postBarCodeDataUseCase;
    private GetInfoQrTokoCashUseCase getInfoQrTokoCashUseCase;
    private GetBalanceTokoCashUseCase getBalanceTokoCashUseCase;
    private BranchIODeeplinkUseCase branchIODeeplinkUseCase;
    private Context context;
    private UserSession userSession;
    private LocalCacheHandler localCacheHandler;

    @Inject
    public QrScannerPresenter(PostBarCodeDataUseCase postBarCodeDataUseCase,
                              GetInfoQrTokoCashUseCase getInfoQrTokoCashUseCase,
                              GetBalanceTokoCashUseCase getBalanceTokoCashUseCase,
                              BranchIODeeplinkUseCase branchIODeeplinkUseCase,
                              @ApplicationContext Context context, UserSession userSession,
                              @IdentifierWalletQualifier LocalCacheHandler localCacheHandler
    ) {
        this.postBarCodeDataUseCase = postBarCodeDataUseCase;
        this.getInfoQrTokoCashUseCase = getInfoQrTokoCashUseCase;
        this.context = context;
        this.userSession = userSession;
        this.localCacheHandler = localCacheHandler;
        this.getBalanceTokoCashUseCase = getBalanceTokoCashUseCase;
        this.branchIODeeplinkUseCase = branchIODeeplinkUseCase;
    }

    @Override
    public void onBarCodeScanComplete(String barcodeData) {
        Uri uri = Uri.parse(barcodeData);
        String host = uri.getHost();

        if (host != null && uri.getPathSegments() != null) {
            if (host.equals(QrScannerTypeDef.PAYMENT_QR_CODE)) {
                onScanCompleteGetInfoQrPayment(uri.getPathSegments().get(0));
            } else if (host.equals(QrScannerTypeDef.CAMPAIGN_QR_CODE)) {
                onScanCompleteGetInfoQrCampaign(uri.getPathSegments().get(0));
            } else if(host.contains("tokopedia.link")){
                onScanBranchIOLink(barcodeData);
            }else if(host.contains("tokopedia")){
                openActivity(barcodeData);
            }
        } else {
            getView().showErrorGetInfo(context.getString(R.string.msg_dialog_wrong_scan));
        }
    }

    private void onScanBranchIOLink(String qrCode) {
        getView().showProgressDialog();
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("url", qrCode);
        requestParams.putString("branch_key","key_live_abhHgIh1DQiuPxdBNg9EXepdDugwwkHr");
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
                    CampaignTracking.eventScanQRCode("fail",context.getString(R.string.msg_dialog_wrong_scan),"");
                } else {
                    getView().showErrorNetwork(e);
                    CampaignTracking.eventScanQRCode("fail",context.getString(R.string.msg_dialog_wrong_scan),"");
                }
            }

            @Override
            public void onNext(BranchIOAndroidDeepLink branchIOAndroidDeepLink) {
                openActivity(Constants.Schemes.APPLINKS + "://" + branchIOAndroidDeepLink.getAndroidDeeplinkPath());
                CampaignTracking.eventScanQRCode("success","",branchIOAndroidDeepLink.getAndroidDeeplinkPath());
            }
        });
    }

    private void onScanCompleteGetInfoQrPayment(String qrcode) {
        if (isUserLogin()) {
            getAbTagsForContinuingPayment(qrcode);
        } else {
            localCacheHandler.putString(GetInfoQrTokoCashUseCase.IDENTIFIER, qrcode);
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
                getView().finish();
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof CampaignException) {
                    getView().showErrorGetInfo(context.getString(R.string.msg_dialog_wrong_scan));
                } else {
                    getView().showErrorNetwork(e);
                }
                CampaignTracking.eventScanQRCode("fail",idCampaign,"");
            }

            @Override
            public void onNext(CampaignResponseEntity s) {
                openActivity(s.getUrl());
                CampaignTracking.eventScanQRCode("success",idCampaign,s.getUrl());
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
            getAbTagsForContinuingPayment(localCacheHandler.getString(GetInfoQrTokoCashUseCase.IDENTIFIER));
        } else {
            localCacheHandler.putString(GetInfoQrTokoCashUseCase.IDENTIFIER, "");
            localCacheHandler.applyEditor();
        }
    }

    private boolean isUserLogin() {
        return userSession.isLoggedIn();
    }

    private void getAbTagsForContinuingPayment(final String qrCode) {
        getView().showProgressDialog();
        getBalanceTokoCashUseCase.execute(RequestParams.EMPTY, new Subscriber<BalanceTokoCash>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideProgressDialog();
                getView().showErrorNetwork(e);
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
        });
    }

    private boolean isContinuingPayment(List<String> abTags) {
        for (String abTag : abTags) {
            if (abTag.equals(TAG_QR_PAYMENT)) {
                return true;
            }
        }
        return false;
    }

    private void getInfoQrWallet(final String qrcode) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetInfoQrTokoCashUseCase.IDENTIFIER, qrcode);
        getInfoQrTokoCashUseCase.execute(requestParams, new Subscriber<InfoQrTokoCash>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideProgressDialog();
                if (e instanceof WalletException) {
                    getView().showErrorGetInfo(e.getMessage());
                } else {
                    getView().showErrorNetwork(e);
                }
            }

            @Override
            public void onNext(InfoQrTokoCash infoQrTokoCash) {
                getView().hideProgressDialog();
                if (infoQrTokoCash != null) {
                    Intent intent = NominalQrPaymentActivity.newInstance(context, qrcode, infoQrTokoCash);
                    getView().startActivityForResult(intent, getView().getRequestCodeForQrPayment());
                } else {
                    getView().showErrorGetInfo(context.getString(R.string.msg_dialog_wrong_scan));
                }
            }
        });
    }

    @Override
    public void destroyView() {
        if (getBalanceTokoCashUseCase != null) getBalanceTokoCashUseCase.unsubscribe();
        if (getInfoQrTokoCashUseCase != null) getInfoQrTokoCashUseCase.unsubscribe();
        if (postBarCodeDataUseCase != null) postBarCodeDataUseCase.unsubscribe();
    }
}
