package com.tokopedia.topads.edit.view.model.edit

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topads.edit.view.adapter.edit.EditAdGroupTypeFactory

data class EditAdGroupItemUiModel(
    var title: String = "",
    var subtitle: String = "",
    var applink: String = ""
) : Visitable<EditAdGroupTypeFactory> {

    override fun type(typeFactory: EditAdGroupTypeFactory): Int {
        return typeFactory.type(this)
    }
}
