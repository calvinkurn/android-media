package com.tokopedia.feedplus.detail.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.FeedXHeaderRequestFields
import com.tokopedia.content.common.usecase.FeedXHeaderUseCase
import com.tokopedia.feedplus.browse.data.model.HeaderDetailModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by meyta.taliti on 07/09/23.
 */
class FeedDetailRepositoryImpl @Inject constructor(
    private val feedXHeaderUseCase: FeedXHeaderUseCase,
    private val dispatchers: CoroutineDispatchers
) : FeedDetailRepository {

    override suspend fun getHeader(source: String): HeaderDetailModel {
        return withContext(dispatchers.io) {
            try {
                feedXHeaderUseCase.setRequestParams(
                    FeedXHeaderUseCase.createParam(
                        fields = listOf(FeedXHeaderRequestFields.DETAIL.value),
                        sources = listOf(
                            mapOf(
                                "sourcesRequestType" to "cdp-pagename",
                                "sourcesRequestID" to source
                            )
                        )
                    )
                )
                val response = feedXHeaderUseCase.executeOnBackground()
                HeaderDetailModel.create(response.feedXHeaderData.data.detail)
            } catch (_: Throwable) {
                HeaderDetailModel.DEFAULT
            }
        }
    }
}
