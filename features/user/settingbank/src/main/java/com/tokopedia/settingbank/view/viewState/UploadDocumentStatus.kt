package com.tokopedia.settingbank.view.viewState

sealed class UploadDocumentStatus
object DocumentUploadStarted : UploadDocumentStatus()
object DocumentUploadEnd : UploadDocumentStatus()
data class DocumentUploadError(val throwable: Throwable) : UploadDocumentStatus()
data class DocumentUploaded(val message : String?) : UploadDocumentStatus()