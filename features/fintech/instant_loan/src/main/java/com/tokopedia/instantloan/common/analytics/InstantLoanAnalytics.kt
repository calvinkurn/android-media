package com.tokopedia.instantloan.common.analytics

import android.content.Context

import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext

import javax.inject.Inject

class InstantLoanAnalytics @Inject
constructor(@ApplicationContext context: Context?) {

    private var tracker: AnalyticTracker? = (context?.applicationContext as? AbstractionRouter)?.analyticTracker

    fun eventLoanBannerImpression(eventLabel: String) {
        tracker?.sendEventTracking(InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_BANNER_IMPRESSION,
                eventLabel)
    }

    fun eventLoanBannerClick(eventLabel: String) {
        tracker?.sendEventTracking(
                InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_BANNER_CLICK,
                eventLabel
        )
    }

    fun eventCariPinjamanClick(eventLabel: String) {
        tracker?.sendEventTracking(
                InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_CARI_PINJAMAN_CLICK,
                eventLabel
        )
    }

    fun eventLoanPopupClick(eventLabel: String) {
        tracker?.sendEventTracking(
                InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_POP_UP_CLICK,
                eventLabel
        )
    }

    fun eventIntroSliderScrollEvent(eventLabel: String) {
        tracker?.sendEventTracking(
                InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_POP_UP_CLICK,
                eventLabel
        )
    }

    fun eventInstantLoanPermissionStatus(eventLabel: String) {
        tracker?.sendEventTracking(
                InstantLoanEventConstants.Event.EVENT_CLICK_FINTECH_MICROSITE,
                InstantLoanEventConstants.Category.FINTECH_HOMEPAGE,
                InstantLoanEventConstants.Action.PL_POP_UP_CLICK,
                eventLabel
        )
    }
}
