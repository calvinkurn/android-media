package com.tokopedia.developer_options.drawonpicture.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.developer_options.drawonpicture.DispatcherProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by furqan on 01/10/2020
 */
class DrawOnPictureViewModel @Inject constructor(private val dispatcherProvider: DispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {

    private val mutableShowPencilOptions = MutableLiveData<Boolean>()
    val showPencilOptions: LiveData<Boolean>
        get() = mutableShowPencilOptions

    private var isOnDrawProcess: Boolean = false

    fun startDrawing() {
        isOnDrawProcess = true
        launch(dispatcherProvider.ui()) {
            delay(1000)
            if (isOnDrawProcess) mutableShowPencilOptions.postValue(false)
        }
    }

    fun stopDrawing() {
        isOnDrawProcess = false
        mutableShowPencilOptions.postValue(true)
    }

}