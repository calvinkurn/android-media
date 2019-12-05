package com.tokopedia.feedplus.view.listener

import android.content.Context
import android.content.res.Resources

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel

import java.util.ArrayList

/**
 * @author by nisie on 5/15/17.
 */

interface FeedPlus {

    interface View : CustomerView {

        fun sendMoEngageOpenFeedEvent()

        fun stopTracePerformanceMon()

        fun onAddToCartSuccess()

        fun onAddToCartFailed(pdpAppLink: String)


        interface Polling {

            fun onVoteOptionClicked(rowNumber: Int, pollId: String, optionId: String)

            fun onGoToLink(link: String)
        }

        fun setLastCursorOnFirstPage(lastCursor: String)

        fun setFirstPageCursor(firstPageCursor: String)

        fun onInfoClicked()

        fun onErrorGetFeedFirstPage(errorMessage: String)

        fun sendFeedPlusScreenTracking()

        fun onErrorFollowKol(errorMessage: String, id: Int, status: Int, rowNumber: Int)

        fun onSuccessFollowUnfollowKol(rowNumber: Int)

        fun onErrorLikeDislikeKolPost(errorMessage: String)

        fun onSuccessLikeDislikeKolPost(rowNumber: Int)

        fun onSuccessFollowKolFromRecommendation(rowNumber: Int, position: Int, isFollow: Boolean)

        fun onSearchShopButtonClicked()

        fun showSnackbar(s: String)

        fun updateFavorite(adapterPosition: Int)

        fun showRefresh()

        fun finishLoading()

        fun showInterestPick()

        fun updateCursor(currentCursor: String)

        fun onRetryClicked()

        fun onShowRetryGetFeed()

        fun hideAdapterLoading()

        fun getString(msg_network_error: Int): String

        fun getColor(color: Int): Int

        fun onShowEmpty()

        fun clearData()

        fun setEndlessScroll()

        fun unsetEndlessScroll()

        fun onShowNewFeed(totalData: String)

        fun onHideNewFeed()

        fun hasFeed(): Boolean

        fun updateFavoriteFromEmpty(shopId: String)

        fun onGoToLogin()

        fun onSuccessSendVote(rowNumber: Int, optionId: String,
                              voteStatisticDomainModel: VoteStatisticDomainModel)

        fun onErrorSendVote(message: String)

        fun onSuccessToggleFavoriteShop(rowNumber: Int, adapterPosition: Int)

        fun onErrorToggleFavoriteShop(message: String, rowNumber: Int, adapterPosition: Int,
                                      shopId: String)

        fun onSuccessDeletePost(rowNumber: Int)

        fun onErrorDeletePost(errorMessage: String, id: Int, rowNumber: Int)

    }

    interface Presenter : CustomerPresenter<View> {

        fun favoriteShop(promotedShopViewModel: Data, adapterPosition: Int)

        fun setCursor(cursor: String)

        fun followKol(id: Int, rowNumber: Int)

        fun unfollowKol(id: Int, rowNumber: Int)

        fun likeKol(id: Int, rowNumber: Int)

        fun unlikeKol(id: Int, rowNumber: Int)

        fun sendVote(rowNumber: Int, pollId: String, optionId: String)

        fun followKolFromRecommendation(id: Int, rowNumber: Int, position: Int)

        fun unfollowKolFromRecommendation(id: Int, rowNumber: Int, position: Int)

        fun toggleFavoriteShop(rowNumber: Int, shopId: String)

        fun toggleFavoriteShop(rowNumber: Int, adapterPosition: Int, shopId: String)

        fun trackAffiliate(url: String)

        fun addPostTagItemToCart(postTagItem: PostTagItem)

        fun deletePost(id: Int, rowNumber: Int)
    }
}
