package com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.data.model.AutoApply;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.data.model.AutoapplyStack;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.data.model.AutoapplyV2;
import com.tokopedia.purchase_platform.features.cart.data.model.response.Ticker;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.egold.EgoldAttributes;
import com.tokopedia.purchase_platform.common.feature.promo_global.data.model.response.GlobalCouponAttr;
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.PromoSuggestion;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 21/02/18.
 */

public class ShipmentAddressFormDataResponse {
    @SerializedName("errors")
    @Expose
    private List<String> errors = new ArrayList<>();
    @SerializedName("error_code")
    @Expose
    private int errorCode;
    @SerializedName("is_multiple")
    @Expose
    private int isMultiple;
    @SerializedName("group_address")
    @Expose
    private List<GroupAddress> groupAddress = new ArrayList<>();
    @SerializedName("kero_token")
    @Expose
    private String keroToken;
    @SerializedName("kero_discom_token")
    @Expose
    private String keroDiscomToken;
    @SerializedName("kero_unix_time")
    @Expose
    private int keroUnixTime;
    @SerializedName("donation")
    @Expose
    private Donation donation;
    @SerializedName("cod")
    @Expose
    private Cod cod;
    @SerializedName("message")
    @Expose
    private Message message;
    @SerializedName("is_robinhood")
    @Expose
    private int isRobinhood;
    @SerializedName("is_hide_courier_name")
    @Expose
    private boolean isHidingCourier;
    @SerializedName("is_blackbox")
    @Expose
    private int isBlackbox;
    @SerializedName("promo_suggestion")
    @Expose
    private PromoSuggestion promoSuggestion;
    @SerializedName("autoapply")
    @Expose
    private AutoApply autoApply;
    @SerializedName("autoapply_v2")
    @Expose
    private AutoapplyV2 autoapplyV2;
    @SerializedName("egold_attributes")
    @Expose
    private EgoldAttributes egoldAttributes;
    @SerializedName("autoapply_stack")
    @Expose
    private AutoapplyStack autoapplyStack;
    @SerializedName("global_coupon_attr")
    @Expose
    private GlobalCouponAttr globalCouponAttr;
    @SerializedName("is_show_onboarding")
    @Expose
    private boolean isShowOnboarding;
    @SerializedName("is_ineligbile_promo_dialog_enabled")
    @Expose
    private boolean isIneligbilePromoDialogEnabled;
    @SerializedName("tickers")
    @Expose
    private List<Ticker> tickers = new ArrayList<>();

    @Deprecated
    public AutoapplyV2 getAutoapplyV2() {
        return autoapplyV2;
    }

    public List<String> getErrors() {
        return errors;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public int getIsMultiple() {
        return isMultiple;
    }

    public List<GroupAddress> getGroupAddress() {
        return groupAddress;
    }

    public String getKeroToken() {
        return keroToken;
    }

    public String getKeroDiscomToken() {
        return keroDiscomToken;
    }

    public int getKeroUnixTime() {
        return keroUnixTime;
    }

    public Donation getDonation() {
        return donation;
    }

    public Cod getCod() {
        return cod;
    }

    public Message getMessage() {
        return message;
    }

    public int getIsRobinhood() {
        return isRobinhood;
    }

    public boolean getHideCourier() {
        return isHidingCourier;
    }

    public int getIsBlackbox() {
        return isBlackbox;
    }

    public PromoSuggestion getPromoSuggestion() {
        return promoSuggestion;
    }

    @Deprecated
    public AutoApply getAutoApply() { return autoApply; }

    public EgoldAttributes getEgoldAttributes() {
        return egoldAttributes;
    }

    public void setEgoldAttributes(EgoldAttributes egoldAttributes) {
        this.egoldAttributes = egoldAttributes;
    }
    public AutoapplyStack getAutoapplyStack() { return autoapplyStack; }

    public GlobalCouponAttr getGlobalCouponAttr() { return globalCouponAttr; }

    public boolean isShowOnboarding() {
        return isShowOnboarding;
    }

    public boolean isIneligbilePromoDialogEnabled() {
        return isIneligbilePromoDialogEnabled;
    }

    public List<Ticker> getTickers() {
        return tickers;
    }
}
