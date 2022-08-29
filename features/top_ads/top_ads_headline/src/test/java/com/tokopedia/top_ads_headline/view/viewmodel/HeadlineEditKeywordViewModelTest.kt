package com.tokopedia.top_ads_headline.view.viewmodel

import com.tokopedia.top_ads_headline.Constants
import com.tokopedia.top_ads_headline.Constants.EDIT_HEADLINE_PAGE
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import com.tokopedia.topads.common.domain.usecase.GetAdKeywordUseCase
import io.mockk.*
import org.junit.Assert.*

import org.junit.After
import org.junit.Test

class HeadlineEditKeywordViewModelTest {

    private val getAdKeywordUseCase: GetAdKeywordUseCase = mockk(relaxed = true)
    private val viewModel = spyk(HeadlineEditKeywordViewModel(getAdKeywordUseCase))

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun getAdKeyword() {
    }

    @Test
    fun `getSelectedKeywords exception test`() {
        every { getAdKeywordUseCase.executeQuerySafeMode(any(), captureLambda()) } answers {
            lambda<(Throwable) -> Unit>().invoke(Throwable())
        }
        var successCalled = false
        viewModel.getAdKeyword("", 0, "", { _, _ -> successCalled = true }, "")
        assertTrue(!successCalled)
    }

    @Test
    fun `selectedKeywords should be empty if cursor isBlank in getSelectedKeywords`() {
        every { getAdKeywordUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            lambda<(GetKeywordResponse) -> Unit>().invoke(mockk(relaxed = true))
        }
        viewModel.getAdKeyword("", 0, "", { _, _ -> }, "")

        assertTrue(viewModel.getSelectedKeywords().isEmpty())
    }

    @Test
    fun `getSelectedKeywords selectedKeywords should have keywords of type - type_phrase or type_exact when keywortype==keywordPositive`() {
        val obj: GetKeywordResponse = mockk(relaxed = true)

        every { obj.topAdsListKeyword.data.pagination.cursor } returns ""
        every { obj.topAdsListKeyword.data.keywords } returns listOf(
            GetKeywordResponse.KeywordsItem(type = Constants.KEYWORD_TYPE_PHRASE),
            GetKeywordResponse.KeywordsItem(type = Constants.KEYWORD_TYPE_EXACT),
            GetKeywordResponse.KeywordsItem(type = Constants.KEYWORD_TYPE_NEGATIVE_EXACT),
        )
        every { getAdKeywordUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            lambda<(GetKeywordResponse) -> Unit>().invoke(obj)
        }
        viewModel.getAdKeyword("", 0, "", { _, _ -> }, "keywordPositive")

        assertTrue(viewModel.getSelectedKeywords().size == 2)
    }

    @Test
    fun `getSelectedKeywords selectedKeywords should have keywords of type - type_negative_phrase or type_negative_exact when keywortype is not keywordPositive`() {
        val obj: GetKeywordResponse = mockk(relaxed = true)

        every { obj.topAdsListKeyword.data.pagination.cursor } returns ""
        every { obj.topAdsListKeyword.data.keywords } returns listOf(
            GetKeywordResponse.KeywordsItem(type = Constants.KEYWORD_TYPE_EXACT),
            GetKeywordResponse.KeywordsItem(type = Constants.KEYWORD_TYPE_NEGATIVE_EXACT),
            GetKeywordResponse.KeywordsItem(type = Constants.KEYWORD_TYPE_NEGATIVE_PHRASE),
        )
        every { getAdKeywordUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            lambda<(GetKeywordResponse) -> Unit>().invoke(obj)
        }
        viewModel.getAdKeyword("", 0, "", { _, _ -> }, ".")

        assertTrue(viewModel.getSelectedKeywords().size == 2)
    }

    private fun `getAdKeyword on success base test`(): List<GetKeywordResponse.KeywordsItem>? {
        val obj: GetKeywordResponse = mockk(relaxed = true)

        every { obj.topAdsListKeyword.data.keywords } returns listOf(
            GetKeywordResponse.KeywordsItem(type = Constants.KEYWORD_TYPE_PHRASE, groupId = "9"),
            GetKeywordResponse.KeywordsItem(type = Constants.KEYWORD_TYPE_EXACT),
            GetKeywordResponse.KeywordsItem(type = Constants.KEYWORD_TYPE_NEGATIVE_EXACT),
        )
        every { getAdKeywordUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            lambda<(GetKeywordResponse) -> Unit>().invoke(obj)
        }

        var successList: List<GetKeywordResponse.KeywordsItem>? = null
        viewModel.getAdKeyword("", 0, "", { l, _ -> successList = l }, "keywordPositive")

        return successList
    }

    @Test
    fun `getAdKeyword on success ,list is copy`() {
        val list = `getAdKeyword on success base test`()
        assertTrue(list !== viewModel.getSelectedKeywords())
    }

    @Test
    fun `getAdKeyword on success list have same content of selected keyword`() {
        val list = `getAdKeyword on success base test`()
        assertTrue(list == viewModel.getSelectedKeywords())
    }

    @Test
    fun `getAdKeyword on success cursor equal to response`() {
        val obj: GetKeywordResponse = mockk(relaxed = true)

        every { obj.topAdsListKeyword.data.pagination.cursor } returns "21"
        every { getAdKeywordUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            lambda<(GetKeywordResponse) -> Unit>().invoke(obj)
        }

        var cursor = ""
        viewModel.getAdKeyword("", 0, "", { _, c -> cursor = c }, "keywordPositive")

        assertEquals(cursor, "21")
    }

    @Test
    fun `getAdKeyword param test`() {
        every { getAdKeywordUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            lambda<(GetKeywordResponse) -> Unit>().invoke(mockk(relaxed = true))
        }
        viewModel.getAdKeyword("22", 11, "c", { _, c -> }, "keywordPositive")

        verify { getAdKeywordUseCase.setParams(11,"c", "22", EDIT_HEADLINE_PAGE, keywordStatus =  listOf(1,3)) }
    }
}