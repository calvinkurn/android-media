package com.tokopedia.discovery2.data

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryVisitable
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.DiscoveryHomeFactory

class ComponentOneDataModel : DiscoveryVisitable {

    // To be Removed
    override fun type(): String {
        return ""
    }

    val name: String? = "Discovery Component One Data Model"

}