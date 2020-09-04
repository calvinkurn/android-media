package com.tokopedia.product.manage.common.feature.variant.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.manage.common.coroutine.CoroutineDispatchers
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.common.feature.variant.data.mapper.ProductManageVariantMapper.mapToVariantsResult
import com.tokopedia.product.manage.common.feature.variant.data.mapper.ProductManageVariantMapper.mapVariantsToEditResult
import com.tokopedia.product.manage.common.feature.variant.domain.GetProductVariantUseCase
import com.tokopedia.product.manage.common.feature.variant.presentation.data.EditVariantResult
import com.tokopedia.product.manage.common.feature.variant.presentation.data.GetVariantResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuickEditVariantViewModel @Inject constructor(
        private val getProductVariantUseCase: GetProductVariantUseCase,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    val getProductVariantsResult: LiveData<GetVariantResult>
        get() = _getProductVariantsResult

    val editVariantResult: LiveData<EditVariantResult>
        get() = _editVariantResult

    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar

    val showErrorView: LiveData<Boolean>
        get() = _showErrorView

    val showStockTicker: LiveData<Boolean>
        get() = _showStockTicker

    private val _getProductVariantsResult = MutableLiveData<GetVariantResult>()
    private val _editVariantResult = MutableLiveData<EditVariantResult>()
    private val _showProgressBar = MutableLiveData<Boolean>()
    private val _showErrorView = MutableLiveData<Boolean>()
    private val _showStockTicker = MutableLiveData<Boolean>()

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

            if (result.variants.isNotEmpty()) {
                setShowStockTicker(result)
                setEditVariantResult(productId, result)
                _getProductVariantsResult.value = result
            } else {
                showErrorView()
            }

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

    fun setStockWarningTicker() {
        _editVariantResult.value?.run {
            val showTicker = _showStockTicker.value ?: false
            val isAllStockEmpty = isAllStockEmpty()

            if(showTicker != isAllStockEmpty) {
                _showStockTicker.value = isAllStockEmpty
            }
        }
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


    private fun setShowStockTicker(result: GetVariantResult) {
        val allStockEmpty = result.isAllStockEmpty()
        _showStockTicker.value = allStockEmpty
    }

    private fun showProgressBar() {
        _showProgressBar.value = true
    }

    private fun hideProgressBar() {
        _showProgressBar.value = false
    }

    private fun showErrorView() {
        _showErrorView.value = true
    }

    private fun hideErrorView() {
        _showErrorView.value = false
    }
}