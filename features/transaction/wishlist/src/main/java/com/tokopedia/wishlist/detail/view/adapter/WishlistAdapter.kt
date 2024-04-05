package com.tokopedia.wishlist.detail.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.PercentageScrollListener
import com.tokopedia.abstraction.base.view.adapter.adapter.listener.IAdsViewHolderTrackListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.collection.view.adapter.viewholder.WishlistCollectionEmptyStateViewHolder
import com.tokopedia.wishlist.collection.view.fragment.WishlistCollectionDetailFragment
import com.tokopedia.wishlist.databinding.WishlistCountDeletionItemBinding
import com.tokopedia.wishlist.databinding.WishlistEmptyStateCarouselBinding
import com.tokopedia.wishlist.databinding.WishlistEmptyStateItemBinding
import com.tokopedia.wishlist.databinding.WishlistEmptyStateNotFoundItemBinding
import com.tokopedia.wishlist.databinding.WishlistGridItemBinding
import com.tokopedia.wishlist.databinding.WishlistListItemBinding
import com.tokopedia.wishlist.databinding.WishlistLoaderGridItemBinding
import com.tokopedia.wishlist.databinding.WishlistLoaderListItemBinding
import com.tokopedia.wishlist.databinding.WishlistRecommendationCarouselItemBinding
import com.tokopedia.wishlist.databinding.WishlistRecommendationItemBinding
import com.tokopedia.wishlist.databinding.WishlistRecommendationTitleItemBinding
import com.tokopedia.wishlist.databinding.WishlistStickyItemBinding
import com.tokopedia.wishlist.databinding.WishlistTdnItemBinding
import com.tokopedia.wishlist.databinding.WishlistTickerItemBinding
import com.tokopedia.wishlist.detail.data.model.WishlistRecommendationDataModel
import com.tokopedia.wishlist.detail.data.model.WishlistTypeLayoutData
import com.tokopedia.wishlist.detail.data.model.WishlistUiModel
import com.tokopedia.wishlist.detail.util.WishlistConsts.TYPE_COUNT_MANAGE_ROW
import com.tokopedia.wishlist.detail.util.WishlistConsts.TYPE_DELETION_PROGRESS_WIDGET
import com.tokopedia.wishlist.detail.util.WishlistConsts.TYPE_EMPTY_NOT_FOUND
import com.tokopedia.wishlist.detail.util.WishlistConsts.TYPE_EMPTY_STATE
import com.tokopedia.wishlist.detail.util.WishlistConsts.TYPE_EMPTY_STATE_CAROUSEL
import com.tokopedia.wishlist.detail.util.WishlistConsts.TYPE_EMPTY_STATE_COLLECTION
import com.tokopedia.wishlist.detail.util.WishlistConsts.TYPE_GRID
import com.tokopedia.wishlist.detail.util.WishlistConsts.TYPE_LIST
import com.tokopedia.wishlist.detail.util.WishlistConsts.TYPE_LOADER_GRID
import com.tokopedia.wishlist.detail.util.WishlistConsts.TYPE_LOADER_LIST
import com.tokopedia.wishlist.detail.util.WishlistConsts.TYPE_RECOMMENDATION_CAROUSEL
import com.tokopedia.wishlist.detail.util.WishlistConsts.TYPE_RECOMMENDATION_LIST
import com.tokopedia.wishlist.detail.util.WishlistConsts.TYPE_RECOMMENDATION_TITLE
import com.tokopedia.wishlist.detail.util.WishlistConsts.TYPE_RECOMMENDATION_TITLE_WITH_MARGIN
import com.tokopedia.wishlist.detail.util.WishlistConsts.TYPE_TICKER
import com.tokopedia.wishlist.detail.util.WishlistConsts.TYPE_TOPADS
import com.tokopedia.wishlist.detail.view.adapter.viewholder.WishlistCountManageRowItemViewHolder
import com.tokopedia.wishlist.detail.view.adapter.viewholder.WishlistDeletionProgressWidgetItemViewHolder
import com.tokopedia.wishlist.detail.view.adapter.viewholder.WishlistEmptyStateCarouselViewHolder
import com.tokopedia.wishlist.detail.view.adapter.viewholder.WishlistEmptyStateNotFoundViewHolder
import com.tokopedia.wishlist.detail.view.adapter.viewholder.WishlistEmptyStateViewHolder
import com.tokopedia.wishlist.detail.view.adapter.viewholder.WishlistGridItemViewHolder
import com.tokopedia.wishlist.detail.view.adapter.viewholder.WishlistGridLoaderViewHolder
import com.tokopedia.wishlist.detail.view.adapter.viewholder.WishlistListItemViewHolder
import com.tokopedia.wishlist.detail.view.adapter.viewholder.WishlistListLoaderViewHolder
import com.tokopedia.wishlist.detail.view.adapter.viewholder.WishlistRecommendationCarouselViewHolder
import com.tokopedia.wishlist.detail.view.adapter.viewholder.WishlistRecommendationItemViewHolder
import com.tokopedia.wishlist.detail.view.adapter.viewholder.WishlistRecommendationTitleViewHolder
import com.tokopedia.wishlist.detail.view.adapter.viewholder.WishlistTdnViewHolder
import com.tokopedia.wishlist.detail.view.adapter.viewholder.WishlistTickerViewHolder

class WishlistAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var actionListener: ActionListener? = null
    private var listTypeData = mutableListOf<WishlistTypeLayoutData>()
    private var isShowCheckbox = false
    private var isTickerCloseClicked = false
    var isRefreshing = false
    private var isAutoSelected = false
    private var isAddBulkModeFromOthers = false

    private var recyclerView: RecyclerView? = null
    private val percentageScrollListener by lazy(LazyThreadSafetyMode.NONE) {
        PercentageScrollListener()
    }

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
        fun onThreeDotsMenuClicked(itemWishlist: WishlistUiModel.Item)
        fun onCheckBulkOption(productId: String, isChecked: Boolean, position: Int)
        fun onValidateCheckBulkOption(productId: String, isChecked: Boolean, position: Int)
        fun onUncheckAutomatedBulkDelete(productId: String, isChecked: Boolean, position: Int)
        fun onAtc(wishlistItem: WishlistUiModel.Item, position: Int)
        fun onCheckSimilarProduct(url: String)
        fun onResetFilter()
        fun onManageClicked(showCheckbox: Boolean, isDeleteOnly: Boolean, isBulkAdd: Boolean)
        fun onProductItemClicked(wishlistItem: WishlistUiModel.Item, position: Int)
        fun onViewProductCard(wishlistItem: WishlistUiModel.Item, position: Int)
        fun onBannerTopAdsImpression(topAdsImageViewModel: TopAdsImageViewModel, position: Int)
        fun onBannerTopAdsClick(topAdsImageViewModel: TopAdsImageViewModel, position: Int)
        fun onRecommendationItemImpression(recommendationItem: RecommendationItem, position: Int)
        fun onRecommendationItemClick(recommendationItem: RecommendationItem, position: Int)
        fun onRecommendationCarouselItemImpression(
            recommendationItem: RecommendationItem,
            position: Int
        )

        fun onRecommendationCarouselItemClick(recommendationItem: RecommendationItem, position: Int)
        fun onTickerCTAShowBottomSheet(bottomSheetCleanerData: WishlistUiModel.StorageCleanerBottomSheet)
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
                val binding = WishlistStickyItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistCountManageRowItemViewHolder(binding, actionListener)
            }
            LAYOUT_LOADER_LIST -> {
                val binding = WishlistLoaderListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistListLoaderViewHolder(binding)
            }
            LAYOUT_LOADER_GRID -> {
                val binding = WishlistLoaderGridItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistGridLoaderViewHolder(binding)
            }
            LAYOUT_LIST -> {
                val binding = WishlistListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistListItemViewHolder(binding, actionListener)
            }
            LAYOUT_GRID -> {
                val binding = WishlistGridItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistGridItemViewHolder(binding, actionListener)
            }
            LAYOUT_EMPTY_STATE -> {
                val binding = WishlistEmptyStateItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistEmptyStateViewHolder(binding, actionListener)
            }
            LAYOUT_EMPTY_STATE_CAROUSEL -> {
                val binding = WishlistEmptyStateCarouselBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistEmptyStateCarouselViewHolder(binding, actionListener)
            }
            LAYOUT_EMPTY_NOT_FOUND -> {
                val binding = WishlistEmptyStateNotFoundItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistEmptyStateNotFoundViewHolder(binding, actionListener)
            }
            LAYOUT_RECOMMENDATION_TITLE -> {
                val binding = WishlistRecommendationTitleItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistRecommendationTitleViewHolder(binding, false)
            }
            LAYOUT_RECOMMENDATION_LIST -> {
                val binding = WishlistRecommendationItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistRecommendationItemViewHolder(binding, actionListener)
            }
            LAYOUT_TOPADS -> {
                val binding = WishlistTdnItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistTdnViewHolder(binding, actionListener)
            }
            LAYOUT_RECOMMENDATION_CAROUSEL -> {
                val binding = WishlistRecommendationCarouselItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistRecommendationCarouselViewHolder(binding, actionListener)
            }
            LAYOUT_RECOMMENDATION_TITLE_WITH_MARGIN -> {
                val binding = WishlistRecommendationTitleItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistRecommendationTitleViewHolder(binding, true)
            }
            LAYOUT_TICKER -> {
                val binding = WishlistTickerItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistTickerViewHolder(binding, actionListener)
            }
            LAYOUT_DELETION_PROGRESS_WIDGET -> {
                val binding = WishlistCountDeletionItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistDeletionProgressWidgetItemViewHolder(binding)
            }
            LAYOUT_EMPTY_STATE_COLLECTION -> {
                val binding = WishlistEmptyStateItemBinding.inflate(
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
                        (holder as WishlistCountManageRowItemViewHolder).setManageLabel(
                            holder.itemView.context.getString(
                                R.string.wishlist_manage_label
                            )
                        )
                    }
                    (holder as WishlistCountManageRowItemViewHolder).bind(element, isShowCheckbox)
                }
                TYPE_LOADER_LIST -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistListLoaderViewHolder).bind()
                }
                TYPE_LOADER_GRID -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = false
                    holder.itemView.layoutParams = params
                    (holder as WishlistGridLoaderViewHolder).bind()
                }
                TYPE_LIST -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistListItemViewHolder).bind(
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
                    (holder as WishlistGridItemViewHolder).bind(
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
                    (holder as WishlistEmptyStateViewHolder).bind(element)
                }
                TYPE_EMPTY_STATE_CAROUSEL -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistEmptyStateCarouselViewHolder).bind()
                }
                TYPE_EMPTY_NOT_FOUND -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistEmptyStateNotFoundViewHolder).bind(element)
                }
                TYPE_RECOMMENDATION_LIST -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = false
                    holder.itemView.layoutParams = params
                    (holder as WishlistRecommendationItemViewHolder).bind(
                        element,
                        holder.adapterPosition
                    )
                }
                TYPE_RECOMMENDATION_TITLE -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistRecommendationTitleViewHolder).bind(
                        element,
                        isShowCheckbox
                    )
                }
                TYPE_TOPADS -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistTdnViewHolder).bind(
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
                    (holder as WishlistRecommendationCarouselViewHolder).bind(
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
                    (holder as WishlistRecommendationTitleViewHolder).bind(
                        element,
                        isShowCheckbox
                    )
                }
                TYPE_TICKER -> {
                    val params =
                        (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params
                    (holder as WishlistTickerViewHolder).bind(
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
                    (holder as WishlistDeletionProgressWidgetItemViewHolder).bind(element)
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

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        this.recyclerView?.addOnScrollListener(percentageScrollListener)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView?.removeOnScrollListener(percentageScrollListener)
        this.recyclerView = null
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is IAdsViewHolderTrackListener) {
            holder.onViewAttachedToWindow(recyclerView)
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is IAdsViewHolderTrackListener) {
            holder.onViewDetachedFromWindow(recyclerView)
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

    fun addList(list: List<WishlistTypeLayoutData>) {
        listTypeData.clear()
        listTypeData.addAll(list)
        notifyDataSetChanged()
    }

    fun appendList(list: List<WishlistTypeLayoutData>) {
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

    fun setActionListener(v2Fragment: WishlistCollectionDetailFragment) {
        this.actionListener = v2Fragment
    }

    fun getRecommendationDataAtIndex(index: Int): WishlistRecommendationDataModel {
        return listTypeData[index].dataObject as WishlistRecommendationDataModel
    }

    fun showLoader(typeLayout: String?) {
        listTypeData.clear()

        if (typeLayout == TYPE_LIST) {
            for (x in START_LOADER until TOTAL_LOADER) {
                listTypeData.add(WishlistTypeLayoutData("", TYPE_LOADER_LIST))
            }
        } else {
            for (x in START_LOADER until TOTAL_LOADER) {
                listTypeData.add(WishlistTypeLayoutData("", TYPE_LOADER_GRID))
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
