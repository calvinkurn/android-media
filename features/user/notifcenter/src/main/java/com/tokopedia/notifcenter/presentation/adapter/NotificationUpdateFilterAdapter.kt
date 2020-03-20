package com.tokopedia.notifcenter.presentation.adapter

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.notifcenter.data.viewbean.NotificationUpdateFilterSectionViewBean
import com.tokopedia.notifcenter.data.viewbean.NotificationUpdateFilterViewBean
import com.tokopedia.notifcenter.listener.NotificationFilterListener
import com.tokopedia.notifcenter.presentation.adapter.typefactory.filter.NotificationUpdateFilterSectionTypeFactory
import com.tokopedia.notifcenter.presentation.adapter.typefactory.filter.NotificationUpdateFilterSectionTypeFactoryImpl
import com.tokopedia.notifcenter.presentation.adapter.viewholder.NotificationUpdateFilterSectionItemViewHolder
import com.tokopedia.notifcenter.widget.ChipFilterItemDivider
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import kotlin.collections.HashMap

/**
 * @author : Steven 11/04/19
 */

class NotificationUpdateFilterAdapter(
        private val notificationUpdateTypeFactory: NotificationUpdateFilterSectionTypeFactoryImpl,
        private val listener: NotificationFilterListener,
        private val userSession: UserSessionInterface
) : BaseListAdapter<Visitable<*>,
        BaseAdapterTypeFactory>(notificationUpdateTypeFactory),
        NotificationUpdateFilterSectionItemViewHolder.FilterSectionListener,
        ChipFilterItemDivider.ChipFilterListener {

    private val filterTypePosition = HashMap<String, Int>()
    private val filterTypeId = HashMap<String, Int>()
    private var data: ArrayList<NotificationUpdateFilterViewBean> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        val view = onCreateViewItem(parent, viewType)
        return notificationUpdateTypeFactory.createViewHolder(view, viewType, this)
    }

    override fun addElement(visitables: List<Visitable<*>>) {
        val position = visitables.size - 1
        this.visitables.addAll(visitables)
        notifyItemRangeInserted(position, visitables.size)
    }

    fun updateData(data: ArrayList<NotificationUpdateFilterViewBean>) {
        val filteredData = filterData(data)
        val flatViewModel = flattenFilterViewModel(filteredData)
        mapFilterData(filteredData)
        addElement(flatViewModel)
    }

    private fun filterData(data: ArrayList<NotificationUpdateFilterViewBean>): ArrayList<NotificationUpdateFilterViewBean> {
        if (userSession.hasShop()) return data
        val itemToRemove = arrayListOf<NotificationUpdateFilterViewBean>()
        for (filter in data) {
            if (filter.filterType == NotificationUpdateFilterViewBean.FilterType.TYPE_ID.type) {
                itemToRemove.add(filter)
            }
        }
        data.removeAll(itemToRemove)
        return data
    }

    override fun onFilterClicked(element: NotificationUpdateFilterSectionViewBean) {
        var counter = 0
        for (filter in data) {
            val filterType = filter.filterType
            val oldPosition = filterTypePosition[filterType] ?: NONE_SELECTED_POSITION
            val filterTypeIndex = filter.list.indexOf(element)
            val newPosition = filterTypeIndex + counter
            counter += filter.list.size

            if (filterTypeIndex == NONE_SELECTED_POSITION) continue

            if (oldPosition != NONE_SELECTED_POSITION) {
                notifyItemChanged(oldPosition, NotificationUpdateFilterSectionItemViewHolder.PAYLOAD_DESELECTED)
            }

            if (oldPosition == newPosition) {
                filterTypePosition[filterType] = NONE_SELECTED_POSITION
                filterTypeId.remove(filterType)
                notifyItemChanged(oldPosition, NotificationUpdateFilterSectionItemViewHolder.PAYLOAD_DESELECTED)
            } else {
                filterTypePosition[filterType] = newPosition
                filterTypeId[filterType] = element.id.toIntOrZero()
                notifyItemChanged(newPosition, NotificationUpdateFilterSectionItemViewHolder.PAYLOAD_SELECTED)
            }
        }

        val analyticData = getLabelFilterName()
        listener.updateFilter(filterTypeId)
        listener.sentFilterAnalytic(analyticData)
    }

    private fun getLabelFilterName(): String {
        var typeName = ""
        var tagName = ""
        for (filter in data) {
            val filterType = filter.filterType
            if (!filterTypeId.containsKey(filterType)) continue

            val filterPosition = filterTypePosition[filterType] ?: continue

            if (visitables.size <= filterPosition) continue

            val data = visitables[filterPosition]
                    as? NotificationUpdateFilterSectionViewBean
                    ?: continue

            when (filterType) {
                NotificationUpdateFilterViewBean.FilterType.TYPE_ID.type -> typeName = data.text
                NotificationUpdateFilterViewBean.FilterType.TAG_ID.type -> tagName = data.text
            }
        }

        return String.format("%s - %s", typeName, tagName)
    }

    private fun mapFilterData(data: ArrayList<NotificationUpdateFilterViewBean>) {
        this.data = data
        for (filterType in data) {
            filterTypePosition[filterType.filterType] = NONE_SELECTED_POSITION
        }
    }

    private fun flattenFilterViewModel(types: ArrayList<NotificationUpdateFilterViewBean>)
            : List<Visitable<NotificationUpdateFilterSectionTypeFactory>> {
        val visitable = arrayListOf<Visitable<NotificationUpdateFilterSectionTypeFactory>>()
        for (type in types) {
            visitable.addAll(type.list)
        }
        return visitable.toList()
    }

    override fun getDividerPositions(): List<Int> {
        val positions = arrayListOf<Int>()
        val lastFilterSize = data.size - 1
        var previousItem = 0
        for ((index, filter) in data.withIndex()) {
            val filterSectionSize = filter.list.size
            if (index != lastFilterSize) {
                val dividerPosition = filterSectionSize + previousItem - 1
                positions.add(dividerPosition)
            }
            previousItem += filterSectionSize
        }
        return positions
    }

    companion object {
        const val NONE_SELECTED_POSITION = -1
    }
}