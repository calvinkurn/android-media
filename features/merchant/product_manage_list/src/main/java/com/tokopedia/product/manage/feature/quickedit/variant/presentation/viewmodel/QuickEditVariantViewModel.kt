package com.tokopedia.product.manage.feature.quickedit.variant.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.manage.common.coroutine.CoroutineDispatchers
import com.tokopedia.product.manage.feature.quickedit.variant.data.mapper.ProductManageVariantMapper.mapToVariantsResult
import com.tokopedia.product.manage.feature.quickedit.variant.data.mapper.ProductManageVariantMapper.updateVariant
import com.tokopedia.product.manage.feature.quickedit.variant.data.mapper.ProductManageVariantMapper.mapVariantsToEditResult
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.ViewState
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.ViewState.*
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.result.EditVariantResult
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.result.GetVariantResult
import com.tokopedia.product.manage.feature.quickedit.variant.domain.GetProductVariantUseCase
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuickEditVariantViewModel @Inject constructor(
    private val getProductVariantUseCase: GetProductVariantUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val getProductVariantsResult: LiveData<Result<GetVariantResult>>
        get() = _getProductVariantsResult

    val editVariantResult: LiveData<EditVariantResult>
        get() = _editVariantResult

    val viewState: LiveData<ViewState>
        get() = _viewState

    private val _getProductVariantsResult = MutableLiveData<Result<GetVariantResult>>()
    private val _editVariantResult = MutableLiveData<EditVariantResult>()
    private val _viewState = MutableLiveData<ViewState>()

    fun getProductVariants(productId: String) {
        showProgressBar()
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val requestParams = GetProductVariantUseCase.createRequestParams(productId)
                val response = getProductVariantUseCase.execute(requestParams)

                val variant = response.getProductV3
                mapToVariantsResult(variant)
            }
            _getProductVariantsResult.value = Success(result)
            _editVariantResult.value = mapVariantsToEditResult(productId, result)
            hideProgressBar()
        }) {
            _getProductVariantsResult.value = Fail(it)
            hideProgressBar()
        }
    }

    fun updateVariantPrice(variantId: String, price: Int) {
        _editVariantResult.value?.let {
            val editVariantResult = it.updateVariant(variantId) { variant ->
                variant.copy(price = price)
            }
            _editVariantResult.value = editVariantResult
        }
    }

    fun updateVariantStock(variantId: String, stock: Int) {
        _editVariantResult.value?.let {
            val editVariantResult = it.updateVariant(variantId) { variant ->
                variant.copy(stock = stock)
            }
            _editVariantResult.value = editVariantResult
        }
    }

    fun updateVariantStatus(variantId: String, status: ProductStatus) {
        editVariantResult.value?.let {
            val editVariantResult = it.updateVariant(variantId) { variant ->
                variant.copy(status = status)
            }
            _editVariantResult.value = editVariantResult
        }
    }

    private fun showProgressBar() {
        _viewState.value = ShowProgressBar
    }

    private fun hideProgressBar() {
        _viewState.value = HideProgressBar
    }
}