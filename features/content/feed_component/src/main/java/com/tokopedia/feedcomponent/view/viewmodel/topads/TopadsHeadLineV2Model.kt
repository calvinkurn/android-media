package com.tokopedia.feedcomponent.view.viewmodel.topads

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.pojo.template.Template
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.sdk.domain.model.CpmModel

data class TopadsHeadLineV2Model(var cpmModel: CpmModel? = null, var template: Template = Template(),
                            var topadsHeadLinePage: Int = 0 , var feedXCard: FeedXCard=FeedXCard()) : Visitable<DynamicFeedTypeFactory> {

    val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(typeFactory: DynamicFeedTypeFactory): Int {
        return typeFactory.type(this)
    }
}
