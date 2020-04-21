package com.tokopedia.product.manage.feature.quickedit.variant.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.manage.common.coroutine.CoroutineDispatchers
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.feature.quickedit.variant.data.mapper.ProductManageVariantMapper.mapToVariantsResult
import com.tokopedia.product.manage.feature.quickedit.variant.data.mapper.ProductManageVariantMapper.mapVariantsToEditResult
import com.tokopedia.product.manage.feature.quickedit.variant.data.mapper.ProductManageVariantMapper.updateVariant
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.ViewState
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.ViewState.HideErrorView
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.ViewState.HideProgressBar
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.ViewState.ShowErrorView
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.ViewState.ShowProgressBar
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.result.EditVariantResult
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.result.GetVariantResult
import com.tokopedia.product.manage.feature.quickedit.variant.domain.GetProductVariantUseCase
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuickEditVariantViewModel @Inject constructor(
    private val getProductVariantUseCase: GetProductVariantUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val getProductVariantsResult: LiveData<GetVariantResult>
        get() = _getProductVariantsResult

    val editVariantResult: LiveData<EditVariantResult>
        get() = _editVariantResult

    val viewState: LiveData<ViewState>
        get() = _viewState

    private val _getProductVariantsResult = MutableLiveData<GetVariantResult>()
    private val _editVariantResult = MutableLiveData<EditVariantResult>()
    private val _viewState = MutableLiveData<ViewState>()

    fun getProductVariants(productId: String) {
        hideErrorView()
        showProgressBar()
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val requestParams = GetProductVariantUseCase.createRequestParams(productId)
                val response = getProductVariantUseCase.execute(requestParams)

                val variant = response.getProductV3
                mapToVariantsResult(variant)
            }
            setEditVariantResult(productId, result)
            _getProductVariantsResult.value = result
            hideProgressBar()
        }) {
            hideProgressBar()
            showErrorView()
        }
    }

    fun setVariantPrice(variantId: String, price: Int) {
        updateVariant(variantId) { it.copy(price = price) }
    }

    fun setVariantStock(variantId: String, stock: Int) {
        updateVariant(variantId) { it.copy(stock = stock) }
    }

    fun setVariantStatus(variantId: String, status: ProductStatus) {
        updateVariant(variantId) { it.copy(status = status) }
    }

    private fun updateVariant(variantId: String, update: (ProductVariant) -> ProductVariant) {
        editVariantResult.value?.run {
            val editVariantResult = updateVariant(variantId) { update.invoke(it) }
            _editVariantResult.value = editVariantResult
        }
    }

    private fun setEditVariantResult(productId: String, result: GetVariantResult) {
        _editVariantResult.value = mapVariantsToEditResult(productId, result)
    }

    private fun showProgressBar() {
        _viewState.value = ShowProgressBar
    }

    private fun hideProgressBar() {
        _viewState.value = HideProgressBar
    }

    private fun showErrorView() {
        _viewState.value = ShowErrorView
    }

    private fun hideErrorView() {
        _viewState.value = HideErrorView
    }
}