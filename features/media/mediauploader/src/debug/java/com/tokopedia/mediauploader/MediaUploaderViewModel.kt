package com.tokopedia.mediauploader

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MediaUploaderViewModel : ViewModel() {

    private val _uploading = MutableLiveData<UploadState>()
    val uploading: LiveData<UploadState> get() = _uploading

    fun setUploadingStatus(uploading: UploadState) {
        _uploading.value = uploading
    }

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