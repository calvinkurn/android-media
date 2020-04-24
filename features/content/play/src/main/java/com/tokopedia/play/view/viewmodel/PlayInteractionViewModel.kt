package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.domain.PostFollowPartnerUseCase
import com.tokopedia.play.domain.PostLikeUseCase
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
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
        private val postLikeUseCase: PostLikeUseCase,
        private val postFollowPartnerUseCase: PostFollowPartnerUseCase,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatcherProvider
) : PlayBaseViewModel(dispatchers.main) {

    private val _observableFollowPartner = MutableLiveData<Result<Boolean>>()
    val observableFollowPartner: LiveData<Result<Boolean>> = _observableFollowPartner

    private val _observableLoggedInInteractionEvent = MutableLiveData<Event<LoginStateEvent>>()
    val observableLoggedInInteractionEvent: LiveData<Event<LoginStateEvent>> = _observableLoggedInInteractionEvent

    fun doInteractionEvent(event: InteractionEvent) {
        _observableLoggedInInteractionEvent.value = Event(
                if (event.needLogin && !userSession.isLoggedIn) LoginStateEvent.NeedLoggedIn(event)
                else LoginStateEvent.InteractionAllowed(event)
        )
    }

    fun doLikeUnlike(contentId: Int, contentType: Int, likeType: Int, shouldLike: Boolean) {
        scope.launchCatchError(block = {
            withContext(dispatchers.io) {
                postLikeUseCase.params = PostLikeUseCase.createParam(contentId, contentType, likeType, shouldLike)
                postLikeUseCase.executeOnBackground()
            }
        }) {}
    }

    fun doFollow(shopId: Long, action: PartnerFollowAction) {
        scope.launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                postFollowPartnerUseCase.params = PostFollowPartnerUseCase.createParam(shopId.toString(), action)
                postFollowPartnerUseCase.executeOnBackground()
            }

            _observableFollowPartner.value = Success(response)
        }) {
            if (it !is CancellationException) _observableFollowPartner.value = Fail(it)
        }
    }
}