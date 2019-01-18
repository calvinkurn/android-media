package com.tokopedia.feedcomponent.view.viewmodel.topads

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Title
import com.tokopedia.feedcomponent.data.pojo.template.Template
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.TrackingRecommendationModel
import com.tokopedia.topads.sdk.domain.model.Data

/**
 * @author by milhamj on 08/01/19.
 */
data class TopadsShopViewModel(
        val title: Title = Title(),
        val dataList: MutableList<Data> = ArrayList(),
        val template: Template = Template(),
        val trackingList: List<TrackingRecommendationModel> = ArrayList()
): Visitable<DynamicFeedTypeFactory> {
    override fun type(typeFactory: DynamicFeedTypeFactory?): Int {
        return typeFactory!!.type(this)
    }
}