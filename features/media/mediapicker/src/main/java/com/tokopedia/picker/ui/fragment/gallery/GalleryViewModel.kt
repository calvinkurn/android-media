package com.tokopedia.picker.ui.fragment.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.picker.data.entity.Directory
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.data.param.ConfigLoaderParam
import com.tokopedia.picker.data.repository.FileLoaderRepository
import javax.inject.Inject

class GalleryViewModel @Inject constructor(
    private val repository: FileLoaderRepository
) : ViewModel() {

    private var _files = MutableLiveData<List<Media>>()
    val files: LiveData<List<Media>> get() = _files

    private var _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    init {
        load()
    }

    fun load() {
        repository.abort()
        repository.loadFiles(ConfigLoaderParam(), object : FileLoaderRepository.LoaderListener {
            override fun onFileLoaded(files: List<Media>, dirs: List<Directory>) {
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