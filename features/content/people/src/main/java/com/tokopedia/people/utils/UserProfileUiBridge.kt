package com.tokopedia.people.utils

import com.tokopedia.people.di.UserProfileScope
import com.tokopedia.play_common.eventbus.EventBus
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 12, 2023
 */
@UserProfileScope
class UserProfileUiBridge @Inject constructor() {

    val eventBus = EventBus<Event>()

    sealed interface Event {
        object OpenProfileSettingsPage : Event
    }
}
