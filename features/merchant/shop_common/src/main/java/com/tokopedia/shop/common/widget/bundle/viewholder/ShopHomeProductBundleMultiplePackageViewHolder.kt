package com.tokopedia.shop.common.widget.bundle.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.databinding.ItemShopHomeBundleProductMultipleBinding
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel
import com.tokopedia.shop.common.widget.model.ShopHomeWidgetLayout
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeProductBundleMultiplePackageViewHolder(
        itemView: View,
        private val itemListener: MultipleProductBundleListener
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
            itemListener.impressionProductItemBundleMultiple(
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
            itemListener.onMultipleBundleProductClicked(
                bundleParent.shopId,
                bundleParent.warehouseId,
                bundleProductItem,
                bundleDetail,
                bundleParent.bundleName,
                bundleParent.bundleType,
                bundlePosition,
                widgetTitle,
                widgetName,
                adapterPosition
            )
        }
    }
}

interface MultipleProductBundleListener {
    fun onMultipleBundleProductClicked(
            shopId: String,
            warehouseId: String,
            selectedProduct: ShopHomeBundleProductUiModel,
            selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
            bundleName: String,
            bundleType: String,
            bundlePosition: Int,
            widgetTitle: String,
            widgetName: String,
            productItemPosition: Int
    )
    fun addMultipleBundleToCart(
            shopId: String,
            warehouseId: String,
            selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
            bundleListSize: Int,
            productDetails: List<ShopHomeBundleProductUiModel>,
            bundleName: String,
            bundleType: String,
            bundlePosition: Int,
            widgetLayout: ShopHomeWidgetLayout,
            bundleGroupId: String
    )
    fun impressionProductBundleMultiple(
            shopId: String,
            warehouseId: String,
            selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
            bundleName: String,
            bundleType: String,
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