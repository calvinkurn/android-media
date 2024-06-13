package com.tokopedia.home_component.viewholders.shorten.viewholder.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentItemSmallCardBinding
import com.tokopedia.home_component.viewholders.shorten.viewholder.listener.ProductWidgetListener
import com.tokopedia.home_component.visitable.shorten.ItemProductWidgetUiModel
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.utils.view.binding.viewBinding

class ItemProductWidgetViewHolder(
    view: View,
    private val productWidgetListener: ProductWidgetListener
) : RecyclerView.ViewHolder(view) {

    private val binding: HomeComponentItemSmallCardBinding? by viewBinding()

    fun bind(element: ItemProductWidgetUiModel) {
        binding?.card?.setData(element.card)
        binding?.card?.setOnClickListener {
            productWidgetListener.itemProductClicked(element, bindingAdapterPosition)
        }

        binding?.root?.addOnImpression1pxListener(element.impression) {
            productWidgetListener.itemProductImpressed(element, bindingAdapterPosition)
        }
    }

    companion object {

        @LayoutRes
        private val LAYOUT = R.layout.home_component_item_small_card

        fun create(parent: ViewGroup, productWidgetListener: ProductWidgetListener) =
            ItemProductWidgetViewHolder(
                LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false),
                productWidgetListener
            )
    }
}

