package com.tokopedia.shop.common.widget.bundle.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.databinding.ItemShopHomeBundleProductMultipleBinding
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeProductBundleMultiplePackageViewHolder(
        itemView: View,
        private val itemListener: MultipleProductBundleClickListener
): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_bundle_product_multiple
    }

    private var viewBinding: ItemShopHomeBundleProductMultipleBinding? by viewBinding()
    private var typographyBundleProductName: Typography? = null
    private var imageBundleProduct: ImageUnify? = null

    init {
        viewBinding?.apply {
            typographyBundleProductName = tvBundleProductMultipleName
            imageBundleProduct = ivBundleProductMultiple
        }
    }

    fun bind(
            bundleProductItem: ShopHomeBundleProductUiModel,
            bundleDetail: ShopHomeProductBundleDetailUiModel,
            bundleParent: ShopHomeProductBundleItemUiModel,
            bundlePosition: Int
    ) {
        imageBundleProduct?.loadImage(bundleProductItem.productImageUrl)
        typographyBundleProductName?.text = bundleProductItem.productName

        itemView.setOnClickListener {
            itemListener.onMultipleBundleProductClicked(
                    bundleProductItem,
                    bundleDetail,
                    bundleParent.bundleName,
                    bundlePosition
            )
        }
    }

}

interface MultipleProductBundleClickListener {
    fun onMultipleBundleProductClicked(
            selectedProduct: ShopHomeBundleProductUiModel,
            selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
            bundleName: String,
            bundlePosition: Int
    )
    fun addMultipleBundleToCart(
            selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
            productDetails: List<ShopHomeBundleProductUiModel>,
            bundleName: String,
    )
}