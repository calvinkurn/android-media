package com.tokopedia.review.feature.inbox.buyerreview.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.ReputationDataUiModel

/**
 * @author by nisie on 8/10/17.
 */
interface InboxReputation {
    interface View : CustomerView {
        fun showLoadingFull()
        fun onErrorGetFirstTimeInboxReputation(throwable: Throwable)
        fun onSuccessGetFirstTimeInboxReputation(inboxReputationUiModel: InboxReputationUiModel)
        fun finishLoadingFull()
        fun onErrorGetNextPage(throwable: Throwable)
        fun onSuccessGetNextPage(inboxReputationUiModel: InboxReputationUiModel)
        fun onErrorRefresh(throwable: Throwable)
        fun onSuccessRefresh(inboxReputationUiModel: InboxReputationUiModel)
        fun showLoadingNext()
        fun finishLoading()
        fun onGoToDetail(
            reputationId: String, invoice: String, createTime: String,
            revieweeName: String, revieweeImage: String,
            reputationDataUiModel: ReputationDataUiModel, textDeadline: String,
            adapterPosition: Int, role: Int
        )

        fun showRefreshing()
        fun onSuccessGetFilteredInboxReputation(inboxReputationUiModel: InboxReputationUiModel)
        fun onErrorGetFilteredInboxReputation(throwable: Throwable)
        fun finishRefresh()
        fun onShowEmpty()
        fun onShowEmptyFilteredInboxReputation()
    }

    interface Presenter : CustomerPresenter<View?> {
        fun getFirstTimeInboxReputation(tab: Int)

        fun getNextPage(
            lastItemPosition: Int, visibleItem: Int, query: String,
            timeFilter: String, statusFilter: String, tab: Int
        )

        fun getFilteredInboxReputation(
            query: String,
            timeFilter: String,
            statusFilter: String,
            tab: Int
        )
    }
}