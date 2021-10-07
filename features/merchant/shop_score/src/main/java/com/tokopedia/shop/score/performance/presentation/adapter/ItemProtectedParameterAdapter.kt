package com.tokopedia.shop.score.performance.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemParameterProtectedDetailBinding
import com.tokopedia.shop.score.performance.presentation.model.ItemProtectedParameterUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemProtectedParameterAdapter :
    RecyclerView.Adapter<ItemProtectedParameterAdapter.ItemProtectedParameterViewHolder>() {

    private val itemProtectedParameterList = mutableListOf<ItemProtectedParameterUiModel>()

    fun setProtectedParameterList(protectedParameterList: List<ItemProtectedParameterUiModel>?) {
        if (protectedParameterList.isNullOrEmpty()) return
        itemProtectedParameterList.clear()
        itemProtectedParameterList.addAll(protectedParameterList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemProtectedParameterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_parameter_protected_detail, parent, false)
        return ItemProtectedParameterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemProtectedParameterViewHolder, position: Int) {
        val data = itemProtectedParameterList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = itemProtectedParameterList.size

    inner class ItemProtectedParameterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemParameterProtectedDetailBinding? by viewBinding()
        fun bind(data: ItemProtectedParameterUiModel) {
            binding?.run {
                tvTitleParameterReliefDetail.text = data.parameterTitle
            }
        }
    }
}