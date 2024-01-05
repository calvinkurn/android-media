package com.tokopedia.deals.ui.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.databinding.ItemDealsCategoryBinding
import com.tokopedia.deals.ui.home.listener.DealsCategoryListener
import com.tokopedia.deals.ui.home.ui.adapter.viewholder.DealsCategoryViewHolder
import com.tokopedia.deals.ui.home.ui.dataview.DealsCategoryDataView

class DealsCategoryAdapter(private val dealsCategoryListener: DealsCategoryListener) :
    RecyclerView.Adapter<DealsCategoryViewHolder>() {

    var isEnableSeeMoreCategories: Boolean = false
    var maxItem = 0

    var dealsCategories: MutableList<DealsCategoryDataView> = mutableListOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(DealsCategoryDiffCallback(field, value))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealsCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDealsCategoryBinding.inflate(inflater, parent, false)
        return DealsCategoryViewHolder(binding)
    }

    fun enableSeeMoreCategory(maxItem: Int) {
        isEnableSeeMoreCategories = true
        this.maxItem = maxItem
        notifyItemInserted(itemCount + 1)
    }

    override fun getItemCount(): Int {
        return if (isEnableSeeMoreCategories) maxItem else dealsCategories.size
    }

    override fun onBindViewHolder(holder: DealsCategoryViewHolder, position: Int) {
        if (isEnableSeeMoreCategories && position == itemCount - 1) {
            holder.bindOnSeeAllCategories(dealsCategories, dealsCategoryListener)
        }
        else holder.bindData(dealsCategories[position], dealsCategoryListener)
    }

    class DealsCategoryDiffCallback(
        private val oldDealsCategories: List<DealsCategoryDataView>,
        private val newDealsCategories: List<DealsCategoryDataView>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldDealsCategories.size

        override fun getNewListSize(): Int = newDealsCategories.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldDealsCategories[oldItemPosition].id == newDealsCategories[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldDealsCategories[oldItemPosition] == newDealsCategories[newItemPosition]
        }
    }
}
