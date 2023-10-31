package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.home_component.databinding.HomeComponentItemMissionWidgetCardBinding
import com.tokopedia.home_component.listener.MissionWidgetComponentListener
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMissionWidgetDataModel
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class MissionWidgetCardItemViewHolder(
    view: View,
    private val missionWidgetComponentListener: MissionWidgetComponentListener,
) : AbstractViewHolder<CarouselMissionWidgetDataModel>(view) {

    private var binding: HomeComponentItemMissionWidgetCardBinding? by viewBinding()

    companion object {
        val LAYOUT = home_componentR.layout.home_component_item_mission_widget_card
        private const val MAX_LINES_MISSION_WIDGET = 2
    }

    override fun bind(element: CarouselMissionWidgetDataModel) {
        setLayout(element)
    }

    private fun setLayout(element: CarouselMissionWidgetDataModel) {
        binding?.run {
            cardContainerMissionWidget.animateOnPress = element.animateOnPress
            cardContainerMissionWidget.rootView.setOnClickListener {
                missionWidgetComponentListener.onMissionClicked(element, element.cardPosition)
            }
            cardContainerMissionWidget.rootView.addOnImpressionListener(element) {
                missionWidgetComponentListener.onMissionImpressed(element, element.cardPosition)
            }
            imageMissionWidget.loadImage(element.data.imageURL)
            titleMissionWidget.text = element.data.title
            subtitleMissionWidget.height = element.subtitleHeight
            subtitleMissionWidget.text = element.data.subTitle
            subtitleMissionWidget.maxLines = MAX_LINES_MISSION_WIDGET
            subtitleMissionWidget.ellipsize = TextUtils.TruncateAt.END
        }
    }
}
