package com.tokopedia.feedcomponent.view.viewmodel.banner

import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.kotlin.model.ImpressHolder


/**
 * @author by milhamj on 08/05/18.
 */

data class BannerItemViewModel(
        val activityId: Int = 0,
        val imageUrl: String = "",
        val redirectUrl: String = "",
        val trackingBannerModel: TrackingBannerModel = TrackingBannerModel(),
        val tracking: MutableList<TrackingViewModel> = ArrayList(),
        val impressHolder: ImpressHolder = ImpressHolder()
)
