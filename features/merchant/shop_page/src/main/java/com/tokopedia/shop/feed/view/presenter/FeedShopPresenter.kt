package com.tokopedia.shop.feed.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GlobalConfig
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
        private val getFeedShopFirstUseCase: GetFeedShopFirstUseCase
):
        BaseDaggerPresenter<FeedShopContract.View>(),
        FeedShopContract.Presenter {

    override var cursor: String = ""

    override fun detachView() {
        super.detachView()
        getFeedShopFirstUseCase.unsubscribe()
    }

    override fun getFeedFirstPage() {
        if (isViewAttached) {
            cursor = ""
            getFeedShopFirstUseCase.execute(
                    GetFeedShopFirstUseCase.createRequestParams(getUserId(),getShopId()),
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

    override fun getProfilePost() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    private fun getShopId(): String {
        var shopId = "0"
        if (!view.getUserSession().shopId.isEmpty()) {
            shopId = view.getUserSession().shopId
        }
        return shopId
    }
}