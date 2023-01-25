package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeBannerItemMissionWidgetBinding
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMissionWidgetDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class MissionWidgetItemViewHolder(
    view: View,
    private val cardInteraction: Boolean = false
) : AbstractViewHolder<CarouselMissionWidgetDataModel>(view) {

    private var binding: HomeBannerItemMissionWidgetBinding? by viewBinding()

    companion object {
        val LAYOUT = R.layout.home_banner_item_mission_widget
        private const val MAX_LINES_MISSION_WIDGET = 2
    }

    override fun bind(element: CarouselMissionWidgetDataModel) {
        setLayout(element)
    }

    private fun setLayout(element: CarouselMissionWidgetDataModel) {
        binding?.run {
            cardContainerMissionWidget.animateOnPress = if (cardInteraction) {
                CardUnify2.ANIMATE_OVERLAY_BOUNCE
            } else {
                CardUnify2.ANIMATE_OVERLAY
            }
            cardContainerMissionWidget.rootView.setOnClickListener {
                element.missionWidgetComponentListener.onMissionClicked(element, absoluteAdapterPosition)
            }
            cardContainerMissionWidget.rootView.addOnImpressionListener(element) {
                element.missionWidgetComponentListener.onMissionImpressed(element, absoluteAdapterPosition)
            }
            imageMissionWidget.setImageUrl(element.imageURL)
            titleMissionWidget.text = element.title
            subtitleMissionWidget.height = element.subtitleHeight
            subtitleMissionWidget.text = element.subTitle
            subtitleMissionWidget.maxLines = MAX_LINES_MISSION_WIDGET
            subtitleMissionWidget.ellipsize = TextUtils.TruncateAt.END
        }
    }
}
