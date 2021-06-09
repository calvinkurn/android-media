package com.tokopedia.kyc_centralized.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.kyc_centralized.domain.GetUserProjectInfoUseCase
import com.tokopedia.kyc_centralized.view.viewmodel.UserIdentificationViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.user_identification_common.domain.pojo.KycUserProjectInfoPojo
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import util.isEqualsTo

@ExperimentalCoroutinesApi
class UserIdentificationViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getUserProjectInfoUseCase: GetUserProjectInfoUseCase = mockk(relaxed = true)
    private val userProjectInfoObservable: Observer<Result<KycUserProjectInfoPojo>> = mockk(relaxed = true)
    private lateinit var viewModel: UserIdentificationViewModel

    @Before fun setup() {
        viewModel = UserIdentificationViewModel(
                getUserProjectInfoUseCase,
                CoroutineTestDispatchersProvider
        )
        viewModel.userProjectInfo.observeForever(userProjectInfoObservable)
    }

    @Test
    fun `it should get the user project info`() = runBlockingTest {
        val expectedReturn = KycUserProjectInfoPojo()
        val expectedValue = Success(expectedReturn)
        val testProjectId = 0

        coEvery {
            getUserProjectInfoUseCase.executeOnBackground()
        } returns expectedReturn

        viewModel.getUserProjectInfo(testProjectId)

        verify { userProjectInfoObservable.onChanged(expectedValue) }
        viewModel.userProjectInfo isEqualsTo expectedValue
    }

    @Test
    fun `it should throw error user project info`() = runBlockingTest {
        val expectedReturn = Throwable("Oops")
        val expectedValue = Fail(expectedReturn)
        val testProjectId = 0

        coEvery {
            getUserProjectInfoUseCase.executeOnBackground()
        } throws expectedReturn

        viewModel.getUserProjectInfo(testProjectId)

        verify { userProjectInfoObservable.onChanged(expectedValue) }
        viewModel.userProjectInfo isEqualsTo expectedValue
    }

    @Test
    fun `it should cancel all jobs`() = runBlockingTest {
        viewModel.onCleared()
        verify {
            getUserProjectInfoUseCase.cancelJobs()
        }
    }
}