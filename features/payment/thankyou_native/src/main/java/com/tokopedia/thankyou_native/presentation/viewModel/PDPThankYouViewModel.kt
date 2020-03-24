package com.tokopedia.thankyou_native.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.thankyou_native.presentation.viewModel.state.Loaded
import com.tokopedia.thankyou_native.presentation.viewModel.state.Loading
import com.tokopedia.thankyou_native.presentation.viewModel.state.RequestDataState
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PDPThankYouViewModel @Inject constructor(
        private val getRecommendationUseCase: GetRecommendationUseCase,
        val addWishListUseCase: AddWishListUseCase,
        val removeWishListUseCase: RemoveWishListUseCase,
        val userSession: UserSessionInterface,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val titleLiveData: MutableLiveData<RequestDataState<String>> = MutableLiveData()

    val recommendationListModel = MutableLiveData<RequestDataState<List<RecommendationWidget>>>()

    fun loadRecommendationData() {
        recommendationListModel.value = Loading
        launch(Dispatchers.IO) {
            val topAdsProductDef = try {
                val data = getRecommendationUseCase
                        .createObservable(getRecommendationUseCase.getRecomParams(
                                pageNumber = 0,
                                pageName = "thankyou",
                                productIds = arrayListOf()
                        )).toBlocking()
                Loaded(Success(data.first() ?: emptyList()))
            } catch (e: Throwable) {
                Loaded(Fail(e))
            }

            withContext(Dispatchers.Main) {
                recommendationListModel.value = if ((topAdsProductDef.data as? Success)?.data != null) {
                    titleLiveData.value = Loaded(Success(data = topAdsProductDef.data.data.first().title))
                    Loaded(Success((topAdsProductDef.data as? Success)?.data!!))
                } else {
                    Loaded(Fail(RuntimeException()))
                }
            }
        }
    }

    fun addToWishlist(model: RecommendationItem, callback: ((Boolean, Throwable?) -> Unit)) {
        if (model.isTopAds) {
            /*val params = RequestParams.create()
            params.putString(TopAdsWishlishedUseCase.WISHSLIST_URL, model.wishlistUrl)
            topAdsWishlishedUseCase.execute(params, object : Subscriber<WishlistModel>() {
                override fun onCompleted() {
                }

                override fun onError(e: Throwable) {
                    callback.invoke(false, e)
                }

                override fun onNext(wishlistModel: WishlistModel) {
                    if (wishlistModel.data != null) {
                        callback.invoke(true, null)
                    }
                }
            })*/
        } else {
            addWishListUseCase.createObservable(model.productId.toString(), userSession.userId, object : WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                    callback.invoke(false, Throwable(errorMessage))
                }

                override fun onSuccessAddWishlist(productId: String?) {
                    callback.invoke(true, null)
                }

                override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                    // do nothing
                }

                override fun onSuccessRemoveWishlist(productId: String?) {
                    // do nothing
                }
            })
        }
    }


    fun removeFromWishlist(model: RecommendationItem, wishlistCallback: (((Boolean, Throwable?) -> Unit))) {
        removeWishListUseCase.createObservable(model.productId.toString(), userSession.userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                // do nothing
            }

            override fun onSuccessAddWishlist(productId: String?) {
                // do nothing
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                wishlistCallback.invoke(false, Throwable(errorMessage))
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                wishlistCallback.invoke(true, null)
            }
        })
    }


    override fun onCleared() {
        super.onCleared()
        //recommendationProductUseCase.unsubscribe()
    }


}
