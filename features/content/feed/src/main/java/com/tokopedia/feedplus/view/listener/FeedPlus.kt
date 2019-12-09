package com.tokopedia.feedplus.view.listener

import android.content.Context
import android.content.res.Resources

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem

import java.util.ArrayList

/**
 * @author by nisie on 5/15/17.
 */

interface FeedPlus {

    interface View : CustomerView {

        fun sendMoEngageOpenFeedEvent()

        fun stopTracePerformanceMon()

        interface Polling {

            fun onVoteOptionClicked(rowNumber: Int, pollId: String, optionId: String)

            fun onGoToLink(link: String)
        }

        fun setLastCursorOnFirstPage(lastCursor: String)

        fun setFirstPageCursor(firstPageCursor: String)

        fun onInfoClicked()

        fun onErrorGetFeedFirstPage(errorMessage: String)

        fun sendFeedPlusScreenTracking()

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

        fun onSuccessToggleFavoriteShop(rowNumber: Int, adapterPosition: Int)

        fun onErrorToggleFavoriteShop(message: String, rowNumber: Int, adapterPosition: Int,
                                      shopId: String)

    }

    interface Presenter : CustomerPresenter<View> {

        fun setCursor(cursor: String)

        fun toggleFavoriteShop(rowNumber: Int, shopId: String)

        fun toggleFavoriteShop(rowNumber: Int, adapterPosition: Int, shopId: String)

        fun trackAffiliate(url: String)

    }
}
