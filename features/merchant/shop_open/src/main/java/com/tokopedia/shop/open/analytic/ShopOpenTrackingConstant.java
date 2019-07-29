package com.tokopedia.shop.open.analytic;

/**
 * Created by zulfikarrahman on 1/8/18.
 */

public class ShopOpenTrackingConstant {
    //event
    public static String CLICK_CREATE_SHOP = "clickCreateShop";
    public static String EVENT_SHOP_CREATED = "shopCreated";

    //category
    public static String OPEN_SHOP_BIODATA_FORM = "open shop - shop biodata form";
    public static String OPEN_SHOP_DOMAIN_RESERVE = "open shop - domain reserve";
    public static String OPEN_SHOP_INFO_FORM = "open shop - shop info form";
    public static String OPEN_SHOP_SHIPPING_FORM = "open shop - shipping service form";
    public static String OPEN_SHOP_THANKS_PAGE = "open shop - thank you page";
    public static String OPEN_SHOP_SHOP_LOCATION_FORM = "open shop - shop location form";

    //action
    public static String OPEN_SHOP_SUCCESS = "click open shop success";
    public static String OPEN_SHOP_ERROR = "click open shop error ";
    public static String OPEN_SHOP_ERROR_WITH_DATA = "click open shop error with data";
    public static String OPEN_SHOP_ERROR_DOMAIN = "domain error";
    public static String OPEN_SHOP_ERROR_NAME = "name error";
    public static String OPEN_SHOP_CLICK_NEXT_STEP = "click next step";
    public static String OPEN_SHOP_CLICK_NEXT_STEP_SUCCESS = "click next step success";
    public static String OPEN_SHOP_CLICK_NEXT_STEP_ERROR = "click next step error";
    public static String OPEN_SHOP_CLICK_NEXT_STEP_ERROR_WITH_DATA = "click next step error with data";
    public static String OPEN_SHOP_CLICK_CARGO_SERVICES = "click cargo service rules";
    public static String OPEN_SHOP_CLICK_LEARN_MORE = "click learn more";
    public static String OPEN_SHOP_CLICK_ADD_PRODUCT = "click add product";
    public static String OPEN_SHOP_CLICK_MY_SHOP_PAGE = "click my shop page";
    public static String OPEN_SHOP_CLICK_ADDRESS_LIST = "click address list";
    public static String OPEN_SHOP_CLICK_PINPOINT_LOCATION = "click pinpoint location";
    public static String OPEN_SHOP_CLICK_DELETE_PINPOINT_LOCATION = "click delete pinpoint location";

    public static String EVENT_CLICK_OPEN_SHOP = "clickOpenShop";
    public static String CATEGORY_CLICK_OPEN_SHOP = "open shop page";
    public static String CATEGORY_CLICK_OPEN_SHOP_SUCCESS = "open shop - success";
    public static String ACTION_TNC = "click terms and condition";
    public static String ACTION_PRIVACY_POLICY = "click privacy";
    public static String ACTION_OPEN_SHOP_SUCCESS = "click open shop - success";
    public static String ACTION_ADD_PRODUCT_CLICK = "click Tambah Produk";
    public static String ACTION_ADD_LATER_CLICK = "click Nanti Saja";
    public static String ACTION_LINK = "click di sini";


    // app shortcut
    static String EVENT = "event";
    static String EVENT_CATEGORY = "eventCategory";
    static String EVENT_ACTION = "eventAction";
    static String EVENT_LABEL = "eventLabel";

    static String USER_ID = "userId";

    static String CLICK_SELL = "Click Jual";

    static String TAKE_TO_SHOP = "Take to Shop";

    static String LONG_CLICK = "longClick";
    static String LONG_PRESS = "Long Press";

    public interface Keys{
        String USERID = "user_id";
        String SHOPID = "shop_id";
        String USEREMAIL = "user_email";
        String PHONE = "phone";
    }
}
