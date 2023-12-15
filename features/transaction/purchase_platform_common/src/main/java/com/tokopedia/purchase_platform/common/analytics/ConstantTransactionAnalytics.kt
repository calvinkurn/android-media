package com.tokopedia.purchase_platform.common.analytics

/**
 * @author anggaprasetiyo on 18/05/18.
 */
object ConstantTransactionAnalytics {
    object Key {
        const val PROMO_ID = "promoId"
        const val E_COMMERCE = "ecommerce"
        const val PAYMENT_ID = "payment_id"
        const val CURRENT_SITE = "currentSite"
        const val PROMOTIONS = "promotions"
        const val ID = "id"
        const val NAME = "name"
        const val CREATIVE = "creative"
        const val POSITION = "position"
        const val PROMO_ID_ = "promo_id"
        const val PROMO_CODE = "promo_code"
        const val BRAND = "brand"
        const val CATEGORY = "category"
        const val CATEGORY_ID = "category_id"
        const val PRICE = "price"
        const val QUANTITY = "quantity"
        const val SHOP_ID = "shop_id"
        const val SHOP_NAME = "shop_name"
        const val SHOP_TYPE = "shop_type"
        const val VARIANT = "variant"
        const val CREATIVE_NAME = "creative_name"
        const val CREATIVE_SLOT = "creative_slot"
        const val DIMENSION40 = "dimension40"
        const val ITEM_ID = "item_id"
        const val ITEM_NAME = "item_name"
        const val ITEM_VARIANT = "item_variant"
        const val ITEM_BRAND = "item_brand"
    }

    object EventName {
        const val CLICK_ATC = "clickATC"
        const val CLICK_BUY = "clickBuy"
        const val CLICK_CHECKOUT = "clickCheckout"
        const val VIEW_ATC = "viewATC"
        const val VIEW_ATC_IRIS = "viewATCIris"
        const val REMOVE_FROM_CART = "removeFromCart"
        const val CHECKOUT = "checkout"
        const val ADD_TO_CART = "addToCart"
        const val CLICK_COURIER = "clickCourier"
        const val CLICK_COUPON = "clickCoupon"
        const val VIEW_COURIER = "viewCourier"
        const val CLICK_SHIPPING = "clickShipping"
        const val VIEW_SHIPPING = "viewShipping"
        const val VIEW_PROMO = "viewPromo"
        const val CLICK_ADDRESS = "clickAddress"
        const val PRODUCT_CLICK = "productClick"
        const val PRODUCT_VIEW = "productView"
        const val PROMO_VIEW = "promoView"
        const val PROMO_CLICK = "promoClick"
        const val PURCHASE_PROTECTION_CLICK = "clickFintechMicrosite"
        const val PURCHASE_PROTECTION_IMPRESSION = "viewFintechMicrositeIris"
        const val VIEW_CART = "viewCart"
        const val VIEW_CART_IRIS = "viewCartIris"
        const val CLICK_CART = "clickCart"
        const val VIEW_REGISTER = "viewRegister"
        const val CLICK_CHECKOUT_EXPRESS = "clickCheckoutExpress"
        const val VIEW_CHECKOUT_EXPRESS_IRIS = "viewCheckoutExpressIris"
        const val VIEW_SOM = "viewSOM"
        const val VIEW_COURIER_IRIS = "viewCourierIris"
        const val VIEW_TRADEIN = "viewTradeIn"
        const val CLICK_TRADEIN = "clickTradeIn"
        const val CART = "Cart"
        const val CLICK_WISHLIST = "clickWishlist"
        const val CLICK_NAVIGATION_DRAWER = "clickNavigationDrawer"
        const val VIEW_CHECKOUT_IRIS = "viewCheckoutIris"
        const val VIEW_ORDER_IRIS = "viewOrderIris"
        const val CLICK_ORDER = "clickOrder"
        const val VIEW_ITEM = "view_item"
        const val CLICK_PP = "clickPP"
        const val VIEW_PP_IRIS = "viewPPIris"
        const val CLICK_CX = "clickCX"
        const val SELECT_CONTENT = "select_content"
        const val REMOVE_FROM_CART_V2 = "remove_from_cart"
        const val CLICK_PG = "clickPG"
        const val VIEW_PG_IRIS = "viewPGIris"
        const val CLICK_DIGITAL = "clickDigital"
    }

    object EventCategory {
        const val CART = "cart"
        const val EMPTY_CART = "empty cart"
        const val COURIER_SELECTION = "courier selection"
        const val CART_CHANGE_ADDRESS = "cart change address"
        const val CART_MULTIPLE_ADDRESS = "cart multiple address"
        const val PURCHASE_PROTECTION = "fin - mp checkout"
        const val EXPRESS_CHECKOUT = "express checkout"
        const val COURIER_SELECTION_TRADE_IN = "courier selection trade in"
        const val TRACK_SOM = "track seller order management"
        const val INSURANCE_INFO_TOOLTIP = "disclaimer box - insurance page"

        // OCC
        const val PURCHASE_SETTING = "purchase setting"
        const val PURCHASE_PROTECTION_OCC = "fin - order summary occ"
        const val ORDER_SUMMARY = "order summary"

        // cross_sell
        const val BU_RECHARGE = "recharge"
        const val POSITION = "position"
        const val DIGITAL_PRODUCT_NAME = "digital_product_name"
        const val CREATIVE = "creative"
        const val ACTION_FIELD = "actionField"
        const val OPTION = "option"
        const val STEP = "step"
        const val PRODUCTS = "products"
    }

    object EventAction {
        const val CLICK_ALL_COURIER_SELECTED = "click all courier selected"
        const val CLICK_GUNAKAN_FROM_PILIH_MERCHANT_VOUCHER = "click gunakan from pilih merchant voucher"
        const val CLICK_GUNAKAN_ON_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER = "click gunakan on merchant voucher from pilih merchant voucher"
        const val CLICK_HAPUS_ON_TOP_RIGHT_CORNER = "click hapus on top right corner"
        const val CLICK_SHOP = "click - shop - login"
        const val CLICK_PRODUCT_NAME = "click product name"
        const val CLICK_BUTTON_PLUS = "click button +"
        const val CLICK_BUTTON_MIN = "click button -"
        const val CLICK_TRASH_BIN = "click trash bin"
        const val CLICK_ARROW_BACK = "click arrow back"
        const val CLICK_BELANJA_SEKARANG_ON_EMPTY_CART = "click belanja sekarang on empty cart"
        const val CLICK_GUNAKAN_KODE_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON = "click gunakan kode from gunakan kode promo atau kupon"
        const val CLICK_KUPON_SAYA_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON = "click kupon saya from gunakan kode promo atau kupon"
        const val CLICK_KODE_PROMO_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON = "click kode promo from gunakan kode promo atau kupon"
        const val CLICK_KUPON_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON = "click kupon from gunakan kode promo atau kupon"
        const val CLICK_BACK_ARROW = "click back arrow"
        const val CLICK_GANTI_ALAMAT_ATAU_KIRIM_KE_BEBERAPA_ALAMAT = "click ganti alamat atau kirim ke beberapa alamat"
        const val CLICK_SUBTOTAL = "click subtotal"
        const val CLICK_ASURANSI_PENGIRIMAN = "click asuransi pengiriman"
        const val CLICK_INSURANCE_INFO_TOOLTIP = "click button info tool tip"
        const val CLICK_DROPSHIP = "click dropship"
        const val CLICK_INPUT_QUANTITY = "click input quantity"
        const val CLICK_TULIS_CATATAN = "click tulis catatan"
        const val CLICK_TAMBAH_ALAMAT_BARU_FROM_GANTI_ALAMAT = "click tambah alamat baru from ganti alamat"
        const val CLICK_UBAH_FROM_PILIH_ALAMAT_LAINNYA = "click ubah from pilih alamat lainnya"
        const val CLICK_CHECKLIST_ALAMAT_FROM_PILIH_ALAMAT_LAINNYA = "click checklist alamat from pilih alamat lainnya"
        const val IMPRESSION_CHANGE_ADDRESS = "impression change address"
        const val SUBMIT_SEARCH_FROM_PILIH_ALAMAT_LAINNYA = "submit search from pilih alamat lainnya"
        const val CLICK_X_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON = "click x from gunakan kode promo atau kupon"
        const val CLICK_ARROW_BACK_FROM_HAPUS = "click arrow back from hapus"
        const val CLICK_ARROW_BACK_FROM_GANTI_ALAMAT = "click arrow back from ganti alamat"
        const val CLICK_PILIH_METODE_PEMBAYARAN = "click pilih metode pembayaran"
        const val IMPRESSION_CART_EMPTY = "impression cart empty"
        const val IMPRESSION_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER = "impression merchant voucher from pilih merchant voucher"
        const val CLICK_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER = "click merchant voucher from pilih merchant voucher"
        const val IMPRESSION_ON_POP_UP_KUPON = "impression on pop up kupon"
        const val CLICK_HAPUS_FROM_TRASH_BIN = "click hapus from trash bin"
        const val CLICK_HAPUS_PRODUK_BERKENDALA = "click hapus produk berkendala"
        const val CLICK_HAPUS_FROM_HAPUS_PRODUK_BERKENDALA = "click hapus from hapus produk berkendala"
        const val VALIDATION_ERROR_VOUCHER_PROMO_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON = "validation error voucher promo from gunakan kode promo atau kupon"
        const val CLICK_KUPON_FROM_KUPON_SAYA = "click kupon from kupon saya"
        const val CLICK_BACK_ARROW_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON = "click back arrow from gunakan kode promo atau kupon"
        const val CLICK_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS = "click kota atau kecamatan pada + address"
        const val CLICK_KODE_POS_PADA_TAMBAH_ADDRESS = "click kode pos pada + address"
        const val CLICK_TANDAI_LOKASI_PADA_TAMBAH_ADDRESS = "click tandai lokasi pada + address"
        const val CLICK_TAMBAH_ALAMAT_FROM_TAMBAH = "click tambah alamat from +"
        const val CLICK_CHECKLIST_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS = "click checklist kota atau kecamatan pada + address"
        const val CLICK_X_POJOK_KANAN_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS = "click x pojok kanan kota atau kecamatan pada + address"
        const val CLICK_X_POJOK_KIRI_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS = "click x pojok kiri kota atau kecamatan pada + address"
        const val CLICK_CHECKLIST_KODE_POS_PADA_TAMBAH_ADDRESS = "click checklist kode pos pada + address"
        const val CLICK_FILL_KODE_POS_PADA_TAMBAH_ADDRESS = "click fill kode pos pada + address"
        const val VIEW_VALIDATION_ERROR_NOT_FILL = "validation error not fill"
        const val CLICK_DROPDOWN_SUGGESTION_TANDAI_LOKASI_PADA_TAMBAH_ADDRESS = "click dropdown suggestion tandai lokasi pada + address"
        const val CLICK_V_TANDAI_LOKASI_PADA_TAMBAH_ADDRESS = "click v tandai lokasi pada + address"
        const val CLICK_BACK_ARROW_TANDAI_LOKASI_PADA_TAMBAH_ADDRESS = "click back arrow tandai lokasi pada + address"
        const val CLICK_PIN_BUTTON_FROM_TANDAI_LOKASI = "click pin button from tandai lokasi"
        const val VIEW_VALIDATION_ERROR_TANDAI_LOKASI = "validation error tandai lokasi"
        const val CLICK_RADIO_BUTTON_FROM_PILIH_ALAMAT_LAINNYA = "click radio button from pilih alamat lainnya"
        const val CLICK_TAMBAH_FROM_ALAMAT_PENGIRIMAN = "click + from alamat pengiriman"
        const val CLICK_ALAMAT_SEBAGAI_PADA_TAMBAH_ADDRESS = "click alamat sebagai pada + address"
        const val CLICK_CHECKLIST_ALAMAT_SEBAGAI_PADA_TAMBAH_ADDRESS = "click checklist alamat sebagai pada + address"
        const val CLICK_NAMA_PADA_TAMBAH_ADDRESS = "click nama pada + address"
        const val CLICK_TELEPON_PADA_TAMBAH_ADDRESS = "click telepon pada + address"
        const val CLICK_ALAMAT_PADA_TAMBAH_ADDRESS = "click alamat pada + address"
        const val VIEW_CART_LIST = "view cart list"
        const val VIEW_CART_PAGE = "view cart page"
        const val VIEW_CHECKOUT_PAGE = "view checkout page"
        const val CLICK_BELI_ON_WISHLIST = "click - beli on wishlist"
        const val CLICK_BELI_ON_RECENT_VIEW_PAGE = "click - beli on recent view page"
        const val IMPRESSION_ERROR_COURIER_NO_AVAILABLE = "impression error courier no available"
        const val CLICK_MORE_LIKE_THIS = "click more like this"
        const val CLICK_PRODUCT_WISHLIST_ON_CART_LIST = "click product wishlist on cart list"
        const val CLICK_PRODUCT_LAST_SEEN_ON_CART_LIST = "click product last seen on cart list"
        const val CLICK_DONATION = "click donation"
        const val VIEW_AUTO_CHECK_ON_DONATION = "view autocheck on donation"
        const val IMPRESSION_CROSS_SELL_ICON = "impression cross sell icon"
        const val CHECK_CROSS_SELL_ICON = "check cross sell icon"
        const val UNCHECK_CROSS_SELL_ICON = "uncheck cross sell icon"
        const val CLICK_PILIH_METODE_PEMBAYARAN_CROSS_SELL = "click pilih metode pembayaran - cross sell"
        const val CLICK_PAYMENT_OPTION_BUTTON = "click payment option button"

        const val VALIDATION_ERROR_ALAMAT_SEBAGAI_PADA_TAMBAH_ADDRESS = "validation error alamat sebagai pada + address"
        const val VALIDATION_ERROR_NAMA_PADA_TAMBAH_ADDRESS = "validation error nama pada + address"
        const val VALIDATION_ERROR_TELEPON_PADA_TAMBAH_ADDRESS = "validation error telepon pada + address"
        const val VALIDATION_ERROR_KOTA_KECAMATAN_PADA_TAMBAH_ADDRESS = "validation error kota / kecamatan pada + address"
        const val VALIDATION_ERROR_KODE_POS_PADA_TAMBAH_ADDRESS = "validation error kode pos pada + address"
        const val VALIDATION_ERROR_ALAMAT_PADA_TAMBAH_ADDRESS = "validation error alamat pada + address"

        // PHASE 2
        const val CLICK_PILIH_ALAMAT_LAIN = "click pilih alamat lain"
        const val CLICK_TOP_DONASI = "click top donasi"
        const val CLICK_CHECKOUT = "click checkout"
        const val CLICK_PILIH_SEMUA_PRODUK = "click pilih semua produk"
        const val CLICK_BAYAR = "click bayar"
        const val CLICK_UBAH_KURIR = "click ubah kurir"
        const val VIEW_ERROR_ON_CHECKOUT = "view error when checkout"

        // ROBINHOOD
        const val CLICK_X_PADA_DURASI_PENGIRIMAN = "click x pada durasi pengiriman"
        const val CLICK_CHECKLIST_PILIH_DURASI_PENGIRIMAN = "click checklist pilih durasi pengiriman"
        const val VIEW_PRESELECTED_COURIER_OPTION = "view preselected courier option"
        const val CLICK_CHANGE_COURIER_OPTION = "click change courier option"
        const val CLICK_X_PADA_KURIR_PENGIRIMAN = "click x pada kurir pengiriman"
        const val CLICK_UBAH_DURASI = "click ubah durasi"
        const val VIEW_DURATION = "view duration"
        const val VIEW_COURIER_OPTION = "view courier option"

        // EMPTY CART
        const val CLICK_PRODUCT_WISHLIST = "click product wishlist on empty cart"
        const val CLICK_PRODUCT_LAST_SEEN = "click product last seen on empty cart"
        const val CLICK_PRODUCT_RECOMMENDATION = "click on product recommendation"
        const val VIEW_PRODUCT = "view product"

        // PURCHASE PROTECTION
        const val CLICK_PURCHASE_PROTECTION_PAY = "pp - bayar click"
        const val IMPRESSION_PELAJARI = "pp - ins section impression"

        // TOKOPEDIA CORNER
        const val VIEW_CORNER_ERROR = "view tokopedia corner not available"
        const val VIEW_CORNER_PO_ERROR = "view error pre order tokopedia corner"
        const val CLICK_PLUS_FROM_MULTIPLE = "click + from multiple"
        const val CLICK_BUTTON_SIMPAN = "click button simpan"
        const val CLICK_BUTTON_SIMPAN_FROM_EDIT = "click button simpan from edit"

        // TRADEIN
        const val VIEW_CHECKOUYT_PAGE_TRADE_IN = "view checkout page trade in"
        const val CLICK_KURIR_TRADE_IN = "click kurir trade in"
        const val CLICK_JEMPUT_TAB = "click - jemput tab"
        const val CLICK_DROP_OFF_TAB = "click - drop off tab"

        // PROMO STACKING
        const val CLICK_PROMO_LOGISTIC_TICKER = "click promo logistic ticker"
        const val CLICK_LANJUTKAN_TERAPKAN_PROMO = "click lanjutkan di pop up terapkan promo"
        const val VIEW_PROMO_LOGISTIC_TICKER = "view promo logistic ticker"
        const val CLICK_X_ON_PROMO_STACKING_LOGISTIC = "click x on promo stacking logistic"
        const val VIEW_INFORMATION_AND_WARNING_TICKER_IN_CHECKOUT = "view information and warning ticker in checkout"
        const val VIEW_POP_UP_PRICE_INCREASE = "view pop up price increase"
        const val VIEW_PROMO_LOGISTIC_TICKER_DISABLE = "view promo logistic ticker disable"

        // Promo not eligible bottomsheet
        const val CLICK_LANJUTKAN_ON_ERROR_PROMO_CONFIRMATION = "click lanjutkan on error promo confirmation"
        const val CLICK_BATAL_ON_ERROR_PROMO_CONFIRMATION = "click batal on error promo confirmation"
        const val VIEW_POP_UP_ERROR_PROMO_CONFIRMATION = "view pop up error promo confirmation"
        const val SEARCH_NOT_FOUND = "search result not found"

        // WIDGET_RECOMMENDATION
        const val IMPRESSION_ON_PRODUCT_RECOMMENDATION = "impression on product recommendation"
        const val CLICK_ADD_TO_CART = "click add to cart"
        const val CLICK_ADD_WISHLIST_ON_PRODUCT_RECOMMENDATION = "click add wishlist on product recommendation"
        const val CLICK_ADD_WISHLIST_ON_PRODUCT_RECOMMENDATION_EMPTY_CART = "click add wishlist on product recommendation - empty_cart"
        const val CLICK_REMOVE_WISHLIST_ON_PRODUCT_RECOMMENDATION = "click remove wishlist on product recommendation"
        const val CLICK_REMOVE_WISHLIST_ON_PRODUCT_RECOMMENDATION_EMPTY_CART = "click remove wishlist on product recommendation - empty_cart"

        // Retry Pickup
        const val VIEW_TUNGGU_CARI_DRIVER = "view tunggu cari driver"
        const val VIEW_BUTTON_CARI_DRIVER = "view button cari driver"
        const val CLICK_BUTTON_CARI_DRIVER = "click button cari driver"

        // Wishlist
        const val ADD_WISHLIST_AVAILABLE_SECTION = "add wishlist - available section - login"
        const val ADD_WISHLIST_UNAVAILABLE_SECTION = "add wishlist - unavailable section - login"
        const val REMOVE_WISHLIST_UNAVAILABLE_SECTION = "remove wishlist - unavailable section - login"
        const val ADD_WISHLIST_LAST_SEEN = "add wishlist - last seen - login"
        const val REMOVE_WISHLIST_LAST_SEEN = "remove wishlist - last seen - login"
        const val ADD_WISHLIST_WISHLIST = "add wishlist - wishlist - login"
        const val REMOVE_WISHLIST_WISHLIST = "remove wishlist - wishlist - login"

        // Tobacco
        const val CLICK_BROWSE_BUTTON_ON_TICKER_PRODUCT_CONTAIN_TOBACCO = "click browse button on ticker product contain tobacco"
        const val VIEW_TICKER_PRODUCT_CONTAIN_TOBACCO = "view ticker product contain tobacco"
        const val CLICK_HAPUS_BUTTON_ON_PRODUCT_CONTAIN_TOBACCO = "click hapus button on product contain tobacco"
        const val CLICK_TRASH_ICON_BUTTON_ON_PRODUCT_CONTAIN_TOBACCO = "click trash icon button on product contain tobacco"

        // Campaign
        const val VIEW_POP_UP_MESSAGE_TIMER = "view pop up message payment time expired"
        const val CLICK_BELANJA_LAGI_ON_POP_UP = "click belanja lagi on pop up message payment time expired"

        // Promo checkout revamp
        const val VIEW_PROMO_MESSAGE = "view promo message"

        // OCC
        const val CLICK_BACK_ARROW_IN_PILIH_METHOD_PAYMENT = "click back in pilih metode bayar"
        const val EDIT_QUANTITY_INCREASE = "product - click button plus"
        const val EDIT_QUANTITY_DECREASE = "product - click button minus"
        const val EDIT_SELLER_NOTES = "product - click tulis catatan"
        const val USER_CHANGE_COURIER_OSP = "courier - click arrow to change courier option"
        const val CLICK_UBAH_WHEN_DURATION_ERROR = "click ubah when duration error"
        const val CLICK_ON_INSURANCE = "courier - click on asuransi pengiriman"
        const val CLICK_BAYAR_NOT_SUCCESS = "click bayar - not success"
        const val CLICK_PILIH_PEMBAYARAN_NOT_SUCCESS = "click pilih pembayaran - not success"
        const val CLICK_PILIH_PEMBAYARAN = "click pilih pembayaran"
        const val VIEW_ERROR_ON_OSP = "view error message"
        const val CHOOSE_BBO_AS_DURATION = "courier - click pilih on tersedia bebas ongkir"
        const val CHOOSE_COURIER_FROM_COURIER_SELECTION_OSP = "courier - click selected courier option"
        const val CLICK_RINGKASAN_BELANJA_OSP = "click arrow on ringkasan belanja"
        const val CLICK_BACK_FROM_OSP = "click back in order summary page"
        const val CLICK_PROMO_SECTION_APPLIED_OSP = "click promo section with promo applied"
        const val CLICK_PROMO_SECTION_NOT_APPLIED_OSP = "click promo section with promo not applied"
        const val CLICK_LANJUT_BAYAR_PROMO_ERROR_OSP = "promo - click lanjut bayar on bottom sheet promo error"
        const val CLICK_PILIH_PROMO_LAIN_PROMO_ERROR_OSP = "promo - click pilih promo lain on bottom sheet promo error"
        const val VIEW_ORDER_SUMMARY_PAGE = "view order summary page"
        const val VIEW_BOTTOMSHEET_PROMO_ERROR = "view bottom sheet promo error"
        const val VIEW_PROMO_ALREADY_APPLIED = "view promo already applied in order summary list"
        const val VIEW_PROMO_RELEASED = "view promo released after adjust item"
        const val VIEW_PROMO_DECREASED = "view promo decreased after adjust item"
        const val VIEW_OVERWEIGHT_TICKER = "view overweight ticker"
        const val VIEW_MESSAGE_IN_COURIER_2_JAM_SAMPAI = "view message in courier 2 jam sampai"
        const val CLICK_ARROW_TO_CHANGE_ADDRESS_OPTION = "address - click arrow to change address option"
        const val CLICK_SELECTED_ADDRESS_OPTION = "address - click selected address option"
        const val CLICK_REFRESH_ON_COURIER_SECTION = "click refresh on courier section"
        const val CLICK_ARROW_TO_CHANGE_DURATION_OPTION = "duration - click arrow to change duration option"
        const val CLICK_SELECTED_DURATION_OPTION_NEW = "duration - click selected duration option"
        const val CLICK_ARROW_TO_CHANGE_PAYMENT_OPTION = "payment - click arrow to change payment option"
        const val CLICK_SELECTED_PAYMENT_OPTION = "payment - click selected payment option"
        const val VIEW_COACHMARK_1_FOR_NEW_BUYER_AFTER_CREATE_PROFILE = "view coachmark 1 for new buyer after create profile"
        const val VIEW_COACHMARK_2_FOR_NEW_BUYER_AFTER_CREATE_PROFILE = "view coachmark 2 for new buyer after create profile"
        const val VIEW_COACHMARK_3_FOR_NEW_BUYER_AFTER_CREATE_PROFILE = "view coachmark 3 for new buyer after create profile"
        const val CLICK_DONE_ON_COACHMARK_3_FOR_NEW_BUYER_AFTER_CREATE_PROFILE = "click done on coachmark 3 for new buyer after create profile"
        const val VIEW_ERROR_PRODUCT_LEVEL_TICKER = "view error product level tickers"
        const val VIEW_ERROR_ORDER_LEVEL_TICKER = "view error order level tickers"
        const val VIEW_ERROR_TOASTER_MESSAGE = "view error toaster message"
        const val VIEW_TOP_UP_GOPAY_BUTTON = "view top up gopay button"
        const val CLICK_TOP_UP_GOPAY_BUTTON = "click top up gopay button"
        const val VIEW_PAYMENT_METHOD = "view payment method"
        const val VIEW_TENURE_OPTION = "view tenure option"
        const val CLICK_TENURE_OPTIONS_BOTTOMSHEET = "click tenure options bottomsheet"

        // OCC PP
        const val PP_IMPRESSION_ON_INSURANCE_SECTION = "pp - ins section impression"
        const val PP_CLICK_TOOLTIP = "pp - click tooltip"
        const val PP_CLICK_BAYAR = "pp - click bayar"

        // Shipping experience
        const val VIEW_SUMMARY_TRANSACTION_TICKER_COURIER_NOT_COMPLETE = "view summary transaction ticker courier not complete"
        const val CLICK_CEK_ON_SUMMARY_TRANSACTION_TICKER_COURIER_NOT_COMPLETE = "click cek on summary transaction ticker courier not complete"

        // Cart Revamp
        const val VIEW_REMAINING_STOCK_INFO = "view remaining stock info"
        const val VIEW_INFORMATION_LABEL_IN_PRODUCT_CARD = "view information label in product card"
        const val CLICK_DETAIL_TAGIHAN = "click detail tagihan"
        const val ADD_WISHLIST_CART_LOGIN = "add wishlist - cart - login"
        const val REMOVE_WISHLIST_CART_LOGIN = "remove wishlist - cart - login"
        const val CLICK_DELETE_PRODUCT_ON_UNAVAILABLE_SECTION = "click delete product on unavailable section"
        const val CLICK_LIHAT_PRODUK_SERUPA_ON_UNAVAILABLE_SECTION = "click lihat produk serupa on unavailable section"
        const val CLICK_CHECKOUT_MELALUI_BROWSER_ON_UNAVAILABLE_SECTION = "click checkout melalui browser on unavailable section"
        const val CLICK_DELETE_ALL_UNAVAILABLE_PRODUCT = "click delete all unavailable product"
        const val CLICK_ACCORDION_ON_UNAVAILABLE_PRODUCT = "click %s on unavailable section"
        const val CLICK_UNDO_AFTER_DELETE_PRODUCT = "click undo after delete product"
        const val VIEW_ERROR_PAGE_WHEN_LOAD_CART = "view error page when load cart"
        const val CLICK_WISHLIST_ICON_IN_CART_PAGE = "click wishlist icon in cart page"
        const val CLICK_FOLLOW_SHOP_ON_UNAVAILABLE_SECTION = "click follow shop on unavailable section"
        const val CLICK_GLOBAL_MENU_NAV = "click global menu nav"

        // Checkout TokoNow
        const val VIEW_TICKER_PRODUCT_LEVEL_ERROR_IN_CHECKOUT_PAGE = "view ticker product level error in checkout page"
        const val VIEW_TICKER_ORDER_LEVEL_ERROR_IN_CHECKOUT_PAGE = "view ticker order level error in checkout page"
        const val VIEW_TICKER_PAYMENT_LEVEL_ERROR_IN_CHECKOUT_PAGE = "view ticker payment level error in checkout page"
        const val CLICK_LIHAT_ON_TICKER_ORDER_LEVEL_ERROR_IN_CHECKOUT_PAGE = "click lihat on ticker order level error in checkout page"
        const val CLICK_REFRESH_WHEN_ERROR_LOAD_COURIER = "click refresh when error load courier"
        const val VIEW_ERROR_IN_COURIER_SECTION = "view error in courier section"

        // Cart TokoNow
        const val VIEW_TOASTER_ERROR_IN_CART_PAGE = "view toaster error in cart page"
        const val LOAD_CART_WITH_UNAVAILABLE_PRODUCT = "load cart with unavailable product"
        const val CLICK_ON_PRODUCT_IMAGE_ON_COLLAPSE_VIEW = "click on product image on collapse view"
        const val CLICK_LIHAT_SELENGKAPNYA_FOR_NOW_PRODUCT = "click lihat selengkapnya for now product"
        const val CLICK_LIHAT_ON_PLUS_LAINNYA_ON_NOW_PRODUCT = "click lihat on + lainnya on now product"

        // Cart Bundling
        const val CLICK_LIHAT_BARANG_SERUPA_FOR_UNAVAILABLE_BUNDLE_PACKAGE = "click lihat barang serupa for Unavailable Bundle Package"
        const val CLICK_UBAH_IN_PRODUCT_BUNDLING_PACKAGE_PRODUCT_CARD = "click ubah in product bundling package product card"
        const val CLICK_BUNDLING_WIDGET = "click bundling widget"
        const val IMPRESSION_BUNDLING_COMPONENT = "impression - bundling component"
        const val CLICK_PRODUCT_BUNDLING = "click - product bundling"

        // Cart Bo Affordability
        const val CLICK_ARROW_IN_BO_TICKER_TO_REACH_SHOP_PAGE = "click arrow in BO Ticker to reach shop page"
        const val VIEW_BO_TICKER_WORDING = "view BO Ticker wording"

        // Gifting
        const val VIEW_ADD_ONS_WIDGET = "view add ons widget"
        const val CLICK_ADD_ONS_DETAIL = "click add ons detail"
        const val CLICK_SIMPAN_ON_ADD_ONS_BOTTOMSHEET = "click simpan on add ons bottomsheet"

        // Gotoplus
        const val VIEW_GOTOPLUS_TICKER = "view gotoplus ticker"
        const val VIEW_GOTOPLUS_UPSELL_TICKER = "view gotoplus upsell ticker"
        const val CLICK_GOTOPLUS_UPSELL_TICKER = "click gotoplus upsell ticker"
        const val VIEW_GOTOPLUS_CROSS_SELL_CEK_PLUS = "view gotoplus cross sell - cek plus"
        const val VIEW_GOTOPLUS_CROSS_SELL_BATAL = "view gotoplus cross sell - batal"
        const val CLICK_GOTOPLUS_CROSS_SELL_CEK_PLUS = "click gotoplus cross sell - cek plus"
        const val CLICK_GOTOPLUS_CROSS_SELL_BATAL = "click gotoplus cross sell - batal"

        // Platform Fee
        const val CLICK_INFO_BUTTON_IN_PLATFORM_FEE = "click info button in platform fee"
        const val VIEW_PLATFORM_FEE_IN_CHECKOUT_PAGE = "view platform fee in checkout page"

        // Add Ons Product Service
        const val VIEW_ADD_ONS_PRODUCT_WIDGET = "view addons product widget"
        const val CLICK_ADD_ONS_PRODUCT_WIDGET = "click addons product widget"
        const val CLICK_LIHAT_SEMUA_ON_ADDONS_PRODUCT_WIDGET = "click lihat semua on addons product widget"

        // Cart Revamp
        const val CLICK_SIMPAN_ON_NOTE_BOTTOMSHEET = "click simpan on note bottomsheet"
        const val CLICK_NOTE_ICON = "click note icon"
        const val CLICK_BUTTON_MIN_TO_DELETE_CART = "click button - to delete cart"
        const val IMPRESSION_CART = "impression cart"
        const val CLICK_SWIPE_ON_PRODUCT_CART = "click swipe on product cart"
        const val CLICK_REMOVE_CART_FROM_SWIPE = "click remove cart from swipe"

        // Checkout Revamp
        const val CLICK_SNK_ASURANSI_DAN_PROTEKSI = "click snk asuransi dan proteksi"
        const val CLICKS_INFO_BUTTON_OF_ADDONS = "clicks info button of addons"

        // BMGM
        const val CLICK_SNK_BMGM = "click snk bmgm"
        const val CLICK_BMGM_RECOMMENDATION = "click bmgm recommendation"
        const val IMPRESSION_BMGM_RECOMMENDATION = "impression bmgm recommendation"

        // dropship
        const val IMPRESSION_DROPSHIP_WIDGET = "impression dropship widget"
        const val CLICK_INFO_DROPSHIP_WIDGET = "click info dropship widget"
        const val CLICK_TOGGLE_DROPSHIP_WIDGET = "click toggle dropship widget"
        const val CLICK_PILIH_PEMBAYARAN_WITH_DROPSHIP_ENABLED = "click pilih pembayaran with dropship enabled"

        // OFOC
        const val VIEW_SPLIT_OFOC_POP_UP_BOX = "view split ofoc pop up box"
        const val CLICK_OK_TO_SPLIT_ORDER_OFOC = "click ok to split order ofoc"
    }

    object EventLabel {
        const val SUCCESS = "success"
        const val ERROR = "error"
        const val NOT_SUCCESS = "not success"
        const val COURIER_NOT_COMPLETE = "courier not complete"
        const val PROMO_RED_STATE = "promo red state"
        const val KUOTA_PENUKARAN = "kuota penukaran"
        const val FAILED = "failed"
        const val CHECKLIST = "checklist"
        const val UN_CHECKLIST = "unchecklist"
        const val FAILED_DROPSHIPPER = "failed dropshipper"
        const val CHECKOUT_SUCCESS_DEFAULT = "success - default"
        const val CHECKOUT_SUCCESS_CHECK_ALL = "success - check all"
        const val CHECKOUT_SUCCESS_PARTIAL_SHOP = "success - partial shop"
        const val CHECKOUT_SUCCESS_PARTIAL_PRODUCT = "success - partial product"
        const val CHECKOUT_SUCCESS_PARTIAL_SHOP_AND_PRODUCT = "success - partial shop and product"
        const val CHECKOUT_SUCCESS_DEFAULT_ELIGIBLE_COD = "success - default - cod"
        const val CHECKOUT_SUCCESS_CHECK_ALL_ELIGIBLE_COD = "success - check all - cod"
        const val CHECKOUT_SUCCESS_PARTIAL_SHOP_ELIGIBLE_COD = "success - partial shop - cod"
        const val CHECKOUT_SUCCESS_PARTIAL_PRODUCT_ELIGIBLE_COD = "success - partial product - cod"
        const val CHECKOUT_SUCCESS_PARTIAL_SHOP_AND_PRODUCT_ELIGIBLE_COD = "success - partial shop and product - cod"
        const val PRODUCT_WISHLIST = "product wishlist"
        const val PRODUCT_LAST_SEEN = "product last seen"
        const val SUCCESS_TICKED_PPP = "yes"
        const val SUCCESS_UNTICKED_PPP = "no"
        const val SEPARATOR = " - "
        const val PROMO = "promo"
        const val NON_PROMO = "non promo"
        const val COD = "cod"

        // Promo checkout revamp
        const val INELIGIBLE_PRODUCT = "ineligible product"
        const val NO_PROMO = "no promo"
        const val INELIGIBLE_PROMO_LIST = "ineligible promo list"
        const val ELIGIBLE_PROMO = "eligible promo"
        const val BLACKLIST_ERROR = "blacklist error"
        const val PHONE_VERIFICATION_MESSAGE = "phone verification message"

        // Cart Bo Affordability
        const val BO_FULFILL = "fulfill"
        const val BO_UNFULFILL = "unfulfill"

        // Gifting
        const val ADD_ON_CHECKED = "add on checked"
        const val ADD_ON_NOT_CHECKED = "add on not checked"

        // Cart Bundling
        const val BUNDLE_TYPE_SINGLE = "single"
        const val BUNDLE_TYPE_MULTIPLE = "multiple"
        const val CART_BUNDLING_BOTTOM_SHEET_BUNDLE_LIST_NAME = "/cart - bundling"

        // Cart Checkout Revamp
        const val NOTE_SIMPAN = "simpan"
        const val NOTE_EDIT = "edit"

        // dropship
        const val TOGGLE_ON = "toggle on"
        const val TOGGLE_OFF = "toggle off"
    }

    object ExtraKey {
        const val EVENT = "event"
        const val EVENT_ACTION = "eventAction"
        const val EVENT_CATEGORY = "eventCategory"
        const val EVENT_LABEL = "eventLabel"
        const val USER_ID = "userId"
        const val CART_ID = "cartId"
        const val PROMO_CODE = "promoCode"
        const val PAYMENT_METHOD = "paymentMethod"
        const val BUSINESS_UNIT = "businessUnit"
        const val CURRENT_SITE = "currentSite"
        const val PAGE_TYPE = "pageType"
        const val PAGE_PATH = "pagePath"
        const val PROMO_FLAG = "promoFlag"
        const val TRACKER_ID = "trackerId"
        const val PROMOTIONS = "promotions"
        const val PLATFORM_FEE = "platformFee"
        const val ITEMS = "items"
    }

    object ScreenName {
        const val CART = "/cart"
        const val CHECKOUT = "/cart/shipment"
        const val ONE_CLICK_SHIPMENT = "/cart/shipment/ocs"
        const val PROMO_PAGE_FROM_CART_TAB_PROMO = "/cart#voucher"
        const val PROMO_PAGE_FROM_CART_TAB_COUPON = "/cart#coupon"
        const val PROMO_PAGE_FROM_CHECKOUT_TAB_PROMO = "/cart/shipment#voucher"
        const val PROMO_PAGE_FROM_CHECKOUT_TAB_COUPON = "/cart/shipment#coupon"
        const val ADDRESS_LIST_PAGE = "/cart/address"
        const val ADD_NEW_ADDRESS_PAGE = "/cart/address/create"
        const val ADD_NEW_ADDRESS_PAGE_FROM_EMPTY_ADDRESS_CART = "/user/address/create/cart"
        const val ADD_NEW_ADDRESS_PAGE_USER = "/user/address/create"
    }

    object CustomDimension {
        const val DIMENSION_CURRENT_SITE_MARKETPLACE = "tokopediamarketplace"
        const val DIMENSION_CURRENT_SITE_MARKETPLACE_FINTECH = "tokopediafintechinsurance"
        const val DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM = "purchase platform"
        const val DIMENSION_BUSINESS_UNIT_HOME_BROWSE = "home & browse"
        const val DIMENSION_BUSINESS_UNIT_FINTECH = "fintech"
        const val DIMENSION_BUSINESS_UNIT_PROMO = "promo"
        const val DIMENSION_SESSION_IRIS = "sessionIris"
    }

    object TrackerId {
        const val VIEW_GOTOPLUS_TICKER_CART = "33464"
        const val VIEW_GOTOPLUS_TICKER_COURIER_SELECTION = "33465"
        const val VIEW_GOTOPLUS_UPSELL_TICKER = "33466"
        const val CLICK_GOTOPLUS_UPSELL_TICKER = "33467"
        const val VIEW_GOTOPLUS_TICKER_OCC = "33468"
        const val VIEW_GOTOPLUS_CROSS_SELL_CEK_PLUS = "36316"
        const val VIEW_GOTOPLUS_CROSS_SELL_BATAL = "36318"
        const val CLICK_GOTOPLUS_CROSS_SELL_CEK_PLUS = "36317"
        const val CLICK_GOTOPLUS_CROSS_SELL_BATAL = "36319"

        const val CLICK_BAYAR_OCC = "15570"
        const val CLICK_INSURANCE_INFO_TOOLTIP = "36048"

        const val STEP_0_VIEW_CART_PAGE = "15458"
        const val STEP_1_CART_PAGE_LOADED = "15454"
        const val STEP_2_CHECKOUT_PAGE_LOADED = "15455"
        const val STEP_3_CLICK_ALL_COURIER_SELECTED = "15456"
        const val STEP_4_CLICK_PAYMENT_OPTION_BUTTON = "15457"

        // Cart Bundling Bottom Sheet
        const val IMPRESSION_CART_BUNDLING_BOTTOM_SHEET_BUNDLE = "41253"
        const val CLICK_CART_BUNDLING_BOTTOM_SHEET_BUNDLE_WIDGET_ACTION = "41254"

        // Cart Shop Group Ticker
        const val CLICK_CART_SHOP_GROUP_TICKER_BUNDLE_CROSS_SELL = "41252"

        // Platform Fee
        const val CLICK_INFO_BUTTON_IN_PLATFORM_FEE = "43346"
        const val VIEW_PLATFORM_FEE_IN_CHECKOUT_PAGE = "43710"

        // AddOns Product Service
        const val VIEW_ADDONS_PRODUCT_WIDGET = "45171"
        const val CLICK_ADDONS_PRODUCT_WIDGET = "45173"
        const val CLICK_LIHAT_SEMUA_ADDONS_PRODUCT_WIDGET = "45174"
        const val VIEW_ADDONS_PRODUCT_WIDGET_CART = "45176"
        const val CLICK_ADDONS_PRODUCT_WIDGET_CART = "45177"
        const val VIEW_ADDONS_PRODUCT_WIDGET_OCC = "45322"
        const val CLICK_ADDONS_PRODUCT_WIDGET_OCC = "45323"
        const val CLICK_LIHAT_SEMUA_ADDONS_PRODUCT_WIDGET_OCC = "45324"

        // Gopay Cicil
        const val IMPRESSION_PROMO_ACTIVATED_GOPAY_CICIL = "45451"
        const val CLICK_ACTIVATED_GOPAY_CICIL = "45453"
        const val IMPRESSION_ELIGIBLE_PROMO_SECTION_GOPAY_CICIL = "45454"
        const val IMPRESSION_INELIGIBLE_PROMO_SECTION_GOPAY_CICIL_PROMO_VALIDATION = "45456"

        // Cart Revamp
        const val CLICK_SIMPAN_ON_NOTE_BOTTOMSHEET = "46928"
        const val CLICK_NOTE_ICON = "46929"
        const val IMPRESSION_CART = "46932"
        const val CLICK_BUTTON_MIN_TO_DELETE_CART = "46933"
        const val CLICK_SWIPE_ON_PRODUCT_CART = "48948"
        const val CLICK_REMOVE_CART_FROM_SWIPE = "48949"
        const val CLICK_UNDO_AFTER_DELETE_PRODUCT = "15468"

        // Checkout Revamp
        const val CLICK_SNK_ASURANSI_DAN_PROTEKSI = "46930"
        const val CLICKS_INFO_BUTTON_OF_ADDONS = "46931"

        // BMGM
        const val CLICK_SNK_BMGM = "46781"
        const val CLICK_BMGM_RECOMMENDATION = "47209"
        const val VIEW_BMGM_RECOMMENDATION = "47208"

        // dropship
        const val IMPRESSION_DROPSHIP_WIDGET = "48774"
        const val CLICK_INFO_DROPSHIP_WIDGET = "48775"
        const val CLICK_TOGGLE_DROPSHIP_WIDGET = "48776"
        const val CLICK_PILIH_PEMBAYARAN_WITH_DROPSHIP_ENABLED = "48777"

        // OFOC
        const val VIEW_SPLIT_OFOC_POP_UP_BOX = "48544"
        const val CLICK_OK_TO_SPLIT_ORDER_OFOC = "48547"
    }
}
