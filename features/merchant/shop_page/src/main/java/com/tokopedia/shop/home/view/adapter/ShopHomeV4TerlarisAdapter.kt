package com.tokopedia.shop.home.view.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.shop.R
import com.tokopedia.shop.home.util.loadImageRounded
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeV4TerlarisViewHolder
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeV4TerlarisUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ShopHomeV4TerlarisAdapter(
    private val listener: ShopHomeV4TerlarisViewHolder.ShopHomeV4TerlarisViewHolderListener
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
        holder.bindData(productListData[position])
    }

    override fun getItemCount(): Int {
        return productListData.size
    }

    inner class TerlarisWidgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val context: Context
        private var productContainer1: ConstraintLayout? = itemView.findViewById(R.id.terlaris_item_products)
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

        fun bindData(productListData: List<ShopHomeProductUiModel>) {
//            productContainer1?.setOnClickListener {
//                listener.onProductClick(productId = productListData[0].id)
//            }
//            productImg1?.loadImageRounded(url = productListData[0].imageUrl.orEmpty())
//            productName1?.text = productListData[0].name
//            productPrice1?.text = productListData[0].displayedPrice
//            productRank1?.text = "1"
//            productContainer2?.setOnClickListener {
//                listener.onProductClick(productId = productListData[1].id)
//            }
//            productImg2?.loadImageRounded(url = productListData[1].imageUrl.orEmpty())
//            productName2?.text = productListData[1].name
//            productPrice2?.text = productListData[1].displayedPrice
//            productRank2?.text = "2"
//            productContainer3?.setOnClickListener {
//                listener.onProductClick(productId = productListData[2].id)
//            }
//            productImg3?.loadImageRounded(url = productListData[2].imageUrl.orEmpty())
//            productName3?.text = productListData[2].name
//            productPrice3?.text = productListData[2].displayedPrice
//            productRank3?.text = "3"
        }
    }
}
