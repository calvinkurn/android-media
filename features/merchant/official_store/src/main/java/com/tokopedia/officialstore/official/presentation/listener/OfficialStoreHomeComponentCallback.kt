package com.tokopedia.officialstore.official.presentation.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.model.ChannelModel

class OfficialStoreHomeComponentCallback(): HomeComponentListener {
    override fun onChannelExpired(channelModel: ChannelModel, channelPosition: Int, visitable: Visitable<*>) {

    }
}