package com.tokopedia.notifcenter.presentation.fragment

import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.data.consts.EmptyDataStateProvider
import com.tokopedia.notifcenter.data.model.NotificationViewData
import com.tokopedia.notifcenter.data.viewbean.NotificationUpdateFilterViewBean
import com.tokopedia.notifcenter.presentation.adapter.NotificationUpdateFilterAdapter

/**
 * Created by faisalramd on 10/02/20.
 */
class NotificationUpdateSellerFragment : NotificationUpdateFragment() {

    override fun onSuccessInitiateData(): (NotificationViewData) -> Unit {
        return {
            hideLoading()
            notificationUpdateAdapter.removeEmptyState()
            val notificationList = it.list.filter { s -> s.label == FILTER_SELLER_TYPE_VALUE }

            if (isFirstLoaded && notificationList.isEmpty()) {
                filterRecyclerView.hide()
            }

            if (notificationList.isEmpty()) {
                updateScrollListenerState(false)
                notificationUpdateAdapter.addElement(EmptyDataStateProvider.emptyData())
            } else {
                val canLoadMore = it.paging.hasNext
                if (canLoadMore && notificationList.isNotEmpty()) {
                    cursor = (notificationList.last().notificationId)
                }
                if (swipeToRefresh.isRefreshing) {
                    notificationUpdateListener?.onSuccessLoadNotifUpdate()
                }

                isFirstLoaded = false
                filterRecyclerView.show()

                notificationUpdateAdapter.addElement(notificationList)
                updateScrollListenerState(canLoadMore)

                if (notificationUpdateAdapter.dataSize < minimumScrollableNumOfItems
                        && endlessRecyclerViewScrollListener != null && canLoadMore) {
                    endlessRecyclerViewScrollListener.loadMoreNextPage()
                }
            }
        }
    }

    override fun onSuccessGetFilter(): (ArrayList<NotificationUpdateFilterViewBean>) -> Unit {
        return {
            it.removeAt(0)
            filterAdapter?.updateData(it)
        }
    }

    override fun updateFilter(filter: HashMap<String, Int>) {
        val filterSeller = HashMap<String, Int>()
        filterSeller[FILTER_SELLER_TYPE_KEY] = FILTER_SELLER_TYPE_VALUE
        filterSeller[FILTER_SELLER_TAG_KEY] = filter[FILTER_SELLER_TAG_KEY] ?:
                NotificationUpdateFilterAdapter.NONE_SELECTED_POSITION
        presenter.updateFilter(filterSeller)
        cursor = ""
        loadInitialData()
    }

    companion object {
        const val FILTER_SELLER_TYPE_KEY = "typeId"
        const val FILTER_SELLER_TYPE_VALUE = 2
        const val FILTER_SELLER_TAG_KEY = "tagId"
    }
}