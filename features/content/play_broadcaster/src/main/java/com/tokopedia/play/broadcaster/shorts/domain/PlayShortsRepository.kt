package com.tokopedia.play.broadcaster.shorts.domain

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.ConfigurationUiModel

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
interface PlayShortsRepository {

    suspend fun getAccountList(): List<ContentAccountUiModel>

    suspend fun getShortsConfiguration(
        authorId: String,
        authorType: String,
    ): PlayShortsConfigUiModel

    suspend fun createShorts(
        authorId: String,
        authorType: String,
    ): String
}
