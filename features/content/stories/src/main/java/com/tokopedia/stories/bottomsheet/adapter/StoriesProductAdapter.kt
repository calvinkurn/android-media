package com.tokopedia.stories.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.content.common.databinding.ViewContentTaggedProductBottomSheetCardBinding
import com.tokopedia.content.common.view.ContentTaggedProductBottomSheetItemView
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.content.common.R as commonR

/**
 * @author by astidhiyaa on 21/08/23
 */
class StoriesProductAdapter(
    listener: ContentTaggedProductBottomSheetItemView.Listener
) : BaseDiffUtilAdapter<ContentTaggedProductUiModel>(isFlexibleType = true) {

    init {
        delegatesManager.addDelegate(ProductDelegate(listener))
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

    inner class ProductDelegate(
        private val listener: ContentTaggedProductBottomSheetItemView.Listener
    ) : TypedAdapterDelegate<ContentTaggedProductUiModel, ContentTaggedProductUiModel, ProductViewHolder>(
        commonR.layout.view_content_tagged_product_bottom_sheet_card
    ) {

        override fun onBindViewHolder(
            item: ContentTaggedProductUiModel,
            holder: ProductViewHolder
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ProductViewHolder {
            return ProductViewHolder.create(
                parent,
                listener
            )
        }
    }

    class ProductViewHolder(
        binding: ViewContentTaggedProductBottomSheetCardBinding,
        listener: ContentTaggedProductBottomSheetItemView.Listener
    ) : ViewHolder(binding.root) {

        private val rootView = binding as ContentTaggedProductBottomSheetItemView

        init {
            rootView.setListener(listener)
        }

        fun bind(item: ContentTaggedProductUiModel) {
            rootView.bindData(item)
        }

        companion object {

            fun create(
                parent: ViewGroup,
                listener: ContentTaggedProductBottomSheetItemView.Listener
            ) = ProductViewHolder(
                ViewContentTaggedProductBottomSheetCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                ),
                listener
            )
        }
    }
}
