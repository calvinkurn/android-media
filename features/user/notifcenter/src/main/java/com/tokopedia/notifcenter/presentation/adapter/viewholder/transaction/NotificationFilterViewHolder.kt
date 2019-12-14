package com.tokopedia.notifcenter.presentation.adapter.viewholder.transaction

import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.domain.model.NotificationFilterSection
import com.tokopedia.notifcenter.domain.model.NotificationFilterSectionWrapper
import com.tokopedia.notifcenter.presentation.adapter.NotificationUpdateFilterAdapter
import com.tokopedia.notifcenter.presentation.adapter.typefactory.NotificationUpdateFilterSectionTypeFactoryImpl
import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationUpdateFilterItemViewModel
import com.tokopedia.notifcenter.widget.ChipFilterItemDivider
import com.tokopedia.user.session.UserSessionInterface

class NotificationFilterViewHolder(
        val view: View,
        val listener: NotifFilterListener,
        val userSession: UserSessionInterface
): AbstractViewHolder<NotificationFilterSectionWrapper>(view), NotificationUpdateFilterAdapter.FilterAdapterListener {

    private val lstFilter = view.findViewById<RecyclerView>(R.id.filter_list)
    private var filterAdapter: NotificationUpdateFilterAdapter?= null

    override fun bind(element: NotificationFilterSectionWrapper) {
        if (userSession.hasShop() && listener.isHasNotification()) {
            lstFilter.show()
        }

        if (filterAdapter == null) {
            filterAdapter = NotificationUpdateFilterAdapter(
                    NotificationUpdateFilterSectionTypeFactoryImpl(),
                    this,
                    userSession)
            lstFilter.adapter = filterAdapter
            filterAdapter?.updateData(map(element.filters))
            lstFilter.addItemDecoration(ChipFilterItemDivider(view.context))
        }
    }

    override fun sentFilterAnalytic(analyticData: String) {
        listener.sentFilterAnalytic(analyticData)
    }

    override fun updateFilter(filter: HashMap<String, Int>) {
        listener.updateFilter(filter)
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_filter

        fun map(elements: List<NotificationFilterSection>): ArrayList<NotificationUpdateFilterItemViewModel> {
            val filterItem = arrayListOf<NotificationUpdateFilterItemViewModel>()
            elements.forEach { notificationFilterSection ->
                filterItem.add(NotificationUpdateFilterItemViewModel(
                        notificationFilterSection.filterType,
                        notificationFilterSection.title,
                        notificationFilterSection.list))
            }
            return filterItem
        }
    }

    interface NotifFilterListener {
        fun updateFilter(filter: HashMap<String, Int>)
        fun sentFilterAnalytic(analyticData: String)
        fun isHasNotification(): Boolean
    }

}