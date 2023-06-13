package com.tokopedia.topads.edit.view.model

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.*
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.usecase.GetAdKeywordUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetPromoUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import com.tokopedia.topads.edit.data.response.EditSingleAdResponse
import com.tokopedia.topads.edit.usecase.EditSingleAdUseCase
import com.tokopedia.topads.edit.usecase.GetAdsUseCase
import com.tokopedia.topads.edit.usecase.GroupInfoUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsCreateUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSession
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class EditFormDefaultViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val validGroupUseCase: TopAdsGroupValidateNameUseCase = mockk(relaxed = true)
    private val bidInfoUseCase: BidInfoUseCase = mockk(relaxed = true)
    private val getAdsUseCase: GetAdsUseCase = mockk(relaxed = true)
    private val getAdKeywordUseCase: GetAdKeywordUseCase = mockk(relaxed = true)
    private val groupInfoUseCase: GroupInfoUseCase = mockk(relaxed = true)
    private val bidInfoDefaultUseCase: BidInfoUseCase = mockk(relaxed = true)
    private val editSingleAdUseCase: EditSingleAdUseCase = mockk(relaxed = true)
    private val topAdsCreateUseCase: TopAdsCreateUseCase = mockk(relaxed = true)
    private val testDispatcher = TestCoroutineDispatcher()
    private val singleAdInfoUseCase: TopAdsGetPromoUseCase = mockk(relaxed = true)
    private lateinit var viewModel: EditFormDefaultViewModel
    private val userSession: UserSession = mockk()
    private var groupId = 123

    @Before
    fun setUp() {
        viewModel = EditFormDefaultViewModel(
            testDispatcher,
            validGroupUseCase,
            bidInfoUseCase,
            bidInfoDefaultUseCase,
            getAdsUseCase,
            getAdKeywordUseCase,
            groupInfoUseCase,
            editSingleAdUseCase,
            singleAdInfoUseCase,
            userSession,
            topAdsCreateUseCase
        )
    }

    @Test
    fun validateGroup() {
        var actual : ResponseGroupValidateName.TopAdsGroupValidateNameV2 ?= null
        val data = ResponseGroupValidateName()
        every { userSession.shopId } returns "123"
        every {
            validGroupUseCase.execute(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(ResponseGroupValidateName) -> Unit>()
            onSuccess.invoke(data)
        }

        viewModel.validateGroup("name") {actual = it}

        verify {
            validGroupUseCase.execute(any(), any())
        }
        Assert.assertEquals(data.topAdsGroupValidateName, actual)
    }

    @Test
    fun `validateGroup error`() {
        every {
            validGroupUseCase.execute(any(), captureLambda())
        } answers {
            lambda<(Throwable) -> Unit>().invoke(Throwable())
        }

        var successCalled = false
        viewModel.validateGroup("name") {successCalled = true}

        assert(!successCalled)
    }

    @Test
    fun getBidInfoDefault() {
        var actual : List<TopadsBidInfo.DataItem> ?= null
        val data = ResponseBidInfo.Result()
        val suggestion: List<DataSuggestions> = mockk()
        every {
            bidInfoDefaultUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(ResponseBidInfo.Result) -> Unit>()
            onSuccess.invoke(data)
        }

        viewModel.getBidInfoDefault(suggestion) {actual = it}

        verify {
            bidInfoDefaultUseCase.executeQuerySafeMode(any(), any())
        }
        Assert.assertEquals(data.topadsBidInfo.data, actual)
    }

    @Test
    fun `getBidInfoDefault error`() {
        every {
            bidInfoDefaultUseCase.executeQuerySafeMode(any(), captureLambda())
        } answers {
            lambda<(Throwable) -> Unit>().invoke(Throwable())
        }

        var successCalled = false
        viewModel.getBidInfoDefault(listOf()) {successCalled = true}

        assert(!successCalled)
    }

    @Test
    fun editSingleAd() {
        val data = EditSingleAdResponse()
        every {
            editSingleAdUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(EditSingleAdResponse) -> Unit>()
            onSuccess.invoke(data)
        }

        viewModel.editSingleAd(groupId.toString(), 20.0F, 300F)

        verify {
            editSingleAdUseCase.executeQuerySafeMode(any(), any())
        }
    }

    @Test
    fun `editSingleAd error`() {
        val throwable = spyk(Throwable())
        every {
            editSingleAdUseCase.executeQuerySafeMode(any(), captureLambda())
        } answers {
            lambda<(Throwable) -> Unit>().invoke(throwable)
        }

        viewModel.editSingleAd(groupId.toString(), 20.0F, 300F)
        verify { throwable.printStackTrace() }
    }

    @Test
    fun getAds() {
        val data = GetAdProductResponse()
        every {
            getAdsUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(GetAdProductResponse) -> Unit>()
            onSuccess.invoke(data)
        }

        viewModel.getAds(
            1,
            groupId.toString(), "") { _: List<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>, _: Int, _: Int -> }

        verify {
            getAdsUseCase.executeQuerySafeMode(any(), any())
        }
    }

    @Test
    fun `getAds error`() {
        every {
            getAdsUseCase.executeQuerySafeMode(any(), captureLambda())
        } answers {
            lambda<(Throwable) -> Unit>().invoke(Throwable())
        }

        var successCalled = false
        viewModel.getAds(1, groupId.toString(), "") { _, _, _ -> successCalled = true }

        assert(!successCalled)
    }

    @Test
    fun getGroupInfo() {
        val data = GroupInfoResponse()
        every {
            groupInfoUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(GroupInfoResponse) -> Unit>()
            onSuccess.invoke(data)
        }

        viewModel.getGroupInfo(groupId.toString()) {}

        verify {
            groupInfoUseCase.executeQuerySafeMode(any(), any())
        }
    }

    @Test
    fun `getGroupInfo error`() {
        every {
            groupInfoUseCase.executeQuerySafeMode(any(), captureLambda())
        } answers {
            lambda<(Throwable) -> Unit>().invoke(Throwable())
        }

        var successCalled = false
        viewModel.getGroupInfo(groupId.toString()) { successCalled = true }

        assert(!successCalled)
    }

    @Test
    fun getAdKeyword() {
        val data = GetKeywordResponse()
        every { userSession.shopId } returns "123"
        every {
            getAdKeywordUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(GetKeywordResponse) -> Unit>()
            onSuccess.invoke(data)
        }

        viewModel.getAdKeyword(
            groupId,
            ""
        ) { list: List<GetKeywordResponse.KeywordsItem>, s: String -> }


        verify {
            getAdKeywordUseCase.executeQuerySafeMode(any(), any())
        }
    }

    @Test
    fun `getAdKeyword error`() {
        every { userSession.shopId } returns "123"
        every {
            getAdKeywordUseCase.executeQuerySafeMode(any(), captureLambda())
        } answers {
            lambda<(Throwable) -> Unit>().invoke(Throwable())
        }

        var successCalled = false
        viewModel.getAdKeyword(1, "") { _,_-> successCalled = true }

        assert(!successCalled)
    }

    @Test
    fun getBidInfo() {
        val data = ResponseBidInfo.Result()
        val suggestion: List<DataSuggestions> = mockk()

        every {
            bidInfoUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(ResponseBidInfo.Result) -> Unit>()
            onSuccess.invoke(data)
        }

        viewModel.getBidInfo(suggestion) {}

        verify {
            bidInfoUseCase.executeQuerySafeMode(any(), any())
        }
    }

    @Test
    fun `getbidinfo error`() {
        every {
            bidInfoUseCase.executeQuerySafeMode(any(), captureLambda())
        } answers {
            lambda<(Throwable) -> Unit>().invoke(Throwable())
        }

        var successCalled = false
        viewModel.getBidInfo(listOf()) { successCalled = true }

        assert(!successCalled)
    }

    @Test
    fun topAdsCreated() {
        val dataProduct: Bundle = mockk()
        val dataKeyword: HashMap<String, Any?> = mockk()
        val dataGroup: HashMap<String, Any?> = mockk()
        viewModel.topAdsCreated(dataProduct, dataKeyword, dataGroup, {}, {})
        coVerify {
            topAdsCreateUseCase.execute(any<RequestParams>())
        }
    }

    @Test
    fun `topadscreated success check`() {
        every { topAdsCreateUseCase.setParam(any(),any(),any(),any()) } returns mockk(relaxed = true)
        coEvery { topAdsCreateUseCase.execute(any<RequestParams>()) } returns FinalAdResponse()

        var successCalled = false
        viewModel.topAdsCreated(mockk(), mockk(), mockk(), {successCalled = true}, {})
        Assert.assertTrue(successCalled)
    }

    @Test
    fun `topadscreated error check`() {
        every { topAdsCreateUseCase.setParam(any(),any(),any(),any()) } returns mockk(relaxed = true)
        coEvery { topAdsCreateUseCase.execute(any<RequestParams>()) } returns FinalAdResponse(
            FinalAdResponse.TopadsManageGroupAds(FinalAdResponse.TopadsManageGroupAds.KeywordResponse(errors = listOf(
                FinalAdResponse.TopadsManageGroupAds.ErrorsItem())),
                FinalAdResponse.TopadsManageGroupAds.GroupResponse(errors = listOf(FinalAdResponse.TopadsManageGroupAds.ErrorsItem()))))

        var successCalled = false
        viewModel.topAdsCreated(mockk(), mockk(), mockk(), {successCalled = true}, {})
        Assert.assertTrue(!successCalled)
    }

    @Test
    fun `topadscreated exception check`() {
        every { topAdsCreateUseCase.setParam(any(),any(),any(),any()) } throws Throwable()

        var successCalled = false
        viewModel.topAdsCreated(mockk(), mockk(), mockk(), {successCalled = true}, {})
        Assert.assertTrue(!successCalled)
    }

    @Test
    fun getSingleAdInfo() {
        val adId = "121"
        val mockThrowable = mockk<Throwable>(relaxed = true)
        every { userSession.shopId } returns "123"
        every {
            singleAdInfoUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getSingleAdInfo(adId) {}

        verify { singleAdInfoUseCase.execute(any(), any()) }

    }

    @Test
    fun `getsingleadinfo error`() {
        every { userSession.shopId } returns "123"
        every {
            singleAdInfoUseCase.execute(any(), captureLambda())
        } answers {
            lambda<(Throwable) -> Unit>().invoke(Throwable())
        }

        var successCalled = false
        viewModel.getSingleAdInfo("") { successCalled = true }

        assert(!successCalled)
    }

    @Test
    fun onClearedTest() {
        viewModel.onCleared()
        verify { validGroupUseCase.cancelJobs() }
        verify { bidInfoUseCase.cancelJobs() }
        verify { bidInfoDefaultUseCase.cancelJobs() }
        verify { getAdsUseCase.cancelJobs() }
        verify { getAdKeywordUseCase.cancelJobs() }
        verify { groupInfoUseCase.cancelJobs() }
        verify { editSingleAdUseCase.cancelJobs() }
        verify { singleAdInfoUseCase.cancelJobs() }
    }
}