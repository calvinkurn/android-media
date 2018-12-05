package com.tokopedia.checkout.view.common.holderitemdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.checkout.domain.datamodel.cartlist.AutoApplyData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.ShipmentData;

/**
 * @author anggaprasetiyo on 20/02/18.
 */

public class CartItemPromoHolderData implements ShipmentData, Parcelable {

    public static final int TYPE_PROMO_NOT_ACTIVE = 0;
    public static final int TYPE_PROMO_VOUCHER = 1;
    public static final int TYPE_PROMO_COUPON = 2;

    private int typePromo;

    private String voucherCode;
    private String voucherMessage;
    private long voucherDiscountAmount;

    private String couponTitle;
    private String couponMessage;
    private String couponCode;
    private long couponDiscountAmount;

    private String defaultSelectedTabString;
    private boolean fromAutoApply;
    private boolean visible;

    public CartItemPromoHolderData() {
    }

    protected CartItemPromoHolderData(Parcel in) {
        typePromo = in.readInt();
        voucherCode = in.readString();
        voucherMessage = in.readString();
        voucherDiscountAmount = in.readLong();
        couponTitle = in.readString();
        couponMessage = in.readString();
        couponCode = in.readString();
        couponDiscountAmount = in.readLong();
        defaultSelectedTabString = in.readString();
        fromAutoApply = in.readByte() != 0;
        visible = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(typePromo);
        dest.writeString(voucherCode);
        dest.writeString(voucherMessage);
        dest.writeLong(voucherDiscountAmount);
        dest.writeString(couponTitle);
        dest.writeString(couponMessage);
        dest.writeString(couponCode);
        dest.writeLong(couponDiscountAmount);
        dest.writeString(defaultSelectedTabString);
        dest.writeByte((byte) (fromAutoApply ? 1 : 0));
        dest.writeByte((byte) (visible ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartItemPromoHolderData> CREATOR = new Creator<CartItemPromoHolderData>() {
        @Override
        public CartItemPromoHolderData createFromParcel(Parcel in) {
            return new CartItemPromoHolderData(in);
        }

        @Override
        public CartItemPromoHolderData[] newArray(int size) {
            return new CartItemPromoHolderData[size];
        }
    };

    public int getTypePromo() {
        return typePromo;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public String getVoucherMessage() {
        return voucherMessage;
    }

    public String getDefaultSelectedTabString() {
        return defaultSelectedTabString;
    }

    public void setDefaultSelectedTabString(String defaultSelectedTabString) {
        this.defaultSelectedTabString = defaultSelectedTabString;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public String getCouponMessage() {
        return couponMessage;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public long getVoucherDiscountAmount() {
        return voucherDiscountAmount;
    }

    public long getCouponDiscountAmount() {
        return couponDiscountAmount;
    }

    public boolean isFromAutoApply() {
        return fromAutoApply;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setPromoVoucherType(
            String voucherCode, String voucherMessage, long voucherDiscountAmount
    ) {
        this.typePromo = TYPE_PROMO_VOUCHER;
        this.voucherMessage = voucherMessage;
        this.voucherCode = voucherCode;
        this.voucherDiscountAmount = voucherDiscountAmount;
        this.visible = true;
    }

    public void setPromoVoucherTypeFromAutoApply(
            String voucherCode, String voucherMessage, long voucherDiscountAmount
    ) {
        this.typePromo = TYPE_PROMO_VOUCHER;
        this.voucherMessage = voucherMessage;
        this.voucherCode = voucherCode;
        this.voucherDiscountAmount = voucherDiscountAmount;
        this.fromAutoApply = true;
        this.visible = true;
    }

    public void setPromoCouponType(
            String couponTitle, String couponCode, String couponMessage, long couponDiscountAmount
    ) {
        this.typePromo = TYPE_PROMO_COUPON;
        this.couponMessage = couponMessage;
        this.couponCode = couponCode;
        this.couponDiscountAmount = couponDiscountAmount;
        this.couponTitle = couponTitle;
        this.visible = true;
    }

    public void setPromoCouponTypeFromAutoApply(
            String couponTitle, String couponCode, String couponMessage, long couponDiscountAmount
    ) {
        this.typePromo = TYPE_PROMO_COUPON;
        this.couponMessage = couponMessage;
        this.couponCode = couponCode;
        this.couponDiscountAmount = couponDiscountAmount;
        this.couponTitle = couponTitle;
        this.fromAutoApply = true;
        this.visible = true;
    }

    public void setPromoNotActive() {
        this.typePromo = TYPE_PROMO_NOT_ACTIVE;
        this.voucherMessage = "";
        this.voucherCode = "";
        this.voucherDiscountAmount = 0;
        this.couponMessage = "";
        this.couponCode = "";
        this.couponDiscountAmount = 0;
        this.couponTitle = "";
        this.defaultSelectedTabString = "";
        this.visible = true;
    }

    public static CartItemPromoHolderData createInstanceFromAppliedPromo(
            PromoCodeAppliedData promoCodeAppliedData
    ) {
        CartItemPromoHolderData data = new CartItemPromoHolderData();
        if (promoCodeAppliedData == null) {
            data.setPromoNotActive();
        } else if (promoCodeAppliedData.isFromAutoApply()) {
            switch (promoCodeAppliedData.getTypeVoucher()) {
                case PromoCodeAppliedData.TYPE_COUPON:
                    data.setPromoCouponTypeFromAutoApply(
                            promoCodeAppliedData.getCouponTitle(),
                            promoCodeAppliedData.getPromoCode(),
                            promoCodeAppliedData.getDescription(),
                            promoCodeAppliedData.getAmount()
                    );
                    break;
                case PromoCodeAppliedData.TYPE_VOUCHER:
                    data.setPromoVoucherTypeFromAutoApply(
                            promoCodeAppliedData.getPromoCode(),
                            promoCodeAppliedData.getDescription(),
                            promoCodeAppliedData.getAmount()
                    );
                    break;
            }
        } else {
            switch (promoCodeAppliedData.getTypeVoucher()) {
                case PromoCodeAppliedData.TYPE_COUPON:
                    data.setPromoCouponType(
                            promoCodeAppliedData.getCouponTitle(),
                            promoCodeAppliedData.getPromoCode(),
                            promoCodeAppliedData.getDescription(),
                            promoCodeAppliedData.getAmount()
                    );
                    break;
                case PromoCodeAppliedData.TYPE_VOUCHER:
                    data.setPromoVoucherType(
                            promoCodeAppliedData.getPromoCode(),
                            promoCodeAppliedData.getDescription(),
                            promoCodeAppliedData.getAmount());
                    break;
            }
        }
        return data;
    }

    public static CartItemPromoHolderData createInstanceFromAutoApply(AutoApplyData autoApplyData) {
        CartItemPromoHolderData data = new CartItemPromoHolderData();
        if (autoApplyData.getIsCoupon() == 1) {
            data.setPromoCouponTypeFromAutoApply(
                    autoApplyData.getTitleDescription(),
                    autoApplyData.getCode(),
                    autoApplyData.getMessageSuccess(),
                    autoApplyData.getDiscountAmount()
            );
        } else {
            data.setPromoVoucherType(
                    autoApplyData.getCode(),
                    autoApplyData.getMessageSuccess(),
                    autoApplyData.getDiscountAmount());
        }

        return data;
    }
}
