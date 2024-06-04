package com.tokopedia.home_account.explicitprofile

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.filters.LargeTest
import com.tokopedia.home_account.explicitprofile.data.QuestionDataModel
import com.tokopedia.home_account.explicitprofile.personalize.ui.PersonalizeQuestionScreen
import com.tokopedia.home_account.explicitprofile.personalize.ui.QuestionSection
import com.tokopedia.home_account.explicitprofile.personalize.ui.SuccessScreen
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@LargeTest
@UiTest
class ExplicitPersonalizeActivityTest {

    @get:Rule
    val composeRule = createComposeRule()

    @get:Rule
    val composeTestRule = createEmptyComposeRule()

    private lateinit var listQuestion : List<QuestionDataModel>

    @Before
    fun before() {
        composeTestRule.mainClock.autoAdvance = true
        listQuestion = listOf(
            QuestionDataModel(questionId = 1, property = QuestionDataModel.Property(
                name = "Fashion", options = mutableListOf(
                    QuestionDataModel.Property.Options(caption = "Muslim", isSelected = true),
                    QuestionDataModel.Property.Options(caption = "Wanita", isSelected = false),
                    QuestionDataModel.Property.Options(caption = "Anak & Bayi", isSelected = true),
                    QuestionDataModel.Property.Options(caption = "Pria", isSelected = true)
                )
            ))
        )
    }

    @Test
    fun when_success_then_show_list() {
        composeRule.setContent {
            PersonalizeQuestionScreen(
                listQuestion = listQuestion,
                countItemSelected = 0,
                maxItemSelected = 5,
                minItemSelected = 0,
                onSave = {  },
                onSkip = {  },
                onOptionSelected = {  },
                isLoadingSaveAnswer = false
            )
        }

        composeRule.apply {
            onNodeWithTag("skip button", useUnmergedTree = true).assertExists()
            onNodeWithTag("title header section", useUnmergedTree = true).assertExists()
            onNodeWithTag("subtitle header section", useUnmergedTree = true).assertExists()
            onNodeWithTag("counter header section", useUnmergedTree = true).assertExists()
            onNodeWithTag("question section", useUnmergedTree = true).assertExists()
            onNodeWithTag("button save", useUnmergedTree = true).assertExists()
        }
    }

    @Test
    fun when_success_then_click_skip() {
        var callback = false

        composeRule.setContent {
            PersonalizeQuestionScreen(
                listQuestion = listQuestion,
                countItemSelected = 0,
                maxItemSelected = 5,
                minItemSelected = 0,
                onSave = {  },
                onSkip = { callback = true },
                onOptionSelected = {  },
                isLoadingSaveAnswer = false
            )
        }

        composeRule.apply {
            onNodeWithTag("skip button", useUnmergedTree = true).performClick()
        }

        assert(callback)
    }

    @Test
    fun when_success_submit_then_show_success_page() {
        composeRule.setContent {
            SuccessScreen()
        }

        composeRule.apply {
            onNodeWithTag("title success page", useUnmergedTree = true).assertExists()
            onNodeWithTag("subtitle success page", useUnmergedTree = true).assertExists()
        }
    }

    @Test
    fun when_click_options_then_run_callback() {
        var callback = false

        composeRule.setContent {
            QuestionSection(
                urlImage = "",
                sectionTitle = "Fashion",
                isMaxOptionsSelected = false,
                listOptions = listOf(QuestionDataModel.Property.Options(caption = "Wanita")),
                onOptionSelected = {
                    callback = true
                },
                indexCategory = 0,
                questionId = 1
            )
        }

        composeRule.apply {
            onNodeWithText("Wanita", useUnmergedTree = true).performClick()
        }

        assert(callback)
    }

    @Test
    fun when_save_then_run_callback() {
        var callback = false

        composeRule.setContent {
            PersonalizeQuestionScreen(
                listQuestion = listQuestion,
                countItemSelected = 0,
                maxItemSelected = 5,
                minItemSelected = 0,
                onSave = { callback = true },
                onSkip = {  },
                onOptionSelected = {  },
                isLoadingSaveAnswer = false
            )
        }

        composeRule.apply {
            onNodeWithTag("button save", useUnmergedTree = true).performClick()
        }

        assert(callback)
    }

}
