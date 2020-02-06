package ai.advance.liveness.view.viewmodel

import ai.advance.liveness.data.model.response.LivenessData
import ai.advance.liveness.domain.UploadLivenessResultUseCase
import ai.advance.liveness.utils.LivenessConstants
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import javax.inject.Inject

class LivenessDetectionViewModel @Inject constructor(
        private val uploadLivenessResultUseCase: UploadLivenessResultUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    val livenessDataResult = MutableLiveData<LivenessData>() //TODO please check the Result by Coroutine in ProfileInfoViewModel

    interface UploadState {
        fun isSuccess(state: Boolean)
        fun error(errorCode: Int)
    }

    fun uploadImages(ktpPath: String, facePath: String, tkpdProjectId: String, uploadState: UploadState) {
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                val livenessResponseResult = uploadLivenessResultUseCase.uploadImages(ktpPath, facePath, tkpdProjectId)
                uploadState.isSuccess(livenessResponseResult?.isSuccessRegister?: false)
                livenessDataResult.postValue(livenessResponseResult)
            }
        }) {
            Log.e("API ERROR", it.message)
            if(it is SocketTimeoutException){
                uploadState.error(LivenessConstants.FAILED_TIMEOUT)
            } else {
                uploadState.error(LivenessConstants.FAILED_GENERAL)
            }
        }
    }
}