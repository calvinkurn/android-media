package com.tokopedia.topads.trackers

import com.tokopedia.topads.trackers.SeePerformanceTopadsTrackerConst.Action.CLICK_CREDIT_TOPADS
import com.tokopedia.topads.trackers.SeePerformanceTopadsTrackerConst.Action.CLICK_DATE_RANGE
import com.tokopedia.topads.trackers.SeePerformanceTopadsTrackerConst.Action.CLICK_EXPAND_GROUP_SETTINGS
import com.tokopedia.topads.trackers.SeePerformanceTopadsTrackerConst.Action.CLICK_EXPAND_TIPS
import com.tokopedia.topads.trackers.SeePerformanceTopadsTrackerConst.Action.CLICK_GROUP_IKLAN
import com.tokopedia.topads.trackers.SeePerformanceTopadsTrackerConst.Action.CLICK_IKLAN_LIHAT_SELENGKAPNYA
import com.tokopedia.topads.trackers.SeePerformanceTopadsTrackerConst.Action.CLICK_IKLAN_OTOMATIS
import com.tokopedia.topads.trackers.SeePerformanceTopadsTrackerConst.Action.CLICK_IKLAN_PENCARIAN
import com.tokopedia.topads.trackers.SeePerformanceTopadsTrackerConst.Action.CLICK_IKLAN_REKOMENDASI
import com.tokopedia.topads.trackers.SeePerformanceTopadsTrackerConst.Action.CLICK_IKLAN_SEMUA_PENEMPATAN
import com.tokopedia.topads.trackers.SeePerformanceTopadsTrackerConst.Action.CLICK_MUAT_ULANG
import com.tokopedia.topads.trackers.SeePerformanceTopadsTrackerConst.Action.CLICK_PENEMPATAN_IKLAN
import com.tokopedia.topads.trackers.SeePerformanceTopadsTrackerConst.Action.CLICK_STATUS_IKLAN
import com.tokopedia.topads.trackers.SeePerformanceTopadsTrackerConst.Action.CLICK_STATUS_IKLAN_AKTIF
import com.tokopedia.topads.trackers.SeePerformanceTopadsTrackerConst.Action.CLICK_STATUS_IKLAN_TIDAK_AKTIF
import com.tokopedia.topads.trackers.SeePerformanceTopadsTrackerConst.Action.CLICK_TAMBAH_KREDIT
import com.tokopedia.topads.trackers.SeePerformanceTopadsTrackerConst.Action.CLICK_TAMBAH_KREDIT_PAGE
import com.tokopedia.topads.trackers.SeePerformanceTopadsTrackerConst.Action.VIEW_ERROR_FETCHING
import com.tokopedia.topads.trackers.SeePerformanceTopadsTrackerConst.Action.VIEW_KREDIT_PAGE

object SeePerformanceTopadsTrackerConst {

    const val EVENT_CLICK = "clickTopAds"
    const val EVENT_VIEW = "viewTopAdsIris"
    const val EVENT_CATEGORY_MP = "product list page"
    const val EVENT_CATEGORY_PDP = "product list page"
    const val EVENT_LABEL = ""
    const val BUSINESS_UNIT = "ads solution"
    const val CURRENT_SITE_MANAGE_PRODUCT_PAGE = "tokopediaseller"
    const val CURRENT_SITE_PRODUCT_DETAIL_PAGE = "pdp"

    object Action {
        const val CLICK_GROUP_IKLAN = "click - atur group iklan"
        const val CLICK_IKLAN_OTOMATIS = "click - atur iklan otomatis"
        const val CLICK_DATE_RANGE = "click - date range"
        const val VIEW_ERROR_FETCHING = "view - error fetching"
        const val CLICK_MUAT_ULANG = "click - muat ulang"
        const val CLICK_CREDIT_TOPADS = "click - credit topads"
        const val CLICK_EXPAND_GROUP_SETTINGS = "click - expand group setting"
        const val CLICK_PENEMPATAN_IKLAN = "click - filter penempatan iklan"
        const val CLICK_IKLAN_PENCARIAN = "click - filter penempatan iklan di pencarian"
        const val CLICK_IKLAN_REKOMENDASI = "click - filter penempatan iklan di rekomendasi"
        const val CLICK_IKLAN_SEMUA_PENEMPATAN = "click - filter penempatan iklan semua penempatan"
        const val CLICK_STATUS_IKLAN = "click - status iklan"
        const val CLICK_STATUS_IKLAN_AKTIF = "click - status iklan aktif"
        const val CLICK_STATUS_IKLAN_TIDAK_AKTIF = "click - status iklan tidak aktif"
        const val VIEW_KREDIT_PAGE = "view - tambah kredit page"
        const val CLICK_TAMBAH_KREDIT_PAGE = "click - tambah kredit page"
        const val CLICK_TAMBAH_KREDIT = "click - tambah kredit"
        const val CLICK_EXPAND_TIPS = "click - expand tips optimalkan iklan"
        const val CLICK_IKLAN_LIHAT_SELENGKAPNYA = "click - tips optimalkan iklan lihat selengkapnya"
    }

    private val trackerIdsManageProductPage = mapOf(
        CLICK_GROUP_IKLAN to "41276",
        CLICK_IKLAN_OTOMATIS to "41277",
        CLICK_DATE_RANGE to "41278",
        VIEW_ERROR_FETCHING to "41279",
        CLICK_MUAT_ULANG to "41280",
        CLICK_CREDIT_TOPADS to "41281",
        CLICK_EXPAND_GROUP_SETTINGS to "41282",
        CLICK_PENEMPATAN_IKLAN to "41283",
        CLICK_IKLAN_PENCARIAN to "41284",
        CLICK_IKLAN_REKOMENDASI to "41285",
        CLICK_IKLAN_SEMUA_PENEMPATAN to "41286",
        CLICK_STATUS_IKLAN to "41287",
        CLICK_STATUS_IKLAN_AKTIF to "41288",
        CLICK_STATUS_IKLAN_TIDAK_AKTIF to "41289",
        VIEW_KREDIT_PAGE to "41290",
        CLICK_TAMBAH_KREDIT_PAGE to "41291",
        CLICK_TAMBAH_KREDIT to "41292",
        CLICK_EXPAND_TIPS to "41293",
        CLICK_IKLAN_LIHAT_SELENGKAPNYA to "41294"
    )

    private val trackerIdsProductDetailPage = mapOf(
        CLICK_GROUP_IKLAN to "41257",
        CLICK_IKLAN_OTOMATIS to "41258",
        CLICK_DATE_RANGE to "41259",
        VIEW_ERROR_FETCHING to "41260",
        CLICK_MUAT_ULANG to "41261",
        CLICK_CREDIT_TOPADS to "41262",
        CLICK_EXPAND_GROUP_SETTINGS to "41263",
        CLICK_PENEMPATAN_IKLAN to "41264",
        CLICK_IKLAN_PENCARIAN to "41265",
        CLICK_IKLAN_REKOMENDASI to "41266",
        CLICK_IKLAN_SEMUA_PENEMPATAN to "41267",
        CLICK_STATUS_IKLAN to "41268",
        CLICK_STATUS_IKLAN_AKTIF to "41269",
        CLICK_STATUS_IKLAN_TIDAK_AKTIF to "41270",
        VIEW_KREDIT_PAGE to "41271",
        CLICK_TAMBAH_KREDIT_PAGE to "41272",
        CLICK_TAMBAH_KREDIT to "41273",
        CLICK_EXPAND_TIPS to "41274",
        CLICK_IKLAN_LIHAT_SELENGKAPNYA to "41275"
    )

    fun getTrackerId(action: String, site: String): String {
        return when (site) {
            CURRENT_SITE_MANAGE_PRODUCT_PAGE -> trackerIdsManageProductPage[action] ?: ""
            CURRENT_SITE_PRODUCT_DETAIL_PAGE -> trackerIdsProductDetailPage[action] ?: ""
            else -> ""
        }
    }
}
