package com.tokopedia.instantloan.common.analytics

import javax.inject.Inject
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

class InstantLoanAnalytics @Inject
constructor() {

    fun eventLoanBannerImpression(eventLabel: String) {
        TrackApp.getInstance()?.getGTM()?.sendGeneralEvent(TrackAppUtils.gtmData(InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_BANNER_IMPRESSION,
                eventLabel))
    }

    fun eventLoanBannerClick(eventLabel: String) {
        TrackApp.getInstance()?.getGTM()?.sendGeneralEvent(TrackAppUtils.gtmData(
                InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_BANNER_CLICK,
                eventLabel
        ))
    }

    fun eventCariPinjamanClick(eventLabel: String) {
        TrackApp.getInstance()?.getGTM()?.sendGeneralEvent(TrackAppUtils.gtmData(
                InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_CARI_PINJAMAN_CLICK,
                eventLabel
        ))
    }

    fun eventLoanPopupClick(eventLabel: String) {
        TrackApp.getInstance()?.getGTM()?.sendGeneralEvent(TrackAppUtils.gtmData(
                InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_POP_UP_CLICK,
                eventLabel
        ))
    }

    fun eventIntroSliderScrollEvent(eventLabel: String) {
        TrackApp.getInstance()?.getGTM()?.sendGeneralEvent(TrackAppUtils.gtmData(
                InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_POP_UP_CLICK,
                eventLabel
        ))
    }

    fun eventInstantLoanPermissionStatus(eventLabel: String) {
        TrackApp.getInstance()?.getGTM()?.sendGeneralEvent(TrackAppUtils.gtmData(
                InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_POP_UP_CLICK,
                eventLabel
        ))
    }
}
