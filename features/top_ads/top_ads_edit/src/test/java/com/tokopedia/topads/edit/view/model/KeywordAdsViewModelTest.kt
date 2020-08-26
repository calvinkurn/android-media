package com.tokopedia.topads.edit.view.model

import com.tokopedia.topads.common.data.response.KeywordSuggestionResponse
import com.tokopedia.topads.edit.usecase.SuggestionKeywordUseCase
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordItemViewModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class KeywordAdsViewModelTest {

    private val suggestionKeywordUseCase: SuggestionKeywordUseCase = mockk(relaxed = true)
    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: KeywordAdsViewModel
    private lateinit var count: HashMap<String, ArrayList<Int>>
    private lateinit var search: HashSet<String>

    @Before
    fun setUp() {
        count = mockk(relaxed = true)
        search = mockk(relaxed = true)
        viewModel = KeywordAdsViewModel(testDispatcher, suggestionKeywordUseCase)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun getSuggestionKeyword() {
        val data = KeywordSuggestionResponse.Result()
        every {
            suggestionKeywordUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(KeywordSuggestionResponse.Result) -> Unit>()
            onSuccess.invoke(data)
        }
        viewModel.getSuggestionKeyword("name", 12) {}

        verify {
            suggestionKeywordUseCase.executeQuerySafeMode(any(), any())
        }
    }

    @Test
    fun addNewKeyword() {
        val expected: KeywordItemViewModel = mockk()
        val actual = viewModel.addNewKeyword("iphone", 200)
        assertNotEquals(actual, expected)
    }


    @Test
    fun onCleared() {
        viewModel.onCleared()
        verify { suggestionKeywordUseCase.cancelJobs() }
    }
}