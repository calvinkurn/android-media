package com.tokopedia.feedplus.oldFeed.view.listener

import android.content.Context
import com.tokopedia.feedplus.oldFeed.view.viewmodel.feeddetail.FeedDetailProductModel

/**
 * @author by nisie on 5/18/17.
 */
interface FeedPlusDetailListener {
    fun onGoToShopDetail(activityId: String?, shopId: Int)
    fun onGoToProductDetail(feedDetailProductModel: FeedDetailProductModel, adapterPosition: Int)
    fun onBackPressed()
    fun onBottomSheetMenuClicked(
        item: FeedDetailProductModel,
        context: Context,
        shopId: String = ""
    )
    fun onAddToWishlistButtonClicked(item: FeedDetailProductModel, productPosition: Int)
    fun onAddToCartButtonClicked(item: FeedDetailProductModel)
}
