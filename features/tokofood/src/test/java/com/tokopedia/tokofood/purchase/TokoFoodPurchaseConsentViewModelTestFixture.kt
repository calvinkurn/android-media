package com.tokopedia.tokofood.purchase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokofood.common.util.Result
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.AgreeConsentUseCase
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.subview.TokoFoodPurchaseConsentViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.After
import org.junit.Before
import org.junit.Rule

@FlowPreview
@ExperimentalCoroutinesApi
abstract class TokoFoodPurchaseConsentViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    protected lateinit var agreeConsentUseCase: Lazy<AgreeConsentUseCase>

    protected lateinit var viewModel: TokoFoodPurchaseConsentViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokoFoodPurchaseConsentViewModel(
            agreeConsentUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    protected fun collectFromSharedFlow(whenAction: () -> Unit,
                                        then: (Result<Boolean>?) -> Unit) {
        val testCoroutineScope = TestCoroutineScope().apply {
            pauseDispatcher()
        }
        var actualUiModelState: Result<Boolean>? = null
        val job = testCoroutineScope.launch {
            viewModel.agreeConsentData.collect {
                actualUiModelState = it
            }
        }
        testCoroutineScope.runCurrent()
        whenAction()
        testCoroutineScope.runCurrent()
        then(actualUiModelState)
        job.cancel()
    }

}