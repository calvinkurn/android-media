package com.tokopedia.shop.feed.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.shop.feed.domain.DynamicFeedShopDomain
import com.tokopedia.shop.feed.domain.usecase.GetFeedShopFirstUseCase
import com.tokopedia.shop.feed.view.contract.FeedShopContract
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by yfsx on 08/05/19.
 */
class FeedShopPresenter @Inject constructor(
        private val getFeedShopFirstUseCase: GetFeedShopFirstUseCase,
        private val getDynamicFeedUseCase: GetDynamicFeedUseCase
):
        BaseDaggerPresenter<FeedShopContract.View>(),
        FeedShopContract.Presenter {

    override var cursor: String = ""

    override fun detachView() {
        super.detachView()
        getFeedShopFirstUseCase.unsubscribe()
        getDynamicFeedUseCase.unsubscribe()
    }

    override fun getFeedFirstPage(shopId: String) {
        if (isViewAttached) {
            cursor = ""
            getFeedShopFirstUseCase.execute(
                    GetFeedShopFirstUseCase.createRequestParams(getUserId(), shopId),
                    object : Subscriber<DynamicFeedShopDomain>() {
                        override fun onNext(t: DynamicFeedShopDomain?) {
                            t?.let {
                                view.onSuccessGetFeedFirstPage(it.dynamicFeedDomainModel.postList)
                            }
                        }

                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable?) {
                            if (GlobalConfig.isAllowDebuggingTools()) {
                                e?.printStackTrace()
                            }
                            view.showGetListError(e)
                        }
                    }
            )
        }
    }

    override fun getFeed(shopId: String) {
        if (isViewAttached) {
            getDynamicFeedUseCase.execute(
                    GetDynamicFeedUseCase.createRequestParams(getUserId(), cursor, GetDynamicFeedUseCase.SOURCE_SHOP, shopId),
                    object : Subscriber<DynamicFeedDomainModel>() {
                        override fun onNext(t: DynamicFeedDomainModel?) {
                            t?.let {
                                view.onSuccessGetFeed(t.postList, t.cursor)
                            }
                        }

                        override fun onCompleted() {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onError(e: Throwable?) {
                            if (GlobalConfig.isAllowDebuggingTools()) {
                                e?.printStackTrace()
                            }
                            view.showGetListError(e)
                        }
                    }
            )
        }
    }

    override fun followKol(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unfollowKol(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun likeKol(id: Int, rowNumber: Int, likeListener: KolPostListener.View.Like) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unlikeKol(id: Int, rowNumber: Int, likeListener: KolPostListener.View.Like) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deletePost(id: Int, rowNumber: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun trackPostClick(uniqueTrackingId: String, redirectLink: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun trackPostClickUrl(url: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getUserId(): String {
        var userId = "0"
        if (!view.getUserSession().userId.isEmpty()) {
            userId = view.getUserSession().userId
        }
        return userId
    }
}