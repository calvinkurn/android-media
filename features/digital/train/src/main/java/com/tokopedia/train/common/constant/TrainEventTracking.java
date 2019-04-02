package com.tokopedia.train.common.constant;

/**
 * Created by Rizky on 12/07/18.
 */
public interface TrainEventTracking {

    String EVENT_CATEGORY = "eventCategory";
    String EVENT_ACTION = "eventAction";
    String EVENT_LABEL = "eventLabel";
    String EVENT = "event";
    String ECOMMERCE = "ecommerce";

    interface Event {
        String GENERIC_TRAIN_EVENT = "genericTrainEvent";
        String PROMO_CLICK = "promoCLick";
        String PRODUCT_VIEW = "productView";
        String PRODUCT_CLICK = "productClick";
        String VIEW_PRODUCT = "viewProduct";
        String ADD_TO_CART = "addToCart";
        String KAI_CLICK = "digitalKAIClick";
    }

    interface Category {
        String DIGITAL_TRAIN = "digital - tiket kereta api";
    }

    interface Action {
        String CHOOSE_SINGLE_TRIP = "choose single trip";
        String CHOOSE_ROUND_TRIP = "choose round trip";
        String CLICK_FIND_TICKET = "click find ticket";
        String CHECK_ORDER = "check order";
        String CLICK_PROMO_LIST = "click promo list";
        String CLICK_HELP = "click help";
        String CLICK_TRANSACTION_LIST = "click transaction list";
        String CLICK_PASSENGER_LIST = "click passenger list";
        String CHECK_MY_TICKET = "check my ticket";
        String PRODUCT_IMPRESSIONS = "product impressions";
        String PRODUCT_CLICK = "product click";
        String CLICK_CHANGE_DATE_ON_BOTTOM_BAR = "click change date on bottom bar";
        String CLICK_SORT_ON_BOTTOM_BAR = "click sort on bottom bar";
        String CLICK_FILTER_ON_BOTTOM_BAR = "click filter on bottom bar";
        String VIEW_ROUTE_NOT_AVAILABLE_PAGE = "view route not available page";
        String PRODUCT_DETAIL_IMPRESSIONS = "product detail impressions";
        String ADD_TO_CART = "add to cart";
        String CLICK_DETAIL = "click detail";
        String CLICK_NEXT_ON_CUSTOMERS_PAGE = "click next on customers page";
        String CLICK_USE_VOUCHER_CODE = "click gunakan voucher code";
        String VOUCHER_SUCCESS = "voucher success";
        String VOUCHER_ERROR = "voucher error";
        String PROCEED_TO_PAYMENT = "click proceed to payment";
        String VIEW_MY_TICKET_PAGE = "view my ticket page";
        String CLICK_MY_TICKET = "click my ticket";
    }

    interface Label {

    }

    interface EnhanceEcommerce {
        String NAME = "name";
        String ID = "id";
        String PRICE = "price";
        String CATEGORY = "category";
        String VARIANT = "variant";
        String QUANTITY = "quantity";
        String COUPON = "coupon";
        String LIST = "list";
        String BRAND = "brand";
    }

}
