package com.tokopedia.feed.component.product

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.feedcomponent.R

/**
 * Created by meyta.taliti on 11/05/23.
 */
class FeedTaggedProductBottomSheetAdapter(
    listener: FeedTaggedProductBottomSheetViewHolder.Listener
): BaseDiffUtilAdapter<ContentTaggedProductUiModel>(isFlexibleType = true) {

    init {
        delegatesManager.addDelegate(Delegate(listener))
    }

    override fun areItemsTheSame(
        oldItem: ContentTaggedProductUiModel,
        newItem: ContentTaggedProductUiModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ContentTaggedProductUiModel,
        newItem: ContentTaggedProductUiModel
    ): Boolean {
        return oldItem == newItem
    }

    class Delegate(
        private val listener: FeedTaggedProductBottomSheetViewHolder.Listener
    ): TypedAdapterDelegate<ContentTaggedProductUiModel, ContentTaggedProductUiModel, FeedTaggedProductBottomSheetViewHolder>(R.layout.item_feed_tagged_product) {

        override fun onBindViewHolder(
            item: ContentTaggedProductUiModel,
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
