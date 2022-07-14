package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingThinDividerFullBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ThinDividerUiModel
import com.tokopedia.unifycomponents.toPx

class ThinDividerViewHolder(view: View): AbstractViewHolder<ThinDividerUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_thin_divider_full
    }

    private val binding = ItemTokofoodOrderTrackingThinDividerFullBinding.bind(itemView)

    override fun bind(element: ThinDividerUiModel) {
        element.marginTop?.let {
            with(binding.thinDividerFull) {
                val layoutParamsMargin = layoutParams as? RecyclerView.LayoutParams
                layoutParamsMargin?.topMargin = it.toPx()
                layoutParams = layoutParamsMargin
            }
        }
    }

}