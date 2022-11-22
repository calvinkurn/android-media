package com.tokopedia.feedcomponent.view.viewmodel.posttag

import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.view.adapter.posttag.PostTagTypeFactory

/**
 * @author by yoasfs on 2019-07-18
 */

class CtaPostTagModel(
        var id: String = "",
        var text: String = "",
        var type: String = "",
        var applink: String = "",
        var weblink: String = "",
        var position: MutableList<Float> = ArrayList(),
        val postTagItemPojo: PostTagItem = PostTagItem(),
        override var feedType: String = "",
        override var positionInFeed: Int = 0,
        override var postId: String = "0"


): BasePostTagModel {
    override fun type(typeFactory: PostTagTypeFactory): Int {
        return typeFactory.type(this)
    }
}
