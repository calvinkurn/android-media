package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.analytics.v2.DynamicIconTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.DynamicIconComponentListener
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.home_component.visitable.DynamicIconComponentDataModel

/**
 * Created by Lukas on 1/14/21.
 */
class DynamicIconComponentCallback (private val context: Context?, private val homeCategoryListener: HomeCategoryListener): DynamicIconComponentListener {
    companion object {
        val TOKONOW_ICON_ID = arrayOf("792", "831")
    }
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

    override fun onImpressIcon(dynamicIcon: DynamicIconComponent.DynamicIcon, iconPosition: Int, adapterPosition: Int, type: Int, view: View) {
        homeCategoryListener.putEEToTrackingQueue(
                DynamicIconTracking.sendDynamicIconImpress(
                        userId = homeCategoryListener.userId,
                        dynamicIcon = dynamicIcon,
                        iconPosition = iconPosition,
                        adapterPposition = adapterPosition,
                        type = type
                )
        )

        if (dynamicIcon.id in TOKONOW_ICON_ID) {
            homeCategoryListener.onTokonowViewCaptured(view, adapterPosition)
        }
    }

    override fun onIconChannelImpressed(iconComponentModel: DynamicIconComponentDataModel, parentPosition: Int) {

    }

    override fun onIconScroll(parentPosition: Int) {
        homeCategoryListener.dismissTokonowCoachmark(parentPosition)
    }
}