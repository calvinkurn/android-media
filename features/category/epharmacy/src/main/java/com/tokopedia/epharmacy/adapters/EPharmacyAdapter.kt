package com.tokopedia.epharmacy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAdapterFactory
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel

class EPharmacyAdapter (asyncDifferConfig: AsyncDifferConfig<BaseEPharmacyDataModel>,
                        private val ePharmacyAdapterTypeFactory: EPharmacyAdapterFactory)
    : ListAdapter<BaseEPharmacyDataModel, AbstractViewHolder<*>>(asyncDifferConfig){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val view = onCreateViewItem(parent, viewType)
        return ePharmacyAdapterTypeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        bind(holder as AbstractViewHolder<BaseEPharmacyDataModel>, getItem(position))
    }

    private fun onCreateViewItem(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(viewType, parent, false)
    }

    private fun bind(holder: AbstractViewHolder<BaseEPharmacyDataModel>, item: BaseEPharmacyDataModel) {
        holder.bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < 0 || position >= currentList.size) {
            HideViewHolder.LAYOUT
        } else currentList[position]?.type(ePharmacyAdapterTypeFactory) ?: HideViewHolder.LAYOUT
    }
}