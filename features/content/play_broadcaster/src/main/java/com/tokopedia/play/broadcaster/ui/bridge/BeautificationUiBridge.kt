package com.tokopedia.play.broadcaster.ui.bridge

import com.tokopedia.play.broadcaster.di.ActivityRetainedScope
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 17, 2023
 */
@ActivityRetainedScope
class BeautificationUiBridge @Inject constructor() {

    val eventBus = EventBus<Event>()

    sealed interface Event {
        data class BeautificationBottomSheetShown(val bottomSheetHeight: Int) : Event
        object BeautificationBottomSheetDismissed : Event
    }
}
