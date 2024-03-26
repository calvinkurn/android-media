package com.tokopedia.topads.edit.viewmodel

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.common.data.model.TotalProductKeyResponse
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.common.data.response.GetAdProductResponse
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.common.data.response.ImpressionPredictionResponse
import com.tokopedia.topads.common.data.response.ResponseBidInfo
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.data.response.TopAdsGetBidSuggestionResponse
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.usecase.GetAdKeywordUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsCreateUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetBidSuggestionByProductIDsUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetTotalAdsAndKeywordsUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsImpressionPredictionBrowseUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsImpressionPredictionSearchUseCase
import com.tokopedia.topads.edit.usecase.GetAdsUseCase
import com.tokopedia.topads.edit.usecase.GroupInfoUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi

class EditAdGroupViewModelTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher: CoroutineDispatchers = CoroutineTestDispatchersProvider
    private val groupInfoUseCase: GroupInfoUseCase = mockk(relaxed = true)
    private val getTotalAdsAndKeywordsUseCase: TopAdsGetTotalAdsAndKeywordsUseCase =
        mockk(relaxed = true)
    private val validateNameAdGroupUseCase: TopAdsGroupValidateNameUseCase = mockk(relaxed = true)
    private val topAdsImpressionPredictionSearchUseCase: TopAdsImpressionPredictionSearchUseCase =
        mockk(relaxed = true)
    private val topAdsImpressionPredictionBrowseUseCase: TopAdsImpressionPredictionBrowseUseCase =
        mockk(relaxed = true)
    private val topAdsGetBidSuggestionByProductIDsUseCase: TopAdsGetBidSuggestionByProductIDsUseCase =
        mockk(relaxed = true)
    private val getAdKeywordUseCase: GetAdKeywordUseCase = mockk(relaxed = true)
    private val getAdsUseCase: GetAdsUseCase = mockk(relaxed = true)
    private val topAdsCreateUseCase: TopAdsCreateUseCase = mockk(relaxed = true)
    private val bidInfoUseCaseDefault: BidInfoUseCase = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private lateinit var viewModel: EditAdGroupViewModel

    @Before
    fun setUp() {
        viewModel = EditAdGroupViewModel(
            testDispatcher,
            groupInfoUseCase,
            getTotalAdsAndKeywordsUseCase,
            validateNameAdGroupUseCase,
            topAdsImpressionPredictionSearchUseCase,
            topAdsImpressionPredictionBrowseUseCase,
            topAdsGetBidSuggestionByProductIDsUseCase,
            getAdKeywordUseCase,
            getAdsUseCase,
            topAdsCreateUseCase,
            bidInfoUseCaseDefault,
            userSession
        )
    }

    @Test
    fun `getGroupInfo success`() {
        val data = GroupInfoResponse(
            topAdsGetPromoGroup = GroupInfoResponse.TopAdsGetPromoGroup(
                data = GroupInfoResponse.TopAdsGetPromoGroup.Data(
                    groupName = String.EMPTY,
                    dailyBudget = Float.ZERO,
                    shopId = String.EMPTY,
                    groupId = String.EMPTY,
                    status = String.EMPTY,
                    groupTotal = String.EMPTY,
                    bidSettings = listOf(),
                    strategies = listOf()
                )
            )
        )
        var actual: GroupInfoResponse.TopAdsGetPromoGroup.Data? = null
        coEvery {
            groupInfoUseCase.executeQuerySafeMode(any(), any())
        } answers {
            firstArg<(GroupInfoResponse) -> Unit>().invoke(data)
        }
        viewModel.getGroupInfo(String.EMPTY) {
            actual = it
        }
        assertEquals(data.topAdsGetPromoGroup?.data, actual)
    }

    @Test
    fun `getGroupInfo failure`() {
        val throwable = Throwable()
        coEvery {
            groupInfoUseCase.executeQuerySafeMode(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
        viewModel.getGroupInfo(String.EMPTY) {
        }
        coVerify { groupInfoUseCase.executeQuerySafeMode(any(), any()) }
    }

    @Test
    fun `getTotalAdsAndKeywordsCount failure`() {
        val throwable = Throwable()
        coEvery {
            getTotalAdsAndKeywordsUseCase(any())
        } answers {
            throw throwable
        }
        viewModel.getTotalAdsAndKeywordsCount(String.EMPTY)
        assertTrue(viewModel.adsKeywordCount.value is Fail)
    }

    @Test
    fun `getTotalAdsAndKeywordsCount success`() {
        val count = TotalProductKeyResponse()
        coEvery {
            getTotalAdsAndKeywordsUseCase(any())
        } answers {
            count
        }
        viewModel.getTotalAdsAndKeywordsCount(String.EMPTY)
        assertTrue(viewModel.adsKeywordCount.value is Success)
    }

    @Test
    fun `validateGroup failure`() {
        val throwable = Throwable("error")
        var actual: String? = null
        coEvery {
            validateNameAdGroupUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
        viewModel.validateGroup(String.EMPTY, {}, {
            actual = it
        })
        assertEquals(throwable.message, actual)
    }

    @Test
    fun `validateGroup success empty error`() {
        val data = ResponseGroupValidateName(
            topAdsGroupValidateName = ResponseGroupValidateName.TopAdsGroupValidateNameV2(
                errors = listOf()
            )
        )
        var actual: ResponseGroupValidateName.TopAdsGroupValidateNameV2? = null

        coEvery {
            validateNameAdGroupUseCase.execute(any(), any())
        } answers {
            firstArg<(ResponseGroupValidateName) -> Unit>().invoke(data)
        }

        viewModel.validateGroup(String.EMPTY, { response ->
            actual = response
        }, {})

        assertEquals(data.topAdsGroupValidateName, actual)
    }

    @Test
    fun `validateGroup success with error`() {
        val errorMessage = "Some error"
        val data = ResponseGroupValidateName(
            topAdsGroupValidateName = ResponseGroupValidateName.TopAdsGroupValidateNameV2(
                errors = listOf(com.tokopedia.topads.common.data.response.Error().apply {
                    detail = errorMessage
                })
            )
        )
        var actual: String? = null

        coEvery {
            validateNameAdGroupUseCase.execute(any(), any())
        } answers {
            firstArg<(ResponseGroupValidateName) -> Unit>().invoke(data)
        }

        viewModel.validateGroup(String.EMPTY, {}, { response ->
            actual = response
        })
        assertEquals(errorMessage, actual)
    }

    @Test
    fun `getAds failure`() {
        val throwable = Throwable()
        coEvery {
            getAdsUseCase.executeQuerySafeMode(any(), any())
        } answers {
            throw throwable
        }
        viewModel.getAds(Int.ZERO, String.EMPTY, String.EMPTY)
        assertTrue(viewModel.adsData.value is Fail)
    }

    @Test
    fun `getAds success with throwable`() {
        val data = Throwable()
        coEvery {
            getAdsUseCase.executeQuerySafeMode(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(data)
        }
        viewModel.getAds(Int.ZERO, String.EMPTY, String.EMPTY)
        assertTrue(viewModel.adsData.value is Fail)
    }

    @Test
    fun `getAds success with data`() {
        val data = GetAdProductResponse()
        coEvery {
            getAdsUseCase.executeQuerySafeMode(any(), any())
        } answers {
            firstArg<(GetAdProductResponse) -> Unit>().invoke(data)
        }
        viewModel.getAds(Int.ZERO, String.EMPTY, String.EMPTY)
        assertTrue(viewModel.adsData.value is Success)
    }

    @Test
    fun `getBrowsePerformanceData failure`() {
        val throwable = Throwable()
        coEvery {
            topAdsImpressionPredictionBrowseUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } answers {
            throw throwable
        }
        viewModel.getBrowsePerformanceData(listOf(), Float.ZERO, Float.ZERO, Float.ZERO)
        assertTrue(viewModel.browsePerformanceData.value is Fail)
    }

    @Test
    fun `getBrowsePerformanceData success`() {
        val data = Success(
            ImpressionPredictionResponse(
                umpGetImpressionPrediction = ImpressionPredictionResponse.UmpGetImpressionPrediction(
                    impressionPredictionData = ImpressionPredictionResponse.UmpGetImpressionPrediction.ImpressionPredictionData(
                        impression = ImpressionPredictionResponse.UmpGetImpressionPrediction.ImpressionPredictionData.Impression(
                            finalImpression = Int.ZERO,
                            increment = Int.ZERO,
                            oldImpression = Int.ZERO
                        )
                    ),
                    error = ImpressionPredictionResponse.UmpGetImpressionPrediction.Error(
                        title = String.EMPTY
                    )
                )
            )
        )
        coEvery {
            topAdsImpressionPredictionBrowseUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } answers {
            data
        }
        viewModel.getBrowsePerformanceData(listOf(), Float.ZERO, Float.ZERO, Float.ZERO)
        assertTrue(viewModel.browsePerformanceData.value is Success)
    }


    @Test
    fun `getPerformanceData success`() {
        val adsImpression = Success(
            ImpressionPredictionResponse(
                umpGetImpressionPrediction = ImpressionPredictionResponse.UmpGetImpressionPrediction(
                    impressionPredictionData = ImpressionPredictionResponse.UmpGetImpressionPrediction.ImpressionPredictionData(
                        impression = ImpressionPredictionResponse.UmpGetImpressionPrediction.ImpressionPredictionData.Impression(
                            finalImpression = Int.ZERO,
                            increment = Int.ZERO,
                            oldImpression = Int.ZERO
                        )
                    ),
                    error = ImpressionPredictionResponse.UmpGetImpressionPrediction.Error(
                        title = ""
                    )
                )
            )
        )

        coEvery {
            topAdsImpressionPredictionSearchUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns adsImpression

        coEvery {
            topAdsImpressionPredictionBrowseUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns adsImpression

        viewModel.getPerformanceData(listOf("1", "2"), mutableListOf(0f, 1f, 2f), Float.ZERO)

        coVerify {
            topAdsImpressionPredictionSearchUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any()
            )
            topAdsImpressionPredictionBrowseUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        }
        Assert.assertNotNull(viewModel.performanceData.value)
    }

    @Test
    fun `getPerformanceData failure`() {
        val result = Fail(Throwable())
        coEvery {
            topAdsImpressionPredictionSearchUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns result

        coEvery {
            topAdsImpressionPredictionBrowseUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns result

        viewModel.getPerformanceData(listOf("1", "2"), mutableListOf(0f, 1f, 2f), Float.ZERO)

        coVerify {
            topAdsImpressionPredictionSearchUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any()
            )
            topAdsImpressionPredictionBrowseUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        }

    }

    @Test
    fun `topAdsCreated failure`() {
        val throwable = Throwable()
        coEvery {
            topAdsCreateUseCase.execute(any())
        } answers {
            throw throwable
        }
        viewModel.topAdsCreated(
            dataPro = Bundle(),
            dataKey = hashMapOf(),
            dataGrp = hashMapOf(),
            onSuccess = {},
            onError = { _ -> }
        )
        coVerify { topAdsCreateUseCase.execute(any()) }
    }

    @Test
    fun `topAdsCreated success no error`() {
        val data = FinalAdResponse(
            topadsManageGroupAds = FinalAdResponse.TopadsManageGroupAds(
                keywordResponse = FinalAdResponse.TopadsManageGroupAds.KeywordResponse(
                    data = listOf(),
                    errors = null
                ),
                groupResponse = FinalAdResponse.TopadsManageGroupAds.GroupResponse(
                    data = FinalAdResponse.TopadsManageGroupAds.TopadsGroupResponseV2(
                        id = String.EMPTY,
                        resourceUrl = ""
                    ),
                    errors = null
                )
            )
        )
        var isSuccess = false
        coEvery {
            topAdsCreateUseCase.execute(any())
        } answers {
            data
        }
        viewModel.topAdsCreated(
            dataPro = Bundle(),
            dataKey = hashMapOf(),
            dataGrp = hashMapOf(),
            onSuccess = {
                isSuccess = true
            },
            onError = { _ -> }
        )
        assertTrue(isSuccess)
    }

    @Test
    fun `topAdsCreated success with errors`() {
        val data = FinalAdResponse(
            topadsManageGroupAds = FinalAdResponse.TopadsManageGroupAds(
                keywordResponse = FinalAdResponse.TopadsManageGroupAds.KeywordResponse(
                    data = listOf(),
                    errors = listOf(FinalAdResponse.TopadsManageGroupAds.ErrorsItem(detail = String.EMPTY))
                ),
                groupResponse = FinalAdResponse.TopadsManageGroupAds.GroupResponse(
                    data = FinalAdResponse.TopadsManageGroupAds.TopadsGroupResponseV2(
                        id = String.EMPTY,
                        resourceUrl = String.EMPTY
                    ),
                    errors = listOf(
                        FinalAdResponse.TopadsManageGroupAds.ErrorsItem(
                            detail = String.EMPTY
                        )
                    )
                )
            )
        )
        val errorData = String.EMPTY + String.EMPTY
        var errorActual: String? = null
        coEvery {
            topAdsCreateUseCase.execute(any())
        } answers {
            data
        }
        viewModel.topAdsCreated(
            dataPro = Bundle(),
            dataKey = hashMapOf(),
            dataGrp = hashMapOf(),
            onSuccess = {},
            onError = {
                errorActual = it
            }
        )
        assertEquals(errorData, errorActual)
    }

    @Test
    fun `getProductBid failure`() {
        coEvery {
            topAdsGetBidSuggestionByProductIDsUseCase.invoke(any(), any())
        } answers {
            throw Throwable()
        }
        viewModel.getProductBid(listOf())
        assertTrue(viewModel.bidProductData.value is Fail)
    }

    @Test
    fun `getProductBid success`() {
        val data = Success(
            TopAdsGetBidSuggestionResponse(
                topAdsGetBidSuggestionByProductIDs = TopAdsGetBidSuggestionResponse.TopAdsGetBidSuggestionByProductIDs(
                    bidData = TopAdsGetBidSuggestionResponse.TopAdsGetBidSuggestionByProductIDs.BidData(
                        bidSuggestion = Int.ZERO
                    ),
                    error = TopAdsGetBidSuggestionResponse.TopAdsGetBidSuggestionByProductIDs.Error(
                        code = String.EMPTY,
                        detail = String.EMPTY,
                        title = String.EMPTY
                    )
                )
            )
        )
        coEvery {
            topAdsGetBidSuggestionByProductIDsUseCase.invoke(any(), any())
        } answers {
            data
        }
        viewModel.getProductBid(listOf())
        assertTrue(viewModel.bidProductData.value is Success)
    }

    @Test
    fun `getAdKeyword failure`() {
        coEvery {
            getAdKeywordUseCase.executeQuerySafeMode(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }
        viewModel.getAdKeyword(Int.ZERO, String.EMPTY) { _, _ -> }
        coVerify { getAdKeywordUseCase.executeQuerySafeMode(any(), any()) }
    }

    @Test
    fun `getAdKeyword success`() {
        val data = GetKeywordResponse()
        val keyword = data.topAdsListKeyword.data.keywords
        val cursor = data.topAdsListKeyword.data.pagination.cursor
        var keywordActual: List<GetKeywordResponse.KeywordsItem>? = null
        var cursorActual: String? = null
        coEvery {
            getAdKeywordUseCase.executeQuerySafeMode(any(), any())
        } answers {
            firstArg<(GetKeywordResponse) -> Unit>().invoke(data)
        }
        viewModel.getAdKeyword(Int.ZERO, String.EMPTY) { key, cur ->
            keywordActual = key
            cursorActual = cur
        }
        assertEquals(keyword, keywordActual)
        assertEquals(cursor, cursorActual)
    }

    @Test
    fun `getBidInfoDefault failure`() {
        coEvery {
            bidInfoUseCaseDefault.executeQuerySafeMode(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }
        viewModel.getBidInfoDefault(listOf()) { _ -> }
        coVerify { bidInfoUseCaseDefault.executeQuerySafeMode(any(), any()) }
    }

    @Test
    fun `getBidInfoDefault success`() {
        val data = ResponseBidInfo.Result()
        val dataItem = data.topadsBidInfo.data
        var actual: List<TopadsBidInfo.DataItem>? = null
        coEvery {
            bidInfoUseCaseDefault.executeQuerySafeMode(any(), any())
        } answers {
            firstArg<(ResponseBidInfo.Result) -> Unit>().invoke(data)
        }
        viewModel.getBidInfoDefault(listOf()) {
            actual = it
        }
        assertEquals(dataItem, actual)
    }

    @Test
    fun `get searchPerformanceData`(){
        assertNull(viewModel.searchPerformanceData.value)
    }
}
