package com.tokopedia.picker.ui.fragment.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.picker.data.entity.Album
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.ui.PickerParam
import com.tokopedia.picker.data.repository.FileLoaderRepository
import javax.inject.Inject

class MediaViewModel @Inject constructor(
    private val repository: FileLoaderRepository
) : ViewModel() {

    private var _files = MutableLiveData<List<Media>>()
    val files: LiveData<List<Media>> get() = _files

    private var _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    fun fetch(config: PickerParam) {
        repository.abort()
        repository.loadFiles(config, object : FileLoaderRepository.LoaderListener {
            override fun onFileLoaded(files: List<Media>, dirs: List<Album>) {
                _files.postValue(files)
            }

            override fun onFailed(throwable: Throwable) {
                _error.postValue(throwable)
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        repository.abort()
    }

}