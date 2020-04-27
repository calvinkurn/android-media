package com.tokopedia.settingbank.banklist.v2.view.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.settingbank.banklist.v2.domain.UploadDocumentPojo
import com.tokopedia.settingbank.banklist.v2.domain.usecase.UploadDocumentUseCase
import com.tokopedia.settingbank.banklist.v2.view.viewState.*
import rx.Subscriber
import javax.inject.Inject

class UploadDocumentViewModel @Inject constructor(
        private val uploadDocumentUseCase: UploadDocumentUseCase) : ViewModel() {

    val uploadDocumentStatus = MutableLiveData<UploadDocumentStatus>()

    fun uploadDocument(uploadDocumentPojo: UploadDocumentPojo) {
        if(uploadDocumentStatus.value ==  DocumentUploadStarted)
            return
        uploadDocumentStatus.value =  DocumentUploadStarted
        uploadDocumentUseCase.execute(UploadDocumentUseCase.getParam(uploadDocumentPojo),
                object : Subscriber<String>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        uploadDocumentStatus.value =  DocumentUploadEnd
                        uploadDocumentStatus.value = DocumentUploadError(e)

                    }

                    override fun onNext(message: String) {
                        uploadDocumentStatus.value =  DocumentUploadEnd
                        uploadDocumentStatus.value =  DocumentUploaded(message)
                    }
                })
    }
}