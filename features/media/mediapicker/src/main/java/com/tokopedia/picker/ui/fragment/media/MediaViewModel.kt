package com.tokopedia.picker.ui.fragment.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.data.repository.MediaRepository
import com.tokopedia.picker.ui.PickerParam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaViewModel @Inject constructor(
    private val repository: MediaRepository,
) : ViewModel() {

    private var _files = MutableLiveData<List<Media>>()
    val files: LiveData<List<Media>> get() = _files

    private var _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    fun fetch(bucketId: Long, param: PickerParam) {
        viewModelScope.launch {
            val result = repository(bucketId, param)

            withContext(Dispatchers.Main) {
                _files.value = result
            }
        }
    }

}