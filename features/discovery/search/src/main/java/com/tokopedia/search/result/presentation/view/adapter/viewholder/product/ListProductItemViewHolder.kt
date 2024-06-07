package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.productcard.IProductCardView
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductCardListBinding
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import com.tokopedia.search.utils.sendEventRealtimeClickAdsByteIo
import com.tokopedia.utils.view.binding.viewBinding

class ListProductItemViewHolder(
    itemView: View,
    productListener: ProductListener,
    isAutoplayEnabled: Boolean = false,
) : ProductItemViewHolder(itemView, productListener, isAutoplayEnabled) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_card_list
        val LAYOUT_WITH_VIEW_STUB = R.layout.search_result_product_card_list_with_viewstub

        @LayoutRes
        fun layout(isUsingViewStub: Boolean): Int {
            if (isUsingViewStub) return LAYOUT_WITH_VIEW_STUB
            return LAYOUT
        }
    }

    private var binding: SearchResultProductCardListBinding? by viewBinding()

    override val productCardView: IProductCardView?
        get() = binding?.productCardView

    override fun bind(productItemData: ProductItemDataView?) {
        if (productItemData == null) return

        val productCardView = binding?.productCardView ?: return
        val productCardModel =
            productItemData.toProductCardModel(
                productItemData.imageUrl,
                true,
                productItemData.getProductListTypeEnum()
            )

        registerLifecycleObserver(productCardModel)

        productCardView.setProductModel(productCardModel)

        productCardView.setThreeDotsOnClickListener {
            productListener.onThreeDotsClick(productItemData, adapterPosition)
        }

        productCardView.setOnLongClickListener {
            productListener.onThreeDotsClick(productItemData, adapterPosition)
            true
        }

        productCardView.setOnClickListener(object: ProductCardClickListener {
            override fun onClick(v: View) {
                productListener.onItemClicked(productItemData, adapterPosition)
            }

            override fun onAreaClicked(v: View) {
                sendEventRealtimeClickAdsByteIo(itemView.context, productItemData, AdsLogConst.Refer.AREA)
            }

            override fun onProductImageClicked(v: View) {
                sendEventRealtimeClickAdsByteIo(itemView.context, productItemData, AdsLogConst.Refer.COVER)
            }

            override fun onSellerInfoClicked(v: View) {
                sendEventRealtimeClickAdsByteIo(itemView.context, productItemData, AdsLogConst.Refer.SELLER_NAME)
            }
        })

        productCardView.setAddToCartOnClickListener {
            productListener.onAddToCartClick(productItemData)
        }

        productCardView.setImageProductViewHintListener(
            productItemData,
            createImageProductViewHintListener(productItemData)
        )

        productCardView.addOnImpression1pxListener(productItemData.byteIOImpressHolder) {
            productListener.onProductImpressedByteIO(productItemData)
        }
    }

    private fun ProductItemDataView.getProductListTypeEnum(): ProductCardModel.ProductListType {
        return when (productListType) {
            SearchConstant.ProductListType.LIST_VIEW -> ProductCardModel.ProductListType.LIST_VIEW
            else -> ProductCardModel.ProductListType.CONTROL
        }
    }
}
