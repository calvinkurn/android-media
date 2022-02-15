package com.tokopedia.shop.common.widget.bundle.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.databinding.ItemShopHomeProductBundleMultipleWidgetBinding
import com.tokopedia.shop.common.widget.bundle.adapter.ShopHomeProductBundleMultipleAdapter
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeProductBundleMultipleViewHolder(
        itemView: View,
        private val multipleProductBundleClickListener: MultipleProductBundleClickListener
): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_bundle_multiple_widget
    }

    private var viewBinding: ItemShopHomeProductBundleMultipleWidgetBinding? by viewBinding()
    private var typographyBundleName: Typography? = null
    private var typographyBundleProductDisplayPrice: Typography? = null
    private var typographyBundleProductOriginalPrice: Typography? = null
    private var typographyBundleProductSavingAmount: Typography? = null
    private var buttonAtc: UnifyButton? = null
    private var labelBundleDiscount: Label? = null
    private var rvBundleProducts: RecyclerView? = null

    init {
        viewBinding?.apply {
            typographyBundleName = tvBundleName
            typographyBundleProductDisplayPrice = tvBundleDisplayPrice
            typographyBundleProductOriginalPrice = tvBundleOriginalPrice
            labelBundleDiscount = labelDiscountBundle
            typographyBundleProductSavingAmount = tvSavingAmountPriceWording
            buttonAtc = btnBundleAtc
            rvBundleProducts = rvMultipleBundleProducts
        }
    }

    fun bind(bundle: ShopHomeProductBundleItemUiModel) {
        val multipleBundleItem = bundle.bundleDetails.firstOrNull() ?: ShopHomeProductBundleDetailUiModel()

        // bundle card item details
        typographyBundleName?.text = bundle.bundleName
        typographyBundleProductDisplayPrice?.text = multipleBundleItem.displayPrice
        typographyBundleProductOriginalPrice?.text = multipleBundleItem.originalPrice
        labelBundleDiscount?.setLabel("${multipleBundleItem.discountPercentage}%")
        typographyBundleProductOriginalPrice?.apply {
            text = multipleBundleItem.originalPrice
            paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        typographyBundleProductSavingAmount?.text = HtmlLinkHelper(itemView.context, multipleBundleItem.savingAmountWording).spannedString

        // bundle products list
        initBundleProductsRecyclerView(spanSize = bundle.bundleProducts.size)
        (rvBundleProducts?.adapter as ShopHomeProductBundleMultipleAdapter).updateDataSet(
                newList = bundle.bundleProducts,
                bundleDetail = multipleBundleItem,
                bundleParent = bundle
        )

        // bind listeners
        buttonAtc?.setOnClickListener {
            // add to cart bundle
            multipleProductBundleClickListener.addMultipleBundleToCart(
                    multipleBundleItem,
                    bundle.bundleProducts,
                    bundle.bundleName
            )
        }
    }

    private fun initBundleProductsRecyclerView(spanSize: Int) {
        rvBundleProducts?.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, spanSize, GridLayoutManager.VERTICAL, false)
            adapter = ShopHomeProductBundleMultipleAdapter(multipleProductBundleClickListener)
        }
    }

}