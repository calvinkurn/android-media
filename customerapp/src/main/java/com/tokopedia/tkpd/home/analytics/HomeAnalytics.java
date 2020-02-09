package com.tokopedia.tkpd.home.analytics;

import android.content.Context;
import android.text.TextUtils;

import com.moengage.push.PushManager;
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.home.account.analytics.data.model.UserAttributeData;
import com.tokopedia.track.TrackApp;

import java.util.HashMap;
import java.util.Map;

import static com.moe.pushlibrary.utils.MoEHelperConstants.USER_ATTRIBUTE_UNIQUE_ID;
import static com.moe.pushlibrary.utils.MoEHelperConstants.USER_ATTRIBUTE_USER_BDAY;
import static com.moe.pushlibrary.utils.MoEHelperConstants.USER_ATTRIBUTE_USER_EMAIL;
import static com.moe.pushlibrary.utils.MoEHelperConstants.USER_ATTRIBUTE_USER_FIRST_NAME;
import static com.moe.pushlibrary.utils.MoEHelperConstants.USER_ATTRIBUTE_USER_GENDER;
import static com.moe.pushlibrary.utils.MoEHelperConstants.USER_ATTRIBUTE_USER_MOBILE;
import static com.moe.pushlibrary.utils.MoEHelperConstants.USER_ATTRIBUTE_USER_NAME;
import static com.tokopedia.core.analytics.AppEventTracking.MOENGAGE.HAS_PURCHASED_MARKETPLACE;
import static com.tokopedia.core.analytics.AppEventTracking.MOENGAGE.LAST_TRANSACT_DATE;
import static com.tokopedia.core.analytics.AppEventTracking.MOENGAGE.SHOP_ID;
import static com.tokopedia.core.analytics.AppEventTracking.MOENGAGE.SHOP_NAME;
import static com.tokopedia.core.analytics.AppEventTracking.MOENGAGE.TOPADS_AMT;
import static com.tokopedia.core.analytics.AppEventTracking.MOENGAGE.TOTAL_SOLD_ITEM;

/**
 * @author okasurya on 8/15/18.
 */
public class HomeAnalytics {
    public static void setUserAttribute(Context context, UserAttributeData profileData) {
        Map<String, Object> value = new HashMap<>();
        value.put(USER_ATTRIBUTE_USER_NAME, profileData.getProfile() == null ? "" : profileData.getProfile().getFullName());
        value.put(USER_ATTRIBUTE_USER_FIRST_NAME, profileData.getProfile() != null && profileData.getProfile().getFirstName() != null ? profileData.getProfile().getFirstName() : "");
        value.put(USER_ATTRIBUTE_UNIQUE_ID, profileData.getProfile() == null ? "" : profileData.getProfile().getUserId());
        value.put(USER_ATTRIBUTE_USER_EMAIL, profileData.getProfile() == null ? "" : profileData.getProfile().getEmail());
        value.put(USER_ATTRIBUTE_USER_MOBILE, TrackingUtils.normalizePhoneNumber(profileData.getProfile() == null || profileData.getProfile().getPhone() == null ? "" : profileData.getProfile().getPhone()));
        value.put(USER_ATTRIBUTE_USER_BDAY, DateFormatUtils.formatDate(DateFormatUtils.FORMAT_YYYY_MM_DD, DateFormatUtils.FORMAT_DD_MM_YYYY, TrackingUtils.extractFirstSegment(context, profileData.getProfile().getBday() != null ? profileData.getProfile().getBday() : "", "T")));
        value.put(SHOP_ID, profileData.getUserShopInfo() != null ? profileData.getUserShopInfo().getInfo().getShopId() : "");
        value.put(SHOP_NAME, profileData.getUserShopInfo() != null ? profileData.getUserShopInfo().getInfo().getShopName() : "");
        value.put(TOTAL_SOLD_ITEM, profileData.getUserShopInfo() != null ? profileData.getUserShopInfo().getStats().getShopItemSold() : "0");
        value.put(TOPADS_AMT, profileData.getTopadsDeposit() != null ? profileData.getTopadsDeposit().getTopadsAmount() + "" : "");
        value.put(HAS_PURCHASED_MARKETPLACE, profileData.getPaymentAdminProfile().getIsPurchasedMarketplace() != null ? profileData.getPaymentAdminProfile().getIsPurchasedMarketplace() : false);
        value.put(LAST_TRANSACT_DATE, DateFormatUtils.formatDate(DateFormatUtils.FORMAT_YYYY_MM_DD, DateFormatUtils.FORMAT_DD_MM_YYYY, TrackingUtils.extractFirstSegment(context, profileData.getPaymentAdminProfile() != null ? profileData.getPaymentAdminProfile().getLastPurchaseDate() : "", "T")));
        value.put(USER_ATTRIBUTE_USER_GENDER, profileData.getProfile().getGender() != null ? profileData.getProfile().getGender() : "0");

        TrackApp.getInstance().getMoEngage().setUserData(value, "GRAPHQL");
        if (!TextUtils.isEmpty(FCMCacheManager.getRegistrationId(context.getApplicationContext())))
            PushManager.getInstance().refreshToken(context.getApplicationContext(), FCMCacheManager.getRegistrationId(context.getApplicationContext()));
    }
}
