package com.tokopedia.shop.common.widget.bundle.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.databinding.ItemShopHomeBundleProductMultipleBinding
import com.tokopedia.shop.common.widget.bundle.model.*
import com.tokopedia.shop.common.widget.model.ShopHomeWidgetLayout
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeProductBundleMultiplePackageViewHolder(
        itemView: View,
        private val itemListener: MultipleProductBundleListener? = null
): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_bundle_product_multiple
    }

    private var viewBinding: ItemShopHomeBundleProductMultipleBinding? by viewBinding()
    private var typographyBundleProductName: Typography? = null
    private var imageBundleProduct: ImageUnify? = null
    private var bundleProductsContainer: ConstraintLayout? = null

    init {
        viewBinding?.apply {
            typographyBundleProductName = tvBundleProductMultipleName
            imageBundleProduct = ivBundleProductMultiple
            bundleProductsContainer = multipleBundleProductsContainer
        }
    }

    fun bind(
            bundleProductItem: ShopHomeBundleProductUiModel,
            bundleDetail: ShopHomeProductBundleDetailUiModel,
            bundleParent: ShopHomeProductBundleItemUiModel,
            bundlePosition: Int,
            widgetTitle: String,
            widgetName: String
    ) {
        imageBundleProduct?.loadImage(bundleProductItem.productImageUrl)
        typographyBundleProductName?.text = bundleProductItem.productName
        itemView.addOnImpressionListener(bundleProductItem){
            itemListener?.impressionProductItemBundleMultiple(
                bundleProductItem,
                bundleDetail,
                bundleParent.bundleName,
                bundlePosition,
                widgetTitle,
                widgetName,
                adapterPosition
            )
        }
        itemView.setOnClickListener {
            itemListener?.onMultipleBundleProductClicked(
                bundleProductItem,
                bundleDetail,
                bundleParent.bundleName,
                bundlePosition,
                widgetTitle,
                widgetName,
                adapterPosition
            )
        }
    }

    fun bind(
        bundleProductItem: BundleProductUiModel,
        onViewImpression: (position: Int) -> Unit,
        onClickImpression: (position: Int) -> Unit
    ) {
        imageBundleProduct?.loadImage(bundleProductItem.productImageUrl)
        typographyBundleProductName?.text = bundleProductItem.productName
        itemView.addOnImpressionListener(bundleProductItem) { onViewImpression.invoke(adapterPosition) }
        itemView.setOnClickListener { onClickImpression.invoke(adapterPosition) }
    }
}

interface MultipleProductBundleListener {
    fun onMultipleBundleProductClicked(
            selectedProduct: ShopHomeBundleProductUiModel,
            selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
            bundleName: String,
            bundlePosition: Int,
            widgetTitle: String,
            widgetName: String,
            productItemPosition: Int
    )
    fun addMultipleBundleToCart(
            selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
            bundleListSize: Int,
            productDetails: List<ShopHomeBundleProductUiModel>,
            bundleName: String,
            widgetLayout: ShopHomeWidgetLayout
    )
    fun impressionProductBundleMultiple(
            selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
            bundleName: String,
            bundlePosition: Int,
    )

    fun impressionProductItemBundleMultiple(
        selectedProduct: ShopHomeBundleProductUiModel,
        selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
        bundleName: String,
        bundlePosition: Int,
        widgetTitle: String,
        widgetName: String,
        productItemPosition: Int
    )
}