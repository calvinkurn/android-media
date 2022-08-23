package com.tokopedia.navigation.domain.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.navigation.presentation.adapter.InboxTypeFactory
import com.tokopedia.topads.sdk.domain.model.CpmModel

class TopadsHeadlineUiModel(
    var cpmModel: CpmModel? = null,
    var topadsHeadLinePage: Int = 0,
    var index: Int = 0
) : Visitable<InboxTypeFactory> {

    val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(typeFactory: InboxTypeFactory): Int {
        return typeFactory.type(this)
    }
}