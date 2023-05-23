package com.tokopedia.feed.component.product

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.feedcomponent.R

/**
 * Created by meyta.taliti on 11/05/23.
 */
class FeedTaggedProductBottomSheetAdapter(
    listener: FeedTaggedProductBottomSheetViewHolder.Listener
): BaseDiffUtilAdapter<FeedTaggedProductUiModel>(isFlexibleType = true) {

    init {
        delegatesManager.addDelegate(Delegate(listener))
    }

    override fun areItemsTheSame(
        oldItem: FeedTaggedProductUiModel,
        newItem: FeedTaggedProductUiModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: FeedTaggedProductUiModel,
        newItem: FeedTaggedProductUiModel
    ): Boolean {
        return oldItem == newItem
    }

    class Delegate(
        private val listener: FeedTaggedProductBottomSheetViewHolder.Listener
    ): TypedAdapterDelegate<FeedTaggedProductUiModel, FeedTaggedProductUiModel, FeedTaggedProductBottomSheetViewHolder>(R.layout.item_feed_tagged_product) {

        override fun onBindViewHolder(
            item: FeedTaggedProductUiModel,
            holder: FeedTaggedProductBottomSheetViewHolder
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): FeedTaggedProductBottomSheetViewHolder {
            return FeedTaggedProductBottomSheetViewHolder.create(
                parent,
                listener
            )
        }
    }

}
