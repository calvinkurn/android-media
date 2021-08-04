package com.tokopedia.feedcomponent.view.viewmodel.topads

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.pojo.template.Template
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.sdk.domain.model.CpmModel

class TopadsHeadlineUiModel(var cpmModel: CpmModel? = null, var template: Template = Template(),
                            var topadsHeadLinePage: Int = 0) : Visitable<DynamicFeedTypeFactory> {

    val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(typeFactory: DynamicFeedTypeFactory): Int {
        return typeFactory.type(this)
    }
}