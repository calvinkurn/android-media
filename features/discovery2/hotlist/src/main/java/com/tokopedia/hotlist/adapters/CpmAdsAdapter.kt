package com.tokopedia.hotlist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.hotlist.R
import com.tokopedia.hotlist.data.cpmAds.CpmItem
import com.tokopedia.hotlist.interfaces.CpmTopAdsListener

class CpmAdsAdapter(private var cpmItemList: ArrayList<CpmItem>,
                    private var cpmTopAdsListener: CpmTopAdsListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        const val VIEW_SHOP = 0
        const val VIEW_PRODUCT = 1
        const val VIEW_SHIMMER = 2
        const val SHIMMER_ITEM_COUNT = 3
    }

    private var isCpmImpressionSent: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {

            VIEW_SHOP -> {
                val v = LayoutInflater.from(parent.context).inflate(ShopViewHolder.LAYOUT, parent, false)
                ShopViewHolder(v)
            }

            VIEW_PRODUCT -> {
                val v = LayoutInflater.from(parent.context).inflate(ProductViewHolder.LAYOUT, parent, false)
                ProductViewHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context).inflate(ShimmerViewHolder.LAYOUT, parent, false)
                ShimmerViewHolder(v)
            }
        }

    }

    override fun getItemCount(): Int {
        return if (cpmItemList.size <= 0) {
            SHIMMER_ITEM_COUNT  // done to load shimmer items
        } else {
            cpmItemList.size
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {

            VIEW_SHOP -> {
                setShopData(holder as ShopViewHolder, position)
            }
            VIEW_PRODUCT -> {
                setProductData(holder as ProductViewHolder, position)
            }
        }
    }

    private fun setShopData(holder: ShopViewHolder, position: Int) {
        val item = cpmItemList[position]
        holder.shopName.text = item.name
        holder.description.text = item.description
        ImageHandler.loadImageCircle2(holder.itemView.context,
                holder.shopImage,
                item.image)
        ImageHandler.loadImage(holder.itemView.context,
                holder.shopBadge,
                item.badge_url,
                com.tokopedia.topads.sdk.R.drawable.loading_page)
        holder.shopCpmParent.setOnClickListener {
            cpmTopAdsListener.onCpmClicked(item.applinks ?: "", item.click_url ?: "", item, item.id.toString(), item.name ?: "", item.image?: "")
        }
    }

    private fun setProductData(holder: ProductViewHolder, position: Int) {
        val item = cpmItemList[position]
        holder.productName.text = item.name
        holder.productPrice.text = item.price_format
        ImageHandler.loadImage(holder.itemView.context,
                holder.productImage,
                item.image, com.tokopedia.topads.sdk.R.drawable.loading_page)
        holder.productCpmParent.setOnClickListener {
            cpmTopAdsListener.onCpmClicked(item.applinks ?: "", item.click_url ?: "", item, item.id.toString(), item.name?: "", item.image?: "")
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (cpmItemList.size <= 0) {
            VIEW_SHIMMER
        } else {
            if (cpmItemList[position].is_product) {
                VIEW_PRODUCT
            } else
                VIEW_SHOP
        }
    }


    class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            val LAYOUT = R.layout.item_nav_hotlist_cpm_shop
        }

        val shopBadge: ImageView = itemView.findViewById(R.id.badge)
        val shopName: TextView = itemView.findViewById(R.id.shop_name)
        val description: TextView = itemView.findViewById(R.id.description)
        val shopImage: ImageView = itemView.findViewById(R.id.shop_image)
        val shopCpmParent: ConstraintLayout = itemView.findViewById(R.id.shop_cpm_parent)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            val LAYOUT = R.layout.item_nav_hotlist_cpm_product
        }

        val productImage: ImageView = itemView.findViewById(R.id.product_image)
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
        val productCpmParent: ConstraintLayout = itemView.findViewById(R.id.product_cpm_parent)
    }

    class ShimmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            val LAYOUT = R.layout.item_nav_cpm_shimmer
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        // one time impression only for shop item
        val position = holder.adapterPosition
        if (!isCpmImpressionSent && holder.itemViewType == VIEW_SHOP) {
            isCpmImpressionSent = true
            cpmItemList[position].apply {
                cpmTopAdsListener.onCpmImpression(this.impression_url ?: "", this.id.toString(), this.name ?: "", this.image ?: "")
            }
        }
    }
}