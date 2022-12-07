package com.tokopedia.productbundlewidget.adapter.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.productbundlewidget.adapter.ProductBundleSingleAdapter
import com.tokopedia.productbundlewidget.listener.ProductBundleAdapterListener
import com.tokopedia.productbundlewidget.model.*
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.ItemProductbundleSingleWidgetBinding

class ProductBundleSingleViewHolder(
    itemView: View,
    private val containerWidgetParams: Int
): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_productbundle_single_widget
    }

    private var viewBinding: ItemProductbundleSingleWidgetBinding? by viewBinding()
    private var listener: ProductBundleAdapterListener? = null
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
    private val bundleDetailAdapter = ProductBundleSingleAdapter()

    init {
        viewBinding?.apply {
            typographyBundleName = bundleWidgetHeaderContainer.tvBundleName
            typographyBundlePreOrder = bundleWidgetHeaderContainer.tvBundlePreorder
            typographyBundleProductName = tvBundleProductSingleName
            typographyBundleProductDisplayPrice = tvBundleDisplayPrice
            typographyBundleProductOriginalPrice = tvBundleOriginalPrice
            typographyBundleProductSavingAmount = bundleWidgetHeaderFooter.tvSavingAmountPriceWording
            buttonAtc = bundleWidgetHeaderFooter.btnBundleAtc
            labelBundleDiscount = labelDiscountBundle
            imageBundleProduct = ivBundleImage
            rvBundleDetails = rvBundleSinglePackage
            widgetContainer = bundleWidgetContainer
        }
        initBundleDetailsRecyclerView()
    }

    fun bind(bundle: BundleUiModel) {
        val bundleDetail = bundle.bundleDetails.firstOrNull() ?: BundleDetailUiModel()
        val product = bundleDetail.products.firstOrNull() ?: BundleProductUiModel()

        // bundle card item details
        renderBundlePriceDetails(bundleDetail)
        typographyBundleName?.text = bundle.bundleName

        // single bundle product detail
        imageBundleProduct?.loadImage(product.productImageUrl)
        typographyBundleProductName?.text = product.productName

        // bundle variant package list
        (rvBundleDetails?.adapter as ProductBundleSingleAdapter).updateDataSet(bundle.bundleDetails)

        initPreorderAndSoldItem(bundleDetail)
        initShopInfo(bundleDetail.shopInfo, bundle.bundleName)
        initActionButton(bundle.actionButtonText, bundleDetail.isPreOrder)
        initListener(bundle, bundleDetail, product)
    }

    private fun initListener(
        bundle: BundleUiModel,
        bundleDetail: BundleDetailUiModel,
        product: BundleProductUiModel
    ) {
        imageBundleProduct?.setOnClickListener {
            listener?.onBundleProductClicked(
                BundleTypes.SINGLE_BUNDLE,
                bundle,
                bundleDetail,
                product,
                adapterPosition
            )
        }

        typographyBundleProductName?.setOnClickListener {
            imageBundleProduct?.callOnClick()
        }

        itemView.addOnImpressionListener(bundle) {
            listener?.impressionProductBundleSingle(
                bundleDetail,
                product,
                bundle.bundleName,
                adapterPosition
            )
        }

        buttonAtc?.setOnClickListener {
            listener?.onSingleBundleActionButtonClicked(
                bundleDetail,
                product
            )
        }

        bundleDetailAdapter.setSelectionListener { selectedBundle ->
            renderBundlePriceDetails(selectedBundle)
            bundle.selectedBundleId = selectedBundle.bundleId
            bundle.selectedBundleApplink = selectedBundle.applink
            listener?.onTrackSingleVariantChange(
                product,
                selectedBundle,
                bundle.bundleName
            )
        }
    }

    private fun initPreorderAndSoldItem(bundleDetail: BundleDetailUiModel) {
        typographyBundlePreOrder?.text = when {
            bundleDetail.useProductSoldInfo -> bundleDetail.productSoldInfo
            bundleDetail.isPreOrder -> bundleDetail.preOrderInfo
            bundleDetail.totalSold.isZero() -> ""
            else -> itemView.context.getString(R.string.product_bundle_bundle_sold,
                bundleDetail.totalSold.toString())
        }
    }

    private fun initShopInfo(shopInfo: BundleShopUiModel?, bundleName: String) {
        val hasShopInfo = shopInfo != null
        viewBinding?.bundleWidgetHeaderContainer?.apply {
            root.isVisible = bundleName.isNotEmpty()
            iconShop.isVisible = hasShopInfo
            tvShopName.isVisible = hasShopInfo
            tvBundleName.isVisible = hasShopInfo
            tvBundleNameLarge.isVisible = !hasShopInfo
            if (hasShopInfo) {
                iconShop.loadImage(shopInfo?.shopIconUrl)
                tvShopName.text = shopInfo?.shopName
            } else {
                tvBundleNameLarge.text = bundleName
            }
        }
    }

    private fun initBundleDetailsRecyclerView() {
        rvBundleDetails?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = bundleDetailAdapter
        }

        val constraintSet = ConstraintSet()
        widgetContainer?.layoutParams?.width = containerWidgetParams
        constraintSet.clone(widgetContainer)
        constraintSet.applyTo(widgetContainer)
    }

    private fun initActionButton(atcButtonText: String?, isPreOrder: Boolean) {
        buttonAtc?.text = if (atcButtonText != null) {
            atcButtonText
        } else if (isPreOrder) {
            itemView.context.getString(R.string.shop_page_product_bundle_preorder_button_text)
        } else {
            itemView.context.getString(R.string.product_bundle_action_button_text)
        }
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

    fun setListener(listener: ProductBundleAdapterListener?) {
        this.listener = listener
    }

}
