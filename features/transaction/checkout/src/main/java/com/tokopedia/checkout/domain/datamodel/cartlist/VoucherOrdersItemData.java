package com.tokopedia.checkout.domain.datamodel.cartlist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fwidjaja on 12/03/19.
 */
public class VoucherOrdersItemData implements Parcelable {

    private String code;
    private boolean success;
    private String uniqueId;
    private int cartId;
    private int shopId;
    private int isPO;
    private int addressId;
    private String type;
    private int cashbackWalletAmount;
    private int discountAmount;
    private String invoiceDescription;
    private String state;
    private String messageText;
    private String variant;
    private String titleDescription;

    public VoucherOrdersItemData() {
    }

    public static final Creator<VoucherOrdersItemData> CREATOR = new Creator<VoucherOrdersItemData>() {
        @Override
        public VoucherOrdersItemData createFromParcel(Parcel in) {
            return new VoucherOrdersItemData(in);
        }

        @Override
        public VoucherOrdersItemData[] newArray(int size) {
            return new VoucherOrdersItemData[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getIsPO() {
        return isPO;
    }

    public void setIsPO(int isPO) {
        this.isPO = isPO;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCashbackWalletAmount() {
        return cashbackWalletAmount;
    }

    public void setCashbackWalletAmount(int cashbackWalletAmount) {
        this.cashbackWalletAmount = cashbackWalletAmount;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getInvoiceDescription() {
        return invoiceDescription;
    }

    public void setInvoiceDescription(String invoiceDescription) {
        this.invoiceDescription = invoiceDescription;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getVariant() { return variant; }

    public void setVariant(String variant) { this.variant = variant; }

    public String getTitleDescription() {
        return titleDescription;
    }

    public void setTitleDescription(String titleDescription) {
        this.titleDescription = titleDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeString(uniqueId);
        dest.writeInt(cartId);
        dest.writeInt(shopId);
        dest.writeInt((isPO));
        dest.writeInt(addressId);
        dest.writeString(type);
        dest.writeInt(cashbackWalletAmount);
        dest.writeInt(discountAmount);
        dest.writeString(invoiceDescription);
        dest.writeString(state);
        dest.writeString(messageText);
        dest.writeString(variant);
        dest.writeString(titleDescription);
    }

    protected VoucherOrdersItemData(Parcel in) {
        code = in.readString();
        success = in.readByte() != 0;
        uniqueId = in.readString();
        cartId = in.readInt();
        shopId = in.readInt();
        isPO = in.readInt();
        addressId = in.readInt();
        type = in.readString();
        cashbackWalletAmount = in.readInt();
        discountAmount = in.readInt();
        invoiceDescription = in.readString();
        state = in.readString();
        messageText = in.readString();
        variant = in.readString();
        titleDescription = in.readString();
    }
}
