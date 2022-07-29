package com.tokopedia.shop.common.widget.bundle.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.databinding.ItemProductBundleSingleWidgetBinding
import com.tokopedia.shop.common.widget.bundle.adapter.ProductBundleSingleAdapter
import com.tokopedia.shop.common.widget.bundle.model.BundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleProductUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ProductBundleSingleViewHolder(
    itemView: View,
    private val containerWidgetParams: Int
): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_bundle_single_widget
    }

    private var viewBinding: ItemProductBundleSingleWidgetBinding? by viewBinding()
    private var typographyBundleName: Typography? = null
    private var typographyBundlePreOrder: Typography? = null
    private var typographyBundleProductName: Typography? = null
    private var typographyBundleProductDisplayPrice: Typography? = null
    private var typographyBundleProductOriginalPrice: Typography? = null
    private var typographyBundleProductSavingAmount: Typography? = null
    private var buttonAtc: UnifyButton? = null
    private var labelBundleDiscount: Label? = null
    private var imageBundleProduct: ImageUnify? = null
    private var rvBundleDetails: RecyclerView? = null
    private var widgetContainer: ConstraintLayout? = null
    private var selectedSingleBundle: BundleDetailUiModel = BundleDetailUiModel()
    private var parentSingleBundle: BundleUiModel = BundleUiModel()

    init {
        viewBinding?.apply {
            typographyBundleName = tvBundleName
            typographyBundlePreOrder = tvBundlePreorder
            typographyBundleProductName = tvBundleProductSingleName
            typographyBundleProductDisplayPrice = tvBundleDisplayPrice
            typographyBundleProductOriginalPrice = tvBundleOriginalPrice
            typographyBundleProductSavingAmount = tvSavingAmountPriceWording
            buttonAtc = btnBundleAtc
            labelBundleDiscount = labelDiscountBundle
            imageBundleProduct = ivBundleImage
            rvBundleDetails = rvBundleSinglePackage
            widgetContainer = bundleWidgetContainer
        }
        initBundleDetailsRecyclerView()
    }

    fun bind(bundle: BundleUiModel) {
        parentSingleBundle = bundle
        selectedSingleBundle = parentSingleBundle.bundles.firstOrNull() ?: BundleDetailUiModel()
        val product = selectedSingleBundle.products.firstOrNull() ?: BundleProductUiModel()

        // bundle card item details
        renderBundlePriceDetails(selectedSingleBundle)
        typographyBundleName?.text = parentSingleBundle.bundleName
        typographyBundlePreOrder?.shouldShowWithAction(selectedSingleBundle.isPreOrder) {
            typographyBundlePreOrder?.text = selectedSingleBundle.preOrderInfo
        }
        buttonAtc?.text = if (selectedSingleBundle.isPreOrder) {
            itemView.context.getString(R.string.shop_page_product_bundle_preorder_button_text)
        } else {
            itemView.context.getString(R.string.product_bundle_action_button_text)
        }

        // single bundle product detail
        imageBundleProduct?.loadImage(product.productImageUrl)
        typographyBundleProductName?.text = product.productName

        // bundle variant package list
        (rvBundleDetails?.adapter as ProductBundleSingleAdapter).updateDataSet(parentSingleBundle.bundles)
    }

    private fun initBundleDetailsRecyclerView() {
        rvBundleDetails?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ProductBundleSingleAdapter()
        }

        val constraintSet = ConstraintSet()

        widgetContainer?.layoutParams?.width = containerWidgetParams
        constraintSet.clone(widgetContainer)
        constraintSet.applyTo(widgetContainer)
    }

    private fun renderBundlePriceDetails(bundle: BundleDetailUiModel) {
        itemView.context?.let { ctx ->
            labelBundleDiscount?.setLabel("${bundle.discountPercentage}%")
            typographyBundleProductDisplayPrice?.text = bundle.displayPrice
            typographyBundleProductOriginalPrice?.apply {
                text = bundle.originalPrice
                paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            typographyBundleProductSavingAmount?.text = HtmlLinkHelper(ctx, bundle.savingAmountWording).spannedString
        }
    }

}