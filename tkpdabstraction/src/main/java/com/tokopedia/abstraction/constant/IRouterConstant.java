package com.tokopedia.abstraction.constant;

/**
 * @author anggaprasetiyo on 01/03/18.
 */

public interface IRouterConstant {

    interface LoyaltyModule {
        int LOYALTY_ACTIVITY_REQUEST_CODE = 12345;

        interface ExtraLoyaltyActivity {
            String EXTRA_COUPON_ACTIVE = "EXTRA_COUPON_ACTIVE";
            String EXTRA_PLATFORM = "EXTRA_PLATFORM";
            String EXTRA_PLATFORM_PAGE = "EXTRA_PLATFORM_PAGE";
            String EXTRA_CATEGORY = "EXTRA_CATEGORY";
            String EXTRA_CATEGORY_NAME = "EXTRA_CATEGORY_NAME";
            String EXTRA_ADDITIONAL_STRING_DATA = "EXTRA_ADDITIONAL_STRING_DATA";
            String EXTRA_SELECTED_TAB = "EXTRA_SELECTED_TAB";
            String EXTRA_CART_ID = "EXTRA_CART_ID";
            String EXTRA_PRODUCTID = "EXTRA_PRODUCTID";
            String EXTRA_CATEGORYID = "EXTRA_CATEGORYID";
            String EXTRA_TRAIN_RESERVATION_ID = "EXTRA_TRAIN_RESERVATION_ID";
            String EXTRA_TRAIN_RESERVATION_CODE = "EXTRA_TRAIN_RESERVATION_CODE";
            String EXTRA_ONE_CLICK_SHIPMENT = "EXTRA_ONE_CLICK_SHIPMENT";

            String MARKETPLACE_STRING = "marketplace";
            String PLATFORM_PAGE_MARKETPLACE_CART_LIST = "PLATFORM_PAGE_MARKETPLACE_CART_LIST";
            String PLATFORM_PAGE_MARKETPLACE_CART_SHIPMENT = "PLATFORM_PAGE_MARKETPLACE_CART_SHIPMENT";

            //            String MARKETPLACE_CART_LIST_STRING = "marketplace_cart_list";
//            String MARKETPLACE_CART_SHIPMENT_STRING = "marketplace_cart_shipment";
            String DIGITAL_STRING = "digital";
            String VOUCHER_CODE = "voucher_code";
            String VOUCHER_MESSAGE = "voucher_message";
            String VOUCHER_AMOUNT = "voucher_amount";
            String CATEGORY_ID = "category_id";
            String TYPE ="type";

            String COUPON_CODE = "coupon_code";
            String COUPON_MESSAGE = "coupon_message";
            String COUPON_AMOUNT = "coupon_amount";
            String COUPON_TITLE = "coupon_title";
            String VOUCHER_CASHBACK_AMOUNT = "VOUCHER_CASHBACK_AMOUNT";
            String VOUCHER_DISCOUNT_AMOUNT = "VOUCHER_DISCOUNT_AMOUNT";
            String COUPON_DISCOUNT_AMOUNT = "COUPON_DISCOUNT_AMOUNT";
            String COUPON_CASHBACK_AMOUNT = "COUPON_CASHBACK_AMOUNT";

            String FLIGHT_STRING = "flight";
            String EVENT_STRING = "events";
            String DEALS_STRING = "deals";
            String TRAIN_STRING = "train";
            String KAI = "kai";

            String COUPON_STATE = "coupon";
            int VOUCHER_TAB = 0;
            int COUPON_TAB = 1;


        }

        interface ResultLoyaltyActivity {
            int VOUCHER_RESULT_CODE = 12;
            int COUPON_RESULT_CODE = 15;
        }
    }
}
