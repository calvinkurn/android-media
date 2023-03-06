package com.tokopedia.feedcomponent.view.adapter.bottomsheetadapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.bottomsheets.ProductItemInfoBottomSheet
import com.tokopedia.feedcomponent.view.adapter.viewholder.posttag.FeedTaggedProductViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.posttag.ProductPostTagViewHolderNew
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagModelNew

class ProductInfoBottomSheetAdapter(listener: ProductItemInfoBottomSheet.Listener, feedTaggedProductViewHolderListener: FeedTaggedProductViewHolder.Listener?) : BaseDiffUtilAdapter<ProductPostTagModelNew>(true)
{
    init {
        delegatesManager
            .addDelegate(ProductItemDelegate(listener))
            .addDelegate(FeedTaggedProductItemDelegate(feedTaggedProductViewHolderListener))
    }

    override fun areItemsTheSame(
        oldItem: ProductPostTagModelNew,
        newItem: ProductPostTagModelNew
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ProductPostTagModelNew,
        newItem: ProductPostTagModelNew
    ): Boolean {
        return oldItem == newItem
    }

    private class ProductItemDelegate(private val listener: ProductItemInfoBottomSheet.Listener) :
        TypedAdapterDelegate<ProductPostTagModelNew, ProductPostTagModelNew, ProductPostTagViewHolderNew>
            (R.layout.item_producttag_list_new) {

        override fun onBindViewHolder(
            item: ProductPostTagModelNew,
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

    private class FeedTaggedProductItemDelegate(private val listener: FeedTaggedProductViewHolder.Listener?) :
        TypedAdapterDelegate<ProductPostTagModelNew, ProductPostTagModelNew, FeedTaggedProductViewHolder>
            (R.layout.item_producttag_list_new) {

        override fun onBindViewHolder(
            item: ProductPostTagModelNew,
            holder: FeedTaggedProductViewHolder
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): FeedTaggedProductViewHolder {
            return FeedTaggedProductViewHolder.create(parent, listener)
        }

    }

}
