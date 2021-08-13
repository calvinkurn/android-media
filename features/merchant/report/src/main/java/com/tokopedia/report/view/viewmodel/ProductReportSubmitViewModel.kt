package com.tokopedia.report.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.mediauploader.domain.UploaderUseCase
import com.tokopedia.report.domain.interactor.SubmitReportUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.cancel
import rx.Subscriber
import java.io.File
import javax.inject.Inject

class ProductReportSubmitViewModel @Inject constructor(private val useCase: SubmitReportUseCase,
                                                       private val uploaderUseCase: UploaderUseCase,
                                                       dispatcher: CoroutineDispatchers): BaseViewModel(dispatcher.io){

    companion object {
        private const val SOURCE_ID = "OfQTGl"
        private const val KEY_PHOTO = "photo"
        private const val KEY_UPLOAD_IDS = "upload_ids"
    }

    fun submitReport(productId: Int, categoryId: Int, input: Map<String, Any>,
                     onSuccess: (Boolean) -> Unit, onFail: (Throwable?) -> Unit){
        val uploadIdList: ArrayList<String> = ArrayList()
        launchCatchError(block = {
            val photos = input[KEY_PHOTO] as? List<String> ?: listOf()
            repeat(photos.size) {
                val imageId = uploadImageAndGetId(photos[it])
                if (imageId.isEmpty()) {
                    onFail.invoke(Throwable())
                    this@launchCatchError.cancel()
                }
                uploadIdList.add(imageId)
            }
            (input as MutableMap<String, Any>).apply {
                put(KEY_UPLOAD_IDS, uploadIdList)
                remove(KEY_PHOTO)
                useCase.execute(SubmitReportUseCase.createRequestParam(categoryId, productId, this), object : Subscriber<Boolean>() {
                    override fun onNext(t: Boolean) {
                        onSuccess.invoke(t)
                    }

                    override fun onCompleted() {}

                    override fun onError(e: Throwable?) {
                        onFail.invoke(e)
                        e?.printStackTrace()
                    }

                })
            }
        }) {
            onFail.invoke(it)
        }
    }

    private suspend fun uploadImageAndGetId(imagePath: String): String {
        val filePath = File(imagePath)
        val params = uploaderUseCase.createParams(
                sourceId = SOURCE_ID,
                filePath = filePath
        )
        return when (val result = uploaderUseCase(params)) {
            is UploadResult.Success -> {
                result.uploadId
            }
            is UploadResult.Error -> {
                ""
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        useCase.unsubscribe()
    }
}