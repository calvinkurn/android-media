package com.tokopedia.home_component.viewholders.shorten.viewholder.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentItemSmallCardBinding
import com.tokopedia.home_component.viewholders.shorten.viewholder.listener.ThumbnailWidgetListener
import com.tokopedia.home_component.visitable.shorten.ItemThumbnailWidgetUiModel
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.utils.view.binding.viewBinding

class ItemThumbnailWidgetViewHolder(
    view: View,
    private val thumbnailWidgetListener: ThumbnailWidgetListener
) : RecyclerView.ViewHolder(view) {

    private val binding: HomeComponentItemSmallCardBinding? by viewBinding()

    fun bind(element: ItemThumbnailWidgetUiModel) {
        binding?.card?.setData(element.card)
        binding?.card?.setOnClickListener {
            thumbnailWidgetListener.thumbnailClicked(element, bindingAdapterPosition)
        }

        binding?.root?.addOnImpression1pxListener(element.impression) {
            thumbnailWidgetListener.thumbnailImpressed(element, bindingAdapterPosition)
        }
    }

    companion object {

        @LayoutRes
        private val LAYOUT = R.layout.home_component_item_small_card

        fun create(parent: ViewGroup, thumbnailWidgetListener: ThumbnailWidgetListener) =
            ItemThumbnailWidgetViewHolder(
                LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false),
                thumbnailWidgetListener
            )
    }
}
