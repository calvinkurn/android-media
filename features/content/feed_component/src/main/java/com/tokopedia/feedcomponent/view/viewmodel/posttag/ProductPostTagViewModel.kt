package com.tokopedia.feedcomponent.view.viewmodel.posttag

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTag
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItemTag
import com.tokopedia.feedcomponent.view.adapter.posttag.PostTagTypeFactory

/**
 * @author by yoasfs on 2019-07-18
 */
data class ProductPostTagViewModel (
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
        val tags: List<PostTagItemTag> = ArrayList(),
        val postTagItemPojo: PostTagItem = PostTagItem(),
        val rating: Int = 0,
        val needToResize: Boolean = false,
        override var feedType: String = "",
        override var positionInFeed: Int = 0,
        override var postId: Int = 0

): BasePostTagViewModel {
    override fun type(typeFactory: PostTagTypeFactory): Int {
        return typeFactory.type(this)
    }
}