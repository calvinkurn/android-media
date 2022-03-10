package com.tokopedia.home_account.view.explicitprofile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home_account.explicitprofile.data.ExplicitprofileGetQuestion
import com.tokopedia.home_account.explicitprofile.domain.GetQuestionsUseCase
import com.tokopedia.home_account.explicitprofile.features.categories.CategoryViewModel
import com.tokopedia.home_account.explicitprofile.wrapper.ExplicitProfileResult
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExplicitProfileCategoryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var viewModel: CategoryViewModel? = null
    private val dispatcherProviderTest = CoroutineTestDispatchersProvider

    private val getQuestionsUseCase = mockk<GetQuestionsUseCase>(relaxed = true)

    private val observerQuestions = mockk<Observer<ExplicitProfileResult<ExplicitprofileGetQuestion>>>(relaxed = true)

    private val paramTemplate = "template"
    private val mockThrowable = Throwable("Opps!")
    private val mockResponse = ExplicitprofileGetQuestion()

    @Before
    fun setup() {
        viewModel = CategoryViewModel(getQuestionsUseCase, dispatcherProviderTest)
        viewModel?.questions?.observeForever(observerQuestions)
    }

    @After
    fun tearDown() {
        viewModel?.questions?.removeObserver(observerQuestions)
    }

    @Test
    fun `getQuestion - success flow`() {

        coEvery {
            getQuestionsUseCase(any())
        } returns mockResponse

        viewModel?.getQuestion(paramTemplate)

        coVerify {
            observerQuestions.onChanged(
                ExplicitProfileResult.Success(mockResponse)
            )
        }

        val result = viewModel?.questions?.value
        assert(result is ExplicitProfileResult.Success)
    }

    @Test
    fun `getQuestion - failed flow`() {

        coEvery {
            getQuestionsUseCase(any())
        } throws mockThrowable

        viewModel?.getQuestion(paramTemplate)

        coVerify {
            observerQuestions.onChanged(
                ExplicitProfileResult.Failure(mockThrowable)
            )
        }

        val result = viewModel?.questions?.value
        assert(result is ExplicitProfileResult.Failure)
    }

}