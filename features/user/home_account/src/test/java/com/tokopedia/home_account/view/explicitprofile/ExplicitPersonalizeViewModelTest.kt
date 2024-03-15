package com.tokopedia.home_account.view.explicitprofile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home_account.explicitprofile.data.ExplicitProfileGetQuestionDataModel
import com.tokopedia.home_account.explicitprofile.data.ExplicitProfileSaveMultiAnswers
import com.tokopedia.home_account.explicitprofile.data.ExplicitprofileGetQuestion
import com.tokopedia.home_account.explicitprofile.data.FeatureVariantsItem
import com.tokopedia.home_account.explicitprofile.data.QuestionDataModel
import com.tokopedia.home_account.explicitprofile.data.RolloutUserVariant
import com.tokopedia.home_account.explicitprofile.data.RolloutUserVariantResponse
import com.tokopedia.home_account.explicitprofile.data.SectionsDataModel
import com.tokopedia.home_account.explicitprofile.data.TemplateDataModel
import com.tokopedia.home_account.explicitprofile.domain.GetQuestionsUseCase
import com.tokopedia.home_account.explicitprofile.domain.GetRolloutUserVariantUseCase
import com.tokopedia.home_account.explicitprofile.domain.SaveMultipleAnswersUseCase
import com.tokopedia.home_account.explicitprofile.personalize.ExplicitPersonalizeResult
import com.tokopedia.home_account.explicitprofile.personalize.ExplicitPersonalizeViewModel
import com.tokopedia.home_account.explicitprofile.personalize.PersonalizeSaveAnswerResult
import com.tokopedia.home_account.explicitprofile.personalize.ui.OptionSelected
import com.tokopedia.home_account.getOrAwaitValue
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExplicitPersonalizeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var viewModel: ExplicitPersonalizeViewModel

    private val getQuestionsUseCase = mockk<GetQuestionsUseCase>(relaxed = true)
    private val getRolloutUserVariantUseCase = mockk<GetRolloutUserVariantUseCase>(relaxed = true)
    private val saveMultipleAnswersUseCase = mockk<SaveMultipleAnswersUseCase>(relaxed = true)

    private val minAnswer = 3
    private val maxAnswer = 10
    private val listQuestion = mutableListOf(QuestionDataModel(
        questionId = 1,
        property = QuestionDataModel.Property(
            options = mutableListOf(QuestionDataModel.Property.Options(value = "999", caption = "Olahraga"))
        ),
        answerValueList = mutableListOf("999")
    ))
    private val responseSuccessGetQuestion = ExplicitprofileGetQuestion(
        ExplicitProfileGetQuestionDataModel(
            template = TemplateDataModel(
                id = 123,
                rules = TemplateDataModel.Rules(
                    minAnswer = minAnswer,
                    maxAnswer = maxAnswer
                ),
                sections = mutableListOf(
                    SectionsDataModel(
                        sectionId = 456,
                        layout = "MultipleAnswer",
                        questions = listQuestion
                    )
                )
            )
        )
    )
    private val responseRollance = RolloutUserVariantResponse(
        RolloutUserVariant(featureVariants = listOf(FeatureVariantsItem(feature = "cat_affinity_android", variant = "treatment_variant")))
    )

    @Before
    fun setup() {
        viewModel = ExplicitPersonalizeViewModel(
            getQuestionsUseCase,
            getRolloutUserVariantUseCase,
            saveMultipleAnswersUseCase,
            dispatcher
        )
    }

    @Test
    fun `when get question then rollance off`() {
        val expected = ExplicitPersonalizeResult.Failed
        val response = RolloutUserVariantResponse(RolloutUserVariant(featureVariants = listOf(FeatureVariantsItem(variant = ""))))
        val responseSuccessGetQuestion = responseSuccessGetQuestion

        coEvery { getRolloutUserVariantUseCase(any()) } returns response
        coEvery { getQuestionsUseCase(any()) } returns responseSuccessGetQuestion
        viewModel.getQuestion()

        val result = viewModel.stateGetQuestion.getOrAwaitValue()
        assertEquals(expected, result)
    }

    @Test
    fun `when get question then failed get question`() {
        val expected = ExplicitPersonalizeResult.Failed
        val responseRollance = responseRollance

        coEvery { getRolloutUserVariantUseCase(any()) } returns responseRollance
        coEvery { getQuestionsUseCase(any()) } throws Throwable()
        viewModel.getQuestion()

        val result = viewModel.stateGetQuestion.getOrAwaitValue()
        assertEquals(expected, result)
    }

    @Test
    fun `when get question then section empty`() {
        val expected = ExplicitPersonalizeResult.Failed
        val responseRollance = responseRollance
        val responseGetQuestion = ExplicitprofileGetQuestion()

        coEvery { getRolloutUserVariantUseCase(any()) } returns responseRollance
        coEvery { getQuestionsUseCase(any()) } returns responseGetQuestion
        viewModel.getQuestion()

        val result = viewModel.stateGetQuestion.getOrAwaitValue()
        assertEquals(expected, result)
    }

    @Test
    fun `when get question then no multi answer`() {
        val expected = ExplicitPersonalizeResult.Failed
        val responseRollance = responseRollance
        val responseGetQuestion = ExplicitprofileGetQuestion(
            ExplicitProfileGetQuestionDataModel(
                template = TemplateDataModel(
                    sections = mutableListOf(
                        SectionsDataModel(layout = "SingleAnswer")
                    )
                )
            )
        )

        coEvery { getRolloutUserVariantUseCase(any()) } returns responseRollance
        coEvery { getQuestionsUseCase(any()) } returns responseGetQuestion
        viewModel.getQuestion()

        val result = viewModel.stateGetQuestion.getOrAwaitValue()
        assertEquals(expected, result)
    }

    @Test
    fun `when get question then success get question`() {
        val expected = ExplicitPersonalizeResult.Success(
            listQuestion = listQuestion,
            minItemSelected = minAnswer,
            maxItemSelected = maxAnswer
        )
        val responseRollance = responseRollance
        val responseGetQuestion = responseSuccessGetQuestion

        coEvery { getRolloutUserVariantUseCase(any()) } returns responseRollance
        coEvery { getQuestionsUseCase(any()) } returns responseGetQuestion
        viewModel.getQuestion()

        val resultQuestion = viewModel.stateGetQuestion.getOrAwaitValue()
        val resultCounter = viewModel.counterState.getOrAwaitValue()
        assertEquals(expected, resultQuestion)
        assertEquals(1, resultCounter)
    }

    @Test
    fun `when select item then data updated`() {
        val expected = ExplicitPersonalizeResult.Success(
            listQuestion = mutableListOf(QuestionDataModel(
                questionId = 1,
                property = QuestionDataModel.Property(
                    options = mutableListOf(QuestionDataModel.Property.Options(value = "999", caption = "Olahraga", isSelected = true))
                ),
                answerValueList = mutableListOf("999")
            )),
            minItemSelected = minAnswer,
            maxItemSelected = maxAnswer
        )
        val responseRollance = responseRollance
        val responseGetQuestion = responseSuccessGetQuestion
        val itemSelected = OptionSelected(
            indexOption = 0,
            indexCategory = 0,
            isSelected = false,
            name = "Olahraga",
            questionId = 1
        )

        coEvery { getRolloutUserVariantUseCase(any()) } returns responseRollance
        coEvery { getQuestionsUseCase(any()) } returns responseGetQuestion
        viewModel.getQuestion()
        viewModel.itemSelected(itemSelected)


        val resultQuestion = viewModel.stateGetQuestion.getOrAwaitValue()
        val resultCounter = viewModel.counterState.getOrAwaitValue()
        assertEquals(expected, resultQuestion)
        assertEquals(2, resultCounter)
    }

    @Test
    fun `when unselect item then data updated`() {
        val expected = ExplicitPersonalizeResult.Success(
            listQuestion = mutableListOf(QuestionDataModel(
                questionId = 1,
                property = QuestionDataModel.Property(
                    options = mutableListOf(QuestionDataModel.Property.Options(value = "999", caption = "Olahraga", isSelected = false))
                ),
                answerValueList = mutableListOf("999")
            )),
            minItemSelected = minAnswer,
            maxItemSelected = maxAnswer
        )
        val responseRollance = responseRollance
        val responseGetQuestion = ExplicitprofileGetQuestion(
            ExplicitProfileGetQuestionDataModel(
                template = TemplateDataModel(
                    rules = TemplateDataModel.Rules(
                        minAnswer = minAnswer,
                        maxAnswer = maxAnswer
                    ),
                    sections = mutableListOf(
                        SectionsDataModel(
                            layout = "MultipleAnswer",
                            questions = mutableListOf(QuestionDataModel(
                                questionId = 1,
                                property = QuestionDataModel.Property(
                                    options = mutableListOf(QuestionDataModel.Property.Options(value = "999", caption = "Olahraga", isSelected = true))
                                ),
                                answerValueList = mutableListOf("999")
                            ))
                        )
                    )
                )
            )
        )
        val itemSelected = OptionSelected(
            indexOption = 0,
            indexCategory = 0,
            isSelected = true,
            name = "Olahraga",
            questionId = 1
        )

        coEvery { getRolloutUserVariantUseCase(any()) } returns responseRollance
        coEvery { getQuestionsUseCase(any()) } returns responseGetQuestion
        viewModel.getQuestion()
        viewModel.itemSelected(itemSelected)


        val resultQuestion = viewModel.stateGetQuestion.getOrAwaitValue()
        val resultCounter = viewModel.counterState.getOrAwaitValue()
        assertEquals(expected, resultQuestion)
        assertEquals(0, resultCounter)
    }

    @Test
    fun `when submit question then failed`() {
        val responseRollance = responseRollance
        val responseGetQuestion = responseSuccessGetQuestion
        val responseSaveAnswers = ExplicitProfileSaveMultiAnswers()

        coEvery { getRolloutUserVariantUseCase(any()) } returns responseRollance
        coEvery { getQuestionsUseCase(any()) } returns responseGetQuestion
        coEvery { saveMultipleAnswersUseCase(any()) } returns responseSaveAnswers
        viewModel.getQuestion()
        viewModel.saveAnswers()

        val result = viewModel.stateSaveAnswer.getOrAwaitValue()
        assert(result is PersonalizeSaveAnswerResult.Failed)
    }

    @Test
    fun `when submit question then success`() {
        val responseRollance = responseRollance
        val responseGetQuestion = responseSuccessGetQuestion
        val responseSaveAnswers = ExplicitProfileSaveMultiAnswers(
            ExplicitProfileSaveMultiAnswers.ResponseMutlipleAnswerDataModel(isSuccess = true)
        )

        coEvery { getRolloutUserVariantUseCase(any()) } returns responseRollance
        coEvery { getQuestionsUseCase(any()) } returns responseGetQuestion
        coEvery { saveMultipleAnswersUseCase(any()) } returns responseSaveAnswers
        viewModel.getQuestion()
        viewModel.saveAnswers()

        val result = viewModel.stateSaveAnswer.getOrAwaitValue()
        assert(result is PersonalizeSaveAnswerResult.Success)
    }

    @Test
    fun `when submit question then throwable`() {
        val responseRollance = responseRollance
        val responseGetQuestion = responseSuccessGetQuestion

        coEvery { getRolloutUserVariantUseCase(any()) } returns responseRollance
        coEvery { getQuestionsUseCase(any()) } returns responseGetQuestion
        coEvery { saveMultipleAnswersUseCase(any()) } throws Throwable()
        viewModel.getQuestion()
        viewModel.saveAnswers()

        val result = viewModel.stateSaveAnswer.getOrAwaitValue()
        assert(result is PersonalizeSaveAnswerResult.Failed)
    }

}
