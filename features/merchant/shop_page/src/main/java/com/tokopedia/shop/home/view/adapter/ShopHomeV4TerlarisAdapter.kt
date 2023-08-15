package com.tokopedia.shop.home.view.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.shop.R
import com.tokopedia.shop.home.util.loadImageRounded
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeV4TerlarisViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeV4TerlarisViewHolder.Companion.PRODUCT_THREE
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ShopHomeV4TerlarisAdapter(
    private val listener: ShopHomeV4TerlarisViewHolder.ShopHomeV4TerlarisViewHolderListener
) : RecyclerView.Adapter<ShopHomeV4TerlarisAdapter.TerlarisWidgetViewHolder>() {

    private var productListData: List<List<ShopHomeProductUiModel>> = listOf()
    private var fontColor: Int? = null

    fun updateData(productList: List<List<ShopHomeProductUiModel>>, color: Int?) {
        productListData = productList
        fontColor = color
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
        private var productRank1: Typography? = itemView.findViewById(R.id.terlaris_item_product_rank_number_1)
        private var productContainer2: ConstraintLayout? = itemView.findViewById(R.id.terlaris_item_product_detail_2)
        private var productImg2: ImageUnify? = itemView.findViewById(R.id.terlaris_item_img_product_2)
        private var productName2: Typography? = itemView.findViewById(R.id.terlaris_item_product_name_2)
        private var productPrice2: Typography? = itemView.findViewById(R.id.terlaris_item_product_price_2)
        private var productRank2: Typography? = itemView.findViewById(R.id.terlaris_item_product_rank_number_2)
        private var productContainer3: ConstraintLayout? = itemView.findViewById(R.id.terlaris_item_product_detail_3)
        private var productImg3: ImageUnify? = itemView.findViewById(R.id.terlaris_item_img_product_3)
        private var productName3: Typography? = itemView.findViewById(R.id.terlaris_item_product_name_3)
        private var productPrice3: Typography? = itemView.findViewById(R.id.terlaris_item_product_price_3)
        private var productRank3: Typography? = itemView.findViewById(R.id.terlaris_item_product_rank_number_3)

        init {
            context = itemView.context
        }

        fun bindData(productListData: List<ShopHomeProductUiModel>, rank: List<Int>) {
            if (!productListData.size.isZero() && productListData.size == PRODUCT_THREE) {
                fontColor?.let {
                    // If fontColor equals to null then use default color from xml layout or
                    // use Dark/ light mode from user preferences
                    overrideWidgetTheme(fontColor = it)
                }

                productContainer1?.setOnClickListener {
                    listener.onProductClick(productId = productListData[0].id)
                }
                productImg1?.loadImageRounded(url = productListData[0].imageUrl.orEmpty())
                productName1?.text = productListData[0].name
                productPrice1?.text = productListData[0].displayedPrice
                productRank1?.text = rank[0].toString()
                productContainer2?.setOnClickListener {
                    listener.onProductClick(productId = productListData[1].id)
                }
                productImg2?.loadImageRounded(url = productListData[1].imageUrl.orEmpty())
                productName2?.text = productListData[1].name
                productPrice2?.text = productListData[1].displayedPrice
                productRank2?.text = rank[1].toString()
                productContainer3?.setOnClickListener {
                    listener.onProductClick(productId = productListData[2].id)
                }
                productImg3?.loadImageRounded(url = productListData[2].imageUrl.orEmpty())
                productName3?.text = productListData[2].name
                productPrice3?.text = productListData[2].displayedPrice
                productRank3?.text = rank[2].toString()
            }
        }

        private fun overrideWidgetTheme(fontColor: Int) {
            productName1?.setTextColor(ContextCompat.getColor(itemView.context, fontColor))
            productPrice1?.setTextColor(ContextCompat.getColor(itemView.context, fontColor))
            productName2?.setTextColor(ContextCompat.getColor(itemView.context, fontColor))
            productPrice2?.setTextColor(ContextCompat.getColor(itemView.context, fontColor))
            productName3?.setTextColor(ContextCompat.getColor(itemView.context, fontColor))
            productPrice3?.setTextColor(ContextCompat.getColor(itemView.context, fontColor))
        }
    }
}
