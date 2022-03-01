package com.tokopedia.pdpsimulation.paylater.domain.model

import com.tokopedia.pdpsimulation.paylater.presentation.adapter.PayLaterAdapterFactory

data class SeeMoreOptionsUiModel(
        val remainingItems: List<Detail>
) : BasePayLaterWidgetUiModel {
    override fun type(typeFactory: PayLaterAdapterFactory) = typeFactory.type(this)

}