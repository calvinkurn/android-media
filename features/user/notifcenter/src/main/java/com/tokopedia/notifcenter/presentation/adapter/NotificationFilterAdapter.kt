package com.tokopedia.notifcenter.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.data.entity.filter.NotifcenterFilterResponse
import com.tokopedia.notifcenter.data.uimodel.filter.FilterLoadingUiModel
import com.tokopedia.notifcenter.data.uimodel.filter.FilterUiModel
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationFilterTypeFactory
import com.tokopedia.notifcenter.presentation.adapter.viewholder.filter.FilterViewHolder
import com.tokopedia.notifcenter.widget.NotificationFilterView

class NotificationFilterAdapter constructor(
        private val typeFactory: NotificationFilterTypeFactory
) : BaseListAdapter<Visitable<*>, NotificationFilterTypeFactory>(typeFactory),
        FilterViewHolder.Listener {

    var filterListener: NotificationFilterView.FilterListener? = null

    private val itemFilterLoading = FilterLoadingUiModel()
    private val defaultFilter = FilterUiModel()
    private var selectedFilter = defaultFilter

    fun showLoading(data: NotifcenterFilterResponse?) {
        if (data == null) {
            visitables.clear()
        }
        visitables.add(itemFilterLoading)
        notifyDataSetChanged()
        if (data != null) {
            showFilter(data)
        }
    }

    fun successLoading(data: NotifcenterFilterResponse?, needUpdate: Boolean) {
        if (needUpdate && data != null) {
            showFilter(data)
        }
    }

    override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int
    ): AbstractViewHolder<out Visitable<*>> {
        return typeFactory.onCreateViewHolder(parent, viewType, this)
    }

    override fun onFilterClicked(element: FilterUiModel) {
        deselectOtherSelectedFilter(element)
        notifyFilterChanged(element)
    }

    override fun isSelected(element: FilterUiModel): Boolean {
        return selectedFilter == element
    }

    private fun deselectOtherSelectedFilter(element: FilterUiModel) {
        if (isSelected(element)) return
        val previouslySelectedFilterPosition = visitables.indexOf(selectedFilter)
        if (previouslySelectedFilterPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(previouslySelectedFilterPosition, Any())
        }
    }

    private fun notifyFilterChanged(element: FilterUiModel) {
        if (isSelected(element)) {
            selectedFilter = defaultFilter
        } else {
            selectedFilter = element
        }
        filterListener?.onFilterChanged(selectedFilter.tagId)
    }

    private fun showFilter(data: NotifcenterFilterResponse) {
        val notificationFilter = data.notifcenterFilterV2.notifcenterTagList.list
        visitables.clear()
        visitables.addAll(notificationFilter)
        notifyDataSetChanged()
    }

}