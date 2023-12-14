package com.tokopedia.topads.common.domain.model.createedit

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topads.common.view.adapter.createedit.CreateEditAdGroupTypeFactory

data class CreateAdGroupItemUiModel(
    var tag: CreateEditAdGroupItemTag,
    var title: String = "",
    var subtitle: String = "",
    var isEditable: Boolean = false,
    var subtitleOne: String = "",
    var subtitleOneValue: String = "",
    var subtitleTwo: String = "",
    var subtitleTwoValue: String = "",
    var isManualAdBid: Boolean = false,
    var isItemClickable: Boolean = false,
    var hasDivider: Boolean = false,
    val clickListener: (tag: CreateEditAdGroupItemTag) -> Unit
) : CreateEditItemUiModel {

    override fun itemTag(): CreateEditAdGroupItemTag? {
        return tag
    }

    override fun setItemSubtitle(subtitle: String) {
        this.subtitle = subtitle
    }

    override fun type(typeFactory: CreateEditAdGroupTypeFactory): Int {
        return typeFactory.type(this)
    }
}
