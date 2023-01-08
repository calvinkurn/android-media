package com.tokopedia.minicart.common.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.minicart.common.domain.data.ProductBundleRecomResponse
import com.tokopedia.minicart.common.domain.query.GetProductBundleRecomQuery.OPERATION_NAME
import com.tokopedia.minicart.common.domain.query.GetProductBundleRecomQuery.PARAM_EXCLUDE_BUNDLE_IDS
import com.tokopedia.minicart.common.domain.query.GetProductBundleRecomQuery.PARAM_PRODUCT_IDS
import com.tokopedia.minicart.common.domain.query.GetProductBundleRecomQuery.PARAM_QUERY_PARAM
import com.tokopedia.minicart.common.domain.query.GetProductBundleRecomQuery.PARAM_WAREHOUSE_ID
import com.tokopedia.minicart.common.domain.query.GetProductBundleRecomQuery.QUERY
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Product Bundle Recom Now Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/TokoNow/pages/1989613339/WIP+Product+Bundling+NOW#GQL
 */

@GqlQuery(OPERATION_NAME, QUERY)
class GetProductBundleRecomUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper
): GraphqlUseCase<ProductBundleRecomResponse>(graphqlRepository) {

    init {
        setTypeClass(ProductBundleRecomResponse::class.java)
        setGraphqlQuery(TokonowBundleWidget())
    }

    suspend fun execute(
        productIds: List<String>,
        excludeBundleIds: List<String>,
        queryParam: String = "type=SINGLE,MULTIPLE"
    ): ProductBundleRecomResponse {
        setRequestParams(RequestParams.create().apply {
            putString(PARAM_WAREHOUSE_ID, chosenAddressRequestHelper.getChosenAddress().tokonow.warehouseId)
            putObject(PARAM_PRODUCT_IDS, productIds)
            putObject(PARAM_EXCLUDE_BUNDLE_IDS, excludeBundleIds)
            putString(PARAM_QUERY_PARAM, queryParam)
        }.parameters)
        return executeOnBackground()
    }

}