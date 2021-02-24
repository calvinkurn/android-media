package com.tokopedia.product.manage.common.feature.variant.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductManageAccessUseCase
import com.tokopedia.product.manage.common.feature.list.view.mapper.ProductManageAccessMapper
import com.tokopedia.product.manage.common.feature.list.view.mapper.ProductManageTickerMapper.mapToTickerList
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageTicker
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.common.feature.variant.data.mapper.ProductManageVariantMapper.mapToVariantsResult
import com.tokopedia.product.manage.common.feature.variant.data.mapper.ProductManageVariantMapper.mapVariantsToEditResult
import com.tokopedia.product.manage.common.feature.variant.data.mapper.ProductManageVariantMapper.updateVariant
import com.tokopedia.product.manage.common.feature.variant.data.mapper.ProductManageVariantMapper.setEditStockAndStatus
import com.tokopedia.product.manage.common.feature.variant.domain.GetProductVariantUseCase
import com.tokopedia.product.manage.common.feature.variant.presentation.data.EditVariantResult
import com.tokopedia.product.manage.common.feature.variant.presentation.data.GetVariantResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.shop.common.domain.interactor.GetAdminInfoShopLocationUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuickEditVariantViewModel @Inject constructor(
        private val getProductVariantUseCase: GetProductVariantUseCase,
        private val getProductManageAccessUseCase: GetProductManageAccessUseCase,
        private val getAdminInfoShopLocationUseCase: GetAdminInfoShopLocationUseCase,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    val getProductVariantsResult: LiveData<GetVariantResult>
        get() = _getProductVariantsResult

    val onClickSaveButton: LiveData<EditVariantResult>
        get() = _onClickSaveButton

    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar

    val showErrorView: LiveData<Boolean>
        get() = _showErrorView

    val showSaveBtn: LiveData<Boolean>
        get() = _showSaveBtn

    val tickerList: LiveData<List<ProductManageTicker>>
        get() = _tickerList

    val showStockInfo: LiveData<Boolean>
        get() = _showStockInfo

    private val _getProductVariantsResult = MutableLiveData<GetVariantResult>()
    private val _onClickSaveButton = MutableLiveData<EditVariantResult>()
    private val _productManageAccess = MutableLiveData<ProductManageAccess>()
    private val _showProgressBar = MutableLiveData<Boolean>()
    private val _showErrorView = MutableLiveData<Boolean>()
    private val _showSaveBtn = MutableLiveData<Boolean>()
    private val _tickerList = MutableLiveData<List<ProductManageTicker>>()
    private val _showStockInfo = MutableLiveData<Boolean>()

    private var warehouseId: String? = null
    private var editVariantResult: EditVariantResult? = null

    fun getData(productId: String) {
        hideErrorView()
        showProgressBar()
        setEmptyTicker()

        launchCatchError(block = {
            val access = getProductManageAccess()
            _productManageAccess.value = access
            getProductVariants(productId, access)
        }) {
            setEmptyTicker()
            hideProgressBar()
            showErrorView()
        }
    }

    private fun getProductVariants(productId: String, access: ProductManageAccess) {
        hideErrorView()
        showProgressBar()
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val warehouseId = getWarehouseId(userSession.shopId)
                val requestParams = GetProductVariantUseCase.createRequestParams(productId, warehouseId = warehouseId)
                val response = getProductVariantUseCase.execute(requestParams)
                val variant = response.getProductV3
                mapToVariantsResult(variant, access)
            }
            val variants = result.variants
            val variantNotEmpty = variants.isNotEmpty()
            setShowSaveButton(access, variantNotEmpty)

            if (variantNotEmpty) {
                _getProductVariantsResult.value = result
                setEditVariantResult(productId, result)
                getTickerList()
            } else {
                setEmptyTicker()
                showErrorView()
            }

            hideProgressBar()
        }) {
            setEmptyTicker()
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

    private fun setShowSaveButton(access: ProductManageAccess, variantNotEmpty: Boolean) {
        val hasEditStockAccess = access.editStock
        val hasEditProductAccess = access.editProduct
        val shouldShow = (hasEditStockAccess || hasEditProductAccess) && variantNotEmpty

        _showSaveBtn.value = shouldShow
    }

    fun setVariantPrice(variantId: String, price: Int) {
        updateVariant(variantId) { it.copy(price = price) }
    }

    fun setVariantStock(variantId: String, stock: Int) {
        updateVariant(variantId) {
            it.copy(stock = stock)
        }
        setShowStockInfo()
    }

    fun setVariantStatus(variantId: String, status: ProductStatus) {
        updateVariant(variantId) { it.copy(status = status) }
    }

    fun getTickerList() {
        val multiLocationShop = userSession.isMultiLocationShop
        val canEditStock = _productManageAccess.value?.editStock == true
        val isAllStockEmpty = editVariantResult?.isAllStockEmpty() == true
        _tickerList.value = mapToTickerList(multiLocationShop, canEditStock, isAllStockEmpty)
    }

    fun saveVariants() {
        editVariantResult?.let {
            val variantList = _getProductVariantsResult.value?.variants.orEmpty()
            _onClickSaveButton.value = it.setEditStockAndStatus(variantList)
        }
    }

    private suspend fun getWarehouseId(shopId: String): String? {
        return if(warehouseId == null) {
            val shopLocation = getAdminInfoShopLocationUseCase.execute(shopId.toIntOrZero())
            warehouseId = shopLocation.firstOrNull { it.isMainLocation() }?.locationId.toString()
            warehouseId
        } else {
            warehouseId
        }
    }

    private fun setEmptyTicker() {
        _tickerList.value = emptyList()
    }

    private fun setShowStockInfo() {
        val isAllStockEmpty = editVariantResult?.isAllStockEmpty() == true
        val showStockInfo = _showStockInfo.value
        val shouldShow = !isAllStockEmpty

        if(showStockInfo != shouldShow) {
            _showStockInfo.value = shouldShow
        }
    }

    private fun updateVariant(variantId: String, update: (ProductVariant) -> ProductVariant) {
        editVariantResult = editVariantResult?.updateVariant(variantId) { update.invoke(it) }
    }

    private fun setEditVariantResult(productId: String, result: GetVariantResult) {
        editVariantResult = mapVariantsToEditResult(productId, result)
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