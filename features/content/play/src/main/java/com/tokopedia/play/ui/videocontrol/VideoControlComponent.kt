package com.tokopedia.play.ui.videocontrol

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.view.event.ScreenStateEvent
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
        uiView.hide()

        launch {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.VideoPropertyChanged -> {
                                uiView.run {
                                    if (it.videoProp.type.isLive) uiView.hide()
                                    else if (it.videoProp.type.isVod) uiView.show()
                                }
                            }
                            is ScreenStateEvent.SetVideo -> {
                                uiView.setPlayer(it.videoPlayer)
                            }
                            is ScreenStateEvent.VideoStreamChanged -> {
                                if (it.videoStream.videoType.isLive) uiView.hide()
                                else if (it.videoStream.videoType.isVod) uiView.show()
                            }
                        }
                    }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        uiView.onDestroy()
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