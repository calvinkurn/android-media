package com.tokopedia.picker.ui.activity.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.picker.data.entity.Album
import com.tokopedia.picker.data.repository.AlbumRepository
import com.tokopedia.picker.ui.PickerParam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlbumViewModel @Inject constructor(
    private val album: AlbumRepository
) : ViewModel() {

    private var _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> get() = _albums

    fun fetch(param: PickerParam) {
        viewModelScope.launch {
            val albums = album(param)

            withContext(Dispatchers.Main) {
                _albums.value = albums
            }
        }
    }

}