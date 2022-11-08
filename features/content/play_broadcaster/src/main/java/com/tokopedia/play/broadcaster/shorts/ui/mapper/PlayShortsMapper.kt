package com.tokopedia.play.broadcaster.shorts.ui.mapper

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
interface PlayShortsMapper {

    fun mapAuthorList(response: WhitelistQuery): List<ContentAccountUiModel>
}
