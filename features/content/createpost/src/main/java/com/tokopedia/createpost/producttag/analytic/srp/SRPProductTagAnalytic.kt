package com.tokopedia.createpost.producttag.analytic.srp

import com.tokopedia.createpost.producttag.view.uimodel.tracker.SRPTrackerUiModel

/**
 * Created By : Jonathan Darwin on May 23, 2022
 */
interface SRPProductTagAnalytic {

    fun trackSRP(model: SRPTrackerUiModel)
}