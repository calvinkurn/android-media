package com.tokopedia.vouchergame.list.usecase

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchergame.list.data.VoucherGameListData
import javax.inject.Inject

/**
 * @author by resakemal on 15/08/19
 */

class VoucherGameListUseCase @Inject constructor(val useCase: GraphqlUseCase<VoucherGameListData.Response>) {

    suspend fun getVoucherGameList(rawQuery: String, mapParam: Map<String, Any>): Result<VoucherGameListData> {
        try {
            useCase.setGraphqlQuery(rawQuery)
            useCase.setRequestParams(mapParam)
            useCase.setTypeClass(VoucherGameListData.Response::class.java)
            useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                    .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 10).build())

            val voucherGameListData = useCase.executeOnBackground()
            return Success(voucherGameListData.response)
        } catch (throwable: Throwable) {
            useCase.clearCache()
            return Fail(throwable)
        }
    }

    suspend fun searchVoucherGame(searchQuery: String, rawQuery: String, mapParam: Map<String, Any>): Result<VoucherGameListData> {
        var response = getVoucherGameList(rawQuery, mapParam)
        if (response is Success) {
            val data = response.data
            val filteredData = data.operators.filter{ it.attributes.name.contains(searchQuery, true) }
            response = Success(VoucherGameListData(
                    componentID = data.componentID,
                    name = data.name,
                    paramName = data.paramName,
                    text = data.text,
                    operators = filteredData))
        }
        return response
    }
}