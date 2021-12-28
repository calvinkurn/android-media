package com.tokopedia.picker.data.repository

import com.tokopedia.picker.data.entity.Album
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.ui.PickerParam

interface FileLoaderRepository {
    fun loadFiles(config: PickerParam, listener: LoaderListener)
    fun abort()

    interface LoaderListener {
        fun onFileLoaded(files: List<Media>, dirs: List<Album>)
        fun onFailed(throwable: Throwable)
    }
}