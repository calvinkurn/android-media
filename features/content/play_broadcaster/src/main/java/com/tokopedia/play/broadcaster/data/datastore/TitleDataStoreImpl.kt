package com.tokopedia.play.broadcaster.data.datastore

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.domain.usecase.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.play.broadcaster.ui.model.title.PlayTitleUiModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 29/03/21
 */
class TitleDataStoreImpl @Inject constructor(
        private val dispatcher: CoroutineDispatchers,
        private val updateChannelUseCase: PlayBroadcastUpdateChannelUseCase,
        private val userSession: UserSessionInterface,
) : TitleDataStore {

    private val _observableTitle: MutableStateFlow<PlayTitleUiModel> = MutableStateFlow(PlayTitleUiModel.NoTitle)

    private val mTitle: PlayTitleUiModel
        get() = _observableTitle.value

    override fun getObservableTitle(): Flow<PlayTitleUiModel> {
        return _observableTitle
    }

    override fun getTitle(): PlayTitleUiModel {
        return mTitle
    }

    override fun setTitle(title: String) {
        _observableTitle.value = PlayTitleUiModel.HasTitle(title)
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