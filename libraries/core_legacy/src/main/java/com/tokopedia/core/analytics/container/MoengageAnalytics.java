package com.tokopedia.core.analytics.container;

import android.content.Context;
import android.support.v4.util.Preconditions;
import android.text.TextUtils;

import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;
import com.moengage.core.MoEngage;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.model.CustomerWrapper;
import com.tokopedia.track.interfaces.ContextAnalytics;

import org.json.JSONObject;

import java.util.Map;

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
                        .build();
        MoEngage.initialise(moEngage);
    }

    @Override
    public void sendGeneralEvent(Map<String, Object> value) {

    }

    public void isExistingUser(final boolean bol) {
        CommonUtils.dumper("MoEngage check is existing user " + bol);
        MoEHelper.getInstance(getContext()).setExistingUser(bol);
    }

    /**
     * will be eliminate soon
     *
     * @param customerWrapper
     */
    @Deprecated
    public void setUserProfile(CustomerWrapper customerWrapper) {
        setUserProfile(customerWrapper.getCustomerId(), customerWrapper.getFullName(), customerWrapper.getEmailAddress());
    }

    @SuppressWarnings("RestrictedApi")
    public void setUserProfile(String... customerWrapper) {
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

    public void sendRegistrationStartEvent(String medium) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.MEDIUM, medium);
        sendEvent(
                new PayloadBuilder()
                        .putAttrString(AppEventTracking.MOENGAGE.MEDIUM, medium)
                        .build()
                , AppEventTracking.EventMoEngage.REG_START
        );
    }

    public void sendRegisterEvent(String fullName, String mobileNo) {
        CommonUtils.dumper("MoEngage check user " + fullName);
        sendEvent(
                new PayloadBuilder()
                        .putAttrString(AppEventTracking.MOENGAGE.NAME, fullName)
                        .putAttrString(AppEventTracking.MOENGAGE.MOBILE_NUM, mobileNo)
                        .build()
                , AppEventTracking.EventMoEngage.REG_COMPL
        );
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

    public void sendEvent(JSONObject data, final String eventName) {
        MoEHelper.getInstance(getContext()).trackEvent(eventName, data);
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
        MoEHelper.getInstance(getContext()).logoutUser();
    }
}
