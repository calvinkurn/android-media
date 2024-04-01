package com.tokopedia.topads.dashboard.recommendation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.common.data.response.Deposit
import com.tokopedia.topads.common.data.response.Error
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.common.data.response.ResponseBidInfo
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsCreateUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import com.tokopedia.topads.dashboard.data.model.ProductRecommendationModel
import com.tokopedia.topads.dashboard.recommendation.data.mapper.ProductRecommendationMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopadsProductListState
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsGetGroupDetailListUseCase
import com.tokopedia.topads.dashboard.recommendation.usecase.TopadsGetProductRecommendationV2Usecase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ProductRecommendationViewModelTest {

    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private val bidInfoUseCase: BidInfoUseCase = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val topadsGetProductRecommendationV2Usecase: TopadsGetProductRecommendationV2Usecase =
        mockk(relaxed = true)
    private val mapper: ProductRecommendationMapper = mockk(relaxed = true)
    private val topAdsGroupValidateNameUseCase: TopAdsGroupValidateNameUseCase =
        mockk(relaxed = true)
    private val topAdsCreateUseCase: TopAdsCreateUseCase = mockk(relaxed = true)
    private val topAdsGetGroupDetailListUseCase: TopAdsGetGroupDetailListUseCase =
        mockk(relaxed = true)
    private val topAdsGetDepositUseCase: TopAdsGetDepositUseCase = mockk(relaxed = true)
    private lateinit var viewModel: ProductRecommendationViewModel

    @Before
    fun setup() {
        viewModel = ProductRecommendationViewModel(
            rule.dispatchers,
            bidInfoUseCase,
            userSession,
            topadsGetProductRecommendationV2Usecase,
            mapper,
            topAdsGroupValidateNameUseCase,
            topAdsCreateUseCase,
            topAdsGetGroupDetailListUseCase,
            topAdsGetDepositUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `loadProductList failure`() {
        coEvery {
            topadsGetProductRecommendationV2Usecase(userSession.shopId)
        } answers {
            throw Throwable()
        }
        viewModel.loadProductList()
        assertTrue(viewModel.productItemsLiveData.value is TopadsProductListState.Fail)
    }

    @Test
    fun `loadProductList success, no errors`() {
        val data = ProductRecommendationModel()
        coEvery {
            topadsGetProductRecommendationV2Usecase(userSession.shopId)
        } answers {
            data
        }
        viewModel.loadProductList()
        assertTrue(viewModel.productItemsLiveData.value is TopadsProductListState.Success)
    }

    @Test
    fun `loadProductList success, with errors`() {
        val data = listOf(Error())
        coEvery {
            topadsGetProductRecommendationV2Usecase(userSession.shopId).topadsGetProductRecommendation.errors
        } answers {
            data
        }
        viewModel.loadProductList()
        assertTrue(viewModel.productItemsLiveData.value is TopadsProductListState.Fail)
    }

    @Test
    fun `validateGroupName failure`() {
        coEvery {
            topAdsGroupValidateNameUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }
        viewModel.validateGroupName("test")
        assertTrue(viewModel.validateNameLiveData.value == null)
    }

    @Test
    fun `validateGroupName success`() {
        val data = ResponseGroupValidateName()
        coEvery {
            topAdsGroupValidateNameUseCase.execute(any(), any())
        } answers {
            firstArg<(ResponseGroupValidateName) -> Unit>().invoke(data)
        }
        viewModel.validateGroupName("test")
        assertTrue(viewModel.validateNameLiveData.value is ResponseGroupValidateName.TopAdsGroupValidateNameV2)
    }

    @Test
    fun `getBidInfo failure`() {
        coEvery {
            bidInfoUseCase.executeQuerySafeMode(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }
        viewModel.getBidInfo(listOf())
        assertTrue(viewModel.bidInfoLiveData.value == null)
    }

    @Test
    fun `getBidInfo success`() {
        val data = ResponseBidInfo.Result()
        coEvery {
            bidInfoUseCase.executeQuerySafeMode(any(), any())
        } answers {
            firstArg<(ResponseBidInfo.Result) -> Unit>().invoke(data)
        }
        viewModel.getBidInfo(listOf())
        assertTrue(viewModel.bidInfoLiveData.value is List<TopadsBidInfo.DataItem>)
    }

    @Test
    fun `getTopadsGroupList failure`() {
        coEvery {
            topAdsGetGroupDetailListUseCase.executeOnBackground(any(), any(), any())
        } answers {
            throw Throwable()
        }
        viewModel.getTopadsGroupList(String.EMPTY, Int.ZERO)
        assertTrue(viewModel.groupListLiveData.value is TopadsProductListState.Fail)
    }

    @Test
    fun `getTopadsGroupList success`() {
        val data: TopadsProductListState<List<GroupListUiModel>> =
            TopadsProductListState.Success(emptyList())
        coEvery {
            topAdsGetGroupDetailListUseCase.executeOnBackground(any(), any(), any())
        } answers {
            data
        }
        viewModel.getTopadsGroupList(String.EMPTY, Int.ZERO)
        assertTrue(viewModel.groupListLiveData.value is TopadsProductListState.Success)
    }

    @Test
    fun `topAdsCreateGroup failure`() {
        coEvery {
            topAdsCreateUseCase.execute(any())
        } answers {
            throw Throwable()
        }
        viewModel.topAdsCreateGroup(listOf(), String.EMPTY, Double.NaN)
        assertTrue(viewModel.createGroupLiveData.value is TopadsProductListState.Fail)
    }

    @Test
    fun `topAdsCreateGroup success no errors`() {
        val data = FinalAdResponse()
        coEvery {
            topAdsCreateUseCase.execute(any())
        } answers {
            data
        }
        viewModel.topAdsCreateGroup(listOf(), String.EMPTY, Double.NaN)
        assertTrue(viewModel.createGroupLiveData.value is TopadsProductListState.Success)
    }

    @Test
    fun `topAdsCreateGroup success with errors`() {
        val data = listOf(FinalAdResponse.TopadsManageGroupAds.ErrorsItem())
        coEvery {
            topAdsCreateUseCase.execute(any()).topadsManageGroupAds.groupResponse.errors
        } answers {
            data
        }
        coEvery {
            topAdsCreateUseCase.execute(any()).topadsManageGroupAds.keywordResponse.errors
        } answers {
            data
        }
        viewModel.topAdsCreateGroup(listOf(), String.EMPTY, Double.NaN)
        assertTrue(viewModel.createGroupLiveData.value is TopadsProductListState.Fail)
    }

    @Test
    fun `topAdsMoveGroup failure`() {
        coEvery {
            topAdsCreateUseCase.execute(any())
        } answers {
            throw Throwable()
        }
        viewModel.topAdsMoveGroup(String.EMPTY, listOf())
        assertTrue(viewModel.createGroupLiveData.value is TopadsProductListState.Fail)
    }

    @Test
    fun `topAdsMoveGroup success no errors`() {
        val data = FinalAdResponse()
        coEvery {
            topAdsCreateUseCase.execute(any())
        } answers {
            data
        }
        viewModel.topAdsMoveGroup(String.EMPTY, listOf())
        assertTrue(viewModel.createGroupLiveData.value is TopadsProductListState.Success)
    }

    @Test
    fun `topAdsMoveGroup success with errors`() {
        val data = listOf(FinalAdResponse.TopadsManageGroupAds.ErrorsItem())
        coEvery {
            topAdsCreateUseCase.execute(any()).topadsManageGroupAds.groupResponse.errors
        } answers {
            data
        }
        coEvery {
            topAdsCreateUseCase.execute(any()).topadsManageGroupAds.keywordResponse.errors
        } answers {
            data
        }
        viewModel.topAdsMoveGroup(String.EMPTY, listOf())
        assertTrue(viewModel.createGroupLiveData.value is TopadsProductListState.Fail)
    }

    @Test
fun `getTopAdsDeposit failure`() {
        coEvery {
            topAdsGetDepositUseCase.executeOnBackground()
        } answers {
            throw Throwable()
        }
        viewModel.getTopAdsDeposit()
        assertTrue(viewModel.topadsDeposits.value == null)
    }

    @Test
    fun `getTopAdsDeposit success`() {
        val data = Deposit()
        coEvery {
            topAdsGetDepositUseCase.executeOnBackground()
        } answers {
            data
        }
        viewModel.getTopAdsDeposit()
        assertTrue(viewModel.topadsDeposits.value is Deposit)
    }

    @Test
    fun `getSelectedProductItems failure`() {
        assertTrue(viewModel.getSelectedProductItems() == null)
    }

    @Test
    fun `getSelectedProductItems success`() {
        `loadProductList success, no errors`()
        assertTrue(viewModel.getSelectedProductItems() != null)
    }

    @Test
    fun `getGroupList failure`() {
        assertTrue(viewModel.getGroupList() == null)
    }

    @Test
    fun `getGroupList success`() {
        `getTopadsGroupList success`()
        assertTrue(viewModel.getGroupList() != null)
    }

    @Test
    fun `getMapperInstance success`() {
        assertTrue(viewModel.getMapperInstance() is ProductRecommendationMapper)
    }

}
