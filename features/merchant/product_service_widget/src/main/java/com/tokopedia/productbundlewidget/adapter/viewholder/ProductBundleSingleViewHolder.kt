package com.tokopedia.productbundlewidget.adapter.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.ItemProductbundleSingleWidgetBinding
import com.tokopedia.productbundlewidget.adapter.ProductBundleSingleAdapter
import com.tokopedia.productbundlewidget.listener.ProductBundleAdapterListener
import com.tokopedia.productbundlewidget.model.*
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.ColorMode
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ProductBundleSingleViewHolder(
    itemView: View,
    private val containerWidgetParams: Int,
    private val isOverrideWidgetTheme: Boolean
) : RecyclerView.ViewHolder(itemView) {

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
    private var cardBundling: CardUnify? = null
    private var icBundleDiscount: IconUnify? = null

    init {
        viewBinding?.apply {
            typographyBundleName = bundleWidgetHeaderContainer.tvBundleName
            typographyBundlePreOrder = bundleWidgetHeaderContainer.tvBundlePreorder
            typographyBundleProductName = tvBundleProductSingleName
            typographyBundleProductDisplayPrice = tvBundleDisplayPrice
            typographyBundleProductOriginalPrice = tvBundleOriginalPrice
            typographyBundleProductSavingAmount = bundleWidgetFooter.tvSavingAmountPriceWording
            buttonAtc = bundleWidgetFooter.btnBundleAtc
            labelBundleDiscount = labelDiscountBundle
            imageBundleProduct = ivBundleImage
            rvBundleDetails = rvBundleSinglePackage
            widgetContainer = bundleWidgetContainer
            cardBundling = container
            icBundleDiscount = bundleWidgetFooter.icBundleDiscount
        }
        initBundleDetailsRecyclerView()
    }

    fun bind(bundle: BundleUiModel) {
        val bundleDetail = bundle.bundleDetails.firstOrNull() ?: BundleDetailUiModel()
        val product = bundleDetail.products.firstOrNull() ?: BundleProductUiModel()

        initFooterStyle(bundle)

        // Setup widget theme if the page owner wants to change the widget theme
        overrideWidgetTheme()

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

    private fun initFooterStyle(bundle: BundleUiModel) {
        val isMinimalMode = bundle.bundleName.isEmpty()
        val isWideMode = containerWidgetParams.isMoreThanZero()
        viewBinding?.apply {
            bundleWidgetMaximalFooter.isVisible = !isMinimalMode
            bundleWidgetMinimalFooter.root.isVisible = isMinimalMode
            if (isWideMode) {
                bundleWidgetMinimalFooter.tvBundleDisplayPrice.layoutParams = FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.MATCH_PARENT,
                    FlexboxLayout.LayoutParams.MATCH_PARENT
                )
            }
            if (isMinimalMode) {
                bundleWidgetMinimalFooter.let {
                    typographyBundleProductDisplayPrice = it.tvBundleDisplayPrice
                    typographyBundleProductOriginalPrice = it.tvBundleOriginalPrice
                    labelBundleDiscount = it.labelDiscountBundle
                    buttonAtc = it.btnBundleAtc
                }
            }
        }
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

        updateActionButtonListener(bundleDetail, product, adapterPosition)

        bundleDetailAdapter.setSelectionListener { selectedBundle ->
            renderBundlePriceDetails(selectedBundle)
            bundle.selectedBundleId = selectedBundle.bundleId
            bundle.selectedBundleApplink = selectedBundle.applink
            listener?.onTrackSingleVariantChange(
                product,
                selectedBundle,
                bundle.bundleName
            )
            updateActionButtonListener(selectedBundle, product, adapterPosition)
        }
    }

    private fun initPreorderAndSoldItem(bundleDetail: BundleDetailUiModel) {
        typographyBundlePreOrder?.text = when {
            bundleDetail.useProductSoldInfo -> bundleDetail.productSoldInfo
            bundleDetail.isPreOrder -> bundleDetail.preOrderInfo
            bundleDetail.totalSold.isZero() -> ""
            else -> itemView.context.getString(
                R.string.product_bundle_bundle_sold,
                bundleDetail.totalSold.toString()
            )
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

    private fun updateActionButtonListener(
        bundleDetail: BundleDetailUiModel,
        product: BundleProductUiModel,
        bundlePosition: Int
    ) {
        buttonAtc?.setOnClickListener {
            listener?.onSingleBundleActionButtonClicked(bundleDetail, product, bundlePosition)
        }
    }

    fun setListener(listener: ProductBundleAdapterListener?) {
        this.listener = listener
    }

    private fun overrideWidgetTheme() {
        if (isOverrideWidgetTheme) {
            viewBinding?.bundleWidgetHeaderContainer?.let {
                it.tvBundleNameLarge.setTextColor(ContextCompat.getColor(itemView.context, R.color.dms_high_emphasis))
            }
            cardBundling?.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
            typographyBundleName?.setTextColor(ContextCompat.getColor(itemView.context, R.color.dms_high_emphasis))
            typographyBundlePreOrder?.setTextColor(ContextCompat.getColor(itemView.context, R.color.dms_low_emphasis))
            typographyBundleProductName?.setTextColor(ContextCompat.getColor(itemView.context, R.color.dms_high_emphasis))
            typographyBundleProductDisplayPrice?.setTextColor(ContextCompat.getColor(itemView.context, R.color.dms_high_emphasis))
            typographyBundleProductOriginalPrice?.setTextColor(ContextCompat.getColor(itemView.context, R.color.dms_low_emphasis))
            typographyBundleProductSavingAmount?.setTextColor(ContextCompat.getColor(itemView.context, R.color.dms_high_emphasis))
            icBundleDiscount?.setImage(newLightEnable = ContextCompat.getColor(itemView.context, R.color.dms_high_emphasis))

            buttonAtc?.applyColorMode(colorMode = ColorMode.LIGHT_MODE)
        }
    }
}
