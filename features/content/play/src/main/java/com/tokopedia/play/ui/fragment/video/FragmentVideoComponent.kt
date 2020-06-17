package com.tokopedia.play.ui.fragment.video

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.FragmentManager
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.fragment.video.interaction.FragmentVideoInteractionEvent
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 05/05/20
 */
class FragmentVideoComponent(
        channelId: String,
        container: ViewGroup,
        fragmentManager: FragmentManager,
        private val bus: EventBusFactory,
        private val scope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<FragmentVideoInteractionEvent>, FragmentVideoView.Listener {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(channelId, container, fragmentManager)

    init {
        scope.launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.Init -> {
                                uiView.init()
                                uiView.show()
                            }
                            is ScreenStateEvent.SetVideo -> if (it.videoPlayer.isYouTube) {
                                uiView.release()
                                uiView.hide()
                            }
                            is ScreenStateEvent.OnNewPlayRoomEvent -> if (it.event.isBanned || it.event.isFreeze) {
                                uiView.release()
                                uiView.hide()
                            }
                        }
                    }
        }
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<FragmentVideoInteractionEvent> {
        return bus.getSafeManagedFlow(FragmentVideoInteractionEvent::class.java)
    }

    override fun onFragmentClicked(view: FragmentVideoView) {
        scope.launch {
            bus.emit(FragmentVideoInteractionEvent::class.java, FragmentVideoInteractionEvent.OnClicked)
        }
    }

    protected open fun initView(channelId: String, container: ViewGroup, fragmentManager: FragmentManager) =
            FragmentVideoView(channelId, container, fragmentManager, this)

}