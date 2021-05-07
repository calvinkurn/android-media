package com.tokopedia.applink;

/**
 * @author ricoharisin .
 * <p>
 * Central interface for AppLink in apps, do not use any new applink module
 */

public interface ApplinkConst {
    String APPLINK_CUSTOMER_SCHEME = "tokopedia";
    String HOME_NAVIGATION = "tokopedia://navigation/main";
    String HOME = "tokopedia://home";
    String HOME_FEED = "tokopedia://home/feed";
    String HOME_ACCOUNT = "tokopedia://home/account";
    String HOME_ACCOUNT_SELLER = "tokopedia://home/account/seller";
    String HOME_RECOMMENDATION = "tokopedia://home/recommendation";
    String FEED = "tokopedia://feed";
    String FIND = "tokopedia://find";
    String AMP_FIND = "tokopedia://amp/find";
    String FEED_HASHTAG = "tokopedia://feed/hashtag/{hashtag}";
    String FEED_DETAILS = "tokopedia://feedcommunicationdetail";
    String HOME_CATEGORY = "tokopedia://home/category";
    String HOME_HOT_HOST = "hot";
    String HOME_HOTLIST = "tokopedia://hot";
    String HOST_CATEGORY_P = "p";
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
    String SHOP_HOME = "tokopedia://shop/{shop_id}/home";
    String SHOP_PRODUCT = "tokopedia://shop/{shop_id}/product";
    String SHOP_FEED = "tokopedia://shop/{shop_id}/feed";
    String SHOP_ETALASE_LIST = "tokopedia://shop/{shop_id}/etalase-list";
    String MY_SHOP_ETALASE_LIST = "tokopedia://my-shop/etalase/list";
    String SHOP_FOLLOWER_LIST = "tokopedia://shop/{shop_id}/follower";
    String SHOP_SETTINGS_CUSTOMER_APP = "tokopedia://shop/{shop_id}/settings";

    String SHOP_SETTINGS_NOTE = "tokopedia://setting/shop/note";
    String SHOP_SETTINGS_INFO = "tokopedia://setting/shop/info";
    String PRODUCT_INFO = "tokopedia://product/{product_id}";
    String PRODUCT_IMAGE_REVIEW = "tokopedia://product/{product_id}/imagereview";
    String PRODUCT_REVIEW = "tokopedia://product/{id}/review";
    String PRODUCT_ADD = "tokopedia://product/add";
    String DEFAULT_RECOMMENDATION_PAGE = "tokopedia://rekomendasi/";
    String RECOMMENDATION_PAGE = "tokopedia://rekomendasi/{product_id}/?ref={ref}";
    String CREDIT_CARD_AUTH_SETTING = "tokopedia://payment/credit-card";
    String ADD_CREDIT_CARD = "tokopedia://payment/credit-card/add";
    String CART = "tokopedia://cart";
    String ATC_EXTERNAL = "tokopedia://cart?product_id={product_id}";
    String CHECKOUT = "tokopedia://checkout";
    String SELLER_NEW_ORDER = "tokopedia://seller/new-order";
    String SELLER_SHIPMENT = "tokopedia://seller/shipment";
    String SELLER_STATUS = "tokopedia://seller/status";
    String SELLER_HISTORY = "tokopedia://seller/history";
    String CREATE_SHOP = "tokopedia://buka-toko-online-gratis";
    String REPUTATION = "tokopedia://review";
    String REPUTATION_DETAIL = "tokopedia://review/{reputation_id}";
    String REVIEW_DETAIL = "tokopedia://review/detail/{feedback_id}";
    String PRODUCT_CREATE_REVIEW = "tokopedia://product-review/create/";
    String PRODUCT_REPUTATION = "tokopedia://product/{product_id}/review";
    String SELLER_REVIEW = "tokopedia://seller-review-detail";
    String REVIEW_REMINDER = "tokopedia://review-reminder";
    String WEBVIEW = "tokopedia://webview";
    String WEBVIEW_DOWNLOAD = "tokopedia://webviewdownload";
    String WEBVIEW_DOWNLOAD_HTML = "tokopedia://webview-download-html";
    String WEBVIEW_PARENT_HOME = "tokopedia://webviewbackhome";
    String PRODUCT_TALK = "tokopedia://product/{product_id}/talk";
    String DIGITAL = "tokopedia://digital";
    String DIGITAL_PRODUCT = "tokopedia://digital/form";
    String DIGITAL_SUBHOMEPAGE_HOME = "tokopedia://recharge/home";
    String DIGITAL_SMARTCARD = "tokopedia://digital/smartcard";
    String DIGITAL_SMARTBILLS = "tokopedia://digital/bayarsekaligus";
    String DIGITAL_CART = "tokopedia://digital/cart";
    String DIGITAL_CATEGORY = "tokopedia://digital/category";
    String RECHARGE = "tokopedia://recharge";
    String TRAIN_HOMEPAGE = "tokopedia://kereta";
    String DISCOVERY_PAGE = "tokopedia://discovery/{page_id}";
    String REACT_DISCOVERY_PAGE = "tokopedia://reactDiscovery/{page_id}";
    String DISCOVERY_NEW_USER = "tokopedia://discovery/pengguna-baru";
    String HOME_EXPLORE = "tokopedia://jump";
    String PROMO = "tokopedia://promo";
    String PROMO_CATEGORY = "tokopedia://promo/{promo_id}";
    String PROMO_WITH_DASH = "tokopedia://promo/{promo_id}/";
    String PROMO_DETAIL = "tokopedia://promo/{slug}";
    String DISCOVERY_CATEGORY = "tokopedia://category";
    String DISCOVERY_CATEGORY_DETAIL = "tokopedia://category/{DEPARTMENT_ID}";
    String DISCOVERY_SEARCH = "tokopedia://search";
    String DISCOVERY_SEARCH_AUTOCOMPLETE = "tokopedia://search-autocomplete";
    String DISCOVERY_HOTLIST_DETAIL = "tokopedia://hot/{alias}";
    String DISCOVERY_CATALOG = "tokopedia://catalog";
    String PAYMENT_BACK_TO_DEFAULT = "tokopedia://payment/backtodefault";
    String WISHLIST = "tokopedia://wishlist";
    String NEW_WISHLIST = "tokopedia://new-wishlist";
    String WISHLIST_HOME = "tokopedia://wishlisthome";
    String RECENT_VIEW = "tokopedia://recentlyviewed";
    String HOST_LOGIN = "login";
    String LOGIN = "tokopedia://login";
    String OTP = "tokopedia://otp";
    String OTP_PUSH_NOTIF_RECEIVER = "tokopedia://otp-verify";
    String OFFICIAL_STORES = "tokopedia://official-stores";
    String OFFICIAL_STORE = "tokopedia://official-store";
    String OFFICIAL_STORE_CATEGORY = "tokopedia://official-store/{key_category}";
    String RESCENTER = "tokopedia://resolution/{resolution_id}";
    String RESCENTER_BUYER = "tokopedia://resolution/list/buyer";
    String RESCENTER_SELLER = "tokopedia://resolution/list/seller";
    String ORDER_HISTORY = "tokopedia://product-order-history";
    String ORDER_HISTORY_SHOP = "tokopedia://product-order-history/{shop_id}";
    String TOPCHAT = "tokopedia://topchat/{message_id}";
    String TOP_CHAT = "tokopedia://topchat";
    String TOPCHAT_IDLESS = "tokopedia://topchat";
    String TOPCHAT_OLD = "tokopedia://topchatold";
    /**
     * Go to chat room and chat with seller use one of the pattern below:
     * - {@value TOPCHAT_ROOM_ASKSELLER} - use shopId only
     * - {@value TOPCHAT_ROOM_ASKSELLER_WITH_MSG} - use shopId and provide initial msg in editText
     * - {@value TOPCHAT_ASKSELLER} - use shopId and several parameters
     *
     */
    String TOPCHAT_ROOM_ASKSELLER = "tokopedia://topchat/askseller/{toShopId}";
    String TOPCHAT_ROOM_ASKSELLER_WITH_MSG = "tokopedia://topchat/askseller/{toShopId}" +
            "?customMessage={customMessage}";
    String TOPCHAT_ASKSELLER = "tokopedia://topchat/askseller/{toShopId}?customMessage" +
            "={customMessage}&source={source}&opponent_name={opponent_name}&avatar={avatar}";
    /**
     * Go to chat room and chat with buyer use one of the pattern below:
     * - {@value TOPCHAT_ROOM_ASKBUYER} - use userId only
     * - {@value TOPCHAT_ROOM_ASKBUYER_WITH_MSG} - use userId and provide initial msg in editText
     * - {@value TOPCHAT_ASKBUYER} - use userId and several parameters
     *
     */
    String TOPCHAT_ROOM_ASKBUYER = "tokopedia://topchat/askbuyer/{toUserId}";
    String TOPCHAT_ROOM_ASKBUYER_WITH_MSG = "tokopedia://topchat/askbuyer/{toUserId}" +
            "?customMessage={customMessage}";
    String TOPCHAT_ASKBUYER = "tokopedia://topchat/askbuyer/{toUserId}?customMessage" +
            "={customMessage}&source={source}&opponent_name={opponent_name}&avatar={avatar}";
    String CHATBOT = "tokopedia://chatbot/{message_id}";
    String CHAT_BOT = "tokopedia://chatbot";
    String CHAT_TEMPLATE = "tokopedia://chat/settings/templatechat";
    String REFERRAL = "tokopedia://referral";
    String WALLET_HOME = "tokopedia://wallet";
    String WALLET_ACTIVATION = "tokopedia://wallet/activation";
    String WALLET_TRANSACTION_HISTORY = "tokopedia://wallet/transaction/history";
    String BROWSER = "tokopedia://browser";
    String FAVORITE = "tokopedia://home/favorite";
    String SUB_PROMO = "tokopedia://sale/{slug}/{category_slug}";
    String SUB_PROMO_WITH_SLASH = "tokopedia://sale/{slug}/{category_slug}/";
    String BRAND_LIST = "tokopedia://official-store/brand";
    String BRAND_LIST_WITH_SLASH = "tokopedia://official-store/brand/";
    String BRAND_LIST_CATEGORY = "tokopedia://official-store/brand/{category_id}";
    String REGISTER = "tokopedia://registration";
    String REGISTER_INIT = "tokopedia://register-init";
    String PROFILE = "tokopedia://people/{user_id}";
    String PROFILE_AFTER_POST = "tokopedia://people/{user_id}?after_post=true";
    String PROFILE_AFTER_EDIT = "tokopedia://people/{user_id}?after_edit=true";
    String PROFILE_SUCCESS_POST = "tokopedia://people/{user_id}?success_post=true";
    String PROFILE_COMPLETION = "tokopedia://profilecompletion";
    String HOWTOPAY = "tokopedia://howtopay";
    String GOLD_MERCHANT_STATISTIC_DASHBOARD = "tokopedia://gold-merchant-statistic-dashboard";
    String SHOP_SCORE_DETAIL = "tokopedia://shop-score-detail";

    String EVENTS = "tokopedia://events";
    String EVENTS_CATEGORY = "tokopedia://events/category";
    String EVENTS_ACTIVITIES = "tokopedia://events/activities";
    String EVENTS_DETAILS = "tokopedia://events/{event}";
    String REFERRAL_WELCOME = "tokopedia://referral/{code}/{owner}";
    String PROMO_LIST = "tokopedia://promoNative";
    String EXPLORE = "tokopedia://jump/{section}";

    String DIGITAL_ORDER = "tokopedia://digital/order";
    String EVENTS_ORDER = "tokopedia://events/order";
    String DEALS_ORDER = "tokopedia://deals/order";
    String FLIGHT_ORDER = "tokopedia://pesawat/order";
    String TRAIN_ORDER = "tokopedia://kereta/order";
    String GIFT_CARDS_ORDER = "tokopedia://giftcards/order";
    String INSURANCE_ORDER = "tokopedia://insurance/order";
    String MODAL_TOKO_ORDER = "tokopedia://modaltoko/order";
    String HOTEL_ORDER = "tokopedia://hotel/order";
    String HOTEL_HOST = "hotel";
    String HOTEL = "tokopedia://hotel";
    String HOTEL_SRP = "tokopedia://hotel/result";
    String TRAVEL_SUBHOMEPAGE = "tokopedia://travelentertainment";
    String TRAVEL_SUBHOMEPAGE_HOME = "tokopedia://travelentertainment/home";

    String OMS_ORDER_DETAIL = "tokopedia://order/";
    String MARKETPLACE_ORDER = "tokopedia://marketplace/order";
    String BELANJA_ORDER = "tokopedia://belanja/order";
    String MARKETPLACE_ORDER_SUB = "tokopedia://order/marketplace/filter";
    String MARKETPLACE_ORDER_FILTER = "tokopedia://order/marketplace/filter/{filter_id}";
    String TRAVEL_AND_ENTERTAINMENT_ORDER = "tokopedia://travelent/order";
    String SNAPSHOT_ORDER = "tokopedia://snapshot/order";

    String MARKETPLACE_WAITING_CONFIRMATION = "tokopedia://order/marketplace/filter/5";
    String MARKETPLACE_SENT = "tokopedia://order/marketplace/filter/13";
    String MARKETPLACE_ORDER_PROCESSED = "tokopedia://order/marketplace/filter/12";
    String MARKETPLACE_DELIVERED = "tokopedia://order/marketplace/filter/14";

    String BUYER_INFO = "tokopedia://notif-center";
    String BUYER_INFO_WITH_ID = "tokopedia://notif-center/{notif_id}";
    String SELLER_INFO = "tokopedia://sellerinfo";
    String SELLER_INFO_DETAIL = "tokopedia://sellerinfo/detail";

    String TC_LANDING = "tokopedia://tc-landing";

    String PLAY_NOTIFICATION_VIDEO = "tokopedia://play-notif-video";
    String INBOX_TICKET = "tokopedia://customercare";
    String TICKET_DETAIL = "tokopedia://customercare/{ticket_id}";

    String OVO_REGISTER_INIT = "tokopedia://ovo-reg-init";
    String OVO_FINAL_PAGE = "tokopedia://ovo-final";

    String ORDER_TRACKING = "tokopedia://shipping/tracking/{order_id}";

    String FLIGHT = "tokopedia://pesawat";
    String FLIGHT_PHONE_VERIFICATION = "tokopedia-android-internal://pesawat/phone-verification";

    String PRODUCT_MANAGE = "tokopedia://seller/product/manage";
    String PRODUCT_DRAFT = "tokopedia://seller/product/draft";
    String PRODUCT_EDIT = "tokopedia://product/edit/{product_id}";
    String SELLER_TRANSACTION = "tokopedia://seller";
    String SELLER_CENTER = "tokopedia://seller/seller-center";
    String SELLER_SHIPPING_EDITOR = "tokopedia://seller/setting/shipping-editor";
    String SELLER_COD_ACTIVATION = "tokopedia://seller/setting/cod-activation";
    String SELLER_WAREHOUSE_DATA = "tokopedia://seller/setting/shop-address";
    String CONTACT_US = "tokopedia://contact-us";
    String CONTACT_US_NATIVE = "tokopedia://contactus";

    String ORDER_LIST = "tokopedia://order";
    String ORDER_LIST_WEBVIEW = "tokopedia://order_list";
    String TOKOPOINTS = "tokopedia://tokopoints";
    String TOKOPEDIA_REWARD = "tokopedia://rewards";
    String COUPON_LISTING = "tokopedia://tokopoints/kupon-saya";

    String FEEDBACK_FORM = "tokopedia://internal-feedback";
    String DEVELOPER_OPTIONS = "tokopedia://setting/dev-opts";
    String SETTING_DEVELOPER_OPTIONS = "tokopedia://setting/dev-opts/{type}";
    String SETTING_PAYMENT = "tokopedia://setting/payment";
    String SETTING_ACCOUNT = "tokopedia://setting/account";
    String SETTING_PASSWORD = "tokopedia://setting/password";
    String SETTING_PROFILE = "tokopedia://setting/profile";

    String NOTIFICATION = "tokopedia://notification";
    String NOTIFICATION_TROUBLESHOOTER = "tokopedia://notification-troubleshooter";
    String PMS = "tokopedia://buyer/payment";
    String PURCHASE_ORDER = "tokopedia://buyer/order";
    String PURCHASE_ORDER_DETAIL = "tokopedia://buyer/order/{order_id}";
    String PURCHASE_CONFIRMED = "tokopedia://buyer/confirmed";
    String PURCHASE_PROCESSED = "tokopedia://buyer/processed";
    String PURCHASE_SHIPPING_CONFIRM = "tokopedia://buyer/shipping-confirm";
    String PURCHASE_SHIPPED = "tokopedia://buyer/shipped";
    String PURCHASE_DELIVERED = "tokopedia://buyer/delivered";
    String PURCHASE_HISTORY = "tokopedia://buyer/history";
    String PURCHASE_ONGOING = "tokopedia://buyer/ongoing-order";

    String SELLER_PURCHASE_READY_TO_SHIP = "tokopedia://seller/ready-to-ship";
    String SELLER_PURCHASE_SHIPPED = "tokopedia://seller/shipped";
    String SELLER_PURCHASE_DELIVERED = "tokopedia://seller/delivered";

    String SELLER_PURCHASE_CANCELED = "tokopedia://seller/cancelled";
    String SELLER_PURCHASE_CANCELLATION_REQUEST = "tokopedia://seller/cancellationrequest";
    String SELLER_PURCHASE_WAITING_PICKUP = "tokopedia://seller/waitingpickup";
    String SELLER_PURCHASE_WAITING_AWB = "tokopedia://seller/waitingawb";
    String SELLER_PURCHASE_AWB_INVALID = "tokopedia://seller/awbinvalid";
    String SELLER_PURCHASE_AWB_CHANGE = "tokopedia://seller/awbchange";
    String SELLER_PURCHASE_RETUR = "tokopedia://seller/retur";
    String SELLER_PURCHASE_COMPLAINT = "tokopedia://seller/complaint";
    String SELLER_PURCHASE_FINISHED = "tokopedia://seller/finished";
    String SELLER_ORDER_DETAIL = "tokopedia://seller/order";

    String SALDO = "tokopedia://saldo";
    String LAYANAN_FINANSIAL = "tokopedia://layanan-finansial";
    String SALDO_INTRO = "tokopedia://saldo-intro";


    String CHANGE_PASSWORD = "tokopedia://settings/changepassword";
    String HAS_PASSWORD = "tokopedia://settings/haspassword";
    String SETTING_BANK = "tokopedia://settings/bankaccount";
    String SETTING_NOTIFICATION = "tokopedia://settings/notification";

    String CONTENT_EXPLORE = "tokopedia://content/explore/{tab_name}/{category_id}";
    String CONTENT_DETAIL = "tokopedia://content/{post_id}";
    String CONTENT_CREATE_POST = "tokopedia://content/create_post/";
    String CONTENT_DRAFT_POST = "tokopedia://content/draft/{draft_id}";
    String INTEREST_PICK = "tokopedia://interestpick";
    String KOL_COMMENT = "tokopedia://kolcomment/{id}";
    String KOL_YOUTUBE_HOST_BASE = "kolyoutube";
    String KOL_YOUTUBE = "tokopedia://" + KOL_YOUTUBE_HOST_BASE+ "/{youtube_url}";

    String AFFILIATE_CREATE_POST = "tokopedia://affiliate/create_post/{product_id}/{ad_id}";
    String AFFILIATE_DRAFT_POST = "tokopedia://affiliate/draft/{draft_id}";
    String AFFILIATE_DEFAULT_CREATE_POST = "tokopedia://affiliate/create_post/";
    String AFFILIATE_DASHBOARD = "tokopedia://affiliate/dashboard";
    String AFFILIATE_ONBOARDING = "tokopedia://affiliate/onboarding";
    String AFFILIATE_EDUCATION = "tokopedia://affiliate/education";
    String AFFILIATE_EXPLORE = "tokopedia://affiliate/explore";
    String AFFILIATE_PRODUCT = "tokopedia://affiliate/product/{product_id}";

    String PLAY_DETAIL = "tokopedia://play/{channel_id}";
    String PLAY_BROADCASTER = "tokopedia://play-broadcaster";

    String CHALLENGE = "tokopedia://challenges";
    String ADD_NAME_REGISTER = "tokopedia://addnameregister/{phone}";
    String ADD_NAME_PROFILE = "tokopedia://addname";
    String RESET_PASSWORD = "tokopedia://resetpassword";
    String PHONE_VERIFICATION = "tokopedia://phoneverification";
    String CHANGE_INACTIVE_PHONE = "tokopedia://changeinactivephone";
    String ADD_PIN_ONBOARD = "tokopedia://add-pin-onboarding";
    String ADD_FINGERPRINT_ONBOARDING = "tokopedia://add-fingerprint-onboarding";

    String KYC_NO_PARAM = "tokopedia://kyc";
    String KYC_SELLER_DASHBOARD = "tokopedia://kyc?source=seller";
    String KYC = "tokopedia://kyc?projectId=1";

    String KYC_FORM_NO_PARAM = "tokopedia://kyc-form";
    String KYC_FORM = "tokopedia://kyc-form?projectId={projectId}";

    String CONSUMER_SPLASH_SCREEN = "tokopedia://splashscreen/consumer";
    String IMAGE_PREVIEW = "tokopedia://imagepreview";

    String HOME_CREDIT_KTP_WITHOUT_TYPE = "tokopedia://fintech/home-credit/ktp";
    String HOME_CREDIT_KTP_WITH_TYPE = "tokopedia://fintech/home-credit/ktp/{type}";
    String HOME_CREDIT_SELFIE_WITHOUT_TYPE = "tokopedia://fintech/home-credit/selfie";
    String HOME_CREDIT_SELFIE_WITH_TYPE = "tokopedia://fintech/home-credit/selfie/{type}";
    String PAYLATER = "tokopedia://fintech/paylater";
    String INBOX_HOST = "inbox";
    String INBOX = "tokopedia://inbox";

    String PLAY_WEBVIEW = "tokopedia://play/webview?url={url}&titlebar={has_titlebar}";
    String SMC_REFERRAL = "tokopedia://smc-referral";

    String CATEGORY_BELANJA = "tokopedia://category_belanja/{CATEGORY_NAME}";

    String POWER_MERCHANT_SUBSCRIBE = "tokopedia://power_merchant/subscribe";

    String CATEGORY = "tokopedia://category";
    String TRADEIN = "tokopedia://category/tradein";
    String QRSCAN = "tokopedia://scanqr";
    String OVOP2PTRANSFERFORM = "tokopedia-android-internal://ovop2ptransfer?phone";
    String OVOP2PTRANSFERFORM_SHORT = "tokopedia-android-internal://ovop2ptransfer";
    String OVOP2PTHANKYOUPAGE = "tokopedia://ovop2pthankyoupage/{transfer_id}";
    String OVO_WALLET = "tokopedia://ovo";
    String DEALS_HOME = "tokopedia://deals";
    String DEALS_DETAIL = "tokopedia://deals/{slug}";
    String DEALS_BRAND_DETAIL = "tokopedia://deals/brand/{slug}";
    String DEALS_ALL_BRANDS = "tokopedia://deals/allbrands/{isVoucher}";
    String DEALS_CATEGORY = "tokopedia://deals/category/page";

    String SALAM_UMRAH_ORDER_DETAIL = "tokopedia://order-details/umroh";
    String SALAM_UMRAH = "tokopedia://s/umroh";
    String SALAM_UMRAH_SEARCH = "tokopedia://s/umroh/search";
    String SALAM_UMRAH_PACKET = "tokopedia://s/umroh/paket/{category_id}";
    String SALAM_UMRAH_PDP = "tokopedia://s/umroh/produk/{slug}";
    String SALAM_UMRAH_CHECKOUT = "tokopedia://s/umroh/checkout";
    String SALAM_UMRAH_SHOP = "tokopedia://shop/7298319";
    String THANK_YOU_PAGE_NATIVE = "tokopedia://payment/thankyou";
    String THANKYOU_PAGE_NATIVE = "tokopedia://payment/thankyou?payment_id={payment_id}&merchant={merchant_code}";

    String SALAM_UMRAH_AGEN = "tokopedia://s/umroh/agen/{slug}";
    String SALAM_UMRAH_LIST_AGEN = "tokopedia://s/umroh/agen";

    String MERCHANT_VOUCHER_LIST = "tokopedia://merchant-voucher/list";

    String DFFALLBACKURL_KEY = "dffallbackurl";

    String OCC = "tokopedia://occ";

    String SELLER_MIGRATION = "tokopedia://seller/seller-migration";

    String ACCOUNT = "tokopedia://account";

    interface Discovery {
        String CATEGORY = "tokopedia://category";
    }

    interface Gamification {
        String CRACK = "tokopedia://gamification";
        String TAP_TAP_MANTAP = "tokopedia://gamification2";
        String DAILY_GIFT_BOX = "tokopedia://gamification_gift_daily";
        String GIFT_TAP_TAP = "tokopedia://gamification_gift_60s";
    }

    interface Digital {
        String DIGITAL_BROWSE = "tokopedia://category-explore";
        String DIGITAL_PRODUCT = "tokopedia://digital/form";
        String DIGITAL = "tokopedia://digital";
        String DIGITAL_CATEGORY = "tokopedia://digital/category";
        String DIGITAL_CART = "tokopedia://digital/cart";
    }

    interface TokoPoints {
        String HOMEPAGE = "tokopedia://tokopoints";
        String HOMEPAGE2 = "tokopedia://tokopoints/";
        String HOMEPAGE_REWARD1 = "tokopedia://rewards";
        String HOMEPAGE_REWARD2 = "tokopedia://rewards/";
        String COUPON_DETAIL = "kupon-saya/detail";
        String COUPON_DETAIL_VALUE = "kupon-detail";
        String CATALOG_DETAIL = "tukar-point/detail";
        String CATALOG_DETAIL_VALUE = "tukar-detail";
        String CATALOG_DETAIL_NEW = "kupon/detail";
        String CATALOG_LIST_NEW = "kupon";
        String CATALOG_LIST_VALUE = "tukar-point";
        String HISTORY = "tokopedia://tokopoints/history";
    }

    String OQR_PIN_URL_ENTRY_LINK = "tokopedia://ovoqrthanks/";
    String DISCOVERY = "tokopedia://discovery";

    String MONEYIN = "tokopedia://money_in/device_validation";

    interface WebViewUrl {
        String SALDO_DETAIL = "https://m.tokopedia.com/deposit";
    }

    interface CustomerApp {
        String TOPADS_DASHBOARD = "tokopedia://topads/dashboard";
    }

    interface SellerApp {
        String PRODUCT_ADD = "sellerapp://product/add";
        String SALES = "sellerapp://sales";
        String TOPADS_CREDIT = "sellerapp://topads/buy";
        String TOPADS_AUTO_TOPUP = "sellerapp://topads/auto-topup";
        String TOPADS_PRODUCT_CREATE = "sellerapp://topads/create";
        String TOPADS_CREATE_ADS = "sellerapp://topads/create-ads";
        String TOPADS_CREATE_ONBOARDING = "sellerapp://topads/creation-onboard";
        String TOPADS_HEADLINE_CREATE = "sellerapp://topads/headline-ad-creation";
        String TOPADS_HEADLINE_EDIT = "sellerapp://topads/headline-ad-edit";
        String TOPADS_HEADLINE_DETAIL = "sellerapp://topads/headline-ad-detail";
        String TOPADS_CREATE_AUTO_ADS = "sellerapp://topads/create-autoads";
        String TOPADS_EDIT_AUTO_ADS = "sellerapp://topads/edit-autoads";
        String TOPADS_CREDIT_HISTORY = "sellerapp://topads/history-credit";
        String TOPADS_CREATE_CHOOSER = "sellerapp://topads/ad-picker";
        String GOLD_MERCHANT = "sellerapp://gold";
        String SELLER_APP_HOME = "sellerapp://home";
        String TOPADS_DASHBOARD = "sellerapp://topads";
        String POWER_MERCHANT_SUBSCRIBE = "sellerapp://power_merchant/subscribe";
        String BROWSER = "sellerapp://browser";
        String WEBVIEW = "sellerapp://webview";
        String VOUCHER_LIST = "sellerapp://voucher-list";
        String VOUCHER_ACTIVE = "sellerapp://voucher-list/active";
        String VOUCHER_HISTORY = "sellerapp://voucher-list/history";
        String VOUCHER_DETAIL = "sellerapp://voucher-detail";
        String CREATE_VOUCHER = "sellerapp://create-voucher";
        String SELLER_SEARCH = "sellerapp://seller-search";
        String PLAY_BROADCASTER = "sellerapp://play-broadcaster";
        String CENTRALIZED_PROMO = "sellerapp://centralized-promo";
        String SHOP_FEED = "sellerapp://shop/{shop_id}/feed";
        String CONTENT_CREATE_POST = "sellerapp://content/create_post";
        String SELLER_SHIPPING_EDITOR = "sellerapp://setting/shipping-editor";
        String STATISTIC_DASHBOARD = "sellerapp://gold-merchant-statistic-dashboard";
        String SHOP_SETTINGS_SELLER_APP = "sellerapp://shop/{shop_id}/settings";
    }

    interface Query {
        String ORDER_TRACKING_ORDER_ID = "order_id";
        String ORDER_TRACKING_URL_LIVE_TRACKING = "url_live_tracking";
        String ORDER_TRACKING_CALLER = "caller";

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

        String PRODUCT_PREVIEWS = "product_previews";

        String INVOICE_ID = "invoice_id";
        String INVOICE_CODE = "invoice_code";
        String INVOICE_TITLE = "invoice_title";
        String INVOICE_DATE = "invoice_date";
        String INVOICE_IMAGE_URL = "invoice_image";
        String INVOICE_URL = "invoice_url";
        String INVOICE_STATUS_ID = "invoice_status_id";
        String INVOICE_STATUS = "invoice_status";
        String INVOICE_TOTAL_AMOUNT = "invoice_total_amount";

        String PATH_ASK_SELLER = "askseller";
        String PATH_ASK_BUYER = "askbuyer";

        String SOURCE_ASK_SELLER = "tx_ask_seller";
        String SOURCE_PAGE = "source_page";
        String SEARCH_CREATE_TIME = "search_create_time_str";
        String SEARCH_PRODUCT_KEYWORD = "search_product_keyword";

        String SHOP_FOLLOWERS_CHAT_KEY = "shop_followers_chat_key";

        // chat source page
        String SOURCE_CHAT_SEARCH = "chat_search";
    }

    interface AttachInvoice {
        String PARAM_MESSAGE_ID = "msgId";
        String PARAM_OPPONENT_NAME = "opponentName";
    }

    interface AttachVoucher {
        String PARAM_VOUCHER_PREVIEW = "voucher_preview";
    }

    interface OrderHistory {
        String PARAM_SHOP_ID = "shop_id";
    }

    interface AttachProduct {
        String TOKOPEDIA_ATTACH_PRODUCT_SHOP_ID_KEY = "TKPD_ATTACH_PRODUCT_SHOP_ID";
        String TOKOPEDIA_ATTACH_PRODUCT_IS_SELLER_KEY = "TKPD_ATTACH_PRODUCT_IS_SELLER";
        String TOKOPEDIA_ATTACH_PRODUCT_SHOP_NAME_KEY = "TKPD_ATTACH_PRODUCT_SHOP_NAME";
        String TOKOPEDIA_ATTACH_PRODUCT_MAX_CHECKED = "TKPD_ATTACH_PRODUCT_MAX_CHECKED";
        String TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY = "TKPD_ATTACH_PRODUCT_SOURCE";
        String TOKOPEDIA_ATTACH_PRODUCT_HIDDEN = "TKPD_ATTACH_PRODUCT_HIDDEN";
        String TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY = "TKPD_ATTACH_PRODUCT_RESULTS";
    }

    interface Notification {
        String BUYER_HOST = "notif-center";
        String GENERAL_HOST = "notification";
    }

    interface Profile {
        String PARAM_USER_ID = "{user_id}";
    }

    interface DigitalInstantDebit {
        String INSTANT_DEBIT_BCA_APPLINK = "tokopedia://instantdebitbca";
        String INSTANT_DEBIT_BCA_EDITLIMIT_APPLINK = "tokopedia://editbcaoneklik";
    }

    interface Transaction {
        String ORDER_HISTORY = "tokopedia://orderlist/digital";
        String ORDER_DETAIL = "tokopedia://digital/order/{order_id}";
        String ORDER_OMS_DETAIL = "tokopedia://order/{order_id}";
        String ORDER_MARKETPLACE_DETAIL = "tokopedia://marketplace/order/{order_id}";
        String ORDER_MARKETPLACE_DETAIL_WAITING_INVOICE = "tokopedia://marketplace/order?payment_id={payment_id}&cart_string={cart_string}";
        String ORDER_OMS_DETAIL_UPSTREAM = "tokopedia://order/{order_id}?upstream={upstream}";

        String EXTRA_SHOP_ID = "shop_id";
        String EXTRA_PRODUCT_ID = "product_id";
        String EXTRA_NOTES = "notes";
        String EXTRA_QUANTITY = "quantity";
        String EXTRA_SELECTED_VARIANT_ID = "selected_variant_id";
        String EXTRA_ACTION = "action";
        String EXTRA_PRODUCT_IMAGE = "product_image";
        String EXTRA_SHOP_TYPE = "shop_type";
        String EXTRA_SHOP_NAME = "shop_name";
        String EXTRA_OCS = "ocs";
        String EXTRA_NEED_REFRESH = "extra_need_refresh";
        String TRACKER_ATTRIBUTION = "tracker_attribution";
        String TRACKER_LIST_NAME = "tracker_list_name";
        String EXTRA_REFERENCE = "reference";
        String EXTRA_IS_LEASING = "is_leasing";
        String EXTRA_CUSTOM_EVENT_LABEL = "custom_event_label";
        String EXTRA_CUSTOM_EVENT_ACTION = "custom_event_action";
        String EXTRA_CUSTOM_DIMENSION40 = "custom_dimension40";
        String EXTRA_LAYOUT_NAME = "layout_name";
        String EXTRA_ATC_EXTERNAL_SOURCE = "atc_external_source";

        String EXTRA_CATEGORY_ID = "category_id";
        String EXTRA_CATEGORY_NAME = "category_name";
        String EXTRA_CART_ID = "cart_id";
        String EXTRA_PRODUCT_TITLE = "product_title";
        String EXTRA_PRODUCT_PRICE = "product_price";
        String EXTRA_PRODUCT_CONDITION = "product_condition";
        String RESULT_ATC_SUCCESS_MESSAGE = "atc_success_message";

        String ORDER_LIST = "tokopedia-android-internal://transaction/order-list";
    }

    interface Liveness {
        String EXTRA_IS_SUCCESS_REGISTER = "isSuccessRegister";
        String EXTRA_LIST_RETAKE = "listRetake";
        String EXTRA_LIST_MESSAGE = "listMessage";
        String EXTRA_TITLE = "title";
        String EXTRA_SUBTITLE = "subtitle";
        String EXTRA_BUTTON = "button";
    }

    interface ResCenter {
        String MOBILE = "/mobile";
        String RESO_CREATE = "resolution-center/create/%s" + MOBILE;
    }

    interface RewardFallback {

        interface RemoteConfig {
            String APP_SHOW_TOKOPOINT_NATIVE = "app_enable_tokopoint_native";
        }

        interface RewardWebview {
            String REWARD_WEBVIEW = "https://m.tokopedia.com/tokopoints";
        }

        interface Reward {
            String REWARDS = "rewards";
        }
    }

    interface Navigation {
        String MAIN_NAV = "tokopedia://navigation/main";
    }

    interface Inbox {
        String PARAM_PAGE = "page";
        String VALUE_PAGE_NOTIFICATION = "notification";
        String VALUE_PAGE_CHAT = "chat";
        String VALUE_PAGE_TALK = "talk";
        String VALUE_PAGE_REVIEW = "review";

        String PARAM_ROLE = "role";
        String VALUE_ROLE_BUYER = "buyer";
        String VALUE_ROLE_SELLER = "seller";

        String PARAM_SOURCE = "source";
    }

    interface GeneralInfo {
        String GENERAL_INFO_FORCE_CLOSE_PAGE = "tokopedia://general-info-close";
    }
}
