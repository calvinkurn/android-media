package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.data.TotalLike
import com.tokopedia.play.domain.GetTotalLikeUseCase
import com.tokopedia.play.domain.PostFollowShopUseCase
import com.tokopedia.play.domain.PostLikeUseCase
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.util.event.Event
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayInteractionViewModel @Inject constructor(
        private val getTotalLikeUseCase: GetTotalLikeUseCase,
        private val postLikeUseCase: PostLikeUseCase,
        private val postFollowShopUseCase: PostFollowShopUseCase,
        private val userSession: UserSessionInterface,
        dispatchers: CoroutineDispatcherProvider
) : BaseViewModel(dispatchers.main) {

    private val job = SupervisorJob()

    private val _observableTotalLikes = MutableLiveData<Result<TotalLike>>()
    val observableTotalLikes: LiveData<Result<TotalLike>> = _observableTotalLikes

    private val _observableLoggedInInteractionEvent = MutableLiveData<Event<LoginStateEvent>>()
    val observableLoggedInInteractionEvent: LiveData<Event<LoginStateEvent>> = _observableLoggedInInteractionEvent

    fun getTotalLikes(channelId: String) {
        launchCatchError(block = {
            val response = withContext(Dispatchers.IO) {
                getTotalLikeUseCase.channelId = channelId
                getTotalLikeUseCase.executeOnBackground()
            }
            _observableTotalLikes.value = Success(response)
        }) {
            _observableTotalLikes.value = Fail(it)
        }
    }

    fun doInteractionEvent(event: InteractionEvent) {
        _observableLoggedInInteractionEvent.value = Event(
                if (event in InteractionEvent.needLoginEvents &&
                        !userSession.isLoggedIn) LoginStateEvent.NeedLoggedIn
                else LoginStateEvent.InteractionAllowed(event)
        )
    }

    override fun onCleared() {
        super.onCleared()
        job.cancelChildren()
    }
}