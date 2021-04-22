package com.tokopedia.checkout.domain.model.cartshipmentform;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.logisticcart.shipping.model.ShopShipment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class GroupShop implements Parcelable {
    private boolean isError;
    private String errorMessage;

    private Shop shop;
    private List<ShopShipment> shopShipments = new ArrayList<>();
    private List<Product> products = new ArrayList<>();

    private int shippingId;
    private int spId;
    private String dropshipperName;
    private String dropshipperPhone;
    private boolean useInsurance;
    private String cartString;
    private boolean hasPromoList;
    private boolean saveStateFlag;

    private boolean isFulfillment;
    private int fulfillmentId;
    private String fulfillmentName;
    private String fulfillmentBadgeUrl;

    private boolean isLeasingProduct;
    private int bookingFee;
    private List<String> listPromoCodes;

    private ShipmentInformationData shipmentInformationData;

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<ShopShipment> getShopShipments() {
        return shopShipments;
    }

    public void setShopShipments(List<ShopShipment> shopShipments) {
        this.shopShipments = shopShipments;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
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

    public int getShippingId() {
        return shippingId;
    }

    public void setShippingId(int shippingId) {
        this.shippingId = shippingId;
    }

    public int getSpId() {
        return spId;
    }

    public void setSpId(int spId) {
        this.spId = spId;
    }

    public String getDropshipperName() {
        return dropshipperName;
    }

    public void setDropshipperName(String dropshipperName) {
        this.dropshipperName = dropshipperName;
    }

    public String getDropshipperPhone() {
        return dropshipperPhone;
    }

    public void setDropshipperPhone(String dropshipperPhone) {
        this.dropshipperPhone = dropshipperPhone;
    }

    public boolean isUseInsurance() {
        return useInsurance;
    }

    public void setUseInsurance(boolean useInsurance) {
        this.useInsurance = useInsurance;
    }

    public boolean isFulfillment() {
        return isFulfillment;
    }

    public void setFulfillment(boolean fulfillment) {
        isFulfillment = fulfillment;
    }

    public int getFulfillmentId() {
        return fulfillmentId;
    }

    public void setFulfillmentId(int fulfillmentId) {
        this.fulfillmentId = fulfillmentId;
    }

    public String getFulfillmentName() {
        return fulfillmentName;
    }

    public void setFulfillmentName(String fulfillmentName) {
        this.fulfillmentName = fulfillmentName;
    }

    public String getFulfillmentBadgeUrl() {
        return fulfillmentBadgeUrl;
    }

    public void setFulfillmentBadgeUrl(String fulfillmentBadgeUrl) {
        this.fulfillmentBadgeUrl = fulfillmentBadgeUrl;
    }

    public String getCartString() { return cartString; }

    public void setCartString(String cartString) { this.cartString = cartString; }

    public boolean isHasPromoList() { return hasPromoList; }

    public void setHasPromoList(boolean hasPromoList) { this.hasPromoList = hasPromoList; }

    public GroupShop() {
    }

    public boolean isSaveStateFlag() {
        return saveStateFlag;
    }

    public void setSaveStateFlag(boolean saveStateFlag) {
        this.saveStateFlag = saveStateFlag;
    }

    public boolean getIsLeasingProduct() { return isLeasingProduct; }

    public void setIsLeasingProduct(boolean leasingProduct) { isLeasingProduct = leasingProduct; }

    public int getBookingFee() { return bookingFee; }

    public void setBookingFee(int bookingFee) { this.bookingFee = bookingFee; }

    public List<String> getListPromoCodes() { return listPromoCodes; }

    public void setListPromoCodes(List<String> listPromoCodes) { this.listPromoCodes = listPromoCodes; }

    public ShipmentInformationData getShipmentInformationData() {
        return shipmentInformationData;
    }

    public void setShipmentInformationData(ShipmentInformationData shipmentInformationData) {
        this.shipmentInformationData = shipmentInformationData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isError ? (byte) 1 : (byte) 0);
        dest.writeString(this.errorMessage);
        dest.writeParcelable(this.shop, flags);
        dest.writeTypedList(this.shopShipments);
        dest.writeTypedList(this.products);
        dest.writeInt(this.shippingId);
        dest.writeInt(this.spId);
        dest.writeString(this.dropshipperName);
        dest.writeString(this.dropshipperPhone);
        dest.writeByte(this.useInsurance ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isFulfillment ? (byte) 1 : (byte) 0);
        dest.writeInt(this.fulfillmentId);
        dest.writeString(this.fulfillmentName);
        dest.writeString(this.fulfillmentBadgeUrl);
        dest.writeString(cartString);
        dest.writeByte(this.hasPromoList ? (byte) 1 : (byte) 0);
        dest.writeByte(this.saveStateFlag ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isLeasingProduct ? (byte) 1 : (byte) 0);
        dest.writeInt(this.bookingFee);
        dest.writeStringList(this.listPromoCodes);
        dest.writeParcelable(this.shipmentInformationData, flags);
    }

    protected GroupShop(Parcel in) {
        this.isError = in.readByte() != 0;
        this.errorMessage = in.readString();
        this.shop = in.readParcelable(Shop.class.getClassLoader());
        this.shopShipments = in.createTypedArrayList(ShopShipment.CREATOR);
        this.products = in.createTypedArrayList(Product.CREATOR);
        this.shippingId = in.readInt();
        this.spId = in.readInt();
        this.dropshipperName = in.readString();
        this.dropshipperPhone = in.readString();
        this.useInsurance = in.readByte() != 0;
        this.isFulfillment = in.readByte() != 0;
        this.fulfillmentId = in.readInt();
        this.fulfillmentName = in.readString();
        this.fulfillmentBadgeUrl = in.readString();
        this.cartString = in.readString();
        this.hasPromoList = in.readByte() != 0;
        this.saveStateFlag = in.readByte() != 0;
        this.isLeasingProduct = in.readByte() != 0;
        this.bookingFee = in.readInt();
        this.listPromoCodes = in.createStringArrayList();
        this.shipmentInformationData = in.readParcelable(ShipmentInformationData.class.getClassLoader());
    }

    public static final Creator<GroupShop> CREATOR = new Creator<GroupShop>() {
        @Override
        public GroupShop createFromParcel(Parcel source) {
            return new GroupShop(source);
        }

        @Override
        public GroupShop[] newArray(int size) {
            return new GroupShop[size];
        }
    };
}
