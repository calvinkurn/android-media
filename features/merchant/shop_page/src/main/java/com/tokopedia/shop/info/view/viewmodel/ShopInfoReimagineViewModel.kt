package com.tokopedia.shop.info.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.info.domain.usecase.ProductRevGetShopRatingAndTopicsUseCase
import com.tokopedia.shop.info.domain.usecase.ProductRevGetShopReviewReadingListUseCase
import com.tokopedia.shop.info.view.model.ShopInfoUiState
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ShopInfoReimagineViewModel @Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    private val coroutineDispatcherProvider: CoroutineDispatchers,
    private val getShopRating: ProductRevGetShopRatingAndTopicsUseCase,
    private val getShopReview: ProductRevGetShopReviewReadingListUseCase
) : BaseViewModel(coroutineDispatcherProvider.main) {

    private val _uiState = MutableStateFlow(ShopInfoUiState())
    val uiState = _uiState.asStateFlow()
    
    fun getShopRating(shopId: String) {
        launchCatchError(
            context = coroutineDispatcherProvider.io,
            block = {
                _uiState.update { it.copy(isLoading = true) }
                
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
                
            
                _uiState.update { it.copy(isLoading = false, rating = shopRating, review = shopReview) }

            },
            onError = {error ->
                _uiState.update { it.copy(isLoading = false, error = error) }
            }
        ) 
    }
}
