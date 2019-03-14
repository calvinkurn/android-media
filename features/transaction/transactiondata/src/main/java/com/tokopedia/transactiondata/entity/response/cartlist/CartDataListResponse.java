package com.tokopedia.transactiondata.entity.response.cartlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.transactiondata.entity.response.cartlist.shopgroup.Donation;
import com.tokopedia.transactiondata.entity.response.cartlist.shopgroup.ShopGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class CartDataListResponse {

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
    @SerializedName("autoapply_v2")
    @Expose
    private AutoapplyV2 autoapplyV2;
    @SerializedName("autoapply_stack")
    @Expose
    private AutoapplyStack autoapplyStack;

    public AutoapplyStack getAutoapplyStack() {
        return autoapplyStack;
    }

    public AutoapplyV2 getAutoapplyV2() {
        return autoapplyV2;
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

    public String getDefaultPromoDialogTab() {
        return defaultPromoDialogTab;
    }

    public AutoApply getAutoApply() {
        return autoApply;
    }

    public List<ShopGroup> getShopGroups() {
        return shopGroups;
    }

    public Donation getDonation() {
        return donation;
    }
}
