package com.tokopedia.shop_nib.presentation.submission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop_nib.domain.entity.UploadFileResult
import com.tokopedia.shop_nib.domain.usecase.UploadFileUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import java.util.*
import com.tokopedia.usecase.launch_cache_error.launchCatchError

import javax.inject.Inject

class NibSubmissionViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val uploadFileUseCase: UploadFileUseCase
) : BaseViewModel(dispatchers.main) {

    private var fileUri : String = ""
    private var nibPublishDate : Date? = null

    private val _validInput = MutableLiveData<Boolean>()
    val validInput: LiveData<Boolean>
        get() = _validInput

    private val _fileUpload = MutableLiveData<Result<UploadFileResult>>()
    val fileUpload: LiveData<Result<UploadFileResult>>
        get() = _fileUpload

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

    fun onSelectInvalidFile() {
        this.fileUri = ""
        validateInput()
    }

    fun onTapSubmitButton() {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = uploadFileUseCase.execute(fileUri)
                _fileUpload.postValue(Success(result))
            },
            onError = { error ->
                _fileUpload.postValue(Fail(error))
            }
        )
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
