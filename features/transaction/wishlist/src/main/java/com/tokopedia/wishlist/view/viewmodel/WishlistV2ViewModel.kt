package com.tokopedia.wishlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.data.model.WishlistV2Params
import com.tokopedia.wishlist.data.model.WishlistV2Response
import com.tokopedia.wishlist.domain.WishlistV2UseCase
import com.tokopedia.wishlist.util.WishlistV2Consts
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by fwidjaja on 15/10/21.
 */
class WishlistV2ViewModel @Inject constructor(dispatcher: CoroutineDispatchers,
                                              private val wishlistV2V2UseCase: WishlistV2UseCase,
                                              private val recommendationUseCase: GetRecommendationUseCase) : BaseViewModel(dispatcher.main) {

    private val _wishlistV2Result = MutableLiveData<Result<WishlistV2Response.Data.WishlistV2>>()
    val wishlistV2Result: LiveData<Result<WishlistV2Response.Data.WishlistV2>>
        get() = _wishlistV2Result

    private val _recommendationResult = MutableLiveData<Result<List<RecommendationWidget>>>()
    val recommendationResult: LiveData<Result<List<RecommendationWidget>>>
        get() = _recommendationResult

    fun loadWishlistV2(params: WishlistV2Params) {
        launch {
            _wishlistV2Result.value = wishlistV2V2UseCase.executeSuspend(params)
        }
    }

    fun loadRecommendationList(pageNumber: Int) {
        launch {
            try {
                val data = recommendationUseCase.getData(
                    GetRecommendationRequestParam(
                        pageNumber = pageNumber,
                        pageName = WishlistV2Consts.PAGE_NAME
                    )
                )
                _recommendationResult.value = Success(data)
            } catch (e: Exception) {
                _recommendationResult.value = Fail(e.fillInStackTrace())
            }
        }
    }
}