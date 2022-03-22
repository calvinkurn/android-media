package com.tokopedia.play.model

import com.tokopedia.play.model.quickreply.QuickReplyBuilder
import com.tokopedia.play.model.quickreply.QuickReplyBuilderImpl
import com.tokopedia.play.model.status.ChannelStatusBuilder
import com.tokopedia.play.model.status.ChannelStatusBuilderImpl
import com.tokopedia.play.model.tagitem.TagItemBuilder
import com.tokopedia.play.model.tagitem.TagItemBuilderImpl

class UiModelBuilder private constructor(
    channelStatusBuilder: ChannelStatusBuilder = ChannelStatusBuilderImpl(),
    tagItemBuilder: TagItemBuilder = TagItemBuilderImpl(),
    quickReplyBuilder: QuickReplyBuilder = QuickReplyBuilderImpl()
) : ChannelStatusBuilder by channelStatusBuilder,
        TagItemBuilder by tagItemBuilder,
        QuickReplyBuilder by quickReplyBuilder
{

    companion object {
        fun get() = UiModelBuilder()
    }
}