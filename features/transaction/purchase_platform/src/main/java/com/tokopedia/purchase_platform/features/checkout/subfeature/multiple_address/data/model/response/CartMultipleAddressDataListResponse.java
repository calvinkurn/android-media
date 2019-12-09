package com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.data.model.AutoApply;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.data.model.AutoApplyStack;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.data.model.AutoapplyV2;
import com.tokopedia.purchase_platform.features.cart.data.model.response.CartList;
import com.tokopedia.purchase_platform.common.data.model.response.Messages;
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.PromoSuggestion;
import com.tokopedia.purchase_platform.features.cart.data.model.response.Donation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Irfan Khoirul on 31/08/18.
 */

public class CartMultipleAddressDataListResponse {

    @SerializedName("errors")
    @Expose
    private List<String> errors = new ArrayList<>();
    @SerializedName("is_coupon_active")
    @Expose
    private int isCouponActive;
    @SerializedName("max_quantity")
    @Expose
    private int maxQuantity;
    @SerializedName("max_char_note")
    @Expose
    private int maxCharNote;
    @SerializedName("messages")
    @Expose
    private Messages messages;
    @SerializedName("promo_suggestion")
    @Expose
    private PromoSuggestion promoSuggestion;
    @SerializedName("autoapply")
    @Expose
    private AutoApply autoApply;
    @SerializedName("default_promo_dialog_tab")
    @Expose
    private String defaultPromoDialogTab;
    @SerializedName("donation")
    @Expose
    private Donation donation;
    @SerializedName("cart_list")
    @Expose
    private List<CartList> cartList = new ArrayList<>();
    @SerializedName("autoapply_v2")
    @Expose
    private AutoapplyV2 autoapplyV2;
    @SerializedName("autoapply_stack")
    @Expose
    private AutoApplyStack autoapplyStack;
    @SerializedName("is_show_onboarding")
    @Expose
    private boolean isShowOnboarding;

    public AutoApplyStack getAutoapplyStack() {
        return autoapplyStack;
    }

    public AutoapplyV2 getAutoapplyV2() {
        return autoapplyV2;
    }

    public void setAutoapplyV2(AutoapplyV2 autoapplyV2) {
        this.autoapplyV2 = autoapplyV2;
    }

    public int getIsCouponActive() {
        return isCouponActive;
    }

    public List<String> getErrors() {
        return errors;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public int getMaxCharNote() {
        return maxCharNote;
    }

    public Messages getMessages() {
        return messages;
    }

    public PromoSuggestion getPromoSuggestion() {
        return promoSuggestion;
    }

    public List<CartList> getCartList() {
        return cartList;
    }

    public String getDefaultPromoDialogTab() {
        return defaultPromoDialogTab;
    }

    public AutoApply getAutoApply() {
        return autoApply;
    }

    public Donation getDonation() {
        return donation;
    }

    public boolean isShowOnboarding() {
        return isShowOnboarding;
    }
}
