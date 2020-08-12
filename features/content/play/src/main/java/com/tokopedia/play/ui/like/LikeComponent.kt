package com.tokopedia.play.ui.like

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.like.interaction.LikeInteractionEvent
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 02/12/19
 */
open class LikeComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        private val scope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<LikeInteractionEvent>, LikeView.Listener {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    init {
        scope.launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.Init -> {
                                uiView.setEnabled(false)
                                uiView.show()
                            }
                            is ScreenStateEvent.BottomInsetsChanged -> if (it.isAnyShown) uiView.hide() else uiView.show()
                            is ScreenStateEvent.LikeContent -> {
                                uiView.setEnabled(true)
                                uiView.playLikeAnimation(it.likeState.isLiked, !it.likeState.fromNetwork && !it.isFirstTime)
                            }
                            is ScreenStateEvent.OnNewPlayRoomEvent -> if(it.event.isFreeze || it.event.isBanned) uiView.hide()
                            is ScreenStateEvent.SetTotalLikes -> uiView.setTotalLikes(it.totalLikes)
                        }
                    }
        }
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<LikeInteractionEvent> {
        return bus.getSafeManagedFlow(LikeInteractionEvent::class.java)
    }

    override fun onLikeClicked(view: LikeView, shouldLike: Boolean) {
        scope.launch {
            bus.emit(
                    LikeInteractionEvent::class.java,
                    LikeInteractionEvent.LikeClicked(shouldLike)
            )
        }
    }

    protected open fun initView(container: ViewGroup) =
            LikeView(container, this)
}