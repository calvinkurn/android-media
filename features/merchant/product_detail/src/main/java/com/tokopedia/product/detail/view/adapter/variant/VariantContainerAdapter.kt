package com.tokopedia.product.detail.view.adapter.variant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.variant.VariantCategory
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_variant_container_view_holder.view.*

/**
 * Created by Yehezkiel on 2020-02-27
 */
class VariantContainerAdapter(val listener: DynamicProductDetailListener) : ListAdapter<VariantCategory, VariantContainerAdapter.VariantContainerViewHolder>(VariantContainerDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantContainerViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_variant_container_view_holder, parent, false)
        return VariantContainerViewHolder(view)
    }

    override fun onBindViewHolder(holder: VariantContainerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VariantContainerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: VariantCategory) = with(view) {
            txtVariantCategoryName.text = context.getString(R.string.variant_option_builder_1, data.identifier)

            if (data.variantGuideline.isNotEmpty()) {
                txtVariantGuideline.show()
            } else {
                txtVariantGuideline.hide()
            }

            if (data.selectedValue.isNotEmpty()) {
                txtVariantSelectedOption.text = data.selectedValue
            } else {
                txtVariantSelectedOption.text = context.getString(R.string.variant_option_builder_2, data.variantOptions.size)
            }
        }
    }

    class VariantContainerDiffUtil : DiffUtil.ItemCallback<VariantCategory>() {
        override fun areItemsTheSame(oldItem: VariantCategory, newItem: VariantCategory): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: VariantCategory, newItem: VariantCategory): Boolean {
            return oldItem.variantOptions == newItem.variantOptions
        }

    }
}