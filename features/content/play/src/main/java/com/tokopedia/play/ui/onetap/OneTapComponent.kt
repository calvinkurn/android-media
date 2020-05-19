package com.tokopedia.play.ui.onetap

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
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
 * Created by jegul on 20/12/19
 */
open class OneTapComponent(
        container: ViewGroup,
        bus: EventBusFactory,
        scope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<Unit> {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    init {
        scope.launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.Init -> uiView.hide()
                            is ScreenStateEvent.ShowOneTapOnboarding -> {
                                val screenOrientation = it.stateHelper.screenOrientation
                                val videoOrientation = it.stateHelper.videoOrientation
                                if (!screenOrientation.isLandscape && !videoOrientation.isHorizontal) uiView.showAnimated()
                            }
                            is ScreenStateEvent.OnNewPlayRoomEvent -> if(it.event.isFreeze || it.event.isBanned) uiView.hide()
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

    protected open fun initView(container: ViewGroup) =
            OneTapView(container)
}