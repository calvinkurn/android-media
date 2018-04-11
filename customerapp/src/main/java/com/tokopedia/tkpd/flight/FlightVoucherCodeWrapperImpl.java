package com.tokopedia.tkpd.flight;

import com.tokopedia.flight.review.domain.FlightVoucherCodeWrapper;
import com.tokopedia.loyalty.view.activity.LoyaltyActivity;

/**
 * @author  by alvarisi on 4/3/18.
 */

public class FlightVoucherCodeWrapperImpl implements FlightVoucherCodeWrapper {
    public FlightVoucherCodeWrapperImpl() {
    }

    @Override
    public int voucherResultCode() {
        return LoyaltyActivity.VOUCHER_RESULT_CODE;
    }

    @Override
    public String voucherCode() {
        return LoyaltyActivity.VOUCHER_CODE;
    }

    @Override
    public String voucherMessage() {
        return LoyaltyActivity.VOUCHER_MESSAGE;
    }

    @Override
    public String voucherDiscountAmount() {
        return LoyaltyActivity.VOUCHER_DISCOUNT_AMOUNT;
    }

    @Override
    public int couponResultCode() {
        return LoyaltyActivity.COUPON_RESULT_CODE;
    }

    @Override
    public String couponCode() {
        return LoyaltyActivity.COUPON_CODE;
    }

    @Override
    public String couponTitle() {
        return LoyaltyActivity.COUPON_TITLE;
    }

    @Override
    public String couponMessage() {
        return LoyaltyActivity.COUPON_MESSAGE;
    }

    @Override
    public String couponDiscountAmount() {
        return LoyaltyActivity.COUPON_DISCOUNT_AMOUNT;
    }
}
