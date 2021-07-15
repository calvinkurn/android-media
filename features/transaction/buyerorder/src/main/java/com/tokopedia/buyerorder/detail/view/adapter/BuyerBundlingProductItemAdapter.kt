package com.tokopedia.buyerorder.detail.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.common.util.BuyerUtils.toCurrencyFormatted
import com.tokopedia.buyerorder.detail.view.adapter.uimodel.ProductBundleItem
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class BuyerBundlingProductItemAdapter(private val itemList: List<ProductBundleItem>): RecyclerView.Adapter<BuyerBundlingProductItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.bottomsheet_cancel_product_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList.getOrNull(position))
    }

    override fun getItemCount(): Int = itemList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val buyerBundleProductImage: ImageUnify? = itemView.findViewById(R.id.iv_item_buyer_order_bundling_thumbnail)
        private val buyerBundleProductNameText: Typography? = itemView.findViewById(R.id.tv_item_buyer_order_bundling_product_name)
        private val buyerBundleProductPriceText: Typography? = itemView.findViewById(R.id.tv_item_buyer_order_bundling_product_price)

        fun bind(uiModel: ProductBundleItem?) {
            uiModel?.let {
                buyerBundleProductImage?.setImageUrl(it.productThumbnailUrl)
                buyerBundleProductNameText?.text = it.productName
                buyerBundleProductPriceText?.text = it.productPrice.toCurrencyFormatted()
            }
        }

    }
}