package com.tokopedia.play.ui.toolbar

import android.view.ViewGroup
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.toolbar.interaction.PlayToolbarInteractionEvent
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.ui.toolbar.model.PartnerType
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
                            is ScreenStateEvent.SetChannelTitle ->
                                uiView.setTitle(it.title)
                            is ScreenStateEvent.SetPartnerInfo ->
                                uiView.setPartnerInfo(it.partnerInfo)
                            is ScreenStateEvent.VideoStreamChanged ->
                                uiView.setLiveBadgeVisibility(it.videoStream.videoType.isLive)
                            is ScreenStateEvent.KeyboardStateChanged -> if (it.isShown) uiView.hide() else uiView.show()
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

    override fun onFollowButtonClicked(view: ToolbarView, partnerId: Long, action: PartnerFollowAction) {
        launch {
            bus.emit(PlayToolbarInteractionEvent::class.java, PlayToolbarInteractionEvent.FollowButtonClicked(partnerId, action))
        }
    }

    override fun onPartnerNameClicked(view: ToolbarView, partnerId: Long, type: PartnerType) {
        launch {
            bus.emit(PlayToolbarInteractionEvent::class.java, PlayToolbarInteractionEvent.PartnerNameClicked(partnerId, type))
        }
    }

    private fun initView(container: ViewGroup) =
            ToolbarView(container, this)
}