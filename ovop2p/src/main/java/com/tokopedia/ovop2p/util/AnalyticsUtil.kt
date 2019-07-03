package com.tokopedia.ovop2p.util

import android.content.Context

import com.tokopedia.track.TrackApp

import java.util.HashMap

object AnalyticsUtil {
    interface EventName {
        companion object {
            val CLICK_OVO = "clickOVO"
        }
    }

    interface EventCategory {
        companion object {
            val OVO_TRNSFR = "ovo transfer"
            val OVO_CONF_TRANSFER = "ovo konfirmasi transfer"
            val OVO_SUMRY_TRNSFR_SUCS = "ovo summary transfer sukses"
            val OVO_SUMRY_TRNSFR_GAGAL = "ovo summary transfer gagal"
        }
    }

    interface EventAction {
        companion object {
            val CLK_LNJKTN = "click lanjuktan"
            val CLK_TRNSFR = "click transfer"
            val CLK_BTLKN = "click batalkan"
            val CLK_TRY_AGN = "click coba lagi"
            val CLK_SEE_DTL = "click lihat detail"
            val CLK_KMBL_TKPD = "click kembali ke tokopedia"
        }
    }

    interface Keys {
        companion object {
            val USR_ID = "user_id"
            val DEVICE_ID = "device_id"
            val ENT = "event"
            val ENT_CAT = "eventCategory"
            val ENT_ACT = "eventAction"
            val ENT_LBL = "eventLable"
        }
    }

    fun sendEvent(context: Context, event: String, category: String,
                  label: String, action: String) {
        val eventMap = HashMap<String, Any>()
        eventMap[Keys.USR_ID] = OvoP2pUtil.getUserName(context)
        eventMap[Keys.DEVICE_ID] = OvoP2pUtil.getDeviceId(context)
        eventMap[Keys.ENT] = event
        eventMap[Keys.ENT_ACT] = action
        eventMap[Keys.ENT_CAT] = category
        eventMap[Keys.ENT_LBL] = label
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }
}
