package com.tokopedia.recommendation_widget_common.widget.entrypointcard.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.widget.entrypointcard.typefactory.RecomEntryPointCardTypeFactory

data class RecomEntryPointCardUiModel(
    val productTitle: String,
    val productSubTitle: String,
    val productImageUrl: String,
    val backgroundColor: String,
    val labelState: LabelState
): Visitable<RecomEntryPointCardTypeFactory>, ImpressHolder() {
    data class LabelState(
        val iconUrl: String,
        val title: String,
        val textColor: String
    )

    override fun type(typeFactory: RecomEntryPointCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
