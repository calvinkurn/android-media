package com.tokopedia.vouchergame.detail.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchergame.common.util.VoucherGameDispatchersProvider
import com.tokopedia.vouchergame.detail.data.VoucherGameDetailData
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by resakemal on 16/08/19.
 */
class VoucherGameDetailViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                     private val dispatcher: VoucherGameDispatchersProvider)
    : BaseViewModel(dispatcher.Main) {

    val voucherGameProducts = MutableLiveData<Result<VoucherGameDetailData>>()

    fun getVoucherGameProducts(rawQuery: String, mapParam: Map<String, Any>) {
        launchCatchError(block = {
            val data = withContext(dispatcher.IO) {
                val graphqlRequest = GraphqlRequest(rawQuery, VoucherGameDetailData.Response::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
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

    companion object {
        const val PARAM_MENU_ID = "menuID"
        const val PARAM_OPERATOR = "operator"
    }

}