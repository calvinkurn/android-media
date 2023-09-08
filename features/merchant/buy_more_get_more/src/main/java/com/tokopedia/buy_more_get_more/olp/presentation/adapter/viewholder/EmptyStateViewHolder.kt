package com.tokopedia.buy_more_get_more.olp.presentation.adapter.viewholder

import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.ItemEmptyStateBinding
import com.tokopedia.buy_more_get_more.olp.domain.entity.EmptyStateUiModel
import com.tokopedia.utils.view.binding.viewBinding

class EmptyStateViewHolder(itemView: View):
    AbstractViewHolder<EmptyStateUiModel>(itemView)  {

    private val binding: ItemEmptyStateBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_empty_state
    }

    override fun bind(element: EmptyStateUiModel) {
        binding?.emptyState?.apply {
            setImageUrl(element.imageUrl)
            setTitle(element.title)
            setDescription(element.description)
        }
    }
}
