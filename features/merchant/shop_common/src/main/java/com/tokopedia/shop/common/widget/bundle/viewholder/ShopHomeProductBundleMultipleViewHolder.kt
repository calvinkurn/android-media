package com.tokopedia.shop.common.widget.bundle.viewholder

import android.graphics.Paint
import android.util.TypedValue
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.databinding.ItemProductBundleMultipleWidgetBinding
import com.tokopedia.shop.common.widget.bundle.adapter.ShopHomeProductBundleMultipleAdapter
import com.tokopedia.shop.common.widget.bundle.adapter.ShopHomeProductBundleWidgetAdapter
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel
import com.tokopedia.shop.common.widget.model.ShopHomeWidgetLayout
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeProductBundleMultipleViewHolder(
        itemView: View,
        private val multipleProductBundleListener: MultipleProductBundleListener,
        private val bundleListSize: Int,
        private val widgetLayout: ShopHomeWidgetLayout
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
            typographyBundleName = bundleWidgetHeaderContainer.tvBundleNameLarge
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

    fun bind(bundle: ShopHomeProductBundleItemUiModel, widgetTitle: String, widgetName: String) {
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
        typographyBundlePreOrder?.shouldShowWithAction(multipleBundleItem.isPreOrder) {
            typographyBundlePreOrder?.text = multipleBundleItem.preOrderInfo
        }
        buttonAtc?.text = if (multipleBundleItem.isPreOrder) {
            itemView.context.getString(R.string.shop_page_product_bundle_preorder_button_text)
        } else {
            itemView.context.getString(R.string.shop_page_product_bundle_atc_button_text)
        }

        // bundle products list
        initBundleProductsRecyclerView(spanSize = bundle.bundleProducts.size, widgetTitle, widgetName)
        (rvBundleProducts?.adapter as ShopHomeProductBundleMultipleAdapter).updateDataSet(
                newList = bundle.bundleProducts,
                bundleDetail = multipleBundleItem,
                bundleParent = bundle
        )

        // bind listeners
        itemView.addOnImpressionListener(bundle) {
            multipleProductBundleListener.impressionProductBundleMultiple(
                    shopId = bundle.shopId,
                    warehouseId = bundle.warehouseId,
                    selectedMultipleBundle = multipleBundleItem,
                    bundleName = bundle.bundleName,
                    bundleType = bundle.bundleType,
                    bundlePosition = adapterPosition
            )
        }

        buttonAtc?.setOnClickListener {
            // add to cart bundle
            multipleProductBundleListener.addMultipleBundleToCart(
                    bundle.shopId,
                    bundle.warehouseId,
                    multipleBundleItem,
                    bundleListSize,
                    bundle.bundleProducts,
                    bundle.bundleName,
                    bundle.bundleType,
                    adapterPosition,
                    widgetLayout,
                    bundle.bundleGroupId
            )
        }
    }

    private fun initBundleProductsRecyclerView(spanSize: Int, widgetTitle: String, widgetName: String) {
        rvBundleProducts?.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, spanSize, GridLayoutManager.VERTICAL, false)
            adapter = ShopHomeProductBundleMultipleAdapter(multipleProductBundleListener, widgetTitle, widgetName)
        }

        val constraintSet = ConstraintSet()
        val containerWidgetParams = if (bundleListSize == ShopHomeProductBundleWidgetAdapter.SINGLE_SIZE_WIDGET) {
            ConstraintLayout.LayoutParams.MATCH_PARENT
        } else {
            TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ShopHomeProductBundleWidgetAdapter.BUNDLE_WIDGET_DEFAULT_WIDTH,
                    itemView.resources.displayMetrics
            ).toInt()
        }
        widgetContainer?.layoutParams?.width = containerWidgetParams
        constraintSet.clone(widgetContainer)
        constraintSet.applyTo(widgetContainer)
    }
}