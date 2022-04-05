package com.tokopedia.createpost.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.data.non_seller_model.SearchShopModel
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class SearchResultShopListAdapter(
    private val itemList: MutableList<SearchShopModel.AceSearchShop.ShopItem> = mutableListOf(),
) :
    RecyclerView.Adapter<SearchResultShopListAdapter.BarangItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarangItemViewHolder {
        return BarangItemViewHolder(parent.inflateLayout(R.layout.search_result_item_toko_view))

    }

    override fun onBindViewHolder(holder: BarangItemViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    fun updateList(shopItemDataList: List <SearchShopModel.AceSearchShop.ShopItem>) {
        itemList.addAll(shopItemDataList)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    inner class BarangItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(shopItemData:  SearchShopModel.AceSearchShop.ShopItem) {
            with(itemView) {
                val shopBadge = findViewById<ImageUnify>(R.id.shop_badge)
                val shopName = findViewById<Typography>(R.id.shop_name)
                val shopLocation = findViewById<Typography>(R.id.shop_location)
                val shopImage = findViewById<ImageUnify>(R.id.shop_image)
                shopLocation.text = shopItemData.location
                shopImage.loadImageCircle(shopItemData.image)

                initImageShopBadge(shopItemData, shopBadge)
                shopName.text = shopItemData.name


            }
        }
        private fun initImageShopBadge(shopItemData:  SearchShopModel.AceSearchShop.ShopItem, shopBadgeView: ImageUnify) {
            shopBadgeView.let { imageViewShopBadge ->
                val isImageShopBadgeVisible = getIsImageShopBadgeVisible(shopItemData)
                val isGoldShop = shopItemData.goldShop == ShopStatus.KEY_SHOP_IS_GOLD

                imageViewShopBadge.shouldShowWithAction(isImageShopBadgeVisible) {
                    when {
                        shopItemData.isOfficial -> shopBadgeView.setImageResource(R.drawable.content_creation_ic_official_store)
                        shopItemData.isPMPro -> shopBadgeView.setImageResource(R.drawable.content_creation_ic_pm_pro)
                        isGoldShop -> shopBadgeView.setImageResource(R.drawable.ic_content_creation_power_merchant)
                    }
                }
            }
        }

        private fun getIsImageShopBadgeVisible(shopItem: SearchShopModel.AceSearchShop.ShopItem): Boolean {
            return shopItem.isOfficial
                    || shopItem.isPMPro
                    || shopItem.goldShop == ShopStatus.KEY_SHOP_IS_GOLD
        }

    }

    interface ShopStatus {
        companion object {
            const val KEY_SHOP_IS_GOLD = 1
            const val KEY_SHOP_STATUS_CLOSED = 2
            const val KEY_SHOP_STATUS_MODERATED = 3
            const val KEY_SHOP_STATUS_INACTIVE = 4
        }
    }


}