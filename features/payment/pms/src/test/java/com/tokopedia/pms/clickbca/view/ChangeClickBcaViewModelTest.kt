package com.tokopedia.pms.clickbca.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.pms.clickbca.data.model.EditKlikbca
import com.tokopedia.pms.clickbca.domain.ChangeClickBcaUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ChangeClickBcaViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val changeClickBcaUseCase = mockk<ChangeClickBcaUseCase>(relaxed = true)
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: ChangeClickBcaViewModel


    private val fetchFailedErrorMessage = "Fetch Failed"
    private val nullDataErrorMessage = "NULL DATA"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)


    @Before
    fun setUp() {
        viewModel = ChangeClickBcaViewModel(changeClickBcaUseCase, dispatcher)
    }

    @Test
    fun `changeClickBcaUserId invoke Failed`() {
        coEvery {
            changeClickBcaUseCase.changeClickBcaId(
                any(),
                any(),
                "t1",
                "m1",
                "id1"
            )
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.changeClickBcaUserId("t1", "m1", "id1")
        assert(viewModel.editResultLiveData.value is Fail)
    }

    @Test
    fun `changeClickBcaUserId changed successfully`() {
        val data = EditKlikbca().also { it.isSuccess = true }
        coEvery {
            changeClickBcaUseCase.changeClickBcaId(
                any(),
                any(),
                "t1",
                "m1",
                "id1"
            )
        } coAnswers {
            firstArg<(EditKlikbca) -> Unit>().invoke(data)
        }
        viewModel.changeClickBcaUserId("t1", "m1", "id1")
        assert(viewModel.editResultLiveData.value is Success)
        Assertions.assertThat((viewModel.editResultLiveData.value as Success).data.isSuccess)
            .isEqualTo(true)
    }
}