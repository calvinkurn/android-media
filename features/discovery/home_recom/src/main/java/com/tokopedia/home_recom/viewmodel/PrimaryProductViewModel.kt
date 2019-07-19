package com.tokopedia.home_recom.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_recom.view.fragment.ProductInfoFragment
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class PrimaryProductViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                  private val userSessionInterface: UserSessionInterface,
                                                  private val addWishListUseCase: AddWishListUseCase,
                                                  private val removeWishlistUseCase: RemoveWishListUseCase,
                                                  private val addToCartUseCase: AddToCartUseCase,
                                                  @Named("Main")
                                                  val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

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

    fun isLoggedIn() = userSessionInterface.isLoggedIn
}