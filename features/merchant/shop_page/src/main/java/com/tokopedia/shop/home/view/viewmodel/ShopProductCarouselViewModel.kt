package com.tokopedia.shop.home.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.home.view.model.Product
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject


class ShopProductCarouselViewModel @Inject constructor(
    private val dispatcherProvider: CoroutineDispatchers,
    private val getShopProductUseCase: GqlGetShopProductUseCase,
) : BaseViewModel(dispatcherProvider.main) {

    companion object {
        private const val FIRST_PAGE = 1
        private const val PRODUCT_COUNT_TO_FETCH = 5
    }

    private val _products = MutableLiveData<Result<List<Product>>>()
    val products: LiveData<Result<List<Product>>>
        get() = _products

    fun getShopProduct(
        sortId: Int,
        shopId: String,
        userAddress: LocalCacheModel,
    ) {
        launchCatchError(
            context = dispatcherProvider.io,
            block = {
                getShopProductUseCase.params = GqlGetShopProductUseCase.createParams(
                    shopId,
                    ShopProductFilterInput().apply {
                        etalaseMenu = ShopPageConstant.ALL_SHOWCASE_ID
                        this.page = FIRST_PAGE
                        sort = sortId
                        perPage = PRODUCT_COUNT_TO_FETCH
                        userDistrictId = userAddress.district_id
                        userCityId = userAddress.city_id
                        userLat = userAddress.lat
                        userLong = userAddress.long
                    }
                )
                val response = getShopProductUseCase.executeOnBackground()

                val products = response.data.map {  product ->
                    Product(
                        product.productId,
                        product.primaryImage.original,
                        product.name,
                        product.price.textIdr,
                        product.campaign.originalPriceFmt,
                        product.campaign.discountedPercentage,
                        product.stats.averageRating,
                        1
                    )
                }
                _products.postValue(Success(products))
            } ,
            onError = { throwable ->
                _products.postValue(Fail(throwable))
            }
        )
    }
}
