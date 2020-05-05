package com.tokopedia.play.ui.videocontrol

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

/**
 * Created by jegul on 05/12/19
 */
open class VideoControlComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        coroutineScope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<Unit>, CoroutineScope by coroutineScope {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    init {
        launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            ScreenStateEvent.Init -> uiView.hide()
                            is ScreenStateEvent.SetVideo -> {
                                uiView.setPlayer(it.videoPlayer)
                            }
                            is ScreenStateEvent.VideoStreamChanged -> {
                                if (it.videoStream.channelType.isVod && !it.stateHelper.bottomInsets.isAnyShown) uiView.show()
                                else uiView.hide()
                            }
                            is ScreenStateEvent.OnNewPlayRoomEvent -> if (it.event.isFreeze || it.event.isBanned) {
                                uiView.hide()
                                uiView.setPlayer(null)
                            }
                            is ScreenStateEvent.BottomInsetsChanged -> {
                                if (!it.isAnyShown && it.stateHelper.channelType.isVod) uiView.show()
                                else uiView.hide()
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

    protected open fun initView(container: ViewGroup) =
            VideoControlView(container)
}