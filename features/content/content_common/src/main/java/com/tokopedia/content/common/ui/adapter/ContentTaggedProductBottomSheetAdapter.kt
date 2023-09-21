package com.tokopedia.content.common.ui.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.content.common.ui.viewholder.ContentTaggedProductBottomSheetViewHolder
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.content.common.R as commonR

/**
 * Created by meyta.taliti on 11/05/23.
 */
class ContentTaggedProductBottomSheetAdapter(
    listener: ContentTaggedProductBottomSheetViewHolder.Listener
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
        private val listener: ContentTaggedProductBottomSheetViewHolder.Listener
    ): TypedAdapterDelegate<ContentTaggedProductUiModel, ContentTaggedProductUiModel, ContentTaggedProductBottomSheetViewHolder>(commonR.layout.item_content_tagged_product) {

        override fun onBindViewHolder(
            item: ContentTaggedProductUiModel,
            holder: ContentTaggedProductBottomSheetViewHolder
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ContentTaggedProductBottomSheetViewHolder {
            return ContentTaggedProductBottomSheetViewHolder.create(
                parent,
                listener
            )
        }
    }

}
