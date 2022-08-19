package com.tokopedia.play.ui.productsheet.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.ui.productsheet.adapter.delegate.ProductSheetAdapterDelegate
import com.tokopedia.play.ui.productsheet.viewholder.ProductSheetSectionViewHolder
import com.tokopedia.play.view.custom.ProductBottomSheetCardView
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel

/**
 * Created by kenny.hadisaputra on 18/08/22
 */
class ProductSheetAdapter(
    sectionListener: ProductSheetSectionViewHolder.Listener,
    productListener: ProductBottomSheetCardView.Listener,
) : BaseDiffUtilAdapter<ProductSheetAdapter.Item>() {

    init {
        delegatesManager
            .addDelegate(ProductSheetAdapterDelegate.Section(sectionListener))
            .addDelegate(ProductSheetAdapterDelegate.Product(productListener))
            .addDelegate(ProductSheetAdapterDelegate.Loading())
    }

    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return if (oldItem is Item.Product && newItem is Item.Product) {
            oldItem.product.id == newItem.product.id
        } else if (oldItem is Item.ProductWithSection && newItem is Item.ProductWithSection) {
            oldItem.id == newItem.id
        } else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }

    sealed class Item : ImpressHolder() {

        data class Product(val product: PlayProductUiModel.Product) : Item()
        data class ProductWithSection(
            val id: String,
            val config: ProductSectionUiModel.Section.ConfigUiModel,
            val product: PlayProductUiModel.Product,
        ) : Item()
        object Loading : Item()
    }
}