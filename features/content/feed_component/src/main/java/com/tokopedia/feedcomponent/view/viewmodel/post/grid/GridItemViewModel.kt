package com.tokopedia.feedcomponent.view.viewmodel.post.grid

import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel

/**
 * @author by milhamj on 07/12/18.
 */
data class GridItemViewModel (
        val id: String = "0",
        val text: String = "",
        val price: String = "",
        val redirectLink: String = "",
        val thumbnail: String = "",
        val trackingList: MutableList<TrackingViewModel> = ArrayList()
        )