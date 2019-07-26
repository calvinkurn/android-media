package com.tokopedia.home.account.presentation.presenter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.account.AccountConstants
import com.tokopedia.home.account.domain.GetBuyerAccountUseCase
import com.tokopedia.home.account.presentation.BuyerAccount
import com.tokopedia.home.account.presentation.subscriber.GetBuyerAccountSubscriber
import com.tokopedia.home.account.presentation.viewmodel.AccountRecommendationTitleViewModel
import com.tokopedia.home.account.presentation.viewmodel.RecommendationProductViewModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase

import java.util.ArrayList
import java.util.HashMap

import rx.Subscriber

/**
 * @author okasurya on 7/17/18.
 */
class BuyerAccountPresenter(
        private val getBuyerAccountUseCase: GetBuyerAccountUseCase,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val userSessionInterface: UserSessionInterface) : BuyerAccount.Presenter {
    private var view: BuyerAccount.View? = null

    override fun getFirstRecomData() {
        if (this.view == null)
            return
        getRecommendationUseCase.execute(getRecommendationUseCase.getRecomParams(0,
                X_SOURCE_RECOM_WIDGET,
                AKUN_PAGE,
                ArrayList()),
                object : Subscriber<List<RecommendationWidget>>() {
                    override fun onStart() {
                        view!!.showLoadMoreLoading()
                    }

                    override fun onCompleted() {
                        view!!.hideLoadMoreLoading()
                    }

                    override fun onError(e: Throwable) {
                        view!!.hideLoadMoreLoading()
                    }

                    override fun onNext(recommendationWidgets: List<RecommendationWidget>) {
                        val visitables = ArrayList<Visitable<*>>()
                        val recommendationWidget = recommendationWidgets[0]
                        visitables.add(AccountRecommendationTitleViewModel(recommendationWidget.title))
                        visitables.addAll(getRecommendationVisitables(recommendationWidget))
                        view!!.hideLoadMoreLoading()
                        view!!.onRenderRecomAccountBuyer(visitables)
                    }
                })
    }

    override fun getRecomData(page: Int) {
        if (this.view == null)
            return
        getRecommendationUseCase.execute(getRecommendationUseCase.getRecomParams(
                page,
                X_SOURCE_RECOM_WIDGET,
                AKUN_PAGE,
                ArrayList()),
                object : Subscriber<List<RecommendationWidget>>() {
                    override fun onStart() {
                        view!!.showLoadMoreLoading()
                    }

                    override fun onCompleted() {
                        view!!.hideLoadMoreLoading()
                    }

                    override fun onError(e: Throwable) {
                        view!!.hideLoadMoreLoading()
                    }

                    override fun onNext(recommendationWidgets: List<RecommendationWidget>) {
                        view!!.hideLoadMoreLoading()
                        val recommendationWidget = recommendationWidgets[0]
                        view!!.onRenderRecomAccountBuyer(getRecommendationVisitables(recommendationWidget))
                    }
                })
    }

    private fun getRecommendationVisitables(recommendationWidget: RecommendationWidget): List<Visitable<*>> {
        val recomendationList = ArrayList<Visitable<*>>()
        for (item in recommendationWidget.recommendationItemList) {
            recomendationList.add(RecommendationProductViewModel(item, recommendationWidget.title))
        }
        return recomendationList
    }

    override fun getBuyerData(query: String, saldoQuery: String) {
        view?.run {
            showLoading()
        }
        val requestParams = RequestParams.create()

        requestParams.putString(AccountConstants.QUERY, query)
        requestParams.putString(AccountConstants.SALDO_QUERY, saldoQuery)
        requestParams.putObject(AccountConstants.VARIABLES, HashMap<Any, Any>())

        getBuyerAccountUseCase.execute(requestParams, GetBuyerAccountSubscriber(view))
    }

    override fun attachView(view: BuyerAccount.View) {
        this.view = view
    }

    override fun detachView() {
        getBuyerAccountUseCase.unsubscribe()
        view = null
    }

    companion object {
        val X_SOURCE_RECOM_WIDGET = "account_buyer_page"
        val AKUN_PAGE = "account"
    }

    override fun addWishlist(model: RecommendationItem, callback: (Boolean, Throwable?) -> Unit){
        if(model.isTopAds){
            val params = RequestParams.create()
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
            })
        } else {
            addWishListUseCase.createObservable(model.productId.toString(), userSessionInterface.userId, object: WishListActionListener {
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

    override fun removeWishlist(model: RecommendationItem, wishlistCallback: (Boolean, Throwable?) -> Unit){
        removeWishListUseCase.createObservable(model.productId.toString(), userSessionInterface.userId, object: WishListActionListener {
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

    fun isLoggedIn() = userSessionInterface.isLoggedIn
}
