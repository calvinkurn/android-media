package com.tokopedia.mediauploader

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.WorkInfo
import com.tokopedia.mediauploader.services.UploaderWorker

class MediaUploaderViewModel : ViewModel() {

    private val _uploading = MutableLiveData<UploadState>()
    val uploading: LiveData<UploadState> get() = _uploading

    private val _uploadFile = MutableLiveData<List<WorkInfo>>()
    val uploadFile: LiveData<List<WorkInfo>> get() = _uploadFile

    fun setUploadingStatus(uploading: UploadState) {
        _uploading.value = uploading
    }

    fun upload(
        context: Context,
        sourceId: String,
        filePath: String,
        isSupportTranscode: Boolean
    ) = UploaderWorker.createChainedWorkRequests(
        context = context,
        sourceId = sourceId,
        filePath = filePath,
        isSupportTranscode = isSupportTranscode
    )

    companion object {
        sealed class UploadState {
            object Idle: UploadState()
            object Uploading: UploadState()
            object Stopped: UploadState()
            object Aborted: UploadState()
            object Finished: UploadState()
        }
    }

}