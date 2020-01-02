package com.tokopedia.play.ui.stats

import android.view.ViewGroup
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.view.event.ScreenStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

/**
 * Created by jegul on 03/12/19
 */
class StatsComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        coroutineScope: CoroutineScope
) : UIComponent<Unit>, CoroutineScope by coroutineScope {

    private val uiView = initView(container)

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    init {
        launch {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.SetTotalViews -> uiView.setTotalViews(it.totalView)
                            is ScreenStateEvent.SetTotalLikes -> uiView.setTotalLikes(it.totalLikes)
                            is ScreenStateEvent.KeyboardStateChanged -> if (it.isShown) uiView.hide() else uiView.show()
                        }
                    }
        }
    }

    override fun getUserInteractionEvents(): Flow<Unit> {
        return emptyFlow()
    }

    private fun initView(container: ViewGroup): StatsView =
            StatsView(container)
}