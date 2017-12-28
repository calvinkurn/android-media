package com.tokopedia.digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

public class AttributesCart {

    @SerializedName("client_number")
    @Expose
    private String clientNumber;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("operator_name")
    @Expose
    private String operatorName;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("price_plain")
    @Expose
    private long pricePlain;
    @SerializedName("instant_checkout")
    @Expose
    private boolean instantCheckout;
    @SerializedName("need_otp")
    @Expose
    private boolean needOtp;
    @SerializedName("enable_voucher")
    @Expose
    private boolean enableVoucher;
    @SerializedName("is_coupon_active")
    @Expose
    private int isCouponActive;
    @SerializedName("sms_state")
    @Expose
    private String smsState;
    @SerializedName("user_input_price")
    @Expose
    private UserInputPrice userInputPrice;
    @SerializedName("main_info")
    @Expose
    private List<MainInfo> mainInfo = null;
    @SerializedName("additional_info")
    @Expose
    private List<AdditionalInfo> additionalInfo = null;
    @SerializedName("voucher_autocode")
    @Expose
    private String voucherAutoCode;

    public String getClientNumber() {
        return clientNumber;
    }

    public String getPrice() {
        return price;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public String getIcon() {
        return icon;
    }

    public long getPricePlain() {
        return pricePlain;
    }

    public boolean isInstantCheckout() {
        return instantCheckout;
    }

    public boolean isNeedOtp() {
        return needOtp;
    }

    public String getSmsState() {
        return smsState;
    }

    public UserInputPrice getUserInputPrice() {
        return userInputPrice;
    }

    public List<MainInfo> getMainInfo() {
        return mainInfo;
    }

    public List<AdditionalInfo> getAdditionalInfo() {
        return additionalInfo;
    }

    public boolean isEnableVoucher() {
        return enableVoucher;
    }

    public int isCouponActive() {
        return isCouponActive;
    }

    public String getVoucherAutoCode() {
        return voucherAutoCode;
    }
}
