package com.tokopedia.checkout.subfeature.multiple_address.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.checkout.data.model.response.Messages;
import com.tokopedia.checkout.data.model.response.shipment_address_form.Donation;

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
    @SerializedName("default_promo_dialog_tab")
    @Expose
    private String defaultPromoDialogTab;
    @SerializedName("donation")
    @Expose
    private Donation donation;
    @SerializedName("cart_list")
    @Expose
    private List<CartList> cartList = new ArrayList<>();
    @SerializedName("is_show_onboarding")
    @Expose
    private boolean isShowOnboarding;

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

    public List<CartList> getCartList() {
        return cartList;
    }

    public String getDefaultPromoDialogTab() {
        return defaultPromoDialogTab;
    }

    public Donation getDonation() {
        return donation;
    }

    public boolean isShowOnboarding() {
        return isShowOnboarding;
    }
}
