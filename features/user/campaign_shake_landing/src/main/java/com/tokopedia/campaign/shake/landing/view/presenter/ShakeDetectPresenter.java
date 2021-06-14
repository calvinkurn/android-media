package com.tokopedia.campaign.shake.landing.view.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

import com.google.android.gms.location.LocationServices;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.campaign.shake.landing.R;
import com.tokopedia.campaign.shake.landing.domain.GetCampaignUseCase;
import com.tokopedia.campaign.shake.landing.view.subscriber.SecondShakeSubscriber;
import com.tokopedia.campaign.shake.landing.view.subscriber.ShakeCampaignSubscriber;
import com.tokopedia.locationmanager.DeviceLocation;
import com.tokopedia.locationmanager.LocationDetectorHelper;
import com.tokopedia.utils.permission.PermissionCheckerHelper;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.shakedetect.ShakeDetectManager;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.TestOnly;

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
    public Subscription subscription = null;
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

    public volatile boolean secondShakeHappen = false;

    @Override
    public void onShakeDetect() {
        if (getView().isLongShakeTriggered()) {
            onShakeDetectLongShakeTriggered();
            return;
        } else if (!isFirstShake && isDoubleShakeShakeEnable()) {
            onShakeDetectDoubleShakeShake();
        } else {
            onShakeDetectNormalShake();
        }
    }

    private void onShakeDetectLongShakeTriggered() {
        getView().setInvisibleCounter();
        getView().showDisableShakeShakeVisible();
        vibrate();
    }

    private void onShakeDetectDoubleShakeShake() {
        isFirstShake = true;
        waitForSecondShake();
        vibrate();
    }

    private void onShakeDetectNormalShake() {
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

    private void getCampaign(Double latitude, Double longitude) {
        String screenName = ShakeDetectManager.sTopActivity.trim().replaceAll(" ", "_");
        getCampaignUseCase.execute(GetCampaignUseCase.generateParam(
                screenName,
                latitude,
                longitude,
                false),
                new ShakeCampaignSubscriber(getView(), context));

    }

    @Override
    public void setPermissionChecker(PermissionCheckerHelper permissionCheckerHelper) {
        this.permissionCheckerHelper = permissionCheckerHelper;
    }

    public void addLocationParameterBeforeRequest(Activity activity) {

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
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SecondShakeSubscriber(this));
    }

    public void finishShake() {
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
        if (isLogin()) {
            getView().goToGeneralSetting();
        } else {
            getView().makeInvisibleShakeShakeDisableView();
            getView().setSnackBarErrorMessage();
        }

    }

    public boolean isLogin(){
        return userSession.isLoggedIn();
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

    @TestOnly
    public boolean getFirstShake() {
        return isFirstShake;
    }

    @TestOnly
    public void onShakeDetectTest(boolean isLongShake, boolean isFirstShake, boolean isDoubleShake) {
        if (isLongShake) {
            onShakeDetectLongShakeTriggered();
            return;
        } else if (!isFirstShake && isDoubleShake) {
            onShakeDetectDoubleShakeShake();
        } else {
            onShakeDetectNormalShake();
        }
    }
}
