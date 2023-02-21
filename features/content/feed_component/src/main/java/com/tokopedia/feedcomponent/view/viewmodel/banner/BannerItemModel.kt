package com.tokopedia.feedcomponent.view.viewmodel.banner

import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingModel
import com.tokopedia.kotlin.model.ImpressHolder


/**
 * @author by milhamj on 08/05/18.
 */

data class BannerItemModel(
    val activityId: String = "",
    val imageUrl: String = "",
    val redirectUrl: String = "",
    val trackingBannerModel: TrackingBannerModel = TrackingBannerModel(),
    val tracking: MutableList<TrackingModel> = ArrayList(),
    val impressHolder: ImpressHolder = ImpressHolder()
)
