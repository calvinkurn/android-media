package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailDataModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.GroupDetailAdapterFactory
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder.GroupDetailChipsViewHolder

class GroupDetailAdapter(private val adapterTypeFactory: GroupDetailAdapterFactory) :
    ListAdapter<GroupDetailDataModel, AbstractViewHolder<*>>(
        InsightListDiffUtilCallBack()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<*> {
        val view = onCreateViewItem(parent, viewType)
        return adapterTypeFactory.createViewHolder(view, viewType)
    }

    private fun onCreateViewItem(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(viewType, parent, false)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        (holder as? AbstractViewHolder<GroupDetailDataModel>)?.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position]?.type(adapterTypeFactory) ?: HideViewHolder.LAYOUT
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<*>) {
        (holder as? GroupDetailChipsViewHolder)?.onViewAttachedToWindow()
    }

}

class InsightListDiffUtilCallBack : DiffUtil.ItemCallback<GroupDetailDataModel>() {
    override fun areItemsTheSame(
        oldItem: GroupDetailDataModel,
        newItem: GroupDetailDataModel
    ): Boolean {
        return oldItem.type() == newItem.type()
    }

    override fun areContentsTheSame(
        oldItem: GroupDetailDataModel,
        newItem: GroupDetailDataModel
    ): Boolean {
        return oldItem.equalsWith(newItem)
    }
}
