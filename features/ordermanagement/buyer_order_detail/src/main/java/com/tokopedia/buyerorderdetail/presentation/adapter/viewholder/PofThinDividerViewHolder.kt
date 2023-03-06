package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.ItemPartialOrderFulfillmentThinDividerBinding
import com.tokopedia.buyerorderdetail.presentation.model.PofThinDividerUiModel
import com.tokopedia.unifycomponents.toPx

class PofThinDividerViewHolder(view: View?) :
    AbstractViewHolder<PofThinDividerUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_partial_order_fulfillment_thin_divider
    }

    private val binding = ItemPartialOrderFulfillmentThinDividerBinding.bind(itemView)

    override fun bind(element: PofThinDividerUiModel) {
        setupMarginDivider(element.marginHorizontal, element.marginTop)
    }

    private fun setupMarginDivider(marginHorizontal: Int?, marginTop: Int?) {
        with(binding.root) {
            val layoutParamsMargin = layoutParams as? RecyclerView.LayoutParams
            marginTop?.let {
                layoutParamsMargin?.topMargin = it.toPx()
            }
            marginHorizontal?.let { marginHorizontal ->
                layoutParamsMargin?.marginStart = marginHorizontal.toPx()
                layoutParamsMargin?.marginEnd = marginHorizontal.toPx()
            }
            layoutParams = layoutParamsMargin
        }
    }
}
