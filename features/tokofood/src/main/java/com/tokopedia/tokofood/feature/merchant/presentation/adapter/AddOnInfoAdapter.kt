package com.tokopedia.tokofood.feature.merchant.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokofood.databinding.TokofoodItemAddOnInfoLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomListItem
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.AddOnInfoViewHolder

class AddOnInfoAdapter : RecyclerView.Adapter<AddOnInfoViewHolder>() {

    private var customListItems: List<CustomListItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddOnInfoViewHolder {
        val binding = TokofoodItemAddOnInfoLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddOnInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddOnInfoViewHolder, position: Int) {
        customListItems[position].addOnUiModel?.run {
            holder.bindData(this)
        }
    }

    override fun getItemCount(): Int {
        return customListItems.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCustomListItems(customListItems: List<CustomListItem>) {
        this.customListItems = customListItems.filter {
            it.addOnUiModel?.selectedAddOns?.isNotEmpty() == true
        }
        notifyDataSetChanged()
    }
}