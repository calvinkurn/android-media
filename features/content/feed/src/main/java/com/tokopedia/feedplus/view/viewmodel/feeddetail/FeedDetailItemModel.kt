package com.tokopedia.feedplus.view.viewmodel.feeddetail

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.TagsItem
import com.tokopedia.feedplus.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactory

/**
 * @author by nisie on 5/18/17.
 */
class FeedDetailItemModel(var productId: Int,
                          var name: String,
                          var price: String,
                          var priceOriginal: String,
                          var imageSource: String,
                          var url: String,
                          var cashback: String,
                          var isWishlist: Boolean,
                          var tags: List<TagsItem>,
                          val rating: Double,
                          var countReview: String,
                          var shopId: String = "",
                          var activityId: String = ""
      ) : Visitable<FeedPlusDetailTypeFactory> {
    override fun type(typeFactory: FeedPlusDetailTypeFactory): Int {
        return typeFactory.type(this)
    }

}