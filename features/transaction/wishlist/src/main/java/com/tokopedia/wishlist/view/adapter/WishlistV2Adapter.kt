package com.tokopedia.wishlist.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.data.model.response.WishlistV2Response
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.*
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
                val binding = WishlistV2CountManageRowItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2CountManageRowItemViewHolder(binding, actionListener)
            }
            LAYOUT_LOADER_LIST -> {
                val binding = WishlistV2LoaderListItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2ListLoaderViewHolder(binding)
            }
            LAYOUT_LOADER_GRID -> {
                // TODO : create loader grid
                val binding = WishlistV2LoaderListItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2ListLoaderViewHolder(binding)
            }
            LAYOUT_LIST -> {
                val binding = WishlistV2ListItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2ListItemViewHolder(binding, actionListener)
            }
            LAYOUT_GRID -> {
                val binding = WishlistV2GridItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2GridItemViewHolder(binding, actionListener)
            }
            LAYOUT_EMPTY_STATE -> {
                val binding = WishlistV2EmptyStateItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2EmptyStateViewHolder(binding, actionListener)
            }
            LAYOUT_EMPTY_STATE_CAROUSEL -> {
                val binding = WishlistV2EmptyStateCarouselBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2EmptyStateCarouselViewHolder(binding, actionListener)
            }
            LAYOUT_EMPTY_NOT_FOUND -> {
                val binding = WishlistV2EmptyStateNotFoundItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2EmptyStateNotFoundViewHolder(binding, actionListener)
            }
            LAYOUT_RECOMMENDATION_TITLE -> {
                val binding = WishlistV2RecommendationTitleItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2RecommendationTitleViewHolder(binding, false)
            }
            LAYOUT_RECOMMENDATION_LIST -> {
                val binding = WishlistV2RecommendationItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2RecommendationItemViewHolder(binding, actionListener)
            }
            LAYOUT_TOPADS -> {
                val binding = WishlistV2TdnItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2TdnViewHolder(binding, actionListener)
            }
            LAYOUT_RECOMMENDATION_CAROUSEL -> {
                val binding = WishlistV2RecommendationCarouselItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2RecommendationCarouselViewHolder(binding, actionListener)
            }
            LAYOUT_RECOMMENDATION_TITLE_WITH_MARGIN -> {
                val binding = WishlistV2RecommendationTitleItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2RecommendationTitleViewHolder(binding, true)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val element = listTypeData[position]
        when (holder) {
            is WishlistV2CountManageRowItemViewHolder-> {
                if (isRefreshing) {
                    isRefreshing = false
                    holder.setManageLabel(holder.itemView.context.getString(R.string.wishlist_manage_label))
                }
                holder.bind(element, isShowCheckbox)
            }
            is WishlistV2ListItemViewHolder-> {
                holder.bind(element, holder.adapterPosition, isShowCheckbox)
            }
            is WishlistV2GridItemViewHolder -> {
                holder.bind(element, holder.adapterPosition, isShowCheckbox)
            }
            is WishlistV2EmptyStateViewHolder -> {
                holder.bind(element)
            }
            is WishlistV2EmptyStateCarouselViewHolder -> {
                holder.bind(element)
            }
            is WishlistV2EmptyStateNotFoundViewHolder -> {
                holder.bind(element)
            }
            is WishlistV2RecommendationTitleViewHolder -> {
                holder.bind(element, isShowCheckbox)
            }
            is WishlistV2RecommendationItemViewHolder -> {
                holder.bind(element, holder.adapterPosition)
            }
            is WishlistV2TdnViewHolder -> {
                holder.bind(element, holder.adapterPosition, isShowCheckbox)
            }
            is WishlistV2RecommendationCarouselViewHolder -> {
                holder.bind(element, holder.adapterPosition, isShowCheckbox)
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

    fun showLoader() {
        listTypeData.clear()
        for (x in 0 until 5) {
            listTypeData.add(WishlistV2TypeLayoutData("", TYPE_LOADER_LIST))
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
                it.typeLayout = prefLayout
            }
            notifyDataSetChanged()
        }
    }
}