package com.tokopedia.topads.dashboard.recommendation.views.adapter.recommendation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ProductItemUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ProductListUiModel
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.FAILED_LIST_STATE_IMG_URL
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.INVALID_GROUP_TYPE
import com.tokopedia.topads.dashboard.recommendation.data.model.local.EmptyProductListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.FeaturedProductsUiModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.viewholders.FailedStateViewHolder
import com.tokopedia.topads.dashboard.recommendation.views.adapter.viewholders.FeaturedProductsViewHolder
import com.tokopedia.topads.dashboard.recommendation.views.adapter.viewholders.ProductListItemViewHolder

class ProductListAdapter(
    private val onItemCheckedChangeListener: (() -> Unit)?,
    private val reloadPage: (() -> Unit)?
) :
    ListAdapter<ProductListUiModel, RecyclerView.ViewHolder>(ProductListDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.topads_potential_product_item_layout -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.topads_potential_product_item_layout, parent, false)
                ProductListItemViewHolder(view)
            }
            R.layout.topads_insight_centre_featured_products_layout -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.topads_insight_centre_featured_products_layout, parent, false)
                FeaturedProductsViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.topads_failed_state_layout, parent, false)
                FailedStateViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ProductItemUiModel -> {
                (holder as? ProductListItemViewHolder)?.bind(item, onItemCheckedChangeListener)
            }
            is EmptyProductListUiModel -> {
                (holder as? FailedStateViewHolder)?.bind(FAILED_LIST_STATE_IMG_URL, reloadPage = reloadPage)
            }
            is FeaturedProductsUiModel -> {
                (holder as? FeaturedProductsViewHolder)?.bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ProductItemUiModel -> R.layout.topads_potential_product_item_layout
            is EmptyProductListUiModel -> R.layout.topads_insight_centre_empty_state_layout
            is FeaturedProductsUiModel -> R.layout.topads_insight_centre_featured_products_layout
            else -> throw IllegalArgumentException(INVALID_GROUP_TYPE)
        }
    }
}

class ProductListDiffUtilCallBack : DiffUtil.ItemCallback<ProductListUiModel>() {
    override fun areItemsTheSame(
        oldItem: ProductListUiModel,
        newItem: ProductListUiModel
    ): Boolean {
        return oldItem.id() == newItem.id()
    }

    override fun areContentsTheSame(
        oldItem: ProductListUiModel,
        newItem: ProductListUiModel
    ): Boolean {
        return oldItem.equalsWith(newItem)
    }

}
