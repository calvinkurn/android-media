package com.tokopedia.kol.feature.video.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedNewUseCase
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * @author by yfsx on 26/03/19.
 */
class GetVideoDetailUseCase
@Inject constructor(@ApplicationContext private val context: Context,
                    private val getDynamicFeedUseCase: GetDynamicFeedNewUseCase,
                    graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<DynamicFeedDomainModel>(graphqlRepository){

     suspend fun execute(cursor: String = "", limit: Int = 5, detailId: String = "") : DynamicFeedDomainModel{
        try {
            return  getDynamicFeedUseCase.execute(cursor, limit, detailId)
        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
    }

    companion object {
        val DETAIL_ID = "detailID"
        fun createRequestParams(userId: String, detailId: String): RequestParams {
            val requestParams = GetDynamicFeedUseCase.createRequestParams(
                    userId = userId,
                    cursor = "",
                    source = GetDynamicFeedUseCase.FeedV2Source.Detail
            )
            requestParams.putString(DETAIL_ID, detailId)
            return requestParams
        }
    }
}