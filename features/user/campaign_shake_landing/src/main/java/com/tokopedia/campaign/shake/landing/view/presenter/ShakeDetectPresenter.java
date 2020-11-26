package com.tokopedia.campaign.shake.landing.view.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;

import com.google.android.gms.location.LocationServices;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.campaign.shake.landing.R;
import com.tokopedia.campaign.shake.landing.analytics.CampaignTracking;
import com.tokopedia.campaign.shake.landing.data.entity.CampaignGqlResponse;
import com.tokopedia.campaign.shake.landing.data.entity.CampaignResponseEntity;
import com.tokopedia.campaign.shake.landing.data.entity.ValidCampaignPojo;
import com.tokopedia.campaign.shake.landing.data.model.CampaignException;
import com.tokopedia.campaign.shake.landing.domain.GetCampaignUseCase;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ServerErrorException;
import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.locationmanager.DeviceLocation;
import com.tokopedia.locationmanager.LocationDetectorHelper;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.shakedetect.ShakeDetectManager;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

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
    private UserSessionInterface userSession;

    @Inject
    public ShakeDetectPresenter(GetCampaignUseCase getCampaignUseCase,
                                @ApplicationContext Context context) {
        this.getCampaignUseCase = getCampaignUseCase;
        this.context = context;

        initUserSession();
        initRemoteConfig();
    }

    private void initUserSession() {
        userSession = new UserSession(context);
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
                        CampaignTracking.eventShakeShake("fail", ShakeDetectManager.sTopActivity, "", "");

                        if (e instanceof UnknownHostException || e instanceof ConnectException) {
                            getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
                        } else if (e instanceof SocketTimeoutException) {
                            getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                        } else if (e instanceof CampaignException) {
                            if (((CampaignException) e).isMissingAuthorizationCredentials()) {
                                redirectToLoginPage();
                            } else {
                                getView().showErrorGetInfo();
                                return;
                            }

                        } else if (e instanceof ResponseDataNullException) {
                            getView().showErrorNetwork(e.getMessage());
                        } else if (e instanceof HttpErrorException) {
                            getView().showErrorNetwork(e.getMessage());
                        } else if (e instanceof ServerErrorException) {
                            getView().showErrorNetwork(ErrorHandler.getErrorMessage(context, e));
                        } else {
                            getView().showErrorGetInfo();
                            return;
                        }

                        getView().finish();
                    }

                    @Override
                    public void onNext(GraphqlResponse graphqlResponse) {

                        if (graphqlResponse.getError(CampaignGqlResponse.class) != null
                                && graphqlResponse.getError(CampaignGqlResponse.class).size() > 0) {
                            CampaignTracking.eventShakeShake("fail", ShakeDetectManager.sTopActivity, "", "");
                            getView().showMessage(graphqlResponse.getError(CampaignGqlResponse.class).get(0).getMessage() != null ?
                                    graphqlResponse.getError(CampaignGqlResponse.class).get(0).getMessage() : "");
                            return;
                        }

                        CampaignGqlResponse response =
                                graphqlResponse.getData(CampaignGqlResponse.class);

                        if (response != null && response.getCampaignResponseEntity() != null) {
                            CampaignResponseEntity s = response.getCampaignResponseEntity();
                            if (s.getValidCampaignPojos().size() > 0) {
                                ValidCampaignPojo campaign = s.getValidCampaignPojos().get(0);

                                if (!campaign.isValid()) {
                                    CampaignTracking.eventShakeShake("shake shake disable", ShakeDetectManager.sTopActivity, "", "");
                                    return;
                                }

                                if ((campaign.getErrorMessage()) != null
                                        && !campaign.getErrorMessage().isEmpty()
                                        && campaign.getUrl() != null && campaign.getUrl().isEmpty()) {
                                    CampaignTracking.eventShakeShake("fail", ShakeDetectManager.sTopActivity, "", "");
                                    getView().showMessage(campaign.getErrorMessage());
                                    return;
                                }

                                Intent intentFromRouter = RouteManager.getIntentNoFallback(context, campaign.getUrl());
                                if(intentFromRouter == null){
                                    getView().showErrorNetwork(context.getString(R.string.shake_landing_shake_shake_wrong_deeplink));
                                    CampaignTracking.eventShakeShake("fail", ShakeDetectManager.sTopActivity, "", "");
                                    return;
                                }

                                // Vibrate for 500 milliseconds
                                if (campaign.getVibrate() == 1)
                                    vibrate();

                                redirectToCampaignUrl(campaign.getUrl());

                                CampaignTracking.eventShakeShake("success",
                                        ShakeDetectManager.sTopActivity, "", campaign.getUrl());

                                //Open next activity based upon the result from server

                            }
                        } else {
                            CampaignTracking.eventShakeShake("fail", ShakeDetectManager.sTopActivity, "", "");
                            getView().showErrorGetInfo();
                        }
                    }
                });

    }

    private void redirectToCampaignUrl(String url) {

        if (TextUtils.isEmpty(url)) {
            return;
        }

        final Intent intent = RouteManager.getIntent(context, url);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        }, 500);
    }

    private void redirectToLoginPage() {
        final Intent intent = RouteManager.getIntent(context, ApplinkConst.LOGIN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }, 500);
    }

    @Override
    public void setPermissionChecker(PermissionCheckerHelper permissionCheckerHelper) {
        this.permissionCheckerHelper = permissionCheckerHelper;
    }

    private void addLocationParameterBeforeRequest(Activity activity) {

        LocationDetectorHelper locationDetectorHelper = new LocationDetectorHelper(
                permissionCheckerHelper,
                LocationServices.getFusedLocationProviderClient(activity
                        .getApplicationContext()),
                activity.getApplicationContext());
        locationDetectorHelper.getLocation(onGetLocation(), activity,
                LocationDetectorHelper.TYPE_DEFAULT_FROM_CLOUD,
                activity.getString(R.string.shake_landing_rationale_need_location_for_promotion));

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
    public void onDisableShakeShake() {
        //disable the shake shake
        ShakeDetectManager.getShakeDetectManager().disableShakeShake();
        if (userSession.isLoggedIn()) {
            getView().goToGeneralSetting();
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
