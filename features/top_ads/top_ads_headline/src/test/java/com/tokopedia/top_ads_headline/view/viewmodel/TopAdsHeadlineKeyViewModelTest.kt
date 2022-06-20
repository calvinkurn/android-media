package com.tokopedia.top_ads_headline.view.viewmodel

import com.tokopedia.topads.common.data.response.*
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.usecase.SuggestionKeywordUseCase
import io.mockk.*

import org.junit.After
import org.junit.Assert
import org.junit.Test

class TopAdsHeadlineKeyViewModelTest {

    private val bidInfoUseCase: BidInfoUseCase = mockk(relaxed = true)
    private val suggestionKeywordUseCase: SuggestionKeywordUseCase = mockk(relaxed = true)
    private val viewModel =
        spyk(TopAdsHeadlineKeyViewModel(bidInfoUseCase, suggestionKeywordUseCase))

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getSuggestionKeyword success,non empty data test`() {
        val fake = mockk<KeywordSuggestionResponse.Result>(relaxed = true)
        val expected = listOf(KeywordData())
        var actual: List<KeywordData>? = null

        every { fake.topAdsGetKeywordSuggestionV3.data } returns expected
        every { suggestionKeywordUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            lambda<(KeywordSuggestionResponse.Result) -> Unit>().invoke(fake)
        }

        viewModel.getSuggestionKeyword("", 0, { actual = it }, {})
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `getSuggestionKeyword empty data test`() {
        val fake = mockk<KeywordSuggestionResponse.Result>(relaxed = true)
        var actual: List<KeywordData>? = null

        every { fake.topAdsGetKeywordSuggestionV3.data } returns emptyList()
        every { suggestionKeywordUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            lambda<(KeywordSuggestionResponse.Result) -> Unit>().invoke(fake)
        }

        viewModel.getSuggestionKeyword("", 0, { actual = it }, {})
        Assert.assertEquals(null, actual)
    }

    @Test
    fun `getSuggestionKeyword exception test`() {
        every { suggestionKeywordUseCase.executeQuerySafeMode(any(), captureLambda()) } answers {
            lambda<(Throwable) -> Unit>().invoke(Throwable())
        }
        var actual: List<KeywordData>? = null
        viewModel.getSuggestionKeyword("", 0, { actual = it }, {})
        Assert.assertEquals(null, actual)
    }

    @Test
    fun `getBidInfo success,non empty data test`() {
        val fake = mockk<ResponseBidInfo.Result>(relaxed = true)
        val expected = listOf(TopadsBidInfo.DataItem())
        var actual: List<TopadsBidInfo.DataItem>? = null

        every { fake.topadsBidInfo.data } returns expected
        every { bidInfoUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            lambda<(ResponseBidInfo.Result) -> Unit>().invoke(fake)
        }

        viewModel.getBidInfo(emptyList(), { actual = it }, {})
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `getBidInfo empty data test`() {
        val fake = mockk<ResponseBidInfo.Result>(relaxed = true)
        val expected = listOf(TopadsBidInfo.DataItem())
        var actual: List<TopadsBidInfo.DataItem>? = null

        every { fake.topadsBidInfo.data } returns emptyList()
        every { bidInfoUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            lambda<(ResponseBidInfo.Result) -> Unit>().invoke(fake)
        }

        viewModel.getBidInfo(emptyList(), { actual = it }, {})
        Assert.assertEquals(null, actual)
    }


    @Test
    fun `getBidInfo exception test`() {
        every { bidInfoUseCase.executeQuerySafeMode(any(), captureLambda()) } answers {
            lambda<(Throwable) -> Unit>().invoke(Throwable())
        }
        var actual: List<TopadsBidInfo.DataItem>? = null
        viewModel.getBidInfo(emptyList(), { actual = it }, {})
        Assert.assertEquals(null, actual)
    }
}