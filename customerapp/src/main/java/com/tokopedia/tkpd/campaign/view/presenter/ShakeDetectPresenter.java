package com.tokopedia.tkpd.campaign.view.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

import com.google.android.gms.location.LocationServices;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.applink.ApplinkDelegate;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ServerErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.home.account.presentation.activity.GeneralSettingActivity;
import com.tokopedia.locationmanager.DeviceLocation;
import com.tokopedia.locationmanager.LocationDetectorHelper;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.campaign.analytics.CampaignTracking;
import com.tokopedia.tkpd.campaign.data.entity.CampaignGqlResponse;
import com.tokopedia.tkpd.campaign.data.entity.CampaignResponseEntity;
import com.tokopedia.tkpd.campaign.data.model.CampaignException;
import com.tokopedia.tkpd.campaign.domain.shake.GetCampaignUseCase;
import com.tokopedia.tkpd.campaign.domain.shake.ShakeUseCase;
import com.tokopedia.tkpd.campaign.view.ShakeDetectManager;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.usecase.RequestParams;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sandeepgoyal on 14/02/18.
 */

public class ShakeDetectPresenter extends BaseDaggerPresenter<ShakeDetectContract.View> implements ShakeDetectContract.Presenter {


    protected final Context context;
    private final GetCampaignUseCase getCampaignUseCase;
    protected boolean isFirstShake;
    private RemoteConfig remoteConfig;
    public static final String FIREBASE_DOUBLE_SHAKE_CONFIG_KEY = "app_double_shake_enabled";
    public static final String SHAKE_SHAKE_ERROR = "Oops! Kejutannya masih dibungkus. Yuk, shake lagi handphone-mu";

    public final static int SHAKE_SHAKE_WAIT_TIME_SEC = 5;
    Subscription subscription = null;
    private PermissionCheckerHelper permissionCheckerHelper;

    @Inject
    public ShakeDetectPresenter(ShakeUseCase shakeUseCase,
                                GetCampaignUseCase getCampaignUseCase,
                                @ApplicationContext Context context) {
        this.getCampaignUseCase = getCampaignUseCase;
        this.context = context;
        initRemoteConfig();
    }

    private void initRemoteConfig() {
        remoteConfig = new FirebaseRemoteConfigImpl(context);
    }

    private boolean isDoubleShakeShakeEnable() {
        return remoteConfig.getBoolean(FIREBASE_DOUBLE_SHAKE_CONFIG_KEY, true);

    }

    volatile boolean secondShakeHappen = false;

    @Override
    public void onShakeDetect() {
        if (getView().isLongShakeTriggered()) {
            getView().setInvisibleCounter();
            getView().showDisableShakeShakeVisible();
            vibrate();
            return;
        } else if (!isFirstShake && isDoubleShakeShakeEnable()) {
            isFirstShake = true;
            waitForSecondShake();
            vibrate();

        } else {
            if (getCampaignUseCase != null)
                getCampaignUseCase.unsubscribe();
            if (isFirstShake) {
                isFirstShake = false;
                secondShakeHappen = true;
            }
            getView().setInvisibleCounter();
            getView().setCancelButtonVisible();
            addLocationParameterBeforeRequest(getView().getCurrentActivity());
        }
    }

    private void getCampaign(Double latitude, Double longitude) {
        String screenName = ShakeDetectManager.sTopActivity.trim().replaceAll(" ", "_");
        getCampaignUseCase.execute(GetCampaignUseCase.generateParam(
                screenName,
                latitude,
                longitude,
                false),
                new Subscriber<GraphqlResponse>() {
                    @Override
                    public void onCompleted() {
                        getView().finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Intent intent = new Intent(ShakeDetectManager.ACTION_SHAKE_SHAKE_SYNCED);
                        CampaignTracking.eventShakeShake("fail", ShakeDetectManager.sTopActivity, "", "");

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
                            getView().showErrorNetwork(ErrorHandler.getErrorMessage(context, e));
                        } else {
                            getView().showErrorGetInfo(SHAKE_SHAKE_ERROR);
                            return;
                        }

                        getView().sendBroadcast(intent);
                        getView().finish();
                    }

                    @Override
                    public void onNext(GraphqlResponse graphqlResponse) {
                        CampaignGqlResponse response =
                                graphqlResponse.getData(CampaignGqlResponse.class);

                        CampaignResponseEntity s = response.getCampaignResponseEntity();

                        if (!s.isEnable()) {
                            CampaignTracking.eventShakeShake("shake shake disable", ShakeDetectManager.sTopActivity, "", "");
                            return;
                        }
                        if ((s.getMessage()) != null && !s.getMessage().isEmpty() &&
                                s.getUrl() != null && s.getUrl().isEmpty()) {
                            CampaignTracking.eventShakeShake("fail", ShakeDetectManager.sTopActivity, "", "");
                            getView().showMessage(s.getMessage());
                            return;
                        }
                        ApplinkDelegate deepLinkDelegate = DeeplinkHandlerActivity.getApplinkDelegateInstance();
                        if (!deepLinkDelegate.supportsUri(s.getUrl())) {
                            getView().showErrorNetwork(context.getString(R.string.shake_shake_wrong_deeplink));
                            CampaignTracking.eventShakeShake("fail", ShakeDetectManager.sTopActivity, "", "");
                            return;
                        }
                        Intent intent = new Intent(ShakeDetectManager.ACTION_SHAKE_SHAKE_SYNCED);
                        intent.putExtra("isSuccess", true);
                        intent.putExtra("data", s.getUrl());

                        // Vibrate for 500 milliseconds
                        if (s.getVibrate() == 1)
                            vibrate();
                        getView().sendBroadcast(intent);
                        CampaignTracking.eventShakeShake("success", ShakeDetectManager.sTopActivity, "", s.getUrl());

                        //Open next activity based upon the result from server

                    }
                });

//        shakeUseCase.execute(requestParams, new Subscriber<CampaignResponseEntity>() {
//            @Override
//            public void onCompleted() {
//                getView().finish();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Intent intent = new Intent(ShakeDetectManager.ACTION_SHAKE_SHAKE_SYNCED);
//                CampaignTracking.eventShakeShake("fail", ShakeDetectManager.sTopActivity, "", "");
//
//                intent.putExtra("isSuccess", false);
//
//                if (e instanceof UnknownHostException || e instanceof ConnectException) {
//                    getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
//                } else if (e instanceof SocketTimeoutException) {
//                    getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
//                } else if (e instanceof CampaignException) {
//                    if (((CampaignException) e).isMissingAuthorizationCredentials()) {
//                        intent.putExtra("needLogin", true);
//                    } else {
//                        getView().showErrorGetInfo(e.getMessage());
//                        return;
//                    }
//
//                } else if (e instanceof ResponseDataNullException) {
//                    getView().showErrorNetwork(e.getMessage());
//                } else if (e instanceof HttpErrorException) {
//                    getView().showErrorNetwork(e.getMessage());
//                } else if (e instanceof ServerErrorException) {
//                    getView().showErrorNetwork(ErrorHandler.getErrorMessage(context, e));
//                } else {
//                    getView().showErrorGetInfo(SHAKE_SHAKE_ERROR);
//                    return;
//                }
//
//
//                getView().sendBroadcast(intent);
//                getView().finish();
//
//            }
//
//            @Override
//            public void onNext(final CampaignResponseEntity s) {
//                if (!s.isEnable()) {
//                    CampaignTracking.eventShakeShake("shake shake disable", ShakeDetectManager.sTopActivity, "", "");
//                    return;
//                }
//                if ((s.getMessage()) != null && !s.getMessage().isEmpty() &&
//                        s.getUrl() != null && s.getUrl().isEmpty()) {
//                    CampaignTracking.eventShakeShake("fail", ShakeDetectManager.sTopActivity, "", "");
//                    getView().showMessage(s.getMessage());
//                    return;
//                }
//                ApplinkDelegate deepLinkDelegate = DeeplinkHandlerActivity.getApplinkDelegateInstance();
//                if (!deepLinkDelegate.supportsUri(s.getUrl())) {
//                        getView().showErrorNetwork(context.getString(R.string.shake_shake_wrong_deeplink));
//                    CampaignTracking.eventShakeShake("fail", ShakeDetectManager.sTopActivity, "", "");
//                    return;
//                }
//                Intent intent = new Intent(ShakeDetectManager.ACTION_SHAKE_SHAKE_SYNCED);
//                intent.putExtra("isSuccess", true);
//                intent.putExtra("data", s.getUrl());
//
//                // Vibrate for 500 milliseconds
//                if (s.getVibrate() == 1)
//                    vibrate();
//                getView().sendBroadcast(intent);
//                CampaignTracking.eventShakeShake("success", ShakeDetectManager.sTopActivity, "", s.getUrl());
//
//                //Open next activity based upon the result from server
//            }
//        });

    }

    @Override
    public void setPermissionChecker(PermissionCheckerHelper permissionCheckerHelper) {
        this.permissionCheckerHelper = permissionCheckerHelper;
    }

    private void addLocationParameterBeforeRequest(Activity activity) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            LocationDetectorHelper locationDetectorHelper = new LocationDetectorHelper(
                    permissionCheckerHelper,
                    LocationServices.getFusedLocationProviderClient(activity
                            .getApplicationContext()),
                    activity.getApplicationContext());
            locationDetectorHelper.getLocation(onGetLocation(), activity,
                    LocationDetectorHelper.TYPE_DEFAULT_FROM_CLOUD,
                    activity.getString(R.string.rationale_need_location_for_promotion));
        } else {
            getCampaign(0.0, 0.0);
        }

    }

    private Function1<DeviceLocation, Unit> onGetLocation() {
        return (deviceLocation) -> {
            getCampaign(deviceLocation.getLatitude(), deviceLocation.getLongitude());
            return null;
        };
    }


    private void waitForSecondShake() {
        subscription = Observable.interval(0, 1, TimeUnit.SECONDS).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Long l) {
                        if (l == SHAKE_SHAKE_WAIT_TIME_SEC && !secondShakeHappen) {
                            finishShake();
                            return;
                        }


                    }
                });
    }

    void finishShake() {
        if (subscription != null)
            subscription.unsubscribe();
        getView().setInvisibleCounter();
        isFirstShake = false;
        if (getCampaignUseCase != null)
            getCampaignUseCase.unsubscribe();
        getView().finish();
    }

    @Override
    public void onDestroyView() {
        if (getCampaignUseCase != null) getCampaignUseCase.unsubscribe();
    }

    @Override
    public void onRetryClick() {
        getView().finish();
    }

    @Override
    public void onDisableShakeShake() {
        //disable the shake shake
        ShakeDetectManager.getShakeDetectManager().disableShakeShake();
        if (SessionHandler.isV4Login(getView().getCurrentActivity())) {
            getView().getCurrentActivity().startActivity(GeneralSettingActivity.createIntent(getView().getCurrentActivity()));
            getView().finish();
        } else {
            getView().makeInvisibleShakeShakeDisableView();
            getView().setSnackBarErrorMessage();
        }

    }

    public void vibrate() {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
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
