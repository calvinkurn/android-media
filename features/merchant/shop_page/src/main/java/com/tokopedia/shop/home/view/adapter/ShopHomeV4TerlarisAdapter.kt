package com.tokopedia.shop.home.view.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.shop.R
import com.tokopedia.shop.home.util.loadImageRounded
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeV4TerlarisViewHolder
import com.tokopedia.shop.home.view.model.ShopHomeV4TerlarisUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ShopHomeV4TerlarisAdapter(
    private val listener: ShopHomeV4TerlarisViewHolder.ShopHomeV4TerlarisViewHolderListener
) : RecyclerView.Adapter<ShopHomeV4TerlarisAdapter.TerlarisWidgetViewHolder>() {

    private var productListData: List<List<ShopHomeV4TerlarisUiModel.ShopHomeV4TerlarisItemUiModel>> = listOf()

    fun updateData(productList: List<List<ShopHomeV4TerlarisUiModel.ShopHomeV4TerlarisItemUiModel>>) {
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
        private var productContainer1: ConstraintLayout? = itemView.findViewById(R.id.terlaris_product_detail_1)
        private var productImg1: ImageUnify? = itemView.findViewById(R.id.terlaris_img_product_1)
        private var productName1: Typography? = itemView.findViewById(R.id.terlaris_product_name_1)
        private var productPrice1: Typography? = itemView.findViewById(R.id.terlaris_product_price_1)
        private var productRank1: Typography? = itemView.findViewById(R.id.terlaris_product_rank_number_1)
        private var productContainer2: ConstraintLayout? = itemView.findViewById(R.id.terlaris_product_detail_2)
        private var productImg2: ImageUnify? = itemView.findViewById(R.id.terlaris_img_product_1)
        private var productName2: Typography? = itemView.findViewById(R.id.terlaris_product_name_1)
        private var productPrice2: Typography? = itemView.findViewById(R.id.terlaris_product_price_1)
        private var productRank2: Typography? = itemView.findViewById(R.id.terlaris_product_rank_number_1)
        private var productContainer3: ConstraintLayout? = itemView.findViewById(R.id.terlaris_product_detail_3)
        private var productImg3: ImageUnify? = itemView.findViewById(R.id.terlaris_img_product_3)
        private var productName3: Typography? = itemView.findViewById(R.id.terlaris_product_name_3)
        private var productPrice3: Typography? = itemView.findViewById(R.id.terlaris_product_price_3)
        private var productRank3: Typography? = itemView.findViewById(R.id.terlaris_product_rank_number_3)

        init {
            context = itemView.context
        }

        fun bindData(productListData: List<ShopHomeV4TerlarisUiModel.ShopHomeV4TerlarisItemUiModel>) {
            productContainer1?.setOnClickListener {
                listener.onProductClick(productId = productListData[0].appUrl)
            }
            productImg1?.loadImageRounded(url = productListData[0].imgUrl)
            productName1?.text = productListData[0].name
            productPrice1?.text = productListData[0].price
            productRank1?.text = "0"
            productContainer2?.setOnClickListener {
                listener.onProductClick(productId = productListData[1].appUrl)
            }
            productImg2?.loadImageRounded(url = productListData[1].imgUrl)
            productName2?.text = productListData[1].name
            productPrice2?.text = productListData[1].price
            productRank2?.text = "1"
            productContainer3?.setOnClickListener {
                listener.onProductClick(productId = productListData[2].appUrl)
            }
            productImg3?.loadImageRounded(url = productListData[2].imgUrl)
            productName3?.text = productListData[2].name
            productPrice3?.text = productListData[2].price
            productRank3?.text = "3"
        }
    }
}
