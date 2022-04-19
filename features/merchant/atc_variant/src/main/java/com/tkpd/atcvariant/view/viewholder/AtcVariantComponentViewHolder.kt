package com.tkpd.atcvariant.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.atcvariant.R
import com.tkpd.atcvariant.data.uidata.VariantComponentDataModel
import com.tkpd.atcvariant.view.adapter.variantitem.AtcVariantContainerAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.common.view.AtcVariantListener

/**
 * Created by Yehezkiel on 07/05/21
 */
class AtcVariantComponentViewHolder(private val view: View, listener: AtcVariantListener) : AbstractViewHolder<VariantComponentDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.atc_variant_viewholder
    }

    private var variantContainerAdapter: AtcVariantContainerAdapter? = AtcVariantContainerAdapter(listener)
    private var rvVariant: RecyclerView? = view.findViewById(R.id.rv_variant_viewholder)

    override fun bind(element: VariantComponentDataModel) {
        element.listOfVariantCategory?.let {
            rvVariant?.adapter = variantContainerAdapter
            rvVariant?.itemAnimator = null
            variantContainerAdapter?.setData(it)
        }
    }

    override fun bind(element: VariantComponentDataModel, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        element.listOfVariantCategory?.let {
            variantContainerAdapter?.variantContainerData = it
            variantContainerAdapter?.notifyItemRangeChanged(0, it.size, 1)
        }
    }

}