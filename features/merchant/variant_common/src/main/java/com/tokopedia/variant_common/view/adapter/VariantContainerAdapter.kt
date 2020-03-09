package com.tokopedia.variant_common.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.variant_common.R
import com.tokopedia.variant_common.model.VariantCategory
import com.tokopedia.variant_common.view.ProductVariantListener
import kotlinx.android.synthetic.main.item_variant_container_view_holder.view.*

/**
 * Created by Yehezkiel on 08/03/20
 */
class VariantContainerAdapter(val listener: ProductVariantListener) : RecyclerView.Adapter<VariantContainerAdapter.VariantContainerViewHolder>() {

    private var variantContainerData: List<VariantCategory> = listOf()
    private var variantOptionAdapter: VariantOptionAdapter? = null

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
            variantOptionAdapter = VariantOptionAdapter(listener)
            if (data.getPositionSelectedOption() > 4) {
                rv_variant.layoutManager?.scrollToPosition(data.getPositionSelectedOption())
            }
            rv_variant.adapter = variantOptionAdapter

            txtVariantCategoryName.text = context.getString(R.string.variant_option_builder_1, data.name)

            if (data.getSelectedOption() == null) {
                txtVariantSelectedOption.text = context.getString(R.string.variant_option_builder_2, data.variantOptions.size)
            } else {
                txtVariantSelectedOption.text = data.getSelectedOption()?.variantName
            }

            if (data.isLeaf && listener.getStockWording() != "") {
                txtVariantStockWording.show()
                txtVariantStockWording.text = MethodChecker.fromHtml(listener.getStockWording())
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
            variantOptionAdapter?.setData(data)
        }
    }
}