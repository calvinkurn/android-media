package com.tokopedia.shareexperience.ui.model.channel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelModel
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory

class ShareExSocialChannelUiModel(
    val socialChannel: ShareExChannelModel
): Visitable<ShareExTypeFactory> {
    override fun type(typeFactory: ShareExTypeFactory): Int {
        return typeFactory.type(this)
    }
}
