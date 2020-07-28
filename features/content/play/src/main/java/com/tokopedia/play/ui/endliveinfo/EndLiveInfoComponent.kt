package com.tokopedia.play.ui.endliveinfo

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.endliveinfo.interaction.EndLiveInfoInteractionEvent
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayRoomEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 14/01/20
 */
open class EndLiveInfoComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        private val scope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<EndLiveInfoInteractionEvent>, EndLiveInfoView.Listener {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    init {
        scope.launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.Init -> uiView.hide()
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
                            is ScreenStateEvent.SetTotalViews -> uiView.setTotalViews(it.totalView)
                            is ScreenStateEvent.SetTotalLikes -> uiView.setTotalLikes(it.totalLikes)
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
        scope.launch {
            bus.emit(EndLiveInfoInteractionEvent::class.java, EndLiveInfoInteractionEvent.ButtonActionClicked(btnUrl))
        }
    }

    protected open fun initView(container: ViewGroup) =
            EndLiveInfoView(container, this)
}