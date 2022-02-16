package com.tokopedia.vouchercreation.product.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.product.create.data.response.ProductId
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.list.domain.model.request.GoodsSortInput
import com.tokopedia.vouchercreation.product.list.domain.model.response.ProductListResponse
import com.tokopedia.vouchercreation.product.list.domain.model.response.Selection
import com.tokopedia.vouchercreation.product.list.domain.usecase.GetProductListUseCase
import com.tokopedia.vouchercreation.product.list.domain.usecase.GetProductVariantsUseCase
import com.tokopedia.vouchercreation.product.list.view.model.ProductUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ManageProductViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatchers,
        private val getProductListUseCase: GetProductListUseCase,
        private val getProductVariantsUseCase: GetProductVariantsUseCase
) : BaseViewModel(dispatchers.main) {

    // PRODUCT SELECTIONS
    var isSelectAllMode = true

    private var maxProductLimit = 0
    private var couponSettings: CouponSettings? = null
    private var selectedProductIds = ArrayList<ProductId>()

    private val getProductListResultLiveData = MutableLiveData<Result<ProductListResponse>>()
    val productListResult: LiveData<Result<ProductListResponse>> get() = getProductListResultLiveData

    val selectedProductListLiveData = MutableLiveData<List<ProductUiModel>>(listOf())

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

    fun updateProductUiModelsDisplayMode(isEditing:Boolean, productList: List<ProductUiModel>): List<ProductUiModel> {
        val mutableProductList = productList.toMutableList()
        mutableProductList.forEach { productUiModel ->
            productUiModel.isEditing = isEditing
        }
        return mutableProductList.toList()
    }

    private fun getVariantName(combination: List<Int>, selections: List<Selection>): String {
        val sb = StringBuilder()
        combination.forEach { index ->
            sb.append(selections[index].options[index].value)
            sb.append(" ")
        }
        return sb.toString().trim()
    }

    fun setMaxProductLimit(maxProductLimit: Int) {
        this.maxProductLimit = maxProductLimit
    }

    fun setCouponSettings(couponSettings: CouponSettings?) {
        this.couponSettings = couponSettings
    }

    fun setSelectedProductIds(selectedProductIds: ArrayList<ProductId>) {
        this.selectedProductIds = selectedProductIds
    }

    fun getMaxProductLimit(): Int {
        return maxProductLimit
    }

    fun setSetSelectedProducts(productList: List<ProductUiModel>) {
        this.selectedProductListLiveData.value = productList
    }
}