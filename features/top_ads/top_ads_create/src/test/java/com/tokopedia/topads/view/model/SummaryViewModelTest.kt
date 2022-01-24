package com.tokopedia.topads.view.model

import android.content.Context
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.domain.usecase.TopAdsCreateUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import com.tokopedia.topads.view.RequestHelper
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SummaryViewModelTest {
    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private lateinit var viewModel: SummaryViewModel

    private lateinit var repository: GraphqlRepository
    private lateinit var context: Context

    private var topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase = mockk(relaxed = true)
    private val validGroupUseCase: TopAdsGroupValidateNameUseCase = mockk(relaxed = true)
    private val topAdsCreateUseCase: TopAdsCreateUseCase = mockk(relaxed = true)


    @Before
    fun setUp() {
        repository = mockk()
        context = mockk(relaxed = true)
        viewModel = spyk(SummaryViewModel(rule.dispatchers,validGroupUseCase, topAdsGetShopDepositUseCase, topAdsCreateUseCase))
        mockkObject(RequestHelper)
        every { RequestHelper.getGraphQlRequest(any(), any(), any()) } returns mockk(relaxed = true)
        every { RequestHelper.getCacheStrategy() } returns mockk(relaxed = true)
    }

    @Test
    fun `test result in getTopAdsDeposit`() {
        viewModel.getTopAdsDeposit({}, {})
        verify {
            topAdsGetShopDepositUseCase.execute(any(), any())
        }
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

        viewModel.validateGroup("name", {})

        verify {
            validGroupUseCase.execute(any(), any())
        }
    }

    @Test
    fun `test exception in topAdsCreated`() {
        val dataProduct: Bundle = mockk()
        val dataKeyword: HashMap<String, Any?> = mockk()
        val dataGroup: HashMap<String, Any?> = mockk()
        viewModel.topAdsCreated(dataProduct, dataKeyword, dataGroup, {}, {})
        verify {
            topAdsCreateUseCase.execute(any(), any())
        }
    }
}