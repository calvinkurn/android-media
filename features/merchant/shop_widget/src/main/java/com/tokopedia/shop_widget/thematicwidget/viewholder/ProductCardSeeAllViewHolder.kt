package com.tokopedia.shop_widget.thematicwidget.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardSeeAllUiModel
import com.tokopedia.shop_widget.databinding.ItemProductCardSeeAllBinding
import com.tokopedia.utils.view.binding.viewBinding

class ProductCardSeeAllViewHolder(
    itemView: View,
    private var listener: ProductCardSeeAllListener? = null
): AbstractViewHolder<ProductCardSeeAllUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_card_see_all
    }

    private var binding: ItemProductCardSeeAllBinding? by viewBinding()

    override fun bind(element: ProductCardSeeAllUiModel) {
        binding?.apply {
            cvSeeMore.setOnClickListener {
                listener?.onProductCardSeeAllClickListener(element.appLink)
            }
            iuChevron.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500), BlendModeCompat.SRC_ATOP)
        }
    }

    interface ProductCardSeeAllListener {
        fun onProductCardSeeAllClickListener(appLink: String)
    }
}