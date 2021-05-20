package com.tokopedia.logisticcart.shipping.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData;

import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public class ShippingDurationUiModel implements Parcelable, RatesViewModelType {

    private ServiceData serviceData;
    private List<ShippingCourierUiModel> shippingCourierUiModelList;
    private boolean selected;
    private boolean showShowCase;
    private String errorMessage;
    private boolean isCodAvailable;
    private String codText;
    private boolean showShippingInformation;
    private MerchantVoucherModel merchantVoucherModel;
    private int etaErrorCode;
    private DynamicPricingModel dynamicPricingModel;

    public ShippingDurationUiModel() {
    }

    protected ShippingDurationUiModel(Parcel in) {
        serviceData = in.readParcelable(ServiceData.class.getClassLoader());
        shippingCourierUiModelList = in.createTypedArrayList(ShippingCourierUiModel.CREATOR);
        selected = in.readByte() != 0;
        showShowCase = in.readByte() != 0;
        errorMessage = in.readString();
        showShippingInformation = in.readByte() != 0;
        etaErrorCode = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(serviceData, flags);
        dest.writeTypedList(shippingCourierUiModelList);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeByte((byte) (showShowCase ? 1 : 0));
        dest.writeString(errorMessage);
        dest.writeByte((byte) (showShippingInformation ? 1 : 0));
        dest.writeInt(etaErrorCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShippingDurationUiModel> CREATOR = new Creator<ShippingDurationUiModel>() {
        @Override
        public ShippingDurationUiModel createFromParcel(Parcel in) {
            return new ShippingDurationUiModel(in);
        }

        @Override
        public ShippingDurationUiModel[] newArray(int size) {
            return new ShippingDurationUiModel[size];
        }
    };

    public ServiceData getServiceData() {
        return serviceData;
    }

    public void setServiceData(ServiceData serviceData) {
        this.serviceData = serviceData;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<ShippingCourierUiModel> getShippingCourierViewModelList() {
        return shippingCourierUiModelList;
    }

    public void setShippingCourierViewModelList(List<ShippingCourierUiModel> shippingCourierUiModelList) {
        this.shippingCourierUiModelList = shippingCourierUiModelList;
    }

    public boolean isShowShowCase() {
        return showShowCase;
    }

    public void setShowShowCase(boolean showShowCase) {
        this.showShowCase = showShowCase;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isCodAvailable() {
        return isCodAvailable;
    }

    public void setCodAvailable(boolean codAvailable) {
        isCodAvailable = codAvailable;
    }

    public String getCodText() {
        return codText;
    }

    public void setCodText(String codText) {
        this.codText = codText;
    }

    public boolean isShowShippingInformation() {
        return showShippingInformation;
    }

    public void setShowShippingInformation(boolean showShippingInformation) {
        this.showShippingInformation = showShippingInformation;
    }

    public MerchantVoucherModel getMerchantVoucherModel() {
        return merchantVoucherModel;
    }

    public void setMerchantVoucherModel(MerchantVoucherModel merchantVoucherModel) {
        this.merchantVoucherModel = merchantVoucherModel;
    }

    public int getEtaErrorCode() {
        return etaErrorCode;
    }

    public void setEtaErrorCode(int etaErrorCode) {
        this.etaErrorCode = etaErrorCode;
    }

    public DynamicPricingModel getDynamicPricingModel() {
        return dynamicPricingModel;
    }

    public void setDynamicPricingModel(DynamicPricingModel dynamicPricingModel) {
        this.dynamicPricingModel = dynamicPricingModel;
    }
}
