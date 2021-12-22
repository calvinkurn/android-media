package com.tokopedia.picker.ui.activity.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.picker.data.entity.Directory
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.data.repository.FileLoaderRepository
import com.tokopedia.picker.ui.PickerParam
import javax.inject.Inject

class AlbumViewModel @Inject constructor(
    private val repository: FileLoaderRepository
) : ViewModel() {

    private var _directories = MutableLiveData<List<Directory>>()
    val directories: LiveData<List<Directory>> get() = _directories

    private var _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    fun fetch(config: PickerParam) {
        repository.abort()
        repository.loadFiles(config, object : FileLoaderRepository.LoaderListener {
            override fun onFileLoaded(files: List<Media>, dirs: List<Directory>) {
                _directories.postValue(dirs)
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