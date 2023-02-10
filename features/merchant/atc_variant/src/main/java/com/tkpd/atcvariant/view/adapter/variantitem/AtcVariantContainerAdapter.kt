package com.tkpd.atcvariant.view.adapter.variantitem

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.atcvariant.view.viewholder.item.ItemContainerViewHolder
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.view.AtcVariantListener

/**
 * Created by Yehezkiel on 08/03/20
 */
class AtcVariantContainerAdapter(val listener: AtcVariantListener) :
    RecyclerView.Adapter<ItemContainerViewHolder>() {

    var variantContainerData: List<VariantCategory> = listOf()

    fun setData(data: List<VariantCategory>) {
        variantContainerData = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemContainerViewHolder {
        return ItemContainerViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: ItemContainerViewHolder, position: Int) {
        holder.bind(variantContainerData[position])
    }

    override fun getItemCount(): Int = variantContainerData.size
}
