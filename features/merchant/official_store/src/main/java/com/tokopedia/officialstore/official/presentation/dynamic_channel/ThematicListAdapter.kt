package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Grid
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid

class ThematicListAdapter(
        private val ctx: Context?,
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
                                productImageUrl = item.imageUrl,
                                productName = item.name,
                                formattedPrice = item.price
                        ),
                        BlankSpaceConfig(
                                price = true,
                                productName = true
                        ))
                setOnClickListener {
                    RouteManager.route(ctx, item.applink)
                }
            }
        }
    }

    class ThematicItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productCard: ProductCardViewSmallGrid by lazy {
            view.findViewById<ProductCardViewSmallGrid>(R.id.dc_thematic_card_item)
        }
    }
}
