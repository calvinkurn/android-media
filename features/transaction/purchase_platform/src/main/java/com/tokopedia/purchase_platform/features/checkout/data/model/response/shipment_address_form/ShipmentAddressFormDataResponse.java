package com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.cart.data.model.response.Ticker;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.egold.EgoldAttributes;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.PromoSAFResponse;

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
    @SerializedName("egold_attributes")
    @Expose
    private EgoldAttributes egoldAttributes;
    @SerializedName("is_show_onboarding")
    @Expose
    private boolean isShowOnboarding;
    @SerializedName("is_ineligbile_promo_dialog_enabled")
    @Expose
    private boolean isIneligbilePromoDialogEnabled;
    @SerializedName("disabled_features")
    @Expose
    private List<String> disabledFeatures = new ArrayList<>();
    @SerializedName("tickers")
    @Expose
    private List<Ticker> tickers = new ArrayList<>();
    @SerializedName("donation_checkbox_status")
    @Expose
    private boolean donationCheckboxStatus;
    @SerializedName("campaign_timer")
    @Expose
    private CampaignTimer campaignTimer;
    @SerializedName("addresses")
    @Expose
    private Addresses addresses;
    @SerializedName("disabled_features_detail")
    @Expose
    private DisabledFeaturesDetail disabledFeaturesDetail;
    @SerializedName("promo")
    @Expose
    private PromoSAFResponse promoSAFResponse;
    @SerializedName("potential_gained_points")
    @Expose
    private PotentialGainedPoints potentialGainedPoints;

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

    public EgoldAttributes getEgoldAttributes() {
        return egoldAttributes;
    }

    public void setEgoldAttributes(EgoldAttributes egoldAttributes) {
        this.egoldAttributes = egoldAttributes;
    }

    public boolean isShowOnboarding() {
        return isShowOnboarding;
    }

    public boolean isIneligbilePromoDialogEnabled() {
        return isIneligbilePromoDialogEnabled;
    }

    public List<String> getDisabledFeatures() {
        return disabledFeatures;
    }

    public List<Ticker> getTickers() {
        return tickers;
    }

    public boolean isDonationCheckboxStatus() {
        return donationCheckboxStatus;
    }

    public CampaignTimer getCampaignTimer() {
        return campaignTimer;
    }

    public Addresses getAddresses() {
        return addresses;
    }

    public DisabledFeaturesDetail getDisabledFeaturesDetail() {
        return disabledFeaturesDetail;
    }

    public PromoSAFResponse getPromoSAFResponse() { return promoSAFResponse; }

    public void setPromoSAFResponse(PromoSAFResponse promoSAFResponse) { this.promoSAFResponse = promoSAFResponse; }

    public PotentialGainedPoints getPotentialGainedPoints() { return potentialGainedPoints; }

    public void setPotentialGainedPoints(PotentialGainedPoints potentialGainedPoints) {
        this.potentialGainedPoints = potentialGainedPoints;
    }
}
