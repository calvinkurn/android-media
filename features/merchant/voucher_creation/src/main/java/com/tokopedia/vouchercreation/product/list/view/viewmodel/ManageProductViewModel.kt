package com.tokopedia.vouchercreation.product.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.product.list.domain.model.request.GoodsSortInput
import com.tokopedia.vouchercreation.product.list.domain.model.response.ProductListResponse
import com.tokopedia.vouchercreation.product.list.domain.model.response.Selection
import com.tokopedia.vouchercreation.product.list.domain.usecase.GetProductListUseCase
import com.tokopedia.vouchercreation.product.list.domain.usecase.GetProductVariantsUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ManageProductViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatchers,
        private val getProductListUseCase: GetProductListUseCase,
        private val getProductVariantsUseCase: GetProductVariantsUseCase
) : BaseViewModel(dispatchers.main) {

    private val getProductListResultLiveData = MutableLiveData<Result<ProductListResponse>>()
    val productListResult: LiveData<Result<ProductListResponse>> get() = getProductListResultLiveData

    fun getProductList(
            shopId: String? = null
    ) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val params = GetProductListUseCase.createRequestParams(
                        shopId = shopId
                )
                getProductListUseCase.setRequestParams(params = params.parameters)
                getProductListUseCase.executeOnBackground()
            }
            getProductListResultLiveData.value = Success(result)
        }, onError = {
            getProductListResultLiveData.value = Fail(it)
        })
    }

    private fun getVariantName(combination: List<Int>, selections: List<Selection>): String {
        val sb = StringBuilder()
        combination.forEach { index ->
            sb.append(selections[index].options[index].value)
            sb.append(" ")
        }
        return sb.toString().trim()
    }
}