package com.tokopedia.tokopoints.view.util;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyticsTrackerUtil {
    public interface EventKeys {
        String EVENT = "event";
        String EVENT_CATEGORY = "eventCategory";
        String EVENT_ACTION = "eventAction";
        String EVENT_LABEL = "eventLabel";
        String ECOMMERCE = "ecommerce";
        String EVENT_TOKOPOINT = "eventTokopoint";
        String EVENT_LUCKY_EGG = "luckyEggClick";
        String EVENT_VIEW_TOKOPOINT = "eventViewTokopoint";
        String EVENT_CLICK_COUPON = "clickCoupon";
        String EVENT_VIEW_COUPON = "viewCoupon";
        String EVENT_VIEW_PROMO = "promoView";
        String TOKOPOINTS_LABEL = "tokopoints";
        String TOKOPOINTS_ON_BOARDING_LABEL = "tokopoints on boarding";
        String TOKOPOINTS_LUCKY_EGG_CLOSE_LABEL = "close cara mendapatkan lucky egg";
        String BACK_ARROW_LABEL = "back arrow detail kupon";
    }

    public interface CategoryKeys {
        String HOMEPAGE = "homepage-tokopoints";
        String TOKOPOINTS = "tokopoints";
        String TOKOPOINTS_EGG = "tokopoints-egg";
        String TOKOPOINTS_PENUKARAN_POINT = "tokopoints-penukaran point";
        String PENUKARAN_POINT_DETAIL = "penukaran point - coupon detail";
        String PENUKARAN_POINT = "penukaran point";
        String TOKOPOINTS_KUPON_SAYA = "tokopoints-kupon saya";
        String KUPON_MILIK_SAYA = "kupon milik saya";
        String KUPON_MILIK_SAYA_DETAIL = "kupon milik saya - coupon detail";
        String RIWAYAT_TOKOPOINTS = "riwayat tokopoints";
        String POPUP_KONFIRMASI = "pop up konfirmasi tukar points";
        String POPUP_PENUKARAN_BERHASIL = "pop up penukaran berhasil";
        String POPUP_PENUKARAN_POINT_TIDAK = "pop up point tidak cukup";
        String POPUP_KUOTA_HABIS = "pop up kuota penukaran habis";
        String POPUP_VERIFIED = "pop up belum verified";
        String POPUP_KONFIRMASI_GUNAKAN_KUPON = "pop up konfirmasi gunakan kupon";
        String POPUP_KIRIM_KUPON = "pop up kirim kupon";
        String POPUP_TERIMA_HADIAH = "pop up terima hadiah kupon";
    }

    public interface ActionKeys {
        String CLICK_POINT = "click point & tier status";
        String CLICK_CEK = "click cek tokopoints";
        String CLICK_MEMBERSHIP = "click lihat status membership";
        String CLICK_POINT_SAYA = "click points saya";
        String CLICK_LOYALTY_SAYA = "click loyalty saya";
        String VIEW_TICKER = "view ticker";
        String CLICK_LIHAT_SEMUA = "click lihat semua";
        String CLICK_BANTUAN = "click bantuan tokopoints";
        String CLICK_EGG = "click floating lucky egg";
        String CLICK_EGG_EMPTY = "click raih points dan kumpulkan loyalty";
        String CLICK_EGG_BELI = "click beli - untuk dapat lucky egg";
        String CLICK_EGG_BAYAR = "click bayar - untuk dapat lucky egg";
        String CLICK_EGG_PESAWAT = "click pesawat - untuk dapat lucky egg";
        String CLICK_EGG_KARETA = "click kereta - untuk dapat lucky egg";
        String CLICK_CLOSE_BUTTON = "click close button";
        String CLICK_KETENTUAN = "click ketentuan";
        String CLICK_CARA_PAKAI = "click cara pakai";
        String CLICK_BACK_ARROW = "click back arrow";
        String CLICK_GUNAKAN = "click gunakan";
        String CLICK_TUKAR = "click tukar";
        String CLICK_BATAL = "click batal";
        String CLICK_NANTI_SAJA = "click nanti saja";
        String CLICK_OK = "click ok";
        String CLICK_BELANJA = "click belanja";
        String CLICK_INCOMPLETE_PROFILE = "click lengkapi profil";
        String CLICK_LIHAT_KUPON = "click lihat kupon";
        String VIEW_COUPON = "view coupon detail";
        String VIEW_MY_COUPON_DETAIL = "view my coupon detail";
        String VIEW_MY_COUPON = "view my coupon";
        String CLICK_COUPON = "click coupon";
        String CLICK_PENUKARAN = "click penukaran point";
        String CLICK_KUPON_SAYA = "click kupon saya";
        String CLICK_DYNAMIC_CAT = "click dynamic category";
        String VIEW_DYNAMIC_CAT = "view dynamic category";
        String CLICK_FLOATING_LUCKY = "click floating lucky egg";
        String CLICK_FILTER = "click filter";
        String PILIH_FILTER = "pilih filter";
        String CLICK_SAVE_FILTER = "click simpan filter";
        String SWIPE_COUPON = "swipe untuk lihat code";
        String COPY_CODE = "salin kode swipe";
        String CLICK_MEM_BOTTOM = "click footer status membership";
        String CLICK_SELL_ALL_COUPON = "click kupon milik saya";
        String CLICK_PENUKARAN_POINTS = "click penukaran point";
        String CLICK_COUPON_SAYA = "click coupon saya";
        String CLICK_KIRIM_SEKARANG = "click kirim sekarang";
        String CLICK_OK_ON_SUCCESS = "click ok on success";
        String CLICK_OK_ON_FAILED = "click ok on failed";
        String CLICK_GUNAKAN_KUPON = "click gunakan kupon";
        String VIEW_REDEEM_SUCCESS = "view redeem success";
    }

    public static void sendScreenEvent(Activity context, String screenName) {
        TrackApp.getInstance().getGTM().sendScreenAuthenticated( screenName);
    }

    public static void sendEvent(Context context, String event, String category,
                                 String action, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(event, category, action, label));
    }

    public static void sendECommerceEvent(Context context, String event, String category,
                                          String action, String label, Map<String, Map<String, List<Map<String, String>>>> ecommerce) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(EventKeys.EVENT, event);
        map.put(EventKeys.EVENT_CATEGORY, category);
        map.put(EventKeys.EVENT_ACTION, action);
        map.put(EventKeys.EVENT_LABEL, label);
        map.put(EventKeys.ECOMMERCE, ecommerce);
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(map);
    }

    public interface ScreenKeys {
        String MY_COUPON_LISTING_SCREEN_NAME = "/tokopoints/kupon-saya";
        String COUPON_CATALOG_SCREEN_NAME = "/tokopoints/tukar-point/detail";
        String CATALOG_LISTING_SCREEN_NAME = "/tokopoints/tukar-point";
        String COUPON_DETAIL_SCREEN_NAME = "/tokopoints/kupon-saya/detail";
        String HOME_PAGE_SCREEN_NAME = "/tokopoints";
    }
}
