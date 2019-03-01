package com.tokopedia.transactionanalytics;

/**
 * @author anggaprasetiyo on 18/05/18.
 */
public interface ConstantTransactionAnalytics {

    interface Key {
        String EVENT = "event";
        String EVENT_CATEGORY = "eventCategory";
        String EVENT_ACTION = "eventAction";
        String EVENT_LABEL = "eventLabel";
        String E_COMMERCE = "ecommerce";
        String PAYMENT_ID = "payment_id";
        String CURRENT_SITE = "currentSite";
    }


    interface EventName {
        String CLICK_ATC = "clickATC";
        String CLICK_BUY = "clickBuy";
        String CLICK_CHECKOUT = "clickCheckout";
        String VIEW_ATC = "viewATC";
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
        String CLICK_PDP = "clickPDP";
        String PURCHASE_PROTECTION = "fintechppandroid";
        String VIEW_CART = "viewCart";
        String CLICK_REGISTER = "clickRegister";
        String VIEW_REGISTER = "viewRegister";
    }

    interface EventCategory {
        String CART = "cart";
        String COURIER_SELECTION = "courier selection";
        String ADD_TO_CART = "add to cart";
        String CART_CHANGE_ADDRESS = "cart change address";
        String CART_MULTIPLE_ADDRESS = "cart multiple address";
        String ORDER_TRACKING = "order tracking";
        String PRODUCT_DETAIL_PAGE = "product detail page";
        String PURCHASE_PROTECTION = "fin - mp checkout";
        String CORNER_ADDRES = "tokopedia corner address";
    }

    interface EventAction {
        String CLICK_GUNAKAN_KODE_PROMO_ATAU_KUPON = "click gunakan kode promo atau kupon";
        String CLICK_HAPUS_ON_TOP_RIGHT_CORNER = "click hapus on top right corner";
        String CLICK_SHOP_NAME = "click shop name";
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

        String VALIDATION_ERROR_ALAMAT_SEBAGAI_PADA_TAMBAH_ADDRESS = "validation error alamat sebagai pada + address";
        String VALIDATION_ERROR_NAMA_PADA_TAMBAH_ADDRESS = "validation error nama pada + address";
        String VALIDATION_ERROR_TELEPON_PADA_TAMBAH_ADDRESS = "validation error telepon pada + address";
        String VALIDATION_ERROR_KOTA_KECAMATAN_PADA_TAMBAH_ADDRESS = "validation error kota / kecamatan pada + address";
        String VALIDATION_ERROR_KODE_POS_PADA_TAMBAH_ADDRESS = "validation error kode pos pada + address";
        String VALIDATION_ERROR_ALAMAT_PADA_TAMBAH_ADDRESS = "validation error alamat pada + address";

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
        String CLICK_PRODUCT_RECOMMENDATION = "click product recommendation on empty cart";
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
    }

    interface EventLabel {
        String SUCCESS = "success";
        String NOT_SUCCESS = "not success";
        String COURIER_NOT_COMPLETE = "courier not complete";
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

        String CHECKOUT_COUPON_AUTO_APPLY = "coupon autoapply";
        String CHECKOUT_COUPON_OR_PROMO_MANUAL_APPLY = "%s manual apply";

        String PRODUCT_WISHLIST = "product wishlist";
        String PRODUCT_LAST_SEEN = "product last seen";
        String PRODUCT_RECOMMENDATION = "product recommendation";

        String SUCCESS_TICKED_PPP = "success - yes";
        String SUCCESS_UNTICKED_PPP = "success - no";
        String APPEAR = "appear";
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
    }

    interface CustomDimension {
        String DIMENSION_CURRENT_SITE_MARKETPLACE = "tokopediamarketplace";
    }
}
