package com.tokopedia.topads.dashboard.recommendation.views.adapter.recommendation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ProductItemUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ProductListUiModel
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.EMPTY_PRODUCT_LIST_IMG_URL
import com.tokopedia.topads.dashboard.recommendation.data.model.local.EmptyProductListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.FeaturedProductsUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class ProductListAdapter(
    private val onItemCheckedChangeListener: (() -> Unit)?,
    private val reloadPage: (() -> Unit)?
) :
    ListAdapter<ProductListUiModel, RecyclerView.ViewHolder>(ProductListDiffUtilCallBack()) {

    inner class ProductListItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private val title: com.tokopedia.unifyprinciples.Typography = view.findViewById(R.id.title)
        private val description: com.tokopedia.unifyprinciples.Typography =
            view.findViewById(R.id.description)
        private val image: ImageUnify = view.findViewById(R.id.image)
        private val checkboxUnify: CheckboxUnify = view.findViewById(R.id.checkbox)

        fun bind(
            item: ProductItemUiModel,
            onItemCheckedChangeListener: (() -> Unit)?
        ) {
            title.text = item.productName
            description.text = HtmlCompat.fromHtml(
                String.format(
                    view.context.getString(R.string.topads_insight_centre_product_item_description),
                    item.searchCount
                ),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            image.urlSrc = item.imgUrl
            checkboxUnify.isChecked = item.isSelected

            checkboxUnify.setOnClickListener {
                item.isSelected = checkboxUnify.isChecked
                onItemCheckedChangeListener?.invoke()
            }
        }
    }

    inner class EmptyStateViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageUnify = view.findViewById(R.id.emptyStateImage)
        private val reloadCta: UnifyButton = view.findViewById(R.id.emptyStateCta)

        fun bind(item: EmptyProductListUiModel, reloadPage: (() -> Unit)?) {
            image.urlSrc = EMPTY_PRODUCT_LIST_IMG_URL
            reloadCta.setOnClickListener {
                reloadPage?.invoke()
            }
        }
    }

    inner class FeaturedProductsViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageUnify = view.findViewById(R.id.productImg)

        fun bind(item: FeaturedProductsUiModel) {
            image.urlSrc = item.imgUrl
        }
    }

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
                    .inflate(R.layout.topads_potential_product_empty_layout, parent, false)
                EmptyStateViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ProductItemUiModel -> {
                (holder as? ProductListItemViewHolder)?.bind(item, onItemCheckedChangeListener)
            }
            is EmptyProductListUiModel -> {
                (holder as? EmptyStateViewHolder)?.bind(item, reloadPage)
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
            else -> throw IllegalArgumentException("Invalid item type")
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
