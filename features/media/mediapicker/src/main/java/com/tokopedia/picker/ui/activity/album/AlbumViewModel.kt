package com.tokopedia.picker.ui.activity.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.picker.data.entity.Album
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.data.repository.FileLoaderRepository
import com.tokopedia.picker.ui.PickerParam
import javax.inject.Inject

class AlbumViewModel @Inject constructor(
    private val repository: FileLoaderRepository
) : ViewModel() {

    private var _result = MutableLiveData<Pair<List<Media>, List<Album>>>()
    val result: LiveData<Pair<List<Media>, List<Album>>> get() = _result

    private var _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    fun fetch(config: PickerParam) {
        repository.abort()
        repository.loadFiles(config, object : FileLoaderRepository.LoaderListener {
            override fun onFileLoaded(files: List<Media>, dirs: List<Album>) {
                _result.postValue(Pair(files, dirs))
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