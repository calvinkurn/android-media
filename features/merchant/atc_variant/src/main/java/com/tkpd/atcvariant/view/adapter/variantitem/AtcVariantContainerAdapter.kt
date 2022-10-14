package com.tkpd.atcvariant.view.adapter.variantitem

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.atcvariant.view.viewholder.item.ItemContainerViewHolder
import com.tkpd.atcvariant.view.viewholder.item.ItemContainerChipGroupViewHolder
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.view.AtcVariantListener

/**
 * Created by Yehezkiel on 08/03/20
 */
class AtcVariantContainerAdapter(val listener: AtcVariantListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var variantContainerData: List<VariantCategory> = listOf()

    fun setData(data: List<VariantCategory>) {
        variantContainerData = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (listener.showVariantWithChipGroup()) {
            ItemContainerChipGroupViewHolder.create(parent, listener)
        } else {
            ItemContainerViewHolder.create(parent, listener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemContainerChipGroupViewHolder -> holder.bind(variantContainerData[position])
            is ItemContainerViewHolder -> holder.bind(variantContainerData[position])
        }
    }

    override fun getItemCount(): Int = variantContainerData.size
}
