package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Grid
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid

// TODO: Update data mapping when back-end finished
class ThematicListAdapter(
        private val listData: MutableList<Grid?>
) : RecyclerView.Adapter<ThematicListAdapter.ThematicItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ThematicItemViewHolder(
            LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.dynamic_channel_thematic_list_item, parent, false)
    )

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ThematicItemViewHolder, position: Int) {
        val itemData = listData[position]

        itemData?.let { item ->
            holder.productCard.apply {
                setLinesProductTitle(2)
                setProductModel(
                        ProductCardModel(
                                isWishlistVisible = true,
                                productImageUrl = item.imageUrl,
                                productName = item.name,
                                formattedPrice = item.price
                        ),
                        BlankSpaceConfig(
                                price = true,
                                productName = true
                        ))
            }
        }
    }

    class ThematicItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productCard: ProductCardViewSmallGrid by lazy {
            view.findViewById<ProductCardViewSmallGrid>(R.id.dc_thematic_card_item)
        }
    }
}
