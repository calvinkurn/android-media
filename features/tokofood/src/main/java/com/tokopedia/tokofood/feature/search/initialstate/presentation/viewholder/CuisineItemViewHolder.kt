package com.tokopedia.tokofood.feature.search.initialstate.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.presentation.viewholder.CustomPayloadViewHolder
import com.tokopedia.tokofood.databinding.CuisineItemInitialStateBinding
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.ChipsListUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.CuisineItemUiModel

class CuisineItemViewHolder(
    view: View,
    private val actionListener: ActionListener
) : CustomPayloadViewHolder<CuisineItemUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.cuisine_item_initial_state
    }

    private val binding = CuisineItemInitialStateBinding.bind(itemView)

    override fun bind(element: CuisineItemUiModel) {
        setCuisineImage(element.imageUrl)
        setCuisineTitle(element.title)
        setCuisineAction(element)
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        super.bindPayload(payloads)
        payloads?.let { (oldItem, newItem) ->
            if (oldItem is CuisineItemUiModel && newItem is CuisineItemUiModel) {
                if (oldItem.imageUrl != newItem.imageUrl) {
                    setCuisineImage(newItem.imageUrl)
                }
                if (oldItem.title != newItem.title) {
                    setCuisineTitle(newItem.title)
                }
                if (oldItem != newItem) {
                    setCuisineAction(newItem)
                }
            }
        }
    }

    private fun setCuisineImage(imageUrl: String) {
        binding.ivCuisineItem.setImageUrl(imageUrl)
    }

    private fun setCuisineTitle(title: String) {
        binding.tvCuisineName.text = title
    }

    private fun setCuisineAction(item: CuisineItemUiModel) {
        itemView.setOnClickListener {
            actionListener.setCuisineItemClicked(item)
        }
    }

    interface ActionListener {
        fun setCuisineItemClicked(item: CuisineItemUiModel)
    }
}