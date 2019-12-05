package com.tokopedia.play.ui.video_control

import android.view.ViewGroup
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayVODType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

/**
 * Created by jegul on 05/12/19
 */
class VideoControlComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        coroutineScope: CoroutineScope
) : UIComponent<Unit>, CoroutineScope by coroutineScope {

    private val uiView = initView(container)

    init {
        launch {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.Play -> {
                                uiView.run {
                                    if (it.vodType is PlayVODType.Live) {
                                        setPlayer(null)
                                        uiView.hide()
                                    }
                                    else {
                                        setPlayer(it.vodType.exoPlayer)
                                        uiView.show()
                                    }
                                }
                            }
                        }
                    }
        }
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<Unit> {
        return emptyFlow()
    }

    private fun initView(container: ViewGroup) =
            VideoControlView(container)
}