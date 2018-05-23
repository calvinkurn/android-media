package com.tokopedia.checkout.view.holderitemdata;

import com.tokopedia.checkout.domain.datamodel.cartlist.AutoApplyData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.checkout.view.view.shipment.ShipmentData;

/**
 * @author anggaprasetiyo on 20/02/18.
 */

public class CartItemPromoHolderData implements ShipmentData {

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

    private boolean fromAutoApply;

    public int getTypePromo() {
        return typePromo;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public String getVoucherMessage() {
        return voucherMessage;
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

    public void setPromoVoucherType(
            String voucherCode, String voucherMessage, long voucherDiscountAmount
    ) {
        this.typePromo = TYPE_PROMO_VOUCHER;
        this.voucherMessage = voucherMessage;
        this.voucherCode = voucherCode;
        this.voucherDiscountAmount = voucherDiscountAmount;
    }

    public void setPromoVoucherTypeFromAutoApply(
            String voucherCode, String voucherMessage, long voucherDiscountAmount
    ) {
        this.typePromo = TYPE_PROMO_VOUCHER;
        this.voucherMessage = voucherMessage;
        this.voucherCode = voucherCode;
        this.voucherDiscountAmount = voucherDiscountAmount;
        this.fromAutoApply = true;
    }

    public void setPromoCouponType(
            String couponTitle, String couponCode, String couponMessage, long couponDiscountAmount
    ) {
        this.typePromo = TYPE_PROMO_COUPON;
        this.couponMessage = couponMessage;
        this.couponCode = couponCode;
        this.couponDiscountAmount = couponDiscountAmount;
        this.couponTitle = couponTitle;
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
