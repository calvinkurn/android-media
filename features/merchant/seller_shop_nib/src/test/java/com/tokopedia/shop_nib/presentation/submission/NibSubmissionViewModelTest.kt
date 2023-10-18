package com.tokopedia.shop_nib.presentation.submission

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop_nib.domain.entity.UploadFileResult
import com.tokopedia.shop_nib.domain.usecase.UploadFileUseCase
import com.tokopedia.shop_nib.presentation.submission.uimodel.UiEffect
import com.tokopedia.shop_nib.presentation.submission.uimodel.UiEvent
import com.tokopedia.shop_nib.presentation.submission.uimodel.UiState
import com.tokopedia.shop_nib.util.tracker.NibSubmissionPageTracker
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class NibSubmissionViewModelTest {

    @RelaxedMockK
    lateinit var  uploadFileUseCase: UploadFileUseCase

    @RelaxedMockK
    lateinit var tracker: NibSubmissionPageTracker

    private lateinit var viewModel: NibSubmissionViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel =  NibSubmissionViewModel(
            CoroutineTestDispatchersProvider,
            uploadFileUseCase,
            tracker
        )
    }

    //region TapSelectFile
    @Test
    fun `When receive TapSelectFile event, should emit ShowFilePicker effect`() = runBlockingTest {
        //Given
        val expectedEffect = UiEffect.ShowFilePicker

        val emittedEffects = arrayListOf<UiEffect>()
        val job = launch {
            viewModel.uiEffect.toList(emittedEffects)
        }

        //When
        viewModel.processEvent(UiEvent.TapSelectFile)

        //Then
        val actualEffect = emittedEffects.last()
        assertEquals(expectedEffect, actualEffect)

        job.cancel()
    }
    //endregion

    //region ConfirmFile
    @Test
    fun `When confirm file, if file uri is empty, file state should be NotSelected`() = runBlockingTest {
        val fileUri = ""
        val fileExtension = ""
        val fileSizeBytes : Long = 0
        val fileState = UiState.FileState.NotSelected

        val emittedValues = arrayListOf<UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(UiEvent.ConfirmFile(fileUri, fileExtension, fileSizeBytes))

        val actual = emittedValues.last()

        assertEquals(fileUri, actual.selectedFileUri)
        assertEquals(fileExtension, actual.selectedFileExtension)
        assertEquals(fileSizeBytes, actual.selectedFileSizeBytes)
        assertEquals(fileState, actual.fileState)
        assertEquals(false, actual.isInputValid)

        job.cancel()
    }

    @Test
    fun `When confirm file, if file extension is png, file state should be Valid`() = runBlockingTest {
        val fileUri = "file://image.png"
        val fileExtension = "png"
        val fileSizeBytes : Long = 500
        val fileState = UiState.FileState.Valid(fileUri, fileSizeBytes)

        val emittedValues = arrayListOf<UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(UiEvent.ConfirmFile(fileUri, fileExtension, fileSizeBytes))

        val actual = emittedValues.last()

        assertEquals(fileUri, actual.selectedFileUri)
        assertEquals(fileExtension, actual.selectedFileExtension)
        assertEquals(fileSizeBytes, actual.selectedFileSizeBytes)
        assertEquals(fileState, actual.fileState)
        assertEquals(false, actual.isInputValid)

        job.cancel()
    }

    @Test
    fun `When confirm file, if file extension is not in allowed extension, file state should be InvalidFileExtension`() = runBlockingTest {
        val fileUri = "file://image.gif"
        val fileExtension = "gif"
        val fileSizeBytes : Long = 500
        val fileState = UiState.FileState.InvalidFileExtension

        val emittedValues = arrayListOf<UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(UiEvent.ConfirmFile(fileUri, fileExtension, fileSizeBytes))

        val actual = emittedValues.last()

        assertEquals(fileUri, actual.selectedFileUri)
        assertEquals(fileExtension, actual.selectedFileExtension)
        assertEquals(fileSizeBytes, actual.selectedFileSizeBytes)
        assertEquals(fileState, actual.fileState)
        assertEquals(false, actual.isInputValid)

        job.cancel()
    }

    @Test
    fun `When confirm file, if file size above maximum, file state should be ExceedMaxFileSize`() = runBlockingTest {
        val fileUri = "file://image.png"
        val fileExtension = "png"
        val fileSizeBytes : Long = 5_242_900 //Max is 5_242_880
        val fileState = UiState.FileState.ExceedMaxFileSize

        val emittedValues = arrayListOf<UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(UiEvent.ConfirmFile(fileUri, fileExtension, fileSizeBytes))

        val actual = emittedValues.last()

        assertEquals(fileUri, actual.selectedFileUri)
        assertEquals(fileExtension, actual.selectedFileExtension)
        assertEquals(fileSizeBytes, actual.selectedFileSizeBytes)
        assertEquals(fileState, actual.fileState)
        assertEquals(false, actual.isInputValid)

        job.cancel()
    }

    @Test
    fun `When confirm file, if file size equals to maximum, file state should be Valid`() = runBlockingTest {
        val fileUri = "file://image.png"
        val fileExtension = "png"
        val fileSizeBytes : Long = 5_242_880 //Max is 5_242_880
        val fileState = UiState.FileState.Valid(fileUri, fileSizeBytes)

        val emittedValues = arrayListOf<UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(UiEvent.ConfirmFile(fileUri, fileExtension, fileSizeBytes))

        val actual = emittedValues.last()

        assertEquals(fileUri, actual.selectedFileUri)
        assertEquals(fileExtension, actual.selectedFileExtension)
        assertEquals(fileSizeBytes, actual.selectedFileSizeBytes)
        assertEquals(fileState, actual.fileState)
        assertEquals(false, actual.isInputValid)

        job.cancel()
    }

    @Test
    fun `When confirm file, if file size less than maximum file size, file state should be Valid`() = runBlockingTest {
        val fileUri = "file://image.png"
        val fileExtension = "png"
        val fileSizeBytes : Long = 5_242_879 //Max is 5_242_880
        val fileState = UiState.FileState.Valid(fileUri, fileSizeBytes)

        val emittedValues = arrayListOf<UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(UiEvent.ConfirmFile(fileUri, fileExtension, fileSizeBytes))

        val actual = emittedValues.last()

        assertEquals(fileUri, actual.selectedFileUri)
        assertEquals(fileExtension, actual.selectedFileExtension)
        assertEquals(fileSizeBytes, actual.selectedFileSizeBytes)
        assertEquals(fileState, actual.fileState)
        assertEquals(false, actual.isInputValid)

        job.cancel()
    }

    //endregion

    //region validateInput
    @Test
    fun `When change file, if all file requirements are fulfilled and date is selected, isInputValid should be true`() = runBlockingTest {
        val selectedDate = Date()
        val fileUri = "file://image.png"
        val fileExtension = "png"
        val fileSizeBytes : Long = 5_242_879 //Max is 5_242_880
        val fileState = UiState.FileState.Valid(fileUri, fileSizeBytes)

        val emittedValues = arrayListOf<UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        viewModel.processEvent(UiEvent.ConfirmDate(selectedDate))

        //When
        viewModel.processEvent(UiEvent.ConfirmFile(fileUri, fileExtension, fileSizeBytes))

        val actual = emittedValues.last()

        assertEquals(fileUri, actual.selectedFileUri)
        assertEquals(fileExtension, actual.selectedFileExtension)
        assertEquals(fileSizeBytes, actual.selectedFileSizeBytes)
        assertEquals(fileState, actual.fileState)
        assertEquals(true, actual.isInputValid)

        job.cancel()
    }
    //endregion

    //region UnselectFile
    @Test
    fun `When unselect file, should reset all the selected file properties`() = runBlockingTest {
        val fileUri = ""
        val fileExtension = ""
        val fileSizeBytes : Long = 0 //Max is 5_242_880
        val fileState = UiState.FileState.NotSelected

        val emittedValues = arrayListOf<UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(UiEvent.UnselectFile)

        val actual = emittedValues.last()

        assertEquals(fileUri, actual.selectedFileUri)
        assertEquals(fileExtension, actual.selectedFileExtension)
        assertEquals(fileSizeBytes, actual.selectedFileSizeBytes)
        assertEquals(fileState, actual.fileState)
        assertEquals(false, actual.isInputValid)

        job.cancel()
    }
    //endregion

    //region TapChangeDate
    @Test
    fun `When receive TapChangeDate event, should emit ShowDatePicker effect with previously selected date`() = runBlockingTest {
        //Given
        val previouslySelectedDate : Date? = null
        val expectedEffect = UiEffect.ShowDatePicker(previouslySelectedDate)

        val emittedEffects = arrayListOf<UiEffect>()
        val job = launch {
            viewModel.uiEffect.toList(emittedEffects)
        }

        //When
        viewModel.processEvent(UiEvent.TapChangeDate)

        //Then
        val actualEffect = emittedEffects.last()
        assertEquals(expectedEffect, actualEffect)

        job.cancel()
    }

    @Test
    fun `When receive TapChangeDate event, if date picker already displayed do not emit new show date picker event`() = runBlockingTest {
        //Given

        val expectedEffectSize = 1
        val emittedEffects = arrayListOf<UiEffect>()
        val job = launch {
            viewModel.uiEffect.toList(emittedEffects)
        }

        //When

        //Simulate spamming click calendar icon 4 times to trigger date picker bottomsheet displayed
        viewModel.processEvent(UiEvent.TapChangeDate)
        viewModel.processEvent(UiEvent.TapChangeDate)
        viewModel.processEvent(UiEvent.TapChangeDate)
        viewModel.processEvent(UiEvent.TapChangeDate)

        //Then
        val actualEffectSize = emittedEffects.size
        assertEquals(expectedEffectSize, actualEffectSize)

        job.cancel()
    }
    //endregion

    //region ConfirmDate
    @Test
    fun `When confirm date, selectedDate should be updated with newly selected date`() = runBlockingTest {
        val newlySelectedDate = Date()

        val emittedValues = arrayListOf<UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(UiEvent.ConfirmDate(newlySelectedDate))

        val actual = emittedValues.last()

        assertEquals(newlySelectedDate, actual.selectedDate)
        assertEquals(false, actual.isInputValid)

        job.cancel()
    }
    //endregion


    //region SubmitFile
    @Test
    fun `When submit file while date is not selected yet, should not do any gql call`() = runBlockingTest {
        val formattedNibPublishDate = "2023-01-01"
        val fileUri = "file://image.png"
        val fileExtension = "png"
        val fileSizeBytes : Long = 500
        val result = UploadFileResult(isSuccess = true, errorMessage = "")

        coEvery { uploadFileUseCase.execute(fileUri, formattedNibPublishDate) } returns result
        viewModel.processEvent(UiEvent.ConfirmFile(fileUri, fileExtension, fileSizeBytes))

        //When
        viewModel.processEvent(UiEvent.SubmitFile)

        coVerify(exactly = 0) { uploadFileUseCase.execute(fileUri, formattedNibPublishDate)}
    }

    @Test
    fun `When file submission success, error should be null`() = runBlockingTest {
        val nibPublishDate = buildFakeDate()

        val formattedNibPublishDate = "2023-01-01"
        val fileUri = "file://image.png"
        val fileExtension = "png"
        val fileSizeBytes : Long = 500
        val result = UploadFileResult(isSuccess = true, errorMessage = "")

        val emittedValues = arrayListOf<UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        coEvery { uploadFileUseCase.execute(fileUri, formattedNibPublishDate) } returns result

        viewModel.processEvent(UiEvent.ConfirmDate(nibPublishDate))
        viewModel.processEvent(UiEvent.ConfirmFile(fileUri, fileExtension, fileSizeBytes))

        //When
        viewModel.processEvent(UiEvent.SubmitFile)

        val actual = emittedValues.last()

        assertEquals(null, actual.error)
        assertEquals(false, actual.isLoading)

        job.cancel()
    }

    @Test
    fun `When file submission success, should emit RedirectToSubmissionSuccess effect`() = runBlockingTest {
        val nibPublishDate = buildFakeDate()
        val expectedEffect = UiEffect.RedirectToSubmissionSuccess
        val formattedNibPublishDate = "2023-01-01"
        val fileUri = "file://image.png"
        val fileExtension = "png"
        val fileSizeBytes : Long = 500
        val result = UploadFileResult(isSuccess = true, errorMessage = "")

        val emittedEffects = arrayListOf<UiEffect>()
        val job = launch {
            viewModel.uiEffect.toList(emittedEffects)
        }

        coEvery { uploadFileUseCase.execute(fileUri, formattedNibPublishDate) } returns result

        viewModel.processEvent(UiEvent.ConfirmDate(nibPublishDate))
        viewModel.processEvent(UiEvent.ConfirmFile(fileUri, fileExtension, fileSizeBytes))

        //When
        viewModel.processEvent(UiEvent.SubmitFile)

        //Then
        val actualEffect = emittedEffects.last()
        assertEquals(expectedEffect, actualEffect)

        job.cancel()
    }

    @Test
    fun `When file submission error, should emit ShowUploadError effect`() = runBlockingTest {
        val nibPublishDate = buildFakeDate()
        val errorMessage = "File sudah di upload"
        val expectedEffect = UiEffect.ShowUploadError(errorMessage)

        val formattedNibPublishDate = "2023-01-01"
        val fileUri = "file://image.png"
        val fileExtension = "png"
        val fileSizeBytes : Long = 500
        val result = UploadFileResult(isSuccess = false, errorMessage = errorMessage)

        val emittedEffects = arrayListOf<UiEffect>()
        val job = launch {
            viewModel.uiEffect.toList(emittedEffects)
        }

        coEvery { uploadFileUseCase.execute(fileUri, formattedNibPublishDate) } returns result

        viewModel.processEvent(UiEvent.ConfirmDate(nibPublishDate))
        viewModel.processEvent(UiEvent.ConfirmFile(fileUri, fileExtension, fileSizeBytes))

        //When
        viewModel.processEvent(UiEvent.SubmitFile)

        //Then
        val actualEffect = emittedEffects.last()
        assertEquals(expectedEffect, actualEffect)

        job.cancel()
    }

    @Test
    fun `When file submission error because of exception, should emit ShowError effect`() = runBlockingTest {
        val nibPublishDate = buildFakeDate()
        val error = MessageErrorException("Server error")
        val expectedEffect = UiEffect.ShowError(error)

        val formattedNibPublishDate = "2023-01-01"
        val fileUri = "file://image.png"
        val fileExtension = "png"
        val fileSizeBytes : Long = 500

        val emittedEffects = arrayListOf<UiEffect>()
        val job = launch {
            viewModel.uiEffect.toList(emittedEffects)
        }

        coEvery { uploadFileUseCase.execute(fileUri, formattedNibPublishDate) } throws error

        viewModel.processEvent(UiEvent.ConfirmDate(nibPublishDate))
        viewModel.processEvent(UiEvent.ConfirmFile(fileUri, fileExtension, fileSizeBytes))

        //When
        viewModel.processEvent(UiEvent.SubmitFile)

        //Then
        val actualEffect = emittedEffects.last()
        assertEquals(expectedEffect, actualEffect)

        job.cancel()
    }

    @Test
    fun `When file submission error, error should be updated with the error cause`() = runBlockingTest {
        val nibPublishDate = buildFakeDate()
        val error = MessageErrorException("Server error")

        val formattedNibPublishDate = "2023-01-01"
        val fileUri = "file://image.png"
        val fileExtension = "png"
        val fileSizeBytes : Long = 500

        val emittedValues = arrayListOf<UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        coEvery { uploadFileUseCase.execute(fileUri, formattedNibPublishDate) } throws error

        viewModel.processEvent(UiEvent.ConfirmDate(nibPublishDate))
        viewModel.processEvent(UiEvent.ConfirmFile(fileUri, fileExtension, fileSizeBytes))

        //When
        viewModel.processEvent(UiEvent.SubmitFile)

        val actual = emittedValues.last()

        assertEquals(error, actual.error)
        assertEquals(false, actual.isLoading)

        job.cancel()
    }

    //endregion


    //region handleDatePickerDismissed
    @Test
    fun `When date picker dismissed, should set isDatePickerCurrentlyDisplayed to false`() = runBlockingTest {
        //Given
        val expected = false

        val emittedValues = arrayListOf<UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(UiEvent.DatePickerDismissed)

        val actual = emittedValues.last()

        assertEquals(expected, actual.isDatePickerCurrentlyDisplayed)

        job.cancel()
    }
    //endregion

    //region RecordImpression
    @Test
    fun `When record page impression, should call impression tracker`() = runBlockingTest {
        //Given
        val event = UiEvent.RecordImpression

        //When
        viewModel.processEvent(event)

        //Then
        coVerify { tracker.sendPageImpression() }
    }
    //endregion

    //region processEvent
    @Test
    fun `When unlisted event is triggered, should not emit any effect`() {
        runBlockingTest {
            //When
            viewModel.processEvent(mockk())

            val emittedEffects = arrayListOf<UiEffect>()

            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            //Then
            val actualEffect = emittedEffects.lastOrNull()

            assertEquals(null, actualEffect)

            job.cancel()
        }
    }
    //endregion

    private fun buildFakeDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2023)
        calendar.set(Calendar.MONTH, 0)
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        return calendar.time
    }

}
