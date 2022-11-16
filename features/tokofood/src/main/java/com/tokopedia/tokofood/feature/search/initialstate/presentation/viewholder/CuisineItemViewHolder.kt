package com.tokopedia.tokofood.feature.search.initialstate.presentation.viewholder

import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.LayoutRes
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.presentation.listener.TokofoodScrollChangedListener
import com.tokopedia.tokofood.common.presentation.viewholder.CustomPayloadViewHolder
import com.tokopedia.tokofood.common.util.TokofoodExt.addAndReturnImpressionListener
import com.tokopedia.tokofood.common.util.TokofoodExt.clickWithDebounce
import com.tokopedia.tokofood.databinding.CuisineItemInitialStateBinding
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.CuisineItemUiModel

class CuisineItemViewHolder(
    view: View,
    private val actionListener: ActionListener,
    private val tokofoodScrollChangedListener: TokofoodScrollChangedListener
) : CustomPayloadViewHolder<CuisineItemUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.cuisine_item_initial_state
    }

    private val binding = CuisineItemInitialStateBinding.bind(itemView)

    override fun bind(element: CuisineItemUiModel) {
        bindImpressionCuisineListener(element, bindingAdapterPosition)
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
        itemView.clickWithDebounce {
            actionListener.setCuisineItemClicked(item)
        }
    }

    private fun bindImpressionCuisineListener(
        item: CuisineItemUiModel,
        position: Int
    ) {
        binding.root.addAndReturnImpressionListener(item, tokofoodScrollChangedListener) {
            actionListener.onImpressCuisineItem(item, position)
        }
    }

    interface ActionListener {
        fun setCuisineItemClicked(item: CuisineItemUiModel)
        fun onImpressCuisineItem(item: CuisineItemUiModel, position: Int)
        fun onSetCuisineImpressionListener(listener: ViewTreeObserver.OnScrollChangedListener)
    }
}
