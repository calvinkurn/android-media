package com.tokopedia.profile.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.feedcomponent.data.pojo.FeedPostRelated
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticCommissionUiModel
import com.tokopedia.kolcommon.view.listener.KolPostLikeListener
import com.tokopedia.profile.view.viewmodel.DynamicFeedProfileViewModel

/**
 * @author by milhamj on 9/17/18.
 */
interface ProfileContract {
    interface View : BaseListViewListener<Visitable<*>>, ProfileEmptyContract.View {
        fun onSuccessGetProfileFirstPage(element: DynamicFeedProfileViewModel, isFromLogin: Boolean)

        fun onSuccessGetProfilePost(visitables: List<Visitable<*>>, lastCursor: String)

        fun updateCursor(cursor: String)

        fun onSuccessFollowKol()

        fun onErrorFollowKol(errorMessage: String)

        fun onSuccessDeletePost(rowNumber: Int)

        fun onErrorDeletePost(errorMessage: String, id: Int, rowNumber: Int)

        fun showLoadingLayout()

        fun hideLoadingLayout()

        fun hideHeader()

        fun onSuccessShouldChangeUsername(shouldChange: Boolean, link: String)

        fun onErrorShouldChangeUsername(errorMessage: String, link: String)

        fun onAddToCartSuccess()

        fun onAddToCartFailed(pdpAppLink: String)

        fun onSuccessGetPostStatistic(statisticCommissionModel: PostStatisticCommissionUiModel)

        fun onErrorGetPostStatistic(error: Throwable, activityId: String, productIds: List<String>)
    }

    interface Presenter : CustomerPresenter<View> {
        var cursor: String

        fun getProfileFirstPage(targetUserId: Int, isFromLogin: Boolean)

        fun getProfilePost(targetUserId: Int)

        fun followKol(id: Int, isFollow:Boolean)

        fun likeKol(id: Int, rowNumber: Int, likeListener: KolPostLikeListener, isLiked:Boolean)

        fun deletePost(id: Int, rowNumber: Int)

        fun addPostTagItemToCart(postTagItem: PostTagItem)

        fun trackPostClick(uniqueTrackingId: String, redirectLink: String)

        fun trackPostClickUrl(url: String)

        fun doTopAdsTracker(url: String, shopId: String, shopName: String, imageUrl: String, isClick: Boolean)

        fun shouldChangeUsername(userId: Int, link: String = "")

        fun getRelatedProfile(onErrorGetRelatedProfile: ((throwable: Throwable?) -> Unit)?,
                              onSuccessGetRelatedProfile: ((feedPostRelated: FeedPostRelated?) -> Unit)?)

        fun getPostStatistic(activityId: String, productIds: List<String>, likeCount: Int, commentCount: Int)
    }
}