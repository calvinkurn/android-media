package com.tokopedia.home_account.view.explicitprofile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home_account.explicitprofile.data.*
import com.tokopedia.home_account.explicitprofile.domain.GetCategoriesUseCase
import com.tokopedia.home_account.explicitprofile.domain.SaveMultipleAnswersUseCase
import com.tokopedia.home_account.explicitprofile.features.ExplicitProfileViewModel
import com.tokopedia.home_account.explicitprofile.wrapper.ExplicitProfileResult
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExplicitProfileViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var viewModel: ExplicitProfileViewModel? = null
    private val dispatcherProviderTest = CoroutineTestDispatchersProvider

    private val getCategoriesUseCase = mockk<GetCategoriesUseCase>(relaxed = true)
    private val saveMultipleAnswersUseCase = mockk<SaveMultipleAnswersUseCase>(relaxed = true)

    private val observerExplicitCategories = mockk<Observer<ExplicitProfileResult<CategoriesDataModel>>>(relaxed = true)
    private val observerSaveAnswers = mockk<Observer<ExplicitProfileResult<ExplicitProfileSaveMultiAnswers>>>(relaxed = true)

    private val mockStringValue = "Mock Value"
    private val mockThrowable = MessageErrorException("Opps!")

    private val mockDefaultTemplateData = mutableListOf(
        TemplateDataModel(id = 1, sections = mutableListOf(
            SectionsDataModel(questions = mutableListOf(
                QuestionDataModel(questionId = 11, answerValue = "yes"),
                QuestionDataModel(questionId = 12, answerValue = "yes"),
            ))
        )),
        TemplateDataModel(id = 2, sections = mutableListOf(
            SectionsDataModel(questions = mutableListOf(
                QuestionDataModel(questionId = 21, answerValue = "yes"),
                QuestionDataModel(questionId = 22, answerValue = "no"),
            ))
        )),
        TemplateDataModel(id = 3, sections = mutableListOf(
            SectionsDataModel(questions = mutableListOf(
                QuestionDataModel(questionId = 31, answerValue = "no"),
                QuestionDataModel(questionId = 32, answerValue = "no"),
            ))
        ))
    )

    @Before
    fun setup() {
        viewModel = ExplicitProfileViewModel(
            getCategoriesUseCase,
            saveMultipleAnswersUseCase,
            dispatcherProviderTest
        )

        viewModel?.apply {
            explicitCategories.observeForever(observerExplicitCategories)
            saveAnswers.observeForever(observerSaveAnswers)
        }
    }

    @After
    fun tearDown() {
        viewModel?.apply {
            explicitCategories.removeObserver(observerExplicitCategories)
            saveAnswers.removeObserver(observerSaveAnswers)
        }
    }

    @Test
    fun `getAllCategories - success flow`() {
        val mockResponse = CategoriesDataModel(
            CategoriesDataModel.Data(mutableListOf(
                CategoryDataModel(
                    idCategory = 0,
                    name = mockStringValue,
                    imageDisabled = mockStringValue,
                    imageEnabled = mockStringValue
                )
            ))
        )

        coEvery {
            getCategoriesUseCase(Unit)
        } returns mockResponse

        viewModel?.getAllCategories()

        coVerify {
            observerExplicitCategories.onChanged(
                ExplicitProfileResult.Success(mockResponse)
            )
        }
        val result = viewModel?.explicitCategories?.value
        assert(result is ExplicitProfileResult.Success)
        assert((result as ExplicitProfileResult.Success).data.data.dataCategories.isNotEmpty())
    }

    @Test
    fun `getAllCategories - failed flow`() {

        coEvery {
            getCategoriesUseCase(Unit)
        } throws mockThrowable

        viewModel?.getAllCategories()

        val result = viewModel?.explicitCategories?.value
        assert(result is ExplicitProfileResult.Failure)
    }

    @Test
    fun `saveShoppingPreferences - success flow`() {
        val mockResponse = ExplicitProfileSaveMultiAnswers(
            ExplicitProfileSaveMultiAnswers.ResponseMutlipleAnswerDataModel(
                message = mockStringValue
            )
        )

        coEvery {
            saveMultipleAnswersUseCase(any())
        } returns mockResponse

        viewModel?.saveShoppingPreferences(mockDefaultTemplateData)

        coVerify {
            observerSaveAnswers.onChanged(
                ExplicitProfileResult.Success(mockResponse)
            )
        }

        val result = viewModel?.saveAnswers?.value
        assert(result is ExplicitProfileResult.Success)
        assert((result as ExplicitProfileResult.Success).data.response.message.isNotEmpty())
    }

    @Test
    fun `saveShoppingPreferences - failed with empty response message flow`() {
        val mockResponse = ExplicitProfileSaveMultiAnswers(
            ExplicitProfileSaveMultiAnswers.ResponseMutlipleAnswerDataModel(
                message = ""
            )
        )

        coEvery {
            saveMultipleAnswersUseCase(any())
        } returns mockResponse

        viewModel?.saveShoppingPreferences(mockDefaultTemplateData)

        coVerify {
            observerSaveAnswers.onChanged(
                ExplicitProfileResult.Success(mockResponse)
            )
        }

        val result = viewModel?.saveAnswers?.value
        assert(result is ExplicitProfileResult.Success)
        assert((result as ExplicitProfileResult.Success).data.response.message.isEmpty())
    }

    @Test
    fun `saveShoppingPreferences - failed flow`() {

        coEvery {
            saveMultipleAnswersUseCase(any())
        } throws  mockThrowable

        viewModel?.saveShoppingPreferences(mockDefaultTemplateData)

        val result = viewModel?.saveAnswers?.value
        assert(result is ExplicitProfileResult.Failure)
    }
}