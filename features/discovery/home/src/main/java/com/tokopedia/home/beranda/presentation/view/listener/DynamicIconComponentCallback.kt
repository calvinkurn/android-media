package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.analytics.v2.DynamicIconTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.DynamicIconComponentListener
import com.tokopedia.home_component.model.DynamicIconComponent

/**
 * Created by Lukas on 1/14/21.
 */
class DynamicIconComponentCallback (private val context: Context?, private val homeCategoryListener: HomeCategoryListener): DynamicIconComponentListener {
    override fun onClickIcon(dynamicIcon: DynamicIconComponent.DynamicIcon, position: Int, iconPosition: Int, type: Int) {
        DynamicIconTracking.sendDynamicIconClick(
                userId = homeCategoryListener.userId,
                position = position,
                iconPosition = iconPosition,
                type = type,
                dynamicIcon = dynamicIcon
        )
        RouteManager.route(context, dynamicIcon.applink)
    }

    override fun onImpressIcon(dynamicIcons: List<DynamicIconComponent.DynamicIcon>, position: Int, type: Int) {
        DynamicIconTracking.sendDynamicIconImpress(
                userId = homeCategoryListener.userId,
                dynamicIcons = dynamicIcons,
                position = position,
                type = type
        )
    }
}