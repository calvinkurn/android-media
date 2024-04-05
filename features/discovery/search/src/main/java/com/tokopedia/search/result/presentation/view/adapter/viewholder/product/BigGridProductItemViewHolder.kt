package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.analytics.byteio.topads.models.AdsLogRealtimeClickModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowOverModel
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.productcard.IProductCardView
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductCardBigGridBinding
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import com.tokopedia.utils.view.binding.viewBinding

class BigGridProductItemViewHolder(
    itemView: View,
    productListener: ProductListener,
    isAutoplayEnabled: Boolean = false,
) : ProductItemViewHolder(itemView, productListener, isAutoplayEnabled) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_card_big_grid
    }

    private var binding: SearchResultProductCardBigGridBinding? by viewBinding()

    override val productCardView: IProductCardView?
        get() = binding?.productCardView

    override fun bind(productItemData: ProductItemDataView?) {
        if (productItemData == null) return
        val productCardView = binding?.productCardView ?: return

        val productCardModel =
            productItemData.toProductCardModel(
                productItemData.imageUrl700,
                true,
                ProductCardModel.ProductListType.CONTROL
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
                AppLogTopAds.sendEventRealtimeClick(
                    itemView.context,
                    PageName.SEARCH_RESULT,
                    productItemData.asAdsLogRealtimeClickModel(AdsLogConst.Refer.AREA)
                )
            }

            override fun onProductImageClicked(v: View) {
                AppLogTopAds.sendEventRealtimeClick(
                    itemView.context,
                    PageName.SEARCH_RESULT,
                    productItemData.asAdsLogRealtimeClickModel(AdsLogConst.Refer.COVER)
                )
            }

            override fun onSellerInfoClicked(v: View) {
                AppLogTopAds.sendEventRealtimeClick(
                    itemView.context,
                    PageName.SEARCH_RESULT,
                    productItemData.asAdsLogRealtimeClickModel(AdsLogConst.Refer.SELLER_NAME)
                )
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

    override fun onViewAttachedToWindow(element: ProductItemDataView?) {
        if (element?.isTopAds == true) {
            AppLogTopAds.sendEventShow(
                itemView.context,
                PageName.SEARCH_RESULT,
                element.asAdsLogShowModel()
            )
        }
    }

    override fun onViewDetachedFromWindow(element: ProductItemDataView?, visiblePercentage: Int) {
        if (element?.isTopAds == true) {
            AppLogTopAds.sendEventShowOver(
                itemView.context,
                PageName.SEARCH_RESULT,
                element.asAdsLogShowOverModel(visiblePercentage)
            )
        }
    }
}
