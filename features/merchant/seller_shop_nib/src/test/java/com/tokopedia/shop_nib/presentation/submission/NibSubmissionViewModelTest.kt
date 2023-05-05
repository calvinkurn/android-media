package com.tokopedia.shop_nib.presentation.submission

import com.tokopedia.shop_nib.domain.usecase.UploadFileUseCase
import com.tokopedia.shop_nib.presentation.submission.uimodel.UiEffect
import com.tokopedia.shop_nib.presentation.submission.uimodel.UiEvent
import com.tokopedia.shop_nib.presentation.submission.uimodel.UiState
import com.tokopedia.shop_nib.util.tracker.NibSubmissionPageTracker
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
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
        assertEquals(fileSizeBytes, actual.selectedFileSizeKb)
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
        assertEquals(fileSizeBytes, actual.selectedFileSizeKb)
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
        assertEquals(fileSizeBytes, actual.selectedFileSizeKb)
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
        assertEquals(fileSizeBytes, actual.selectedFileSizeKb)
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
        assertEquals(fileSizeBytes, actual.selectedFileSizeKb)
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
        assertEquals(fileSizeBytes, actual.selectedFileSizeKb)
        assertEquals(fileState, actual.fileState)
        assertEquals(false, actual.isInputValid)

        job.cancel()
    }
    //endregion

    //region UnselectFile

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
    //endregion

    //region ConfirmDate

    //endregion


    //region SubmitFile

    //endregion


    //region RecordImpression

    //endregion
    /*@Test
    fun `When fetch category from remote success, should successfully receive the data`() = runBlockingTest {
        val categories = listOf(FlashSaleCategory(1, "Book"))

        val tabName = "upcoming"

        coEvery { getFlashSaleListForSellerCategoryUseCase.execute(tabName) } returns categories

        val emittedValues = arrayListOf<FlashSaleListUiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(FlashSaleListUiEvent.GetFlashSaleCategory(tabName))

        val actualCategories = emittedValues.last().flashSaleCategories
        val actualTabName = emittedValues.last().tabName

        Assert.assertEquals(tabName, actualTabName)
        Assert.assertEquals(categories, actualCategories)

        job.cancel()
    }

    @Test
    fun `When fetch category from remote error, should emit correct error event`() = runBlockingTest {
        //Given
        val tabName = "upcoming"
        val error = MessageErrorException("Server Error")
        val expectedEvent = FlashSaleListUiEffect.FetchCategoryError(error)

        coEvery { getFlashSaleListForSellerCategoryUseCase.execute(tabName) } throws error

        val emittedValues = arrayListOf<FlashSaleListUiEffect>()
        val job = launch {
            viewModel.uiEffect.toList(emittedValues)
        }

        //When
        viewModel.processEvent(FlashSaleListUiEvent.GetFlashSaleCategory(tabName))

        val actualEvent = emittedValues.last()

        Assert.assertEquals(expectedEvent, actualEvent)

        job.cancel()
    }
*/
}
