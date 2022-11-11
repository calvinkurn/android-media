package com.tokopedia.play.broadcaster.shorts.domain

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.ui.model.ConfigurationUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
interface PlayShortsRepository {

    suspend fun getAccountList(): List<ContentAccountUiModel>

    suspend fun getTagRecommendation(
        creationId: String,
    ): Set<PlayTagUiModel>
}
