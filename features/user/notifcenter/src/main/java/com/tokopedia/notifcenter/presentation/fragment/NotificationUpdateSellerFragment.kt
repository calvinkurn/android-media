package com.tokopedia.notifcenter.presentation.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.notifcenter.data.viewbean.NotificationUpdateFilterViewBean
import com.tokopedia.notifcenter.presentation.adapter.NotificationUpdateFilterAdapter

/**
 * Created by faisalramd on 10/02/20.
 */
class NotificationUpdateSellerFragment : NotificationUpdateFragment() {

    private val filterSeller = hashMapOf(FILTER_SELLER_TYPE_KEY to FILTER_SELLER_TYPE_VALUE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.clearNotifCounter()
    }

    override fun loadData(page: Int) {
        // filter seller data only, using filterSeller
        presenter.updateFilter(filterSeller)
        super.loadData(page)
    }

    override fun onSuccessGetFilter(): (ArrayList<NotificationUpdateFilterViewBean>) -> Unit {
        return {
            // remove role section, force to seller only
            it.removeAt(0)
            filterAdapter?.updateData(it)
        }
    }

    override fun updateFilter(filter: HashMap<String, Int>) {
        // update filter seller params
        filterSeller[FILTER_SELLER_TAG_KEY] = filter[FILTER_SELLER_TAG_KEY] ?:
                NotificationUpdateFilterAdapter.NONE_SELECTED_POSITION
        cursor = ""
        loadInitialData()
    }

    companion object {
        const val FILTER_SELLER_TYPE_KEY = "typeId"
        const val FILTER_SELLER_TYPE_VALUE = 2
        const val FILTER_SELLER_TAG_KEY = "tagId"
    }
}