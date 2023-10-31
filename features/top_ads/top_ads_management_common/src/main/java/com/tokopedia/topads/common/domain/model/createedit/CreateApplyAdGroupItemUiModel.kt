package com.tokopedia.topads.common.domain.model.createedit

import com.tokopedia.topads.common.view.adapter.createedit.CreateEditAdGroupTypeFactory

data class CreateApplyAdGroupItemUiModel(
    var title: String = "",
    val clickListener: () -> Unit
) : CreateEditItemUiModel {
    override fun type(typeFactory: CreateEditAdGroupTypeFactory): Int {
        return typeFactory.type(this)
    }
}
