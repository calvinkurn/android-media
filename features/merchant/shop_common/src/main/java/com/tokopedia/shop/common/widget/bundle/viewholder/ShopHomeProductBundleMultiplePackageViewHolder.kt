package com.tokopedia.shop.common.widget.bundle.viewholder

import android.util.TypedValue
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
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
        private val itemListener: MultipleProductBundleClickListener,
        private val bundleProductsSize: Int
): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_bundle_product_multiple

        private const val MIN_BUNDLE_PRODUCTS_SIZE = 2
        private const val WIDTH_FOR_MIN_BUNDLE_PRODUCTS_SIZE = 120f
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
            bundlePosition: Int
    ) {
        imageBundleProduct?.loadImage(bundleProductItem.productImageUrl)
        typographyBundleProductName?.text = bundleProductItem.productName

        if (bundleProductsSize == MIN_BUNDLE_PRODUCTS_SIZE) {
            // change widget container width if bundle products size is 2
            val constraintSet = ConstraintSet()
            val params = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    WIDTH_FOR_MIN_BUNDLE_PRODUCTS_SIZE,
                    itemView.resources.displayMetrics
            ).toInt()
            bundleProductsContainer?.layoutParams?.width = params
            constraintSet.clone(bundleProductsContainer)
            constraintSet.applyTo(bundleProductsContainer)
        }

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