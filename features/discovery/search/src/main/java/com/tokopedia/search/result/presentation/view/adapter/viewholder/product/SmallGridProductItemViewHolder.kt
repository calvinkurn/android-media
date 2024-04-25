package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnAttachStateChangeListener
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.productcard.IProductCardView
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductCardSmallGridBinding
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import com.tokopedia.search.utils.sendEventRealtimeClickAdsByteIo
import com.tokopedia.utils.view.binding.viewBinding

class SmallGridProductItemViewHolder(
    itemView: View,
    productListener: ProductListener,
    isAutoplayEnabled: Boolean = false,
) : ProductItemViewHolder(itemView, productListener, isAutoplayEnabled) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_card_small_grid
        val LAYOUT_WITH_VIEW_STUB = R.layout.search_result_product_card_small_grid_with_viewstub

        @LayoutRes
        fun layout(isUsingViewStub: Boolean): Int {
            if (isUsingViewStub) return LAYOUT_WITH_VIEW_STUB
            return LAYOUT
        }
    }

    private var binding: SearchResultProductCardSmallGridBinding? by viewBinding()

    override val productCardView: IProductCardView?
        get() = binding?.productCardView

    init {
        itemView.addOnAttachStateChangeListener(
            onViewAttachedToWindow = { onViewAttachedToWindow(elementItem) },
            onViewDetachedFromWindow = { onViewDetachedFromWindow(elementItem, visiblePercentage) }
        )
    }

    override fun bind(productItemData: ProductItemDataView?) {
        this.elementItem = productItemData
        if (productItemData == null) return
        elementItem = productItemData

        val productCardView = binding?.productCardView ?: return
        val productCardModel =
            productItemData.toProductCardModel(
                productItemData.getProductImage(),
                false,
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

        productCardView.setImageProductViewHintListener(
            productItemData,
            createImageProductViewHintListener(productItemData)
        )

        productCardView.setAddToCartOnClickListener {
            productListener.onAddToCartClick(productItemData)
        }

        productCardView.addOnImpression1pxListener(productItemData.byteIOImpressHolder) {
            productListener.onProductImpressedByteIO(productItemData)
        }
    }

    override fun onViewAttachedToWindow(element: ProductItemDataView?) {
        if (element?.isAds == true) {
            AppLogTopAds.sendEventShow(
                itemView.context,
                element.asAdsLogShowModel()
            )
        }
    }

    override fun onViewDetachedFromWindow(element: ProductItemDataView?, visiblePercentage: Int) {
        if (element?.isAds == true) {
            AppLogTopAds.sendEventShowOver(
                itemView.context,
                element.asAdsLogShowOverModel(visiblePercentage)
            )
            setVisiblePercentage(Int.ZERO)
        }
    }

    private fun ProductItemDataView.getProductImage(): String {
        return if (getProductListTypeEnum().needBiggerImage(this))
            imageUrl700
        else
            imageUrl300
    }

    private fun ProductCardModel.ProductListType.needBiggerImage(
        productItemDataView: ProductItemDataView
    ): Boolean {
        return this == ProductCardModel.ProductListType.LONG_IMAGE
            || (this == ProductCardModel.ProductListType.PORTRAIT && productItemDataView.isPortrait)
    }

    private fun ProductItemDataView.getProductListTypeEnum(): ProductCardModel.ProductListType {
        return when (productListType) {
            SearchConstant.ProductListType.VAR_REPOSITION -> ProductCardModel.ProductListType.REPOSITION
            SearchConstant.ProductListType.VAR_LONG_IMG -> ProductCardModel.ProductListType.LONG_IMAGE
            SearchConstant.ProductListType.GIMMICK -> ProductCardModel.ProductListType.GIMMICK
            SearchConstant.ProductListType.PORTRAIT -> ProductCardModel.ProductListType.PORTRAIT
            SearchConstant.ProductListType.ETA -> ProductCardModel.ProductListType.ETA
            SearchConstant.ProductListType.FIXED_GRID -> ProductCardModel.ProductListType.FIXED_GRID
            else -> ProductCardModel.ProductListType.CONTROL
        }
    }
}
