package com.tokopedia.shop.product.view.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.shop.product.data.model.ShopFeaturedProduct
import com.tokopedia.shop.product.domain.interactor.GetShopFeaturedProductUseCase
import com.tokopedia.shop.product.view.model.ShopProductViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopProductLimitedViewModel @Inject constructor(private val userSession: UserSessionInterface,
                                                      private val getShopFeaturedProductUseCase: GetShopFeaturedProductUseCase,
                                                      dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher){

    fun isMyShop(shopId: String) = userSession.shopId == shopId

    val featuredProductResponse = MutableLiveData<Result<List<ShopProductViewModel>>>()

    fun getFeaturedProduct(shopId: String, isForceRefresh: Boolean = false){
        getShopFeaturedProductUseCase.params = GetShopFeaturedProductUseCase.createParams(shopId.toInt())
        getShopFeaturedProductUseCase.isFromCacheFirst = !isForceRefresh

        launchCatchError( block = {
            featuredProductResponse.value = Success( withContext(Dispatchers.IO)
                {getShopFeaturedProductUseCase.executeOnBackground()}.map { it.toProductViewModel()})
        }){
            featuredProductResponse.value = Fail(it)
        }

    }

    private fun ShopFeaturedProduct.toProductViewModel(): ShopProductViewModel = ShopProductViewModel().also {
        it.id = productId.toString()
        it.name = name
        it.displayedPrice = price
        it.originalPrice = originalPrice
        it.discountPercentage = percentageAmount.toString()
        it.imageUrl = imageUri
        it.totalReview = totalReview
        if (isRated){
            it.rating = rating.toDoubleOrZero()
        }
        it.cashback = cashbackDetail.cashbackValue.toDouble()
        it.isWholesale = wholesale
        it.isPo = preorder
        it.isFreeReturn = returnable
        it.isWishList = isWishlist
        it.productUrl = uri
    }

}