package com.tokopedia.wishlistcollection.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.wishlist.databinding.CollectionWishlistCreateItemBinding
import com.tokopedia.wishlist.databinding.CollectionWishlistItemBinding
import com.tokopedia.wishlist.databinding.CollectionWishlistLoaderItemBinding
import com.tokopedia.wishlist.databinding.CollectionWishlistTickerItemBinding
import com.tokopedia.wishlist.databinding.WishlistCollectionDividerItemBinding
import com.tokopedia.wishlist.databinding.WishlistCollectionEmptyStateCarouselBinding
import com.tokopedia.wishlist.databinding.WishlistV2RecommendationItemBinding
import com.tokopedia.wishlist.databinding.WishlistV2RecommendationTitleItemBinding
import com.tokopedia.wishlist.util.WishlistV2Consts
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter
import com.tokopedia.wishlistcollection.data.model.WishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionResponse
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.TYPE_COLLECTION_CREATE
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.TYPE_COLLECTION_DIVIDER
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.TYPE_COLLECTION_EMPTY_CAROUSEL
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.TYPE_COLLECTION_ITEM
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.TYPE_COLLECTION_LOADER
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.TYPE_COLLECTION_TICKER
import com.tokopedia.wishlistcollection.view.adapter.viewholder.WishlistCollectionCreateItemViewHolder
import com.tokopedia.wishlistcollection.view.adapter.viewholder.WishlistCollectionDividerViewHolder
import com.tokopedia.wishlistcollection.view.adapter.viewholder.WishlistCollectionEmptyStateCarouselViewHolder
import com.tokopedia.wishlistcollection.view.adapter.viewholder.WishlistCollectionItemViewHolder
import com.tokopedia.wishlistcollection.view.adapter.viewholder.WishlistCollectionLoaderItemViewHolder
import com.tokopedia.wishlistcollection.view.adapter.viewholder.WishlistCollectionRecommendationItemViewHolder
import com.tokopedia.wishlistcollection.view.adapter.viewholder.WishlistCollectionRecommendationTitleViewHolder
import com.tokopedia.wishlistcollection.view.adapter.viewholder.WishlistCollectionTickerItemViewHolder
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionFragment

class WishlistCollectionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var actionListener: ActionListener? = null
    private var listTypeData = mutableListOf<WishlistCollectionTypeLayoutData>()
    private var isTickerCloseClicked = false
    private var allCollectionView: View? = null
    private var firstCollectionItemView: View? = null
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
            WishlistV2Adapter.LAYOUT_RECOMMENDATION_TITLE -> {
                val binding = WishlistV2RecommendationTitleItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistCollectionRecommendationTitleViewHolder(binding, false)
            }
            WishlistV2Adapter.LAYOUT_RECOMMENDATION_LIST -> {
                val binding = WishlistV2RecommendationItemBinding.inflate(
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
                    (holder as WishlistCollectionTickerItemViewHolder).bind(
                        element,
                        isTickerCloseClicked
                    )
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
                WishlistV2Consts.TYPE_RECOMMENDATION_LIST -> {
                    (holder as WishlistCollectionRecommendationItemViewHolder).bind(
                        element,
                        holder.adapterPosition
                    )
                }
                WishlistV2Consts.TYPE_RECOMMENDATION_TITLE -> {
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

    override fun getItemViewType(position: Int): Int {
        return when (listTypeData[position].typeLayout) {
            TYPE_COLLECTION_TICKER -> LAYOUT_COLLECTION_TICKER
            TYPE_COLLECTION_ITEM -> LAYOUT_COLLECTION_ITEM
            TYPE_COLLECTION_CREATE -> LAYOUT_CREATE_COLLECTION
            TYPE_COLLECTION_EMPTY_CAROUSEL -> LAYOUT_EMPTY_COLLECTION
            WishlistV2Consts.TYPE_RECOMMENDATION_LIST -> WishlistV2Adapter.LAYOUT_RECOMMENDATION_LIST
            WishlistV2Consts.TYPE_RECOMMENDATION_TITLE -> WishlistV2Adapter.LAYOUT_RECOMMENDATION_TITLE
            TYPE_COLLECTION_DIVIDER -> LAYOUT_DIVIDER
            TYPE_COLLECTION_LOADER -> LAYOUT_LOADER
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    fun addList(list: List<WishlistCollectionTypeLayoutData>) {
        listTypeData.clear()
        listTypeData.addAll(list)
        notifyDataSetChanged()
    }

    fun setTickerHasClosed(hasClosed: Boolean) {
        isTickerCloseClicked = hasClosed
        notifyDataSetChanged()
    }

    fun showLoader() {
        listTypeData.clear()
        for (x in START_LOADER until TOTAL_LOADER) {
            listTypeData.add(WishlistCollectionTypeLayoutData("", TYPE_COLLECTION_LOADER))
        }
        notifyDataSetChanged()
    }

    fun getRecommendationItemAtIndex(index: Int): ProductCardModel {
        return listTypeData[index].dataObject as ProductCardModel
    }
}
