package com.tokopedia.feedcomponent.view.viewmodel.recommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Title
import com.tokopedia.feedcomponent.data.pojo.template.Template
import com.tokopedia.feedcomponent.view.adapter.post.DynamicPostTypeFactory

/**
 * @author by milhamj on 20/12/18.
 */
data class FeedRecommendationViewModel (
        val title: Title = Title(),
        val cards: MutableList<RecommendationCardViewModel> = ArrayList(),
        val template: Template = Template()
): Visitable<DynamicPostTypeFactory> {
    override fun type(typeFactory: DynamicPostTypeFactory?): Int {
        return typeFactory!!.type(this)
    }
}