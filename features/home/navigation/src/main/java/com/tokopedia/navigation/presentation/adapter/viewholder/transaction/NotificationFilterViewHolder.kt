package com.tokopedia.navigation.presentation.adapter.viewholder.transaction

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.model.NotificationFilterSection
import com.tokopedia.navigation.presentation.adapter.NotificationUpdateFilterAdapter
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationUpdateFilterSectionTypeFactoryImpl
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterItemViewModel
import com.tokopedia.navigation.widget.ChipFilterItemDivider
import com.tokopedia.user.session.UserSession

class NotificationFilterViewHolder(
        val view: View
): AbstractViewHolder<NotificationFilterSection>(view), NotificationUpdateFilterAdapter.FilterAdapterListener {

    private val context = view.context
    private val lstFilter = view.findViewById<RecyclerView>(R.id.filter_list)
    private var filterAdapter: NotificationUpdateFilterAdapter?= null

    override fun bind(element: NotificationFilterSection) {
        if (filterAdapter == null) {
            filterAdapter = NotificationUpdateFilterAdapter(
                    NotificationUpdateFilterSectionTypeFactoryImpl(),
                    this,
                    UserSession(context?.applicationContext))
            lstFilter.adapter = filterAdapter
        } else {
            filterAdapter?.updateData(map(element))
            lstFilter.addItemDecoration(ChipFilterItemDivider(context))
        }
    }

    override fun sentFilterAnalytic(analyticData: String) {

    }

    override fun updateFilter(filter: HashMap<String, Int>) {

    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_filter

        fun map(elements: NotificationFilterSection): ArrayList<NotificationUpdateFilterItemViewModel> {
            val filterItem = arrayListOf<NotificationUpdateFilterItemViewModel>()
            filterItem.forEach {
                it.filterType = elements.filterType
                it.title = elements.title
                it.list = elements.list
            }
            return filterItem
        }
    }

}