package com.tokopedia.vouchercreation.product.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.product.list.domain.model.response.*
import com.tokopedia.vouchercreation.product.list.domain.usecase.*
import com.tokopedia.vouchercreation.product.list.view.model.ProductUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddProductViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatchers,
        private val getProductListUseCase: GetProductListUseCase,
        private val getProductVariantsUseCase: GetProductVariantsUseCase,
        private val getWarehouseLocationsUseCase: GetWarehouseLocationsUseCase,
        private val getProductListMetaDataUseCase: GetProductListMetaDataUseCase,
        private val getShowCasesByIdUseCase: GetShowCasesByIdUseCase
) : BaseViewModel(dispatchers.main) {

    private var selectedProducts: MutableList<String> = mutableListOf()

    private val getProductListResultLiveData = MutableLiveData<Result<ProductListResponse>>()
    val productListResult: LiveData<Result<ProductListResponse>> get() = getProductListResultLiveData

    private val getProductVariantsResultLiveData = MutableLiveData<Result<GetProductV3Response>>()
    val getProductVariantsResult: LiveData<Result<GetProductV3Response>> get() = getProductVariantsResultLiveData

    private val getSellerLocationsResultLiveData = MutableLiveData<Result<ShopLocGetWarehouseByShopIdsResponse>>()
    val getSellerLocationsResult: LiveData<Result<ShopLocGetWarehouseByShopIdsResponse>> get() = getSellerLocationsResultLiveData

    private val getShowCasesByIdResultLiveData = MutableLiveData<Result<ShopShowcasesByShopIdResponse>>()
    val getShowCasesByIdResult: LiveData<Result<ShopShowcasesByShopIdResponse>> get() = getShowCasesByIdResultLiveData

    fun getProductList(shopId: String) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val params = GetProductListUseCase.createRequestParams(shopId)
                getProductListUseCase.setRequestParams(params = params.parameters)
                getProductListUseCase.executeOnBackground()
            }
            getProductListResultLiveData.value = Success(result)
        }, onError = {
            getProductListResultLiveData.value = Fail(it)
        })
    }

    fun getProductVariants(isVariantEmpty: Boolean, productId: String) {
        if (!isVariantEmpty) return
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val params = GetProductVariantsUseCase.createRequestParams(productId)
                getProductVariantsUseCase.setRequestParams(params = params.parameters)
                getProductVariantsUseCase.executeOnBackground()
            }
            getProductVariantsResultLiveData.value = Success(result)
        }, onError = {
            getProductVariantsResultLiveData.value = Fail(it)
        })
    }

    fun getSellerLocations(shopId: String) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val params = GetWarehouseLocationsUseCase.createRequestParams(shopId)
                getWarehouseLocationsUseCase.setRequestParams(params = params.parameters)
                getWarehouseLocationsUseCase.executeOnBackground()
            }
            getSellerLocationsResultLiveData.value = Success(result)
        }, onError = {
            getSellerLocationsResultLiveData.value = Fail(it)
        })
    }

    fun getProductsMetaData() {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
//                val params = GetCampaignListUseCase.createParams(campaignName, campaignTypeId, listTypeId, statusId)
//                getCampaignListUseCase.setRequestParams(params = params.parameters)
//                getCampaignListUseCase.executeOnBackground()
            }
//            getCampaignListResultLiveData.value = Success(result)
        }, onError = {
//            getCampaignListResultLiveData.value = Fail(it)
        })
    }

    fun getShopShowCases(shopId: String) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val params = GetShowCasesByIdUseCase.createParams(shopId)
                getShowCasesByIdUseCase.setRequestParams(params = params.parameters)
                getShowCasesByIdUseCase.executeOnBackground()
            }
            getShowCasesByIdResultLiveData.value = Success(result)
        }, onError = {
            getShowCasesByIdResultLiveData.value = Fail(it)
        })
    }

    fun mapProductDataToProductUiModel(productDataList: List<ProductData>): List<ProductUiModel> {
        return productDataList.map { productData ->
            ProductUiModel(
                    imageUrl = productData.pictures.first().urlThumbnail,
                    id = productData.id,
                    productName = productData.name,
                    sku = productData.sku,
                    price = productData.price.max.toString(),
                    soldNStock = productData.stock.toString() + " " + productData.txStats.sold
            )
        }
    }

    fun addSelectedProduct(productId: String) {
        selectedProducts.add(productId)
    }

    fun removeSelectedProduct(productId: String) {
        selectedProducts.removeFirst { it == productId }
    }
}