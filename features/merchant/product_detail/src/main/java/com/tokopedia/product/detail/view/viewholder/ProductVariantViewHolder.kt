package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.variant.VariantDataModel
import com.tokopedia.product.detail.view.adapter.variant.VariantContainerAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_variant_container_view_holder.view.*

/**
 * Created by Yehezkiel on 2020-02-26
 */
class ProductVariantViewHolder(val view: View,
                               val listener: DynamicProductDetailListener) : AbstractViewHolder<VariantDataModel>(view) {

    val LAYOUT = R.layout.item_variant_container_view_holder
    val containerAdapter by lazy {
        VariantContainerAdapter(listener)
    }
    override fun bind(element: VariantDataModel) {
        setupAdapter()
        element.listOfVariantCategory?.let {
            containerAdapter.submitList(it)
        }
    }

    private fun setupAdapter() = with(view) {
        rv_variant.adapter = containerAdapter
    }

}