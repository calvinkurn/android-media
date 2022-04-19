package com.tokopedia.review.feature.inbox.buyerreview.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetFirstTimeInboxReputationUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetInboxReputationUseCase
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.GetFilteredInboxReputationSubscriber
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.GetFirstTimeInboxReputationSubscriber
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.GetNextPageInboxReputationSubscriber
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.RefreshInboxReputationSubscriber
import javax.inject.Inject

/**
 * @author by nisie on 8/10/17.
 */
class InboxReputationPresenter @Inject internal constructor(
    private val getFirstTimeInboxReputationUseCase: GetFirstTimeInboxReputationUseCase,
    private val getInboxReputationUseCase: GetInboxReputationUseCase
) : BaseDaggerPresenter<InboxReputation.View>(), InboxReputation.Presenter {

    private var viewListener: InboxReputation.View? = null
    private val pagingHandler: PagingHandler = PagingHandler()

    override fun attachView(view: InboxReputation.View?) {
        super.attachView(view)
        viewListener = view
    }

    override fun getFirstTimeInboxReputation(tab: Int) {
        viewListener?.showLoadingFull()
        getFirstTimeInboxReputationUseCase.execute(
            GetFirstTimeInboxReputationUseCase.getFirstTimeParam(tab),
            viewListener?.let { GetFirstTimeInboxReputationSubscriber(it) }
        )
    }

    override fun getNextPage(
        lastItemPosition: Int, visibleItem: Int,
        query: String, timeFilter: String,
        statusFilter: String, tab: Int
    ) {
        if (hasNextPage() && isOnLastPosition(
                lastItemPosition,
                visibleItem
            )
        ) {
            viewListener?.showLoadingNext()
            pagingHandler.nextPage()
            getInboxReputationUseCase.execute(
                GetInboxReputationUseCase.getParam(
                    pagingHandler.page,
                    query,
                    timeFilter,
                    statusFilter,
                    tab
                ),
                viewListener?.let { GetNextPageInboxReputationSubscriber(it) }
            )
        }
    }

    override fun getFilteredInboxReputation(
        query: String,
        timeFilter: String,
        statusFilter: String,
        tab: Int
    ) {
        viewListener?.showRefreshing()
        pagingHandler.resetPage()
        getInboxReputationUseCase.execute(
            GetInboxReputationUseCase.getParam(
                pagingHandler.page,
                query,
                timeFilter,
                statusFilter,
                tab
            ),
            viewListener?.let { GetFilteredInboxReputationSubscriber(it) }
        )
    }

    private fun hasNextPage(): Boolean {
        return pagingHandler.CheckNextPage()
    }

    private fun isOnLastPosition(itemPosition: Int, visibleItem: Int): Boolean {
        return itemPosition == visibleItem
    }

    fun refreshPage(query: String, timeFilter: String, scoreFilter: String, tab: Int) {
        pagingHandler.resetPage()
        getInboxReputationUseCase.execute(
            GetInboxReputationUseCase.getParam(
                pagingHandler.page, query, timeFilter,
                scoreFilter, tab
            ),
            viewListener?.let {
                RefreshInboxReputationSubscriber(
                    it, isUsingFilter(
                        query,
                        timeFilter, scoreFilter
                    )
                )
            }

        )
    }

    private fun isUsingFilter(query: String, timeFilter: String, scoreFilter: String): Boolean {
        return query.isNotEmpty() || timeFilter.isNotEmpty() || scoreFilter.isNotEmpty()
    }

    fun setHasNextPage(hasNextPage: Boolean) {
        pagingHandler.setHasNext(hasNextPage)
    }

    override fun detachView() {
        super.detachView()
        getFirstTimeInboxReputationUseCase.unsubscribe()
        getInboxReputationUseCase.unsubscribe()
    }

}