package com.tokopedia.transactiondata.entity.response.shippingaddressform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.transactiondata.entity.response.cartlist.AutoApply;
import com.tokopedia.transactiondata.entity.response.cartlist.AutoapplyV2;
import com.tokopedia.transactiondata.entity.response.cartlist.PromoSuggestion;

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
    @SerializedName("is_robinhood")
    @Expose
    private int isRobinhood;
    @SerializedName("promo_suggestion")
    @Expose
    private PromoSuggestion promoSuggestion;
    @SerializedName("autoapply")
    @Expose
    private AutoApply autoApply;
    @SerializedName("autoapply_v2")
    @Expose
    private AutoapplyV2 autoapplyV2;

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

    public int getIsRobinhood() {
        return isRobinhood;
    }

    public PromoSuggestion getPromoSuggestion() {
        return promoSuggestion;
    }

    public AutoApply getAutoApply() {
        return autoApply;
    }
}
