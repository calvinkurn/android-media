package com.tokopedia.feedback_form.drawonpicture.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * @author by furqan on 01/10/2020
 */
class DrawOnPictureViewModel @Inject constructor(private val dispatcherProvider: CoroutineDispatchers)
    : BaseViewModel(dispatcherProvider.main) {

    private val mutableShowPencilOptions = MutableLiveData<Boolean>()
    val showPencilOptions: LiveData<Boolean>
        get() = mutableShowPencilOptions

    private var isOnDrawProcess: Boolean = false

    fun startDrawing() {
        isOnDrawProcess = true
        launchCatchError(dispatcherProvider.io, {
            delay(1000)
            if (isOnDrawProcess) mutableShowPencilOptions.postValue(false)
        }) {
            it.printStackTrace()
        }
    }

    fun stopDrawing() {
        isOnDrawProcess = false
        mutableShowPencilOptions.postValue(true)
    }

}