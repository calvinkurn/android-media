package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeBannerItemMissionWidgetBinding
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMissionWidgetDataModel
import com.tokopedia.home_component.util.ImageUnifyUtils
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class MissionWidgetItemViewHolder (
    view: View,
    private val cardInteraction: Boolean = false
): AbstractViewHolder<CarouselMissionWidgetDataModel>(view) {

    private var binding: HomeBannerItemMissionWidgetBinding? by viewBinding()
    companion object{
        val LAYOUT = R.layout.home_banner_item_mission_widget
    }

    override fun bind(element: CarouselMissionWidgetDataModel) {
        setLayout(element)
    }

    private fun setLayout(element: CarouselMissionWidgetDataModel){
        binding?.cardContainerMissionWidget?.animateOnPress = if (cardInteraction) {
            CardUnify2.ANIMATE_OVERLAY_BOUNCE
        } else CardUnify2.ANIMATE_OVERLAY
        binding?.imageMissionWidget?.setImageUrl(element.imageURL)
        binding?.imageMissionWidget?.outlineProvider = ImageUnifyUtils.cornerRadiusTopImageUnify()
        binding?.titleMissionWidget?.text = element.title
        binding?.subtitleMissionWidget?.text = element.subTitle
        binding?.subtitleMissionWidget?.height = element.subtitleHeight
    }
}