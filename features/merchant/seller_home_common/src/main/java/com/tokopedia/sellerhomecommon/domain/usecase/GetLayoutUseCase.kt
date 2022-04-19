package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.gqlquery.GqlGetLayout
import com.tokopedia.sellerhomecommon.domain.mapper.LayoutMapper
import com.tokopedia.sellerhomecommon.domain.model.GetLayoutResponse
import com.tokopedia.sellerhomecommon.presentation.model.BaseWidgetUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 09/06/20
 */

class GetLayoutUseCase(
    gqlRepository: GraphqlRepository,
    mapper: LayoutMapper,
    dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetLayoutResponse, List<BaseWidgetUiModel<*>>>(
    gqlRepository, mapper, dispatchers, GqlGetLayout.QUERY, false
) {

    companion object {
        private const val KEY_SHOP_ID = "shopID"
        private const val KEY_PAGE = "page"

        fun getRequestParams(shopId: String, pageName: String): RequestParams {
            return RequestParams.create().apply {
                putLong(KEY_SHOP_ID, shopId.toLongOrZero())
                putString(KEY_PAGE, pageName)
            }
        }
    }

    override val classType: Class<GetLayoutResponse>
        get() = GetLayoutResponse::class.java

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<BaseWidgetUiModel<*>> {
        val gqlRequest = GraphqlRequest(GqlGetLayout, classType, params.parameters)
        val gqlResponse: GraphqlResponse = graphqlRepository.response(
            listOf(gqlRequest), cacheStrategy
        )

        val errors: List<GraphqlError>? = gqlResponse.getError(classType)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetLayoutResponse>()
            val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
            return mapper.mapRemoteDataToUiData(data, isFromCache)
        } else {
            throw MessageErrorException(errors.firstOrNull()?.message.orEmpty())
        }
    }
}