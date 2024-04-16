package com.tokopedia.home_component.viewholders.shorten.viewholder.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentItemSmallCardBinding
import com.tokopedia.home_component.visitable.shorten.ItemMissionWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemMissionWidgetViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {

    private val binding: HomeComponentItemSmallCardBinding? by viewBinding()

    fun bind(element: ItemMissionWidgetUiModel) {
        binding?.card?.setData(element.card)
    }

    companion object {

        @LayoutRes
        private val LAYOUT = R.layout.home_component_item_small_card

        fun create(parent: ViewGroup) = ItemMissionWidgetViewHolder(
            LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false)
        )
    }
}
