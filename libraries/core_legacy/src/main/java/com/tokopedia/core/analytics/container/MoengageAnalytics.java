package com.tokopedia.core.analytics.container;

import android.content.Context;
import android.support.v4.util.Preconditions;
import android.text.TextUtils;

import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;
import com.moengage.core.MoEngage;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.model.CustomerWrapper;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.interfaces.ContextAnalytics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.moe.pushlibrary.utils.MoEHelperConstants.*;
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
        setUserData(value, "LOGIN");

        Map<String, Object> value2 = new HashMap<>();
        value.put(AppEventTracking.MOENGAGE.USER_ID, userId);
        value.put(AppEventTracking.MOENGAGE.MEDIUM, loginMethod);
        value.put(AppEventTracking.MOENGAGE.EMAIL, email);
        sendTrackEvent(value2, AppEventTracking.EventMoEngage.LOGIN);
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
    public void sendEnhanceECommerceEvent(Map<String, Object> value) {
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

    public void isExistingUser(final boolean bol) {
        CommonUtils.dumper("MoEngage check is existing user " + bol);
        MoEHelper.getInstance(getContext()).setExistingUser(bol);
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
        Preconditions.checkArrayElementsNotNull(customerWrapper, "please pass 3 value, customerId, fullName, emailAddress");

        final String customerId = customerWrapper[0];
        final String fullName = customerWrapper[1];
        final String emailAddress = customerWrapper[2];

        com.tkpd.library.utils.legacy.CommonUtils.dumper("MoEngage check user " + customerId);

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

    public void sendMoengageRegisterEvent(String fullName, String mobileNo) {
        CommonUtils.dumper("MoEngage check user " + fullName);
        Map<String, Object> map = new HashMap<>();
        map.put(AppEventTracking.MOENGAGE.NAME, fullName);
        map.put(AppEventTracking.MOENGAGE.MOBILE_NUM, mobileNo);
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

        if (checkNull(value.get(USER_ATTRIBUTE_USER_MOBILE)))
            helper.setNumber((String) value.get(USER_ATTRIBUTE_USER_MOBILE));

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

    public void setUserData(CustomerWrapper value, final String source) {
        MoEHelper helper = MoEHelper.getInstance(getContext());

        if (checkNull(value.getFullName()))
            helper.setFullName(value.getFullName());

        if (checkNull(value.getFirstName()))
            helper.setFirstName(value.getFirstName());

        if (checkNull(value.getCustomerId()))
            helper.setUniqueId(value.getCustomerId());

        if (checkNull(value.getEmailAddress()))
            helper.setEmail(value.getEmailAddress());

        if (checkNull(value.getPhoneNumber()))
            helper.setNumber(value.getPhoneNumber());

        if (!TextUtils.isEmpty(value.getDateOfBirth())) {
            helper.setBirthDate(value.getDateOfBirth());
        }

        if (checkNull(value.isGoldMerchant()))
            helper.setUserAttribute(AppEventTracking.MOENGAGE.IS_GOLD_MERCHANT, String.valueOf(value.isGoldMerchant()));

        if (checkNull(value.getShopId()))
            helper.setUserAttribute(AppEventTracking.MOENGAGE.SHOP_ID, value.getShopId());

        if (checkNull(value.getShopName()))
            helper.setUserAttribute(AppEventTracking.MOENGAGE.SHOP_NAME, value.getShopName());

        if (checkNull(value.getTotalItemSold()))
            helper.setUserAttribute(AppEventTracking.MOENGAGE.TOTAL_SOLD_ITEM, value.getTotalItemSold());

        if (checkNull(value.getTopAdsAmt()))
            helper.setUserAttribute(AppEventTracking.MOENGAGE.TOPADS_AMT, value.getTopAdsAmt());

        if (checkNull(value.isHasPurchasedMarketplace()))
            helper.setUserAttribute(AppEventTracking.MOENGAGE.HAS_PURCHASED_MARKETPLACE, value.isHasPurchasedMarketplace());

        if (checkNull(value.getLastTransactionDate()))
            helper.setUserAttribute(AppEventTracking.MOENGAGE.LAST_TRANSACT_DATE, value.getLastTransactionDate());

        if (checkNull(value.getShopScore()))
            helper.setUserAttribute(AppEventTracking.MOENGAGE.SHOP_SCORE, value.getShopScore());

        if (checkNull(value.getGender()))
            helper.setGender(value.getGender().equals("1") ? "male" : "female");

    }

    public void setPushPreference(boolean status) {
        MoEHelper.getInstance(getContext()).setUserAttribute("push_preference", status);
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
