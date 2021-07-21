package com.tokopedia.vouchergame.detail.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchergame.detail.data.VoucherGameDetailData
import com.tokopedia.vouchergame.list.data.VoucherGameOperator
import com.tokopedia.vouchergame.list.usecase.VoucherGameListUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by resakemal on 16/08/19.
 */
class VoucherGameDetailViewModel @Inject constructor(private val voucherGameUseCase: VoucherGameListUseCase,
                                                     private val graphqlRepository: GraphqlRepository,
                                                     private val dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main) {

    val voucherGameOperatorDetails = MutableLiveData<Result<VoucherGameOperator>>()
    val voucherGameProducts = MutableLiveData<Result<VoucherGameDetailData>>()

    // get voucher game operator detail when it is not passed via Intent from category page
    fun getVoucherGameOperators(rawQuery: String, mapParam: Map<String, Any>, isForceRefresh: Boolean = false, operatorId: String) {
        launch {
            val response = voucherGameUseCase.getVoucherGameOperators(rawQuery, mapParam, "", isForceRefresh)

            when (response) {
                is Success -> {
                    val data = response.data.operators.firstOrNull { it.id.toString() == operatorId }
                    if (data != null) {
                        voucherGameOperatorDetails.value = Success(data)
                    } else {
                        voucherGameOperatorDetails.value = Fail(MessageErrorException(VOUCHER_NOT_FOUND_ERROR))
                    }
                }
                is Fail -> {
                    voucherGameOperatorDetails.value = response
                }
            }
        }
    }

    fun getVoucherGameProducts(rawQuery: String, mapParam: Map<String, Any>) {
        launchCatchError(block = {
            val data = withContext(dispatcher.io) {
                val graphqlRequest = GraphqlRequest(rawQuery, VoucherGameDetailData.Response::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest), GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                        .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * VG_CACHE_DURATION_IN_MINS).build())
            }.getSuccessData<VoucherGameDetailData.Response>().response

            // Add product initial position for tracking
            var productCount = 0
            data.product.dataCollections = data.product.dataCollections.map {
                it.products = it.products.mapIndexed { index, item ->
                    item.position = index + productCount
                    return@mapIndexed item
                }
                productCount += it.products.size
                return@map it
            }

            voucherGameProducts.value = Success(data)
        }) {
            voucherGameProducts.value = Fail(it)
        }
    }

    fun createParams(menuID: Int, operator: String): Map<String, Any> {
        val params: MutableMap<String, Any> = mutableMapOf()
        params[PARAM_MENU_ID] = menuID
        params[PARAM_OPERATOR] = operator
        return params
    }

    fun createMenuDetailParams(menuID: Int): Map<String, Any> {
        val params: MutableMap<String, Any> = mutableMapOf()
        params[PARAM_MENU_ID] = menuID
        return params
    }

    companion object {
        const val PARAM_MENU_ID = "menuID"
        const val PARAM_OPERATOR = "operator"
        const val VG_CACHE_DURATION_IN_MINS = 5

        const val VOUCHER_NOT_FOUND_ERROR = "VOUCHER_NOT_FOUND_ERROR"
    }

}