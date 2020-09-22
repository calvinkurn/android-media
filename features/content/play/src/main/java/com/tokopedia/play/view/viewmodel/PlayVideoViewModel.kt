package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
) : PlayBaseViewModel(dispatchers.main) {

    companion object {

        private const val ONE_TAP_ONBOARDING_DELAY = 5000L
    }

    private val _observableOneTapOnboarding = MutableLiveData<Event<Unit>>()
    val observableOneTapOnboarding: LiveData<Event<Unit>>
        get() = _observableOneTapOnboarding

    init {
        val userId = userSession.userId
        if (!userSession.isLoggedIn || !playPreference.isOneTapOnboardingShown(userId)) {
            scope.launch {
                delay(ONE_TAP_ONBOARDING_DELAY)
                _observableOneTapOnboarding.value = Event(Unit)

                playPreference.setOneTapOnboardingShown(userId)
            }
        }
    }
}