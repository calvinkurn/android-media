package com.tokopedia.home_component.viewholders.mission.v3

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.LayoutStaticMissionWidgetCardBinding
import com.tokopedia.home_component.util.loadImageRounded
import com.tokopedia.home_component.visitable.Mission4SquareUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class MissionWidgetCardViewHolder(
    view: View,
    private val listener: Mission4SquareWidgetListener?
) : RecyclerView.ViewHolder(view) {

    private val binding: LayoutStaticMissionWidgetCardBinding? by viewBinding()

    fun bind(item: Mission4SquareUiModel) {
        setupProductBannerImage(item.data.imageURL)

        binding?.txtTitle?.shouldTypographyStyleApplied(item.title)
        binding?.txtSubtitle?.shouldTypographyStyleApplied(item.subtitle)

        itemView.setOnClickListener {
            listener?.onMissionClicked(item, bindingAdapterPosition)
        }
    }

    private fun setupProductBannerImage(url: String) {
        val radius = itemView.context.resources
            .getDimensionPixelSize(R.dimen.home_mission_widget_clear_image_corner_radius)
        binding?.imgBanner?.loadImageRounded(url, radius)
    }

    private fun Typography?.shouldTypographyStyleApplied(
        content: Pair<String, Mission4SquareUiModel.TextStyle?>
    ) {
        val (title, textStyle) = content

        // text
        this?.text = title

        // style
        textStyle?.let { style ->
            this?.weightType = if (style.isBold) Typography.BOLD else Typography.REGULAR
            this?.setTextColor(Color.parseColor(style.textColor))
        }
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
