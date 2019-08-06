package com.tokopedia.checkout.domain.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

/**
 * Created by kris on 1/24/18. Tokopedia
 */

public class MultipleAddressItemData implements Parcelable {

    private RecipientAddressModel recipientAddressModel;
    private int cartPosition;
    private int addressPosition;
    private String cartId = "";
    private int addressStatus;
    private String parentId;
    private String productId = "";
    private String productWeightFmt = "";
    private int productRawWeight;
    private String productQty = "";
    private String productNotes = "";
    private int maxQuantity;
    private int minQuantity;
    private String errorCheckoutPriceLimit;
    private String errorFieldBetween;
    private String errorFieldMaxChar;
    private String errorFieldRequired;
    private String errorProductAvailableStock;
    private String errorProductAvailableStockDetail;
    private String errorProductMaxQuantity;
    private String errorProductMinQuantity;
    private int maxRemark;
    private boolean stateNotesOpen;

    public MultipleAddressItemData() {
    }

    protected MultipleAddressItemData(Parcel in) {
        recipientAddressModel = in.readParcelable(RecipientAddressModel.class.getClassLoader());
        cartPosition = in.readInt();
        addressPosition = in.readInt();
        cartId = in.readString();
        addressStatus = in.readInt();
        parentId = in.readString();
        productId = in.readString();
        productWeightFmt = in.readString();
        productRawWeight = in.readInt();
        productQty = in.readString();
        productNotes = in.readString();
        maxQuantity = in.readInt();
        minQuantity = in.readInt();
        errorCheckoutPriceLimit = in.readString();
        errorFieldBetween = in.readString();
        errorFieldMaxChar = in.readString();
        errorFieldRequired = in.readString();
        errorProductAvailableStock = in.readString();
        errorProductAvailableStockDetail = in.readString();
        errorProductMaxQuantity = in.readString();
        errorProductMinQuantity = in.readString();
        maxRemark = in.readInt();
        stateNotesOpen = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(recipientAddressModel, flags);
        dest.writeInt(cartPosition);
        dest.writeInt(addressPosition);
        dest.writeString(cartId);
        dest.writeInt(addressStatus);
        dest.writeString(parentId);
        dest.writeString(productId);
        dest.writeString(productWeightFmt);
        dest.writeInt(productRawWeight);
        dest.writeString(productQty);
        dest.writeString(productNotes);
        dest.writeInt(maxQuantity);
        dest.writeInt(minQuantity);
        dest.writeString(errorCheckoutPriceLimit);
        dest.writeString(errorFieldBetween);
        dest.writeString(errorFieldMaxChar);
        dest.writeString(errorFieldRequired);
        dest.writeString(errorProductAvailableStock);
        dest.writeString(errorProductAvailableStockDetail);
        dest.writeString(errorProductMaxQuantity);
        dest.writeString(errorProductMinQuantity);
        dest.writeInt(maxRemark);
        dest.writeByte((byte) (stateNotesOpen ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MultipleAddressItemData> CREATOR = new Creator<MultipleAddressItemData>() {
        @Override
        public MultipleAddressItemData createFromParcel(Parcel in) {
            return new MultipleAddressItemData(in);
        }

        @Override
        public MultipleAddressItemData[] newArray(int size) {
            return new MultipleAddressItemData[size];
        }
    };

    public void setCartPosition(int cartPosition) {
        this.cartPosition = cartPosition;
    }

    public int getCartPosition() {
        return cartPosition;
    }

    public int getAddressPosition() {
        return addressPosition;
    }

    public void setAddressPosition(int addressPosition) {
        this.addressPosition = addressPosition;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public int getAddressStatus() {
        return addressStatus;
    }

    public void setAddressStatus(int addressStatus) {
        this.addressStatus = addressStatus;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductWeightFmt() {
        return productWeightFmt;
    }

    public void setProductWeightFmt(String productWeight) {
        this.productWeightFmt = productWeight;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public String getProductNotes() {
        return productNotes;
    }

    public void setProductNotes(String productNotes) {
        this.productNotes = productNotes;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public int getProductRawWeight() {
        return productRawWeight;
    }

    public void setProductRawWeight(int productRawWeight) {
        this.productRawWeight = productRawWeight;
    }

    public String getErrorCheckoutPriceLimit() {
        return errorCheckoutPriceLimit;
    }

    public void setErrorCheckoutPriceLimit(String errorCheckoutPriceLimit) {
        this.errorCheckoutPriceLimit = errorCheckoutPriceLimit;
    }

    public String getErrorFieldBetween() {
        return errorFieldBetween;
    }

    public void setErrorFieldBetween(String errorFieldBetween) {
        this.errorFieldBetween = errorFieldBetween;
    }

    public String getErrorFieldMaxChar() {
        return errorFieldMaxChar;
    }

    public void setErrorFieldMaxChar(String errorFieldMaxChar) {
        this.errorFieldMaxChar = errorFieldMaxChar;
    }

    public String getErrorFieldRequired() {
        return errorFieldRequired;
    }

    public void setErrorFieldRequired(String errorFieldRequired) {
        this.errorFieldRequired = errorFieldRequired;
    }

    public String getErrorProductAvailableStock() {
        return errorProductAvailableStock;
    }

    public void setErrorProductAvailableStock(String errorProductAvailableStock) {
        this.errorProductAvailableStock = errorProductAvailableStock;
    }

    public String getErrorProductAvailableStockDetail() {
        return errorProductAvailableStockDetail;
    }

    public void setErrorProductAvailableStockDetail(String errorProductAvailableStockDetail) {
        this.errorProductAvailableStockDetail = errorProductAvailableStockDetail;
    }

    public String getErrorProductMaxQuantity() {
        return errorProductMaxQuantity;
    }

    public void setErrorProductMaxQuantity(String errorProductMaxQuantity) {
        this.errorProductMaxQuantity = errorProductMaxQuantity;
    }

    public String getErrorProductMinQuantity() {
        return errorProductMinQuantity;
    }

    public void setErrorProductMinQuantity(String errorProductMinQuantity) {
        this.errorProductMinQuantity = errorProductMinQuantity;
    }

    public int getMaxRemark() {
        return maxRemark;
    }

    public void setMaxRemark(int maxRemark) {
        this.maxRemark = maxRemark;
    }

    public boolean isStateNotesOpen() {
        return stateNotesOpen;
    }

    public void setStateNotesOpen(boolean stateNotesOpen) {
        this.stateNotesOpen = stateNotesOpen;
    }

    public RecipientAddressModel getRecipientAddressModel() {
        return recipientAddressModel;
    }

    public void setRecipientAddressModel(RecipientAddressModel recipientAddressModel) {
        this.recipientAddressModel = recipientAddressModel;
    }

}
