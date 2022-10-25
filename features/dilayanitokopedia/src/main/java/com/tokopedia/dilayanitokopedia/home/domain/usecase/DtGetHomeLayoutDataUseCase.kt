package com.tokopedia.dilayanitokopedia.home.domain.usecase

import com.tokopedia.dilayanitokopedia.home.domain.model.GetHomeLayoutResponse
import com.tokopedia.dilayanitokopedia.home.domain.model.HomeLayoutResponse
import com.tokopedia.dilayanitokopedia.home.domain.query.DtGetHomeLayoutData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.usecase.RequestParams
import timber.log.Timber
import javax.inject.Inject

/**
 * Dynamic Home Channel Query Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/HP/pages/381550603/HPB+Home+-+Mojito+Channel
 */

class DtGetHomeLayoutDataUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<GetHomeLayoutResponse>(graphqlRepository) {

    companion object {
        private const val PARAM_PAGE = "page"
        private const val PARAM_PARAM = "param"
        private const val PARAM_GROUPIDS = "groupIDs"
        private const val PARAM_CHANNELIDS = "channelIDs"
        private const val PARAM_LOCATION = "location"

        /*
        TODO - wait from BE - change from empty to dt
         */
//        private const val PARAM_VALUE_PAGE_DT = ""
        private const val PARAM_VALUE_PAGE_DT = "dt"
    }

    init {
        setGraphqlQuery(DtGetHomeLayoutData)
        setTypeClass(GetHomeLayoutResponse::class.java)
    }

    suspend fun execute(
        token: String = "",
        numOfChannel: Int = 0,
        localCacheModel: LocalCacheModel?
    ): List<HomeLayoutResponse> {
        setRequestParams(RequestParams.create()
            .apply {
                putString(PARAM_PAGE, PARAM_VALUE_PAGE_DT)
//            putString(PARAM_LOCATION, mapLocation(localCacheModel))
            }.parameters
        )

        val response = executeOnBackground().response
        response.data.forEach {
            Timber.d("respondDataa ${it.layout}")

        }
        return response.data
    }
}
