package com.tokopedia.tkpd.qrpayment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.core.network.exception.ServerErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;
import com.tokopedia.tkpd.campaign.domain.barcode.PostBarCodeDataUseCase;
import com.tokopedia.tokocash.historytokocash.presentation.ServerErrorHandlerUtil;
import com.tokopedia.tokocash.qrpayment.domain.GetInfoQrTokoCashUseCase;
import com.tokopedia.tokocash.qrpayment.presentation.activity.NominalQrPaymentActivity;
import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;
import com.tokopedia.usecase.RequestParams;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.tkpd.campaign.domain.barcode.PostBarCodeDataUseCase.CAMPAIGN_ID;


/**
 * Created by sandeepgoyal on 18/12/17.
 */

public class QrScannerPresenter extends BaseDaggerPresenter<QrScannerContract.View>
        implements QrScannerContract.Presenter {

    private PostBarCodeDataUseCase postBarCodeDataUseCase;
    private GetInfoQrTokoCashUseCase getInfoQrTokoCashUseCase;
    private Context context;
    private UserSession userSession;

    @Inject
    public QrScannerPresenter(PostBarCodeDataUseCase postBarCodeDataUseCase,
                              GetInfoQrTokoCashUseCase getInfoQrTokoCashUseCase,
                              @ApplicationContext Context context, UserSession userSession) {
        this.postBarCodeDataUseCase = postBarCodeDataUseCase;
        this.getInfoQrTokoCashUseCase = getInfoQrTokoCashUseCase;
        this.context = context;
        this.userSession = userSession;
    }

    @Override
    public void onBarCodeScanComplete(String barcodeData) {
        Uri uri = Uri.parse(barcodeData);
        if (uri.getHost().equals(QrScannerTypeDef.PAYMENT_QR_CODE)) {
            getInfoQrWallet(uri.getPathSegments().get(0));
        } else if (uri.getHost().equals(QrScannerTypeDef.CAMPAIGN_QR_CODE)) {
            getInfoQrCampaign(uri.getPathSegments().get(0));
        } else {
            getView().showErrorGetInfo(context.getString(com.tokopedia.tokocash.R.string.msg_dialog_wrong_scan));
        }
    }

    @Override
    public boolean isUserLogin() {
        return userSession.isLoggedIn();
    }

    private void getInfoQrWallet(final String qrcode) {
        getView().showProgressDialog();
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetInfoQrTokoCashUseCase.IDENTIFIER, qrcode);
        if (isUserLogin()) {
            getInfoQrTokoCashUseCase.execute(requestParams, new Subscriber<InfoQrTokoCash>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    getView().hideProgressDialog();
                    if (e instanceof UnknownHostException || e instanceof ConnectException) {
                        getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
                    } else if (e instanceof SocketTimeoutException) {
                        getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                    } else if (e instanceof ResponseErrorException) {
                        getView().showErrorGetInfo(context.getString(com.tokopedia.tokocash.R.string.msg_dialog_wrong_scan));
                    } else if (e instanceof ResponseDataNullException) {
                        getView().showErrorNetwork(e.getMessage());
                    } else if (e instanceof HttpErrorException) {
                        getView().showErrorNetwork(e.getMessage());
                    } else if (e instanceof ServerErrorException) {
                        ServerErrorHandlerUtil.handleError(e);
                    } else {
                        getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                    }
                }

                @Override
                public void onNext(InfoQrTokoCash infoQrTokoCash) {
                    getView().hideProgressDialog();
                    if (infoQrTokoCash != null) {
                        Intent intent = NominalQrPaymentActivity.newInstance(context, qrcode, infoQrTokoCash);
                        getView().startActivityForResult(intent, getView().getResultCodeForQrPayment());
                    } else {
                        getView().showErrorGetInfo(context.getString(com.tokopedia.tokocash.R.string.msg_dialog_wrong_scan));
                    }
                }
            });
        } else {
            LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, GetInfoQrTokoCashUseCase.IDENTIFIER);
            localCacheHandler.putString(GetInfoQrTokoCashUseCase.IDENTIFIER, qrcode);
            getView().interruptToLoginPage();
        }
    }

    private void getInfoQrCampaign(String idCampaign) {
        getView().showProgressDialog();
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(CAMPAIGN_ID, idCampaign);
        postBarCodeDataUseCase.execute(requestParams, new Subscriber<CampaignResponseEntity>() {
            @Override
            public void onCompleted() {
                Log.e("toko_barcode", "onCompleted ");
                getView().finish();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("toko_barcode", "onError ");
                getView().showErrorNetwork(e.getMessage());
            }

            @Override
            public void onNext(CampaignResponseEntity s) {
                Log.e("toko_barcode", "onNext " + s);
                Uri uri = Uri.parse("" + s.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                getView().startActivity(intent);
                //Open next activity based upon the result from server
            }
        });
    }

    @Override
    public void destroyView() {
        if (getInfoQrTokoCashUseCase != null) getInfoQrTokoCashUseCase.unsubscribe();
        if (postBarCodeDataUseCase != null) postBarCodeDataUseCase.unsubscribe();
    }
}
