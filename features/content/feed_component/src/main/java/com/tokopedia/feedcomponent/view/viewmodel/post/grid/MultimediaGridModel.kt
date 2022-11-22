package com.tokopedia.feedcomponent.view.viewmodel.post.grid

import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.MediaItem
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingModel

/**
 * @author by yoasfs on 2019-07-01
 */
class MultimediaGridModel (
    val itemList: MutableList<GridItemModel> = ArrayList(),
    val mediaItemList: List<MediaItem> = ArrayList(),
    val actionText: String = "",
    val actionLink: String = "",
    val totalItems: Int = 0,
    val showGridButton: Boolean = true,
    val trackingList: MutableList<TrackingModel> = ArrayList(),
    override var postId: String = "0",
    override var positionInFeed: Int = 0
) : BasePostModel
