package com.tkpd.atc_variant.views.adapter.variantitem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.atc_variant.R
import com.tkpd.atc_variant.views.AtcVariantListener
import com.tkpd.atc_variant.views.viewholder.item.ItemContainerViewHolder
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory

/**
 * Created by Yehezkiel on 08/03/20
 */
class AtcVariantContainerAdapter(val listener: AtcVariantListener) : RecyclerView.Adapter<ItemContainerViewHolder>() {

    var variantContainerData: List<VariantCategory> = listOf()

    fun setData(data: List<VariantCategory>) {
        variantContainerData = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemContainerViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_atc_variant_container_view_holder, parent, false)
        return ItemContainerViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ItemContainerViewHolder, position: Int) {
        holder.bind(variantContainerData[position])
    }

    override fun onBindViewHolder(holder: ItemContainerViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        holder.bind(variantContainerData[position], true)
    }

    override fun getItemCount(): Int = variantContainerData.size
}