package com.tokopedia.feedback_form.drawonpicture.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by furqan on 01/10/2020
 */
class DrawOnPictureViewModel @Inject constructor(private val dispatcherProvider: CoroutineDispatchers)
    : BaseViewModel(dispatcherProvider.io) {

    private val mutableShowPencilOptions = MutableLiveData<Boolean>()
    val showPencilOptions: LiveData<Boolean>
        get() = mutableShowPencilOptions

    private var isOnDrawProcess: Boolean = false

    fun startDrawing() {
        isOnDrawProcess = true
        launch(dispatcherProvider.main) {
            delay(1000)
            if (isOnDrawProcess) mutableShowPencilOptions.postValue(false)
        }
    }

    fun stopDrawing() {
        isOnDrawProcess = false
        mutableShowPencilOptions.postValue(true)
    }

}