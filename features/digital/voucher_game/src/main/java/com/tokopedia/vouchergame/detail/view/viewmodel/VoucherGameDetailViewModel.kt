package com.tokopedia.vouchergame.detail.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchergame.detail.data.VoucherGameDetailData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by resakemal on 16/08/19.
 */
class VoucherGameDetailViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                     val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val voucherGameProducts = MutableLiveData<Result<VoucherGameDetailData>>()

    fun getVoucherGameProducts(rawQuery: String, mapParam: Map<String, Any>) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, VoucherGameDetailData.Response::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<VoucherGameDetailData.Response>()

            voucherGameProducts.value = Success(data.response)
        }) {
            voucherGameProducts.value = Fail(it)
        }
    }

    fun createParams(menuID: Int, operator: String): Map<String, Any> {
        val params: MutableMap<String, Any> = mutableMapOf()
        params.put(PARAM_MENU_ID, menuID)
        params.put(PARAM_OPERATOR, operator)
//        params.put(PARAM_MENU_ID, 10)
//        params.put(PARAM_OPERATOR, "472")
        return params
    }

    companion object {
        const val PARAM_MENU_ID = "menuID"
        const val PARAM_OPERATOR = "operator"
    }

}