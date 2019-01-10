package com.tokopedia.feedcomponent.view.viewmodel.banner


/**
 * @author by milhamj on 08/05/18.
 */

data class BannerItemViewModel(
        val activityId: Int = 0,
        val imageUrl: String = "",
        val redirectUrl: String = "",
        val trackingBannerModel: TrackingBannerModel = TrackingBannerModel()
)
