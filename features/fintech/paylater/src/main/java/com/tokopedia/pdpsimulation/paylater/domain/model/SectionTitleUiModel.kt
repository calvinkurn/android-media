package com.tokopedia.pdpsimulation.paylater.domain.model

import com.tokopedia.pdpsimulation.paylater.presentation.adapter.PayLaterAdapterFactory

data class SectionTitleUiModel(
        val titleText: String?
) : BasePayLaterWidgetUiModel {
    override fun type(typeFactory: PayLaterAdapterFactory) = typeFactory.type(this)
}