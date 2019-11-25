package com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.logisticcart.shipping.model.CodModel;
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.TickerData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.AutoApplyData;
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.AutoApplyStackData;
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.CartPromoSuggestionHolderData;
import com.tokopedia.purchase_platform.common.feature.promo_global.domain.model.GlobalCouponAttrData;
import com.tokopedia.purchase_platform.features.checkout.view.viewmodel.EgoldAttributeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 21/02/18.
 */

public class CartShipmentAddressFormData implements Parcelable {
    private boolean hasError;
    private boolean isError;
    private String errorMessage;
    private int errorCode;
    private boolean isShowOnboarding;
    private boolean isMultiple;
    private boolean isMultipleDisable;
    private boolean isDropshipperDisable;
    private boolean isOrderPrioritasDisable;
    private List<GroupAddress> groupAddress = new ArrayList<>();
    private String keroToken;
    private String keroDiscomToken;
    private int keroUnixTime;
    private Donation donation;
    private CodModel cod;
    private boolean useCourierRecommendation;
    private boolean isHidingCourier;
    private boolean isBlackbox;
    private CartPromoSuggestionHolderData cartPromoSuggestionHolderData;
    private AutoApplyData autoApplyData;
    private EgoldAttributeModel egoldAttributes;
    private AutoApplyStackData autoApplyStackData;
    private GlobalCouponAttrData globalCouponAttrData;
    private boolean isIneligbilePromoDialogEnabled;
    private TickerData tickerData;
    private AddressesData addressesData;
    private DisabledFeaturesDetailData disabledFeaturesDetailData;

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isMultiple() {
        return isMultiple;
    }

    public void setMultiple(boolean multiple) {
        isMultiple = multiple;
    }

    public boolean isMultipleDisable() {
        return isMultipleDisable;
    }

    public void setMultipleDisable(boolean multipleDisable) {
        isMultipleDisable = multipleDisable;
    }

    public boolean isDropshipperDisable() {
        return isDropshipperDisable;
    }

    public void setDropshipperDisable(boolean dropshipperDisable) {
        isDropshipperDisable = dropshipperDisable;
    }

    public boolean isOrderPrioritasDisable() {
        return isOrderPrioritasDisable;
    }

    public void setOrderPrioritasDisable(boolean orderPrioritasDisable) {
        isOrderPrioritasDisable = orderPrioritasDisable;
    }

    public List<GroupAddress> getGroupAddress() {
        return groupAddress;
    }

    public void setGroupAddress(List<GroupAddress> groupAddress) {
        this.groupAddress = groupAddress;
    }

    public String getKeroToken() {
        return keroToken;
    }

    public void setKeroToken(String keroToken) {
        this.keroToken = keroToken;
    }

    public String getKeroDiscomToken() {
        return keroDiscomToken;
    }

    public void setKeroDiscomToken(String keroDiscomToken) {
        this.keroDiscomToken = keroDiscomToken;
    }

    public int getKeroUnixTime() {
        return keroUnixTime;
    }

    public void setKeroUnixTime(int keroUnixTime) {
        this.keroUnixTime = keroUnixTime;
    }

    public Donation getDonation() {
        return donation;
    }

    public void setDonation(Donation donation) {
        this.donation = donation;
    }

    public boolean isUseCourierRecommendation() {
        return useCourierRecommendation;
    }

    public void setUseCourierRecommendation(boolean useCourierRecommendation) {
        this.useCourierRecommendation = useCourierRecommendation;
    }

    public boolean getIsBlackbox() {
        return isBlackbox;
    }

    public void setIsBlackbox(boolean blackbox) {
        this.isBlackbox = blackbox;
    }

    public CartPromoSuggestionHolderData getCartPromoSuggestionHolderData() {
        return cartPromoSuggestionHolderData;
    }

    public void setCartPromoSuggestionHolderData(CartPromoSuggestionHolderData cartPromoSuggestionHolderData) {
        this.cartPromoSuggestionHolderData = cartPromoSuggestionHolderData;
    }

    public AutoApplyData getAutoApplyData() {
        return autoApplyData;
    }

    public void setAutoApplyData(AutoApplyData autoApplyData) {
        this.autoApplyData = autoApplyData;
    }

    public AutoApplyStackData getAutoApplyStackData() {
        return autoApplyStackData;
    }

    public void setAutoApplyStackData(AutoApplyStackData autoApplyStackData) {
        this.autoApplyStackData = autoApplyStackData;
    }

    public GlobalCouponAttrData getGlobalCouponAttrData() {
        return globalCouponAttrData;
    }

    public void setGlobalCouponAttrData(GlobalCouponAttrData globalCouponAttrData) {
        this.globalCouponAttrData = globalCouponAttrData;
    }

    public CodModel getCod() {
        return cod;
    }

    public void setCod(CodModel cod) {
        this.cod = cod;
    }

    public EgoldAttributeModel getEgoldAttributes() {
        return egoldAttributes;
    }

    public void setEgoldAttributes(EgoldAttributeModel egoldAttributes) {
        this.egoldAttributes = egoldAttributes;
    }

    public boolean isHidingCourier() {
        return isHidingCourier;
    }

    public void setHidingCourier(boolean hidingCourier) {
        isHidingCourier = hidingCourier;
    }

    public boolean isShowOnboarding() {
        return isShowOnboarding;
    }

    public void setShowOnboarding(boolean showOnboarding) {
        isShowOnboarding = showOnboarding;
    }

    public boolean isIneligbilePromoDialogEnabled() {
        return isIneligbilePromoDialogEnabled;
    }

    public void setIneligbilePromoDialogEnabled(boolean ineligbilePromoDialogEnabled) {
        isIneligbilePromoDialogEnabled = ineligbilePromoDialogEnabled;
    }

    public TickerData getTickerData() {
        return tickerData;
    }

    public void setTickerData(TickerData tickerData) {
        this.tickerData = tickerData;
    }

    public AddressesData getAddressesData() {
        return addressesData;
    }

    public void setAddressesData(AddressesData addressesData) {
        this.addressesData = addressesData;
    }

    public DisabledFeaturesDetailData getDisabledFeaturesDetailData() {
        return disabledFeaturesDetailData;
    }

    public void setDisabledFeaturesDetailData(DisabledFeaturesDetailData disabledFeaturesDetailData) {
        this.disabledFeaturesDetailData = disabledFeaturesDetailData;
    }

    public CartShipmentAddressFormData() {
    }

    protected CartShipmentAddressFormData(Parcel in) {
        hasError = in.readByte() != 0;
        isError = in.readByte() != 0;
        errorMessage = in.readString();
        errorCode = in.readInt();
        isShowOnboarding = in.readByte() != 0;
        isMultiple = in.readByte() != 0;
        isMultipleDisable = in.readByte() != 0;
        isDropshipperDisable = in.readByte() != 0;
        isOrderPrioritasDisable = in.readByte() != 0;
        groupAddress = in.createTypedArrayList(GroupAddress.CREATOR);
        keroToken = in.readString();
        keroDiscomToken = in.readString();
        keroUnixTime = in.readInt();
        donation = in.readParcelable(Donation.class.getClassLoader());
        useCourierRecommendation = in.readByte() != 0;
        isHidingCourier = in.readByte() != 0;
        cartPromoSuggestionHolderData = in.readParcelable(CartPromoSuggestionHolderData.class.getClassLoader());
        autoApplyData = in.readParcelable(AutoApplyData.class.getClassLoader());
        egoldAttributes = in.readParcelable(EgoldAttributeModel.class.getClassLoader());
        autoApplyStackData = in.readParcelable(AutoApplyStackData.class.getClassLoader());
        isIneligbilePromoDialogEnabled = in.readByte() != 0;
        tickerData = in.readParcelable(TickerData.class.getClassLoader());
        addressesData = in.readParcelable(AddressesData.class.getClassLoader());
        disabledFeaturesDetailData = in.readParcelable(DisabledFeaturesDetailData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (hasError ? 1 : 0));
        dest.writeByte((byte) (isError ? 1 : 0));
        dest.writeString(errorMessage);
        dest.writeInt(errorCode);
        dest.writeByte((byte) (isShowOnboarding ? 1 : 0));
        dest.writeByte((byte) (isMultiple ? 1 : 0));
        dest.writeByte((byte) (isMultipleDisable ? 1 : 0));
        dest.writeByte((byte) (isDropshipperDisable ? 1 : 0));
        dest.writeByte((byte) (isOrderPrioritasDisable ? 1 : 0));
        dest.writeTypedList(groupAddress);
        dest.writeString(keroToken);
        dest.writeString(keroDiscomToken);
        dest.writeInt(keroUnixTime);
        dest.writeParcelable(donation, flags);
        dest.writeByte((byte) (useCourierRecommendation ? 1 : 0));
        dest.writeByte((byte) (isHidingCourier ? 1 : 0));
        dest.writeParcelable(cartPromoSuggestionHolderData, flags);
        dest.writeParcelable(autoApplyData, flags);
        dest.writeParcelable(egoldAttributes, flags);
        dest.writeParcelable(autoApplyStackData, flags);
        dest.writeByte((byte) (isIneligbilePromoDialogEnabled ? 1 : 0));
        dest.writeParcelable(tickerData, flags);
        dest.writeParcelable(addressesData, flags);
        dest.writeParcelable(disabledFeaturesDetailData, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartShipmentAddressFormData> CREATOR = new Creator<CartShipmentAddressFormData>() {
        @Override
        public CartShipmentAddressFormData createFromParcel(Parcel in) {
            return new CartShipmentAddressFormData(in);
        }

        @Override
        public CartShipmentAddressFormData[] newArray(int size) {
            return new CartShipmentAddressFormData[size];
        }
    };

    public boolean isAvailablePurchaseProtection() {
        for (GroupAddress address : groupAddress) {
            for (GroupShop groupShop : address.getGroupShop()) {
                for (Product product : groupShop.getProducts()) {
                    if (product.getPurchaseProtectionPlanData() != null &&
                            product.getPurchaseProtectionPlanData().isProtectionAvailable()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
