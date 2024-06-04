package com.tokopedia.feedplus.domain.mapper

import com.tokopedia.feedplus.browse.data.model.AuthorWidgetModel
import com.tokopedia.play.widget.ui.model.PlayWidgetPartnerUiModel

/**
 * Created by Jonathan Darwin on 03 May 2024
 */
internal fun PlayWidgetPartnerUiModel.toAuthorWidgetModel(contentId: String): AuthorWidgetModel {
    return AuthorWidgetModel(
        id = this.id,
        name = this.name,
        avatarUrl = this.avatarUrl,
        coverUrl = "",
        totalViewFmt = "",
        appLink = this.appLink,
        contentId = contentId,
        contentAppLink = "",
        channelType = ""
    )
}
