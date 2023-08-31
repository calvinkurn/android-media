package com.tokopedia.productbundlewidget.adapter.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.ItemProductbundleMultipleWidgetBinding
import com.tokopedia.productbundlewidget.adapter.ProductBundleMultipleAdapter
import com.tokopedia.productbundlewidget.adapter.constant.ProductBundleConstant.MAX_PRODUCT_DISPLAYED
import com.tokopedia.productbundlewidget.listener.ProductBundleAdapterListener
import com.tokopedia.productbundlewidget.model.BundleDetailUiModel
import com.tokopedia.productbundlewidget.model.BundleProductGroupingHelper.groupDataSet
import com.tokopedia.productbundlewidget.model.BundleProductUiModel
import com.tokopedia.productbundlewidget.model.BundleShopUiModel
import com.tokopedia.productbundlewidget.model.BundleUiModel
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.ColorMode
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ProductBundleMultipleViewHolder(
    itemView: View,
    private val containerWidgetParams: Int,
    private val isOverrideWidgetTheme: Boolean
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_productbundle_multiple_widget
    }

    private var viewBinding: ItemProductbundleMultipleWidgetBinding? by viewBinding()
    private var listener: ProductBundleAdapterListener? = null
    private var typographyBundleName: Typography? = null
    private var typographyBundleProductDisplayPrice: Typography? = null
    private var typographyBundleProductOriginalPrice: Typography? = null
    private var typographyBundleProductSavingAmount: Typography? = null
    private var typographyBundlePreOrder: Typography? = null
    private var buttonAtc: UnifyButton? = null
    private var labelBundleDiscount: Label? = null
    private var rvBundleProducts: RecyclerView? = null
    private var widgetContainer: ConstraintLayout? = null
    private var cardBundling: CardUnify? = null
    private var icBundleDiscount: IconUnify? = null

    init {
        viewBinding?.apply {
            typographyBundleName = bundleWidgetHeaderContainer.tvBundleName
            typographyBundleProductDisplayPrice = tvBundleDisplayPrice
            typographyBundleProductOriginalPrice = tvBundleOriginalPrice
            typographyBundlePreOrder = bundleWidgetHeaderContainer.tvBundlePreorder
            labelBundleDiscount = labelDiscountBundle
            typographyBundleProductSavingAmount = bundleWidgetFooter.tvSavingAmountPriceWording
            buttonAtc = bundleWidgetFooter.btnBundleAtc
            rvBundleProducts = rvMultipleBundleProducts
            widgetContainer = bundleWidgetContainer
            cardBundling = container
            icBundleDiscount = icBundleDiscount
        }
    }

    fun bind(bundle: BundleUiModel) {
        val bundleDetail = bundle.bundleDetails.firstOrNull() ?: BundleDetailUiModel()

        initFooterStyle(bundle)

        // Setup widget theme if the page owner wants to change the widget theme
        overrideWidgetTheme()

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

        initPreorderAndSoldItem(bundleDetail)
        initShopInfo(bundleDetail.shopInfo, bundle.bundleName)
        initBundleProductsRecyclerView(bundle, bundleDetail)
        initActionButton(bundle.actionButtonText, bundleDetail.isPreOrder)
        initListener(bundle, bundleDetail, bundleDetail.products)
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

    private fun initBundleProductsRecyclerView(
        bundle: BundleUiModel,
        bundleDetail: BundleDetailUiModel
    ) {
        val displayedProducts: List<BundleProductUiModel>
        val groupedProducts: List<BundleProductUiModel>
        groupDataSet(bundleDetail.products).apply {
            displayedProducts = first
            groupedProducts = second
        }
        val spanSize = bundleDetail.products.size.coerceAtMost(MAX_PRODUCT_DISPLAYED)
        rvBundleProducts?.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, spanSize, GridLayoutManager.VERTICAL, false)
            adapter = ProductBundleMultipleAdapter(
                listener = listener,
                isOverrideWidgetTheme = isOverrideWidgetTheme
            )
            addItemDecoration(MultipleBundleItemDecoration(context))
        }
        (rvBundleProducts?.adapter as ProductBundleMultipleAdapter).updateDataSet(
            displayedProducts,
            groupedProducts,
            bundleDetail,
            bundle
        )

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
        products: List<BundleProductUiModel>
    ) {
        itemView.addOnImpressionListener(bundle) {
            listener?.impressionProductBundleMultiple(
                bundleDetail,
                adapterPosition
            )
            listener?.impressionProductBundleMultiple(
                bundle,
                bundleDetail,
                adapterPosition
            )
        }

        buttonAtc?.setOnClickListener {
            listener?.onMultipleBundleActionButtonClicked(
                bundleDetail,
                products,
                adapterPosition
            )
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
            typographyBundleProductDisplayPrice?.setTextColor(ContextCompat.getColor(itemView.context, R.color.dms_high_emphasis))
            typographyBundleProductOriginalPrice?.setTextColor(ContextCompat.getColor(itemView.context, R.color.dms_low_emphasis))
            typographyBundleProductSavingAmount?.setTextColor(ContextCompat.getColor(itemView.context, R.color.dms_high_emphasis))
            icBundleDiscount?.setImage(newLightEnable = ContextCompat.getColor(itemView.context, R.color.dms_high_emphasis))
            buttonAtc?.applyColorMode(colorMode = ColorMode.LIGHT_MODE)
        }
    }
}
