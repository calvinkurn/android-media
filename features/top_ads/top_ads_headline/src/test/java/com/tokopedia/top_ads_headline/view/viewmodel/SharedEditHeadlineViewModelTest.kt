package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.tokopedia.top_ads_headline.Constants
import com.tokopedia.top_ads_headline.data.HeadlineAdStepperModel
import com.tokopedia.topads.common.data.response.*
import com.tokopedia.topads.common.data.response.nongroupItem.NonGroupResponse
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsGetGroupProductDataUseCase
import com.tokopedia.topads.common.domain.model.createheadline.TopAdsManageHeadlineInput
import com.tokopedia.topads.common.domain.usecase.TopAdsGetPromoUseCase
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SharedEditHeadlineViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = UnconfinedTestRule()

    private val topAdsGetGroupProductUseCase: TopAdsGetGroupProductDataUseCase =
        mockk(relaxed = true)
    private val topAdsGetPromoUseCase: TopAdsGetPromoUseCase = mockk(relaxed = true)
    private val bidInfoUseCase: BidInfoUseCase = mockk(relaxed = true)
    private val viewModel = spyk(
        SharedEditHeadlineViewModel(
            topAdsGetGroupProductUseCase,
            topAdsGetPromoUseCase, bidInfoUseCase
        )
    )

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
            WithoutGroupDataItem()
        )
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
            topAdsGetGroupProductUseCase.setParams(
                any(), any(), any(), any(),
                any(), any(), any(), any()
            )
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
        val nonGroupResponse = NonGroupResponse(
            topadsDashboardGroupProducts = NonGroupResponse.TopadsDashboardGroupProducts(
                data = listOf(
                    WithoutGroupDataItem(
                        adId = "123",
                    )
                )
            )
        )
        coEvery { topAdsGetGroupProductUseCase.execute(any()) } answers {
            nonGroupResponse
        }
        val singleAd = SingleAd(
            cpmDetails = listOf(
                CpmDetail(
                    product = listOf(
                        TopAdsProductModel(
                            productID = "1",
                            departmentID = "1",
                        )
                    )
                )
            )
        )
        val singleAdInFo = SingleAdInFo(
            topAdsGetPromo = TopAdsGetPromo(
                data = listOf(singleAd)
            )
        )
        coEvery { topAdsGetPromoUseCase.execute(captureLambda(), any()) } answers {
            firstArg<(SingleAdInFo) -> Unit>().invoke(singleAdInFo)
        }
        viewModel.getHeadlineAdId("1", "2") {}
        coVerify { topAdsGetGroupProductUseCase.execute(any()) }
        coVerify { topAdsGetPromoUseCase.execute(any(), any()) }
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
    fun `getHeadlineAdId when calls getHeadlineAdDetail,should invoke throwable`() {
        every { topAdsGetPromoUseCase.execute(captureLambda(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }
        var isThrowableCalled = false
        baseGetHeadlineAdDataNotEmpty {
            isThrowableCalled = true
        }
        assertTrue(isThrowableCalled)
    }

    @Test
    fun `getBidInfoDetail - bidinfousecase exception test`() {
        every { bidInfoUseCase.executeQuerySafeMode(any(), captureLambda()) } answers {
            lambda<(Throwable) -> Unit>().invoke(throwable)
        }
        baseInvokeBidInfoDefault()
        verify { throwable.printStackTrace() }
    }

    @Test
    fun `getEditHeadlineAdLiveData should return null`() {
        assertEquals(null, viewModel.getEditHeadlineAdLiveData().value)
    }

    @Test
    fun `saveProductData success`() {
        val stepperModel = HeadlineAdStepperModel().apply {
            slogan = "Test Slogan"
            selectedProductIds = arrayListOf("1", "2", "3")
            adOperations = arrayListOf(TopAdsManageHeadlineInput.Operation.Group.AdOperation())
        }
        val _editHeadlineAdLiveData = viewModel.javaClass.getDeclaredField("editHeadlineAdLiveData")
        _editHeadlineAdLiveData.isAccessible = true
        val editHeadlineAdLiveData =
            _editHeadlineAdLiveData.get(viewModel) as MutableLiveData<HeadlineAdStepperModel>
        editHeadlineAdLiveData.value = stepperModel

        val response = ResponseBidInfo.Result(
            topadsBidInfo = TopadsBidInfo(
                data = listOf(
                    TopadsBidInfo.DataItem(
                        maxBid = "1000.0",
                        minBid = "500.0"
                    )
                )
            )
        )
        coEvery { bidInfoUseCase.executeQuerySafeMode(any(), any()) } answers {
            firstArg<(ResponseBidInfo.Result) -> Unit>().invoke(response)
        }
        viewModel.saveProductData(stepperModel)
        stepperModel.maxBid = "1000.0"
        stepperModel.minBid = "500.0"
        assertEquals(stepperModel, editHeadlineAdLiveData.value)
    }

    @Test
    fun `saveProductData failure`() {
        val stepperModel = HeadlineAdStepperModel().apply {
            slogan = "Test Slogan"
            selectedProductIds = arrayListOf("1", "2", "3")
            adOperations = arrayListOf(TopAdsManageHeadlineInput.Operation.Group.AdOperation())
        }
        val _editHeadlineAdLiveData = viewModel.javaClass.getDeclaredField("editHeadlineAdLiveData")
        _editHeadlineAdLiveData.isAccessible = true
        val editHeadlineAdLiveData =
            _editHeadlineAdLiveData.get(viewModel) as MutableLiveData<HeadlineAdStepperModel>
        editHeadlineAdLiveData.value = stepperModel

        val response: Throwable = mockk(relaxed = true)
        coEvery { bidInfoUseCase.executeQuerySafeMode(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(response)
        }
        viewModel.saveProductData(stepperModel)
        verify { response.printStackTrace() }
    }

    @Test
    fun `saveKeywordOperation success`() {
        val stepperModel = HeadlineAdStepperModel()
        val _editHeadlineAdLiveData = viewModel.javaClass.getDeclaredField("editHeadlineAdLiveData")
        _editHeadlineAdLiveData.isAccessible = true
        val editHeadlineAdLiveData =
            _editHeadlineAdLiveData.get(viewModel) as MutableLiveData<HeadlineAdStepperModel>
        editHeadlineAdLiveData.value = stepperModel
        editHeadlineAdLiveData.value?.let {
            it.adBidPrice = stepperModel.adBidPrice
            it.keywordOperations = stepperModel.keywordOperations
        }
        viewModel.saveKeywordOperation(stepperModel)
        assertEquals(stepperModel, editHeadlineAdLiveData.value)
    }

    @Test
    fun `saveOtherDetails success`() {
        val stepperModel = HeadlineAdStepperModel()
        val _editHeadlineAdLiveData = viewModel.javaClass.getDeclaredField("editHeadlineAdLiveData")
        _editHeadlineAdLiveData.isAccessible = true
        val editHeadlineAdLiveData =
            _editHeadlineAdLiveData.get(viewModel) as MutableLiveData<HeadlineAdStepperModel>
        editHeadlineAdLiveData.value = stepperModel
        editHeadlineAdLiveData.value?.let {
            it.startDate = stepperModel.startDate
            it.endDate = stepperModel.endDate
            it.groupName = stepperModel.groupName
            it.dailyBudget = stepperModel.dailyBudget
            it.adOperations.firstOrNull()?.ad?.title = stepperModel.groupName
        }
        viewModel.saveOtherDetails(stepperModel)
        assertEquals(stepperModel, editHeadlineAdLiveData.value)
    }

}
