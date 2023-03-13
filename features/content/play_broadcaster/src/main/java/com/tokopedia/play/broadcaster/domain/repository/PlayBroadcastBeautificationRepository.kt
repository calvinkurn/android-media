package com.tokopedia.play.broadcaster.domain.repository

import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationConfigUiModel
import okhttp3.ResponseBody

/**
 * Created By : Jonathan Darwin on March 13, 2023
 */
interface PlayBroadcastBeautificationRepository {

    suspend fun saveBeautificationConfig(
        authorId: String,
        authorType: String,
        beautificationConfig: BeautificationConfigUiModel,
    ): Boolean

    suspend fun downloadPresetAsset(
        url: String,
        fileName: String,
    ): Boolean
}
