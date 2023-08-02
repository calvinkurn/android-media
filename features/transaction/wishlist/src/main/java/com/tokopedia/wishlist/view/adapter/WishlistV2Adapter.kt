package com.tokopedia.wishlist.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.data.model.WishlistV2RecommendationDataModel
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.data.model.WishlistV2UiModel
import com.tokopedia.wishlist.databinding.WishlistV2CountDeletionItemBinding
import com.tokopedia.wishlist.databinding.WishlistV2EmptyStateCarouselBinding
import com.tokopedia.wishlist.databinding.WishlistV2EmptyStateItemBinding
import com.tokopedia.wishlist.databinding.WishlistV2EmptyStateNotFoundItemBinding
import com.tokopedia.wishlist.databinding.WishlistV2GridItemBinding
import com.tokopedia.wishlist.databinding.WishlistV2ListItemBinding
import com.tokopedia.wishlist.databinding.WishlistV2LoaderGridItemBinding
import com.tokopedia.wishlist.databinding.WishlistV2LoaderListItemBinding
import com.tokopedia.wishlist.databinding.WishlistV2RecommendationCarouselItemBinding
import com.tokopedia.wishlist.databinding.WishlistV2RecommendationItemBinding
import com.tokopedia.wishlist.databinding.WishlistV2RecommendationTitleItemBinding
import com.tokopedia.wishlist.databinding.WishlistV2StickyItemBinding
import com.tokopedia.wishlist.databinding.WishlistV2TdnItemBinding
import com.tokopedia.wishlist.databinding.WishlistV2TickerItemBinding
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_COUNT_MANAGE_ROW
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_DELETION_PROGRESS_WIDGET
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_EMPTY_NOT_FOUND
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_EMPTY_STATE
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_EMPTY_STATE_CAROUSEL
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_EMPTY_STATE_COLLECTION
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_GRID
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_LIST
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_LOADER_GRID
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_LOADER_LIST
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_CAROUSEL
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_LIST
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_TITLE
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_TITLE_WITH_MARGIN
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_TICKER
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_TOPADS
import com.tokopedia.wishlist.view.adapter.viewholder.WishlistV2CountManageRowItemViewHolder
import com.tokopedia.wishlist.view.adapter.viewholder.WishlistV2DeletionProgressWidgetItemViewHolder
import com.tokopedia.wishlist.view.adapter.viewholder.WishlistV2EmptyStateCarouselViewHolder
import com.tokopedia.wishlist.view.adapter.viewholder.WishlistV2EmptyStateNotFoundViewHolder
import com.tokopedia.wishlist.view.adapter.viewholder.WishlistV2EmptyStateViewHolder
import com.tokopedia.wishlist.view.adapter.viewholder.WishlistV2GridItemViewHolder
import com.tokopedia.wishlist.view.adapter.viewholder.WishlistV2GridLoaderViewHolder
import com.tokopedia.wishlist.view.adapter.viewholder.WishlistV2ListItemViewHolder
import com.tokopedia.wishlist.view.adapter.viewholder.WishlistV2ListLoaderViewHolder
import com.tokopedia.wishlist.view.adapter.viewholder.WishlistV2RecommendationCarouselViewHolder
import com.tokopedia.wishlist.view.adapter.viewholder.WishlistV2RecommendationItemViewHolder
import com.tokopedia.wishlist.view.adapter.viewholder.WishlistV2RecommendationTitleViewHolder
import com.tokopedia.wishlist.view.adapter.viewholder.WishlistV2TdnViewHolder
import com.tokopedia.wishlist.view.adapter.viewholder.WishlistV2TickerViewHolder
import com.tokopedia.wishlist.view.fragment.WishlistV2Fragment
import com.tokopedia.wishlistcollection.view.adapter.viewholder.WishlistCollectionEmptyStateViewHolder
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionDetailFragment

class WishlistV2Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var actionListener: ActionListener? = null
    private var listTypeData = mutableListOf<WishlistV2TypeLayoutData>()
    private var isShowCheckbox = false
    private var isTickerCloseClicked = false
    var isRefreshing = false
    private var isAutoSelected = false
    private var isAddBulkModeFromOthers = false

    companion object {
        const val LAYOUT_LOADER_LIST = 0
        const val LAYOUT_LOADER_GRID = 1
        const val LAYOUT_LIST = 2
        const val LAYOUT_GRID = 3
        const val LAYOUT_EMPTY_STATE = 4
        const val LAYOUT_EMPTY_STATE_CAROUSEL = 5
        const val LAYOUT_RECOMMENDATION_TITLE = 6
        const val LAYOUT_RECOMMENDATION_LIST = 7
        const val LAYOUT_EMPTY_NOT_FOUND = 8
        const val LAYOUT_TOPADS = 9
        const val LAYOUT_RECOMMENDATION_CAROUSEL = 10
        const val LAYOUT_COUNT_MANAGE_ROW = 11
        const val LAYOUT_RECOMMENDATION_TITLE_WITH_MARGIN = 12
        const val LAYOUT_TICKER = 13
        const val LAYOUT_DELETION_PROGRESS_WIDGET = 14
        const val START_LOADER = 0
        const val TOTAL_LOADER = 5
        const val LAYOUT_EMPTY_STATE_COLLECTION = 15
    }

    interface ActionListener {
        fun onCariBarangClicked()
        fun onNotFoundButtonClicked(keyword: String)
        fun onThreeDotsMenuClicked(itemWishlist: WishlistV2UiModel.Item)
        fun onCheckBulkOption(productId: String, isChecked: Boolean, position: Int)
        fun onValidateCheckBulkOption(productId: String, isChecked: Boolean, position: Int)
        fun onUncheckAutomatedBulkDelete(productId: String, isChecked: Boolean, position: Int)
        fun onAtc(wishlistItem: WishlistV2UiModel.Item, position: Int)
        fun onCheckSimilarProduct(url: String)
        fun onResetFilter()
        fun onManageClicked(showCheckbox: Boolean, isDeleteOnly: Boolean, isBulkAdd: Boolean)
        fun onProductItemClicked(wishlistItem: WishlistV2UiModel.Item, position: Int)
        fun onBannerTopAdsImpression(topAdsImageViewModel: TopAdsImageViewModel, position: Int)
        fun onBannerTopAdsClick(topAdsImageViewModel: TopAdsImageViewModel, position: Int)
        fun onRecommendationItemImpression(recommendationItem: RecommendationItem, position: Int)
        fun onRecommendationItemClick(recommendationItem: RecommendationItem, position: Int)
        fun onRecommendationCarouselItemImpression(
            recommendationItem: RecommendationItem,
            position: Int
        )

        fun onRecommendationCarouselItemClick(recommendationItem: RecommendationItem, position: Int)
        fun onTickerCTAShowBottomSheet(bottomSheetCleanerData: WishlistV2UiModel.StorageCleanerBottomSheet)
        fun onTickerCTASortFromLatest()
        fun onTickerCloseIconClicked()
        fun goToWishlistAllToAddCollection()
        fun onChangeCollectionName()
        fun goToMyWishlist()
        fun goToHome()
        fun goToEditWishlistCollectionPage()
    }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LAYOUT_COUNT_MANAGE_ROW -> {
                val binding = WishlistV2StickyItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistV2CountManageRowItemViewHolder(binding, actionListener)
            }
            LAYOUT_LOADER_LIST -> {
                val binding = WishlistV2LoaderListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistV2ListLoaderViewHolder(binding)
            }
            LAYOUT_LOADER_GRID -> {
                val binding = WishlistV2LoaderGridItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistV2GridLoaderViewHolder(binding)
            }
            LAYOUT_LIST -> {
                val binding = WishlistV2ListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistV2ListItemViewHolder(binding, actionListener)
            }
            LAYOUT_GRID -> {
                val binding = WishlistV2GridItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistV2GridItemViewHolder(binding, actionListener)
            }
            LAYOUT_EMPTY_STATE -> {
                val binding = WishlistV2EmptyStateItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistV2EmptyStateViewHolder(binding, actionListener)
            }
            LAYOUT_EMPTY_STATE_CAROUSEL -> {
                val binding = WishlistV2EmptyStateCarouselBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistV2EmptyStateCarouselViewHolder(binding, actionListener)
            }
            LAYOUT_EMPTY_NOT_FOUND -> {
                val binding = WishlistV2EmptyStateNotFoundItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistV2EmptyStateNotFoundViewHolder(binding, actionListener)
            }
            LAYOUT_RECOMMENDATION_TITLE -> {
                val binding = WishlistV2RecommendationTitleItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistV2RecommendationTitleViewHolder(binding, false)
            }
            LAYOUT_RECOMMENDATION_LIST -> {
                val binding = WishlistV2RecommendationItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistV2RecommendationItemViewHolder(binding, actionListener)
            }
            LAYOUT_TOPADS -> {
                val binding = WishlistV2TdnItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistV2TdnViewHolder(binding, actionListener)
            }
            LAYOUT_RECOMMENDATION_CAROUSEL -> {
                val binding = WishlistV2RecommendationCarouselItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistV2RecommendationCarouselViewHolder(binding, actionListener)
            }
            LAYOUT_RECOMMENDATION_TITLE_WITH_MARGIN -> {
                val binding = WishlistV2RecommendationTitleItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistV2RecommendationTitleViewHolder(binding, true)
            }
            LAYOUT_TICKER -> {
                val binding = WishlistV2TickerItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistV2TickerViewHolder(binding, actionListener)
            }
            LAYOUT_DELETION_PROGRESS_WIDGET -> {
                val binding = WishlistV2CountDeletionItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistV2DeletionProgressWidgetItemViewHolder(binding)
            }
            LAYOUT_EMPTY_STATE_COLLECTION -> {
                val binding = WishlistV2EmptyStateItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistCollectionEmptyStateViewHolder(binding, actionListener)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            val element = listTypeData[position]
            when (element.typeLayout) {
                TYPE_COUNT_MANAGE_ROW -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    if (isRefreshing) {
                        isRefreshing = false
                        (holder as WishlistV2CountManageRowItemViewHolder).setManageLabel(
                            holder.itemView.context.getString(
                                R.string.wishlist_manage_label
                            )
                        )
                    }
                    (holder as WishlistV2CountManageRowItemViewHolder).bind(element, isShowCheckbox)
                }
                TYPE_LOADER_LIST -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2ListLoaderViewHolder).bind()
                }
                TYPE_LOADER_GRID -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = false
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2GridLoaderViewHolder).bind()
                }
                TYPE_LIST -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2ListItemViewHolder).bind(
                        element,
                        holder.adapterPosition,
                        isShowCheckbox,
                        isAutoSelected,
                        isAddBulkModeFromOthers
                    )
                }
                TYPE_GRID -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = false
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2GridItemViewHolder).bind(
                        element,
                        holder.adapterPosition,
                        isShowCheckbox,
                        isAutoSelected,
                        isAddBulkModeFromOthers
                    )
                }
                TYPE_EMPTY_STATE -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2EmptyStateViewHolder).bind(element)
                }
                TYPE_EMPTY_STATE_CAROUSEL -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2EmptyStateCarouselViewHolder).bind()
                }
                TYPE_EMPTY_NOT_FOUND -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2EmptyStateNotFoundViewHolder).bind(element)
                }
                TYPE_RECOMMENDATION_LIST -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = false
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2RecommendationItemViewHolder).bind(
                        element,
                        holder.adapterPosition
                    )
                }
                TYPE_RECOMMENDATION_TITLE -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2RecommendationTitleViewHolder).bind(
                        element,
                        isShowCheckbox
                    )
                }
                TYPE_TOPADS -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2TdnViewHolder).bind(
                        element,
                        holder.adapterPosition,
                        isShowCheckbox
                    )
                }
                TYPE_RECOMMENDATION_CAROUSEL -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2RecommendationCarouselViewHolder).bind(
                        element,
                        holder.adapterPosition,
                        isShowCheckbox
                    )
                }
                TYPE_RECOMMENDATION_TITLE_WITH_MARGIN -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2RecommendationTitleViewHolder).bind(
                        element,
                        isShowCheckbox
                    )
                }
                TYPE_TICKER -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2TickerViewHolder).bind(
                        element,
                        isTickerCloseClicked,
                        isShowCheckbox
                    )
                }
                TYPE_DELETION_PROGRESS_WIDGET -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2DeletionProgressWidgetItemViewHolder).bind(element)
                }
                TYPE_EMPTY_STATE_COLLECTION -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistCollectionEmptyStateViewHolder).bind(element)
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
            TYPE_EMPTY_STATE_COLLECTION -> LAYOUT_EMPTY_STATE_COLLECTION
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    fun addList(list: List<WishlistV2TypeLayoutData>) {
        listTypeData.clear()
        listTypeData.addAll(list)
        notifyDataSetChanged()
    }

    fun appendList(list: List<WishlistV2TypeLayoutData>) {
        listTypeData.addAll(list)
        notifyDataSetChanged()
    }

    fun setCheckbox(position: Int, checked: Boolean) {
        listTypeData[position].isChecked = checked
        notifyItemChanged(position)
    }

    fun clearCheckbox() {
        listTypeData.forEach {
            it.isChecked = false
        }
        notifyDataSetChanged()
    }

    fun checkAllCheckbox() {
        listTypeData.forEach {
            it.isChecked = true
        }
    }

    fun setActionListener(v2Fragment: WishlistV2Fragment) {
        this.actionListener = v2Fragment
    }

    fun setActionListener(v2Fragment: WishlistCollectionDetailFragment) {
        this.actionListener = v2Fragment
    }

    fun getRecommendationDataAtIndex(index: Int): WishlistV2RecommendationDataModel {
        return listTypeData[index].dataObject as WishlistV2RecommendationDataModel
    }

    fun showLoader(typeLayout: String?) {
        listTypeData.clear()

        if (typeLayout == TYPE_LIST) {
            for (x in START_LOADER until TOTAL_LOADER) {
                listTypeData.add(WishlistV2TypeLayoutData("", TYPE_LOADER_LIST))
            }
        } else {
            for (x in START_LOADER until TOTAL_LOADER) {
                listTypeData.add(WishlistV2TypeLayoutData("", TYPE_LOADER_GRID))
            }
        }

        notifyDataSetChanged()
    }

    fun showCheckbox(isAutoDeletion: Boolean) {
        isShowCheckbox = true
        isAutoSelected = isAutoDeletion
        if (isAutoDeletion) checkAllCheckbox()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showCheckboxAddBulkFromOthers() {
        isShowCheckbox = true
        isAddBulkModeFromOthers = true
        notifyDataSetChanged()
    }

    fun hideCheckbox() {
        isShowCheckbox = false
        isAutoSelected = false
        isAddBulkModeFromOthers = false
        clearCheckbox()
    }

    fun getCountData(): Int {
        return listTypeData.size
    }

    fun changeTypeLayout(prefLayout: String?) {
        // 0 = LIST, 1 = GRID
        if (listTypeData.isNotEmpty()) {
            listTypeData.forEach {
                if (it.dataObject is ProductCardModel) {
                    it.typeLayout = prefLayout
                }
            }
            notifyDataSetChanged()
        }
    }

    fun hideTicker() {
        isTickerCloseClicked = true
        notifyItemChanged(0)
    }

    fun resetTicker() {
        isTickerCloseClicked = false
    }
}
