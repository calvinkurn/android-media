package com.tokopedia.report.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.report.data.model.SubmitReportParams
import com.tokopedia.report.data.model.SubmitReportResult
import com.tokopedia.report.usecase.SubmitReportUseCase
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

    private val submitResult = MutableLiveData<SubmitReportResult>()
    fun getSubmitResult(): LiveData<SubmitReportResult> = submitResult

    fun submitReport(productId: Long, categoryId: Int, input: Map<String, Any>) {
        val mutableInput = input.toMutableMap()

        launchCatchError(block = {
            val uploadIdList = (mutableInput[KEY_PHOTO] as? List<*>)?.map { photo ->
                uploadImageAndGetId(photo as String).also {
                    if (it.isBlank()) throw Throwable()
                }
            } ?: emptyList()
            mutableInput[KEY_UPLOAD_IDS] = uploadIdList
            mutableInput.remove(KEY_PHOTO)

            val params = SubmitReportParams(productId, categoryId, mutableInput)
            submitReportUseCase.setParams(params)

            val result = submitReportUseCase.executeOnBackground()
            result.submitReport.isSuccess
            submitResult.postValue(SubmitReportResult.Success(result.submitReport.isSuccess))
        }, onError = {
            submitResult.postValue(SubmitReportResult.Fail(it))
        })

    }

    private suspend fun uploadImageAndGetId(imagePath: String): String {
        val filePath = File(imagePath)
        val params = uploaderUseCase.createParams(
            sourceId = SOURCE_ID,
            filePath = filePath
        )
        return when (val result = uploaderUseCase(params)) {
            is UploadResult.Success -> result.uploadId
            is UploadResult.Error -> ""
        }
    }
}