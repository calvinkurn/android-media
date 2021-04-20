package com.tokopedia.purchase_platform.common.analytics;

/**
 * @author anggaprasetiyo on 18/05/18.
 */
public interface ConstantTransactionAnalytics {

    interface Key {
        String EVENT = "event";
        String EVENT_CATEGORY = "eventCategory";
        String EVENT_ACTION = "eventAction";
        String EVENT_LABEL = "eventLabel";
        String PROMO_ID = "promoId";
        String E_COMMERCE = "ecommerce";
        String PAYMENT_ID = "payment_id";
        String CURRENT_SITE = "currentSite";
        String PROMOTIONS = "promotions";
        String ID = "id";
        String NAME = "name";
        String CREATIVE = "creative";
        String POSITION = "position";
        String PROMO_ID_ = "promo_id";
        String PROMO_CODE = "promo_code";
    }


    interface EventName {
        String CLICK_ATC = "clickATC";
        String CLICK_BUY = "clickBuy";
        String CLICK_CHECKOUT = "clickCheckout";
        String VIEW_ATC = "viewATC";
        String VIEW_ATC_IRIS = "viewATCIris";
        String REMOVE_FROM_CART = "removeFromCart";
        String CHECKOUT = "checkout";
        String ADD_TO_CART = "addToCart";
        String CLICK_COURIER = "clickCourier";
        String CLICK_COUPON = "clickCoupon";
        String VIEW_COURIER = "viewCourier";
        String CLICK_SHIPPING = "clickShipping";
        String VIEW_SHIPPING = "viewShipping";
        String VIEW_PROMO = "viewPromo";
        String VIEW_ORDER = "viewOrder";
        String CLICK_ORDER = "clickOrder";
        String CLICK_ADDRESS = "clickAddress";
        String PRODUCT_CLICK = "productClick";
        String PRODUCT_VIEW = "productView";
        String PROMO_VIEW = "promoView";
        String PROMO_CLICK = "promoClick";
        String CLICK_PDP = "clickPDP";
        String PURCHASE_PROTECTION = "fintechppandroid";
        String VIEW_CART = "viewCart";
        String CLICK_REGISTER = "clickRegister";
        String VIEW_REGISTER = "viewRegister";
        String CLICK_CHECKOUT_EXPRESS = "clickCheckoutExpress";
        String VIEW_CHECKOUT_EXPRESS = "viewCheckoutExpress";
        String VIEW_CHECKOUT_EXPRESS_IRIS = "viewCheckoutExpressIris";
        String VIEW_SOM = "viewSOM";
        String CLICK_RECOMMENDATION = "clickRecommendation";
        String VIEW_COURIER_IRIS = "viewCourierIris";
        String VIEW_TRADEIN = "viewTradeIn";
        String CLICK_TRADEIN = "clickTradeIn";
        String CART = "Cart";
        String CLICK_WISHLIST = "clickWishlist";
        String CLICK_NAVIGATION_DRAWER = "clickNavigationDrawer";
    }

    interface EventCategory {
        String CART = "cart";
        String EMPTY_CART = "empty cart";
        String COURIER_SELECTION = "courier selection";
        String ADD_TO_CART = "add to cart";
        String CART_CHANGE_ADDRESS = "cart change address";
        String CART_MULTIPLE_ADDRESS = "cart multiple address";
        String ORDER_TRACKING = "order tracking";
        String PRODUCT_DETAIL_PAGE = "product detail page";
        String PURCHASE_PROTECTION = "fin - mp checkout";
        String CORNER_ADDRES = "tokopedia corner address";
        String EXPRESS_CHECKOUT = "express checkout";
        String COURIER_SELECTION_TRADE_IN = "courier selection trade in";
        String WISHLIST_PAGE = "wishlist page";
        String RECENT_VIEW = "recent view";
        String RECOMMENDATION_PAGE = "recommendation page";
        String TRACK_SOM = "track seller order management";
        String FIN_INSURANCE_CART = "fin - cart page";
        String FIN_INSURANCE_CHECKOUT = "fin - mp checkout";

        //OCC
        String PURCHASE_SETTING = "purchase setting";
        String ORDER_SUMMARY = "order summary";
    }

    interface EventAction {
        String CLICK_ALL_COURIER_SELECTED = "click all courier selected";
        String CLICK_GUNAKAN_KODE_PROMO_ATAU_KUPON = "click gunakan kode promo atau kupon";
        String CLICK_GUNAKAN_KODE_PROMO = "click gunakan kode promo";
        String CLICK_GUNAKAN_FROM_PILIH_MERCHANT_VOUCHER = "click gunakan from pilih merchant voucher";
        String CLICK_GUNAKAN_ON_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER = "click gunakan on merchant voucher from pilih merchant voucher";
        String CLICK_GUNAKAN_KUPON = "click gunakan kupon";
        String CLICK_HAPUS_ON_TOP_RIGHT_CORNER = "click hapus on top right corner";
        String CLICK_SHOP = "click - shop - login";
        String CLICK_PRODUCT_NAME = "click product name";
        String CLICK_BUTTON_PLUS = "click button +";
        String CLICK_BUTTON_MIN = "click button -";
        String CLICK_TRASH_BIN = "click trash bin";
        String CLICK_ARROW_BACK = "click arrow back";
        String CLICK_X_ON_BANNER_PROMO_CODE = "click x on banner promo code";
        String CLICK_BELANJA_SEKARANG_ON_EMPTY_CART = "click belanja sekarang on empty cart";
        String CLICK_PILIH_SEMUA_FROM_HAPUS = "click pilih semua from hapus";
        String CLICK_CHECKLIST_BOX_FROM_HAPUS = "click checklist box from hapus";
        String CLICK_HAPUS_FROM_HAPUS = "click hapus from hapus";
        String CLICK_GUNAKAN_KODE_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON = "click gunakan kode from gunakan kode promo atau kupon";
        String CLICK_KUPON_SAYA_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON = "click kupon saya from gunakan kode promo atau kupon";
        String CLICK_KODE_PROMO_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON = "click kode promo from gunakan kode promo atau kupon";
        String CLICK_KUPON_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON = "click kupon from gunakan kode promo atau kupon";
        String CLICK_BAYAR_ON_ATC_SUCCESS = "click bayar on atc success";
        String CLICK_LANJUTKAN_BELANJA_ON_ATC_SUCCESS = "click lanjutkan belanja on atc success";
        String CLICK_BACK_ARROW = "click back arrow";
        String CLICK_KEMBALI_DAN_HAPUS_PERUBAHAN_FROM_BACK_ARROW = "click kembali dan hapus perubahan from back arrow";
        String CLICK_TETAP_DI_HALAMAN_INI_FROM_BACK_ARROW = "click tetap di halaman ini from back arrow";
        String CLICK_GANTI_ALAMAT_ATAU_KIRIM_KE_BEBERAPA_ALAMAT = "click ganti alamat atau kirim ke beberapa alamat";
        String CLICK_SUBTOTAL = "click subtotal";
        String CLICK_COBA_LAGI_WHEN_GAGAL_DISKON_VOUCHER = "click coba lagi when gagal diskon voucher";
        String CLICK_HAPUS_WHEN_GAGAL_DISKON_VOUCHER = "click hapus when gagal diskon voucher";
        String CLICK_HAPUS_WHEN_SUKSES_DISKON_VOUCHER = "click hapus when sukses diskon voucher";
        String CLICK_X_ON_COURIER_OPTION = "click x on courier option";
        String CLICK_DROPDOWN_JENIS_PENGIRIMAN = "click dropdown jenis pengiriman";
        String CLICK_LOGISTIC_AGENT = "click logistic agent";
        String CLICK_PILIH_LOKASI_PETA = "click pilih lokasi peta";
        String CLICK_ASURANSI_PENGIRIMAN = "click asuransi pengiriman";
        String CLICK_DROPSHIP = "click dropship";
        String CLICK_INPUT_QUANTITY = "click input quantity";
        String CLICK_TULIS_CATATAN = "click tulis catatan";
        String CLICK_PILIH_ALAMAT_LAINNYA_FROM_GANTI_ALAMAT = "click pilih alamat lainnya from ganti alamat";
        String CLICK_TAMBAH_ALAMAT_BARU_FROM_GANTI_ALAMAT = "click tambah alamat baru from ganti alamat";
        String CLICK_KIRIM_KE_BEBERAPA_ALAMAT_FROM_GANTI_ALAMAT = "click kirim ke beberapa alamat from ganti alamat";
        String CLICK_KIRIM_KE_ALAMAT_INI_FROM_GANTI_ALAMAT = "click kirim ke alamat ini from ganti alamat";
        String CLICK_X_FROM_PILIH_ALAMAT_LAINNYA = "click x from pilih alamat lainnya";
        String CLICK_PLUS_FROM_PILIH_ALAMAT_LAINNYA = "click + from pilih alamat lainnya";
        String CLICK_UBAH_FROM_PILIH_ALAMAT_LAINNYA = "click ubah from pilih alamat lainnya";
        String CLICK_CHECKLIST_ALAMAT_FROM_PILIH_ALAMAT_LAINNYA = "click checklist alamat from pilih alamat lainnya";
        String IMPRESSION_CHANGE_ADDRESS = "impression change address";
        String SUBMIT_SEARCH_FROM_PILIH_ALAMAT_LAINNYA = "submit search from pilih alamat lainnya";
        String CLICK_TAMBAH_ALAMAT_FROM_PLUS = "click tambah alamat from +";
        String CLICK_BACK_ARROW_FROM_KIRIM_KE_BEBERAPA_ALAMAT = "click back arrow from kirim ke beberapa alamat";
        String CLICK_KEMBALI_DAN_HAPUS_PERUBAHAN_FROM_KIRIM_KE_BEBERAPA_ALAMAT = "click kembali dan hapus perubahan from kirim ke beberapa alamat";
        String CLICK_TETAP_DI_HALAMAN_INI_FROM_KIRIM_KE_BEBERAPA_ALAMAT = "click tetap di halaman ini from kirim ke beberapa alamat";
        String CLICK_X_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT = "click x from ubah from kirim ke beberapa alamat";
        String CLICK_INPUT_QUANTITY_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT = "click input quantity from ubah from kirim ke beberapa alamat";
        String CLICK_TULIS_CATATAN_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT = "click tulis catatan from ubah from kirim ke beberapa alamat";
        String CLICK_SIMPAN_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT = "click simpan from ubah from kirim ke beberapa alamat";
        String CLICK_TERIMA_SEBAGIAN = "click terima sebagian";
        String CLICK_X_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON = "click x from gunakan kode promo atau kupon";
        String CLICK_ARROW_BACK_FROM_HAPUS = "click arrow back from hapus";
        String CLICK_ARROW_BACK_FROM_GANTI_ALAMAT = "click arrow back from ganti alamat";
        String CLICK_ARROW_BACK_FROM_PLUS = "click arrow back from +";
        String CLICK_PILIH_METODE_PEMBAYARAN = "click pilih metode pembayaran";
        String IMPRESSION_CART_EMPTY = "impression cart empty";
        String IMPRESSION_ATC_SUCCESS = "impression atc success";
        String IMPRESSION_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER = "impression merchant voucher from pilih merchant voucher";
        String CLICK_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER = "click merchant voucher from pilih merchant voucher";
        String IMPRESSION_COURIER_SELECTION = "impression courier selection";
        String IMPRESSION_ON_POP_UP_KUPON = "impression on pop up kupon";
        String CLICK_HAPUS_FROM_CLICK_HAPUS = "click hapus from click hapus";
        String CLICK_HAPUS_DAN_TAMBAH_WISHLIST_FROM_CLICK_HAPUS = "click hapus dan tambah wishlist from click hapus";
        String CLICK_HAPUS_DAN_TAMBAH_WISHLIST_FROM_TRASH_BIN = "click hapus dan tambah wishlist from trash bin";
        String CLICK_HAPUS_FROM_TRASH_BIN = "click hapus from trash bin";
        String CLICK_HAPUS_PRODUK_BERKENDALA = "click hapus produk berkendala";
        String CLICK_HAPUS_DAN_TAMBAH_WISHLIST_FROM_HAPUS_PRODUK_BERKENDALA = "click hapus dan tambah wishlist from hapus produk berkendala";
        String CLICK_HAPUS_FROM_HAPUS_PRODUK_BERKENDALA = "click hapus from hapus produk berkendala";
        String CLICK_BELI = "click beli";
        String CLICK_SELECT_COURIER = "click select courier";
        String CLICK_SELECT_COURIER_ON_CART = "click select courier on cart";
        String CLICK_COURIER_OPTION = "click courier option";
        String IMPRESSION_COURIER_OPTION = "impression courier option";
        String CLICK_PLUS_ICON_FROM_TUJUAN_PENGIRIMAN = "click + from tujuan pengiriman";
        String CLICK_SIMPAN_FROM_TAMBAH_ALAMAT = "click simpan from tambah alamat";
        String VALIDATION_ERROR_VOUCHER_PROMO_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON = "validation error voucher promo from gunakan kode promo atau kupon";
        String CLICK_KUPON_FROM_KUPON_SAYA = "click kupon from kupon saya";
        String CLICK_BACK_ARROW_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON = "click back arrow from gunakan kode promo atau kupon";
        String CLICK_RADIO_BUTTON_FROM_TUJUAN_PENGIRIMAN = "click radio button from tujuan pengiriman";
        String CLICK_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS = "click kota atau kecamatan pada + address";
        String CLICK_KODE_POS_PADA_TAMBAH_ADDRESS = "click kode pos pada + address";
        String CLICK_TANDAI_LOKASI_PADA_TAMBAH_ADDRESS = "click tandai lokasi pada + address";
        String CLICK_TAMBAH_ALAMAT_FROM_TAMBAH = "click tambah alamat from +";
        String CLICK_CHECKLIST_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS = "click checklist kota atau kecamatan pada + address";
        String CLICK_X_POJOK_KANAN_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS = "click x pojok kanan kota atau kecamatan pada + address";
        String CLICK_X_POJOK_KIRI_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS = "click x pojok kiri kota atau kecamatan pada + address";
        String CLICK_CHECKLIST_KODE_POS_PADA_TAMBAH_ADDRESS = "click checklist kode pos pada + address";
        String CLICK_FILL_KODE_POS_PADA_TAMBAH_ADDRESS = "click fill kode pos pada + address";
        String VIEW_VALIDATION_ERROR_NOT_FILL = "validation error not fill";
        String CLICK_DROPDOWN_SUGGESTION_TANDAI_LOKASI_PADA_TAMBAH_ADDRESS = "click dropdown suggestion tandai lokasi pada + address";
        String CLICK_V_TANDAI_LOKASI_PADA_TAMBAH_ADDRESS = "click v tandai lokasi pada + address";
        String CLICK_BACK_ARROW_TANDAI_LOKASI_PADA_TAMBAH_ADDRESS = "click back arrow tandai lokasi pada + address";
        String CLICK_PIN_BUTTON_FROM_TANDAI_LOKASI = "click pin button from tandai lokasi";
        String VIEW_VALIDATION_ERROR_TANDAI_LOKASI = "validation error tandai lokasi";
        String CLICK_RADIO_BUTTON_FROM_PILIH_ALAMAT_LAINNYA = "click radio button from pilih alamat lainnya";
        String IMPRESSION_BUTTON_LIVE_TRACKING = "impression button live tracking";
        String CLICK_BUTTON_LIVE_TRACKING = "click button live tracking";
        String CLICK_TAMBAH_FROM_ALAMAT_PENGIRIMAN = "click + from alamat pengiriman";
        String CLICK_ALAMAT_SEBAGAI_PADA_TAMBAH_ADDRESS = "click alamat sebagai pada + address";
        String CLICK_CHECKLIST_ALAMAT_SEBAGAI_PADA_TAMBAH_ADDRESS = "click checklist alamat sebagai pada + address";
        String CLICK_NAMA_PADA_TAMBAH_ADDRESS = "click nama pada + address";
        String CLICK_TELEPON_PADA_TAMBAH_ADDRESS = "click telepon pada + address";
        String CLICK_ALAMAT_PADA_TAMBAH_ADDRESS = "click alamat pada + address";
        String CLICK_CEK_KERANJANG = "click - cek keranjang";
        String VIEW_CART_LIST = "view cart list";
        String VIEW_CART_PAGE = "view cart page";
        String VIEW_CHECKOUT_PAGE = "view checkout page";
        String CLICK_BELI_ON_WISHLIST = "click - beli on wishlist";
        String CLICK_BELI_ON_RECENT_VIEW_PAGE = "click - beli on recent view page";
        String CLICK_ADD_TO_CART_ON_PRIMARY_PRODUCT = "click add to cart on primary product";
        String IMPRESSION_ERROR_COURIER_NO_AVAILABLE = "impression error courier no available";
        String CLICK_MORE_LIKE_THIS = "click more like this";
        String CLICK_PRODUCT_WISHLIST_ON_CART_LIST = "click product wishlist on cart list";
        String CLICK_PRODUCT_LAST_SEEN_ON_CART_LIST = "click product last seen on cart list";
        String CLICK_DONATION = "click donation";
        String VIEW_AUTO_CHECK_ON_DONATION = "view autocheck on donation";

        String VALIDATION_ERROR_ALAMAT_SEBAGAI_PADA_TAMBAH_ADDRESS = "validation error alamat sebagai pada + address";
        String VALIDATION_ERROR_NAMA_PADA_TAMBAH_ADDRESS = "validation error nama pada + address";
        String VALIDATION_ERROR_TELEPON_PADA_TAMBAH_ADDRESS = "validation error telepon pada + address";
        String VALIDATION_ERROR_KOTA_KECAMATAN_PADA_TAMBAH_ADDRESS = "validation error kota / kecamatan pada + address";
        String VALIDATION_ERROR_KODE_POS_PADA_TAMBAH_ADDRESS = "validation error kode pos pada + address";
        String VALIDATION_ERROR_ALAMAT_PADA_TAMBAH_ADDRESS = "validation error alamat pada + address";

        String VIEW_TICKER_PRICE_DECREASE = "view ticker price decrease";
        String VIEW_TICKER_STOCK_DECREASE_AND_ALREADY_ATC_BY_OTHER_USER = "view ticker stock decrease and already atc by other user";
        String VIEW_TICKER_OUT_OF_STOCK = "view ticker out of stock";
        String VIEW_INFORMATION_AND_WARNING_TICKER_IN_CART = "view information and warning ticker in cart";

        //PHASE 2
        String CLICK_PILIH_ALAMAT_LAIN = "click pilih alamat lain";
        String CLICK_KIRIM_KE_BANYAK_ALAMAT = "click kirim ke banyak alamat";
        String CLICK_EDIT_FROM_KIRIM_KE_BEBERAPA_ALAMAT = "click edit from kirim ke beberapa alamat";
        String CLICK_TAMBAH_PENGIRIMAN_BARU_FROM_KIRIM_KE_BEBERAPA_ALAMAT = "click tambah pengiriman baru from kirim ke beberapa alamat";
        String CLICK_PILIH_KURIR_PENGIRIMAN_FROM_KIRIM_KE_BEBERAPA_ALAMAT = "click pilih kurir pengiriman from kirim ke beberapa alamat";
        String CLICK_PLUS_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT = "click + from ubah from kirim ke beberapa alamat";
        String CLICK_MIN_FROM_UBAH_FROM_KIRIM_KE_BEBERAPA_ALAMAT = "click - from ubah from kirim ke beberapa alamat";
        String CLICK_TOP_DONASI = "click top donasi";

        String CLICK_ADD_FROM_WISHLIST_ON_EMPTY_CART = "click add from wishlist on empty cart";

        String CLICK_CHECKOUT = "click checkout";
        String CLICK_PILIH_SEMUA_PRODUK = "click pilih semua produk";
        String CLICK_BAYAR = "click bayar";
        String CLICK_UBAH_KURIR = "click ubah kurir";
        String CLICK_TAMBAH_FROM_TAMBAH_ALAMAT_BARU = "click tambah from tambah alamat baru";

        String VIEW_PROMO_ELIGBLE_APPLY = "view promo eligible apply";
        String VIEW_ERROR_ON_CHECKOUT = "view error when checkout";

        //ROBINHOOD
        String CLICK_BUTTON_DURASI_PENGIRIMAN = "click button durasi pengiriman";
        String CLICK_X_PADA_DURASI_PENGIRIMAN = "click x pada durasi pengiriman";
        String CLICK_CHECKLIST_PILIH_DURASI_PENGIRIMAN = "click checklist pilih durasi pengiriman";
        String CLICK_CTA_BUTTON = "click cta button";
        String VIEW_PRESELECTED_COURIER_OPTION = "view preselected courier option";
        String CLICK_CHANGE_COURIER_OPTION = "click change courier option";
        String CLICK_X_PADA_KURIR_PENGIRIMAN = "click x pada kurir pengiriman";
        String CLICK_UBAH_DURASI = "click ubah durasi";
        String VIEW_DURATION = "view duration";
        String VIEW_COURIER_OPTION = "view courier option";

        //EMPTY CART
        String CLICK_LIHAT_LAINNYA = "click lihat lainnya on empty cart";
        String CLICK_LIHAT_SEMUA_WISHLIST = "click lihat semua wishlist on empty cart";
        String CLICK_LIHAT_SEMUA_LAST_SEEN = "click lihat semua last seen on empty cart";
        String CLICK_PRODUCT_WISHLIST = "click product wishlist on empty cart";
        String CLICK_PRODUCT_LAST_SEEN = "click product last seen on empty cart";
        String CLICK_PRODUCT_RECOMMENDATION = "click on product recommendation";
        String VIEW_PRODUCT = "view product";

        //PURCHASE PROTECTION
        String CLICK_PELAJARI = "pp - pelajari click";
        String CLICK_PURCHASE_PROTECTION_PAY = "pp - bayar click";
        String IMPRESSION_PELAJARI = "pp - pelajari impression";

        //TOKOPEDIA CORNER
        String CHOOSE_LOCATION_CORNER = "click pilih lokasi tokopedia corner";
        String CLICK_CORNER_ADDRESS = "click alamat tokopedia corner";
        String VIEW_CORNER_ERROR = "view tokopedia corner not available";
        String VIEW_CORNER_PO_ERROR = "view error pre order tokopedia corner";

        //EXPRESS CHECKOUT
        String VIEW_EXPRESS_CHECKOUT = "view express checkout";
        String CLICK_LANJUTKAN_TANPA_TEMPLATE = "click lanjutkan tanpa template";
        String CLICK_X = "click x";
        String VIEW_ERROR_METODE_PEMBAYARAN = "view error metode pembayaran";

        String CLICK_PLUS_FROM_MULTIPLE = "click + from multiple";
        String CLICK_BUTTON_SIMPAN = "click button simpan";
        String CLICK_BUTTON_SIMPAN_FROM_EDIT = "click button simpan from edit";

        //TRADEIN
        String VIEW_CHECKOUYT_PAGE_TRADE_IN = "view checkout page trade in";
        String CLICK_GANTI_NOMOR = "click ganti nomor";
        String CLICK_BUTTON_PILIH_DURASI = "click button pilih durasi";
        String CLICK_KURIR_TRADE_IN = "click kurir trade in";
        String CLICK_JEMPUT_TAB = "click - jemput tab";
        String CLICK_DROP_OFF_TAB = "click - drop off tab";
        String CLICK_UBAH_TITIK_DROP_OFF_BUTTON = "click - ubah titik drop off button";

        //PROMO STACKING
        String CLICK_PILIH_MERCHANT_VOUCHER = "click pilih merchant voucher";
        String CLICK_PAKAI_MERCHANT_VOUCHER_MANUAL_INPUT = "click pakai merchant voucher manual input";
        String CLICK_PAKAI_MERCHANT_VOUCHER = "click pakai merchant voucher";
        String CLICK_DETAIL_MERCHANT_VOUCHER = "click detail merchant voucher";
        String CLICK_TICKER_MERCHANT_VOUCHER = "click ticker merchant voucher";
        String CLICK_HAPUS_PROMO_X_ON_TICKER = "click hapus promo (x) on ticker";
        String VIEW_DETAIL_MERCHANT_VOUCHER = "view detail merchant voucher";
        String CLICK_LIHAT_PROMO_LAINNYA_ON_VOUCHER_DETAIL = "click lihat promo lainnya on voucher detail";
        String CLICK_BATALKAN_PROMO_ON_VOUCHER_DETAIL = "click batalkan promo on voucher detail";
        String CLICK_CARA_PAKAI_ON_VOUCHER_DETAIL = "click cara pakai on voucher detail";
        String CLICK_KETENTUAN_ON_VOUCHER_DETAIL = "click ketentuan on voucher detail";
        String SELECT_PROMO_PROMO_KONFLIK = "select promo - promo konflik";
        String CLICK_SUBMIT_PROMO_CONFLICT = "click submit promo konflik";
        String VIEW_POPUP_PROMO_DISABLE = "view popup promo disable";
        String CLICK_PROMO_LOGISTIC_TICKER = "click promo logistic ticker";
        String CLICK_LANJUTKAN_TERAPKAN_PROMO = "click lanjutkan di pop up terapkan promo";
        String CLICK_BATAL_TERAPKAN_PROMO = "click batal di pop up terapkan promo";
        String VIEW_PROMO_LOGISTIC_TICKER = "view promo logistic ticker";
        String CLICK_X_ON_PROMO_STACKING_LOGISTIC = "click x on promo stacking logistic";
        String VIEW_INFORMATION_AND_WARNING_TICKER_IN_CHECKOUT = "view information and warning ticker in checkout";
        String VIEW_POP_UP_PRICE_INCREASE = "view pop up price increase";
        String VIEW_PROMO_LOGISTIC_TICKER_DISABLE = "view promo logistic ticker disable";

        //Promo not eligible bottomsheet
        String CLICK_LANJUTKAN_ON_ERROR_PROMO_CONFIRMATION = "click lanjutkan on error promo confirmation";
        String CLICK_BATAL_ON_ERROR_PROMO_CONFIRMATION = "click batal on error promo confirmation";
        String VIEW_POP_UP_ERROR_PROMO_CONFIRMATION = "view pop up error promo confirmation";

        String SEARCH_NOT_FOUND = "search result not found";

        //WIDGET_RECOMMENDATION
        String IMPRESSION_ON_PRODUCT_RECOMMENDATION = "impression on product recommendation";
        String CLICK_ADD_TO_CART = "click add to cart";
        String CLICK_ADD_WISHLIST_ON_PRODUCT_RECOMMENDATION = "click add wishlist on product recommendation";
        String CLICK_ADD_WISHLIST_ON_PRODUCT_RECOMMENDATION_EMPTY_CART = "click add wishlist on product recommendation - empty_cart";
        String CLICK_REMOVE_WISHLIST_ON_PRODUCT_RECOMMENDATION = "click remove wishlist on product recommendation";
        String CLICK_REMOVE_WISHLIST_ON_PRODUCT_RECOMMENDATION_EMPTY_CART = "click remove wishlist on product recommendation - empty_cart";

        //Retry Pickup
        String VIEW_TUNGGU_CARI_DRIVER = "view tunggu cari driver";
        String VIEW_BUTTON_CARI_DRIVER = "view button cari driver";
        String CLICK_BUTTON_CARI_DRIVER = "click button cari driver";

        //Error Popup
        String VIEW_HELP_POP_UP_AFTER_ERROR_IN_CHECKOUT = "view help pop up after error in checkout";
        String CLICK_REPORT_ON_HELP_POP_UP_IN_CHECKOUT = "click report on help pop up in checkout";
        String CLICK_CLOSE_ON_HELP_POP_UP_IN_CHECKOUT = "click close on help pop up in checkout";

        //Wishlist
        String ADD_WISHLIST_AVAILABLE_SECTION = "add wishlist - available section - login";
        String REMOVE_WISHLIST_AVAILABLE_SECTION = "remove wishlist - available section - login";
        String ADD_WISHLIST_UNAVAILABLE_SECTION = "add wishlist - unavailable section - login";
        String REMOVE_WISHLIST_UNAVAILABLE_SECTION = "remove wishlist - unavailable section - login";
        String ADD_WISHLIST_LAST_SEEN = "add wishlist - last seen - login";
        String REMOVE_WISHLIST_LAST_SEEN = "remove wishlist - last seen - login";
        String ADD_WISHLIST_WISHLIST = "add wishlist - wishlist - login";
        String REMOVE_WISHLIST_WISHLIST = "remove wishlist - wishlist - login";

        //Tobacco
        String CLICK_BROWSE_BUTTON_ON_TICKER_PRODUCT_CONTAIN_TOBACCO = "click browse button on ticker product contain tobacco";
        String VIEW_TICKER_PRODUCT_CONTAIN_TOBACCO = "view ticker product contain tobacco";
        String CLICK_HAPUS_BUTTON_ON_PRODUCT_CONTAIN_TOBACCO = "click hapus button on product contain tobacco";
        String CLICK_TRASH_ICON_BUTTON_ON_PRODUCT_CONTAIN_TOBACCO = "click trash icon button on product contain tobacco";

        //Insurance
        String FIN_INSURANCE_CART_DELETE = "ins - click delete from cart";
        String FIN_INSURANCE_CART_IMPRESSION = "ins - impression insurance box in cart ";
        String FIN_INSURANCE_STATE_CHANGE = "ins - click tick insurance for payment";
        String FIN_INSURANCE_CLICK_BUY = "ins - click buy";
        String FIN_INSURANCE_CHECKOUT = "ins - click pilih pembayaran";
        String FIN_INSURANCE_CHECKOUT_IMPRESSION = "ins - impression insurance box in checkout page";

        //Campaign
        String VIEW_POP_UP_MESSAGE_TIMER = "view pop up message payment time expired";
        String CLICK_BELANJA_LAGI_ON_POP_UP = "click belanja lagi on pop up message payment time expired";

        // Promo checkout revamp
        String CLICK_BUTTON_VERIFIKASI_NOMOR_HP = "click button verifikasi nomor HP promo page";
        String VIEW_AVAILABLE_PROMO_LIST = "view available promo list";
        String CLICK_PILIH_PROMO_RECOMMENDATION = "click pilih promo recommendation";
        String SELECT_KUPON = "select kupon";
        String DESELECT_KUPON = "deselect kupon";
        String CLICK_LIHAT_DETAIL_KUPON = "click lihat detail kupon";
        String CLICK_EXPAND_PROMO_LIST = "click expand promo list";
        String CLICK_REMOVE_PROMO_CODE = "click remove promo code";
        String CLICK_TERAPKAN_PROMO = "click terapkan promo";
        String SELECT_PROMO = "select promo";
        String DESELECT_PROMO = "deselect promo";
        String VIEW_POP_UP_SAVE_PROMO = "view pop up save promo";
        String CLICK_PAKAI_PROMO = "click pakai promo";
        String VIEW_ERROR_POP_UP = "view error pop up";
        String CLICK_COBA_LAGI = "click coba lagi";
        String CLICK_SIMPAN_PROMO_BARU = "click simpan promo baru";
        String CLICK_PILIH_PROMO = "click pilih promo";
        String CLICK_KELUAR_HALAMAN = "click keluar halaman";
        String CLICK_RESET_PROMO = "click reset promo";
        String CLICK_BELI_TANPA_PROMO = "click beli tanpa promo";
        String VIEW_PROMO_MESSAGE = "view promo message";
        String SELECT_PROMO_CODE_FROM_LAST_SEEN = "select promo code from Last Seen";
        String DISMISS_LAST_SEEN = "dismiss Last Seen";
        String CLICK_INPUT_FIELD = "click input field";
        String SHOW_LAST_SEEN_POP_UP = "show Last Seen pop-up";

        //OCC
        String ADD_PREFERENCE_OCC = "click tambah preferensi from tambah pilihan";
        String CLICK_TRASH_ICON_OCC = "click trash bin on edit preference";
        String CLICK_HAPUS_ON_TRASH_ICON_OCC = "click hapus preferensi from trash bin";
        String CLICK_PILIH_DURASI_PENGIRIMAN_IN_ANA_OCC = "click pilih durasi pengiriman in add new address step";
        String CLICK_DURASI_OPTION_IN_DURASI_PAGE = "click duration option in duration step";
        String CLICK_PILIH_METODE_PEMBAYARAN_IN_DURATION_PAGE = "click pilih metode pembayaran in duration step";
        String CLICK_PAYMENT_METHOD_OPTION_IN_PAYMENT_METHOD_PAGE = "click payment method option in payment method step";
        String CLICK_UBAH_ADDRESS_IN_PREFERENCE_SETTING_PAGE = "click ubah in address section on edit preference";
        String CLICK_UBAH_SHIPPING_IN_PREFERENCE_SETTING_PAGE = "click ubah in duration section on edit preference";
        String CLICK_UBAH_PAYMENT_IN_PREFERENCE_SETTING_PAGE = "click ubah in payment section on edit preference";
        String CLICK_ADDRESS_OPTION_IN_PILIH_ALAMAT_PAGE = "click available address option";
        String CLICK_SIMPAN_ALAMAT_IN_PILIH_ALAMAT_PAGE = "click simpan alamat on pilih alamat pengiriman";
        String CLICK_GEAR_LOGO_IN_PREFERENCE_LIST_PAGE = "click gear logo from tambah pilihan";
        String CLICK_JADIKAN_PILIHAN_UTAMA = "click button jadikan pilihan utama from tambah pilihan";
        String CLICK_BACK_ARROW_IN_EDIT_PREFERENCE = "click back in edit preference";
        String CLICK_BACK_ARROW_IN_PILIH_ALAMAT = "click back in pilih alamat";
        String CLICK_BACK_ARROW_IN_PILIH_DURASI = "click back in pilih durasi";
        String CLICK_BACK_ARROW_IN_PILIH_METHOD_PAYMENT = "click back in pilih metode bayar";
        String CLICK_SIMPAN_ON_SUMMARY_PURCHASE_SETTING = "click simpan on summary purchase setting";

        String EDIT_QUANTITY_INCRESE = "product - click button plus";
        String EDIT_QUANTITY_DECREASE = "product - click button minus";
        String EDIT_SELLER_NOTES = "product - click tulis catatan";
        String FIND_SIMILAR_PRODUCT = "product - click cari barang serupa";
        String USER_CHANGE_COURIER_OSP = "courier - click arrow to change courier option";
        String CLICK_UBAH_WHEN_DURATION_ERROR = "click ubah when duration error";
        String CLICK_SELECTED_DURATION_OPTION = "click selected duration option";
        String CLICK_ON_INSURANCE = "courier - click on asuransi pengiriman";
        String CLICK_BAYAR_NOT_SUCCESS = "click bayar - not success";
        String CLICK_PILIH_PEMBAYARAN_NOT_SUCCESS = "click pilih pembayaran - not success";
        String CLICK_PILIH_PEMBAYARAN = "click pilih pembayaran";
        String VIEW_ERROR_ON_OSP = "view error message";
        String CLICK_ADD_PREFERENSI_FROM_OSP = "preference - click tambah pilihan from ganti pilihan";
        String USER_CHANGES_PROFILE = "preference - click ganti pilihan on order summary";
        String USER_SETS_FIRST_PREFERENCE = "preference - click atur preferensi for new buyer";
        String CHOOSE_BBO_AS_DURATION = "courier - click pilih on tersedia bebas ongkir";
        String CHOOSE_COURIER_FROM_COURIER_SELECTION_OSP = "courier - click selected courier option";
        String CLICK_RINGKASAN_BELANJA_OSP = "click arrow on ringkasan belanja";
        String CLICK_GEAR_LOGO_IN_PREFERENCE_FROM_GANTI_PILIHAN_OSP = "preference - click gear logo from ganti pilihan";
        String GUNAKAN_PILIHAN_INI_FROM_GANTI_PILIHAN_OSP = "preference -  click gunakan pilihan ini from ganti pilihan";
        String CLICK_BACK_FROM_OSP = "click back in order summary page";
        String CLICK_BUTTON_INFO_ON_OSP = "click button info on order summary page";
        String CLICK_YUK_COBA_LAGI_IN_ONBOARDING_TICKER = "click yuk coba lagi in onboarding ticker";
        String VIEW_ONBOARDING_TICKER = "view onboarding ticker";
        String CLICK_PROMO_SECTION_APPLIED_OSP = "click promo section with promo applied";
        String CLICK_PROMO_SECTION_NOT_APPLIED_OSP = "click promo section with promo not applied";
        String CLICK_LANJUT_BAYAR_PROMO_ERROR_OSP = "promo - click lanjut bayar on bottom sheet promo error";
        String CLICK_PILIH_PROMO_LAIN_PROMO_ERROR_OSP = "promo - click pilih promo lain on bottom sheet promo error";

        String VIEW_ORDER_SUMMARY_PAGE = "view order summary page";
        String VIEW_ONBOARDING_INFO = "view onboarding info";
        String VIEW_BOTTOMSHEET_PROMO_ERROR = "view bottom sheet promo error";
        String VIEW_PROMO_ALREADY_APPLIED = "view promo already applied in order summary list";
        String VIEW_PROMO_RELEASED = "view promo released after adjust item";
        String VIEW_PROMO_DECREASED = "view promo decreased after adjust item";

        // OCC Revamp
        String CLICK_TAMBAH_TEMPLATE = "preference - click tambah template";
        String CLICK_PILIH_TEMPLATE_LAIN = "preference - click pilih template lain";
        String VIEW_PROFILE_LIST = "view profile list";
        String CLICK_ARROW_TO_CHANGE_ADDRESS_OPTION = "address - click arrow to change address option";
        String CLICK_SELECTED_ADDRESS_OPTION = "address - click selected address option";
        String CLICK_ARROW_TO_CHANGE_DURATION_OPTION = "duration - click arrow to change duration option";
        String CLICK_SELECTED_DURATION_OPTION_NEW = "duration - click selected duration option";
        String CLICK_ARROW_TO_CHANGE_PAYMENT_OPTION = "payment - click arrow to change payment option";
        String CLICK_SELECTED_PAYMENT_OPTION = "payment - click selected payment option";
        String CLICK_PROFILE_OPTION_ON_PROFILE_LIST = "preference - click profile option on profile list";
        String CLICK_TAMBAH_TEMPLATE_BELI_LANGSUNG_ON_ORDER_SUMMARY = "preference - click tambah template beli langsung on order summary";
        String CLICK_TAMBAH_TEMPLATE_BELI_LANGSUNG_ON_PROFILE_LIST = "preference - click tambah template beli langsung on profile list";
        String CLICK_EDIT_PROFILE_ON_PROFILE_LIST = "preference - click edit profile on profile list";

        String VIEW_COACHMARK_1_FOR_EXISTING_USER_ONE_PROFILE = "view coachmark 1 for existing user one profile";
        String VIEW_COACHMARK_2_FOR_EXISTING_USER_ONE_PROFILE = "view coachmark 2 for existing user one profile";
        String CLICK_DONE_ON_COACHMARK_2_FOR_EXISTING_USER_ONE_PROFILE = "click done on coachmark 2 for existing user one profile";

        String VIEW_COACHMARK_1_FOR_EXISTING_USER_MULTI_PROFILE = "view coachmark 1 for existing user multi profile";
        String VIEW_COACHMARK_2_FOR_EXISTING_USER_MULTI_PROFILE = "view coachmark 2 for existing user multi profile";
        String CLICK_DONE_ON_COACHMARK_2_FOR_EXISTING_USER_MULTI_PROFILE = "click done on coachmark 2 for existing user multi profile";

        String VIEW_COACHMARK_1_FOR_NEW_BUYER_BEFORE_CREATE_PROFILE = "view coachmark 1 for new buyer before create profile";
        String VIEW_COACHMARK_2_FOR_NEW_BUYER_BEFORE_CREATE_PROFILE = "view coachmark 2 for new buyer before create profile";
        String CLICK_LANJUT_ON_COACHMARK_2_FOR_NEW_BUYER_BEFORE_CREATE_PROFILE = "click lanjut on coachmark 2 for new buyer before create profile";

        String VIEW_COACHMARK_1_FOR_NEW_BUYER_AFTER_CREATE_PROFILE = "view coachmark 1 for new buyer after create profile";
        String VIEW_COACHMARK_2_FOR_NEW_BUYER_AFTER_CREATE_PROFILE = "view coachmark 2 for new buyer after create profile";
        String VIEW_COACHMARK_3_FOR_NEW_BUYER_AFTER_CREATE_PROFILE = "view coachmark 3 for new buyer after create profile";
        String CLICK_DONE_ON_COACHMARK_3_FOR_NEW_BUYER_AFTER_CREATE_PROFILE = "click done on coachmark 3 for new buyer after create profile";

        // OCC PP
        String PP_IMPRESSION_ON_INSURANCE_SECTION = "pp - impression on insurance section";
        String PP_CLICK_TOOLTIP = "pp - click tooltip";
        String PP_CLICK_BAYAR = "pp - click bayar";

        // Shipping experience
        String VIEW_SUMMARY_TRANSACTION_TICKER_COURIER_NOT_COMPLETE = "view summary transaction ticker courier not complete";
        String CLICK_CEK_ON_SUMMARY_TRANSACTION_TICKER_COURIER_NOT_COMPLETE = "click cek on summary transaction ticker courier not complete";

        // Cart Revamp
        String VIEW_REMAINING_STOCK_INFO = "view remaining stock info";
        String VIEW_INFORMATION_LABEL_IN_PRODUCT_CARD = "view information label in product card";
        String CLICK_DETAIL_TAGIHAN = "click detail tagihan";
        String ADD_WISHLIST_CART_LOGIN = "add wishlist - cart - login";
        String REMOVE_WISHLIST_CART_LOGIN = "remove wishlist - cart - login";
        String CLICK_DELETE_PRODUCT_ON_UNAVAILABLE_SECTION = "click delete product on unavailable section";
        String CLICK_LIHAT_PRODUK_SERUPA_ON_UNAVAILABLE_SECTION = "click lihat produk serupa on unavailable section";
        String CLICK_CHECKOUT_MELALUI_BROWSER_ON_UNAVAILABLE_SECTION = "click checkout melalui browser on unavailable section";
        String CLICK_DELETE_ALL_UNAVAILABLE_PRODUCT = "click delete all unavailable product";
        String CLICK_ACCORDION_ON_UNAVAILABLE_PRODUCT = "click %s on unavailable section";
        String CLICK_UNDO_AFTER_DELETE_PRODUCT = "click undo after delete product";
        String VIEW_ERROR_PAGE_WHEN_LOAD_CART = "view error page when load cart";
        String CLICK_WISHLIST_ICON_IN_CART_PAGE = "click wishlist icon in cart page";
        String CLICK_FOLLOW_SHOP_ON_UNAVAILABLE_SECTION = "click follow shop on unavailable section";
        String CLICK_BACK_BUTTON_NAV = "click back button nav";
        String CLICK_GLOBAL_MENU_NAV = "click global menu nav";
        String CLICK_WISHLIST_NAV = "click wishlist nav";
    }

    interface EventLabel {
        String SUCCESS = "success";
        String ERROR = "error";
        String NOT_SUCCESS = "not success";
        String COURIER_NOT_COMPLETE = "courier not complete";
        String PROMO_RED_STATE = "promo red state";
        String KUOTA_PENUKARAN = "kuota penukaran";
        String FAILED = "failed";
        String CHECKLIST = "checklist";
        String UN_CHECKLIST = "unchecklist";
        String FAILED_DROPSHIPPER = "failed dropshipper";

        String CHECKOUT_SUCCESS_DEFAULT = "success - default";
        String CHECKOUT_SUCCESS_CHECK_ALL = "success - check all";
        String CHECKOUT_SUCCESS_PARTIAL_SHOP = "success - partial shop";
        String CHECKOUT_SUCCESS_PARTIAL_PRODUCT = "success - partial product";
        String CHECKOUT_SUCCESS_PARTIAL_SHOP_AND_PRODUCT = "success - partial shop and product";

        String CHECKOUT_SUCCESS_DEFAULT_ELIGIBLE_COD = "success - default - cod";
        String CHECKOUT_SUCCESS_CHECK_ALL_ELIGIBLE_COD = "success - check all - cod";
        String CHECKOUT_SUCCESS_PARTIAL_SHOP_ELIGIBLE_COD = "success - partial shop - cod";
        String CHECKOUT_SUCCESS_PARTIAL_PRODUCT_ELIGIBLE_COD = "success - partial product - cod";
        String CHECKOUT_SUCCESS_PARTIAL_SHOP_AND_PRODUCT_ELIGIBLE_COD = "success - partial shop and product - cod";

        String CHECKOUT_COUPON_AUTO_APPLY = "coupon autoapply";
        String CHECKOUT_COUPON_OR_PROMO_MANUAL_APPLY = "%s manual apply";

        String PRODUCT_WISHLIST = "product wishlist";
        String PRODUCT_LAST_SEEN = "product last seen";
        String PRODUCT_RECOMMENDATION = "product recommendation";

        String SUCCESS_TICKED_PPP = "success - yes";
        String SUCCESS_UNTICKED_PPP = "success - no";
        String APPEAR = "appear";

        String SUCCESS_DEFAULT = "success - default";
        String SUCCESS_NOT_DEFAULT = "success - not default";

        String SEPARATOR = " - ";
        String PROMO = "promo";
        String NON_PROMO = "non promo";
        String COD = "cod";

        String SOURCE_CART = "source: cart";

        // Promo checkout revamp
        String INELIGIBLE_PRODUCT = "ineligible product";
        String NO_PROMO = "no promo";
        String INELIGIBLE_PROMO_LIST = "ineligible promo list";
        String ELIGIBLE_PROMO = "eligible promo";
        String FAILED_TERJADI_KESALAHAN_SERVER = "failed - error terjadi kesalahan server";
        String BLACKLIST_ERROR = "blacklist error";
        String PHONE_VERIFICATION_MESSAGE = "phone verification message";

        String NEW_OCC = "new occ";
        String NEW_BUYER = "new buyer";
        String SHOP_CLOSED = "shop closed";
        String EMPTY_STOCK = "empty_stock";
    }

    interface ExtraKey {
        String USER_ID = "userId";
        String PROMO_CODE = "promoCode";
        String PAYMENT_TYPE = "paymentType";
        String BUSINESS_UNIT = "businessUnit";
        String CURRENT_SITE = "currentSite";
        String PAGE_TYPE = "pageType";
        String PAGE_PATH = "pagePath";
    }

    interface ScreenName {
        String CART = "/cart";
        String CHECKOUT = "/cart/shipment";
        String ONE_CLICK_SHIPMENT = "/cart/shipment/ocs";
        String SELECT_COURIER = "/selectcourier";
        String PROMO_PAGE_FROM_CART_TAB_PROMO = "/cart#voucher";
        String PROMO_PAGE_FROM_CART_TAB_COUPON = "/cart#coupon";
        String PROMO_PAGE_FROM_CHECKOUT_TAB_PROMO = "/cart/shipment#voucher";
        String PROMO_PAGE_FROM_CHECKOUT_TAB_COUPON = "/cart/shipment#coupon";
        String MULTI_ADDRESS_PAGE = "/cart/shipment/multiple";
        String EDIT_MULTIPLE_ADDRESS_PAGE = "/cart/shipment/multiple/edit";
        String ADDRESS_LIST_PAGE = "/cart/address";
        String ADD_NEW_ADDRESS_PAGE = "/cart/address/create";
        String ADD_NEW_ADDRESS_PAGE_FROM_EMPTY_ADDRESS_CART = "/user/address/create/cart";
        String ADD_NEW_ADDRESS_PAGE_USER = "/user/address/create";
        String EXPRESS_CHECKOUT = "/express-checkout";
    }

    interface CustomDimension {
        String DIMENSION_CURRENT_SITE_MARKETPLACE = "tokopediamarketplace";
        String DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM = "purchase platform";
        String DIMENSION_BUSINESS_UNIT_HOME_BROWSE = "home & browse";
    }
}
