package com.tokopedia.notifcenter.presentation.adapter.viewholder.transaction

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.model.NotificationFilterSection
import com.tokopedia.notifcenter.data.viewbean.NotificationFilterSectionViewBean
import com.tokopedia.notifcenter.presentation.adapter.NotificationUpdateFilterAdapter
import com.tokopedia.notifcenter.presentation.adapter.typefactory.filter.NotificationUpdateFilterSectionTypeFactoryImpl
import com.tokopedia.notifcenter.data.viewbean.NotificationUpdateFilterViewBean
import com.tokopedia.notifcenter.widget.ChipFilterItemDivider
import com.tokopedia.user.session.UserSessionInterface

class NotificationFilterViewHolder(
        val view: View,
        val listener: NotifFilterListener,
        val userSession: UserSessionInterface
): AbstractViewHolder<NotificationFilterSectionViewBean>(view), NotificationUpdateFilterAdapter.FilterAdapterListener {

    private val lstFilter = view.findViewById<RecyclerView>(R.id.filter_list)
    private var filterAdapter: NotificationUpdateFilterAdapter?= null

    override fun bind(element: NotificationFilterSectionViewBean) {
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

        fun map(elements: List<NotificationFilterSection>): ArrayList<NotificationUpdateFilterViewBean> {
            val filterItem = arrayListOf<NotificationUpdateFilterViewBean>()
            elements.forEach { notificationFilterSection ->
                filterItem.add(NotificationUpdateFilterViewBean(
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