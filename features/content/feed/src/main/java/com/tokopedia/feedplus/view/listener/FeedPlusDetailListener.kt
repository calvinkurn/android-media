package com.tokopedia.feedplus.view.listener

import com.tokopedia.feedplus.view.adapter.viewholder.feeddetail.ProductFeedDetailViewModelNew

/**
 * @author by nisie on 5/18/17.
 */
interface FeedPlusDetailListener {
    fun onGoToShopDetail(activityId: String?, shopId: Int)
    fun onGoToProductDetail(feedDetailViewModel: ProductFeedDetailViewModelNew, adapterPosition: Int)
    fun onBackPressed()
}