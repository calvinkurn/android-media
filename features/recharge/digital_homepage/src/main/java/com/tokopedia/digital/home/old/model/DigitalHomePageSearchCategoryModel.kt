package com.tokopedia.digital.home.old.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageSearchTypeFactory

class DigitalHomePageSearchCategoryModel(id: String = "",
                                         name: String = "",
                                         label: String = "",
                                         applink: String = "",
                                         icon: String = "",
                                         var searchQuery: String = "")
    : DigitalHomePageCategoryModel.Submenu(id, name, label, applink, icon), Visitable<DigitalHomePageSearchTypeFactory> {

    override fun type(typeFactory: DigitalHomePageSearchTypeFactory): Int {
        return typeFactory.type(this)
    }
}