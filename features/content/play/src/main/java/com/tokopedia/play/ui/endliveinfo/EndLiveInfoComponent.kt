package com.tokopedia.play.ui.endliveinfo

import android.view.ViewGroup
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.endliveinfo.interaction.EndLiveInfoInteractionEvent
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayRoomEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 14/01/20
 */
class EndLiveInfoComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        coroutineScope: CoroutineScope
) : UIComponent<EndLiveInfoInteractionEvent>, CoroutineScope by coroutineScope, EndLiveInfoView.Listener {

    private val uiView = initView(container)

    init {
        uiView.hide()

        launch {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.OnNewPlayRoomEvent -> if(it.event is PlayRoomEvent.Freeze) {
                                uiView.setInfo(
                                        title = it.event.title,
                                        message = it.event.message,
                                        btnTitle = it.event.btnTitle,
                                        btnUrl = it.event.btnUrl
                                )
                                uiView.show()
                            } else {
                                uiView.hide()
                            }
                            is ScreenStateEvent.SetTotalViews -> uiView.statsView.setTotalViews(it.totalView)
                            is ScreenStateEvent.SetTotalLikes -> uiView.statsView.setTotalLikes(it.totalLikes)
                        }
                    }
        }
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<EndLiveInfoInteractionEvent> {
        return bus.getSafeManagedFlow(EndLiveInfoInteractionEvent::class.java)
    }

    override fun onButtonActionClicked(view: EndLiveInfoView, btnUrl: String) {
        launch {
            bus.emit(EndLiveInfoInteractionEvent::class.java, EndLiveInfoInteractionEvent.ButtonActionClicked(btnUrl))
        }
    }

    private fun initView(container: ViewGroup) =
            EndLiveInfoView(container, this)
}