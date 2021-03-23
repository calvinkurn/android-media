package com.tokopedia.digital.home.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.digital.home.model.RechargeHomepageSectionModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.home_component.visitable.HomeComponentVisitable

class RechargeHomepageDiffUtil(private val oldList: List<Visitable<*>>, private val newList: List<Visitable<*>>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return when {
            oldItem is RechargeHomepageSectionModel && newItem is RechargeHomepageSectionModel -> {
                oldItem.visitableId().equals(newItem.visitableId())
            }
            oldItem is HomeComponentVisitable && newItem is HomeComponentVisitable -> {
                (oldItem.visitableId() ?: "").equals(newItem.visitableId() ?: "")
            }
            else -> false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return when {
            oldItem is RechargeHomepageSectionModel && newItem is RechargeHomepageSectionModel -> {
                oldItem.equalsWith(newItem)
            }
            oldItem is HomeComponentVisitable && newItem is HomeComponentVisitable -> {
                // Add custom checker for DynamicLegoBannerDataModel
                if (oldItem is DynamicLegoBannerDataModel && newItem is DynamicLegoBannerDataModel) {
                    oldItem.channelModel.channelGrids == newItem.channelModel.channelGrids
                } else oldItem.equalsWith(newItem)
            }
            else -> false
        }
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }
}

