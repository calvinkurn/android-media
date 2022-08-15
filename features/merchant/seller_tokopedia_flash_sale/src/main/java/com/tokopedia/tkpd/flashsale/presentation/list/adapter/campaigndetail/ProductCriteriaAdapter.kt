package com.tokopedia.tkpd.flashsale.presentation.list.adapter.campaigndetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemProductCriteriaBinding

class ProductCriteriaAdapter: RecyclerView.Adapter<ProductCriteriaAdapter.CriteriaViewHolder>() {

    var data: List<String> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriteriaViewHolder {
        val binding = StfsItemProductCriteriaBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return CriteriaViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CriteriaViewHolder, position: Int) {
        data.getOrNull(position)?.let { menu ->
            holder.bind(menu)
        }
    }

    inner class CriteriaViewHolder(private val binding: StfsItemProductCriteriaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.iconExpand.setOnClickListener {
                binding.layoutContent.root.apply {
                    isVisible = !isVisible
                    setExpandIcon(isVisible)
                }
            }
        }

        private fun setExpandIcon(isContentVisible: Boolean) {
            binding.iconExpand.setImage(if (isContentVisible) {
                IconUnify.CHEVRON_UP
            } else {
                IconUnify.CHEVRON_DOWN
            })
        }

        fun bind(title: String) {
            binding.tfCategoryTitle.text = title
            binding.tfProductCount.text = title
        }
    }
}