package com.tokopedia.shop.home.view.adapter.viewholder

import android.graphics.Paint
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.model.ShopHomeTerlarisWidgetTrackerDataModel
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.LayoutShopHomeV4TerlarisWidgetBinding
import com.tokopedia.shop.home.view.adapter.ShopHomeV4TerlarisAdapter
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeV4TerlarisViewHolder(
    itemView: View,
    private val listener: ShopHomeV4TerlarisViewHolderListener
) : AbstractViewHolder<ShopHomeCarousellProductUiModel>(itemView) {

    interface ShopHomeV4TerlarisViewHolderListener {
        fun onProductClick(trackerModel: ShopHomeTerlarisWidgetTrackerDataModel)

        fun onProductImpression(carouselData: List<ShopHomeProductUiModel>, position: Int, widgetId: String)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_shop_home_v4_terlaris_widget

        val PRODUCT_ZERO = 0
        val PRODUCT_ONE = 1
        val PRODUCT_THREE = 3
        val PRODUCT_SIX = 6
        val PRODUCT_NINE = 9
    }

    private val viewBinding: LayoutShopHomeV4TerlarisWidgetBinding? by viewBinding()
    private var rvProductCarousel: RecyclerView? = viewBinding?.rvTerlarisWidget
    private var terlarisWidgetAdapter: ShopHomeV4TerlarisAdapter? = null
    private var widgetTitle: TextView? = viewBinding?.terlarisWidgetTitle
    private var widgetSubtitle: TextView? = viewBinding?.terlarisWidgetSubtitle
    private var terlarisWidgetContainer: ConstraintLayout? = viewBinding?.terlarisWidgetContainer
    private var containerThreeProducts: ConstraintLayout? = viewBinding?.terlarisContainerItemProduct
    private var prodcutCard1: ConstraintLayout? = viewBinding?.terlarisProductDetail1
    private var productImg1: ImageUnify? = viewBinding?.terlarisImgProduct1
    private var productName1: TextView? = viewBinding?.terlarisProductName1
    private var productPrice1: TextView? = viewBinding?.terlarisProductPrice1
    private var productRank1: TextView? = viewBinding?.terlarisProductRankNumber1
    private var containerDiscount1: LinearLayoutCompat? = viewBinding?.terlarisContainerDiscount1
    private var labelDiscount1: Label? = viewBinding?.terlarisLabelDiscountPercentage1
    private var productOriginalPrice1: TextView? = viewBinding?.terlarisOriginalPrice1
    private var prodcutCard2: ConstraintLayout? = viewBinding?.terlarisProductDetail2
    private var productImg2: ImageUnify? = viewBinding?.terlarisImgProduct2
    private var productName2: TextView? = viewBinding?.terlarisProductName2
    private var productPrice2: TextView? = viewBinding?.terlarisProductPrice2
    private var productRank2: TextView? = viewBinding?.terlarisProductRankNumber2
    private var containerDiscount2: LinearLayoutCompat? = viewBinding?.terlarisContainerDiscount2
    private var labelDiscount2: Label? = viewBinding?.terlarisLabelDiscountPercentage1
    private var productOriginalPrice2: TextView? = viewBinding?.terlarisOriginalPrice2
    private var prodcutCard3: ConstraintLayout? = viewBinding?.terlarisProductDetail3
    private var productImg3: ImageUnify? = viewBinding?.terlarisImgProduct3
    private var productName3: TextView? = viewBinding?.terlarisProductName3
    private var productPrice3: TextView? = viewBinding?.terlarisProductPrice3
    private var productRank3: TextView? = viewBinding?.terlarisProductRankNumber3
    private var containerDiscount3: LinearLayoutCompat? = viewBinding?.terlarisContainerDiscount3
    private var labelDiscount3: Label? = viewBinding?.terlarisLabelDiscountPercentage1
    private var productOriginalPrice3: TextView? = viewBinding?.terlarisOriginalPrice3

    override fun bind(element: ShopHomeCarousellProductUiModel?) {
        element?.let {
            val isOverrideTheme = it.header.isOverrideTheme
            val colorSchema = it.header.colorSchema
            val linearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            setHeaderSection(element)
            terlarisWidgetAdapter = ShopHomeV4TerlarisAdapter(
                listener = listener,
                isOverrideTheme = isOverrideTheme,
                colorSchema = colorSchema,
                element = element
            )
            rvProductCarousel?.apply {
                isNestedScrollingEnabled = false
                layoutManager = linearLayoutManager
                adapter = terlarisWidgetAdapter
            }

            val productCarouselData = getProductCarouselData(productList = it.productList)
            productCarouselData?.let { carouselData ->
                val sanitizedProductListCarouselData = getCarouselData(carouselData)
                if (sanitizedProductListCarouselData.size == PRODUCT_ONE) {
                    if (isOverrideTheme) {
                        overrideWidgetTheme(colorSchema = colorSchema)
                    }
                    showThreeItemLayout(
                        productList = sanitizedProductListCarouselData,
                        widgetId = element.widgetId
                    )
                    setupImpressionListener(element, carouselData)
                } else if (sanitizedProductListCarouselData.size > PRODUCT_ONE) {
                    showMoreThanThreeItemLayout(
                        productList = sanitizedProductListCarouselData
                    )
                } else {
                    hideTheWidget()
                }
            }
        }
    }

    private fun setHeaderSection(element: ShopHomeCarousellProductUiModel) {
        val title = element.header.title
        val subTitle = element.header.subtitle
        widgetTitle?.shouldShowWithAction(title.isNotEmpty()) {
            widgetTitle?.text = title
        }
        widgetSubtitle?.shouldShowWithAction(subTitle.isNotEmpty()) {
            widgetSubtitle?.text = subTitle
        }
    }

    private fun showThreeItemLayout(productList: List<List<ShopHomeProductUiModel>>, widgetId: String) {
        showTheContainer()
        showLayoutThreeItem(productList = productList, widgetId = widgetId)
        hideHorizontalProductCarousel()
    }

    private fun showMoreThanThreeItemLayout(
        productList: List<List<ShopHomeProductUiModel>>
    ) {
        showTheContainer()
        hideLayoutThreeItem()
        showHorizontalProductCarousel()
        terlarisWidgetAdapter?.updateData(productList = productList)
    }

    private fun hideTheWidget() {
        hideTheContainer()
        hideLayoutThreeItem()
        hideHorizontalProductCarousel()
    }

    private fun showLayoutThreeItem(productList: List<List<ShopHomeProductUiModel>>, widgetId: String) {
        if (productList.isNotEmpty() && productList[0].size == PRODUCT_THREE) {
            // First product
            containerThreeProducts?.visibility = View.VISIBLE
            prodcutCard1?.setOnClickListener {
                listener.onProductClick(
                    ShopHomeTerlarisWidgetTrackerDataModel(
                        productId = productList[0][0].id,
                        productName = productList[0][0].name,
                        productPrice = productList[0][0].displayedPrice,
                        position = 1,
                        widgetId = widgetId
                    )
                )
            }
            ImageHandler.loadImageRounded2(itemView.context, productImg1, productList[0][0].imageUrl.orEmpty(), 8.toPx().toFloat())
            productName1?.text = productList[0][0].name
            productPrice1?.text = productList[0][0].displayedPrice
            productRank1?.text = "1"
            if (!productList[0][0].discountPercentage.isNullOrEmpty() &&
                !productList[0][0].originalPrice.isNullOrEmpty()
            ) {
                containerDiscount1?.visibility = View.VISIBLE
                labelDiscount1?.text = productList[0][0].discountPercentage
                productOriginalPrice1?.text = productList[0][0].originalPrice
                productOriginalPrice1?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                containerDiscount1?.visibility = View.GONE
            }
            // Second product
            prodcutCard2?.setOnClickListener {
                listener.onProductClick(
                    ShopHomeTerlarisWidgetTrackerDataModel(
                        productId = productList[0][1].id,
                        productName = productList[0][1].name,
                        productPrice = productList[0][1].displayedPrice,
                        position = 2,
                        widgetId = widgetId
                    )
                )
            }
            ImageHandler.loadImageRounded2(itemView.context, productImg2, productList[0][1].imageUrl.orEmpty(), 8.toPx().toFloat())
            productName2?.text = productList[0][1].name
            productPrice2?.text = productList[0][1].displayedPrice
            productRank2?.text = "2"
            if (!productList[0][1].discountPercentage.isNullOrEmpty() &&
                !productList[0][1].originalPrice.isNullOrEmpty()
            ) {
                containerDiscount2?.visibility = View.VISIBLE
                labelDiscount2?.text = productList[0][1].discountPercentage
                productOriginalPrice2?.text = productList[0][1].originalPrice
                productOriginalPrice2?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                containerDiscount2?.visibility = View.GONE
            }
            // Third product
            prodcutCard3?.setOnClickListener {
                listener.onProductClick(
                    ShopHomeTerlarisWidgetTrackerDataModel(
                        productId = productList[0][2].id,
                        productName = productList[0][2].name,
                        productPrice = productList[0][2].displayedPrice,
                        position = 3,
                        widgetId = widgetId
                    )
                )
            }
            ImageHandler.loadImageRounded2(itemView.context, productImg3, productList[0][2].imageUrl.orEmpty(), 8.toPx().toFloat())
            productName3?.text = productList[0][2].name
            productPrice3?.text = productList[0][2].displayedPrice
            productRank3?.text = "3"
            if (!productList[0][2].discountPercentage.isNullOrEmpty() &&
                !productList[0][2].originalPrice.isNullOrEmpty()
            ) {
                containerDiscount3?.visibility = View.VISIBLE
                labelDiscount3?.text = productList[0][2].discountPercentage
                productOriginalPrice3?.text = productList[0][2].originalPrice
                productOriginalPrice3?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                containerDiscount3?.visibility = View.GONE
            }
        }
    }

    private fun getCarouselData(productList: List<ShopHomeProductUiModel>): List<List<ShopHomeProductUiModel>> {
        val chunkSize = 3
        val chunkedData = productList.chunked(chunkSize)
        return chunkedData
    }

    private fun getProductCarouselData(productList: List<ShopHomeProductUiModel>): List<ShopHomeProductUiModel>? {
        return if (productList.size == PRODUCT_THREE ||
            productList.size == PRODUCT_SIX ||
            productList.size == PRODUCT_NINE
        ) {
            productList
        } else if (productList.size in (PRODUCT_THREE + 1) until PRODUCT_SIX) {
            productList.take(PRODUCT_THREE)
        } else if (productList.size in (PRODUCT_SIX + 1) until PRODUCT_NINE) {
            productList.take(PRODUCT_SIX)
        } else if (productList.size > PRODUCT_NINE) {
            productList.take(PRODUCT_NINE)
        } else {
            null
        }
    }

    private fun hideLayoutThreeItem() {
        containerThreeProducts?.visibility = View.GONE
    }

    private fun showHorizontalProductCarousel() {
        rvProductCarousel?.visibility = View.VISIBLE
    }

    private fun hideHorizontalProductCarousel() {
        rvProductCarousel?.visibility = View.GONE
    }

    private fun hideTheContainer() {
        // Hide the widget due to product list equals to 0
        terlarisWidgetContainer?.visibility = View.GONE
    }
    private fun showTheContainer() {
        terlarisWidgetContainer?.visibility = View.VISIBLE
    }

    private fun overrideWidgetTheme(colorSchema: ShopPageColorSchema) {
        widgetTitle?.setTextColor(colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS))
        widgetSubtitle?.setTextColor(colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_LOW_EMPHASIS))
        productName1?.setTextColor(colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS))
        productPrice1?.setTextColor(colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS))
        productName2?.setTextColor(colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS))
        productPrice2?.setTextColor(colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS))
        productName3?.setTextColor(colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS))
        productPrice3?.setTextColor(colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS))
    }

    private fun setupImpressionListener(element: ShopHomeCarousellProductUiModel, carouselData: List<ShopHomeProductUiModel>) {
        itemView.addOnImpressionListener(element.impressHolder) {
            listener.onProductImpression(carouselData, bindingAdapterPosition, element.widgetId)
        }
    }
}
