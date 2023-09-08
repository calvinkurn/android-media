package com.tokopedia.topads.common.domain.model.createedit

import com.tokopedia.topads.common.view.adapter.createedit.CreateEditAdGroupTypeFactory

data class CreateEditAdGroupItemAdsPotentialUiModel(
    var tag: CreateEditAdGroupItemTag,
    var title: String = "",
    var footer: String = "",
    var applink: String = "",
    var listWidget: MutableList<CreateEditAdGroupItemAdsPotentialWidgetUiModel> = arrayListOf(),
    var hasDivider: Boolean = false,
    var state: CreateEditAdGroupItemState = CreateEditAdGroupItemState.LOADING

) : CreateEditItemUiModel {

    override fun itemTag(): CreateEditAdGroupItemTag? {
        return tag
    }

    override fun type(typeFactory: CreateEditAdGroupTypeFactory): Int {
        return typeFactory.type(this)
    }
}
