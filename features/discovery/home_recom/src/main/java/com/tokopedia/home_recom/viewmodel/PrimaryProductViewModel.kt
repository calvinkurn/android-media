package com.tokopedia.home_recom.viewmodel

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_recom.domain.usecases.GetPrimaryProductUseCase
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.util.Response
import com.tokopedia.home_recom.view.dispatchers.RecommendationDispatcher
import com.tokopedia.home_recom.view.fragment.ProductInfoFragment
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
import javax.inject.Named

/**
 * A Class ViewModel For Primary Product Recommendation Page.
 *
 * @param userSessionInterface the handler of user session
 * @param addWishListUseCase use case for add wishlist
 * @param removeWishlistUseCase use case for remove wishlist
 * @param addToCartUseCase use case for add add product to cart
 * @param dispatcher the dispatcher for coroutine
 */
class PrimaryProductViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishlistUseCase: RemoveWishListUseCase,
        private val addToCartUseCase: AddToCartUseCase,
        private val getPrimaryProductUseCase: GetPrimaryProductUseCase,
        dispatcher: RecommendationDispatcher
) : BaseViewModel(dispatcher.getMainDispatcher()) {

    val productInfoDataModel : LiveData<Response<ProductInfoDataModel>> get() = _productInfoDataModel
    private val _productInfoDataModel = MutableLiveData<Response<ProductInfoDataModel>>()

    /**
     * [removeWishList] is the void for handling remove wishlist item
     * @param productId the primary product id
     * @param onSuccessRemoveWishlist the callback for handling success remove wishlist to update UI
     * @param onErrorRemoveWishList the callback for handling error removed wishlist to update UI
     */
    fun removeWishList(productId: String,
                       onSuccessRemoveWishlist: ((productId: String?) -> Unit)?,
                       onErrorRemoveWishList: ((errorMessage: String?) -> Unit)?) {
        removeWishlistUseCase.createObservable(productId,
                userSessionInterface.userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                // no op
            }

            override fun onSuccessAddWishlist(productId: String?) {
                // no op
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                onErrorRemoveWishList?.invoke(errorMessage)
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                onSuccessRemoveWishlist?.invoke(productId)
            }
        })
    }

    /**
     * [addWishList] is the void for handling adding wishlist item
     * @param productId the primary product id
     * @param onSuccessAddWishlist the callback for handling success added wishlist to update UI
     * @param onErrorAddWishList the callback for handling error added wishlist to update UI
     */
    fun addWishList(productId: String,
                    onErrorAddWishList: ((errorMessage: String?) -> Unit)?,
                    onSuccessAddWishlist: ((productId: String?) -> Unit)?) {
        addWishListUseCase.createObservable(productId,
                userSessionInterface.userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                onErrorAddWishList?.invoke(errorMessage)
            }

            override fun onSuccessAddWishlist(productId: String?) {
                onSuccessAddWishlist?.invoke(productId)
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
     * @param addTocartRequestParams the default pojo request to add cart
     * @param success the callback for handling success add to cart
     * @param error the callback for handling error add to cart
     */
    fun addToCart(addTocartRequestParams: AddToCartRequestParams,
                  success: (Map<String, Any>) -> Unit,
                  error: (Throwable) -> Unit) {
        val requestParams = RequestParams.create()
        requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addTocartRequestParams)
        addToCartUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<AddToCartDataModel>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        error(e)
                    }

                    override fun onNext(addToCartResult: AddToCartDataModel) {
                        val result = HashMap<String, Any>()

                        if (addToCartResult.status.equals(AddToCartDataModel.STATUS_OK, true) && addToCartResult.data.success == 1) {
                            result[ProductInfoFragment.STATUS] = true
                            result[ProductInfoFragment.CART_ID] = addToCartResult.data.cartId
                            result[ProductInfoFragment.MESSAGE] = addToCartResult.data.message
                        } else {
                            result[ProductInfoFragment.STATUS] = false
                            result[ProductInfoFragment.MESSAGE] = addToCartResult.errorMessage
                        }
                        success(result)
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