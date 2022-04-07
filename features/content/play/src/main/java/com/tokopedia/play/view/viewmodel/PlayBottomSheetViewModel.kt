package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.data.CartFeedbackResponseModel
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.CartFeedbackUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.PlayUserReportUiModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by mzennis on 2020-03-06.
 */
class PlayBottomSheetViewModel @Inject constructor(
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
    private val repo: PlayViewerRepository,
) : ViewModel() {

    private val _observableUserReportReasoning = MutableLiveData<PlayResult<PlayUserReportUiModel.Loaded>>()
    private val _observableUserReportSubmission = MutableLiveData<PlayResult<Event<Unit>>>()

    private val _observableLoggedInInteractionEvent = MutableLiveData<Event<LoginStateEvent>>()
    val observableLoggedInInteractionEvent: LiveData<Event<LoginStateEvent>> = _observableLoggedInInteractionEvent

    val observableUserReportReasoning : LiveData<PlayResult<PlayUserReportUiModel.Loaded>> = _observableUserReportReasoning
    val observableUserReportSubmission : LiveData<PlayResult<Event<Unit>>> = _observableUserReportSubmission

    fun doInteractionEvent(event: InteractionEvent) {
        _observableLoggedInInteractionEvent.value = Event(
                if (event.needLogin && !userSession.isLoggedIn) LoginStateEvent.NeedLoggedIn(event)
                else LoginStateEvent.InteractionAllowed(event)
        )
    }

    fun getUserReportList(){
        _observableUserReportReasoning.value = PlayResult.Loading(true)

        viewModelScope.launchCatchError(block = {
            val userReportUiModel = withContext(dispatchers.io){
                repo.getReasoningList()
            }
            val data = PlayUserReportUiModel.Loaded(
                reasoningList = userReportUiModel
            )
            _observableUserReportReasoning.value = PlayResult.Success(data = data)

        }){
            _observableUserReportReasoning.value = PlayResult.Failure(it){
                getUserReportList()
            }
        }
    }

    fun submitUserReport(channelId: Long,
               mediaUrl: String,
               shopId: Long,
               reasonId: Int,
               timestamp: Long,
               reportDesc: String){
        viewModelScope.launchCatchError(block = {
           val isSuccess = withContext(dispatchers.io) {
                repo.submitReport(
                    channelId = channelId,
                    mediaUrl = mediaUrl,
                    shopId = shopId,
                    reasonId = reasonId,
                    timestamp = timestamp,
                    reportDesc = reportDesc
                )
           }
            if(isSuccess){
                _observableUserReportSubmission.value = PlayResult.Success(Event(Unit))
            }else{
                throw Throwable()
            }
        }){
            _observableUserReportSubmission.value = PlayResult.Failure(it)
        }

    }

    fun onFreezeBan() {
        viewModelScope.coroutineContext.cancelChildren()
    }

    private fun mappingResponseCart(response: CartFeedbackResponseModel,
                                    product: PlayProductUiModel.Product,
                                    action: ProductAction,
                                    bottomInsetsType: BottomInsetsType) =
            CartFeedbackUiModel(
                    isSuccess = response.isSuccess,
                    errorMessage = response.errorMessage,
                    cartId = response.cartId,
                    product = product,
                    action = action,
                    bottomInsetsType = bottomInsetsType
            )
}