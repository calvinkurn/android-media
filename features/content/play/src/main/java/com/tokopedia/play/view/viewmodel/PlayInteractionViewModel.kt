package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.data.TotalLike
import com.tokopedia.play.domain.GetTotalLikeUseCase
import com.tokopedia.play.domain.PostFollowShopUseCase
import com.tokopedia.play.domain.PostLikeUseCase
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.util.event.Event
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayInteractionViewModel @Inject constructor(
        private val getTotalLikeUseCase: GetTotalLikeUseCase,
        private val postLikeUseCase: PostLikeUseCase,
        private val postFollowShopUseCase: PostFollowShopUseCase,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatcherProvider
) : BaseViewModel(dispatchers.main) {

    private val job = SupervisorJob()

    private val _observableTotalLikes = MutableLiveData<Result<TotalLike>>()
    val observableTotalLikes: LiveData<Result<TotalLike>> = _observableTotalLikes

    private val _observableFollowShop = MutableLiveData<Result<Boolean>>()
    val observableFollowShop: LiveData<Result<Boolean>> = _observableFollowShop

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
                if (event.needLogin && !userSession.isLoggedIn) LoginStateEvent.NeedLoggedIn
                else LoginStateEvent.InteractionAllowed(event)
        )
    }

    fun doLikeUnlike(shouldLike: Boolean) {
        //TODO("Call Like Unlike Use case")
    }

    fun doFollow(shopId: Long, action: PartnerFollowAction) {
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                postFollowShopUseCase.params = PostFollowShopUseCase.createParam(shopId.toString(), action)
                postFollowShopUseCase.executeOnBackground()
            }

            _observableFollowShop.value = Success(response)
        }) {
            _observableFollowShop.value = Fail(it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancelChildren()
    }
}