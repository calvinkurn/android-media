package com.tokopedia.feedcomponent.view.viewmodel.posttag

import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.TagsItem
import com.tokopedia.feedcomponent.view.adapter.posttag.PostTagTypeFactory

/**
 * @author by yoasfs on 2019-07-18
 */
data class ProductPostTagModel(
    var id: String = "",
    var text: String = "",
    var price: String = "",
    var type: String = "",
    var applink: String = "",
    var weblink: String = "",
    var thumbnail: String = "",
    val percentage: String = "",
    val isSelected: Boolean = false,
    var position: MutableList<Float> = ArrayList(),
    var isWishlisted: Boolean = false,
    val tags: List<TagsItem> = ArrayList(),
    val postTagItemPojo: PostTagItem = PostTagItem(),
    val rating: Int = 0,
    val needToResize: Boolean = false,
    val authorType: String = "",
    val mediaType: String = "",
    val shouldHideActionButton: Boolean = false,
    override var feedType: String = "",
    override var positionInFeed: Int = 0,
    override var postId: String = "0"

) : BasePostTagModel {
    override fun type(typeFactory: PostTagTypeFactory): Int {
        return typeFactory.type(this)
    }
}
