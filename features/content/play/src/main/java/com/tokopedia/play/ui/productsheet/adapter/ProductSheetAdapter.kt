package com.tokopedia.play.ui.productsheet.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.ui.productsheet.adapter.delegate.MerchantVoucherAdapterDelegate
import com.tokopedia.play.ui.productsheet.adapter.delegate.ProductLineAdapterDelegate
import com.tokopedia.play.view.type.ProductSheetProduct
import com.tokopedia.play.view.type.ProductSheetContent

/**
 * Created by jegul on 03/03/20
 */
class ProductSheetAdapter : BaseDiffUtilAdapter<ProductSheetContent>() {

    init {
        delegatesManager
                .addDelegate(MerchantVoucherAdapterDelegate())
                .addDelegate(ProductLineAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: ProductSheetContent, newItem: ProductSheetContent): Boolean {
        return if (oldItem is ProductSheetProduct && newItem is ProductSheetProduct) oldItem.id == newItem.id
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ProductSheetContent, newItem: ProductSheetContent): Boolean {
        return oldItem == newItem
    }
}