package com.tokopedia.catalog.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.catalog.ui.model.CatalogComparisonProductsUiModel

class CatalogListingSwitchingCatalogPageDiffer : DiffUtil.ItemCallback<CatalogComparisonProductsUiModel.CatalogComparisonUIModel>() {

    override fun areItemsTheSame(oldItem: CatalogComparisonProductsUiModel.CatalogComparisonUIModel, newItem: CatalogComparisonProductsUiModel.CatalogComparisonUIModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CatalogComparisonProductsUiModel.CatalogComparisonUIModel, newItem: CatalogComparisonProductsUiModel.CatalogComparisonUIModel): Boolean {
        return  oldItem == newItem
    }
}

