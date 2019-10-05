package com.tokopedia.officialstore.official.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapterTypeFactory

class DynamicChannelViewModel(val dynamicChannel: MutableList<Channel>) : Visitable<OfficialHomeAdapterTypeFactory> {

    override fun type(adapterTypeFactory: OfficialHomeAdapterTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }
}
