package com.tokopedia.autocompletecomponent.unify

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.test.espresso.Espresso
import com.tokopedia.autocompletecomponent.unify.domain.model.InitialStateUnifyModel
import com.tokopedia.autocompletecomponent.unify.domain.model.UniverseSuggestionUnifyModel
import com.tokopedia.autocompletecomponent.utils.rawToObject
import com.tokopedia.autocompletecomponent.utils.stubExecute
import com.tokopedia.iris.Iris
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.usecase.coroutines.UseCase
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.tokopedia.autocompletecomponent.test.R as autocompletecomponenttestR

class AutoCompleteScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val irisMock = mockk<Iris>(relaxed = true)
    private val initialStateUseCase = mockk<UseCase<UniverseSuggestionUnifyModel>>(relaxed = true)
    private val suggestionStateUseCase =
        mockk<UseCase<UniverseSuggestionUnifyModel>>(relaxed = true)
    private val autoCompleteViewModel = AutoCompleteViewModel(
        autoCompleteState = AutoCompleteState(),
        initialStateUseCase = initialStateUseCase,
        suggestionStateUseCase = suggestionStateUseCase,
        deleteRecentSearchUseCase = mockk(relaxed = true),
        userSession = mockk(relaxed = true),
        chooseAddressUtilsWrapper = mockk(relaxed = true)
    )

    private lateinit var resultModel: UniverseSuggestionUnifyModel

    @Before
    fun setup() {
        resultModel =
            rawToObject<InitialStateUnifyModel>(autocompletecomponenttestR.raw.unify_initial_state).data
        composeTestRule.setContent {
            NestTheme {
                AutoCompleteScreen(
                    autoCompleteViewModel,
                    iris = irisMock,
                    listener = mockk(relaxed = true)
                )
            }
        }
    }

    @Test
    fun initial_screen_shows_data_from_state() {
        given_initial_state_usecase_success(resultModel)
        when_screen_is_initialized()
        then_assert_that_all_data_exists(resultModel)
    }

    private fun given_initial_state_usecase_success(model: UniverseSuggestionUnifyModel) {
        initialStateUseCase.stubExecute() returns model
    }

    private fun when_screen_is_initialized() {
        autoCompleteViewModel.onScreenInitialized()
    }

    private fun then_assert_that_all_data_exists(model: UniverseSuggestionUnifyModel) {
        Espresso.closeSoftKeyboard()
        model.data.forEachIndexed { index, it ->
            composeTestRule.onAllNodesWithTag("MainLazyColumn").onFirst()
                .performScrollToIndex(index)
            composeTestRule.onAllNodesWithText(it.title.text, ignoreCase = true).onFirst()
                .assertExists()
        }
    }

    @Test
    fun on_item_master_clicked_should_navigate() {
        val inspectedItem = resultModel.data.find { it.template == "master" }
        assert(inspectedItem != null)
        inspectedItem!!
        given_initial_state_usecase_success(resultModel)
        when_screen_is_initialized()
        when_item_is_clicked(inspectedItem.title.text)
        then_assert_that_state_is_navigate_with_applink(inspectedItem.applink)
    }

    private fun when_item_is_clicked(itemText: String) {
        composeTestRule.onNodeWithText(itemText).performClick()
    }

    private fun then_assert_that_state_is_navigate_with_applink(applink: String) {
        assert(autoCompleteViewModel.stateValue.navigate?.applink == applink)
    }
}
