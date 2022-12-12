package com.tokopedia.play.broadcaster.shorts.ui.mapper

import com.tokopedia.content.common.model.GetCheckWhitelistResponse
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.domain.model.GetBroadcasterAuthorConfigResponse
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsConfigUiModel

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
interface PlayShortsMapper {

    fun mapAuthorList(response: GetCheckWhitelistResponse): List<ContentAccountUiModel>

    fun mapShortsConfig(response: GetBroadcasterAuthorConfigResponse): PlayShortsConfigUiModel
}
