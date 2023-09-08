package com.tokopedia.topads.common.domain.model.createedit

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topads.common.view.adapter.createedit.CreateEditAdGroupTypeFactory

interface CreateEditItemUiModel : Visitable<CreateEditAdGroupTypeFactory> {

    fun setItemSubtitle(subtitle: String) {}

    fun itemTag(): CreateEditAdGroupItemTag? = null
}

