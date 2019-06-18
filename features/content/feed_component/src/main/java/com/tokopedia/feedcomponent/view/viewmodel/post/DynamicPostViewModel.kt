package com.tokopedia.feedcomponent.view.viewmodel.post

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.*
import com.tokopedia.feedcomponent.data.pojo.template.Template
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory

/**
 * @author by milhamj on 28/11/18.
 */
data class DynamicPostViewModel(
        val id: Int = 0,
        val title: Title = Title(),
        val header: Header = Header(),
        val postTag: PostTag = PostTag(),
        val footer: Footer = Footer(),
        val caption: Caption = Caption(),
        var contentList: MutableList<BasePostViewModel> = ArrayList(),
        val template: Template = Template(),
        val trackingPostModel: TrackingPostModel = TrackingPostModel()

) : Visitable<DynamicFeedTypeFactory> {
    
    override fun type(typeFactory: DynamicFeedTypeFactory?): Int {
        return typeFactory!!.type(this)
    }
}