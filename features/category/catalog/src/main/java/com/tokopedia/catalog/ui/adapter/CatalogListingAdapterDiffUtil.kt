package com.tokopedia.catalog.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.databinding.ItemCatalogListInChangeComparisonBinding
import com.tokopedia.catalog.databinding.ItemCatalogListInChangeComparisonLoadMoreBinding
import com.tokopedia.catalog.ui.fragment.CatalogSwitchingComparisonFragment.Companion.LIMIT_SELECT_PRODUCT
import com.tokopedia.catalog.ui.model.CatalogComparisonProductsUiModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage

class CatalogListingAdapterDiffUtil(
    asyncDifferConfig: AsyncDifferConfig<CatalogComparisonProductsUiModel.CatalogComparisonUIModel>,
    var currentCatalogSelection: List<String> = listOf(),
    val listener: CatalogListingListener,
    var isShowLoadMore: Boolean = false
) : ListAdapter<CatalogComparisonProductsUiModel.CatalogComparisonUIModel, RecyclerView.ViewHolder>(
    asyncDifferConfig
) {

    private val LOAD_MORE = 1
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == LOAD_MORE) {
            val binding = ItemCatalogListInChangeComparisonLoadMoreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            LoadMoreViewHolder(binding)
        } else {
            val binding = ItemCatalogListInChangeComparisonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isShowLoadMore && (position == itemCount - 1)) {
            LOAD_MORE
        } else {
            super.getItemViewType(position)
        }
    }

    override fun getItemCount(): Int {
        if (isShowLoadMore && currentList.isNotEmpty()) {
            return super.getItemCount() + 1
        }
        return super.getItemCount()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val itemUiModel = getItem(position)
            val isCurrentCatalog = currentCatalogSelection.getOrNull(Int.ZERO) == itemUiModel.id
            holder.bindToView(itemUiModel, isCurrentCatalog)
        }
    }

    inner class ViewHolder(itemView: ItemCatalogListInChangeComparisonBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        private val iuProduct = itemView.iuProduct
        private val tvTitle = itemView.tvTitle
        private val tvPrice = itemView.tvPrice
        private val cbCatalog = itemView.cbCatalog
        private val ivCurrentCatalog = itemView.ivPinCurrentCatalog

        fun bindToView(
            itemUiModel: CatalogComparisonProductsUiModel.CatalogComparisonUIModel,
            isCurrentCatalog: Boolean
        ) {
            iuProduct.loadImage(itemUiModel.catalogImage)
            tvTitle.text = itemUiModel?.name
            tvPrice.text = itemUiModel.price
            cbCatalog.isEnabled =
                !(
                    currentCatalogSelection.size == (LIMIT_SELECT_PRODUCT) && !currentCatalogSelection.contains(
                        itemUiModel.id.orEmpty()
                    )
                    )
            if (isCurrentCatalog) {
                ivCurrentCatalog.show()
                cbCatalog.gone()
            } else {
                ivCurrentCatalog.gone()
                cbCatalog.isChecked = currentCatalogSelection.contains(itemUiModel?.id.orEmpty())
                cbCatalog.setOnClickListener {
                    val isChecked = cbCatalog.isChecked
                    listener.onCheckListener(itemUiModel, isChecked)
                }
            }
        }
    }

    inner class LoadMoreViewHolder(itemView: ItemCatalogListInChangeComparisonLoadMoreBinding) :
        RecyclerView.ViewHolder(itemView.root)
}

interface CatalogListingListener {
    fun onCheckListener(
        item: CatalogComparisonProductsUiModel.CatalogComparisonUIModel,
        isChecked: Boolean
    )
}
