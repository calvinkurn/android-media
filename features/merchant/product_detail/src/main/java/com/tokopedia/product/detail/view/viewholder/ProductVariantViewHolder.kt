package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.variant.VariantDataModel
import com.tokopedia.variant_common.util.VariantItemDecorator
import com.tokopedia.variant_common.view.ProductVariantListener
import com.tokopedia.variant_common.view.adapter.VariantContainerAdapter
import kotlinx.android.synthetic.main.item_product_variant_view_holder.view.*

/**
 * Created by Yehezkiel on 2020-02-26
 */
class ProductVariantViewHolder(val view: View,
                               val listener: ProductVariantListener) : AbstractViewHolder<VariantDataModel>(view) {

    private var containerAdapter: VariantContainerAdapter? = null

    companion object {
        val LAYOUT = R.layout.item_product_variant_view_holder
    }

    override fun bind(element: VariantDataModel) {
        with(view) {
            containerAdapter = VariantContainerAdapter(listener)
            rvContainerVariant.adapter = containerAdapter
            rvContainerVariant.addItemDecoration(VariantItemDecorator(MethodChecker.getDrawable(view.context,R.drawable.bg_separator_variant)))
            rvContainerVariant.itemAnimator = null
            element.listOfVariantCategory?.let {
                containerAdapter?.setData(it)
            }
        }
    }
}