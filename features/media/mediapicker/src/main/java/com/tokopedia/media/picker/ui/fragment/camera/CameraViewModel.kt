package com.tokopedia.media.picker.ui.fragment.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.otaliastudios.cameraview.size.Size
import com.tokopedia.media.picker.data.repository.CreateMediaRepository
import kotlinx.coroutines.flow.*
import java.io.File
import javax.inject.Inject

class CameraViewModel @Inject constructor(
    private val createMediaRepository: CreateMediaRepository
): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _pictureTaken = MutableSharedFlow<File?>()
    val pictureTaken: Flow<File?> get() = _pictureTaken

    private val _videoTaken = MutableSharedFlow<File>()
    val videoTaken: Flow<File> get() = _videoTaken

    fun onPictureTaken(cameraSize: Size?, data: ByteArray) {
        createMediaRepository.image(cameraSize, data)
            .onStart { _isLoading.value = true }
            .onCompletion { _isLoading.value = false }
            .distinctUntilChanged()
            .map {
                _pictureTaken.tryEmit(it)
            }
    }

    fun onVideoTaken() {
        _videoTaken.tryEmit(createMediaRepository.video())
    }

}