package com.tokopedia.wishlistcommon.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.wishlist_common.databinding.AddWishlistCollectionAdditionalSectionTextItemBinding
import com.tokopedia.wishlist_common.databinding.AddWishlistCollectionItemBinding
import com.tokopedia.wishlist_common.databinding.AddWishlistCollectionMainSectionTextItemBinding
import com.tokopedia.wishlistcommon.data.AddToWishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcommon.view.adapter.viewholder.BottomSheetCollectionWishlistAdditionalItemViewHolder
import com.tokopedia.wishlistcommon.view.adapter.viewholder.BottomSheetCollectionWishlistItemViewHolder
import com.tokopedia.wishlistcommon.view.adapter.viewholder.BottomSheetCollectionWishlistMainItemViewHolder
import com.tokopedia.wishlistcommon.view.bottomsheet.BottomSheetAddCollectionWishlist

class BottomSheetCollectionWishlistAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var actionListener: ActionListener? = null
    private var listTypeData = mutableListOf<AddToWishlistCollectionTypeLayoutData>()

    companion object {
        const val LAYOUT_MAIN_SECTION = 0
        const val LAYOUT_ADDITIONAL_SECTION = 1
        const val LAYOUT_COLLECTION_ITEM = 2
        const val LAYOUT_CREATE_NEW_COLLECTION = 3
    }

    interface ActionListener {
        fun onCollectionItemClicked()
        fun onCreateNewCollectionClicked()
    }

    init { setHasStableIds(true) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LAYOUT_MAIN_SECTION  -> {
                val binding = AddWishlistCollectionMainSectionTextItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BottomSheetCollectionWishlistMainItemViewHolder(binding)
            }
            LAYOUT_ADDITIONAL_SECTION -> {
                val binding = AddWishlistCollectionAdditionalSectionTextItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BottomSheetCollectionWishlistAdditionalItemViewHolder(binding)
            }
            LAYOUT_COLLECTION_ITEM -> {
                val binding = AddWishlistCollectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BottomSheetCollectionWishlistItemViewHolder(binding)
            }
            LAYOUT_LIST -> {
                val binding = WishlistV2ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WishlistV2ListItemViewHolder(binding, actionListener)
            }
            LAYOUT_GRID -> {
                val binding = WishlistV2GridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WishlistV2GridItemViewHolder(binding, actionListener)
            }
            LAYOUT_EMPTY_STATE -> {
                val binding = WishlistV2EmptyStateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WishlistV2EmptyStateViewHolder(binding, actionListener)
            }
            LAYOUT_EMPTY_STATE_CAROUSEL -> {
                val binding = WishlistV2EmptyStateCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WishlistV2EmptyStateCarouselViewHolder(binding, actionListener)
            }
            LAYOUT_EMPTY_NOT_FOUND -> {
                val binding = WishlistV2EmptyStateNotFoundItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WishlistV2EmptyStateNotFoundViewHolder(binding, actionListener)
            }
            LAYOUT_RECOMMENDATION_TITLE -> {
                val binding = WishlistV2RecommendationTitleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WishlistV2RecommendationTitleViewHolder(binding, false)
            }
            LAYOUT_RECOMMENDATION_LIST -> {
                val binding = WishlistV2RecommendationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WishlistV2RecommendationItemViewHolder(binding, actionListener)
            }
            LAYOUT_TOPADS -> {
                val binding = WishlistV2TdnItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WishlistV2TdnViewHolder(binding, actionListener)
            }
            LAYOUT_RECOMMENDATION_CAROUSEL -> {
                val binding = WishlistV2RecommendationCarouselItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WishlistV2RecommendationCarouselViewHolder(binding, actionListener)
            }
            LAYOUT_RECOMMENDATION_TITLE_WITH_MARGIN -> {
                val binding = WishlistV2RecommendationTitleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WishlistV2RecommendationTitleViewHolder(binding, true)
            }
            LAYOUT_TICKER -> {
                val binding = WishlistV2TickerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WishlistV2TickerViewHolder(binding, actionListener)
            }
            LAYOUT_DELETION_PROGRESS_WIDGET -> {
                val binding = WishlistV2CountDeletionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WishlistV2DeletionProgressWidgetItemViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            val element = listTypeData[position]
            when (element.typeLayout) {
                TYPE_COUNT_MANAGE_ROW -> {
                    val params = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    if (isRefreshing) {
                        isRefreshing = false
                        (holder as WishlistV2CountManageRowItemViewHolder).setManageLabel(holder.itemView.context.getString(R.string.wishlist_manage_label))
                    }
                    (holder as WishlistV2CountManageRowItemViewHolder).bind(element, isShowCheckbox)
                }
                TYPE_LOADER_LIST -> {
                    val params = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2ListLoaderViewHolder).bind()
                }
                TYPE_LOADER_GRID -> {
                    val params = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = false
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2GridLoaderViewHolder).bind()
                }
                TYPE_LIST -> {
                    val params = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2ListItemViewHolder).bind(element, holder.adapterPosition, isShowCheckbox, isAutoSelected)
                }
                TYPE_GRID -> {
                    val params = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = false
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2GridItemViewHolder).bind(element, holder.adapterPosition, isShowCheckbox, isAutoSelected)
                }
                TYPE_EMPTY_STATE -> {
                    val params = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2EmptyStateViewHolder).bind(element)
                }
                TYPE_EMPTY_STATE_CAROUSEL -> {
                    val params = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2EmptyStateCarouselViewHolder).bind()
                }
                TYPE_EMPTY_NOT_FOUND -> {
                    val params = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2EmptyStateNotFoundViewHolder).bind(element)
                }
                TYPE_RECOMMENDATION_LIST -> {
                    val params = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = false
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2RecommendationItemViewHolder).bind(element, holder.adapterPosition)
                }
                TYPE_RECOMMENDATION_TITLE -> {
                    val params = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2RecommendationTitleViewHolder).bind(element, isShowCheckbox)
                }
                TYPE_TOPADS -> {
                    val params = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2TdnViewHolder).bind(element, holder.adapterPosition, isShowCheckbox)
                }
                TYPE_RECOMMENDATION_CAROUSEL -> {
                    val params = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2RecommendationCarouselViewHolder).bind(element, holder.adapterPosition, isShowCheckbox)
                }
                TYPE_RECOMMENDATION_TITLE_WITH_MARGIN -> {
                    val params = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2RecommendationTitleViewHolder).bind(element, isShowCheckbox)
                }
                TYPE_TICKER -> {
                    val params = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2TickerViewHolder).bind(element, isTickerCloseClicked, isShowCheckbox)
                }
                TYPE_DELETION_PROGRESS_WIDGET -> {
                    val params = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2DeletionProgressWidgetItemViewHolder).bind(element)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listTypeData.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return when (listTypeData[position].typeLayout) {
            TYPE_COUNT_MANAGE_ROW -> LAYOUT_COUNT_MANAGE_ROW
            TYPE_LOADER_LIST -> LAYOUT_LOADER_LIST
            TYPE_LOADER_GRID -> LAYOUT_LOADER_GRID
            TYPE_LIST -> LAYOUT_LIST
            TYPE_GRID -> LAYOUT_GRID
            TYPE_EMPTY_STATE -> LAYOUT_EMPTY_STATE
            TYPE_EMPTY_STATE_CAROUSEL -> LAYOUT_EMPTY_STATE_CAROUSEL
            TYPE_EMPTY_NOT_FOUND -> LAYOUT_EMPTY_NOT_FOUND
            TYPE_RECOMMENDATION_LIST -> LAYOUT_RECOMMENDATION_LIST
            TYPE_RECOMMENDATION_TITLE -> LAYOUT_RECOMMENDATION_TITLE
            TYPE_TOPADS -> LAYOUT_TOPADS
            TYPE_RECOMMENDATION_CAROUSEL -> LAYOUT_RECOMMENDATION_CAROUSEL
            TYPE_RECOMMENDATION_TITLE_WITH_MARGIN -> LAYOUT_RECOMMENDATION_TITLE_WITH_MARGIN
            TYPE_TICKER -> LAYOUT_TICKER
            TYPE_DELETION_PROGRESS_WIDGET -> LAYOUT_DELETION_PROGRESS_WIDGET
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    fun addList(list: List<AddToWishlistCollectionTypeLayoutData>) {
        listTypeData.clear()
        listTypeData.addAll(list)
        notifyDataSetChanged()
    }

    fun setActionListener(bottomsheet: BottomSheetAddCollectionWishlist) {
        this.actionListener = bottomsheet
    }
}