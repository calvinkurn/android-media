package com.tokopedia.play.broadcaster.data.datastore

import com.tokopedia.play.broadcaster.domain.usecase.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.play.broadcaster.ui.model.title.PlayTitleUiModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 29/03/21
 */
class TitleDataStoreImpl @Inject constructor(
        private val dispatcher: CoroutineDispatcherProvider,
        private val updateChannelUseCase: PlayBroadcastUpdateChannelUseCase,
        private val userSession: UserSessionInterface,
) : TitleDataStore {

    private var mTitle: PlayTitleUiModel = PlayTitleUiModel.NoTitle

    override fun getTitle(): PlayTitleUiModel {
        return mTitle
    }

    override fun setTitle(title: String) {
        mTitle = PlayTitleUiModel.HasTitle(title)
    }

    override suspend fun uploadTitle(channelId: String): NetworkResult<Unit> {
        return try {
            uploadTitleToServer(channelId)
            NetworkResult.Success(Unit)
        } catch (e: Throwable) {
            NetworkResult.Fail(e)
        }
    }

    private suspend fun uploadTitleToServer(channelId: String) = withContext(dispatcher.io) {
        val theTitle = when (val title = mTitle) {
            PlayTitleUiModel.NoTitle -> ""
            is PlayTitleUiModel.HasTitle -> title.title
        }

        updateChannelUseCase.apply {
            setQueryParams(
                    PlayBroadcastUpdateChannelUseCase.createUpdateTitleRequest(
                            channelId = channelId,
                            authorId = userSession.shopId,
                            title = theTitle
                    )
            )
        }
        return@withContext updateChannelUseCase.executeOnBackground()
    }
}