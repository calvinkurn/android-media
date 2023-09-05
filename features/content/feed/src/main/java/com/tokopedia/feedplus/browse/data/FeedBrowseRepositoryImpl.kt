package com.tokopedia.feedplus.browse.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.FeedXHeaderRequestFields
import com.tokopedia.content.common.usecase.FeedXHeaderUseCase
import com.tokopedia.content.common.usecase.GetPlayWidgetSlotUseCase
import com.tokopedia.feedplus.browse.presentation.model.ChannelUiState
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.domain.usecase.FeedXHomeUseCase
import com.tokopedia.play.widget.util.PlayWidgetConnectionUtil
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseRepositoryImpl @Inject constructor(
    private val feedXHeaderUseCase: FeedXHeaderUseCase,
    private val feedXHomeUseCase: FeedXHomeUseCase,
    private val playWidgetSlotUseCase: GetPlayWidgetSlotUseCase,
    private val mapper: FeedBrowseMapper,
    private val connectionUtil: PlayWidgetConnectionUtil,
    private val dispatchers: CoroutineDispatchers
) : FeedBrowseRepository {

    override suspend fun getTitle(): String {
        return withContext(dispatchers.io) {
            try {
                feedXHeaderUseCase.setRequestParams(
                    FeedXHeaderUseCase.createParam(
                        listOf(
                            FeedXHeaderRequestFields.BROWSE.value
                        )
                    )
                )
                val response = feedXHeaderUseCase.executeOnBackground()
                mapper.mapTitle(response)
            } catch (err: Throwable) {
                ""
            }
        }
    }

    override suspend fun getSlots(): List<FeedBrowseUiModel> {
        return withContext(dispatchers.io) {
            val response = feedXHomeUseCase(
                feedXHomeUseCase.createParams(source = "browse")
            )
            mapper.mapSlots(response).ifEmpty {
                throw IllegalStateException("no slot available")
            }
        }
    }

    override suspend fun getWidget(extraParams: Map<String, Any>): FeedBrowseItemUiModel {
        val isWifi = connectionUtil.isEligibleForHeavyDataUsage()
        val finalExtraParams = extraParams.toMutableMap().apply {
            put(GetPlayWidgetSlotUseCase.KEY_WIFI, isWifi)
        }
        return withContext(dispatchers.io) {
            try {
                playWidgetSlotUseCase.setRequestParams(
                    mapOf(
                        GetPlayWidgetSlotUseCase.KEY_REQ to finalExtraParams
                    )
                )
                val response = playWidgetSlotUseCase.executeOnBackground()
                mapper.mapWidget(response)
            } catch (err: Throwable) {
                ChannelUiState.Error(err, extraParams = extraParams)
            }
        }
    }
}
