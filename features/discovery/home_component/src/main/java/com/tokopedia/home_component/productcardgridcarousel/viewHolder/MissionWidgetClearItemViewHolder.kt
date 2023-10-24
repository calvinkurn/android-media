package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.home_component.databinding.HomeComponentItemMissionWidgetClearBinding
import com.tokopedia.home_component.listener.MissionWidgetComponentListener
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMissionWidgetDataModel
import com.tokopedia.home_component.util.loadImageRounded
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class MissionWidgetClearItemViewHolder(
    view: View,
    private val missionWidgetComponentListener: MissionWidgetComponentListener,
) : AbstractViewHolder<CarouselMissionWidgetDataModel>(view) {

    private var binding: HomeComponentItemMissionWidgetClearBinding? by viewBinding()

    companion object {
        val LAYOUT = home_componentR.layout.home_component_item_mission_widget_clear
    }

    override fun bind(element: CarouselMissionWidgetDataModel) {
        setupListeners(element)
        renderImage(element.data.imageURL)
        renderText(element)
    }

    private fun setupListeners(element: CarouselMissionWidgetDataModel) {
        binding?.run {
            containerMissionWidget.setOnClickListener {
                missionWidgetComponentListener.onMissionClicked(element, element.cardPosition)
            }
            containerMissionWidget.addOnImpressionListener(element) {
                missionWidgetComponentListener.onMissionImpressed(element, element.cardPosition)
            }
        }
    }

    private fun renderImage(imageUrl: String) {
        binding?.imageMissionWidget?.loadImageRounded(
            imageUrl, 
            itemView.context.resources.getDimensionPixelSize(home_componentR.dimen.home_mission_widget_clear_image_corner_radius)
        )
    }

    private fun renderText(element: CarouselMissionWidgetDataModel) {
        binding?.run {
            if(element.withSubtitle) {
                titleMissionWidget.setWeight(Typography.BOLD)
                subtitleMissionWidget.height = element.subtitleHeight
                subtitleMissionWidget.text = element.data.subTitle
                subtitleMissionWidget.show()
            } else {
                titleMissionWidget.setWeight(Typography.REGULAR)
                subtitleMissionWidget.hide()
            }
            titleMissionWidget.height = element.titleHeight
            titleMissionWidget.text = element.data.title
        }
    }
}
