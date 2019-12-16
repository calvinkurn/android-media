package com.tokopedia.play.ui.toolbar

import android.view.ViewGroup
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.toolbar.interaction.PlayToolbarInteractionEvent
import com.tokopedia.play.view.event.ScreenStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 09/12/19
 */
class ToolbarComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        coroutineScope: CoroutineScope
) : UIComponent<PlayToolbarInteractionEvent>, ToolbarView.Listener, CoroutineScope by coroutineScope {

    private val uiView = initView(container)

    init {
        launch {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.VideoPropertyChanged -> uiView.setLiveBadgeVisibility(it.videoProp.type.isLive)
                            is ScreenStateEvent.SetTitle ->
                                uiView.setTitle(it.title)
                            is ScreenStateEvent.SetTitleToolbar ->
                                uiView.setTitleToolbar(it.titleToolbar)
                            is ScreenStateEvent.VideoStreamChanged ->
                                uiView.setLiveBadgeVisibility(it.videoStream.videoType.isLive)
                        }
                    }
        }
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<PlayToolbarInteractionEvent> {
        return bus.getSafeManagedFlow(PlayToolbarInteractionEvent::class.java)
    }

    override fun onBackButtonClicked(view: ToolbarView) {
        launch {
            bus.emit(PlayToolbarInteractionEvent::class.java, PlayToolbarInteractionEvent.BackButtonClicked)
        }
    }

    override fun onMoreButtonClicked(view: ToolbarView) {
        launch {
            bus.emit(PlayToolbarInteractionEvent::class.java, PlayToolbarInteractionEvent.MoreButtonClicked)
        }
    }

    override fun onFollowButtonClicked(view: ToolbarView) {
        launch {
            bus.emit(PlayToolbarInteractionEvent::class.java, PlayToolbarInteractionEvent.FollowButtonClicked)
        }
    }

    override fun onUnFollowButtonClicked(view: ToolbarView) {

        launch {
            bus.emit(PlayToolbarInteractionEvent::class.java, PlayToolbarInteractionEvent.UnFollowButtonClicked)
        }
    }

    private fun initView(container: ViewGroup) =
            ToolbarView(container, this)
}