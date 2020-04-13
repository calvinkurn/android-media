package com.tokopedia.play.ui.toolbar

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.toolbar.interaction.PlayToolbarInteractionEvent
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 09/12/19
 */
open class ToolbarComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        coroutineScope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<PlayToolbarInteractionEvent>, ToolbarView.Listener, CoroutineScope by coroutineScope {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    init {
        launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            ScreenStateEvent.Init -> uiView.show()
                            is ScreenStateEvent.SetPartnerInfo ->
                                uiView.setPartnerInfo(it.partnerInfo)
                            is ScreenStateEvent.BottomInsetsChanged -> if (it.isAnyShown) uiView.hide() else uiView.show()
                            is ScreenStateEvent.OnNewPlayRoomEvent -> if(it.event.isFreeze || it.event.isBanned) uiView.show()
                            is ScreenStateEvent.OnNoMoreAction -> uiView.hideActionMore()
                            is ScreenStateEvent.FollowPartner -> uiView.setFollowStatus(it.shouldFollow)
                            is ScreenStateEvent.SetTotalCart -> uiView.setCartInfo(it.cartUiModel)
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

    override fun onCartButtonClicked(view: ToolbarView) {
        launch {
            bus.emit(PlayToolbarInteractionEvent::class.java, PlayToolbarInteractionEvent.CartButtonClicked)
        }
    }

    protected open fun initView(container: ViewGroup) =
            ToolbarView(container, this)
}