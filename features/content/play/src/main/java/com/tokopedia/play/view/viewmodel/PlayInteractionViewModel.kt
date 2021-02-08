package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.play.domain.PostFollowPartnerUseCase
import com.tokopedia.play.domain.PostLikeUseCase
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.view.uimodel.recom.PlayLikeParamInfoUiModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayInteractionViewModel @Inject constructor(
        private val postLikeUseCase: PostLikeUseCase,
        private val postFollowPartnerUseCase: PostFollowPartnerUseCase,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatcherProvider,
        private val playPreference: PlayPreference,
) : PlayBaseViewModel(dispatchers.main) {

    private val _observableFollowPartner = MutableLiveData<Result<Boolean>>()
    val observableFollowPartner: LiveData<Result<Boolean>> = _observableFollowPartner

    private val _observableLoggedInInteractionEvent = MutableLiveData<Event<LoginStateEvent>>()
    val observableLoggedInInteractionEvent: LiveData<Event<LoginStateEvent>> = _observableLoggedInInteractionEvent

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

    fun doInteractionEvent(event: InteractionEvent) {
        _observableLoggedInInteractionEvent.value = Event(
                if (event.needLogin && !userSession.isLoggedIn) LoginStateEvent.NeedLoggedIn(event)
                else LoginStateEvent.InteractionAllowed(event)
        )
    }

    fun doLikeUnlike(likeParamInfo: PlayLikeParamInfoUiModel, shouldLike: Boolean) {
        scope.launchCatchError(block = {
            withContext(dispatchers.io) {
                postLikeUseCase.params = PostLikeUseCase.createParam(
                        contentId = likeParamInfo.contentId.toIntOrZero(),
                        contentType = likeParamInfo.contentType.orZero(),
                        likeType = likeParamInfo.likeType.orZero(),
                        action = shouldLike
                )
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

    companion object {

        private const val ONBOARDING_DELAY = 5000L
    }
}