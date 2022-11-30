package com.tokopedia.product.estimasiongkir.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ItemProductShippingSellyBinding
import com.tokopedia.product.detail.databinding.ItemSellyTimeBinding
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingSellyDataModel

class ProductShippingSellyViewHolder(
    view: View
) : AbstractViewHolder<ProductShippingSellyDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_product_shipping_selly
    }

    private val binding = ItemProductShippingSellyBinding.bind(view)
    private var isShow = false
    private var childHeight = 0

    override fun bind(element: ProductShippingSellyDataModel) {

        repeat(10) {
            val childView = ItemSellyTimeBinding.inflate(LayoutInflater.from(binding.root.context))
            childView.root.addOneTimeGlobalLayoutListener {
                childHeight = childView.root.height
            }
            binding.pdpSellyTimeList.addView(
                childView.root
            )
        }
        binding.pdpSellyTimeList.setOnClickListener { toggle() }
    }

    private fun toggle() {
        val view = binding.pdpSellyTimeList
        val targetHeight = if (isShow) childHeight * 2
        else WRAP_CONTENT

        view.layoutParams.height = targetHeight
        view.requestLayout()

        isShow = !isShow
    }


}
