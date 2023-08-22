package com.tokopedia.shop.home.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
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

class ShopCampaignFlashSaleProductCarouselAdapter(val listener: ShopHomeFlashSaleWidgetListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val SINGLE_PRODUCT = 1
        private const val DOUBLE_PRODUCT = 2
        private const val PRODUCT_CARD_LIST = 1
        private const val PRODUCT_CARD_BIG_GRID = 2
        private const val PRODUCT_CARD_GRID = 3
        private const val PLACEHOLDER = 4
    }

    private val differCallback = object :
        DiffUtil.ItemCallback<ShopHomeProductUiModel>() {
        override fun areItemsTheSame(
            oldItem: ShopHomeProductUiModel,
            newItem: ShopHomeProductUiModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ShopHomeProductUiModel,
            newItem: ShopHomeProductUiModel
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    var flashSaleUiModel: ShopHomeFlashSaleUiModel? = null

    var measureListener: HeightMeasureListener? = null

    var parentPosition: Int = -1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            PLACEHOLDER -> {
                val productCardPlaceHolderViewHolder =
                    holder as ShopHomeFlashSaleProductCardPlaceHolderViewHolder
                productCardPlaceHolderViewHolder.bindData(
                    uiModel = differ.currentList[position],
                    fsUiModel = flashSaleUiModel
                )
            }

            PRODUCT_CARD_LIST -> {
                val productCardListViewHolder = holder as ShopHomeFlashSaleProductListViewHolder
                productCardListViewHolder.bindData(
                    uiModel = differ.currentList[position],
                    fsUiModel = flashSaleUiModel
                )
            }

            PRODUCT_CARD_BIG_GRID -> {
                val productCardBigGridViewHolder =
                    holder as ShopHomeFlashSaleProductCardBigGridViewHolder
                productCardBigGridViewHolder.bindData(
                    uiModel = differ.currentList[position],
                    fsUiModel = flashSaleUiModel
                )
                productCardBigGridViewHolder.getHeightOfImageProduct { height ->
                    measureListener?.setHeightListener(height)
                }
            }

            else -> {
                val productCardGridViewHolder = holder as ShopHomeFlashSaleProductCardGridViewHolder
                productCardGridViewHolder.bindData(
                    uiModel = differ.currentList[position],
                    fsUiModel = flashSaleUiModel
                )
                productCardGridViewHolder.getHeightOfImageProduct { height ->
                    measureListener?.setHeightListener(height)
                }
            }
        }
    }

    fun setFsUiModel(flashSaleUiModel: ShopHomeFlashSaleUiModel) {
        this.flashSaleUiModel = flashSaleUiModel
    }

    fun setHeightMeasureListener(listener: HeightMeasureListener) {
        measureListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        val uiModel = differ.currentList[position]
        if (uiModel.isProductPlaceHolder) return PLACEHOLDER
        return when (differ.currentList.size) {
            SINGLE_PRODUCT -> PRODUCT_CARD_LIST
            DOUBLE_PRODUCT -> PRODUCT_CARD_BIG_GRID
            else -> PRODUCT_CARD_GRID
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PLACEHOLDER -> {
                val itemView =
                    parent.inflateLayout(R.layout.item_shop_home_flash_sale_product_card_placeholder)
                ShopHomeFlashSaleProductCardPlaceHolderViewHolder(itemView, listener)
            }

            PRODUCT_CARD_LIST -> {
                val itemView =
                    parent.inflateLayout(R.layout.item_shop_home_flash_sale_product_card_list)
                ShopHomeFlashSaleProductListViewHolder(itemView, listener, parentPosition)
            }

            PRODUCT_CARD_BIG_GRID -> {
                val itemView =
                    parent.inflateLayout(R.layout.item_shop_home_flash_sale_product_card_grid_big)
                ShopHomeFlashSaleProductCardBigGridViewHolder(itemView, listener, parentPosition)
            }

            else -> {
                val itemView =
                    parent.inflateLayout(R.layout.item_shop_home_flash_sale_product_card_grid)
                ShopHomeFlashSaleProductCardGridViewHolder(itemView, listener, parentPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setProductList(data: List<ShopHomeProductUiModel>) {
        differ.submitList(data)
    }
}
