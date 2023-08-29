package com.tokopedia.topads.edit.view.model.edit

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topads.edit.view.adapter.edit.EditAdGroupTypeFactory

data class EditAdGroupItemAdsPotentialUiModel(
    var tag: EditAdGroupItemTag,
    var title: String = "",
    var footer: String = "",
    var applink: String = "",
    var listWidget : MutableList<EditAdGroupItemAdsPotentialWidgetUiModel> = arrayListOf(),
    var state: EditAdGroupItemState = EditAdGroupItemState.LOADING
    
) : Visitable<EditAdGroupTypeFactory> {

    override fun type(typeFactory: EditAdGroupTypeFactory): Int {
        return typeFactory.type(this)
    }
}
