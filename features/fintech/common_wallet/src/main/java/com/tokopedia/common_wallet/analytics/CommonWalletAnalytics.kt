package com.tokopedia.common_wallet.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

import javax.inject.Inject

class CommonWalletAnalytics @Inject constructor() {

    fun eventClickActivationOvoHomepage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(HOME_PAGE, Category.HOMEPAGE,
                Action.CLICK_HOME_ACTIVATION_OVO, ""))
    }

    companion object {
        private const val HOME_PAGE = "clickHomepage"
    }

    private object Category {
        internal var HOMEPAGE = "homepage"
    }

    private object Action {
        internal var CLICK_HOME_ACTIVATION_OVO = "click aktivasi ovo pada homepage"
    }
}
