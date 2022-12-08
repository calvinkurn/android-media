package com.tokopedia.feedcomponent.view.viewmodel.post.grid

import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.TagsItem
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingModel

/**
 * @author by milhamj on 07/12/18.
 */
data class GridItemModel(
    val id: String = "0",
    val text: String = "",
    val price: String = "",
    val priceOriginal: String = "",
    val redirectLink: String = "",
    val thumbnail: String = "",
    val tagsList: MutableList<TagsItem> = ArrayList(),
    val trackingList: MutableList<TrackingModel> = ArrayList(),
    val index: Int = 0
)
