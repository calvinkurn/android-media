package com.tokopedia.wishlist.collection.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.wishlist.collection.data.model.WishlistCollectionTypeLayoutData
import com.tokopedia.wishlist.collection.data.response.GetWishlistCollectionResponse
import com.tokopedia.wishlist.collection.util.WishlistCollectionConsts.TYPE_COLLECTION_CREATE
import com.tokopedia.wishlist.collection.util.WishlistCollectionConsts.TYPE_COLLECTION_DIVIDER
import com.tokopedia.wishlist.collection.util.WishlistCollectionConsts.TYPE_COLLECTION_EMPTY_CAROUSEL
import com.tokopedia.wishlist.collection.util.WishlistCollectionConsts.TYPE_COLLECTION_ITEM
import com.tokopedia.wishlist.collection.util.WishlistCollectionConsts.TYPE_COLLECTION_LOADER
import com.tokopedia.wishlist.collection.util.WishlistCollectionConsts.TYPE_COLLECTION_TICKER
import com.tokopedia.wishlist.collection.view.adapter.viewholder.WishlistCollectionCreateItemViewHolder
import com.tokopedia.wishlist.collection.view.adapter.viewholder.WishlistCollectionDividerViewHolder
import com.tokopedia.wishlist.collection.view.adapter.viewholder.WishlistCollectionEmptyStateCarouselViewHolder
import com.tokopedia.wishlist.collection.view.adapter.viewholder.WishlistCollectionItemViewHolder
import com.tokopedia.wishlist.collection.view.adapter.viewholder.WishlistCollectionLoaderItemViewHolder
import com.tokopedia.wishlist.collection.view.adapter.viewholder.WishlistCollectionRecommendationItemViewHolder
import com.tokopedia.wishlist.collection.view.adapter.viewholder.WishlistCollectionRecommendationTitleViewHolder
import com.tokopedia.wishlist.collection.view.adapter.viewholder.WishlistCollectionTickerItemViewHolder
import com.tokopedia.wishlist.collection.view.fragment.WishlistCollectionFragment
import com.tokopedia.wishlist.databinding.CollectionWishlistCreateItemBinding
import com.tokopedia.wishlist.databinding.CollectionWishlistItemBinding
import com.tokopedia.wishlist.databinding.CollectionWishlistLoaderItemBinding
import com.tokopedia.wishlist.databinding.CollectionWishlistTickerItemBinding
import com.tokopedia.wishlist.databinding.WishlistCollectionDividerItemBinding
import com.tokopedia.wishlist.databinding.WishlistCollectionEmptyStateCarouselBinding
import com.tokopedia.wishlist.databinding.WishlistRecommendationItemBinding
import com.tokopedia.wishlist.databinding.WishlistRecommendationTitleItemBinding
import com.tokopedia.wishlist.detail.util.WishlistConsts
import com.tokopedia.wishlist.detail.view.adapter.WishlistAdapter

class WishlistCollectionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var actionListener: ActionListener? = null
    private var listTypeData = mutableListOf<WishlistCollectionTypeLayoutData>()
    private var allCollectionView: View? = null
    private var carouselItems = arrayListOf<Any>()

    companion object {
        const val LAYOUT_COLLECTION_TICKER = 0
        const val LAYOUT_COLLECTION_ITEM = 1
        const val LAYOUT_CREATE_COLLECTION = 2
        const val LAYOUT_EMPTY_COLLECTION = 3
        const val LAYOUT_DIVIDER = 5

        // note: 16 because type layout is joined with type in WishlistV2Adapter, to prevent any conflict/crashed
        const val LAYOUT_LOADER = 16
        const val START_LOADER = 0
        const val TOTAL_LOADER = 6
    }

    interface ActionListener {
        fun onCloseTicker()
        fun onKebabMenuClicked(
            collectionId: String,
            collectionName: String,
            actions: List<GetWishlistCollectionResponse.GetWishlistCollections.WishlistCollectionResponseData.Action>,
            collectionIndicatorTitle: String
        )

        fun onCreateNewCollectionClicked()
        fun onCollectionItemClicked(id: String)
        fun onCreateCollectionItemBind(allCollectionView: View, createCollectionView: View)
        fun onFirstCollectionItemBind(
            anchorKebabMenuView: View,
            collectionId: String,
            collectionName: String,
            actions: List<GetWishlistCollectionResponse.GetWishlistCollections.WishlistCollectionResponseData.Action>,
            collectionIndicatorTitle: String
        )

        fun onCariBarangClicked()
        fun onRecommendationItemImpression(recommendationItem: RecommendationItem, position: Int)
        fun onRecommendationItemClick(recommendationItem: RecommendationItem, position: Int)
        fun onAffiliateTickerCtaClick() {}
    }

    fun setActionListener(collectionWishlistFragment: WishlistCollectionFragment) {
        this.actionListener = collectionWishlistFragment
    }

    fun setCarouselEmptyData(carouselEmptyData: ArrayList<Any>) {
        carouselItems = carouselEmptyData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LAYOUT_LOADER -> {
                val binding = CollectionWishlistLoaderItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistCollectionLoaderItemViewHolder(binding)
            }
            LAYOUT_COLLECTION_TICKER -> {
                val binding = CollectionWishlistTickerItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistCollectionTickerItemViewHolder(binding, actionListener)
            }
            LAYOUT_COLLECTION_ITEM -> {
                val binding = CollectionWishlistItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistCollectionItemViewHolder(binding, actionListener)
            }
            LAYOUT_CREATE_COLLECTION -> {
                val binding = CollectionWishlistCreateItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistCollectionCreateItemViewHolder(binding, actionListener)
            }
            LAYOUT_EMPTY_COLLECTION -> {
                val binding = WishlistCollectionEmptyStateCarouselBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistCollectionEmptyStateCarouselViewHolder(binding, actionListener)
            }
            WishlistAdapter.LAYOUT_RECOMMENDATION_TITLE -> {
                val binding = WishlistRecommendationTitleItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistCollectionRecommendationTitleViewHolder(binding, false)
            }
            WishlistAdapter.LAYOUT_RECOMMENDATION_LIST -> {
                val binding = WishlistRecommendationItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistCollectionRecommendationItemViewHolder(binding, actionListener)
            }
            LAYOUT_DIVIDER -> {
                val binding = WishlistCollectionDividerItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistCollectionDividerViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemView.layoutParams is GridLayoutManager.LayoutParams) {
            val element = listTypeData[position]
            when (element.typeLayout) {
                TYPE_COLLECTION_LOADER -> {
                    (holder as WishlistCollectionLoaderItemViewHolder).bind()
                }
                TYPE_COLLECTION_TICKER -> {
                    (holder as WishlistCollectionTickerItemViewHolder).bind(element)
                }
                TYPE_COLLECTION_ITEM -> {
                    (holder as WishlistCollectionItemViewHolder).bind(element, position)
                    allCollectionView = if (holder.isAllWishlist) holder.itemView else null
                }
                TYPE_COLLECTION_CREATE -> {
                    (holder as WishlistCollectionCreateItemViewHolder).bind(element)
                    actionListener?.onCreateCollectionItemBind(
                        allCollectionView ?: holder.itemView,
                        holder.itemView
                    )
                }
                WishlistConsts.TYPE_RECOMMENDATION_LIST -> {
                    (holder as WishlistCollectionRecommendationItemViewHolder).bind(
                        element,
                        holder.adapterPosition
                    )
                }
                WishlistConsts.TYPE_RECOMMENDATION_TITLE -> {
                    (holder as WishlistCollectionRecommendationTitleViewHolder).bind(element)
                }
                TYPE_COLLECTION_EMPTY_CAROUSEL -> {
                    (holder as WishlistCollectionEmptyStateCarouselViewHolder).bind(carouselItems)
                }
                TYPE_COLLECTION_DIVIDER -> {
                    (holder as WishlistCollectionDividerViewHolder).bind()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listTypeData.size
    }

    fun getItems(): List<WishlistCollectionTypeLayoutData> {
        return listTypeData
    }

    override fun getItemViewType(position: Int): Int {
        return when (listTypeData[position].typeLayout) {
            TYPE_COLLECTION_TICKER -> LAYOUT_COLLECTION_TICKER
            TYPE_COLLECTION_ITEM -> LAYOUT_COLLECTION_ITEM
            TYPE_COLLECTION_CREATE -> LAYOUT_CREATE_COLLECTION
            TYPE_COLLECTION_EMPTY_CAROUSEL -> LAYOUT_EMPTY_COLLECTION
            WishlistConsts.TYPE_RECOMMENDATION_LIST -> WishlistAdapter.LAYOUT_RECOMMENDATION_LIST
            WishlistConsts.TYPE_RECOMMENDATION_TITLE -> WishlistAdapter.LAYOUT_RECOMMENDATION_TITLE
            TYPE_COLLECTION_DIVIDER -> LAYOUT_DIVIDER
            TYPE_COLLECTION_LOADER -> LAYOUT_LOADER
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    fun set(items: List<WishlistCollectionTypeLayoutData>) {
        val diffCallback = WishlistDiffUtilCallback(listTypeData.toList(), items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listTypeData.clear()
        listTypeData.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    fun add(items: List<WishlistCollectionTypeLayoutData>) {
        val newItems = listTypeData.toMutableList().apply { addAll(items) }
        val diffCallback = WishlistDiffUtilCallback(listTypeData.toList(), newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listTypeData.clear()
        listTypeData.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    fun removeTicker() {
        val position = listTypeData.indexOfFirst { it.typeLayout == TYPE_COLLECTION_TICKER }
        if (position != -1) {
            listTypeData.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun showLoader() {
        set(
            (START_LOADER until TOTAL_LOADER).map { index ->
                WishlistCollectionTypeLayoutData(
                    "${TYPE_COLLECTION_LOADER}_$index",
                    "",
                    TYPE_COLLECTION_LOADER
                )
            }
        )
    }

    fun getRecommendationItemAtIndex(index: Int): ProductCardModel {
        return listTypeData[index].dataObject as ProductCardModel
    }

    inner class WishlistDiffUtilCallback(
        private val oldItems: List<WishlistCollectionTypeLayoutData>,
        private val newItems: List<WishlistCollectionTypeLayoutData>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldItems.size
        }

        override fun getNewListSize(): Int {
            return newItems.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition].id == newItems[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }
    }
}
