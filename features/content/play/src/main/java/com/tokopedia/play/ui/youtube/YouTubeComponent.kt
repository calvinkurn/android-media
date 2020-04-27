package com.tokopedia.play.ui.youtube

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.FragmentManager
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.uimodel.YouTube
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

/**
 * Created by jegul on 27/04/20
 */
open class YouTubeComponent(
        container: ViewGroup,
        fragmentManager: FragmentManager,
        bus: EventBusFactory,
        scope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<Unit> {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container, fragmentManager)

    init {
        scope.launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.Init -> {
                                uiView.hide()
                            }
                            is ScreenStateEvent.OnNewPlayRoomEvent -> if (it.event.isFreeze || it.event.isBanned) {
                                uiView.hide()
                                uiView.release()
                            }
                            is ScreenStateEvent.SetVideo -> if (it.videoPlayer is YouTube) {
                                uiView.setYouTubeId(it.videoPlayer.youtubeId)
                                uiView.show()
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

    protected open fun initView(container: ViewGroup, fragmentManager: FragmentManager) =
            YouTubeView(container, fragmentManager)
}