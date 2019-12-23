package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.util.event.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayVideoViewModel @Inject constructor(
        dispatchers: CoroutineDispatcherProvider
) : BaseViewModel(dispatchers.main) {

    companion object {

        private const val ONE_TAP_ONBOARDING_DELAY = 5000L
    }

    private val _observableOneTapOnboarding = MutableLiveData<Event<Unit>>()
    val observableOneTapOnboarding = _observableOneTapOnboarding

    init {
        launch {
            delay(ONE_TAP_ONBOARDING_DELAY)
            _observableOneTapOnboarding.value = Event(Unit)
        }
    }
}