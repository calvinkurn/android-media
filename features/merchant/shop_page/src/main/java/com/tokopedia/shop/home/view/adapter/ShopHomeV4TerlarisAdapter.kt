package com.tokopedia.shop.home.view.adapter

import android.content.Context
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.model.ShopHomeTerlarisWidgetTrackerDataModel
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeV4TerlarisViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeV4TerlarisViewHolder.Companion.PRODUCT_THREE
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class ShopHomeV4TerlarisAdapter(
    private val listener: ShopHomeV4TerlarisViewHolder.ShopHomeV4TerlarisViewHolderListener,
    private val isOverrideTheme: Boolean,
    private val colorSchema: ShopPageColorSchema,
    private val element: ShopHomeCarousellProductUiModel
) : RecyclerView.Adapter<ShopHomeV4TerlarisAdapter.TerlarisWidgetViewHolder>() {

    private var productListData: List<List<ShopHomeProductUiModel>> = listOf()

    fun updateData(productList: List<List<ShopHomeProductUiModel>>) {
        productListData = productList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopHomeV4TerlarisAdapter.TerlarisWidgetViewHolder {
        return TerlarisWidgetViewHolder(parent.inflateLayout(R.layout.shop_home_v4_terlaris_item_widget))
    }

    override fun onBindViewHolder(holder: ShopHomeV4TerlarisAdapter.TerlarisWidgetViewHolder, position: Int) {
        holder.bindData(
            productListData = productListData[position],
            rank = getProductRank(position)
        )
    }

    override fun getItemCount(): Int {
        return productListData.size
    }

    private fun getProductRank(position: Int): List<Int> {
        return when (position) {
            0 -> {
                listOf(1, 2, 3)
            }
            1 -> {
                listOf(4, 5, 6)
            }
            2 -> {
                listOf(7, 8, 9)
            }
            else -> {
                listOf(0, 0, 0)
            }
        }
    }

    inner class TerlarisWidgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val context: Context
        private var productContainer1: ConstraintLayout? = itemView.findViewById(R.id.terlaris_item_product_detail_1)
        private var productImg1: ImageUnify? = itemView.findViewById(R.id.terlaris_item_img_product_1)
        private var productName1: Typography? = itemView.findViewById(R.id.terlaris_item_product_name_1)
        private var productPrice1: Typography? = itemView.findViewById(R.id.terlaris_item_product_price_1)
        private var terlarisContainerDiscount1: LinearLayoutCompat? = itemView.findViewById(R.id.terlaris_container_discount_1)
        private var productOriginalPrice1: TextView? = itemView.findViewById(R.id.terlaris_original_price_1)
        private var labelDiscount1: Label? = itemView.findViewById(R.id.terlaris_label_discount_percentage_1)
        private var productRank1: Typography? = itemView.findViewById(R.id.terlaris_item_product_rank_number_1)
        private var productContainer2: ConstraintLayout? = itemView.findViewById(R.id.terlaris_item_product_detail_2)
        private var productImg2: ImageUnify? = itemView.findViewById(R.id.terlaris_item_img_product_2)
        private var productName2: Typography? = itemView.findViewById(R.id.terlaris_item_product_name_2)
        private var productPrice2: Typography? = itemView.findViewById(R.id.terlaris_item_product_price_2)
        private var terlarisContainerDiscount2: LinearLayoutCompat? = itemView.findViewById(R.id.terlaris_container_discount_2)
        private var labelDiscount2: Label? = itemView.findViewById(R.id.terlaris_label_discount_percentage_2)
        private var productOriginalPrice2: TextView? = itemView.findViewById(R.id.terlaris_original_price_2)
        private var productRank2: Typography? = itemView.findViewById(R.id.terlaris_item_product_rank_number_2)
        private var productContainer3: ConstraintLayout? = itemView.findViewById(R.id.terlaris_item_product_detail_3)
        private var productImg3: ImageUnify? = itemView.findViewById(R.id.terlaris_item_img_product_3)
        private var productName3: Typography? = itemView.findViewById(R.id.terlaris_item_product_name_3)
        private var productPrice3: Typography? = itemView.findViewById(R.id.terlaris_item_product_price_3)
        private var terlarisContainerDiscount3: LinearLayoutCompat? = itemView.findViewById(R.id.terlaris_container_discount_3)
        private var labelDiscount3: Label? = itemView.findViewById(R.id.terlaris_label_discount_percentage_3)
        private var productOriginalPrice3: TextView? = itemView.findViewById(R.id.terlaris_original_price_3)
        private var productRank3: Typography? = itemView.findViewById(R.id.terlaris_item_product_rank_number_3)

        init {
            context = itemView.context
        }

        fun bindData(productListData: List<ShopHomeProductUiModel>, rank: List<Int>) {
            if (!productListData.size.isZero() && productListData.size == PRODUCT_THREE) {
                setupImpressionListener(productListData)

                if (isOverrideTheme) {
                    overrideWidgetContentTheme()
                }

                productContainer1?.setOnClickListener {
                    listener.onProductClick(
                        ShopHomeTerlarisWidgetTrackerDataModel(
                            productId = productListData[0].id,
                            productName = productListData[0].name,
                            productPrice = productListData[0].displayedPrice,
                            position = rank[0],
                            widgetId = element.widgetId
                        )
                    )
                }
                ImageHandler.loadImageRounded2(itemView.context, productImg1, productListData[0].imageUrl.orEmpty(), 8.toPx().toFloat())
                productName1?.text = productListData[0].name
                productPrice1?.text = productListData[0].displayedPrice
                productRank1?.text = rank[0].toString()
                if (!productListData[0].discountPercentage.isNullOrEmpty() &&
                    !productListData[0].originalPrice.isNullOrEmpty()
                ) {
                    terlarisContainerDiscount1?.visibility = View.VISIBLE
                    labelDiscount1?.text = productListData[0].discountPercentage
                    productOriginalPrice1?.text = productListData[0].originalPrice
                    productOriginalPrice1?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    terlarisContainerDiscount1?.visibility = View.GONE
                }

                productContainer2?.setOnClickListener {
                    listener.onProductClick(
                        ShopHomeTerlarisWidgetTrackerDataModel(
                            productId = productListData[1].id,
                            productName = productListData[1].name,
                            productPrice = productListData[1].displayedPrice,
                            position = rank[1],
                            widgetId = element.widgetId
                        )
                    )
                }
                ImageHandler.loadImageRounded2(itemView.context, productImg2, productListData[1].imageUrl.orEmpty(), 8.toPx().toFloat())
                productName2?.text = productListData[1].name
                productPrice2?.text = productListData[1].displayedPrice
                productRank2?.text = rank[1].toString()
                if (!productListData[1].discountPercentage.isNullOrEmpty() &&
                    !productListData[1].originalPrice.isNullOrEmpty()
                ) {
                    terlarisContainerDiscount2?.visibility = View.VISIBLE
                    labelDiscount2?.text = productListData[1].discountPercentage
                    productOriginalPrice2?.text = productListData[1].originalPrice
                    productOriginalPrice2?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    terlarisContainerDiscount2?.visibility = View.GONE
                }

                productContainer3?.setOnClickListener {
                    listener.onProductClick(
                        ShopHomeTerlarisWidgetTrackerDataModel(
                            productId = productListData[2].id,
                            productName = productListData[2].name,
                            productPrice = productListData[2].displayedPrice,
                            position = rank[2],
                            widgetId = element.widgetId
                        )
                    )
                }
                ImageHandler.loadImageRounded2(itemView.context, productImg3, productListData[2].imageUrl.orEmpty(), 8.toPx().toFloat())
                productName3?.text = productListData[2].name
                productPrice3?.text = productListData[2].displayedPrice
                productRank3?.text = rank[2].toString()
                if (!productListData[2].discountPercentage.isNullOrEmpty() &&
                    !productListData[2].originalPrice.isNullOrEmpty()
                ) {
                    terlarisContainerDiscount3?.visibility = View.VISIBLE
                    labelDiscount3?.text = productListData[2].discountPercentage
                    productOriginalPrice3?.text = productListData[2].originalPrice
                    productOriginalPrice3?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    terlarisContainerDiscount3?.visibility = View.GONE
                }
            }
        }

        private fun overrideWidgetContentTheme() {
            productName1?.setTextColor(colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS))
            productPrice1?.setTextColor(colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS))
            productOriginalPrice1?.setTextColor(colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.DISABLED_TEXT_COLOR))
            productName2?.setTextColor(colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS))
            productPrice2?.setTextColor(colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS))
            productOriginalPrice2?.setTextColor(colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.DISABLED_TEXT_COLOR))
            productName3?.setTextColor(colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS))
            productPrice3?.setTextColor(colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS))
            productOriginalPrice3?.setTextColor(colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.DISABLED_TEXT_COLOR))
        }

        private fun setupImpressionListener(carouselData: List<ShopHomeProductUiModel>) {
            itemView.addOnImpressionListener(element.impressHolder) {
                listener.onProductImpression(carouselData, bindingAdapterPosition, element.widgetId)
            }
        }
    }
}
