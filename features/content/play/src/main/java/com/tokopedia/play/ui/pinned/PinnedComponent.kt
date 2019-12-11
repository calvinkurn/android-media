package com.tokopedia.play.ui.pinned

import android.view.ViewGroup
import com.tokopedia.applink.RouteManager
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
 * Created by jegul on 03/12/19
 */
class PinnedComponent(
        private val container: ViewGroup,
        private val bus: EventBusFactory,
        coroutineScope: CoroutineScope
) : UIComponent<Unit>, PinnedView.Listener, CoroutineScope by coroutineScope {

    private val uiView = initView(container)

    init {
        launch {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.SetPinned -> uiView.setPinnedMessage(it.pinnedMessage)
                            is ScreenStateEvent.SetVideo ->
                                if (it.vodType is PlayVODType.Live) uiView.show() else uiView.hide()
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

    override fun onPinnedActionClicked(pinnedView: PinnedView, applink: String) {
        RouteManager.route(container.context, applink)
    }

    private fun initView(container: ViewGroup): PinnedView =
            PinnedView(container, this)
}