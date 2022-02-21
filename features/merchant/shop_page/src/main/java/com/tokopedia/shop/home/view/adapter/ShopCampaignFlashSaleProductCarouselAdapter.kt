package com.tokopedia.shop.home.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeFlashSaleProductCardBigGridViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeFlashSaleProductCardGridViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeFlashSaleProductCardPlaceHolderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeFlashSaleProductListViewHolder
import com.tokopedia.shop.home.view.listener.ShopHomeFlashSaleWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

class ShopCampaignFlashSaleProductCarouselAdapter(val listener: ShopHomeFlashSaleWidgetListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var flashSaleUiModel: ShopHomeFlashSaleUiModel? = null
    var flashSaleProductList: List<ShopHomeProductUiModel> = listOf()

    companion object {
        private const val SINGLE_PRODUCT = 1
        private const val DOUBLE_PRODUCT = 2
        private const val PRODUCT_CARD_LIST = 1
        private const val PRODUCT_CARD_BIG_GRID = 2
        private const val PRODUCT_CARD_GRID = 3
        private const val PLACEHOLDER = 4
    }

    override fun getItemViewType(position: Int): Int {
        val uiModel = flashSaleProductList[position]
        if (uiModel.isProductPlaceHolder) return PLACEHOLDER
        return when (flashSaleProductList.size) {
            SINGLE_PRODUCT -> PRODUCT_CARD_LIST
            DOUBLE_PRODUCT -> PRODUCT_CARD_BIG_GRID
            else -> PRODUCT_CARD_GRID
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PLACEHOLDER -> {
                val itemView = parent.inflateLayout(R.layout.item_shop_home_flash_sale_product_card_placeholder)
                ShopHomeFlashSaleProductCardPlaceHolderViewHolder(itemView, listener)
            }
            PRODUCT_CARD_LIST -> {
                val itemView = parent.inflateLayout(R.layout.item_shop_home_flash_sale_product_card_list)
                ShopHomeFlashSaleProductListViewHolder(itemView, listener)
            }
            PRODUCT_CARD_BIG_GRID -> {
                val itemView = parent.inflateLayout(R.layout.item_shop_home_flash_sale_product_card_grid_big)
                ShopHomeFlashSaleProductCardBigGridViewHolder(itemView, listener)
            }
            else -> {
                val itemView = parent.inflateLayout(R.layout.item_shop_home_flash_sale_product_card_grid)
                ShopHomeFlashSaleProductCardGridViewHolder(itemView, listener)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            PLACEHOLDER -> {
                val productCardPlaceHolderViewHolder = holder as ShopHomeFlashSaleProductCardPlaceHolderViewHolder
                productCardPlaceHolderViewHolder.bindData(uiModel = flashSaleProductList[position], fsUiModel = flashSaleUiModel)
            }
            PRODUCT_CARD_LIST -> {
                val productCardListViewHolder = holder as ShopHomeFlashSaleProductListViewHolder
                productCardListViewHolder.bindData(uiModel = flashSaleProductList[position])
            }
            PRODUCT_CARD_BIG_GRID -> {
                val productCardBigGridViewHolder =
                    holder as ShopHomeFlashSaleProductCardBigGridViewHolder
                productCardBigGridViewHolder.bindData(uiModel = flashSaleProductList[position])
            }
            else -> {
                val productCardGridViewHolder = holder as ShopHomeFlashSaleProductCardGridViewHolder
                productCardGridViewHolder.bindData(uiModel = flashSaleProductList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return flashSaleProductList.size
    }

    fun setProductList(productList: List<ShopHomeProductUiModel>) {
        this.flashSaleProductList = productList
        notifyDataSetChanged()
    }

    fun setFsUiModel(flashSaleUiModel: ShopHomeFlashSaleUiModel) {
        this.flashSaleUiModel = flashSaleUiModel
    }
}