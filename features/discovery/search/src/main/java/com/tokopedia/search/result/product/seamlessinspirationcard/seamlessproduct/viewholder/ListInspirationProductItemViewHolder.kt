package com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.IProductCardView
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationSeamlessProductCardListBinding
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.InspirationProductItemDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.InspirationProductListener
import com.tokopedia.utils.view.binding.viewBinding

class ListInspirationProductItemViewHolder(
    itemView: View,
    inspirationProductListener: InspirationProductListener,
    productListener: ProductListener
) : InspirationProductItemViewHolder(itemView, inspirationProductListener, productListener) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_inspiration_seamless_product_card_list
        val LAYOUT_WITH_VIEW_STUB = R.layout.search_inspiration_seamless_product_card_list_with_viewstub

        @LayoutRes
        fun layout(isUsingViewStub: Boolean): Int {
            if (isUsingViewStub) return LAYOUT_WITH_VIEW_STUB
            return LAYOUT
        }
    }

    private var binding: SearchInspirationSeamlessProductCardListBinding? by viewBinding()

    override val productCardView: IProductCardView?
        get() = binding?.productCardView

    override fun bind(productItemData: InspirationProductItemDataView?) {
        if (productItemData == null) return
        val productCardView = binding?.productCardView ?: return
        productCardView.addOnImpressionListener(productItemData) {
            inspirationProductListener.onInspirationProductItemImpressed(productItemData)
        }
        val productCardModel =
            productItemData.toProductCardModel()

        registerLifecycleObserver(productCardModel)

        productCardView.setProductModel(productCardModel)

        productCardView.setOnClickListener {
            inspirationProductListener.onInspirationProductItemClicked(productItemData)
        }

        productCardView.setThreeDotsOnClickListener {
            inspirationProductListener.onInspirationProductItemThreeDotsClicked(productItemData)
        }
    }
}
