package com.tokopedia.wishlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.data.model.BulkDeleteWishlistV2Response
import com.tokopedia.wishlist.data.model.DeleteWishlistV2Response
import com.tokopedia.wishlist.data.model.WishlistV2Params
import com.tokopedia.wishlist.data.model.WishlistV2Response
import com.tokopedia.wishlist.domain.BulkDeleteWishlistV2UseCase
import com.tokopedia.wishlist.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlist.domain.WishlistV2UseCase
import com.tokopedia.wishlist.util.WishlistV2Consts
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by fwidjaja on 15/10/21.
 */
class WishlistV2ViewModel @Inject constructor(dispatcher: CoroutineDispatchers,
                                              private val wishlistV2UseCase: WishlistV2UseCase,
                                              private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
                                              private val bulkDeleteWishlistV2UseCase: BulkDeleteWishlistV2UseCase,
                                              private val recommendationUseCase: GetRecommendationUseCase,
                                              private val atcUseCase: AddToCartUseCase) : BaseViewModel(dispatcher.main) {

    private val _wishlistV2Result = MutableLiveData<Result<WishlistV2Response.Data.WishlistV2>>()
    val wishlistV2Result: LiveData<Result<WishlistV2Response.Data.WishlistV2>>
        get() = _wishlistV2Result

    private val _recommendationResult = MutableLiveData<Result<List<RecommendationWidget>>>()
    val recommendationResult: LiveData<Result<List<RecommendationWidget>>>
        get() = _recommendationResult

    private val _deleteWishlistV2Result = MutableLiveData<Result<DeleteWishlistV2Response.Data.WishlistRemoveV2>>()
    val deleteWishlistV2Result: LiveData<Result<DeleteWishlistV2Response.Data.WishlistRemoveV2>>
        get() = _deleteWishlistV2Result

    private val _bulkDeleteWishlistV2Result = MutableLiveData<Result<BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2>>()
    val bulkDeleteWishlistV2Result: LiveData<Result<BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2>>
        get() = _bulkDeleteWishlistV2Result

    private val _atcResult = MutableLiveData<Result<AddToCartDataModel>>()
    val atcResult: LiveData<Result<AddToCartDataModel>>
        get() = _atcResult

    fun loadWishlistV2(params: WishlistV2Params) {
        launch {
            _wishlistV2Result.value = wishlistV2UseCase.executeSuspend(params)
        }
    }

    fun deleteWishlistV2(productId: String, userId: String) {
        launch {
            _deleteWishlistV2Result.value = deleteWishlistV2UseCase.executeSuspend(productId, userId)
        }
    }

    fun bulkDeleteWishlistV2(listProductId: List<String>, userId: String) {
        launch {
            _bulkDeleteWishlistV2Result.value = bulkDeleteWishlistV2UseCase.executeSuspend(listProductId, userId)
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

    fun doAtc(atcParams: AddToCartRequestParams) {
        launch {
            try {
                atcUseCase.setParams(atcParams)
                val result = atcUseCase.executeOnBackground()
                _atcResult.value = Success(result)
            } catch (e: Exception) {
                Timber.d(e)
                _atcResult.value = Fail(e)
            }
        }
    }
}