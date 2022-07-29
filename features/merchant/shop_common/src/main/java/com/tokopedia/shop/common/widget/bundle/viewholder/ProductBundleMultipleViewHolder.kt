package com.tokopedia.shop.common.widget.bundle.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.databinding.ItemProductBundleMultipleWidgetBinding
import com.tokopedia.shop.common.widget.bundle.adapter.ProductBundleMultipleAdapter
import com.tokopedia.shop.common.widget.bundle.model.BundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleShopUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ProductBundleMultipleViewHolder(
    itemView: View,
    private val containerWidgetParams: Int
): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_bundle_multiple_widget
    }

    private var viewBinding: ItemProductBundleMultipleWidgetBinding? by viewBinding()
    private var typographyBundleName: Typography? = null
    private var typographyBundleProductDisplayPrice: Typography? = null
    private var typographyBundleProductOriginalPrice: Typography? = null
    private var typographyBundleProductSavingAmount: Typography? = null
    private var typographyBundlePreOrder: Typography? = null
    private var buttonAtc: UnifyButton? = null
    private var labelBundleDiscount: Label? = null
    private var rvBundleProducts: RecyclerView? = null
    private var widgetContainer: ConstraintLayout? = null

    init {
        viewBinding?.apply {
            typographyBundleName = tvBundleName
            typographyBundleProductDisplayPrice = tvBundleDisplayPrice
            typographyBundleProductOriginalPrice = tvBundleOriginalPrice
            typographyBundlePreOrder = tvBundlePreorder
            labelBundleDiscount = labelDiscountBundle
            typographyBundleProductSavingAmount = tvSavingAmountPriceWording
            buttonAtc = btnBundleAtc
            rvBundleProducts = rvMultipleBundleProducts
            widgetContainer = bundleWidgetContainer
        }
    }

    fun bind(bundle: BundleUiModel) {
        val bundleDetail = bundle.bundleDetails.firstOrNull() ?: BundleDetailUiModel()

        // bundle card item details
        typographyBundleName?.text = bundle.bundleName
        typographyBundleProductDisplayPrice?.text = bundleDetail.displayPrice
        typographyBundleProductOriginalPrice?.text = bundleDetail.originalPrice
        labelBundleDiscount?.setLabel("${bundleDetail.discountPercentage}%")
        typographyBundleProductOriginalPrice?.apply {
            text = bundleDetail.originalPrice
            paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        typographyBundleProductSavingAmount?.text = HtmlLinkHelper(itemView.context, bundleDetail.savingAmountWording).spannedString
        typographyBundlePreOrder?.shouldShowWithAction(bundleDetail.isPreOrder) {
            typographyBundlePreOrder?.text = bundleDetail.preOrderInfo
        }
        buttonAtc?.text = if (bundleDetail.isPreOrder) {
            itemView.context.getString(R.string.shop_page_product_bundle_preorder_button_text)
        } else {
            itemView.context.getString(R.string.product_bundle_action_button_text)
        }

        initShopInfo(bundleDetail.shopInfo)
        initBundleProductsRecyclerView(bundleDetail.products.size, bundle, bundleDetail)
    }

    private fun initShopInfo(shopInfo: BundleShopUiModel?) {
        if (shopInfo != null) {
            viewBinding?.apply {
                iconShop.loadImage(shopInfo.shopIconUrl)
                tvShopName.text = shopInfo.shopName
            }
        }
    }

    private fun initBundleProductsRecyclerView(
        spanSize: Int,
        bundle: BundleUiModel,
        bundleDetail: BundleDetailUiModel
    ) {
        rvBundleProducts?.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, spanSize, GridLayoutManager.VERTICAL, false)
            adapter = ProductBundleMultipleAdapter()
        }
        (rvBundleProducts?.adapter as ProductBundleMultipleAdapter).updateDataSet(
            newList = bundleDetail.products,
            bundleDetail = bundleDetail,
            bundleParent = bundle
        )

        val constraintSet = ConstraintSet()
        widgetContainer?.layoutParams?.width = containerWidgetParams
        constraintSet.clone(widgetContainer)
        constraintSet.applyTo(widgetContainer)
    }
}