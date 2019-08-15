package com.tokopedia.vouchergame.list.usecase

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchergame.list.data.VoucherGameListData
import javax.inject.Inject

/**
 * @author by resakemal on 15/08/19
 */

class VoucherGameListUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun getVoucherGameList(rawQuery: String, mapParam: Map<String, Any>): Result<VoucherGameListData> {
//        launchCatchError(block = {
//            val data = withContext(Dispatchers.Default) {
//                val graphqlRequest = GraphqlRequest(rawQuery, VoucherGameListData.Response::class.java, mapParam)
//                graphqlRepository.getReseponse(listOf(graphqlRequest),
//
//            }.getSuccessData<VoucherGameListData.Response>()
//
//            voucherGameList = data.response.operators
//            searchVoucherGameList.value = Success(voucherGameList)
//        }) {
//            searchVoucherGameList.value = Fail(it)
//        }

        useCase.clearRequest()

        try {
            val graphqlRequest = GraphqlRequest(rawQuery, VoucherGameListData::class.java, mapParam)
            useCase.addRequest(graphqlRequest)
            useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                    .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 10).build())
            val voucherGameListData = useCase.executeOnBackground().getSuccessData<VoucherGameListData.Response>()
            return Success(voucherGameListData.response)
        } catch (throwable: Throwable) {
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

    companion object {
        const val PARAM_MENU_ID = "menuID"
        const val PARAM_PLATFORM_ID = "platformID"
    }
}