package com.tokopedia.tkpd.flight;

import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.flight.review.domain.FlightVoucherCodeWrapper;

/**
 * @author  by alvarisi on 4/3/18.
 */

public class FlightVoucherCodeWrapperImpl implements FlightVoucherCodeWrapper {
    public FlightVoucherCodeWrapperImpl() {
    }

    @Override
    public int voucherResultCode() {
        return IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.VOUCHER_RESULT_CODE;
    }

    @Override
    public String voucherCode() {
        return IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_CODE;
    }

    @Override
    public String voucherMessage() {
        return IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_MESSAGE;
    }

    @Override
    public String voucherDiscountAmount() {
        return IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_DISCOUNT_AMOUNT;
    }

    @Override
    public int couponResultCode() {
        return IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.COUPON_RESULT_CODE;
    }

    @Override
    public String couponCode() {
        return IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_CODE;
    }

    @Override
    public String couponTitle() {
        return IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_TITLE;
    }

    @Override
    public String couponMessage() {
        return IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_MESSAGE;
    }

    @Override
    public String couponDiscountAmount() {
        return IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_DISCOUNT_AMOUNT;
    }
}
