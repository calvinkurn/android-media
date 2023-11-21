package com.tokopedia.tokopedianow.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.common.domain.model.GetHomeBannerV2DataResponse
import com.tokopedia.tokopedianow.common.domain.model.GetHomeBannerV2DataResponse.GetHomeBannerV2Response
import com.tokopedia.tokopedianow.home.domain.mapper.LocationParamMapper.mapLocation
import com.tokopedia.tokopedianow.home.domain.query.GetHomeBannerV2
import com.tokopedia.tokopedianow.home.domain.query.GetHomeBannerV2.PARAM_LOCATION
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetHomeBannerUseCase @Inject constructor(graphqlRepository: GraphqlRepository) {

    private val graphql by lazy { GraphqlUseCase<GetHomeBannerV2DataResponse>(graphqlRepository) }

    suspend fun execute(localCacheModel: LocalCacheModel?): GetHomeBannerV2Response {
        graphql.apply {
            setGraphqlQuery(GetHomeBannerV2)
            setTypeClass(GetHomeBannerV2DataResponse::class.java)

            setRequestParams(
                RequestParams.create().apply {
                    putString(PARAM_LOCATION, mapLocation(localCacheModel))
                }.parameters
            )

            return executeOnBackground().data
        }
    }
}
