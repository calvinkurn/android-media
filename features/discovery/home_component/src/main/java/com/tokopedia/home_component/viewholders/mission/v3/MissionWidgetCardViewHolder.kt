package com.tokopedia.home_component.viewholders.mission.v3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.LayoutStaticMissionWidgetCardBinding
import com.tokopedia.home_component.util.loadImageRounded
import com.tokopedia.home_component.visitable.MissionWidgetDataModel
import com.tokopedia.utils.view.binding.viewBinding

class MissionWidgetCardViewHolder constructor(
    view: View,
    private val listener: Mission4SquareWidgetListener?
) : RecyclerView.ViewHolder(view) {

    private val binding: LayoutStaticMissionWidgetCardBinding? by viewBinding()

    fun bind(item: MissionWidgetDataModel) {
        setupProductBannerImage(item.imageURL)
        setTitle(item.title)
        setSubtitle(item.subTitle)

        itemView.setOnClickListener {
            listener?.onMissionClicked(item, bindingAdapterPosition)
        }
    }

    private fun setupProductBannerImage(url: String) {
        val radius = itemView.context.resources
            .getDimensionPixelSize(R.dimen.home_mission_widget_clear_image_corner_radius)
        binding?.imgBanner?.loadImageRounded(url, radius)
    }

    private fun setTitle(title: String) {
        binding?.txtTitle?.text = title
    }

    private fun setSubtitle(subtitle: String) {
        binding?.txtSubtitle?.text = subtitle
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.layout_static_mission_widget_card

        fun create(parent: ViewGroup, listener: Mission4SquareWidgetListener?) =
            MissionWidgetCardViewHolder(
                LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false),
                listener
            )
    }
}
