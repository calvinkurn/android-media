package com.tokopedia.top_ads_headline.view.viewmodel

import com.tokopedia.top_ads_headline.Constants
import com.tokopedia.topads.common.data.response.*
import com.tokopedia.topads.common.data.response.nongroupItem.NonGroupResponse
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsGetGroupProductDataUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetPromoUseCase
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SharedEditHeadlineViewModelTest {

    private val topAdsGetGroupProductUseCase: TopAdsGetGroupProductDataUseCase =
        mockk(relaxed = true)
    private val topAdsGetPromoUseCase: TopAdsGetPromoUseCase = mockk(relaxed = true)
    private val bidInfoUseCase: BidInfoUseCase = mockk(relaxed = true)
    private val viewModel = spyk(SharedEditHeadlineViewModel(topAdsGetGroupProductUseCase,
        topAdsGetPromoUseCase, bidInfoUseCase))

    private val throwable = spyk(Throwable())
    private val nonGroupResponse: NonGroupResponse = mockk(relaxed = true)
    private val singleAdInFo: SingleAdInFo = mockk(relaxed = true)

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getHeadlineAdId exception test`() {
        val exc = spyk(Throwable())
        coEvery { topAdsGetGroupProductUseCase.execute(any()) } throws exc
        viewModel.getHeadlineAdId("1", "2") {}

        verify { exc.printStackTrace() }
    }

    @Test
    fun `getHeadlineAd should execute topAdsGetGroupProductUseCase`() {
        viewModel.getHeadlineAdId("1", "2", {})
        coVerify { topAdsGetGroupProductUseCase.execute(any()) }
    }

    private fun baseGetHeadlineAdDataNotEmpty(onError: ((String) -> Unit)? = null) {
        every { nonGroupResponse.topadsDashboardGroupProducts.data } returns listOf(
            WithoutGroupDataItem())
        coEvery { topAdsGetGroupProductUseCase.execute(any()) } returns nonGroupResponse
        viewModel.getHeadlineAdId("1", "2") { onError?.invoke(it) }
    }

    @Test
    fun `getHeadlineAd executes topAdsGetPromoUseCase when data is not empty`() {
        baseGetHeadlineAdDataNotEmpty()
        verify { topAdsGetPromoUseCase.setParams(any(), any()) }
        verify { topAdsGetPromoUseCase.execute(any(), any()) }
    }

    @Test
    fun `getHeadlineAdId when calls getHeadlineAdDetail, test topAdsGetPromoUseCase exception test`() {
        every {
            topAdsGetGroupProductUseCase.setParams(any(), any(), any(), any(),
                any(), any(), any(), any())
        } throws throwable
        baseGetHeadlineAdDataNotEmpty()
        verify { throwable.printStackTrace() }
    }

    private fun baseInvokeBidInfoDefault() {
        every { singleAdInFo.topAdsGetPromo.data } returns listOf(SingleAd())
        every { topAdsGetPromoUseCase.execute(captureLambda(), any()) } answers {
            lambda<(SingleAdInFo) -> Unit>().invoke(singleAdInFo)
        }
        baseGetHeadlineAdDataNotEmpty()
    }

    @Test
    fun `getHeadlineAdId when calls getHeadlineAdDetail,should invoke getBidInfoDetail if data not empty`() {
        baseInvokeBidInfoDefault()
        verify { bidInfoUseCase.setParams(any(), Constants.HEADLINE, Constants.EDIT_HEADLINE_PAGE) }
        verify { bidInfoUseCase.executeQuerySafeMode(any(), any()) }
    }

    @Test
    fun `getHeadlineAdId when calls getHeadlineAdDetail,should invoke error when data is empty`() {
        val expected = "me_err"
        every { singleAdInFo.topAdsGetPromo.errors } returns listOf(Error().apply {
            detail = expected
        })
        every { topAdsGetPromoUseCase.execute(captureLambda(), any()) } answers {
            lambda<(SingleAdInFo) -> Unit>().invoke(singleAdInFo)
        }
        var err = ""
        baseGetHeadlineAdDataNotEmpty {
            err = it
        }
        assertEquals(expected, err)
    }

    @Test
    fun `getBidInfoDetail - bidinfousecase exception test`() {
        every { bidInfoUseCase.executeQuerySafeMode(any(), captureLambda()) } answers {
            lambda<(Throwable) -> Unit>().invoke(throwable)
        }
        baseInvokeBidInfoDefault()
        verify { throwable.printStackTrace() }
    }
}
