package com.tokopedia.shop.common.widget.bundle.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.databinding.ItemShopHomeBundleProductSingleBinding
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleDetailUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeProductBundleSinglePackageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_bundle_product_single
    }

    private var viewBinding: ItemShopHomeBundleProductSingleBinding? by viewBinding()
    private var typographyVariantPackage: Typography? = null
    private var variantPackageContainer: ConstraintLayout? = null

    init {
        viewBinding?.apply {
            typographyVariantPackage = tvSingleBundleVariantPackage
            variantPackageContainer = singleBundleVariantPackageContainer
        }
    }

    fun bind(singleBundleDetailVariant: ShopHomeProductBundleDetailUiModel) {
        typographyVariantPackage?.text = singleBundleDetailVariant.minOrderWording
        if (singleBundleDetailVariant.isSelected) {
            itemView.background = itemView.context.getDrawable(R.drawable.bg_product_bundle_item_single_package_active)
        } else {
            itemView.background = itemView.context.getDrawable(R.drawable.bg_product_bundle_item_single_package_inactive)
        }
    }
}