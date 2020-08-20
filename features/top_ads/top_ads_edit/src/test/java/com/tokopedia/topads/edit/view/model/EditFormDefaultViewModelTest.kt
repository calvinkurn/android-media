package com.tokopedia.topads.edit.view.model

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.edit.data.param.DataSuggestions
import com.tokopedia.topads.edit.data.response.*
import com.tokopedia.topads.edit.usecase.*
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
    private val validGroupUseCase: ValidGroupUseCase = mockk(relaxed = true)
    private val bidInfoUseCase: BidInfoUseCase = mockk(relaxed = true)
    private val getAdsUseCase: GetAdsUseCase = mockk(relaxed = true)
    private val getAdKeywordUseCase: GetAdKeywordUseCase = mockk(relaxed = true)
    private val groupInfoUseCase: GroupInfoUseCase = mockk(relaxed = true)
    private val editSingleAdUseCase: EditSingleAdUseCase = mockk(relaxed = true)
    private val topAdsCreateUseCase: TopAdsCreateUseCase = mockk(relaxed = true)
    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: EditFormDefaultViewModel
    private var groupId = 123

    @Before
    fun setUp() {
        viewModel = EditFormDefaultViewModel(
                testDispatcher,
                validGroupUseCase,
                bidInfoUseCase,
                getAdsUseCase,
                getAdKeywordUseCase,
                groupInfoUseCase, editSingleAdUseCase, topAdsCreateUseCase
        )
    }

    @Test
    fun validateGroup() {
        val data = ResponseGroupValidateName()
        every {
            validGroupUseCase.execute(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(ResponseGroupValidateName) -> Unit>()
            onSuccess.invoke(data)
        }

        viewModel.validateGroup("name") {}

        verify {
            validGroupUseCase.executeQuerySafeMode(any(), any())
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

        viewModel.getAds(groupId) {}

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
        every {
            getAdKeywordUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(GetKeywordResponse) -> Unit>()
            onSuccess.invoke(data)
        }

        viewModel.getAdKeyword(groupId) {}

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
        val data = FinalAdResponse()
        val dataProduct: Bundle = mockk()
        val dataKeyword: HashMap<String, Any?> = mockk()
        val dataGroup: HashMap<String, Any?> = mockk()
        every {
            topAdsCreateUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(FinalAdResponse) -> Unit>()
            onSuccess.invoke(data)
        }

        viewModel.topAdsCreated(dataProduct, dataKeyword, dataGroup, {}, {})

        verify {
            topAdsCreateUseCase.executeQuerySafeMode(any(), any())
        }
    }


    @Test
    fun onClearedTest() {
        viewModel.onCleared()
        verify { validGroupUseCase.cancelJobs() }
        verify { bidInfoUseCase.cancelJobs() }
        verify { getAdsUseCase.cancelJobs() }
        verify { getAdKeywordUseCase.cancelJobs() }
        verify { groupInfoUseCase.cancelJobs() }
        verify { topAdsCreateUseCase.cancelJobs() }
        verify { editSingleAdUseCase.cancelJobs() }
    }
}