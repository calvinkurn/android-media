package com.tokopedia.productbundlewidget.adapter.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.productbundlewidget.adapter.ProductBundleMultipleAdapter
import com.tokopedia.productbundlewidget.listener.ProductBundleAdapterListener
import com.tokopedia.productbundlewidget.model.BundleDetailUiModel
import com.tokopedia.productbundlewidget.model.BundleProductUiModel
import com.tokopedia.productbundlewidget.model.BundleShopUiModel
import com.tokopedia.productbundlewidget.model.BundleUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.ItemProductbundleMultipleWidgetBinding

class ProductBundleMultipleViewHolder(
    itemView: View,
    private val containerWidgetParams: Int
): RecyclerView.ViewHolder(itemView) {

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

    init {
        viewBinding?.apply {
            typographyBundleName = bundleWidgetHeaderContainer.tvBundleName
            typographyBundleProductDisplayPrice = tvBundleDisplayPrice
            typographyBundleProductOriginalPrice = tvBundleOriginalPrice
            typographyBundlePreOrder = bundleWidgetHeaderContainer.tvBundlePreorder
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

        initPreorderAndSoldItem(bundleDetail)
        initShopInfo(bundleDetail.shopInfo, bundle.bundleName)
        initBundleProductsRecyclerView(bundleDetail.products.size, bundle, bundleDetail)
        initActionButton(bundle.actionButtonText, bundleDetail.isPreOrder)
        initListener(bundle, bundleDetail, bundleDetail.products)
    }

    private fun initPreorderAndSoldItem(bundleDetail: BundleDetailUiModel) {
        typographyBundlePreOrder?.text = when {
            bundleDetail.useProductSoldInfo -> bundleDetail.productSoldInfo
            bundleDetail.isPreOrder -> bundleDetail.preOrderInfo
            else -> itemView.context.getString(R.string.product_bundle_bundle_sold, bundleDetail.totalSold)
        }
    }

    private fun initShopInfo(shopInfo: BundleShopUiModel?, bundleName: String) {
        val hasShopInfo = shopInfo != null
        viewBinding?.bundleWidgetHeaderContainer?.apply {
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
        spanSize: Int,
        bundle: BundleUiModel,
        bundleDetail: BundleDetailUiModel
    ) {
        rvBundleProducts?.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, spanSize, GridLayoutManager.VERTICAL, false)
            adapter = ProductBundleMultipleAdapter(listener)
        }
        (rvBundleProducts?.adapter as ProductBundleMultipleAdapter).updateDataSet(
            bundleDetail.products,
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
        }

        buttonAtc?.setOnClickListener {
            listener?.onMultipleBundleActionButtonClicked(
                bundleDetail,
                products
            )
        }
    }

    fun setListener(listener: ProductBundleAdapterListener?) {
        this.listener = listener
    }
}
