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
    String FEED_EXPLORE = "tokopedia://feed/explore";
    String FEED_VIDEO = "tokopedia://feed/video";
    String FIND = "tokopedia://find";
    String AMP_FIND = "tokopedia://amp/find";
    String FEED_HASHTAG = "tokopedia://feed/hashtag/{hashtag}";
    String FEED_DETAILS = "tokopedia://feedcommunicationdetail";
    String FEED_PlAY_LIVE_DETAIL = "tokopedia://feedplaylivedetail";
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
    String SHOP_ETALASE = "tokopedia://shop/{shop_id_or_domain}/etalase/{etalase_id_or_alias}";
    String SHOP_TALK = "tokopedia://shop/{shop_id}/talk";
    String SHOP_ETALASE_WITH_KEYWORD_AND_SORT = "tokopedia://shop/{shop_id}/etalase/{etalase_id}/?search={search}&sort={sort}";
    String PRODUCT_BUNDLE = "tokopedia://product-bundle/{product_id}";
    String GIFTING = "tokopedia://gifting/{addon_id}";
    String SHOP_REVIEW = "tokopedia://shop/{shop_id}/review?review-source={source}";
    String SHOP_NOTE = "tokopedia://shop/{shop_id}/note";
    String SHOP_INFO = "tokopedia://shop/{shop_id}/info";
    String SHOP_HOME = "tokopedia://shop/{shop_id}/home";
    String SHOP_PRODUCT = "tokopedia://shop/{shop_id}/product";
    String SHOP_FEED = "tokopedia://shop/{shop_id}/feed";
    String SHOP_ETALASE_LIST = "tokopedia://shop/{shop_id}/etalase-list";
    String MY_SHOP_ETALASE_LIST = "tokopedia://my-shop/etalase/list";
    String SHOP_PENALTY = "tokopedia://shop-penalty";
    String SHOP_PENALTY_DETAIL = "tokopedia://shop-penalty-detail";
    String SHOP_FOLLOWER_LIST = "tokopedia://shop/{shop_id}/follower";
    String SHOP_SETTINGS_CUSTOMER_APP = "tokopedia://shop/{shop_id}/settings";
    //shop widget
    String SHOP_OPERATIONAL_HOUR = "tokopedia://shop/{shop_id}/operational-hour";
    String SHOP_MVC_LOCKED_TO_PRODUCT = "tokopedia://shop/{shop_id}/voucher/{voucher_id}";

    String SHOP_SETTINGS_NOTE = "tokopedia://setting/shop/note";
    String SHOP_SETTINGS_INFO = "tokopedia://setting/shop/info";
    String PRODUCT_INFO = "tokopedia://product/{product_id}";
    String PRODUCT_EDUCATIONAL = "tokopedia://product-edu/{type}";
    String PRODUCT_IMAGE_REVIEW = "tokopedia://product/{product_id}/imagereview";
    String PRODUCT_REVIEW = "tokopedia://product/{id}/review";
    String PRODUCT_AR = "tokopedia://productar/{product_id}";
    String ADD_PATH = "add";
    String AFFILIATE_UNIQUE_ID = "aff_unique_id";
    String PRODUCT_ADD = "tokopedia://product/add";
    String DEFAULT_RECOMMENDATION_PAGE = "tokopedia://rekomendasi/";
    String RECOMMENDATION_PAGE = "tokopedia://rekomendasi/{product_id}/?ref={ref}";
    String CREDIT_CARD_AUTH_SETTING = "tokopedia://payment/credit-card";
    String ADD_CREDIT_CARD = "tokopedia://payment/credit-card/add";
    String CART_HOST = "cart";
    String CART = "tokopedia://cart";
    String ATC_EXTERNAL = "tokopedia://cart?product_id={product_id}";
    String CHECKOUT = "tokopedia://checkout";
    String SELLER_NEW_ORDER = "tokopedia://seller/new-order";
    String SELLER_SHIPMENT = "tokopedia://seller/shipment";
    String SELLER_STATUS = "tokopedia://seller/status";
    String SELLER_HISTORY = "tokopedia://seller/history";
    String CREATE_SHOP = "tokopedia://buka-toko-online-gratis";
    String REVIEW_HOST = "review";
    String REPUTATION = "tokopedia://review";
    String REPUTATION_DETAIL = "tokopedia://review/{reputation_id}";
    String REVIEW_DETAIL = "tokopedia://review/detail/{feedback_id}";
    String PRODUCT_CREATE_REVIEW = "tokopedia://product-review/create/";

    String PRODUCT_BULK_CREATE_REVIEW = "tokopedia://product-review/bulk-create/";
    String PRODUCT_REPUTATION = "tokopedia://product/{product_id}/review";
    String SELLER_REVIEW = "tokopedia://seller-review-detail";
    String REVIEW_REMINDER_PREVIOUS = "tokopedia://review-reminder";
    String WEBVIEW_HOST = "webview";
    String WEBVIEW = "tokopedia://webview";
    String WEBVIEW_DOWNLOAD_HOST = "webviewdownload";
    String WEBVIEW_DOWNLOAD = "tokopedia://webviewdownload";
    String WEBVIEW_DOWNLOAD_HTML = "tokopedia://webview-download-html";
    String WEBVIEW_PARENT_HOME_HOST = "webviewbackhome";
    String WEBVIEW_PARENT_HOME = "tokopedia://webviewbackhome";
    String PRODUCT_TALK = "tokopedia://product/{product_id}/talk";
    String DIGITAL = "tokopedia://digital";
    String DIGITAL_PRODUCT = "tokopedia://digital/form";
    String DIGITAL_SUBHOMEPAGE_HOME = "tokopedia://recharge/home";
    String RECHARGE_SUBHOMEPAGE_HOME_NEW = "tokopedia://recharge/home?platform_id=31&personalize=true";
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
    String PROMO_WITH_DASH = "tokopedia://promo/{promo_id}/";
    String PROMO_DETAIL = "tokopedia://promo/{slug}";
    String DISCOVERY_CATEGORY = "tokopedia://category";
    String DISCOVERY_CATEGORY_DETAIL = "tokopedia://category/{DEPARTMENT_ID}";
    String DISCOVERY_SEARCH = "tokopedia://search";
    String DISCOVERY_SEARCH_AUTOCOMPLETE = "tokopedia://search-autocomplete";
    String DISCOVERY_SEARCH_UNIVERSAL = "tokopedia://universal-page";
    String DISCOVERY_HOTLIST_DETAIL = "tokopedia://hot/{alias}";
    String DISCOVERY_CATALOG = "tokopedia://catalog";
    String PAYMENT_BACK_TO_DEFAULT = "tokopedia://payment/backtodefault";
    String WISHLIST = "tokopedia://wishlist";
    String NEW_WISHLIST = "tokopedia://new-wishlist";
    String WISHLIST_COLLECTION_DETAIL = "tokopedia://wishlist/collection/{collection_id}";
    String RECENT_VIEW = "tokopedia://recentlyviewed";
    String HOST_LOGIN = "login";
    String LOGIN = "tokopedia://login";
    String ADD_PHONE = "tokopedia://add-phone";
    String PRIVACY_CENTER = "tokopedia://privacy-center";
    String OTP = "tokopedia://otp";
    String QR_LOGIN = "tokopedia://login/qr";
    String OTP_PUSH_NOTIF_RECEIVER = "tokopedia://otp-verify";
    String OFFICIAL_STORES = "tokopedia://official-stores";
    String OFFICIAL_STORE = "tokopedia://official-store";
    String OFFICIAL_STORE_CATEGORY = "tokopedia://official-store/{key_category}";
    String RESCENTER = "tokopedia://resolution/{resolution_id}";
    String RESCENTER_BUYER = "tokopedia://resolution/list/buyer";
    String RESCENTER_SELLER = "tokopedia://resolution/list/seller";
    String ORDER_HISTORY = "tokopedia://product-order-history";
    String ORDER_HISTORY_SHOP = "tokopedia://product-order-history/{shop_id}";
    String TOPCHAT_HOST = "topchat";
    String TOPCHAT = "tokopedia://topchat/{message_id}";
    String TOP_CHAT = "tokopedia://topchat";
    String TOPCHAT_IDLESS = "tokopedia://topchat";
    String TOPCHAT_OLD_HOST = "topchatold";
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
    String TOKO_CHAT = "tokopedia://tokochat";

    String CHATBOT_HOST = "chatbot";
    String CHATBOT = "tokopedia://chatbot/{message_id}";
    String CHAT_BOT = "tokopedia://chatbot";
    String CHAT_TEMPLATE = "tokopedia://chat/settings/templatechat";
    String REFERRAL = "tokopedia://referral";
    String BROWSER_HOST = "browser";
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
    String ADMIN_INVITATION = "tokopedia://shop-admin/invitation-page";
    String ADMIN_ACCEPTED = "tokopedia://shop-admin/accepted-page";
    String ADMIN_REDIRECTION = "tokopedia://shop-admin/redirection-page";
    String SHOP_SCORE_DETAIL_ACKNOWLEDGE_INTERRUPT = "tokopedia://shop-score-detail/acknowledge-interrupt";

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
    String HOTEL_DASHBOARD = "tokopedia://hotel/dashboard";
    String HOTEL_SRP = "tokopedia://hotel/result";
    String HOTEL_DETAIL = "tokopedia://hotel/detail";
    String TRAVEL_SUBHOMEPAGE = "tokopedia://travelentertainment";
    String TRAVEL_SUBHOMEPAGE_HOME = "tokopedia://travelentertainment/home";

    String OMS_ORDER_DETAIL = "tokopedia://order/";
    String MARKETPLACE_ORDER = "tokopedia://marketplace/order";
    String BUYER_ORDER_EXTENSION = "tokopedia://marketplace/buyer-order-extension";
    String BELANJA_ORDER = "tokopedia://belanja/order";
    String MARKETPLACE_ORDER_SUB = "tokopedia://order/marketplace/filter";
    String MARKETPLACE_ORDER_FILTER = "tokopedia://order/marketplace/filter/{filter_id}";
    String TRAVEL_AND_ENTERTAINMENT_ORDER = "tokopedia://travelent/order";
    String SNAPSHOT_ORDER = "tokopedia://snapshot/order";
    String ORDER_BUYER_CANCELLATION_REQUEST_PAGE = "tokopedia://buyer/cancellationrequest";

    String MARKETPLACE_WAITING_CONFIRMATION = "tokopedia://order/marketplace/filter/5";
    String MARKETPLACE_SENT = "tokopedia://order/marketplace/filter/13";
    String MARKETPLACE_ORDER_PROCESSED = "tokopedia://order/marketplace/filter/12";
    String MARKETPLACE_DELIVERED = "tokopedia://order/marketplace/filter/14";

    String BUYER_INFO = "tokopedia://notif-center";
    String BUYER_INFO_WITH_ID = "tokopedia://notif-center/{notif_id}";
    String SELLER_INFO = "tokopedia://sellerinfo";
    String SELLER_INFO_DETAIL = "tokopedia://sellerinfo/detail";

    String TC_LANDING = "tokopedia://tc-landing";

    String HOST_PLAY_NOTIF_VIDEO = "play-notif-video";
    String PLAY_NOTIFICATION_VIDEO = "tokopedia://play-notif-video";
    String INBOX_TICKET = "tokopedia://customercare";
    String TICKET_DETAIL = "tokopedia://customercare/{ticket_id}";

    String ORDER_TRACKING = "tokopedia://shipping/tracking/{order_id}";
    String LOGISTIC_SELLER_RESCHEDULE = "tokopedia://seller/reschedulepickup";
    String ORDER_POD = "tokopedia://shipping/pod/{order_id}";
    String SHARE_ADDRESS = "tokopedia://share_address";

    String LINK_ACCOUNT = "tokopedia://gojek-account-link";
    String EXPLICIT_PROFILE = "tokopedia://explicit-profile";

    String FLIGHT = "tokopedia://pesawat";
    String FLIGHT_PHONE_VERIFICATION = "tokopedia-android-internal://pesawat/phone-verification";

    String PRODUCT_MANAGE = "tokopedia://seller/product/manage";
    String PRODUCT_DRAFT = "tokopedia://seller/product/draft";
    String PRODUCT_EDIT = "tokopedia://product/edit/{product_id}";
    String SELLER_TRANSACTION = "tokopedia://seller";
    String SELLER_CENTER = "tokopedia://seller/seller-center";
    String SELLER_SHIPPING_EDITOR = "tokopedia://seller/setting/shipping-editor";
    String SELLER_CUSTOM_PRODUCT_LOGISTIC = "tokopedia://seller/setting/custom-product-logistic";
    String SELLER_COD_ACTIVATION = "tokopedia://seller/setting/cod-activation";
    String SELLER_WAREHOUSE_DATA = "tokopedia://seller/setting/shop-address";
    String CONTACT_US = "tokopedia://contact-us";
    String CONTACT_US_NATIVE = "tokopedia://contactus";
    String RESOLUTION_SUCCESS = "tokopedia://resolution/success-create?url={redirect_url}";
    String TOKOPEDIA_CARE_HELP = "tokopedia://webview?url=https://www.tokopedia.com/help/seller";

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
    String SETTING_ADDRESS = "tokopedia://setting/address";
    String SETTING_EDIT_ADDRESS = "tokopedia://setting/editaddress/";

    String NOTIFICATION = "tokopedia://notification";
    String NOTIFICATION_TROUBLESHOOTER = "tokopedia://notification-troubleshooter";
    String PMS = "tokopedia://buyer/payment";
    String GOPAY_KYC = "tokopedia://payment/gopayKyc";
    String PURCHASE_ORDER = "tokopedia://buyer/order";
    String PURCHASE_ORDER_DETAIL = "tokopedia://buyer/order/{order_id}";
    String PURCHASE_CONFIRMED = "tokopedia://buyer/confirmed";
    String PURCHASE_PROCESSED = "tokopedia://buyer/processed";
    String PURCHASE_SHIPPING_CONFIRM = "tokopedia://buyer/shipping-confirm";
    String PURCHASE_SHIPPED = "tokopedia://buyer/shipped";
    String PURCHASE_DELIVERED = "tokopedia://buyer/delivered";
    String PURCHASE_HISTORY = "tokopedia://buyer/history";
    String PURCHASE_ONGOING = "tokopedia://buyer/ongoing-order";
    String TOKOPEDIA_PLUS_ORDER = "tokopedia://plus/order";

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

    String HAS_PASSWORD = "tokopedia://settings/haspassword";
    String SETTING_BANK = "tokopedia://settings/bankaccount";
    String SETTING_NOTIFICATION = "tokopedia://settings/notification";

    String CONTENT_EXPLORE = "tokopedia://content/explore/{tab_name}/{category_id}";
    String CONTENT_DETAIL = "tokopedia://content/{post_id}";

    String AFFILIATE_PRODUCT_PICKER_FROM_SHOP_NO_PARAM = "tokopedia://productpickerfromshop";
    String AFFILIATE_PRODUCT_PICKER_FROM_SHOP_HOST = "productpickerfromshop";
    String AFFILIATE_DEFAULT_CREATE_POST_V2 = "tokopedia://affiliate/create_post_v2";
    String AFFILIATE_DASHBOARD = "tokopedia://affiliate/dashboard";
    String AFFILIATE_ONBOARDING = "tokopedia://affiliate/onboarding";
    String AFFILIATE_PRODUCT = "tokopedia://affiliate/product/{product_id}";
    String AFFILIATE = "tokopedia://affiliate";
    String FEED_CREATION_PRODUCT_SEARCH = "tokopedia://feed/creation-product-search";
    String FEED_CREATION_SHOP_SEARCH = "tokopedia://feed/creation-shop-search";

    String PLAY_DETAIL = "tokopedia://play/{channel_id}";
    String PLAY_BROADCASTER = "tokopedia://play-broadcaster";
    String PLAY_SHORTS = "tokopedia://play-shorts";

    String CHALLENGE = "tokopedia://challenges";
    String ADD_NAME_REGISTER = "tokopedia://addnameregister/{phone}";
    String ADD_NAME_PROFILE = "tokopedia://addname";
    String RESET_PASSWORD = "tokopedia://resetpassword";
    String PHONE_VERIFICATION = "tokopedia://phoneverification";
    String CHANGE_INACTIVE_PHONE = "tokopedia://changeinactivephone";
    String INPUT_INACTIVE_NUMBER = "tokopedia://inputinactivenumber";
    String ADD_PIN_ONBOARD = "tokopedia://add-pin-onboarding";
    String ADD_FINGERPRINT_ONBOARDING = "tokopedia://add-fingerprint-onboarding";

    String KYC_NO_PARAM = "tokopedia://kyc";
    String KYC_SELLER_DASHBOARD = "tokopedia://kyc?source=seller";
    String KYC = "tokopedia://kyc?projectId=1";

    String KYC_FORM_NO_PARAM = "tokopedia://kyc-form";
    String KYC_FORM = "tokopedia://kyc-form?projectId={projectId}";
    String KYC_FORM_ONLY_NO_PARAM = "tokopedia://user-identification-only";
    String KYC_FORM_ONLY = "tokopedia://user-identification-only?projectId={projectId}&showIntro={showIntro}&redirectUrl={redirectUrl}&type={type}";

    String IMAGE_PREVIEW = "tokopedia://imagepreview";

    String HOME_CREDIT = "home-credit";
    String FINTECH = "fintech";
    String KTP = "ktp";
    String SELFIE = "selfie";
    String HOME_CREDIT_KTP_WITHOUT_TYPE = "tokopedia://fintech/home-credit/ktp";
    String HOME_CREDIT_KTP_WITH_TYPE = "tokopedia://fintech/home-credit/ktp/{type}";
    String HOME_CREDIT_SELFIE_WITHOUT_TYPE = "tokopedia://fintech/home-credit/selfie";
    String HOME_CREDIT_SELFIE_WITH_TYPE = "tokopedia://fintech/home-credit/selfie/{type}";
    String PAYLATER = "tokopedia://fintech/paylater";
    String ACTIVATION_GOPAY = "tokopedia://fintech/activate_gopay";
    String OPTIMIZED_CHECKOUT = "tokopedia://fintech/opt-checkout";
    String INBOX_HOST = "inbox";
    String INBOX = "tokopedia://inbox";

    String PLAY_WEBVIEW = "tokopedia://play/webview?url={url}&titlebar={has_titlebar}";
    String SMC_REFERRAL = "tokopedia://smc-referral";

    String CATEGORY_BELANJA = "tokopedia://category_belanja/{CATEGORY_NAME}";

    String POWER_MERCHANT_SUBSCRIBE = "tokopedia://power_merchant/subscribe";
    String PM_BENEFIT_PACKAGE = "tokopedia://power_merchant/benefit_package";
    String POWER_MERCHANT_PRO_INTERRUPT = "tokopedia://power_merchant/interrupt";

    String CATEGORY_HOST = "category";
    String CATEGORY = "tokopedia://category";
    String TRADEIN = "tokopedia://category/tradein";
    String EPHARMACY = "tokopedia://epharmacy";
    String AFFILIATE_TOKO_HOST = "affiliate";
    String AFFILIATE_TOKO = "tokopedia://affiliate";
    String AFFILIATE_TOKO_HELP = "tokopedia://affiliate/help";
    String AFFILIATE_TOKO_TRANSACTION_HISTORY = "tokopedia://affiliate/transaction-history";
    String AFFILIATE_TOKO_ONBOARDING = "tokopedia://affiliate/onboarding";
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
    String SALAM_UMRAH_SHOP_ID = "7298319";
    String SALAM_UMRAH_SHOP = "tokopedia://shop/" + SALAM_UMRAH_SHOP_ID;
    String THANK_YOU_PAGE_NATIVE = "tokopedia://payment/thankyou";
    String THANKYOU_PAGE_NATIVE = "tokopedia://payment/thankyou?payment_id={payment_id}&merchant={merchant_code}";

    String SALAM_UMRAH_AGEN = "tokopedia://s/umroh/agen/{slug}";
    String SALAM_UMRAH_LIST_AGEN = "tokopedia://s/umroh/agen";

    String MERCHANT_VOUCHER_LIST = "tokopedia://merchant-voucher/list";

    String DFFALLBACKURL_KEY = "dffallbackurl";

    String OCC = "tokopedia://occ";

    String SELLER_MIGRATION = "tokopedia://seller/seller-migration";

    String ACCOUNT_HOST = "account";
    String ACCOUNT = "tokopedia://account";
    String MARKETPLACE_ONBOARDING = "tokopedia://marketplace/onboarding";

    String TELEPHONY_MASKING = "tokopedia://telephony-masking";

    String SHARING_HOST = "sharing";
    String GLOBAL_SHARING = "tokopedia://sharing?text={text}&image={image}&type={type}";

    String WEB_HOST = "www.tokopedia.com";

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
        String CATEGORY_EXPLORE_HOST = "category-explore";
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
        String COUPON_LIST = "kupon-saya";
        String HISTORY = "tokopedia://tokopoints/history";
    }

    String OQR_PIN_URL_ENTRY_LINK = "tokopedia://ovoqrthanks/";
    String DISCOVERY = "tokopedia://discovery";

    String MONEYIN = "tokopedia://money_in/device_validation";
    String IMAGE_PICKER_V2 = "tokopedia://image-picker/v2";

    interface WebViewUrl {
        String SALDO_DETAIL = "https://m.tokopedia.com/deposit";
    }

    interface CustomerApp {
        String TOPADS_DASHBOARD = "tokopedia://topads/dashboard";
    }

    interface SellerApp {
        String SELLER_ONBOARDING = "sellerapp://welcome";
        String PRODUCT_ADD = "sellerapp://product/add";
        String SHOP_PAGE_PRODUCTS_CREATE_SHOWCASE = "sellerapp://shop/showcase-create";
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
        String TOPADS_ONBOARDING = "sellerapp://topads/topads-onboarding";
        String GOLD_MERCHANT = "sellerapp://gold";
        String SELLER_APP_HOME = "sellerapp://home";
        String TOPADS_DASHBOARD = "sellerapp://topads";
        String POWER_MERCHANT_SUBSCRIBE = "sellerapp://power_merchant/subscribe";
        String PM_BENEFIT_PACKAGE = "sellerapp://power_merchant/benefit-package";
        String BROWSER = "sellerapp://browser";
        String WEBVIEW = "sellerapp://webview";
        String VOUCHER_LIST = "sellerapp://voucher-list";
        String VOUCHER_ACTIVE = "sellerapp://voucher-list/active";
        String VOUCHER_HISTORY = "sellerapp://voucher-list/history";
        String VOUCHER_PRODUCT_LIST = "sellerapp://voucher-product-list";
        String VOUCHER_DETAIL = "sellerapp://voucher-detail";
        String VOUCHER_PRODUCT_DETAIL = "sellerapp://voucher-product-detail";
        String CAMPAIGN_LIST = "sellerapp://campaign-list";
        String CREATE_VOUCHER = "sellerapp://create-voucher";
        String CREATE_VOUCHER_PRODUCT = "sellerapp://create-voucher-product";
        String SELLER_SEARCH = "sellerapp://seller-search";
        String PLAY_BROADCASTER = "sellerapp://play-broadcaster";
        String CENTRALIZED_PROMO = "sellerapp://centralized-promo";
        String SHOP_FEED = "sellerapp://shop/{shop_id}/feed";
        String SELLER_SHIPPING_EDITOR = "sellerapp://setting/shipping-editor";
        String STATISTIC_DASHBOARD = "sellerapp://gold-merchant-statistic-dashboard";
        String SHOP_SETTINGS_SELLER_APP = "sellerapp://shop/{shop_id}/settings";
        String TOPADS_CREATE_MANUAL_ADS = "tokopedia://topads/create-manual-ads";
        String REVIEW_REMINDER = "sellerapp://review-reminder";
        String SHOP_SCORE_DETAIL = "sellerapp://shop-score-detail";
        String SELLER_SHOP_FLASH_SALE = "sellerapp://shop-flash-sale";
        String SELLER_TOKOPEDIA_FLASH_SALE = "sellerapp://tokopedia-flash-sale";
        String SELLER_TOKOPEDIA_FLASH_SALE_CAMPAIGN_DETAIL = "sellerapp://tokopedia-flash-sale/campaign-detail/{campaign_id}";
        String SHOP_DISCOUNT = "sellerapp://shop-discount";
        String TOKOMEMBER = "sellerapp://tokomember";
        String TOKOMEMBER_PROGRAM_LIST = "sellerapp://tokomember/program-list";
        String TOKOMEMBER_COUPON_LIST = "sellerapp://tokomember/coupon-list";
        String TOKOMEMBER_PROGRAM_CREATION = "sellerapp://tokomember/program-creation";
        String TOKOMEMBER_COUPON_CREATION = "sellerapp://tokomember/coupon-creation";
        String TOKOMEMBER_PROGRAM_EXTENSION = "sellerapp://tokomember/program-extension/{program_id}";
        String ADMIN_INVITATION = "sellerapp://shop-admin/invitation-page";
        String ADMIN_ACCEPTED = "sellerapp://shop-admin/accepted-page";
        String ADMIN_REDIRECTION = "sellerapp://shop-admin/redirection-page";
        String PRODUCT_MANAGE = "sellerapp://product/manage";

    }

    interface TokopediaNow {
        String HOME = "tokopedia://now";
        String TOKOPEDIA_NOW_PRODUCTION_SHOP_ID_1 = "11515028";
        String TOKOPEDIA_NOW_PRODUCTION_SHOP_ID_2 = "11530573";
        String TOKOPEDIA_NOW_STAGING_SHOP_ID = "480552";
        String SEARCH = "tokopedia://now/search";
        String CATEGORY = "tokopedia://now/category";
        String REPURCHASE = "tokopedia://now/repurchase-page";
        String RECIPE_DETAIL = "tokopedia://now/recipe/detail/{recipe_id}";
        String RECIPE_BOOKMARK = "tokopedia://now/recipe/bookmarks";
        String RECIPE_HOME = "tokopedia://now/recipe";
        String RECIPE_SEARCH = "tokopedia://now/recipe/search";
        String RECIPE_AUTO_COMPLETE = "tokopedia://now/recipe/autocomplete";
    }

    interface TokoFood {
        String GOFOOD = "tokopedia://gofood";
        String MAIN_PATH = "tokopedia://food";
        String HOME = "tokopedia://food/home";
        String CATEGORY = "tokopedia://food/category";
        String MERCHANT = "tokopedia://food/merchant/{merchantId}?product_id={product_id}";
        String POST_PURCHASE = "tokopedia://food/postpurchase/{orderId}";
        String TOKOFOOD_ORDER = "tokopedia://food/order";
        String SEARCH = "tokopedia://food/search";
    }

    interface TokoMart {
        String HOME = "tokopedia://tokomart";
        String SEARCH = "tokopedia://tokomart/search";
        String CATEGORY = "tokopedia://tokomart/category";
        String REPURCHASE = "tokopedia://tokomart/repurchase-page";
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

    interface Tokomember{
        String MAIN_PATH = "sellerapp://tokomember";
        String PROGRAM_EXTENSION = "/tokomember/program-extension";
        String COUPON_DETAIL = "/tokomember/coupon-detail";
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
        String TOKOPEDIA_ATTACH_PRODUCT_MAX_CHECKED = "TKPD_ATTACH_PRODUCT_MAX_CHECKED";
        String TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY = "TKPD_ATTACH_PRODUCT_SOURCE";
        String TOKOPEDIA_ATTACH_PRODUCT_HIDDEN = "TKPD_ATTACH_PRODUCT_HIDDEN";
        String TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY = "TKPD_ATTACH_PRODUCT_RESULTS";
        String TOKOPEDIA_ATTACH_PRODUCT_WAREHOUSE_ID = "TKPD_ATTACH_PRODUCT_WAREHOUSE_ID";
    }

    interface Notification {
        String BUYER_HOST = "notif-center";
        String GENERAL_HOST = "notification";
    }

    interface Profile {
        String PARAM_USER_ID = "{user_id}";
    }

    interface Transaction {
        String ORDER_HISTORY = "tokopedia://orderlist/digital";
        String ORDER_DETAIL = "tokopedia://digital/order/{order_id}";
        String ORDER_OMS_DETAIL = "tokopedia://order/{order_id}";
        String ORDER_MARKETPLACE_DETAIL = "tokopedia://marketplace/order/{order_id}";
        String ORDER_MARKETPLACE_DETAIL_WAITING_INVOICE = "tokopedia://marketplace/order?payment_id={payment_id}&cart_string={cart_string}";
        String ORDER_OMS_DETAIL_UPSTREAM = "tokopedia://order/{order_id}?upstream={upstream}";

        String EXTRA_CART_ID = "cart_id";
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
        String PARAM_SHOW_BOTTOM_NAV = "show_bottom_nav";
    }

    interface MediaPicker {
        String MEDIA_PICKER = "tokopedia://media-picker";
        String MEDIA_PICKER_PREVIEW = "tokopedia://media-picker-preview";

        // this param will determine which page should be landed first
        String PARAM_LANDING_PAGE = "start";

        String VALUE_CAMERA_PAGE = "0"; // camera page
        String VALUE_GALLERY_PAGE = "1"; // gallery page
    }

    interface MediaEditor {
        String MEDIA_EDITOR = "tokopedia://media-editor";
    }

    interface GeneralInfo {
        String GENERAL_INFO_FORCE_CLOSE_PAGE = "tokopedia://general-info-close";
    }

    interface TokoChat {
        String PARAM_SOURCE = "tokochatSource";
        String ORDER_ID_GOJEK = "orderIdGojek";
        String ORDER_ID_TKPD = "orderIdTkpd";

        //bundle params
        String IS_FROM_TOKOFOOD_POST_PURCHASE = "isFromTokoFoodPostPurchase";
    }
}
