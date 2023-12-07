package com.tokopedia.buy_more_get_more.olp.domain.entity

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buy_more_get_more.olp.domain.entity.enum.Status
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.OlpAdapterTypeFactory

data class EmptyStateUiModel(
    val status: Status = Status.SUCCESS,
    val title: String = "",
    val description: String = "",
    val imageUrl: String = ""
) : Visitable<OlpAdapterTypeFactory> {
    override fun type(typeFactory: OlpAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
