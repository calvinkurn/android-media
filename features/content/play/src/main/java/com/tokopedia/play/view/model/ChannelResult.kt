package com.tokopedia.play.view.model

import com.tokopedia.play.data.Channel


/**
 * Created by mzennis on 2019-12-10.
 */

class ChannelResult(val data: Channel): Result<ChannelResult>() {

    val title = Title(data.title)

    val pinnedMessage = PinnedMessage(
            data.pinnedMessage.title,
            data.pinnedMessage.message,
            data.pinnedMessage.redirectUrl,
            data.pinnedMessage.imageUrl)

    val totalView = TotalView(data.totalViews)

    val quickReply = QuickReply(data.quickReply)
}
