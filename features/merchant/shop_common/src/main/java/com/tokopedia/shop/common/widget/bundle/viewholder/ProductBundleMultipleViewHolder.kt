package com.tokopedia.shop.common.widget.bundle.viewholder

import android.graphics.Paint
import android.util.TypedValue
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.databinding.ItemProductBundleMultipleWidgetBinding
import com.tokopedia.shop.common.widget.bundle.adapter.ProductBundleMultipleAdapter
import com.tokopedia.shop.common.widget.bundle.adapter.ShopHomeProductBundleWidgetAdapter
import com.tokopedia.shop.common.widget.bundle.model.BundleDetailUiModel
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

    fun bind(bundle: BundleUiModel, size: Int) {
        val multipleBundleItem = bundle.bundles.firstOrNull() ?: BundleDetailUiModel()

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
        typographyBundlePreOrder?.shouldShowWithAction(multipleBundleItem.isPreOrder) {
            typographyBundlePreOrder?.text = multipleBundleItem.preOrderInfo
        }
        buttonAtc?.text = if (multipleBundleItem.isPreOrder) {
            itemView.context.getString(R.string.shop_page_product_bundle_preorder_button_text)
        } else {
            itemView.context.getString(R.string.product_bundle_action_button_text)
        }

        // bundle products list
        initBundleProductsRecyclerView(multipleBundleItem.products.size)
        (rvBundleProducts?.adapter as ProductBundleMultipleAdapter).updateDataSet(
                newList = multipleBundleItem.products,
                bundleDetail = multipleBundleItem,
                bundleParent = bundle
        )
    }

    private fun initBundleProductsRecyclerView(spanSize: Int) {
        rvBundleProducts?.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, spanSize, GridLayoutManager.VERTICAL, false)
            adapter = ProductBundleMultipleAdapter()
        }

        val constraintSet = ConstraintSet()
        widgetContainer?.layoutParams?.width = containerWidgetParams
        constraintSet.clone(widgetContainer)
        constraintSet.applyTo(widgetContainer)
    }
}