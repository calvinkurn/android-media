package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayVideoViewModel @Inject constructor(
        dispatchers: CoroutineDispatcherProvider,
        playPreference: PlayPreference,
        userSession: UserSessionInterface
) : ViewModel() {

    companion object {

        private const val ONBOARDING_DELAY = 5000L
    }

    private val _observableOnboarding = MutableLiveData<Event<Unit>>()
    val observableOnboarding: LiveData<Event<Unit>>
        get() = _observableOnboarding

    init {
        val userId = userSession.userId
        if (!userSession.isLoggedIn || !playPreference.isOnboardingShown(userId)) {
            viewModelScope.launch(dispatchers.main) {
                delay(ONBOARDING_DELAY)
                _observableOnboarding.value = Event(Unit)

                playPreference.setOnboardingShown(userId)
            }
        }
    }
}