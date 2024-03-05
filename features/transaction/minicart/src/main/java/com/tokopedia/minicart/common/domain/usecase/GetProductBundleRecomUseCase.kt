package com.tokopedia.minicart.common.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.minicart.common.domain.data.ProductBundleRecomResponse
import com.tokopedia.minicart.common.domain.query.GetProductBundleRecomQuery
import com.tokopedia.minicart.common.domain.query.GetProductBundleRecomQuery.PARAM_EXCLUDE_BUNDLE_IDS
import com.tokopedia.minicart.common.domain.query.GetProductBundleRecomQuery.PARAM_PRODUCT_IDS
import com.tokopedia.minicart.common.domain.query.GetProductBundleRecomQuery.PARAM_QUERY_PARAM
import com.tokopedia.minicart.common.domain.query.GetProductBundleRecomQuery.PARAM_WAREHOUSES
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Product Bundle Recom Now Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/TokoNow/pages/1989613339/WIP+Product+Bundling+NOW#GQL
 */
class GetProductBundleRecomUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper
) : GraphqlUseCase<ProductBundleRecomResponse>(graphqlRepository) {

    companion object {
        private const val DEFAULT_QUERY_PARAM = "type=SINGLE,MULTIPLE"
    }

    init {
        setTypeClass(ProductBundleRecomResponse::class.java)
        setGraphqlQuery(GetProductBundleRecomQuery)
    }

    suspend fun execute(
        productIds: List<String> = listOf(),
        excludeBundleIds: List<String> = listOf(),
        queryParam: String = DEFAULT_QUERY_PARAM
    ): ProductBundleRecomResponse {
        setRequestParams(
            RequestParams.create().apply {
                putObject(PARAM_WAREHOUSES, chosenAddressRequestHelper.getWarehouses())
                putObject(PARAM_PRODUCT_IDS, productIds)
                putObject(PARAM_EXCLUDE_BUNDLE_IDS, excludeBundleIds)
                putString(PARAM_QUERY_PARAM, queryParam)
            }.parameters
        )
        return executeOnBackground()
    }
}
