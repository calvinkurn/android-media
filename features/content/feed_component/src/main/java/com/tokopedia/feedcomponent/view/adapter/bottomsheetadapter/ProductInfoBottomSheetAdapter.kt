package com.tokopedia.feedcomponent.view.adapter.bottomsheetadapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.bottomsheets.ProductItemInfoBottomSheet
import com.tokopedia.feedcomponent.view.adapter.viewholder.posttag.ProductPostTagViewHolderNew
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagViewModelNew

class ProductInfoBottomSheetAdapter(listener: ProductItemInfoBottomSheet.Listener) : BaseDiffUtilAdapter<ProductPostTagViewModelNew>(true)
{
    init {
        delegatesManager
            .addDelegate(ProductItemDelegate(listener))
    }

    override fun areItemsTheSame(
        oldItem: ProductPostTagViewModelNew,
        newItem: ProductPostTagViewModelNew
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ProductPostTagViewModelNew,
        newItem: ProductPostTagViewModelNew
    ): Boolean {
        return oldItem == newItem
    }

    private class ProductItemDelegate(private val listener: ProductItemInfoBottomSheet.Listener) :
        TypedAdapterDelegate<ProductPostTagViewModelNew, ProductPostTagViewModelNew, ProductPostTagViewHolderNew>
            (R.layout.item_producttag_list_new) {

        override fun onBindViewHolder(
            item: ProductPostTagViewModelNew,
            holder: ProductPostTagViewHolderNew
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ProductPostTagViewHolderNew {
            return ProductPostTagViewHolderNew.create(parent, listener)
        }

    }

}