package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.cancelChildren
import javax.inject.Inject

/**
 * Created by mzennis on 2020-03-06.
 */
class PlayBottomSheetViewModel @Inject constructor(
    private val userSession: UserSessionInterface,
) : ViewModel() {

    private val _observableLoggedInInteractionEvent = MutableLiveData<Event<LoginStateEvent>>()
    val observableLoggedInInteractionEvent: LiveData<Event<LoginStateEvent>> = _observableLoggedInInteractionEvent

    fun doInteractionEvent(event: InteractionEvent) {
        _observableLoggedInInteractionEvent.value = Event(
                if (event.needLogin && !userSession.isLoggedIn) LoginStateEvent.NeedLoggedIn(event)
                else LoginStateEvent.InteractionAllowed(event)
        )
    }

    fun onFreezeBan() {
        viewModelScope.coroutineContext.cancelChildren()
    }
}