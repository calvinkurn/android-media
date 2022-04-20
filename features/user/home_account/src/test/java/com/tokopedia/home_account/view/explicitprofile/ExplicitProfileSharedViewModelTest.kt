package com.tokopedia.home_account.view.explicitprofile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home_account.explicitprofile.data.QuestionDataModel
import com.tokopedia.home_account.explicitprofile.data.SectionsDataModel
import com.tokopedia.home_account.explicitprofile.data.TemplateDataModel
import com.tokopedia.home_account.explicitprofile.features.ExplicitProfileSharedViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExplicitProfileSharedViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var viewModel: ExplicitProfileSharedViewModel? = null
    private val dispatcherProviderTest = CoroutineTestDispatchersProvider

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
        viewModel = ExplicitProfileSharedViewModel(dispatcherProviderTest)

        mockDefaultTemplateData.forEach {
            viewModel?.setDefaultTemplatesData(it)
        }
    }

    @Test
    fun setDefaultData() {
        mockDefaultTemplateData.forEach {
            viewModel?.setDefaultTemplatesData(it)
        }

        assert(viewModel?.isAnswersSameWithDefault() == true)
    }

    @Test
    fun isAnswersSameWithDefault() {
        assert(viewModel?.isAnswersSameWithDefault() == true)
    }

    @Test
    fun `isAnswersSameWithDefault - user answer changed`() {

        viewModel?.onAnswerChange(
            TemplateDataModel(id = 1, sections = mutableListOf(
                SectionsDataModel(questions = mutableListOf(
                    QuestionDataModel(questionId = 11, answerValue = "yes")
                ))
            ))
        )

        assert(viewModel?.isAnswersSameWithDefault() == false)
    }
}