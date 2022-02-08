package com.tokopedia.kol.feature.video.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedNewUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import timber.log.Timber
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
            Timber.e(e)
            throw e
        }
    }

    companion object {
        val DETAIL_ID = "detailID"
    }
}