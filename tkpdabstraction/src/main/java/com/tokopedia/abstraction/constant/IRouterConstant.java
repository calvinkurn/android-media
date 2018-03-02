package com.tokopedia.abstraction.constant;

/**
 * @author anggaprasetiyo on 01/03/18.
 */

public interface IRouterConstant {

    interface LoyaltyModule {
        int LOYALTY_ACTIVITY_REQUEST_CODE = LoyaltyModule.class.hashCode();

        interface ExtraLoyaltyActivity {
            String EXTRA_COUPON_ACTIVE = "EXTRA_COUPON_ACTIVE";
            String EXTRA_PLATFORM = "EXTRA_PLATFORM";
            String EXTRA_ADDITIONAL_DATA = "EXTRA_ADDITIONAL_DATA";
            String EXTRA_CATEGORY = "EXTRA_CATEGORY";


            String MARKETPLACE_STRING = "marketplace";
            String MARKETPLACE_CART_LIST_STRING = "marketplace_cart_list";
            String MARKETPLACE_CART_SHIPMENT_STRING = "marketplace_cart_shipment";
            String DIGITAL_STRING = "digital";
            String VOUCHER_CODE = "voucher_code";
            String VOUCHER_MESSAGE = "voucher_message";
            String VOUCHER_AMOUNT = "voucher_amount";

            String COUPON_CODE = "coupon_code";
            String COUPON_MESSAGE = "coupon_message";
            String COUPON_AMOUNT = "coupon_amount";
            String COUPON_TITLE = "coupon_title";
            String VOUCHER_CASHBACK_AMOUNT = "VOUCHER_CASHBACK_AMOUNT";
            String VOUCHER_DISCOUNT_AMOUNT = "VOUCHER_DISCOUNT_AMOUNT";
            String COUPON_DISCOUNT_AMOUNT = "COUPON_DISCOUNT_AMOUNT";
            String COUPON_CASHBACK_AMOUNT = "COUPON_CASHBACK_AMOUNT";
        }

        interface ResultLoyaltyActivity {
            int VOUCHER_RESULT_CODE = 12;
            int COUPON_RESULT_CODE = 15;
        }
    }
}
