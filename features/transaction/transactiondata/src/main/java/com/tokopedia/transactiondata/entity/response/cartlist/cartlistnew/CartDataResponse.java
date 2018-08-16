package com.tokopedia.transactiondata.entity.response.cartlist.cartlistnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.transactiondata.entity.response.cartlist.AutoApply;
import com.tokopedia.transactiondata.entity.response.cartlist.Messages;
import com.tokopedia.transactiondata.entity.response.cartlist.PromoSuggestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 16/08/18.
 */

public class CartDataResponse {

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
    @SerializedName("shop_group")
    @Expose
    private List<ShopGroup> shopGroups = new ArrayList<>();
    @SerializedName("donation")
    @Expose
    private Donation donation;

    public List<String> getErrors() {
        return errors;
    }

    public int getIsCouponActive() {
        return isCouponActive;
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

    public AutoApply getAutoApply() {
        return autoApply;
    }

    public String getDefaultPromoDialogTab() {
        return defaultPromoDialogTab;
    }

    public List<ShopGroup> getShopGroups() {
        return shopGroups;
    }

    public Donation getDonation() {
        return donation;
    }
}
