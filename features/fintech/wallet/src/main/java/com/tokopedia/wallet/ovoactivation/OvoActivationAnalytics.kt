package com.tokopedia.wallet.ovoactivation


import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

import javax.inject.Inject

/**
 * Created by nabillasabbaha on 17/10/18.
 */
class OvoActivationAnalytics @Inject constructor() {

    fun eventClickActivationOvoNow() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT, Category.TOKOCASH_TO_OVO,
                Action.CLICK_ACTIVATION_OVO_NOW, ""))
    }

    fun eventClickOvoLearnMore() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT, Category.TOKOCASH_TO_OVO,
                Action.CLICK_LEARN_MORE, ""))
    }

    fun eventClickTnc() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT, Category.TOKOCASH_TO_OVO,
                Action.CLICK_TNC, ""))
    }

    fun eventClickPopupPhoneNumber(textButton: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT, Category.TOKOCASH_TO_OVO,
                Action.CLICK_PHONE_NUMBER + textButton, ""))
    }

    fun eventClickMakeNewOvoAccount() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT, Category.ACTIVATION_OVO,
                Action.CLICK_OVO_NEW_ACCOUNT, ""))
    }

    fun eventClickChangePhoneNumber() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(GENERIC_EVENT, Category.ACTIVATION_OVO,
                Action.CLICK_OVO_CHANGE_PHONE_NUMBER, ""))
    }

    fun eventClickActivationOvoHomepage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(HOME_PAGE, Category.HOMEPAGE,
                Action.CLICK_HOME_ACTIVATION_OVO, ""))
    }

    companion object {
        private const val GENERIC_EVENT = "clickSaldo"
        private const val HOME_PAGE = "clickHomepage"
    }

    private object Category {
        internal var TOKOCASH_TO_OVO = "tokocash to ovo"
        internal var ACTIVATION_OVO = "aktivasi ovo baru"
        internal var HOMEPAGE = "homepage"
    }

    private object Action {
        internal var CLICK_ACTIVATION_OVO_NOW = "click aktifkan ovo sekarang"
        internal var CLICK_LEARN_MORE = "click pelajari lebih lanjut"
        internal var CLICK_TNC = "click lihat syarat & ketentuan"
        internal var CLICK_PHONE_NUMBER = "click "
        internal var CLICK_OVO_NEW_ACCOUNT = "click buat akun ovo baru"
        internal var CLICK_OVO_CHANGE_PHONE_NUMBER = "click ubah nomor ponsel sekarang"
        internal var CLICK_HOME_ACTIVATION_OVO = "click aktivasi ovo pada homepage"
    }
}
