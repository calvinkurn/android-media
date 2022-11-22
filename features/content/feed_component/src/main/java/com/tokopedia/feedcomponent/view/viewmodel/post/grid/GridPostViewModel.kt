package com.tokopedia.feedcomponent.view.viewmodel.post.grid

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel

/**
 * @author by milhamj on 07/12/18.
 */
data class GridPostViewModel(
        val itemList: MutableList<GridItemViewModel> = ArrayList(),
        val actionText: String = "",
        val actionLink: String = "",
        val totalItems: Int = 0,
        val showGridButton: Boolean = true,
        val trackingList: MutableList<TrackingViewModel> = ArrayList(),
        override var postId: String = "0",
        override var positionInFeed: Int = 0,
        val postType: String="",
        val isFollowed: Boolean = false,
        val hasVoucher: Boolean = false,
        val shopId: String="",
        val itemListFeedXProduct :List<FeedXProduct> = ArrayList(),

        ) : BasePostViewModel
