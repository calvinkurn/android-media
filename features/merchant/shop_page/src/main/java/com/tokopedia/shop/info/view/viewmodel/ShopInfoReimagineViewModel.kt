package com.tokopedia.shop.info.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.info.domain.entity.ShopRatingAndReviews
import com.tokopedia.shop.info.domain.usecase.ProductRevGetShopRatingAndTopicsUseCase
import com.tokopedia.shop.info.domain.usecase.ProductRevGetShopReviewReadingListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import javax.inject.Inject

class ShopInfoReimagineViewModel @Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    private val coroutineDispatcherProvider: CoroutineDispatchers,
    private val getShopRating: ProductRevGetShopRatingAndTopicsUseCase,
    private val getShopReview: ProductRevGetShopReviewReadingListUseCase
) : BaseViewModel(coroutineDispatcherProvider.main) {
    
    private val _shopRatingAndReview = MutableLiveData<Result<ShopRatingAndReviews>>()
    val shopRatingAndReview : LiveData<Result<ShopRatingAndReviews>>
        get() = _shopRatingAndReview
    
    fun getShopRating(shopId: String) {
        launchCatchError(
            context = coroutineDispatcherProvider.io,
            block = {

                val shopRatingParam = ProductRevGetShopRatingAndTopicsUseCase.Param(shopId)
                val shopReviewParam = ProductRevGetShopReviewReadingListUseCase.Param(
                    shopID = shopId,
                    limit = 5,
                    page = 1,
                    filterBy = "",
                    sortBy = ""
                )
                val shopRatingDeferred = async { getShopRating.execute(shopRatingParam) }
                val shopReviewDeferred = async { getShopReview.execute(shopReviewParam) }
                
                val shopRating = shopRatingDeferred.await()
                val shopReview = shopReviewDeferred.await()
                
                println(shopRating)
                println(shopReview)

                _shopRatingAndReview.postValue(Success(ShopRatingAndReviews(shopRating, shopReview)))
            },
            onError = {error ->
                _shopRatingAndReview.postValue(Fail(error))
            }
        ) 
    }
}
