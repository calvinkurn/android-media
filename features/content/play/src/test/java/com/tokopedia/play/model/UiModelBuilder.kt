package com.tokopedia.play.model

import com.tokopedia.play.model.status.ChannelStatusBuilder
import com.tokopedia.play.model.status.ChannelStatusBuilderImpl
import com.tokopedia.play.model.tagitem.TagItemBuilder
import com.tokopedia.play.model.tagitem.TagItemBuilderImpl

class UiModelBuilder private constructor(
    channelStatusBuilder: ChannelStatusBuilder = ChannelStatusBuilderImpl(),
    tagItemBuilder: TagItemBuilder = TagItemBuilderImpl()
) : ChannelStatusBuilder by channelStatusBuilder,
        TagItemBuilder by tagItemBuilder
{

    companion object {
        fun get() = UiModelBuilder()
    }
}