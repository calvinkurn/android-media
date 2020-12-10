package com.tokopedia.product.manage.common.feature.variant.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductManageAccessUseCase
import com.tokopedia.product.manage.common.feature.list.view.mapper.ProductManageAccessMapper
import com.tokopedia.product.manage.common.feature.list.view.mapper.ProductManageTickerMapper.mapToTickerList
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageTicker
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageTicker.*
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.common.feature.variant.data.mapper.ProductManageVariantMapper.mapToVariantsResult
import com.tokopedia.product.manage.common.feature.variant.data.mapper.ProductManageVariantMapper.mapVariantsToEditResult
import com.tokopedia.product.manage.common.feature.variant.data.mapper.ProductManageVariantMapper.updateVariant
import com.tokopedia.product.manage.common.feature.variant.domain.GetProductVariantUseCase
import com.tokopedia.product.manage.common.feature.variant.presentation.data.EditVariantResult
import com.tokopedia.product.manage.common.feature.variant.presentation.data.GetVariantResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuickEditVariantViewModel @Inject constructor(
        private val getProductVariantUseCase: GetProductVariantUseCase,
        private val getProductManageAccessUseCase: GetProductManageAccessUseCase,
        private val userSession: UserSessionInterface,
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

    val tickerList: LiveData<List<ProductManageTicker>>
        get() = _tickerList

    private val _getProductVariantsResult = MutableLiveData<GetVariantResult>()
    private val _editVariantResult = MutableLiveData<EditVariantResult>()
    private val _productManageAccess = MutableLiveData<ProductManageAccess>()
    private val _showProgressBar = MutableLiveData<Boolean>()
    private val _showErrorView = MutableLiveData<Boolean>()
    private val _tickerList = MutableLiveData<List<ProductManageTicker>>()

    fun getData(productId: String) {
        hideErrorView()
        showProgressBar()

        launchCatchError(block = {
            val access = getProductManageAccess()
            _productManageAccess.value = access
            getProductVariants(productId, access)
        }) {
            hideProgressBar()
            showErrorView()
        }
    }

    private fun getProductVariants(productId: String, access: ProductManageAccess) {
        hideErrorView()
        showProgressBar()
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val requestParams = GetProductVariantUseCase.createRequestParams(productId)
                val response = getProductVariantUseCase.execute(requestParams)
                val variant = response.getProductV3
                mapToVariantsResult(variant, access)
            }

            if (result.variants.isNotEmpty()) {
                _getProductVariantsResult.value = result
                setEditVariantResult(productId, result)
                getTickerList()
            } else {
                showErrorView()
            }

            hideProgressBar()
        }) {
            hideProgressBar()
            showErrorView()
        }
    }

    private suspend fun getProductManageAccess(): ProductManageAccess {
        return withContext(dispatchers.io) {
            if(userSession.isShopOwner) {
                ProductManageAccessMapper.mapProductManageOwnerAccess()
            } else {
                val response = getProductManageAccessUseCase.execute(userSession.shopId)
                ProductManageAccessMapper.mapToProductManageAccess(response)
            }
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

    fun setTickerList() {
        _editVariantResult.value?.run {
            val tickerList = _tickerList.value?.toMutableList()
            val isAllStockEmpty = isAllStockEmpty()

            if(isAllStockEmpty) {
                tickerList?.add(EmptyStockTicker)
            } else {
                tickerList?.remove(EmptyStockTicker)
            }

            _tickerList.value = tickerList
        }
    }

    private fun getTickerList() {
        val multiLocationShop = userSession.isMultiLocationShop
        val canEditStock = _productManageAccess.value?.editStock == true
        val isAllStockEmpty = _editVariantResult.value?.isAllStockEmpty() == true
        _tickerList.value = mapToTickerList(multiLocationShop, canEditStock, isAllStockEmpty)
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