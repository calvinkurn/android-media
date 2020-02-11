package com.tokopedia.digital_deals.view.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PackageViewModel implements Parcelable {

    private int id;
    private int productId;
    private int categoryId;
    private int brandId;
    private int convenienceFee;
    private int mrp;
    private int commission;
    private String commissionType;
    private int salesPrice;
    private int available;
    private int minQty;
    private int maxQty;
    private String providerStatus;
    private int selectedQuantity;
    private int digitalCategoryID;
    private int digitalProductID;
    private String digitalProductCode;


    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getConvenienceFee() {
        return convenienceFee;
    }

    public void setConvenienceFee(int convenienceFee) {
        this.convenienceFee = convenienceFee;
    }

    public int getMrp() {
        return mrp;
    }

    public void setMrp(int mrp) {
        this.mrp = mrp;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public int getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(int salesPrice) {
        this.salesPrice = salesPrice;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getMinQty() {
        return minQty;
    }

    public void setMinQty(int minQty) {
        this.minQty = minQty;
    }

    public int getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(int maxQty) {
        this.maxQty = maxQty;
    }


    public String getProviderStatus() {
        return providerStatus;
    }

    public void setProviderStatus(String providerStatus) {
        this.providerStatus = providerStatus;
    }


    public int getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public int getDigitalCategoryID() {
        return digitalCategoryID;
    }

    public void setDigitalCategoryID(int digitalCategoryID) {
        this.digitalCategoryID = digitalCategoryID;
    }

    public int getDigitalProductID() {
        return digitalProductID;
    }

    public void setDigitalProductID(int digitalProductID) {
        this.digitalProductID = digitalProductID;
    }

    public String getDigitalProductCode() {
        return digitalProductCode;
    }

    public void setDigitalProductCode(String digitalProductCode) {
        this.digitalProductCode = digitalProductCode;
    }


    public PackageViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.productId);
        dest.writeInt(this.convenienceFee);
        dest.writeInt(this.mrp);
        dest.writeInt(this.commission);
        dest.writeString(this.commissionType);
        dest.writeInt(this.salesPrice);
        dest.writeInt(this.available);
        dest.writeInt(this.minQty);
        dest.writeInt(this.maxQty);
        dest.writeString(this.providerStatus);
        dest.writeInt(this.selectedQuantity);
        dest.writeInt(this.digitalCategoryID);
        dest.writeInt(this.digitalProductID);
        dest.writeString(this.digitalProductCode);
        dest.writeInt(this.categoryId);
        dest.writeInt(this.brandId);
    }

    protected PackageViewModel(Parcel in) {
        this.id = in.readInt();
        this.productId = in.readInt();
        this.convenienceFee = in.readInt();
        this.mrp = in.readInt();
        this.commission = in.readInt();
        this.commissionType = in.readString();
        this.salesPrice = in.readInt();
        this.available = in.readInt();
        this.minQty = in.readInt();
        this.maxQty = in.readInt();
        this.providerStatus = in.readString();
        this.selectedQuantity = in.readInt();
        this.digitalCategoryID = in.readInt();
        this.digitalProductID = in.readInt();
        this.digitalProductCode = in.readString();
        this.categoryId = in.readInt();
        this.brandId = in.readInt();
    }

    public static final Creator<PackageViewModel> CREATOR = new Creator<PackageViewModel>() {
        @Override
        public PackageViewModel createFromParcel(Parcel source) {
            return new PackageViewModel(source);
        }

        @Override
        public PackageViewModel[] newArray(int size) {
            return new PackageViewModel[size];
        }
    };
}
