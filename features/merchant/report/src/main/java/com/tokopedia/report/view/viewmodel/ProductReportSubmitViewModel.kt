package com.tokopedia.report.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.report.data.model.SubmitReportParams
import com.tokopedia.report.usecase.SubmitReportUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import java.io.File
import javax.inject.Inject

class ProductReportSubmitViewModel @Inject constructor(
    private val submitReportUseCase: SubmitReportUseCase,
    private val uploaderUseCase: UploaderUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    companion object {
        private const val SOURCE_ID = "OfQTGl"
        private const val KEY_PHOTO = "photo"
        private const val KEY_UPLOAD_IDS = "upload_ids"
    }

    private val submitResult = MutableLiveData<Result<Boolean>>()
    fun getSubmitResult(): LiveData<Result<Boolean>> = submitResult

    private var currentParams: SubmitReportParams? = null
    fun getCurrentParams() = currentParams

    fun submitReport(productId: Long, categoryId: Int, input: Map<String, Any>) {

        launchCatchError(block = {
            val params = SubmitReportParams(productId, categoryId, processImages(input))
            submitReportUseCase.setParams(params)
            currentParams = params

            val result = submitReportUseCase.executeOnBackground()
            submitResult.postValue(Success(result.submitReport.isSuccess))
        }, onError = {
            submitResult.postValue(Fail(it))
        })
    }

    private suspend fun processImages(input:Map<String, Any>): Map<String, Any>{
        val mutableInput = input.toMutableMap()
        val uploadIdList = (mutableInput[KEY_PHOTO] as? List<*>)?.map { photo ->
            uploadImageAndGetId(photo as String).let {
                when(it){
                    is UploadResult.Success -> it.uploadId
                    is UploadResult.Error -> throw Throwable(it.message)
                }
            }
        } ?: emptyList()
        mutableInput[KEY_UPLOAD_IDS] = uploadIdList
        mutableInput.remove(KEY_PHOTO)
        return mutableInput
    }

    private suspend fun uploadImageAndGetId(imagePath: String): UploadResult {
        val filePath = File(imagePath)
        val params = uploaderUseCase.createParams(
            sourceId = SOURCE_ID,
            filePath = filePath
        )
        return uploaderUseCase(params)
    }
}