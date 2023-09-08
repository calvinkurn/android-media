package com.tokopedia.topads.common.domain.model.createedit

import com.tokopedia.topads.common.view.adapter.createedit.CreateEditAdGroupTypeFactory

data class CreateEditAdGroupItemUiModel(
    val tag: CreateEditAdGroupItemTag,
    var title: String = "",
    var subtitle: String = "",
    var applink: String = "",
    var hasDivider: Boolean = false,
    val clickListener: () -> Unit,
) : CreateEditItemUiModel {
    override fun itemTag(): CreateEditAdGroupItemTag {
        return tag
    }

    override fun setItemSubtitle(subtitle: String) {
        this.subtitle = subtitle
    }

    override fun type(typeFactory: CreateEditAdGroupTypeFactory): Int {
        return typeFactory.type(this)
    }

}
