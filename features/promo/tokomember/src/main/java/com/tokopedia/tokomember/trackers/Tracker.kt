package com.tokopedia.tokomember.trackers

import androidx.annotation.IntDef
import com.tokopedia.tokomember.trackers.TokomemberSource.Companion.DEFAULT
import com.tokopedia.tokomember.trackers.TokomemberSource.Companion.THANK_YOU
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

object Tracker {

    fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    object Constants {
        const val EVENT = "event"
        const val EVENT_CATEGORY = "eventCategory"
        const val EVENT_ACTION = "eventAction"
        const val EVENT_LABEL = "eventLabel"
        const val USER_ID = "userId"
        const val BUSINESS_UNIT = "businessUnit"
        const val TOKOPOINT_BUSINESSUNIT = "tokopoints"
        const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
        const val TOKOMEMBER_BUSINESSUNIT = "tokomember"
        const val CURRENT_SITE = "currentSite"
        const val ECOMMERCE = "ecommerce"
    }

    object Event {
        const val CLICK_TM_BOTTOM_SHEET = "clickBGP"
        const val VIEW_TM_BOTTOM_SHEET = "viewBGPIris"
    }

    object Category {
        const val TM_THANK_YOU = "tokomember - thank you page"
    }

    object Action {
        const val VIEW_TM_BS_OPEN = "open membership - view bottom sheet"
        const val CLICK_TM_BS_OPEN_CTA = "open membership - click bottom sheet - cta"
        const val CLICK_TM_BS_OPEN_CLOSE = "open membership - click bottom sheet - close"
        const val VIEW_TM_BS_CLOSE = "close membership - view bottom sheet"
        const val CLICK_TM_BS_CLOSE_CTA = "open membership - click bottom sheet - cta"
        const val CLICK_TM_BS_CLOSE_CLOSE = "open membership - click bottom sheet - close"

    }
        object Label {
            const val PDP_VIEW = "pdp view"
            const val SHOP_PAGE = "shop page"
            const val MVC_CLOSE_VIEW_SELEGKAPANYA = "mvc_closed_lihat_selengkapnya"
            const val MVC_CLOSE_VIEW_MULAIBELANJA = "mvc_closed_mulai_belanja"
            const val MVC_CLOSE_CEK_INFO = "mvc_closed_cek_info"
        }

        fun fillCommonItems(map: MutableMap<String, Any>, userId: String?, businessUnit: String) {
            map[Constants.BUSINESS_UNIT] = businessUnit
            map[Constants.CURRENT_SITE] = Constants.TOKOPEDIA_MARKETPLACE
            userId?.let {
                map[Constants.USER_ID] = userId
            }
        }
    }

@Retention(AnnotationRetention.SOURCE)
@IntDef(DEFAULT, THANK_YOU)
annotation class TokomemberSource {
    companion object {
        const val DEFAULT = 0
        const val THANK_YOU = 1
    }
}
