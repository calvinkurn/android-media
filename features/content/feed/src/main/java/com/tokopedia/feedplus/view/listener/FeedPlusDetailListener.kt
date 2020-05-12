package com.tokopedia.feedplus.view.listener

import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailItemModel

/**
 * @author by nisie on 5/18/17.
 */
interface FeedPlusDetailListener {
    fun onGoToShopDetail(activityId: String?, shopId: Int)
    fun onGoToProductDetail(feedDetailViewModel: FeedDetailItemModel, adapterPosition: Int)
    fun onBackPressed()
}