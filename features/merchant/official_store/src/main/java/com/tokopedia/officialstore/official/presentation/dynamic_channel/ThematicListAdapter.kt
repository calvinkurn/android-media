package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.officialstore.R
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid

// TODO: Update data mapping when back-end finished
class ThematicListAdapter : RecyclerView.Adapter<ThematicListAdapter.ThematicItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ThematicItemViewHolder(
            LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.dynamic_channel_thematic_list_item, parent, false)
    )

    override fun getItemCount() = 6

    override fun onBindViewHolder(holder: ThematicItemViewHolder, position: Int) {
        holder.productCard.apply {
            setLinesProductTitle(2)
            setProductModel(
                ProductCardModel(
                    productImageUrl = "https://ecs7.tokopedia.net/img/cache/700/product-1/2019/9/18/4034091/4034091_2e5e8833-6903-409c-9e47-b83375124d28_457_457.jpg",
                    isWishlistVisible = false,
                    productName = "Nike Air Force 1 Utility Black Something Else",
                    formattedPrice = "Rp 399.000",
                    shopName = "Store Name"
            ),
                BlankSpaceConfig(
                    price = true,
                    productName = true
            ))
        }
    }

    class ThematicItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productCard: ProductCardViewSmallGrid by lazy {
            view.findViewById<ProductCardViewSmallGrid>(R.id.dc_thematic_card_item)
        }
    }
}
