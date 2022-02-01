package com.tokopedia.pdpsimulation.paylater.domain.model

import com.tokopedia.pdpsimulation.paylater.presentation.adapter.PayLaterAdapterFactory

object SupervisorUiModel: BasePayLaterWidgetUiModel {
    override fun type(typeFactory: PayLaterAdapterFactory) = typeFactory.type(this)
}