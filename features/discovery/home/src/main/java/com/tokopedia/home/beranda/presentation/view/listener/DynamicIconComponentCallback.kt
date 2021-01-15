package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.DynamicIconTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.DynamicIconComponentListener
import com.tokopedia.home_component.model.DynamicIconComponent

/**
 * Created by Lukas on 1/14/21.
 */
class DynamicIconComponentCallback (private val homeCategoryListener: HomeCategoryListener): DynamicIconComponentListener {
    override fun onClickIcon(dynamicIcon: DynamicIconComponent.DynamicIcon, position: Int, iconPosition: Int) {
        DynamicIconTracking.sendDynamicIconClick(
                userId = homeCategoryListener.userId,
                position = position,
                iconPosition = iconPosition,
                dynamicIcon = dynamicIcon
        )
    }

    override fun onImpressIcon(dynamicIcons: List<DynamicIconComponent.DynamicIcon>, position: Int) {
        homeCategoryListener.getTrackingQueueObj()?.let { trackingQueue ->
            DynamicIconTracking.sendDynamicIconImpress(
                    trackingQueue = trackingQueue,
                    userId = homeCategoryListener.userId,
                    position = position,
                    dynamicIcons = dynamicIcons
            )
        }
    }
}