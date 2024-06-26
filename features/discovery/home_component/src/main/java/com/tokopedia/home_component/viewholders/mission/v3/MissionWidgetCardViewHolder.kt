package com.tokopedia.home_component.viewholders.mission.v3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentItemSmallCardBinding
import com.tokopedia.home_component.visitable.Mission4SquareUiModel
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

class MissionWidgetCardViewHolder(
    view: View,
    private val listener: Mission4SquareWidgetListener?
) : RecyclerView.ViewHolder(view) {

    private val binding: HomeComponentItemSmallCardBinding? by viewBinding()

    fun bind(item: Mission4SquareUiModel) {
        binding?.card?.setData(item.card)

        binding?.root?.setOnClickListener {
            listener?.onMissionClicked(item, bindingAdapterPosition)
        }

        if (!item.isCache) {
            binding?.root?.addOnImpressionListener(item) {
                listener?.onMissionImpressed(item, bindingAdapterPosition)
            }
        }

        binding?.root?.addOnImpression1pxListener(item.data.appLogImpressHolder) {
            listener?.onMissionAppLogImpressed(item, bindingAdapterPosition)
        }
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.home_component_item_small_card

        fun create(parent: ViewGroup, listener: Mission4SquareWidgetListener?) =
            MissionWidgetCardViewHolder(
                LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false),
                listener
            )
    }
}
