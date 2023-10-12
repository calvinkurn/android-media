package com.tokopedia.shop.common.widget.bundle.viewholder

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
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.databinding.ItemProductBundleMultipleWidgetBinding
import com.tokopedia.shop.common.widget.bundle.adapter.ProductBundleMultipleAdapter
import com.tokopedia.shop.common.widget.bundle.listener.ProductBundleListener
import com.tokopedia.shop.common.widget.bundle.model.BundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleProductUiModel
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
        private val MIN_GRID_SIZE = 1
    }

    private var viewBinding: ItemProductBundleMultipleWidgetBinding? by viewBinding()
    private var listener: ProductBundleListener? = null
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
                if(!shopInfo?.shopIconUrl.isNullOrEmpty()) {
                    iconShop.visibility = View.VISIBLE
                    iconShop.loadImage(shopInfo?.shopIconUrl)
                }else{
                    iconShop.visibility = View.GONE
                }
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
            layoutManager = GridLayoutManager(context, spanSize.coerceAtLeast(MIN_GRID_SIZE),
                GridLayoutManager.VERTICAL, false)
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
            listener?.addMultipleBundleToCart(
                bundleDetail,
                products
            )
        }
    }

    fun setListener(listener: ProductBundleListener?) {
        this.listener = listener
    }
}
