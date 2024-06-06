package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import android.view.View
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam.ENTER_METHOD
import com.tokopedia.analytics.byteio.AppLogParam.ENTER_METHOD_FMT_PAGENAME
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.analytics.v2.DynamicIconTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.DynamicIconComponentListener
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.home_component.visitable.DynamicIconComponentDataModel

/**
 * Created by Lukas on 1/14/21.
 */
class DynamicIconComponentCallback(private val context: Context?, private val homeCategoryListener: HomeCategoryListener) : DynamicIconComponentListener {
    companion object {
        private val TOKONOW_ICON_ID = arrayOf("792", "831")
    }
    override fun onClickIcon(dynamicIcon: DynamicIconComponent.DynamicIcon, position: Int, iconPosition: Int) {
        DynamicIconTracking.sendDynamicIconClick(
            userId = homeCategoryListener.userId,
            position = position,
            iconPosition = iconPosition,
            dynamicIcon = dynamicIcon
        )
        setAppLogEnterMethod(iconPosition)
        RouteManager.route(context, dynamicIcon.applink)
    }

    override fun onImpressIcon(dynamicIcon: DynamicIconComponent.DynamicIcon, iconPosition: Int, adapterPosition: Int, view: View) {
        homeCategoryListener.putEEToTrackingQueue(
            DynamicIconTracking.sendDynamicIconImpress(
                userId = homeCategoryListener.userId,
                dynamicIcon = dynamicIcon,
                iconPosition = iconPosition,
                adapterPposition = adapterPosition,
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

    override fun onSuccessLoadImage() {
    }

    private fun setAppLogEnterMethod(position: Int) {
        AppLogAnalytics.setGlobalParamOnClick(
            enterMethod = ENTER_METHOD_FMT_PAGENAME.format("channel_icon_${position + 1}")
        )
    }
}
