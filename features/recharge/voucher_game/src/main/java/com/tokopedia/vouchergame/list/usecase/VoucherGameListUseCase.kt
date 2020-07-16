package com.tokopedia.vouchergame.list.usecase

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchergame.list.data.VoucherGameListData
import com.tokopedia.vouchergame.list.data.VoucherGameOperator
import javax.inject.Inject

/**
 * @author by resakemal on 15/08/19
 */

class VoucherGameListUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<VoucherGameListData.Response>(graphqlRepository) {

    suspend fun getVoucherGameOperators(rawQuery: String, mapParam: Map<String, Any>, searchQuery: String, isForceRefresh: Boolean): Result<VoucherGameListData> {
        try {
            this.setGraphqlQuery(rawQuery)
            this.setRequestParams(mapParam)
            this.setTypeClass(VoucherGameListData.Response::class.java)
            this.setCacheStrategy(GraphqlCacheStrategy.Builder(if (isForceRefresh) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST)
                    .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 5).build())

            val voucherGameListData = this.executeOnBackground()
            if(searchQuery.isNotEmpty()){
                voucherGameListData.response.operators = voucherGameListData.response.operators.filter { it.attributes.name.contains(searchQuery, true) }
            }

            // Add product initial position for tracking
            val operatorList = voucherGameListData.response
            operatorList.operators = operatorList.operators.mapIndexed { index, item ->
                item.position = index
                return@mapIndexed item
            }

            return Success(operatorList)
        } catch (throwable: Throwable) {
            this.clearCache()
            return Fail(throwable)
        }
    }

    fun createParams(menuID: Int): Map<String,Any> {
        val params: MutableMap<String, Any> = mutableMapOf()
        params[PARAM_MENU_ID] = menuID
        return params
    }

    companion object {
        const val PARAM_MENU_ID = "menuID"
    }
}