package com.tokopedia.shareexperience.ui.listener

import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelItemModel

interface ShareExChannelListener {
    fun onChannelClicked(element: ShareExChannelItemModel)
}
