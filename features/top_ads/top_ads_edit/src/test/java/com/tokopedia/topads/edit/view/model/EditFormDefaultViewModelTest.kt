package com.tokopedia.topads.edit.view.model

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.common.data.response.ResponseBidInfo
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.usecase.GetAdKeywordUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetPromoUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import com.tokopedia.topads.edit.data.response.EditSingleAdResponse
import com.tokopedia.topads.edit.data.response.GetAdProductResponse
import com.tokopedia.topads.edit.usecase.EditSingleAdUseCase
import com.tokopedia.topads.edit.usecase.GetAdsUseCase
import com.tokopedia.topads.edit.usecase.GroupInfoUseCase
import com.tokopedia.topads.edit.usecase.TopAdsCreateUseCase
import com.tokopedia.user.session.UserSession
import io.mockk.every
import io.mockk.invoke
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
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
                getAdsUseCase,
                getAdKeywordUseCase,
                groupInfoUseCase, editSingleAdUseCase, singleAdInfoUseCase, userSession, topAdsCreateUseCase
        )
    }

    @Test
    fun validateGroup() {
        val data = ResponseGroupValidateName()
        every { userSession.shopId } returns "123"
        every {
            validGroupUseCase.execute(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(ResponseGroupValidateName) -> Unit>()
            onSuccess.invoke(data)
        }

        viewModel.validateGroup("name") {}

        verify {
            validGroupUseCase.execute(any(), any())
        }
    }

    @Test
    fun getBidInfoDefault() {
        val data = ResponseBidInfo.Result()
        val suggestion: List<DataSuggestions> = mockk()
        every {
            bidInfoUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(ResponseBidInfo.Result) -> Unit>()
            onSuccess.invoke(data)
        }

        viewModel.getBidInfoDefault(suggestion) {}

        verify {
            bidInfoUseCase.executeQuerySafeMode(any(), any())
        }
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
    fun getAds() {
        val data = GetAdProductResponse()
        every {
            getAdsUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(GetAdProductResponse) -> Unit>()
            onSuccess.invoke(data)
        }

        viewModel.getAds(1, groupId) { _: List<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>, _: Int, _: Int -> }

        verify {
            getAdsUseCase.executeQuerySafeMode(any(), any())
        }
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
    fun getAdKeyword() {
        val data = GetKeywordResponse()
        every { userSession.shopId } returns "123"
        every {
            getAdKeywordUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(GetKeywordResponse) -> Unit>()
            onSuccess.invoke(data)
        }

        viewModel.getAdKeyword(groupId, "") { list: List<GetKeywordResponse.KeywordsItem>, s: String -> }


        verify {
            getAdKeywordUseCase.executeQuerySafeMode(any(), any())
        }
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
    fun topAdsCreated() {
        val dataProduct: Bundle = mockk()
        val dataKeyword: HashMap<String, Any?> = mockk()
        val dataGroup: HashMap<String, Any?> = mockk()
        viewModel.topAdsCreated(dataProduct, dataKeyword, dataGroup, {}, {})
        verify {
            topAdsCreateUseCase.execute(any(), any())
        }
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
    fun onClearedTest() {
        viewModel.onCleared()
        verify { validGroupUseCase.cancelJobs() }
        verify { bidInfoUseCase.cancelJobs() }
        verify { getAdsUseCase.cancelJobs() }
        verify { getAdKeywordUseCase.cancelJobs() }
        verify { groupInfoUseCase.cancelJobs() }
        verify { topAdsCreateUseCase.unsubscribe() }
        verify { editSingleAdUseCase.cancelJobs() }
        verify { singleAdInfoUseCase.cancelJobs() }
    }
}