package com.tokopedia.home_recom.viewmodel

import android.annotation.SuppressLint
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.home_recom.domain.usecases.GetPrimaryProductUseCase
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.util.Response
import com.tokopedia.home_recom.view.dispatchers.RecommendationDispatcher
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * A Class ViewModel For Primary Product Recommendation Page.
 *
 * @param userSessionInterface the handler of user session
 * @param addWishListUseCase use case for add wishlist
 * @param removeWishlistUseCase use case for remove wishlist
 * @param addToCartUseCase use case for add add product to cart
 * @param dispatcher the dispatcher for coroutine
 */
@SuppressLint("SyntheticAccessor")
class PrimaryProductViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishlistUseCase: RemoveWishListUseCase,
        private val addToCartUseCase: AddToCartUseCase,
        private val getPrimaryProductUseCase: GetPrimaryProductUseCase,
        private val dispatcher: RecommendationDispatcher
) : BaseViewModel(dispatcher.getMainDispatcher()) {

    val productInfoDataModel : LiveData<Response<ProductInfoDataModel>> get() = _productInfoDataModel
    private val _productInfoDataModel = MutableLiveData<Response<ProductInfoDataModel>>()

    val addToCartLiveData : LiveData<Response<AddToCartDataModel>> get() = _addToCartLiveData
    private val _addToCartLiveData = MutableLiveData<Response<AddToCartDataModel>>()

    val buyNowLiveData : LiveData<Response<AddToCartDataModel>> get() = _buyNowLiveData
    private val _buyNowLiveData = MutableLiveData<Response<AddToCartDataModel>>()

    val addWishlistLiveData : LiveData<Response<String>> get() = _addWishlistLiveData
    private val _addWishlistLiveData = MutableLiveData<Response<String>>()

    val removeWishlistLiveData : LiveData<Response<String>> get() = _removeWishlistLiveData
    private val _removeWishlistLiveData = MutableLiveData<Response<String>>()

    /**
     * [removeWishList] is the void for handling remove wishlist item
     * @param productId the primary product id
     */
    fun removeWishList(productId: String) {
        removeWishlistUseCase.createObservable(productId,
                userSessionInterface.userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                // no op
            }

            override fun onSuccessAddWishlist(productId: String?) {
                // no op
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                _removeWishlistLiveData.postValue(Response.error(errorMessage))
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                _removeWishlistLiveData.postValue(Response.success(productId))
            }
        })
    }

    /**
     * [addWishList] is the void for handling adding wishlist item
     * @param productId the primary product id
     */
    fun addWishList(productId: String) {
        addWishListUseCase.createObservable(productId,
                userSessionInterface.userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                _addWishlistLiveData.postValue(Response.error(errorMessage))
            }

            override fun onSuccessAddWishlist(productId: String?) {
                _addWishlistLiveData.postValue(Response.success(productId))
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                // no op
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                // no op
            }
        })
    }

    /**
     * [addToCart] is the void for handling adding add to cart
     * @param addToCartRequestParams the default pojo request to add cart
     */
    fun addToCart(addToCartRequestParams: AddToCartRequestParams) {
        val requestParams = RequestParams.create()
        requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addToCartRequestParams)
        addToCartUseCase.createObservable(requestParams)
                .subscribeOn(dispatcher.getSchedulerIO())
                .observeOn(dispatcher.getSchedulerMain())
                .subscribe(object : Subscriber<AddToCartDataModel>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        _addToCartLiveData.postValue(Response.error(e.message))
                    }

                    override fun onNext(addToCartResult: AddToCartDataModel) {
                        if (addToCartResult.status.equals(AddToCartDataModel.STATUS_OK, true) && addToCartResult.data.success == 1) {
                            _addToCartLiveData.postValue(Response.success(addToCartResult))
                        } else {
                            _addToCartLiveData.postValue(Response.error(addToCartResult.errorMessage.firstOrNull()))
                        }
                    }
                })
    }

    /**
     * [buyNow] is the void for handling adding add to cart
     * @param addToCartRequestParams the default pojo request to add cart
     */
    fun buyNow(addToCartRequestParams: AddToCartRequestParams) {
        val requestParams = RequestParams.create()
        requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addToCartRequestParams)
        addToCartUseCase.createObservable(requestParams)
                .subscribeOn(dispatcher.getSchedulerIO())
                .observeOn(dispatcher.getSchedulerMain())
                .subscribe(object : Subscriber<AddToCartDataModel>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        _buyNowLiveData.postValue(Response.error(e.message))
                    }

                    override fun onNext(addToCartResult: AddToCartDataModel) {
                        if (addToCartResult.status.equals(AddToCartDataModel.STATUS_OK, true) && addToCartResult.data.success == 1) {
                            _buyNowLiveData.postValue(Response.success(addToCartResult))
                        } else {
                            _buyNowLiveData.postValue(Response.error(addToCartResult.errorMessage.firstOrNull()))
                        }
                    }
                })
    }

    /**
     * [isLoggedIn] is the function get user session is login or not login
     */
    fun isLoggedIn() = userSessionInterface.isLoggedIn

    /**
     * [getPrimaryProduct] is the void for get detail primary product data from network
     * @param productId product Id from deeplink
     */
    fun getPrimaryProduct(productId: String, queryParam: String) {
        launchCatchError(block = {
            _productInfoDataModel.value = Response.loading()
            getPrimaryProductUseCase.setParameter(productId.toInt(), queryParam)
            val productRecommendationEntity = getPrimaryProductUseCase.executeOnBackground().productRecommendationProductDetail

            if(productRecommendationEntity.data[0].recommendation.isNotEmpty()){
                val productDetailResponse = productRecommendationEntity.data[0].recommendation[0]
                _productInfoDataModel.value = Response.success(ProductInfoDataModel(productDetailResponse))
            }else{
                _productInfoDataModel.value = Response.empty()
            }
        }) {
            if (!TextUtils.isEmpty(it.message)){
                _productInfoDataModel.value = Response.error(it.message ?: "")
            }
        }
    }
}