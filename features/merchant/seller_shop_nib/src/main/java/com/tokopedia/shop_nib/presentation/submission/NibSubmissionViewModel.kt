package com.tokopedia.shop_nib.presentation.submission

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop_nib.domain.usecase.UploadFileUseCase
import com.tokopedia.shop_nib.presentation.submission.uimodel.UiEffect
import com.tokopedia.shop_nib.presentation.submission.uimodel.UiEvent
import com.tokopedia.shop_nib.presentation.submission.uimodel.UiState
import com.tokopedia.shop_nib.util.tracker.NibSubmissionPageTracker
import java.util.*
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import javax.inject.Inject

class NibSubmissionViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val uploadFileUseCase: UploadFileUseCase,
    private val tracker: NibSubmissionPageTracker
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024 //5 MB
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>(replay = 1)
    val uiEffect = _uiEffect.asSharedFlow()

    private val currentState: UiState
        get() = _uiState.value


    fun processEvent(event: UiEvent) {
        when(event) {
            is UiEvent.TapSelectFile -> _uiEffect.tryEmit(UiEffect.ShowFilePicker)
            is UiEvent.ConfirmFile -> handleConfirmFile(event.fileUri, event.fileExtension, event.fileSizeKb)
            UiEvent.UnselectFile -> handleUnselectFile()
            is UiEvent.TapChangeDate -> _uiEffect.tryEmit(UiEffect.ShowDatePicker(currentState.selectedDate))
            is UiEvent.ConfirmDate -> handleConfirmDate(event.newDate)
            UiEvent.SubmitFile -> handleSubmitFile()
            UiEvent.RecordImpression -> handleRecordImpression()
        }
    }

    private fun handleConfirmFile(fileUri: String, fileExtension: String, fileSizeKb: Long) {
        _uiState.update {
            val fileState = validateFileInput(fileUri, fileExtension, fileSizeKb)
            val isInputValid = validateInput(currentState.selectedDate, fileState)

            it.copy(
                selectedFileUri = fileUri,
                selectedFileExtension = fileExtension,
                selectedFileSizeKb = fileSizeKb,
                fileState = fileState,
                isInputValid = isInputValid
            )
        }
    }

    private fun handleConfirmDate(newDate: Date) {
        val isInputValid = validateInput(newDate, currentState.fileState)
        _uiState.update { it.copy(selectedDate = newDate, isInputValid = isInputValid) }
    }

    private fun handleUnselectFile() {
        _uiState.update {
            it.copy(
                selectedFileUri = "",
                selectedFileExtension = "",
                selectedFileSizeKb = 0,
                fileState = UiState.FileState.NotSelected,
                isInputValid = false
            )
        }
    }

    private fun handleSubmitFile() {
        tracker.sendClickSubmitNibButtonEvent()

        _uiState.update { it.copy(isLoading = true) }

        val fileUri = currentState.selectedFileUri
        val nibPublishDate = currentState.selectedDate

        launchCatchError(
            dispatchers.io,
            block = {
                val result = uploadFileUseCase.execute(fileUri)

                _uiState.update { it.copy(isLoading = false, error = null) }
                if (result.isSuccess) {
                    _uiEffect.tryEmit(UiEffect.RedirectToSubmissionSuccess)
                } else {
                    _uiEffect.tryEmit(UiEffect.ShowUploadError(result.errorMessage))
                }

            },
            onError = { error ->
                _uiState.update { it.copy(isLoading = false, error = error) }
                _uiEffect.tryEmit(UiEffect.ShowError(error))
            }
        )
    }

    private fun handleRecordImpression() {
        tracker.sendPageImpression()
    }


    private fun validateInput(selectedDate: Date?, fileState: UiState.FileState): Boolean {
        return selectedDate != null && fileState is UiState.FileState.Valid
    }

    private fun validateFileInput(fileUri: String, fileExtension: String, fileSizeKb: Long): UiState.FileState {
        if (!isFileSelected(fileUri)) {
            return UiState.FileState.NotSelected
        }

        if (!isValidFileExtension(fileExtension)) {
            return UiState.FileState.InvalidFileExtension
        }

        if (!isValidFileSize(fileSizeKb)) {
            return UiState.FileState.ExceedMaxFileSize
        }

        return UiState.FileState.Valid(fileUri, fileSizeKb)
    }

    private fun isFileSelected(fileUri: String): Boolean {
        return fileUri.isNotEmpty()
    }

    private fun isValidFileExtension(fileExtension: String): Boolean {
        val allowedFileExtensions = listOf("png", "jpeg", "jpg", "pdf")
        return fileExtension in allowedFileExtensions
    }

    private fun isValidFileSize(fileSizeKb: Long): Boolean {
        return fileSizeKb <= MAX_FILE_SIZE_BYTES
    }
}
