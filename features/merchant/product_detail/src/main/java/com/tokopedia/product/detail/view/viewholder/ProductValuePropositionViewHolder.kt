package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductValuePropositionDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_pdp_value_prop.view.*

class ProductValuePropositionViewHolder(val view: View,
                                        val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductValuePropositionDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_pdp_value_prop
    }

    override fun bind(element: ProductValuePropositionDataModel?) {
        renderValueProposition(element?.isOfficialStore ?: false)
    }

    private fun renderValueProposition(isOfficialStore: Boolean) {
        if (isOfficialStore) {
            view.layout_value_proposition.show()
            view.container_ori.setOnClickListener {
                listener.onValuePropositionClicked(R.id.container_ori)
            }

            view.container_guarantee_7_days.setOnClickListener {
                listener.onValuePropositionClicked(R.id.container_guarantee_7_days)
            }

            view.container_ready.setOnClickListener {
                listener.onValuePropositionClicked(R.id.container_ready)
            }

        }
    }

}