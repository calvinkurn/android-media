package com.tokopedia.tokofood.search.searchresult

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.filter.bottomsheet.pricerangecheckbox.item.PriceRangeFilterCheckboxItemUiModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodQuickPriceRangeHelper
import com.tokopedia.tokofood.feature.search.searchresult.presentation.viewmodel.TokofoodQuickPriceRangeViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
open class TokofoodQuickPriceRangeViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    lateinit var helper: TokofoodQuickPriceRangeHelper

    protected lateinit var viewModel: TokofoodQuickPriceRangeViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokofoodQuickPriceRangeViewModel(
            helper,
            CoroutineTestDispatchersProvider
        )
    }

    protected fun getPriceFilterUiModels(): List<PriceRangeFilterCheckboxItemUiModel> {
        return listOf(
            PriceRangeFilterCheckboxItemUiModel(
                Option(
                    name = "$",
                    key = "pricing",
                    value = "0"
                )
            ),
            PriceRangeFilterCheckboxItemUiModel(
                Option(
                    name = "$",
                    key = "pricing",
                    value = "1"
                )
            ),
            PriceRangeFilterCheckboxItemUiModel(
                Option(
                    name = "$",
                    key = "pricing",
                    value = "2"
                )
            ),
            PriceRangeFilterCheckboxItemUiModel(
                Option(
                    name = "$",
                    key = "pricing",
                    value = "3"
                )
            )
        )
    }

}