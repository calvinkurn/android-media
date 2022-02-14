package com.tokopedia.videoTabComponent.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videoTabComponent.domain.model.data.ContentSlotResponse
import com.tokopedia.videoTabComponent.domain.usecase.GetPlayContentUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayFeedVideoTabViewModel@Inject constructor(
        private val baseDispatcher: CoroutineDispatchers,
        private val userSession: UserSessionInterface,
        private val getPlayContentUseCase: GetPlayContentUseCase
): BaseViewModel(baseDispatcher.main){

    private var currentCursor = ""
    val getPlayDataRsp = MutableLiveData<Result<ContentSlotResponse>>()


    fun getInitialPlayData(){
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                getPlayDataResult()
            }
            getPlayDataRsp.value = Success(results)

        }) {
            getPlayDataRsp.value = Fail(it)
        }
    }
    fun getPlayData(){
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                getPlayDataResult()
            }
            getPlayDataRsp.value = Success(results)

        }) {
            getPlayDataRsp.value = Fail(it)
        }
    }


    private suspend fun getPlayDataResult(): ContentSlotResponse {
        try {
            return getPlayContentUseCase.execute(cursor = currentCursor)
        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
    }

}