package com.tokopedia.tokofood.purchase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokofood.feature.purchase.promopage.domain.usecase.MerchantPromoListTokoFoodUseCase
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.MerchantTokoFoodPromoViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.StandardTestRule
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.After
import org.junit.Before
import org.junit.Rule

@FlowPreview
@ExperimentalCoroutinesApi
open class MerchantTokoFoodPromoViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val standardTestRule = StandardTestRule()

    @RelaxedMockK
    protected lateinit var promoListTokoFoodUseCase: Lazy<MerchantPromoListTokoFoodUseCase>

    protected lateinit var viewModel: MerchantTokoFoodPromoViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = MerchantTokoFoodPromoViewModel(
            promoListTokoFoodUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    companion object {
        const val SOURCE = "merchant_page"
    }
}
