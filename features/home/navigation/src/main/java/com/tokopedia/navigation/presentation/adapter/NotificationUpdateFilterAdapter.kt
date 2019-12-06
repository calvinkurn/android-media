package com.tokopedia.navigation.presentation.adapter

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationUpdateFilterSectionTypeFactory
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationUpdateFilterSectionTypeFactoryImpl
import com.tokopedia.navigation.presentation.adapter.viewholder.NotificationUpdateFilterSectionItemViewHolder
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterItemViewModel
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterSectionItemViewModel
import com.tokopedia.navigation.widget.ChipFilterItemDivider
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.List
import kotlin.collections.arrayListOf
import kotlin.collections.toList

/**
 * @author : Steven 11/04/19
 */

class NotificationUpdateFilterAdapter(
        private val notificationUpdateTypeFactory: NotificationUpdateFilterSectionTypeFactoryImpl,
        private val listener: FilterAdapterListener,
        private val userSession: UserSessionInterface
) : BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(notificationUpdateTypeFactory),
        NotificationUpdateFilterSectionItemViewHolder.FilterSectionListener {

    private val filterTypePosition = HashMap<String, Int>()
    private val filterTypeId = HashMap<String, Int>()
    private var data: ArrayList<NotificationUpdateFilterItemViewModel> = arrayListOf()

    interface FilterAdapterListener {
        fun updateFilter(filter: HashMap<String, Int>)
        fun sentFilterAnalytic(analyticData: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        val view = onCreateViewItem(parent, viewType)
        return notificationUpdateTypeFactory.createViewHolder(view, viewType, this)
    }

    override fun addElement(visitables: List<Visitable<*>>) {
        val position = visitables.size - 1
        this.visitables.addAll(visitables)
        notifyItemRangeInserted(position, visitables.size)
    }

    fun updateData(data: ArrayList<NotificationUpdateFilterItemViewModel>) {
        val filteredData = filterData(data)
        val flatViewModel = flattenFilterViewModel(filteredData)
        mapFilterData(filteredData)
        addElement(flatViewModel)
    }

    private fun filterData(data: ArrayList<NotificationUpdateFilterItemViewModel>): ArrayList<NotificationUpdateFilterItemViewModel> {
        if (userSession.hasShop()) return data
        val itemToRemove = arrayListOf<NotificationUpdateFilterItemViewModel>()
        for (filter in data) {
            if (filter.filterType == NotificationUpdateFilterItemViewModel.FilterType.TYPE_ID.type) {
                itemToRemove.add(filter)
            }
        }
        data.removeAll(itemToRemove)
        return data
    }

    override fun onFilterClicked(element: NotificationUpdateFilterSectionItemViewModel) {
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
                    as? NotificationUpdateFilterSectionItemViewModel
                    ?: continue

            when (filterType) {
                NotificationUpdateFilterItemViewModel.FilterType.TYPE_ID.type -> typeName = data.text
                NotificationUpdateFilterItemViewModel.FilterType.TAG_ID.type -> tagName = data.text
            }
        }

        return String.format("%s - %s", typeName, tagName)
    }

    private fun mapFilterData(data: ArrayList<NotificationUpdateFilterItemViewModel>) {
        this.data = data
        for (filterType in data) {
            filterTypePosition[filterType.filterType] = NONE_SELECTED_POSITION
        }
    }

    private fun flattenFilterViewModel(types: ArrayList<NotificationUpdateFilterItemViewModel>)
            : List<Visitable<NotificationUpdateFilterSectionTypeFactory>> {
        val visitable = arrayListOf<Visitable<NotificationUpdateFilterSectionTypeFactory>>()
        for (type in types) {
            visitable.addAll(type.list)
        }
        return visitable.toList()
    }

    companion object {
        const val NONE_SELECTED_POSITION = -1
    }
}