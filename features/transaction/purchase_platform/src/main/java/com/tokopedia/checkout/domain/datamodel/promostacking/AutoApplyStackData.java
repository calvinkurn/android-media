package com.tokopedia.checkout.domain.datamodel.promostacking;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class AutoApplyStackData implements Parcelable {

    private int promoCodeId;
    private List<VoucherOrdersItemData> voucherOrders = new ArrayList<>();
    private int discountAmount;
    private String titleDescription;
    private String stateGlobalPromo;
    private String titleGlobalPromo;
    private String descGlobalPromo;
    private String code;
    private int isCoupon;
    private boolean success;
    private String messageSuccess;
    private String state;

    public AutoApplyStackData() {
    }

    public int getPromoCodeId() {
        return promoCodeId;
    }

    public void setPromoCodeId(int promoCodeId) {
        this.promoCodeId = promoCodeId;
    }

    public List<VoucherOrdersItemData> getVoucherOrders() {
        return voucherOrders;
    }

    public void setVoucherOrders(List<VoucherOrdersItemData> voucherOrders) {
        this.voucherOrders = voucherOrders;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getTitleDescription() {
        return titleDescription;
    }

    public void setTitleDescription(String titleDescription) {
        this.titleDescription = titleDescription;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getStateGlobalPromo() {
        return stateGlobalPromo;
    }

    public void setStateGlobalPromo(String stateGlobalPromo) {
        this.stateGlobalPromo = stateGlobalPromo;
    }

    public String getTitleGlobalPromo() {
        return titleGlobalPromo;
    }

    public void setTitleGlobalPromo(String titleGlobalPromo) {
        this.titleGlobalPromo = titleGlobalPromo;
    }

    public String getDescGlobalPromo() {
        return descGlobalPromo;
    }

    public void setDescGlobalPromo(String descGlobalPromo) {
        this.descGlobalPromo = descGlobalPromo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getIsCoupon() {
        return isCoupon;
    }

    public void setIsCoupon(int isCoupon) {
        this.isCoupon = isCoupon;
    }

    public String getMessageSuccess() {
        return messageSuccess;
    }

    public void setMessageSuccess(String messageSuccess) {
        this.messageSuccess = messageSuccess;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.promoCodeId);
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
        dest.writeString(this.titleDescription);
        dest.writeString(this.titleGlobalPromo);
        dest.writeString(this.descGlobalPromo);
        dest.writeString(this.code);
        dest.writeInt(this.isCoupon);
        dest.writeString(this.messageSuccess);
        dest.writeString(this.state);
    }

    protected AutoApplyStackData(Parcel in) {
        this.success = in.readByte() != 0;
        this.titleDescription = in.readString();
        this.promoCodeId = in.readInt();
        this.discountAmount = in.readInt();
        this.titleGlobalPromo = in.readString();
        this.descGlobalPromo = in.readString();
        this.code = in.readString();
        this.isCoupon = in.readInt();
        this.messageSuccess = in.readString();
        this.state = in.readString();
    }

    public static final Creator<AutoApplyStackData> CREATOR = new Creator<AutoApplyStackData>() {
        @Override
        public AutoApplyStackData createFromParcel(Parcel source) {
            return new AutoApplyStackData(source);
        }

        @Override
        public AutoApplyStackData[] newArray(int size) {
            return new AutoApplyStackData[size];
        }
    };
}
