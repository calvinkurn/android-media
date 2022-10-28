package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.mainnav.data.pojo.order.UohData
import com.tokopedia.homenav.mainnav.data.pojo.order.UohOrders
import com.tokopedia.homenav.mainnav.domain.model.NavProductOrder
import com.tokopedia.homenav.mainnav.domain.usecases.query.GetOrderHistoryMePageQuery
import com.tokopedia.homenav.mainnav.domain.usecases.query.GetOrderHistoryQuery
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created by Fikry on 03/11/20.
 */
class GetUohOrdersNavUseCase (
        private val graphqlUseCase: GraphqlUseCase<UohData>
): UseCase<List<NavProductOrder>>(){

    private var isMePageUsingRollenceVariant = false

    fun setIsMePageUsingRollenceVariant(isMePageUsingRollenceVariant: Boolean) {
        this.isMePageUsingRollenceVariant = isMePageUsingRollenceVariant
    }

    private fun prepareGql() {
        graphqlUseCase.setGraphqlQuery(if(isMePageUsingRollenceVariant) GetOrderHistoryMePageQuery() else GetOrderHistoryQuery())
        graphqlUseCase.setRequestParams(generateParam(NavUohListParam(verticalCategory = VERTICAL_CATEGORY)))
        graphqlUseCase.setTypeClass(UohData::class.java)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    override suspend fun executeOnBackground(): List<NavProductOrder> {
        return try {
            prepareGql()
            val responseData = Success(graphqlUseCase.executeOnBackground().uohOrders?:UohOrders())
            val navProductList = mutableListOf<NavProductOrder>()
            responseData.data.orders?.map {
                if (it.metadata?.products?.isNotEmpty() == true) {
                    val product = it.metadata.products[0]
                    val additionalProductCount = it.metadata.products.size-1
                    val statusOrder = it.status?: ""
                    val estimatedArrival =
                        if (statusOrder == IN_PROCESS || statusOrder == SENDING || statusOrder == PROCESSING) it.metadata.queryParams?.substringAfter(
                            SUBSTRING_AFTER_ESTIMATED_ARRIVAL
                        )?.substringBefore(
                            SUBSTRING_BEFORE_ESTIMATED_ARRIVAL
                        ) ?: "" else ""
                    navProductList.add(NavProductOrder(
                            statusText = it.metadata.status?.label?:"",
                            statusTextColor = it.metadata.status?.textColor?:"",
                            productNameText = product.title?:"",
                            additionalProductCount = additionalProductCount,
                            imageUrl = product.imageURL?:"",
                            id = it.orderUUID?:"",
                            applink = it.metadata.detailURL?.appURL?:"",
                            estimatedArrival = estimatedArrival
                    ))
                }
            }
            navProductList
        } catch (e: Throwable){
            listOf()
        }
    }

    private fun generateParam(param: NavUohListParam): Map<String, Any?> {
        return mapOf(PARAM_INPUT to param)
    }

    companion object{
        private const val PARAM_INPUT = "input"
        private const val VERTICAL_CATEGORY = "marketplace,tokonow,mp_pym,mp_pym_tokonow"
        private const val SUBSTRING_AFTER_ESTIMATED_ARRIVAL = "estimated_arrival_text\":\""
        private const val SUBSTRING_BEFORE_ESTIMATED_ARRIVAL = "\""
        private const val IN_PROCESS = "Dalam Proses"
        private const val PROCESSING = "diproses"
        private const val SENDING = "dikirim"
    }
}
