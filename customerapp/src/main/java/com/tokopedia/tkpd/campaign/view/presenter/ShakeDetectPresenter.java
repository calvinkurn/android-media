package com.tokopedia.tkpd.campaign.view.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ServerErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.campaign.analytics.CampaignTracking;
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;
import com.tokopedia.tkpd.campaign.data.model.CampaignException;
import com.tokopedia.tkpd.campaign.domain.shake.ShakeUseCase;
import com.tokopedia.tkpd.campaign.view.ShakeDetectManager;
import com.tokopedia.tkpd.deeplink.DeepLinkDelegate;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tokocash.historytokocash.presentation.ServerErrorHandlerUtil;
import com.tokopedia.usecase.RequestParams;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.tokopedia.tkpd.campaign.domain.shake.ShakeUseCase.IS_AUDIO;
import static com.tokopedia.tkpd.campaign.domain.shake.ShakeUseCase.SCREEN_NAME;

/**
 * Created by sandeepgoyal on 14/02/18.
 */

public class ShakeDetectPresenter extends BaseDaggerPresenter<ShakeDetectContract.View> implements ShakeDetectContract.Presenter {


    private final ShakeUseCase shakeUseCase;
    protected final Context context;
    protected boolean isFirstShake;
    private RemoteConfig remoteConfig;
    public static final String FIREBASE_DOUBLE_SHAKE_CONFIG_KEY = "app_double_shake_enabled";



    @Inject
    public ShakeDetectPresenter(ShakeUseCase shakeDetectUseCase, @ApplicationContext Context context) {
        this.shakeUseCase = shakeDetectUseCase;
        this.context = context;
        initRemoteConfig();
    }

    private void initRemoteConfig() {
        remoteConfig = new FirebaseRemoteConfigImpl(context);
    }

    private boolean isDoubleShakeShakeEnable() {
        return remoteConfig.getBoolean(FIREBASE_DOUBLE_SHAKE_CONFIG_KEY,true);

    }


    @Override
    public void onShakeDetect() {
        if (!isFirstShake && isDoubleShakeShakeEnable() && !getView().isLongShakeTriggered()) {
            isFirstShake = true;
            waitForSecondShake();
        } else {
            isFirstShake = false;
            RequestParams requestParams = RequestParams.create();
            requestParams.putString(IS_AUDIO, "false");
            if (ShakeDetectManager.sTopActivity != null) {
                requestParams.putString(SCREEN_NAME, ShakeDetectManager.sTopActivity.trim().replaceAll(" ", "_"));
            }
            shakeUseCase.execute(requestParams, new Subscriber<CampaignResponseEntity>() {
                @Override
                public void onCompleted() {
                    getView().finish();
                }

                @Override
                public void onError(Throwable e) {
                    Intent intent = new Intent(ShakeDetectManager.ACTION_SHAKE_SHAKE_SYNCED);

                    intent.putExtra("isSuccess", false);

                    if (e instanceof UnknownHostException || e instanceof ConnectException) {
                        getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
                    } else if (e instanceof SocketTimeoutException) {
                        getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                    } else if (e instanceof CampaignException) {
                        if (((CampaignException) e).isMissingAuthorizationCredentials()) {
                            intent.putExtra("needLogin", true);
                        } else {
                            getView().showErrorGetInfo(e.getMessage());
                            return;
                        }

                    } else if (e instanceof ResponseDataNullException) {
                        getView().showErrorNetwork(e.getMessage());
                    } else if (e instanceof HttpErrorException) {
                        getView().showErrorNetwork(e.getMessage());
                    } else if (e instanceof ServerErrorException) {
                        ServerErrorHandlerUtil.handleError(e);
                    } else {
                        getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                    }


                    CampaignTracking.eventShakeShake("fail", ShakeDetectManager.sTopActivity, "", "");
                    getView().sendBroadcast(intent);
                    getView().finish();

                }

                @Override
                public void onNext(final CampaignResponseEntity s) {
                    DeepLinkDelegate deepLinkDelegate = DeeplinkHandlerActivity.getDelegateInstance();
                    if (!deepLinkDelegate.supportsUri(s.getUrl())) {
                        getView().showErrorNetwork(context.getString(R.string.shake_shake_wrong_deeplink));
                        return;
                    }
                    Intent intent = new Intent(ShakeDetectManager.ACTION_SHAKE_SHAKE_SYNCED);
                    intent.putExtra("isSuccess", true);
                    intent.putExtra("data", s.getUrl());
                    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    if (s.getVibrate() == 1)
                        v.vibrate(500);
                    getView().sendBroadcast(intent);
                    getView().showMessage(context.getString(R.string.shake_shake_success));
                    CampaignTracking.eventShakeShake("success", ShakeDetectManager.sTopActivity, "", s.getUrl());

                    //Open next activity based upon the result from server
                }
            });
        }
    }
    public final static int SHAKE_SHAKE_WAIT_TIME_SEC = 3;
    Subscription subscription = null;
    private void waitForSecondShake() {
        subscription = Observable.interval(0,1,  TimeUnit.SECONDS).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("shakeshake",e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(Long l) {
                if(l==SHAKE_SHAKE_WAIT_TIME_SEC) {
                    finishShake();
                    return;
                }
                getView().updateTimer(SHAKE_SHAKE_WAIT_TIME_SEC -l);



            }
        });
    }
    void finishShake() {
        subscription.unsubscribe();
        isFirstShake = false;
        shakeUseCase.unsubscribe();
        getView().finish();
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

    @Override
    public void onCancelClick() {
        finishShake();
    }
}
