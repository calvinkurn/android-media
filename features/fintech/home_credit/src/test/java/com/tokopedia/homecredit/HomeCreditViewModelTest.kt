package com.tokopedia.homecredit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.otaliastudios.cameraview.size.Size
import com.tokopedia.homecredit.domain.model.ImageDetail
import com.tokopedia.homecredit.domain.usecase.HomeCreditUseCase
import com.tokopedia.homecredit.viewModel.HomeCreditViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeCreditViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val homeCreditUseCase = mockk<HomeCreditUseCase>(relaxed = true)
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: HomeCreditViewModel

    private val saveFailedErrorMessage = "Unable To Save"
    private val mockThrowable = Throwable(message = saveFailedErrorMessage)

    @Before
    fun setup() {
        viewModel = HomeCreditViewModel(homeCreditUseCase, dispatcher = dispatcher)
    }


    @Test
    fun successHomeCreditViewModel() {
        val imageDetail = mockk<ImageDetail>(relaxed = true)
        val byteArray = ByteArray(0)
        val captureSize = Size(0, 0)
        coEvery {
            homeCreditUseCase.saveDetail(any(), any(), byteArray, captureSize)
        } coAnswers {
            firstArg<(ImageDetail) -> Unit>().invoke(imageDetail)

        }
        viewModel.computeImageArray(byteArray, captureSize)
        Assert.assertEquals((viewModel.imageDetailLiveData.value as Success).data, imageDetail)
    }


    @Test
    fun failHomeCreditViewModel() {
        val byteArray = ByteArray(0)
        val captureSize = Size(0, 0)
        coEvery {
            homeCreditUseCase.saveDetail(any(), any(), byteArray, captureSize)
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)

        }
        viewModel.computeImageArray(byteArray, captureSize)
        Assert.assertEquals((viewModel.imageDetailLiveData.value as Fail).throwable, mockThrowable)
    }


}