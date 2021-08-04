package com.tokopedia.common.topupbills.usecase

import com.tokopedia.common.topupbills.data.product.CatalogData
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * @author by jessica on 08/04/21
 */
class RechargeCatalogProductInputUseCase @Inject constructor(
        private val useCase: GraphqlUseCase<CatalogData.Response>
) {
    fun execute(
            params: Map<String, Any>,
            onSuccess: (CatalogData.Response) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        useCase.apply {
            setTypeClass(CatalogData.Response::class.java)
            setRequestParams(params)
            setGraphqlQuery(CommonTopupBillsGqlQuery.rechargeCatalogProductInput)
            setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                    .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * EXP_TIME).build())

            execute(onSuccess, onError)
        }
    }

    companion object {
        private const val KEY_MENU_ID = "menuID"
        private const val KEY_OPERATOR = "operator"

        fun createProductListParams(menuID: Int, operator: String): Map<String, Any> {
            return mapOf(KEY_MENU_ID to menuID, KEY_OPERATOR to operator)
        }

        const val EXP_TIME = 5
    }
}