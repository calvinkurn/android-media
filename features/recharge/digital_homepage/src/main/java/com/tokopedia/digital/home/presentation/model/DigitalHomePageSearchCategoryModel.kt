package com.tokopedia.digital.home.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.digital.home.model.Tracking
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageSearchTypeFactory
import com.tokopedia.digital.home.presentation.adapter.viewholder.DigitalHomePageSearchViewHolder

class DigitalHomePageSearchCategoryModel(
    id: String = "",
    name: String = "",
    label: String = "",
    applink: String = "",
    icon: String = "",
    var searchQuery: String = "",
    var typeLayout: Int = DigitalHomePageSearchViewHolder.LAYOUT,
    var subtitle: String = "",
    var trackerUser: Tracking = Tracking(),
    var trackerItem: Tracking = Tracking()
) : DigitalHomePageCategoryModel.Submenu(id, name, label, applink, icon),
    Visitable<DigitalHomePageSearchTypeFactory> {

    override fun type(typeFactory: DigitalHomePageSearchTypeFactory): Int {
        return typeFactory.type(this)
    }
}