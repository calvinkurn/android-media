package com.tokopedia.applink;

/**
 * @author ricoharisin .
 * <p>
 * Central interface for AppLink in apps, do not use any new applink module
 */

public interface ApplinkConst {
    String APPLINK_CUSTOMER_SCHEME = "tokopedia";
    String HOME = "tokopedia://home";
    String HOME_FEED = "tokopedia://home/feed";
    String HOME_ACCOUNT = "tokopedia://home/account";
    String HOME_RECOMMENDATION = "tokopedia://home/recommendation";
    String FEED = "tokopedia://feed";
    String FEED_DETAILS = "tokopedia://feedcommunicationdetail/{extra_detail_id}";
    String HOME_CATEGORY = "tokopedia://home/category";
    String HOME_HOTLIST = "tokopedia://hot";
    String MESSAGE = "tokopedia://message";
    String MESSAGE_DETAIL = "tokopedia://message/{message_id}";
    String TALK = "tokopedia://talk";
    String TALK_DETAIL = "tokopedia://talk/{talk_id}";
    String RIDE = "tokopedia://ride/uber";
    String RIDE_DETAIL = "tokopedia://ride/uber/{request_id}";
    String SHOP = "tokopedia://shop/{shop_id}";
    String SHOP_ETALASE = "tokopedia://shop/{shop_id}/etalase/{etalase_id}";
    String SHOP_TALK = "tokopedia://shop/{shop_id}/talk";
    String SHOP_ETALASE_WITH_KEYWORD_AND_SORT = "tokopedia://shop/{shop_id}/etalase/{etalase_id}/?search={search}&sort={sort}";
    String SHOP_REVIEW = "tokopedia://shop/{shop_id}/review";
    String SHOP_NOTE = "tokopedia://shop/{shop_id}/note";
    String SHOP_INFO = "tokopedia://shop/{shop_id}/info";
    String PRODUCT_INFO = "tokopedia://product/{product_id}";
    String PRODUCT_IMAGE_REVIEW = "tokopedia://product/{product_id}/imagereview";
    String PRODUCT_ADD = "tokopedia://product/add";
    String CREDIT_CARD_AUTH_SETTING = "tokopedia://payment/credit-card";
    String ADD_CREDIT_CARD = "tokopedia://payment/credit-card/add";
    String CART = "tokopedia://cart";
    String SELLER_NEW_ORDER = "tokopedia://seller/new-order";
    String SELLER_SHIPMENT = "tokopedia://seller/shipment";
    String SELLER_STATUS = "tokopedia://seller/status";
    String SELLER_HISTORY = "tokopedia://seller/history";
    String CREATE_SHOP = "tokopedia://buka-toko-online-gratis";
    String REPUTATION = "tokopedia://review";
    String PRODUCT_REPUTATION = "tokopedia://product/{product_id}/review";
    String WEBVIEW = "tokopedia://webview";
    String WEBVIEW_PARENT_HOME = "tokopedia://webviewbackhome";
    String PRODUCT_TALK = "tokopedia://product/{product_id}/talk";
    String DIGITAL = "tokopedia://digital";
    String DIGITAL_PRODUCT = "tokopedia://digital/form";
    String DIGITAL_SMARTCARD = "tokopedia://digital/smartcard";
    String DIGITAL_CART = "tokopedia://digital/cart";
    String DIGITAL_CATEGORY = "tokopedia://digital/category";
    String TRAIN_HOMEPAGE = "tokopedia://kereta";
    String DISCOVERY_PAGE = "tokopedia://discovery/{page_id}";
    String PROMO = "tokopedia://promo";
    String PROMO_CATEGORY = "tokopedia://promo/{promo_id}";
    String PROMO_WITH_DASH = "tokopedia://promo/{promo_id}/";
    String PROMO_DETAIL = "tokopedia://promo/{slug}";
    String DISCOVERY_CATEGORY = "tokopedia://category";
    String DISCOVERY_CATEGORY_DETAIL = "tokopedia://category/{DEPARTMENT_ID}";
    String DISCOVERY_SEARCH = "tokopedia://search";
    String DISCOVERY_HOTLIST_DETAIL = "tokopedia://hot/{alias}";
    String DISCOVERY_CATALOG = "tokopedia://catalog/{EXTRA_CATALOG_ID}";
    String PAYMENT_BACK_TO_DEFAULT = "tokopedia://payment/backtodefault";
    String WISHLIST = "tokopedia://wishlist";
    String RECENT_VIEW = "tokopedia://recentlyviewed";
    String TOPPICKS = "tokopedia://toppicks";
    String TOPPICK_DETAIL = "tokopedia://toppicks/{toppick_id}";
    String LOGIN = "tokopedia://login";
    String OFFICIAL_STORES = "tokopedia://official-stores";
    String OFFICIAL_STORE = "tokopedia://official-store";
    String OFFICIAL_STORES_CATEGORY = "tokopedia://official-stores/{key_category}";
    String RESCENTER = "tokopedia://resolution/{resolution_id}";
    String RESCENTER_BUYER = "tokopedia://resolution/list/buyer";
    String RESCENTER_SELLER = "tokopedia://resolution/list/seller";
    String TOPCHAT = "tokopedia://topchat/{message_id}";
    String TOPCHAT_IDLESS = "tokopedia://topchat";
    String TOPCHAT_ASKSELLER = "tokopedia://topchat/askseller/{toShopId}?customMessage" +
            "={customMessage}&source={source}&opponent_name={opponent_name}&avatar={avatar}";
    String TOPCHAT_ASKBUYER = "tokopedia://topchat/askbuyer/{toUserId}?customMessage" +
            "={customMessage}&source={source}&opponent_name={opponent_name}&avatar={avatar}";
    String CHATBOT = "tokopedia://chatbot/{message_id}";
    String GROUPCHAT_LIST = "tokopedia://groupchat";
    String REFERRAL = "tokopedia://referral";
    String OFFICIAL_STORES_PROMO = "tokopedia://official-stores/promo/{slug}";
    String OFFICIAL_STORE_PROMO = "tokopedia://official-store/promo/{slug}";
    String OFFICIAL_STORES_PROMO_TERMS = "tokopedia://official-stores/promo-terms";
    String PROMO_SALE = "tokopedia://sale/{slug}/";
    String PROMO_SALE_NO_SLASH = "tokopedia://sale/{slug}";
    String PROMO_SALE_TERMS = "tokopedia://promo-sale/promo-terms";
    String WALLET_HOME = "tokopedia://wallet";
    String WALLET_ACTIVATION = "tokopedia://wallet/activation";
    String WALLET_TRANSACTION_HISTORY = "tokopedia://wallet/transaction/history";
    String BROWSER = "tokopedia://browser";
    String FAVORITE = "tokopedia://home/favorite";
    String SUB_PROMO = "tokopedia://sale/{slug}/{category_slug}";
    String SUB_PROMO_WITH_SLASH = "tokopedia://sale/{slug}/{category_slug}/";
    String BRAND_LIST = "tokopedia://official-store/brand";
    String BRAND_LIST_WITH_SLASH = "tokopedia://official-store/brand/";
    String REGISTER = "tokopedia://registration";
    String PROFILE = "tokopedia://people/{user_id}";
    String PROFILE_AFTER_POST = "tokopedia://people/{user_id}?after_post=true";
    String PROFILE_AFTER_EDIT = "tokopedia://people/{user_id}?after_edit=true";
    String PROFILE_COMPLETION = "tokopedia://profilecompletion";
    String HOWTOPAY = "tokopedia://howtopay";

    String EVENTS = "tokopedia://events";
    String EVENTS_ACTIVITIES = "tokopedia://events/activities";
    String EVENTS_DETAILS = "tokopedia://events/{event}";
    String REFERRAL_WELCOME = "tokopedia://referral/{code}/{owner}";
    String PROMO_LIST = "tokopedia://promoNative";
    String EXPLORE = "tokopedia://jump/{section}";

    String DIGITAL_ORDER = "tokopedia://digital/order";
    String EVENTS_ORDER = "tokopedia://events/order";
    String DEALS_ORDER = "tokopedia://deals/order";
    String FLIGHT_ORDER = "tokopedia://pesawat/order";


    String MARKETPLACE_ORDER = "tokopedia://belanja/order";
    String MARKETPLACE_ORDER_FILTER = "tokopedia://order/marketplace/filter/{filter_id}";

    String MARKETPLACE_WAITING_CONFIRMATION = "tokopedia://order/marketplace/filter/5";
    String MARKETPLACE_SENT = "tokopedia://order/marketplace/filter/13";
    String MARKETPLACE_ORDER_PROCESSED = "tokopedia://order/marketplace/filter/12";
    String MARKETPLACE_DELIVERED = "tokopedia://order/marketplace/filter/14";


    String BUYER_INFO = "tokopedia://buyerinfo";
    String SELLER_INFO = "tokopedia://sellerinfo";
    String SELLER_INFO_DETAIL = "tokopedia://sellerinfo/detail";

    String TC_LANDING = "tokopedia://tc-landing";

    String CONTENT_EXPLORE = "tokopedia://content/explore/{tab_name}/{category_id}";
    String CONTENT_DETAIL = "tokopedia://content/{post_id}";
    String INTEREST_PICK = "tokopedia://interestpick";
    String KOL_COMMENT = "tokopedia://kolcomment/{id}";
    String KOL_YOUTUBE = "tokopedia://kolyoutube/{youtube_url}";

    String PLAY_NOTIFICATION_VIDEO = "tokopedia://play-notif-video";
    String INBOX_TICKET = "tokopedia://customercare";
    String TICKET_DETAIL = "tokopedia://customercare/{ticket_id}";

    String ORDER_TRACKING = "tokopedia://shipping/tracking/{order_id}";

    String FLIGHT = "tokopedia://pesawat";

    String PRODUCT_MANAGE = "tokopedia://seller/product/manage";
    String PRODUCT_DRAFT = "tokopedia://seller/product/draft";
    String SELLER_TRANSACTION = "tokopedia://seller";
    String SELLER_OPPORTUNITY = "tokopedia://seller/opportunity";
    String SELLER_CENTER = "tokopedia://seller/seller-center";
    String CONTACT_US = "tokopedia://contact-us";
    String CONTACT_US_NATIVE = "tokopedia://contactus";

    String ORDER_LIST = "tokopedia://order";
    String TOKOPOINTS = "tokopedia://tokopoints";
    String COUPON_LISTING = "tokopedia://tokopoints/kupon-saya";

    String DEVELOPER_OPTIONS = "tokopedia://setting/dev-opts";
    String SETTING_DEVELOPER_OPTIONS = "tokopedia://setting/dev-opts/{type}";
    String SETTING_PAYMENT = "tokopedia://setting/payment";
    String SETTING_ACCOUNT = "tokopedia://setting/account";
    String SETTING_PASSWORD = "tokopedia://setting/password";
    String SETTING_PROFILE = "tokopedia://setting/profile";
    String NOTIFICATION = "tokopedia://notification";
    String PMS = "tokopedia://buyer/payment";
    String PURCHASE_ORDER = "tokopedia://buyer/order";
    String PURCHASE_ORDER_DETAIL = "tokopedia://buyer/order/{order_id}";
    String PURCHASE_CONFIRMED = "tokopedia://buyer/confirmed";
    String PURCHASE_PROCESSED = "tokopedia://buyer/processed";
    String PURCHASE_SHIPPING_CONFIRM = "tokopedia://buyer/shipping-confirm";
    String PURCHASE_SHIPPED = "tokopedia://buyer/shipped";
    String PURCHASE_DELIVERED = "tokopedia://buyer/delivered";
    String PURCHASE_HISTORY = "tokopedia://buyer/history";

    String SELLER_PURCHASE_READY_TO_SHIP = "tokopedia://seller/ready-to-ship";
    String SELLER_PURCHASE_SHIPPED = "tokopedia://seller/shipped";
    String SELLER_PURCHASE_DELIVERED = "tokopedia://seller/delivered";

    String DEPOSIT = "tokopedia://saldo";
    String INSTANT_LOAN = "tokopedia://loan";
    String INSTANT_LOAN_TAB = "tokopedia://loan/category/{tab_name}";

    String CHANGE_PASSWORD = "tokopedia://settings/changepassword";
    String SETTING_BANK = "tokopedia://settings/bankaccount";

    String AFFILIATE_CREATE_POST = "tokopedia://affiliate/create_post/{product_id}/{ad_id}";
    String AFFILIATE_EDIT_POST = "tokopedia://affiliate/edit/{post_id}";
    String AFFILIATE_DASHBOARD = "tokopedia://affiliate/dashboard";
    String AFFILIATE_ONBOARDING = "tokopedia://affiliate/onboarding";
    String AFFILIATE_EDUCATION = "tokopedia://affiliate/education";
    String AFFILIATE_EXPLORE = "tokopedia://affiliate/explore";
    String AFFILIATE_PRODUCT = "tokopedia://affiliate/product/{product_id}";

    String CHALLENGE = "tokopedia://challenges";
    String ADD_NAME_REGISTER = "tokopedia://addnameregister/{phone}";
    String ADD_NAME_PROFILE = "tokopedia://addname";
    String CREATE_PASSWORD = "tokopedia://createpassword";
    String PHONE_VERIFICATION = "tokopedia://phoneverification";
    String CHANGE_INACTIVE_PHONE = "tokopedia://changeinactivephone";

    String KYC = "tokopedia://kyc";
    String KYC_SELLER_DASHBOARD = "tokopedia://kyc?source=seller";

    String CONSUMER_SPLASH_SCREEN = "tokopedia://splashscreen/consumer";
    String IMAGE_PREVIEW = "tokopedia://imagepreview";

    String HOME_CREDIT_KTP = "tokopedia://fintech/home-credit/ktp/";
    String HOME_CREDIT_SELFIE = "tokopedia://fintech/home-credit/selfie/";

    String PLAY_WEBVIEW = "tokopedia://play/webview?url={url}&titlebar={has_titlebar}";

    interface SellerApp {
        String PRODUCT_ADD = "sellerapp://product/add";
        String SALES = "sellerapp://sales";
        String TOPADS_CREDIT = "sellerapp://topads/buy";
        String TOPADS_PRODUCT_CREATE = "sellerapp://topads/create";
        String GOLD_MERCHANT = "sellerapp://gold";
        String SELLER_APP_HOME = "sellerapp://home";
        String TOPADS_DASHBOARD = "sellerapp://topads";
        String TOPADS_PRODUCT_DETAIL = "sellerapp://topads/product/{ad_id}";
        String TOPADS_PRODUCT_DETAIL_CONSTS = "sellerapp://topads/product";
        String BROWSER = "sellerapp://browser";
    }

    interface Query {
        String ORDER_TRACKING_ORDER_ID = "order_id";
        String ORDER_TRACKING_URL_LIVE_TRACKING = "url_live_tracking";

        String IMAGE_PREVIEW_FILELOC = "fileloc";
        String IMAGE_PREVIEW_IMG_POSITION = "img_pos";
        String IMAGE_PREVIEW_IMAGE_DESC = "image_desc";
        String IMAGE_PREVIEW_FROM_CHAT = "from_chat";
        String IMAGE_PREVIEW_TITLE = "title";
        String IMAGE_PREVIEW_SUBTITLE = "subtitle";

        String PDP_ID = "product_id";
        String PDP_NAME = "product_name";
        String PDP_PRICE = "product_price";
        String PDP_DATE = "product_date";
        String PDP_IMAGE = "product_image";
    }

    interface Chat {
        String MESSAGE_ID = "message_id";
        String OPPONENT_ID = "opponent_id";
        String SHOP_ID = "shop_id";
        String OPPONENT_NAME = "opponent_name";
        String OPPONENT_ROLE = "opponent_role";
        String SOURCE = "source";
        String TO_USER_ID = "toUserId";
        String TO_SHOP_ID = "toShopId";
        String CUSTOM_MESSAGE = "customMessage";
        String AVATAR = "avatar";
        String CUSTOM_SUBJECT = "customSubject";
        String PARAM_HEADER = "header";
    }

    interface Play {
        String PARAM_HAS_TITLEBAR = "titlebar";
        String PARAM_URL = "url";
    }
}
