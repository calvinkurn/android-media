package com.tokopedia.discovery2.data

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryVisitable
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.DiscoveryHomeFactory

class ComponentOneDataModel : DiscoveryVisitable {


    val name: String? = "Discovery Component One Data Model"

    override fun type(discoveryHomeFactory: DiscoveryHomeFactory): Int {
        return discoveryHomeFactory.type(this)
    }

}