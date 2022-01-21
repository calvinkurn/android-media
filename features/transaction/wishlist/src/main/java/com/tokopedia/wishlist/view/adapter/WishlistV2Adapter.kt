package com.tokopedia.wishlist.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.data.model.response.WishlistV2Response
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.*
import com.tokopedia.wishlist.util.WishlistV2Consts
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_COUNT_MANAGE_ROW
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_EMPTY_NOT_FOUND
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_EMPTY_STATE
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_EMPTY_STATE_CAROUSEL
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_GRID
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_LIST
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_LOADER_GRID
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_LOADER_LIST
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_CAROUSEL
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_LIST
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_TITLE
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_TITLE_WITH_MARGIN
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_TOPADS
import com.tokopedia.wishlist.view.adapter.viewholder.*
import com.tokopedia.wishlist.view.fragment.WishlistV2Fragment

class WishlistV2Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var actionListener: ActionListener? = null
    private var listTypeData = mutableListOf<WishlistV2TypeLayoutData>()
    private var isShowCheckbox = false
    var isRefreshing = false

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
    }

    interface ActionListener {
        fun onCariBarangClicked()
        fun onNotFoundButtonClicked(keyword: String)
        fun onThreeDotsMenuClicked(itemWishlist: WishlistV2Response.Data.WishlistV2.Item)
        fun onCheckBulkDeleteOption(productId: String, isChecked: Boolean, position: Int)
        fun onAtc(wishlistItem: WishlistV2Response.Data.WishlistV2.Item, position: Int)
        fun onCheckSimilarProduct(url: String)
        fun onResetFilter()
        fun onManageClicked(showCheckbox: Boolean)
        fun onProductItemClicked(wishlistItem: WishlistV2Response.Data.WishlistV2.Item, position: Int)
        fun onProductRecommItemClicked(productId: String)
        fun onViewProductCard(wishlistItem: WishlistV2Response.Data.WishlistV2.Item, position: Int)
        fun onBannerTopAdsImpression(topAdsImageViewModel: TopAdsImageViewModel, position: Int)
        fun onBannerTopAdsClick(topAdsImageViewModel: TopAdsImageViewModel, position: Int)
        fun onRecommendationItemImpression(recommendationItem: RecommendationItem, position: Int)
        fun onRecommendationItemClick(recommendationItem: RecommendationItem, position: Int)
        fun onRecommendationCarouselItemImpression(recommendationItem: RecommendationItem, position: Int)
        fun onRecommendationCarouselItemClick(recommendationItem: RecommendationItem, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LAYOUT_COUNT_MANAGE_ROW  -> {
                val binding = WishlistV2CountManageRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WishlistV2CountManageRowItemViewHolder(binding, actionListener)
            }
            LAYOUT_LOADER_LIST -> {
                val binding = WishlistV2LoaderListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WishlistV2ListLoaderViewHolder(binding)
            }
            LAYOUT_LOADER_GRID -> {
                val binding = WishlistV2LoaderGridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WishlistV2GridLoaderViewHolder(binding)
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
                    (holder as WishlistV2ListItemViewHolder).bind(element, holder.adapterPosition, isShowCheckbox)
                }
                TYPE_GRID -> {
                    val params = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = false
                    holder.itemView.layoutParams = params
                    (holder as WishlistV2GridItemViewHolder).bind(element, holder.adapterPosition, isShowCheckbox)
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
                    /*val params = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                    params.isFullSpan = true
                    holder.itemView.layoutParams = params*/
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
            }
        }
    }

    override fun getItemCount(): Int {
        return listTypeData.size
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

    private fun clearCheckbox() {
        listTypeData.forEach {
            it.isChecked = false
        }
    }

    fun setActionListener(v2Fragment: WishlistV2Fragment) {
        this.actionListener = v2Fragment
    }

    fun showLoader(typeLayout: String?) {
        listTypeData.clear()

        if (typeLayout == TYPE_LIST) {
            for (x in 0 until 5) {
                listTypeData.add(WishlistV2TypeLayoutData("", TYPE_LOADER_LIST))
            }
        } else {
            for (x in 0 until 5) {
                listTypeData.add(WishlistV2TypeLayoutData("", TYPE_LOADER_GRID))
            }
        }

        notifyDataSetChanged()
    }

    fun showCheckbox() {
        isShowCheckbox = true
        notifyDataSetChanged()
    }

    fun hideCheckbox() {
        isShowCheckbox = false
        clearCheckbox()
        notifyDataSetChanged()
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
}