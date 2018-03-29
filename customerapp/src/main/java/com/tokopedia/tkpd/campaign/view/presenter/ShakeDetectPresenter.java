package com.tokopedia.tkpd.campaign.view.presenter;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ServerErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.campaign.analytics.CampaignAppEventTracking;
import com.tokopedia.tkpd.campaign.analytics.CampaignTracking;
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;
import com.tokopedia.tkpd.campaign.data.model.CampaignException;
import com.tokopedia.tkpd.campaign.domain.shake.ShakeUseCase;
import com.tokopedia.tkpd.campaign.view.ShakeDetectManager;
import com.tokopedia.tkpd.campaign.view.activity.ShakeDetectCampaignActivity;
import com.tokopedia.tokocash.historytokocash.presentation.ServerErrorHandlerUtil;
import com.tokopedia.usecase.RequestParams;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.tokopedia.tkpd.campaign.domain.barcode.PostBarCodeDataUseCase.CAMPAIGN_ID;
import static com.tokopedia.tkpd.campaign.domain.shake.ShakeUseCase.IS_AUDIO;
import static com.tokopedia.tkpd.campaign.domain.shake.ShakeUseCase.SCREEN_NAME;

/**
 * Created by sandeepgoyal on 14/02/18.
 */

public class ShakeDetectPresenter extends BaseDaggerPresenter<ShakeDetectContract.View> implements ShakeDetectContract.Presenter {


    private final ShakeUseCase shakeUseCase;
    protected final Context context;

    @Inject
    public ShakeDetectPresenter(ShakeUseCase shakeDetectUseCase, @ApplicationContext Context context) {
        this.shakeUseCase = shakeDetectUseCase;
        this.context = context;
    }

    @Override
    public void onShakeDetect() {

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(IS_AUDIO, "false");
        requestParams.putString(SCREEN_NAME,ShakeDetectManager.sTopActivity);
        shakeUseCase.execute(requestParams, new Subscriber<CampaignResponseEntity>() {
            @Override
            public void onCompleted() {
                getView().finish();
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
                } else if (e instanceof SocketTimeoutException) {
                    getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else if (e instanceof CampaignException) {
                    getView().showErrorGetInfo(context.getString(R.string.msg_dialog_wrong_scan));
                } else if (e instanceof ResponseDataNullException) {
                    getView().showErrorNetwork(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    getView().showErrorNetwork(e.getMessage());
                } else if (e instanceof ServerErrorException) {
                    ServerErrorHandlerUtil.handleError(e);
                } else {
                    getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
                Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                Intent intent = new Intent(ShakeDetectManager.ACTION_SHAKE_SHAKE_SYNCED);

                intent.putExtra("isSuccess",false);

                CampaignTracking.eventShakeShake("fail",ShakeDetectManager.sTopActivity,"","");
                getView().sendBroadcast(intent);
                getView().finish();
            }

            @Override
            public void onNext(final CampaignResponseEntity s) {

                Intent intent = new Intent(ShakeDetectManager.ACTION_SHAKE_SHAKE_SYNCED);
                intent.putExtra("isSuccess",true);
                intent.putExtra("data",s.getUrl());
                Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                if(s.getVibrate() == 1)
                    v.vibrate(500);
                getView().sendBroadcast(intent);
                getView().showMessage(context.getString(R.string.shake_shake_success));
                CampaignTracking.eventShakeShake("success",ShakeDetectManager.sTopActivity,"",s.getUrl());

                //Open next activity based upon the result from server
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (shakeUseCase != null) shakeUseCase.unsubscribe();
    }

    @Override
    public void onRetryClick() {

        getView().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getView().finish();
    }
}
