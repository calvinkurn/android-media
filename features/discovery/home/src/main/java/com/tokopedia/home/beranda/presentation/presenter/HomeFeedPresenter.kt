package com.tokopedia.home.beranda.presentation.presenter

import android.support.design.widget.Snackbar
import android.view.View
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.home.beranda.domain.interactor.GetHomeFeedUseCase
import com.tokopedia.home.beranda.presentation.view.subscriber.GetHomeFeedsSubscriber
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by Lukas on 2019-07-16
 */
open class HomeFeedPresenter @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val getHomeFeedUseCase: GetHomeFeedUseCase,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase
) : BaseDaggerPresenter<HomeFeedContract.View>(), HomeFeedContract.Presenter{

    fun loadData(recomId: Int, count: Int, page: Int) {
        getHomeFeedUseCase.execute(
                getHomeFeedUseCase.getHomeFeedParam(recomId, count, page),
                GetHomeFeedsSubscriber(view))
    }

    fun addWishlist(model: HomeFeedViewModel, wishlistCallback: (((Boolean, Throwable?) -> Unit))){
        if(model.isTopAds){
            val params = RequestParams.create()
            params.putString(TopAdsWishlishedUseCase.WISHSLIST_URL, model.wishlistUrl)
            topAdsWishlishedUseCase.execute(params, object : Subscriber<WishlistModel>() {
                override fun onCompleted() {
                }

                override fun onError(e: Throwable) {
                    wishlistCallback.invoke(false, e)
                }

                override fun onNext(wishlistModel: WishlistModel) {
                    if (wishlistModel.data != null) {
                        wishlistCallback.invoke(true, null)
                    }
                }
            })
        } else {
            addWishListUseCase.createObservable(model.productId, userSessionInterface.userId, object: WishListActionListener{
                override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                    wishlistCallback.invoke(false, Throwable(errorMessage))
                }

                override fun onSuccessAddWishlist(productId: String?) {
                    wishlistCallback.invoke(true, null)
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

    fun removeWishlist(model: HomeFeedViewModel, wishlistCallback: (((Boolean, Throwable?) -> Unit))){
        removeWishListUseCase.createObservable(model.productId, userSessionInterface.userId, object: WishListActionListener{
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

    fun isLogin() = userSessionInterface.isLoggedIn

    override fun detachView() {
        super.detachView()
        getHomeFeedUseCase.unsubscribe()
    }
}