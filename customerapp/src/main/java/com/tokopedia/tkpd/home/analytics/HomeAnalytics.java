package com.tokopedia.tkpd.home.analytics;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.model.CustomerWrapper;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.home.account.analytics.data.model.UserAttributeData;
import com.tokopedia.tkpd.ConsumerRouterApplication;

/**
 * @author okasurya on 8/15/18.
 */
public class HomeAnalytics {
    public static void setUserAttribute(Context context, UserAttributeData profileData) {
        CustomerWrapper customerWrapper = null;
        try {
            customerWrapper = new CustomerWrapper.Builder()
                    .setTotalItemSold(profileData.getShopInfoMoengage().getStats() != null ? profileData.getShopInfoMoengage().getStats().getShopItemSold() : "0")
                    .setRegDate(DateFormatUtils.formatDate(DateFormatUtils.FORMAT_YYYY_MM_DD, DateFormatUtils.FORMAT_DD_MM_YYYY, TrackingUtils.extractFirstSegment(context, profileData.getProfile().getRegisterDate(), "T")))
                    .setDateShopCreated(profileData.getShopInfoMoengage().getInfo() != null ? profileData.getShopInfoMoengage().getInfo().getDateShopCreated() : "")
                    .setShopLocation(profileData.getShopInfoMoengage().getInfo() != null ? profileData.getShopInfoMoengage().getInfo().getShopLocation() : "")
                    .setTokocashAmt(profileData.getWallet() != null ? profileData.getWallet().getRawBalance() + "" : "")
                    .setSaldoAmt(profileData.getSaldo() != null ? profileData.getSaldo().getSaldo().getDepositLong() + "" : "")
                    .setTopAdsAmt(profileData.getTopadsDeposit() != null ? profileData.getTopadsDeposit().getTopadsAmount() + "" : "")
                    .setTopadsUser(profileData.getTopadsDeposit() != null ? profileData.getTopadsDeposit().getIsTopadsUser() : false)
                    .setHasPurchasedMarketplace(profileData.getPaymentAdminProfile().getIsPurchasedMarketplace() != null ? profileData.getPaymentAdminProfile().getIsPurchasedMarketplace() : false)
                    .setHasPurchasedDigital(profileData.getPaymentAdminProfile().getIsPurchasedDigital() != null ? profileData.getPaymentAdminProfile().getIsPurchasedDigital() : false)
                    .setHasPurchasedTiket(profileData.getPaymentAdminProfile().getIsPurchasedTicket() != null ? profileData.getPaymentAdminProfile().getIsPurchasedTicket() : false)
                    .setLastTransactionDate(DateFormatUtils.formatDate(DateFormatUtils.FORMAT_YYYY_MM_DD, DateFormatUtils.FORMAT_DD_MM_YYYY, TrackingUtils.extractFirstSegment(context, profileData.getPaymentAdminProfile() != null ? profileData.getPaymentAdminProfile().getLastPurchaseDate() : "", "T")))
                    .setTotalActiveProduct(profileData.getShopInfoMoengage().getInfo() != null ? profileData.getShopInfoMoengage().getInfo().getTotalActiveProduct() + "" : "")
                    .setShopScore(profileData.getShopInfoMoengage().getInfo() != null ? profileData.getShopInfoMoengage().getInfo().getShopScore() + "" : "")
                    .setDateOfBirth(DateFormatUtils.formatDate(DateFormatUtils.FORMAT_YYYY_MM_DD, DateFormatUtils.FORMAT_DD_MM_YYYY, TrackingUtils.extractFirstSegment(context, profileData.getProfile().getBday() != null ? profileData.getProfile().getBday() : "", "T")))
                    .setGender(profileData.getProfile().getGender() != null ? profileData.getProfile().getGender() : "0")
                    .setFullName(profileData.getProfile() == null ? "" : profileData.getProfile().getFullName())
                    .setEmailAddress(profileData.getProfile() == null ? "" : profileData.getProfile().getEmail())
                    .setPhoneNumber(TrackingUtils.normalizePhoneNumber(profileData.getProfile() == null || profileData.getProfile().getPhone() == null ? "" : profileData.getProfile().getPhone()))
                    .setCustomerId(profileData.getProfile() == null ? "" : profileData.getProfile().getUserId())
                    .setShopId(profileData.getShopInfoMoengage() != null ? profileData.getShopInfoMoengage().getInfo().getShopId() : "")
                    .setSeller((profileData.getShopInfoMoengage() != null &&
                            profileData.getShopInfoMoengage().getOwner() != null &&
                            profileData.getShopInfoMoengage().getOwner().getIsSeller() != null) ? profileData.getShopInfoMoengage().getOwner().getIsSeller() : false)
                    .setShopName(profileData.getShopInfoMoengage() != null ? profileData.getShopInfoMoengage().getInfo().getShopName() : "")
                    .setFirstName(profileData.getProfile() != null && profileData.getProfile().getFirstName() != null ? profileData.getProfile().getFirstName() : "")
                    .build();
        } catch (Exception e) {
            //no-op
        }
        TrackingUtils.setMoEUserAttributes(context, customerWrapper);
    }
}
