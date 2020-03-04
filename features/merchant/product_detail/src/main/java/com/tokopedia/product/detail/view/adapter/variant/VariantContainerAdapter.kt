package com.tokopedia.product.detail.view.adapter.variant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.variant.VariantCategory
import com.tokopedia.product.detail.view.listener.ProductVariantListener
import kotlinx.android.synthetic.main.item_variant_container_view_holder.view.*

/**
 * Created by Yehezkiel on 2020-02-27
 */
class VariantContainerAdapter(val listener: ProductVariantListener) : RecyclerView.Adapter<VariantContainerAdapter.VariantContainerViewHolder>() {

    private var variantContainerData: List<VariantCategory> = listOf()
    private var variantOptionAdapter: VariantOptionAdapter? = null

    fun setData(data: List<VariantCategory>) {
        variantContainerData = data
        notifyDataSetChanged()
    }

    fun setDataWithPayload(data: List<VariantCategory>?) {
        data?.let {
            variantContainerData = it
        }
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

    override fun onBindViewHolder(holder: VariantContainerViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            holder.bind(variantContainerData[position], payloads.firstOrNull() as? Int ?: 0)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount(): Int = variantContainerData.size

    inner class VariantContainerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: VariantCategory) = with(view) {
            variantOptionAdapter = VariantOptionAdapter(listener)
            txtVariantCategoryName.text = context.getString(R.string.variant_option_builder_1, data.name)

            if (data.getSelectedOption() == null) {
                txtVariantSelectedOption.text = context.getString(R.string.variant_option_builder_2, data.variantOptions.size)
            } else {
                txtVariantSelectedOption.text = data.getSelectedOption()?.variantName
            }

            if (data.isLeaf) {
                txtVariantStockWording.show()
                txtVariantStockWording.text = listener.getStockWording()
            } else {
                txtVariantStockWording.hide()
            }

            if (data.variantGuideline.isNotEmpty()) {
                txtVariantGuideline.show()
                txtVariantGuideline.setOnClickListener {
                    listener.onVariantGuideLineClicked(data.variantGuideline)
                }
            } else {
                txtVariantGuideline.hide()
            }

            rv_variant.itemAnimator = null
            rv_variant.adapter = variantOptionAdapter
            variantOptionAdapter?.setData(data)
        }

        fun bind(data: VariantCategory, payload: Int) = with(view) {
            rv_variant.itemAnimator = null
            variantOptionAdapter?.setDataWithPayload(data)
        }
    }
}