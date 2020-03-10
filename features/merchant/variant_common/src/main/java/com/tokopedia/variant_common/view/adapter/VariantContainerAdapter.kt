package com.tokopedia.variant_common.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.variant_common.R
import com.tokopedia.variant_common.model.VariantCategory
import com.tokopedia.variant_common.view.ProductVariantListener
import com.tokopedia.variant_common.view.holder.VariantContainerViewHolder

/**
 * Created by Yehezkiel on 08/03/20
 */
class VariantContainerAdapter(val listener: ProductVariantListener) : RecyclerView.Adapter<VariantContainerViewHolder>() {

    private var variantContainerData: List<VariantCategory> = listOf()

    fun setData(data: List<VariantCategory>) {
        variantContainerData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantContainerViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_variant_container_view_holder, parent, false)
        return VariantContainerViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: VariantContainerViewHolder, position: Int) {
        holder.bind(variantContainerData[position])
    }

    override fun getItemCount(): Int = variantContainerData.size

}