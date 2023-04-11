package com.tokopedia.tokofood.feature.search.initialstate.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.presentation.viewholder.CustomPayloadViewHolder
import com.tokopedia.tokofood.databinding.SeeMoreCuisineItemInitialStateBinding
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.SeeMoreCuisineUiModel

class SeeMoreCuisineViewHolder(
    view: View,
    private val actionListener: ActionListener
) : CustomPayloadViewHolder<SeeMoreCuisineUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.see_more_cuisine_item_initial_state
    }

    private val binding = SeeMoreCuisineItemInitialStateBinding.bind(itemView)

    override fun bind(element: SeeMoreCuisineUiModel) {
        setSeeMoreCuisineBtn(element)
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        super.bindPayload(payloads)
        payloads?.let { (oldItem, newItem) ->
            if (oldItem is SeeMoreCuisineUiModel && newItem is SeeMoreCuisineUiModel) {
                if (oldItem != newItem) {
                    setSeeMoreCuisineBtn(newItem)
                }
            }
        }
    }

    private fun setSeeMoreCuisineBtn(element: SeeMoreCuisineUiModel) {
        with(binding) {
            seeMoreCuisineBtn.setOnClickListener {
                actionListener.onSeeMoreCuisineBtnClicked(element)
            }
        }
    }

    interface ActionListener {
        fun onSeeMoreCuisineBtnClicked(element: SeeMoreCuisineUiModel)
    }
}