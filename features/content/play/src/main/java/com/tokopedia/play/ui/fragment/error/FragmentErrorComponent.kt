package com.tokopedia.play.ui.fragment.error

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
 * Created by jegul on 05/05/20
 */
class FragmentErrorComponent(
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
                            is ScreenStateEvent.Init -> uiView.hide()
                            is ScreenStateEvent.ShowGlobalError -> {
                                if (it.shouldShow) uiView.show()
                                else uiView.hide()
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
            FragmentErrorView(channelId, container, fragmentManager)

}