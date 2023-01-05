package com.tokopedia.kol.feature.postdetail.domain.interactor

import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedNewUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailUiModel
import timber.log.Timber
import javax.inject.Inject

/**
 * @author by nisie on 26/03/19.
 */
class GetPostDetailUseCase @Inject constructor(
        private val getDynamicFeedUseCase: GetDynamicFeedNewUseCase,
        graphqlRepository: GraphqlRepository
) : GraphqlUseCase<DynamicFeedDomainModel>(graphqlRepository){

    suspend fun executeForCDPRevamp(cursor: String = "", limit: Int = 5, detailId: String = "") : ContentDetailUiModel{
        try {
            val feedXData = getDynamicFeedUseCase.executeForCDP(cursor, limit, detailId)
            return ContentDetailUiModel(
                postList = feedXData.feedXHome.items,
                cursor = ""
            )
        } catch (e: Throwable) {
            Timber.e(e)
            throw e
        }
    }
}
