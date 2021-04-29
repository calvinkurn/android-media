package com.tokopedia.core.analytics.container;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.model.CustomerWrapper;
import com.tokopedia.keys.Keys;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.moengage_wrapper.MoengageInteractor;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.track.interfaces.ContextAnalytics;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class MoengageAnalytics extends ContextAnalytics {
    private final MoengageInteractor moengageInteractor;

    public MoengageAnalytics(Context context) {
        super(context);
        //pass data to moengageWrapper
        moengageInteractor = MoengageInteractor.INSTANCE;
        moengageInteractor.initInteractor(getContext(), Keys.getMoengageKey(getContext()),
                R.drawable.ic_status_bar_notif_customerapp, R.drawable.ic_big_notif_customerapp);
    }

    @Override
    public void initialize() {
        super.initialize();

        /*
          Mandatory to set small/Large notification icon while initialising sdk
          */
        boolean isInitialized = moengageInteractor.initialiseMoengage();
        if (isInitialized)
            executeInstallTrackingAsync();
    }

    private void executeInstallTrackingAsync() {
        try {
            //added the try catch for this issue https://github.com/Kotlin/kotlinx.coroutines/issues/490
            WeaveInterface installTrackingWeave = new WeaveInterface() {
                @NotNull
                @Override
                public Object execute() {
                    return sendExistingUserAndInstallTrackingEvent();
                }
            };
            Weaver.Companion.executeWeaveCoRoutineWithFirebase(installTrackingWeave, RemoteConfigKey.ENABLE_ASYNC_INSTALLTRACK, context);
        } catch(Exception ex){
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "error");
            messageMap.put("name", ex.getMessage());
            ServerLogger.log(Priority.P2, "INIT_MOENGAGE", messageMap);
        }
    }

    @Override
    public void setMoEUserAttributesLogin(String userId, String name, String email, String phoneNumber, boolean isGoldMerchant, String shopName, String shopId, boolean hasShop, String loginMethod) {
        moengageInteractor.setUserDataLogin(userId, name, email, phoneNumber, isGoldMerchant, shopName, shopId);

        Map<String, Object> loginValue = new HashMap<>();
        loginValue.put(AppEventTracking.MOENGAGE.USER_ID, userId);
        loginValue.put(AppEventTracking.MOENGAGE.MEDIUM, loginMethod);
        loginValue.put(AppEventTracking.MOENGAGE.EMAIL, email);
        if (!TextUtils.isEmpty(phoneNumber)) {
            loginValue.put(AppEventTracking.MOENGAGE.MOBILE_NUM, phoneNumber);
        }
        sendTrackEvent(loginValue, AppEventTracking.EventMoEngage.LOGIN);
    }

    @Override
    public void sendGeneralEvent(Map<String, Object> value) {
        // no op, only for GTM
    }

    @Override
    public void sendGeneralEvent(String event, String category, String action, String label) {
        // no op, only for GTM
    }

    @Override
    public void sendEnhanceEcommerceEvent(Map<String, Object> value) {
        // no op, only for GTM
    }

    @Override
    public void sendScreenAuthenticated(String screenName) {
        // no op, only for GTM
    }

    @Override
    public void sendScreenAuthenticated(String screenName, Map<String, String> customDimension) {
        // no op, only for GTM
    }

    @Override
    public void sendScreenAuthenticated(String screenName, String shopID, String shopType, String pageType, String productId) {
        // no op, only for GTM
    }

    @Override
    public void sendEvent(String eventName, Map<String, Object> eventValue) {
        moengageInteractor.sendTrackEvent(eventName, eventValue);
    }

    @NotNull
    private boolean sendExistingUserAndInstallTrackingEvent() {
        if (getContext() != null) {
            UserSessionInterface userSession = new UserSession(getContext());
            moengageInteractor.sendExistingUserAndInstallTrackingEvent(userSession.isLoggedIn());
        }
        return true;
    }

    /**
     * will be eliminate soon
     * refer to setUserProfile(String... customerWrapper)
     *
     * @param customerWrapper
     */
    @Deprecated
    public void setUserProfile(CustomerWrapper customerWrapper) {
        setMoengageUserProfile(customerWrapper.getCustomerId(), customerWrapper.getFullName(), customerWrapper.getEmailAddress());
    }

    @SuppressWarnings("RestrictedApi")
    public void setMoengageUserProfile(String... customerWrapper) {
        moengageInteractor.setMoengageUserProfile(customerWrapper);
    }

    @Override
    public void sendRegistrationStartEvent(String medium) {
        Map<String, Object> map = new HashMap<>();
        map.put(AppEventTracking.MOENGAGE.MEDIUM, medium);
        sendTrackEvent(map, AppEventTracking.EventMoEngage.REG_START);
    }

    public void sendMoengageRegisterEvent(String fullName, String userID, String email, String loginMethod, String phoneNumber, boolean isGoldMerchant, String shopId, String shopName) {
        Timber.d("MoEngage check user " + fullName);

        moengageInteractor.setUserDataRegister(userID, fullName, email, phoneNumber, isGoldMerchant, shopName, shopId);

        Map<String, Object> map = new HashMap<>();
        map.put(AppEventTracking.MOENGAGE.NAME, fullName);
        map.put(AppEventTracking.MOENGAGE.EMAIL, email);
        map.put(AppEventTracking.MOENGAGE.MOBILE_NUM, phoneNumber);
        map.put(AppEventTracking.MOENGAGE.MEDIUM, loginMethod);
        sendTrackEvent(map, AppEventTracking.EventMoEngage.REG_COMPL);

    }

    public void setUserData(Map<String, Object> value, final String source) {
        moengageInteractor.setUserData(value);
    }

    public void setPushPreference(boolean status) {
        moengageInteractor.setPushPreference(status);
    }

    public void setNewsletterEmailPref(boolean status) {
        moengageInteractor.setNewsletterEmailPref(status);
    }

    public void sendTrackEvent(Map<String, Object> data, final String eventName) {
        moengageInteractor.sendTrackEventWithAppInfo(data, eventName, String.valueOf(GlobalConfig.VERSION_CODE));
    }

    //just aliasing
    @Override
    public void sendTrackEvent(String eventName, Map<String, Object> eventValue) {
        sendTrackEvent(eventValue, eventName);
    }

    private boolean checkNull(Object o) {
        if (o instanceof String)
            return !TextUtils.isEmpty((String) o);
        else if (o instanceof Boolean)
            return o != null;
        else
            return o != null;
    }

    public void logoutEvent() {
        moengageInteractor.logoutEvent();
    }

}
