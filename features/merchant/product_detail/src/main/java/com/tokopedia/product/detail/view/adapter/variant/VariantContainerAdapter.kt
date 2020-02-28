package com.tokopedia.product.detail.view.adapter.variant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class VariantContainerAdapter(val listener: DynamicProductDetailListener) : RecyclerView.Adapter<VariantContainerAdapter.VariantContainerViewHolder>() {

    private var variantContainerData: List<VariantCategory> = listOf()

    fun setData(data: List<VariantCategory>) {
        variantContainerData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantContainerViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_variant_container_view_holder, parent, false)
        return VariantContainerViewHolder(view)
    }

    override fun onBindViewHolder(holder: VariantContainerViewHolder, position: Int) {
        holder.bind(variantContainerData[position])
    }

    override fun getItemCount(): Int = variantContainerData.size

    inner class VariantContainerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: VariantCategory) = with(view) {
            val variantOptionAdapter =  VariantOptionAdapter(listener)
            txtVariantCategoryName.text = context.getString(R.string.variant_option_builder_1, data.name)

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

            rv_variant.adapter = variantOptionAdapter
            variantOptionAdapter.setData(data)
        }
    }
}