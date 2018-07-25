package com.tokopedia.checkout.domain.datamodel.cartsingleshipment;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author Aghny A. Putra on 25/01/18
 */
public class CartItemModel implements Parcelable {

    private long cartId;
    private String shopId;
    private String shopName;
    private int productId;
    private String name;
    private double price;
    private int currency;

    private int weightUnit;
    private double weight;
    private String weightFmt;

    private int quantity;
    private String noteToSeller;

    private String imageUrl;

    private String cashback;
    private String freeReturnLogo;

    private boolean isCashback;
    private boolean isPreOrder;
    private boolean isFreeReturn;

    private boolean fInsurance;
    private boolean fCancelPartial;

    private boolean isError;
    private String errorMessage;

    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public int getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(int weightUnit) {
        this.weightUnit = weightUnit;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getWeightFmt() {
        return weightFmt;
    }

    public void setWeightFmt(String weightFmt) {
        this.weightFmt = weightFmt;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNoteToSeller() {
        return noteToSeller;
    }

    public void setNoteToSeller(String noteToSeller) {
        this.noteToSeller = noteToSeller;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public String getFreeReturnLogo() {
        return freeReturnLogo;
    }

    public void setFreeReturnLogo(String freeReturnLogo) {
        this.freeReturnLogo = freeReturnLogo;
    }

    public boolean isCashback() {
        return isCashback;
    }

    public void setCashback(boolean cashback) {
        isCashback = cashback;
    }

    public boolean isPreOrder() {
        return isPreOrder;
    }

    public void setPreOrder(boolean preOrder) {
        isPreOrder = preOrder;
    }

    public boolean isFreeReturn() {
        return isFreeReturn;
    }

    public void setFreeReturn(boolean freeReturn) {
        isFreeReturn = freeReturn;
    }

    public boolean isfInsurance() {
        return fInsurance;
    }

    public void setfInsurance(boolean fInsurance) {
        this.fInsurance = fInsurance;
    }

    public boolean isfCancelPartial() {
        return fCancelPartial;
    }

    public void setfCancelPartial(boolean fCancelPartial) {
        this.fCancelPartial = fCancelPartial;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.cartId);
        dest.writeString(this.shopId);
        dest.writeString(this.shopName);
        dest.writeInt(this.productId);
        dest.writeString(this.name);
        dest.writeDouble(this.price);
        dest.writeInt(this.currency);
        dest.writeInt(this.weightUnit);
        dest.writeDouble(this.weight);
        dest.writeString(this.weightFmt);
        dest.writeInt(this.quantity);
        dest.writeString(this.noteToSeller);
        dest.writeString(this.imageUrl);
        dest.writeString(this.cashback);
        dest.writeString(this.freeReturnLogo);
        dest.writeByte(this.isCashback ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isPreOrder ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isFreeReturn ? (byte) 1 : (byte) 0);
        dest.writeByte(this.fInsurance ? (byte) 1 : (byte) 0);
        dest.writeByte(this.fCancelPartial ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isError ? (byte) 1 : (byte) 0);
        dest.writeString(this.errorMessage);
    }

    public CartItemModel() {
    }

    protected CartItemModel(Parcel in) {
        this.cartId = in.readLong();
        this.shopId = in.readString();
        this.shopName = in.readString();
        this.productId = in.readInt();
        this.name = in.readString();
        this.price = in.readDouble();
        this.currency = in.readInt();
        this.weightUnit = in.readInt();
        this.weight = in.readDouble();
        this.weightFmt = in.readString();
        this.quantity = in.readInt();
        this.noteToSeller = in.readString();
        this.imageUrl = in.readString();
        this.cashback = in.readString();
        this.freeReturnLogo = in.readString();
        this.isCashback = in.readByte() != 0;
        this.isPreOrder = in.readByte() != 0;
        this.isFreeReturn = in.readByte() != 0;
        this.fInsurance = in.readByte() != 0;
        this.fCancelPartial = in.readByte() != 0;
        this.isError = in.readByte() != 0;
        this.errorMessage = in.readString();
    }

    public static final Creator<CartItemModel> CREATOR = new Creator<CartItemModel>() {
        @Override
        public CartItemModel createFromParcel(Parcel source) {
            return new CartItemModel(source);
        }

        @Override
        public CartItemModel[] newArray(int size) {
            return new CartItemModel[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof CartItemModel)) return false;

        CartItemModel that = (CartItemModel) o;

        return new EqualsBuilder()
                .append(getCartId(), that.getCartId())
                .append(getProductId(), that.getProductId())
                .append(getPrice(), that.getPrice())
                .append(getCurrency(), that.getCurrency())
                .append(getWeightUnit(), that.getWeightUnit())
                .append(getWeight(), that.getWeight())
                .append(getQuantity(), that.getQuantity())
                .append(isCashback(), that.isCashback())
                .append(isPreOrder(), that.isPreOrder())
                .append(isFreeReturn(), that.isFreeReturn())
                .append(isfInsurance(), that.isfInsurance())
                .append(isfCancelPartial(), that.isfCancelPartial())
                .append(isError(), that.isError())
                .append(getShopId(), that.getShopId())
                .append(getShopName(), that.getShopName())
                .append(getName(), that.getName())
                .append(getWeightFmt(), that.getWeightFmt())
                .append(getNoteToSeller(), that.getNoteToSeller())
                .append(getImageUrl(), that.getImageUrl())
                .append(isCashback(), that.isCashback())
                .append(getFreeReturnLogo(), that.getFreeReturnLogo())
                .append(getErrorMessage(), that.getErrorMessage())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getCartId())
                .append(getShopId())
                .append(getShopName())
                .append(getProductId())
                .append(getName())
                .append(getPrice())
                .append(getCurrency())
                .append(getWeightUnit())
                .append(getWeight())
                .append(getWeightFmt())
                .append(getQuantity())
                .append(getNoteToSeller())
                .append(getImageUrl())
                .append(isCashback())
                .append(getFreeReturnLogo())
                .append(isCashback())
                .append(isPreOrder())
                .append(isFreeReturn())
                .append(isfInsurance())
                .append(isfCancelPartial())
                .append(isError())
                .append(getErrorMessage())
                .toHashCode();
    }
}
