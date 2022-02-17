package com.tokopedia.shop.common.widget.bundle.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.databinding.ItemShopHomeProductBundleSingleWidgetBinding
import com.tokopedia.shop.common.widget.bundle.adapter.ShopHomeProductBundleSingleAdapter
import com.tokopedia.shop.common.widget.bundle.adapter.ShopHomeProductBundleWidgetAdapter
import com.tokopedia.shop.common.widget.bundle.adapter.SingleBundleVariantSelectedListener
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel
import com.tokopedia.shop.common.widget.model.ShopHomeWidgetLayout
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeProductBundleSingleViewHolder(
        itemView: View,
        private val singleProductBundleListener: SingleProductBundleListener,
        private val bundleListSize: Int,
        private val widgetLayout: ShopHomeWidgetLayout
): RecyclerView.ViewHolder(itemView), SingleBundleVariantSelectedListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_bundle_single_widget
    }

    private var viewBinding: ItemShopHomeProductBundleSingleWidgetBinding? by viewBinding()
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
    private var selectedSingleBundle: ShopHomeProductBundleDetailUiModel = ShopHomeProductBundleDetailUiModel()
    private var singleBundleProduct: ShopHomeBundleProductUiModel = ShopHomeBundleProductUiModel()
    private var parentSingleBundle: ShopHomeProductBundleItemUiModel = ShopHomeProductBundleItemUiModel()

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

    override fun onSingleVariantSelected(selectedBundle: ShopHomeProductBundleDetailUiModel) {
        selectedSingleBundle = selectedBundle
        renderBundlePriceDetails(selectedBundle)
        singleProductBundleListener.onTrackSingleVariantChange(
                singleBundleProduct,
                selectedSingleBundle,
                parentSingleBundle.bundleName
        )
    }

    fun bind(bundle: ShopHomeProductBundleItemUiModel) {
        parentSingleBundle = bundle
        singleBundleProduct = parentSingleBundle.bundleProducts.firstOrNull() ?: ShopHomeBundleProductUiModel()
        selectedSingleBundle = parentSingleBundle.bundleDetails.firstOrNull() ?: ShopHomeProductBundleDetailUiModel()

        // bundle card item details
        renderBundlePriceDetails(selectedSingleBundle)
        typographyBundleName?.text = parentSingleBundle.bundleName
        typographyBundlePreOrder?.shouldShowWithAction(selectedSingleBundle.isPreOrder) {
            typographyBundlePreOrder?.text = selectedSingleBundle.preOrderInfo
        }
        buttonAtc?.text = if (selectedSingleBundle.isPreOrder) {
            itemView.context.getString(R.string.shop_page_product_bundle_preorder_button_text)
        } else {
            itemView.context.getString(R.string.shop_page_product_bundle_atc_button_text)
        }

        // single bundle product detail
        imageBundleProduct?.loadImage(singleBundleProduct.productImageUrl)
        imageBundleProduct?.setOnClickListener {
            singleProductBundleListener.onSingleBundleProductClicked(
                    singleBundleProduct,
                    selectedSingleBundle,
                    parentSingleBundle.bundleName,
                    parentSingleBundle.bundleProducts.indexOf(singleBundleProduct)
            )
        }
        typographyBundleProductName?.text = singleBundleProduct.productName
        typographyBundleProductName?.setOnClickListener {
            singleProductBundleListener.onSingleBundleProductClicked(
                    singleBundleProduct,
                    selectedSingleBundle,
                    parentSingleBundle.bundleName,
                    parentSingleBundle.bundleProducts.indexOf(singleBundleProduct)
            )
        }

        // bundle variant package list
        (rvBundleDetails?.adapter as ShopHomeProductBundleSingleAdapter).updateDataSet(parentSingleBundle.bundleDetails)

        // bind listeners
        itemView.addOnImpressionListener(bundle) {
            singleProductBundleListener.impressionProductBundleSingle(
                    selectedSingleBundle,
                    singleBundleProduct,
                    bundle.bundleName,
                    adapterPosition
            )
        }

        buttonAtc?.setOnClickListener {
            singleProductBundleListener.addSingleBundleToCart(
                    selectedSingleBundle,
                    bundleListSize,
                    singleBundleProduct,
                    parentSingleBundle.bundleName,
                    widgetLayout
            )
        }
    }

    private fun initBundleDetailsRecyclerView() {
        rvBundleDetails?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ShopHomeProductBundleSingleAdapter(
                    singleBundleVariantSelectedListener = this@ShopHomeProductBundleSingleViewHolder
            )
        }

        if (bundleListSize == ShopHomeProductBundleWidgetAdapter.SINGLE_SIZE_WIDGET) {
            // change widget container width to match parent
            val constraintSet = ConstraintSet()
            val params = ConstraintLayout.LayoutParams.MATCH_PARENT
            widgetContainer?.layoutParams?.width = params
            constraintSet.clone(widgetContainer)
            constraintSet.applyTo(widgetContainer)
        }
    }

    private fun renderBundlePriceDetails(bundle: ShopHomeProductBundleDetailUiModel) {
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

interface SingleProductBundleListener {
    fun onSingleBundleProductClicked(
            selectedProduct: ShopHomeBundleProductUiModel,
            selectedSingleBundle: ShopHomeProductBundleDetailUiModel,
            bundleName: String,
            bundlePosition: Int
    )
    fun addSingleBundleToCart(
            selectedBundle: ShopHomeProductBundleDetailUiModel,
            bundleListSize: Int,
            bundleProducts: ShopHomeBundleProductUiModel,
            bundleName: String,
            widgetLayout: ShopHomeWidgetLayout
    )
    fun onTrackSingleVariantChange(
            selectedProduct: ShopHomeBundleProductUiModel,
            selectedSingleBundle: ShopHomeProductBundleDetailUiModel,
            bundleName: String,
    )
    fun impressionProductBundleSingle(
            selectedSingleBundle: ShopHomeProductBundleDetailUiModel,
            selectedProduct: ShopHomeBundleProductUiModel,
            bundleName: String,
            bundlePosition: Int,
    )
}