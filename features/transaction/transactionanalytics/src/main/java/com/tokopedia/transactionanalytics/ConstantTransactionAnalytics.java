package com.tokopedia.transactionanalytics;

/**
 * @author anggaprasetiyo on 18/05/18.
 */
public interface ConstantTransactionAnalytics {


    interface EventName {
        String CLICK_ATC = "clickATC";
        String VIEW_ATC = "viewATC";
        String REMOVE_FORM_CART = "removeFromCart";
    }

    interface EventCategory {
        String CART = "Cart";
        String COURIER_SELECTION = "Courier Selection";
        String ADD_TO_CART = "add to cart";
        String CART_CHANGE_ADDRESS = "Cart Change Address";
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
        String CLICK_PILIH_SEMUA_FORM_HAPUS = "click pilih semua from hapus";
        String CLICK_CHECKLIST_BOX_FORM_HAPUS = "click checklist box from hapus";
        String CLICK_HAPUS_FORM_HAPUS = "click hapus from hapus";
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

        // Cart change address starts here

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
        // Cart change address ends here

        String VIEW_IMPRESSION_CART_EMPTY = "impression cart empty";
        String IMPRESSION_ATC_SUCCESS = "impression atc success";

        String IMPRESSION_COURIER_SELECTION = "impression courier selection";
    }

    interface EventLabel {
        String CLICK_BELI = "click beli";
        String CLICK_HAPUS_FROM_TRASH_BIN = "click hapus from trash bin";
        String CLICK_HAPUS_DAN_TAMBAH_WISHLIST_FROM_TRASH_BIN = "click hapus dan tambah wishlist from trash bin";
        String CLICK_HAPUS_PRODUK_BERKENDALA = "click hapus produk berkendala";
    }
}
