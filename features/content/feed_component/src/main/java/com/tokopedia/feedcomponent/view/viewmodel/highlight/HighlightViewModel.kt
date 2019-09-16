package com.tokopedia.feedcomponent.view.viewmodel.highlight

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Title
import com.tokopedia.feedcomponent.data.pojo.template.Template
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory

/**
 * @author by yoasfs on 2019-08-06
 */

data class HighlightViewModel (
        val postId: String = "",
        val title: Title = Title(),
        val cards: MutableList<HighlightCardViewModel> = ArrayList(),
        val template: Template = Template()
): Visitable<DynamicFeedTypeFactory> {
    override fun type(typeFactory: DynamicFeedTypeFactory?): Int {
        return typeFactory!!.type(this)
    }
}