package com.tokopedia.shop_nib.presentation.submission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import java.util.*

import javax.inject.Inject

class NibSubmissionViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private var fileUri : String = ""
    private var nibPublishDate : Date? = null
    private val _validInput = MutableLiveData<Boolean>()
    val validInput: LiveData<Boolean>
        get() = _validInput

    fun onSelectNibPublishDate(nibPublishDate: Date) {
        this.nibPublishDate = nibPublishDate
        validateInput()
    }
    fun onFileSelected(fileUri: String) {
        this.fileUri = fileUri
        validateInput()
    }

    fun onFileRemoved() {
        this.fileUri = ""
        validateInput()
    }

    fun onTapSubmitButton() {

    }

    private fun validateInput() {
        if (fileUri.isEmpty()) {
            _validInput.value = false
            return
        }

        if (nibPublishDate == null) {
            _validInput.value = false
            return
        }

        _validInput.value = true
    }

    fun getNibPublishDate(): Date? {
        return nibPublishDate
    }
}
