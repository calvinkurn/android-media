package com.tokopedia.navigation.presentation.presenter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.navigation.GlobalNavConstant
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.GetDrawerNotificationUseCase
import com.tokopedia.navigation.domain.model.RecomTitle
import com.tokopedia.navigation.domain.model.Recommendation
import com.tokopedia.navigation.domain.subscriber.InboxSubscriber
import com.tokopedia.navigation.presentation.view.InboxView
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Lukas on 2019-07-31
 */

class InboxPresenter @Inject constructor(
        private val getNotificationUseCase: GetDrawerNotificationUseCase,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val userSessionInterface: UserSessionInterface,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase
) : BaseDaggerPresenter<CustomerView>() {

    private var inboxView: InboxView? = null

    fun setView(inboxView: InboxView) {
        this.inboxView = inboxView
    }

    fun getInboxData() {
        if (this.inboxView == null)
            return

        this.inboxView?.onStartLoading()

        val requestParams = RequestParams.create()
        requestParams.putString(GlobalNavConstant.QUERY,
                GraphqlHelper.loadRawString(this.inboxView?.context?.resources, R.raw.query_notification))
        getNotificationUseCase.execute(requestParams, InboxSubscriber(this.inboxView))
    }

    fun getFirstRecomData() {
        if (this.inboxView == null)
            return
        getRecommendationUseCase.execute(getRecommendationUseCase.getRecomParams(0,
                X_SOURCE_RECOM_WIDGET,
                INBOX_PAGE,
                ArrayList()),
                object : Subscriber<List<RecommendationWidget>>() {
                    override fun onStart() {
                        inboxView?.showLoadMoreLoading()
                    }

                    override fun onCompleted() {
                        inboxView?.hideLoadMoreLoading()
                    }

                    override fun onError(e: Throwable) {
                        inboxView?.hideLoadMoreLoading()
                    }

                    override fun onNext(recommendationWidgets: List<RecommendationWidget>) {
                        val visitables = ArrayList<Visitable<*>>()
                        val recommendationWidget = recommendationWidgets[0]
                        visitables.add(RecomTitle(recommendationWidget.title))
                        visitables.addAll(getRecommendationVisitables(recommendationWidget))
                        inboxView?.hideLoadMoreLoading()
                        inboxView?.onRenderRecomInbox(visitables)
                    }
                })
    }

    fun getRecomData(page: Int) {
        if (this.inboxView == null)
            return
        getRecommendationUseCase.execute(getRecommendationUseCase.getRecomParams(
                page,
                X_SOURCE_RECOM_WIDGET,
                INBOX_PAGE,
                ArrayList()),
                object : Subscriber<List<RecommendationWidget>>() {
                    override fun onStart() {
                        inboxView?.showLoadMoreLoading()
                    }

                    override fun onCompleted() {
                        inboxView?.hideLoadMoreLoading()
                    }

                    override fun onError(e: Throwable) {
                        inboxView?.hideLoadMoreLoading()
                    }

                    override fun onNext(recommendationWidgets: List<RecommendationWidget>) {
                        inboxView?.hideLoadMoreLoading()
                        val recommendationWidget = recommendationWidgets[0]
                        inboxView?.onRenderRecomInbox(getRecommendationVisitables(recommendationWidget))
                    }
                })
    }


    fun addWishlist(model: RecommendationItem, callback: ((Boolean, Throwable?) -> Unit)){
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

    fun removeWishlist(model: RecommendationItem, wishlistCallback: (((Boolean, Throwable?) -> Unit))){
        removeWishListUseCase.createObservable(model.productId.toString(), userSessionInterface.userId, object: WishListActionListener{
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

    fun getUserId(): String = userSessionInterface.userId ?: ""

    private fun getRecommendationVisitables(recommendationWidget: RecommendationWidget): List<Visitable<*>> {
        val recomendationList = ArrayList<Visitable<*>>()
        for (item in recommendationWidget.recommendationItemList) {
            recomendationList.add(Recommendation(item))
        }
        return recomendationList
    }

    fun onResume() {
        this.getInboxData()
    }

    fun onDestroy() {
        this.getRecommendationUseCase.unsubscribe()
        this.getNotificationUseCase.unsubscribe()
        this.inboxView = null
    }

    companion object {
        val X_SOURCE_RECOM_WIDGET = "recom_widget"
        val INBOX_PAGE = "inbox"
    }
}
