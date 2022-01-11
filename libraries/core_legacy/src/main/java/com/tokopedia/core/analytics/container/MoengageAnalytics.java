package com.tokopedia.core.analytics.container;

import android.content.Context;
import com.tokopedia.track.interfaces.ContextAnalytics;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 *@deprecated This MoengageAnalytics is deprecated, We are using this class as no-op
 * */
@Deprecated
public class MoengageAnalytics extends ContextAnalytics {

    public MoengageAnalytics(Context context) {
        super(context);
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void setMoEUserAttributesLogin(String userId, String name, String email, String phoneNumber, boolean isGoldMerchant, String shopName, String shopId, boolean hasShop, String loginMethod) {
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
    }

    @NotNull
    private boolean sendExistingUserAndInstallTrackingEvent() {
        return true;
    }

    @SuppressWarnings("RestrictedApi")
    public void setMoengageUserProfile(String... customerWrapper) {
    }

    @Override
    public void sendRegistrationStartEvent(String medium) {
    }

    public void sendMoengageRegisterEvent(String fullName, String userID, String email, String loginMethod, String phoneNumber, boolean isGoldMerchant, String shopId, String shopName) {


    }

    public void setUserData(Map<String, Object> value, final String source) {
    }

    public void setPushPreference(boolean status) {
    }

    public void setNewsletterEmailPref(boolean status) {
    }

    public void sendTrackEvent(Map<String, Object> data, final String eventName) {
    }

    //just aliasing
    @Override
    public void sendTrackEvent(String eventName, Map<String, Object> eventValue) {
    }

    public void logoutEvent() {
    }

}
