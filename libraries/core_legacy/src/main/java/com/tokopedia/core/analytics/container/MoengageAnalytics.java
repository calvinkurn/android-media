package com.tokopedia.core.analytics.container;

import android.content.Context;
import android.text.TextUtils;

import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;
import com.moengage.core.Logger;
import com.moengage.core.MoEngage;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.model.CustomerWrapper;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.track.interfaces.ContextAnalytics;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

import static com.moe.pushlibrary.utils.MoEHelperConstants.USER_ATTRIBUTE_UNIQUE_ID;
import static com.moe.pushlibrary.utils.MoEHelperConstants.USER_ATTRIBUTE_USER_BDAY;
import static com.moe.pushlibrary.utils.MoEHelperConstants.USER_ATTRIBUTE_USER_EMAIL;
import static com.moe.pushlibrary.utils.MoEHelperConstants.USER_ATTRIBUTE_USER_FIRST_NAME;
import static com.moe.pushlibrary.utils.MoEHelperConstants.USER_ATTRIBUTE_USER_GENDER;
import static com.moe.pushlibrary.utils.MoEHelperConstants.USER_ATTRIBUTE_USER_MOBILE;
import static com.moe.pushlibrary.utils.MoEHelperConstants.USER_ATTRIBUTE_USER_NAME;
import static com.tokopedia.core.analytics.AppEventTracking.MOENGAGE.IS_GOLD_MERCHANT;
import static com.tokopedia.core.analytics.AppEventTracking.MOENGAGE.SHOP_ID;
import static com.tokopedia.core.analytics.AppEventTracking.MOENGAGE.SHOP_NAME;

public class MoengageAnalytics extends ContextAnalytics {
    public MoengageAnalytics(Context context) {
        super(context);
    }

    @Override
    public void initialize() {
        super.initialize();

        /*
          Mandatory to set small/Large notification icon while initialising sdk
          */
        MoEngage moEngage =
                new MoEngage.Builder(getContext(),
                        getContext().getResources().getString(R.string.key_moengage))
                        .setNotificationSmallIcon(R.drawable.ic_status_bar_notif_customerapp)
                        .setNotificationLargeIcon(R.drawable.ic_big_notif_customerapp)
                        .optOutTokenRegistration()
                        .build();
        MoEngage.initialise(moEngage);
        executeInstallTrackingAsync();
    }

    private void executeInstallTrackingAsync(){
        WeaveInterface installTrackingWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return sendExistingUserAndInstallTrackingEvent();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(installTrackingWeave, RemoteConfigKey.ENABLE_ASYNC_INSTALLTRACK, context);
    }

    @Override
    public void setMoEUserAttributesLogin(String userId, String name, String email, String phoneNumber, boolean isGoldMerchant, String shopName, String shopId, boolean hasShop, String loginMethod) {
        Map<String, Object> value = new HashMap<>();
        value.put(USER_ATTRIBUTE_UNIQUE_ID, userId);
        value.put(USER_ATTRIBUTE_USER_NAME, name);
        value.put(USER_ATTRIBUTE_USER_EMAIL, email);
        value.put(IS_GOLD_MERCHANT, isGoldMerchant);
        value.put(SHOP_NAME, shopName);
        value.put(SHOP_ID, shopId);
        value.put(USER_ATTRIBUTE_USER_MOBILE, phoneNumber);
        setUserData(value, "LOGIN");

        Map<String, Object> loginValue = new HashMap<>();
        loginValue.put(AppEventTracking.MOENGAGE.USER_ID, userId);
        loginValue.put(AppEventTracking.MOENGAGE.MEDIUM, loginMethod);
        loginValue.put(AppEventTracking.MOENGAGE.EMAIL, email);
        if(!TextUtils.isEmpty(phoneNumber)) {
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
        PayloadBuilder builder = new PayloadBuilder();
        for (Map.Entry<String, Object> entry : eventValue.entrySet()) {
            builder.putAttrString(entry.getKey(), entry.getValue().toString());
        }
        sendTrackEvent(builder.build(), eventName);
    }

    @NotNull
    private boolean sendExistingUserAndInstallTrackingEvent() {
        if (getContext() != null) {
            UserSessionInterface userSession = new UserSession(getContext());
            MoEHelper.getInstance(getContext()).setExistingUser(userSession.isLoggedIn());
        }
        return true;
    }

    /**
     * will be eliminate soon
     * refer to setUserProfile(String... customerWrapper)
     * @param customerWrapper
     */
    @Deprecated
    public void setUserProfile(CustomerWrapper customerWrapper) {
        setMoengageUserProfile(customerWrapper.getCustomerId(), customerWrapper.getFullName(), customerWrapper.getEmailAddress());
    }

    @SuppressWarnings("RestrictedApi")
    public void setMoengageUserProfile(String... customerWrapper) {
        if (customerWrapper.length != 3) {
            return;
        }
        final String customerId = customerWrapper[0];
        final String fullName = customerWrapper[1];
        final String emailAddress = customerWrapper[2];

        Timber.d("MoEngage check user " + customerId);

        MoEHelper helper = MoEHelper.getInstance(getContext());
        helper.setFullName(fullName);
        helper.setUniqueId(customerId);
        helper.setEmail(emailAddress);
    }

    @Override
    public void sendRegistrationStartEvent(String medium) {
        Map<String, Object> map = new HashMap<>();
        map.put(AppEventTracking.MOENGAGE.MEDIUM, medium);
        sendTrackEvent(map, AppEventTracking.EventMoEngage.REG_START);
    }

    public void sendMoengageRegisterEvent(String fullName, String userID, String email, String loginMethod, String phoneNumber,boolean isGoldMerchant,String shopId,String shopName) {
        Timber.d("MoEngage check user " + fullName);

        Map<String, Object> value = new HashMap<>();
        value.put(USER_ATTRIBUTE_UNIQUE_ID, userID);
        value.put(USER_ATTRIBUTE_USER_NAME, fullName);
        value.put(USER_ATTRIBUTE_USER_EMAIL, email);
        value.put(USER_ATTRIBUTE_USER_MOBILE, phoneNumber);
        value.put(IS_GOLD_MERCHANT, isGoldMerchant);
        value.put(SHOP_NAME, shopName);
        value.put(SHOP_ID, shopId);
        setUserData(value, "Registration_Completed");

        Map<String, Object> map = new HashMap<>();
        map.put(AppEventTracking.MOENGAGE.NAME, fullName);
        map.put(AppEventTracking.MOENGAGE.EMAIL, email);
        map.put(AppEventTracking.MOENGAGE.MOBILE_NUM, phoneNumber);
        map.put(AppEventTracking.MOENGAGE.MEDIUM,loginMethod);
        sendTrackEvent(map, AppEventTracking.EventMoEngage.REG_COMPL);

    }

    public void setUserData(Map<String, Object> value, final String source) {
        MoEHelper helper = MoEHelper.getInstance(getContext());

        if (checkNull(value.get(USER_ATTRIBUTE_USER_NAME)))
            helper.setFullName((String) value.get(USER_ATTRIBUTE_USER_NAME));

        if (checkNull(value.get(USER_ATTRIBUTE_USER_FIRST_NAME)))
            helper.setFirstName((String) value.get(USER_ATTRIBUTE_USER_FIRST_NAME));

        if (checkNull(value.get(USER_ATTRIBUTE_UNIQUE_ID)))
            helper.setUniqueId((String) value.get(USER_ATTRIBUTE_UNIQUE_ID));

        if (checkNull(value.get(USER_ATTRIBUTE_USER_EMAIL)))
            helper.setEmail((String) value.get(USER_ATTRIBUTE_USER_EMAIL));

        if (checkNull(value.get(USER_ATTRIBUTE_USER_MOBILE))) {
            String number=(String) value.get(USER_ATTRIBUTE_USER_MOBILE);
            number= TrackingUtils.normalizePhoneNumber(number);
            helper.setNumber(number);
        }

        if (!TextUtils.isEmpty((String) value.get(USER_ATTRIBUTE_USER_BDAY))) {
            helper.setBirthDate((String) value.get(USER_ATTRIBUTE_USER_BDAY));
        }

        if (checkNull(value.get(AppEventTracking.MOENGAGE.IS_GOLD_MERCHANT)))
            helper.setUserAttribute(AppEventTracking.MOENGAGE.IS_GOLD_MERCHANT, String.valueOf(value.get(AppEventTracking.MOENGAGE.IS_GOLD_MERCHANT)));

        if (checkNull(value.get(AppEventTracking.MOENGAGE.SHOP_ID)))
            helper.setUserAttribute(AppEventTracking.MOENGAGE.SHOP_ID, (String) value.get(AppEventTracking.MOENGAGE.SHOP_ID));

        if (checkNull(value.get(AppEventTracking.MOENGAGE.SHOP_NAME)))
            helper.setUserAttribute(AppEventTracking.MOENGAGE.SHOP_NAME, (String) value.get(AppEventTracking.MOENGAGE.SHOP_NAME));

        if (checkNull(value.get(AppEventTracking.MOENGAGE.TOTAL_SOLD_ITEM)))
            helper.setUserAttribute(AppEventTracking.MOENGAGE.TOTAL_SOLD_ITEM, (String) value.get(AppEventTracking.MOENGAGE.TOTAL_SOLD_ITEM));

        if (checkNull(value.get(AppEventTracking.MOENGAGE.TOPADS_AMT)))
            helper.setUserAttribute(AppEventTracking.MOENGAGE.TOPADS_AMT, (String) value.get(AppEventTracking.MOENGAGE.TOPADS_AMT));

        if (checkNull(value.get(AppEventTracking.MOENGAGE.HAS_PURCHASED_MARKETPLACE)))
            helper.setUserAttribute(AppEventTracking.MOENGAGE.HAS_PURCHASED_MARKETPLACE, (boolean) value.get(AppEventTracking.MOENGAGE.HAS_PURCHASED_MARKETPLACE));

        if (checkNull(value.get(AppEventTracking.MOENGAGE.LAST_TRANSACT_DATE)))
            helper.setUserAttribute(AppEventTracking.MOENGAGE.LAST_TRANSACT_DATE, (String) value.get(AppEventTracking.MOENGAGE.LAST_TRANSACT_DATE));

        if (checkNull(value.get(AppEventTracking.MOENGAGE.SHOP_SCORE)))
            helper.setUserAttribute(AppEventTracking.MOENGAGE.SHOP_SCORE, (String) value.get(AppEventTracking.MOENGAGE.SHOP_SCORE));

        if (checkNull(value.get(USER_ATTRIBUTE_USER_GENDER)))
            helper.setGender(value.get(USER_ATTRIBUTE_USER_GENDER).equals("1") ? "male" : "female");
    }

    public void setPushPreference(boolean status) {
        MoEHelper.getInstance(getContext()).setUserAttribute(AppEventTracking.EventMoEngage.PUSH_PREFERENCE, status);
    }

    public void setNewsletterEmailPref(boolean status) {
        MoEHelper.getInstance(getContext()).setUserAttribute(AppEventTracking.EventMoEngage.EMAIL_PREFERENCE, status);
    }

    private void sendTrackEvent(JSONObject data, final String eventName) {
        MoEHelper.getInstance(getContext()).trackEvent(eventName, data);
    }

    public void sendTrackEvent(Map<String, Object> data, final String eventName) {
        PayloadBuilder builder = new PayloadBuilder();
        if (data.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Object value = entry.getValue();

            if (value instanceof JSONArray) {
                builder.putAttrJSONArray(entry.getKey(), (JSONArray) entry.getValue());
            } else {
                builder.putAttrString(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        builder.putAttrString(AppEventTracking.MOENGAGE.APP_VERSION, String.valueOf(GlobalConfig.VERSION_CODE));
        builder.putAttrString(AppEventTracking.MOENGAGE.PLATFORM, "android");
        sendTrackEvent(builder.build(), eventName);
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
        MoEHelper.getInstance(context).logoutUser();
    }
}
