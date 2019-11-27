package com.tokopedia.purchase_platform.features.checkout.domain.model.cartsingleshipment;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.logisticcart.shipping.model.ShipmentData;

/**
 * @author Aghny A. Putra on 25/01/18
 */

public class ShipmentCostModel implements Parcelable, ShipmentData {

    private int totalItem;
    private double totalItemPrice;
    private double totalPrice;
    private double totalWeight;
    private double shippingFee;
    private double insuranceFee;
    private double priorityFee;
    private int totalPurchaseProtectionItem;
    private double purchaseProtectionFee;
    private double additionalFee;
    private double promoPrice;
    private double donation;
    private String promoMessage;
    private double emasPrice;
    private double tradeInPrice;
    private int totalPromoStackAmount;
    private String totalPromoStackAmountStr;
    private int TotalDiscWithoutCashback;
    private long macroInsurancePrice;
    private String macroInsurancePriceLabel;
    private int bookingFee;
    private String discountLabel;
    private int discountAmount;
    private boolean hasDiscountDetails;
    private String shippingDiscountLabel;
    private int shippingDiscountAmount;
    private String productDiscountLabel;
    private int productDiscountAmount;
    private String cashbackLabel;
    private int cashbackAmount;

    public ShipmentCostModel() {
    }

    protected ShipmentCostModel(Parcel in) {
        totalItem = in.readInt();
        totalItemPrice = in.readDouble();
        totalPrice = in.readDouble();
        totalWeight = in.readDouble();
        shippingFee = in.readDouble();
        insuranceFee = in.readDouble();
        priorityFee = in.readDouble();
        totalPurchaseProtectionItem = in.readInt();
        purchaseProtectionFee = in.readDouble();
        additionalFee = in.readDouble();
        promoPrice = in.readDouble();
        donation = in.readDouble();
        promoMessage = in.readString();
        emasPrice = in.readDouble();
        tradeInPrice = in.readDouble();
        totalPromoStackAmount = in.readInt();
        totalPromoStackAmountStr = in.readString();
        TotalDiscWithoutCashback = in.readInt();
        macroInsurancePrice = in.readLong();
        macroInsurancePriceLabel = in.readString();
        bookingFee = in.readInt();
        discountLabel = in.readString();
        discountAmount = in.readInt();
        hasDiscountDetails = in.readByte() != 0;
        shippingDiscountLabel = in.readString();
        shippingDiscountAmount = in.readInt();
        productDiscountLabel = in.readString();
        productDiscountAmount = in.readInt();
        cashbackLabel = in.readString();
        cashbackAmount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalItem);
        dest.writeDouble(totalItemPrice);
        dest.writeDouble(totalPrice);
        dest.writeDouble(totalWeight);
        dest.writeDouble(shippingFee);
        dest.writeDouble(insuranceFee);
        dest.writeDouble(priorityFee);
        dest.writeInt(totalPurchaseProtectionItem);
        dest.writeDouble(purchaseProtectionFee);
        dest.writeDouble(additionalFee);
        dest.writeDouble(promoPrice);
        dest.writeDouble(donation);
        dest.writeString(promoMessage);
        dest.writeDouble(emasPrice);
        dest.writeDouble(tradeInPrice);
        dest.writeInt(totalPromoStackAmount);
        dest.writeString(totalPromoStackAmountStr);
        dest.writeInt(TotalDiscWithoutCashback);
        dest.writeLong(macroInsurancePrice);
        dest.writeString(macroInsurancePriceLabel);
        dest.writeInt(bookingFee);
        dest.writeString(discountLabel);
        dest.writeInt(discountAmount);
        dest.writeByte((byte) (hasDiscountDetails ? 1 : 0));
        dest.writeString(shippingDiscountLabel);
        dest.writeInt(shippingDiscountAmount);
        dest.writeString(productDiscountLabel);
        dest.writeInt(productDiscountAmount);
        dest.writeString(cashbackLabel);
        dest.writeInt(cashbackAmount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShipmentCostModel> CREATOR = new Creator<ShipmentCostModel>() {
        @Override
        public ShipmentCostModel createFromParcel(Parcel in) {
            return new ShipmentCostModel(in);
        }

        @Override
        public ShipmentCostModel[] newArray(int size) {
            return new ShipmentCostModel[size];
        }
    };

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public double getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(double totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public double getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(double insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    public double getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(double promoPrice) {
        this.promoPrice = promoPrice;
    }

    public String getPromoMessage() {
        return promoMessage;
    }

    public void setPromoMessage(String promoMessage) {
        this.promoMessage = promoMessage;
    }

    public double getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(double additionalFee) {
        this.additionalFee = additionalFee;
    }

    public double getDonation() {
        return donation;
    }

    public void setDonation(double donation) {
        this.donation = donation;
    }

    public int getTotalPurchaseProtectionItem() {
        return totalPurchaseProtectionItem;
    }

    public double getPriorityFee() {
        return priorityFee;
    }

    public void setPriorityFee(double priorityFee) {
        this.priorityFee = priorityFee;
    }

    public void setTotalPurchaseProtectionItem(int totalPurchaseProtectionItem) {
        this.totalPurchaseProtectionItem = totalPurchaseProtectionItem;
    }

    public double getPurchaseProtectionFee() {
        return purchaseProtectionFee;
    }

    public void setPurchaseProtectionFee(double purchaseProtectionFee) {
        this.purchaseProtectionFee = purchaseProtectionFee;
    }

    public double getEmasPrice() {
        return emasPrice;
    }

    public void setEmasPrice(double emasPrice) {
        this.emasPrice = emasPrice;
    }

    public double getTradeInPrice() {
        return tradeInPrice;
    }

    public void setTradeInPrice(double tradeInPrice) {
        this.tradeInPrice = tradeInPrice;
    }

    public int getTotalPromoStackAmount() {
        return totalPromoStackAmount;
    }

    public void setTotalPromoStackAmount(int totalPromoStackAmount) {
        this.totalPromoStackAmount = totalPromoStackAmount;
    }

    public int getTotalDiscWithoutCashback() {
        return TotalDiscWithoutCashback;
    }

    public void setTotalDiscWithoutCashback(int totalDiscWithoutCashback) {
        TotalDiscWithoutCashback = totalDiscWithoutCashback;
    }

    public String getTotalPromoStackAmountStr() {
        return totalPromoStackAmountStr;
    }

    public void setTotalPromoStackAmountStr(String totalPromoStackAmountStr) {
        this.totalPromoStackAmountStr = totalPromoStackAmountStr;
    }

    public int getBookingFee() { return bookingFee; }

    public void setBookingFee(int bookingFee) { this.bookingFee = bookingFee; }

    public long getMacroInsurancePrice() {
        return macroInsurancePrice;
    }

    public void setMacroInsurancePrice(long macroInsurancePrice) {
        this.macroInsurancePrice = macroInsurancePrice;
    }

    public String getMacroInsurancePriceLabel() {
        return macroInsurancePriceLabel;
    }

    public void setMacroInsurancePriceLabel(String macroInsurancePriceLabel) {
        this.macroInsurancePriceLabel = macroInsurancePriceLabel;
    }

    public String getDiscountLabel() {
        return discountLabel;
    }

    public void setDiscountLabel(String discountLabel) {
        this.discountLabel = discountLabel;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public boolean isHasDiscountDetails() {
        return hasDiscountDetails;
    }

    public void setHasDiscountDetails(boolean hasDiscountDetails) {
        this.hasDiscountDetails = hasDiscountDetails;
    }

    public String getShippingDiscountLabel() {
        return shippingDiscountLabel;
    }

    public void setShippingDiscountLabel(String shippingDiscountLabel) {
        this.shippingDiscountLabel = shippingDiscountLabel;
    }

    public int getShippingDiscountAmount() {
        return shippingDiscountAmount;
    }

    public void setShippingDiscountAmount(int shippingDiscountAmount) {
        this.shippingDiscountAmount = shippingDiscountAmount;
    }

    public String getProductDiscountLabel() {
        return productDiscountLabel;
    }

    public void setProductDiscountLabel(String productDiscountLabel) {
        this.productDiscountLabel = productDiscountLabel;
    }

    public int getProductDiscountAmount() {
        return productDiscountAmount;
    }

    public void setProductDiscountAmount(int productDiscountAmount) {
        this.productDiscountAmount = productDiscountAmount;
    }

    public String getCashbackLabel() {
        return cashbackLabel;
    }

    public void setCashbackLabel(String cashbackLabel) {
        this.cashbackLabel = cashbackLabel;
    }

    public int getCashbackAmount() {
        return cashbackAmount;
    }

    public void setCashbackAmount(int cashbackAmount) {
        this.cashbackAmount = cashbackAmount;
    }

}
