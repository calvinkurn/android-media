package com.tokopedia.play.ui.video

import android.view.ViewGroup
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.component.UIView
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * Created by jegul on 02/12/19
 */
class VideoComponent(
        container: ViewGroup,
        bus: EventBusFactory
) : UIComponent<Unit> {

    private val uiView = initUiView(container)

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): ReceiveChannel<Unit> {
        throw IllegalArgumentException()
    }

    private fun initUiView(container: ViewGroup): UIView =
            VideoView(container)
}