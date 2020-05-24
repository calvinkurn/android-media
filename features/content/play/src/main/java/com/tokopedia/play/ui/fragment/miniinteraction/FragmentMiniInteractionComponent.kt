package com.tokopedia.play.ui.fragment.miniinteraction

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.FragmentManager
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

/**
 * Created by jegul on 06/05/20
 */
class FragmentMiniInteractionComponent(
        channelId: String,
        container: ViewGroup,
        fragmentManager: FragmentManager,
        private val bus: EventBusFactory,
        scope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<Unit> {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(channelId, container, fragmentManager)

    init {
        scope.launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.Init -> {
                                uiView.init()
                                uiView.hide()
                            }
                            is ScreenStateEvent.OrientationChanged ->
                                if (it.orientation.isLandscape) uiView.show() else uiView.hide()
                            is ScreenStateEvent.VideoStreamChanged ->
                                if (!it.videoStream.orientation.isHorizontal) {
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

    override fun getUserInteractionEvents(): Flow<Unit> {
        return emptyFlow()
    }

    protected open fun initView(channelId: String, container: ViewGroup, fragmentManager: FragmentManager) =
            FragmentMiniInteractionView(channelId, container, fragmentManager)

}