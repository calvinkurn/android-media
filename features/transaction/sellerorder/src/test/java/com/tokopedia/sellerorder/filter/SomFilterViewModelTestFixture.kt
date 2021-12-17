package com.tokopedia.sellerorder.filter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.applink.order.DeeplinkMapperOrder
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_ORDER
import com.tokopedia.sellerorder.filter.domain.SomFilterResponse
import com.tokopedia.sellerorder.filter.domain.mapper.GetSomFilterMapper
import com.tokopedia.sellerorder.filter.domain.usecase.GetSomOrderFilterUseCase
import com.tokopedia.sellerorder.filter.presentation.model.BaseSomFilter
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterDateUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.sellerorder.filter.presentation.viewmodel.SomFilterViewModel
import com.tokopedia.sellerorder.util.TestHelper
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class SomFilterViewModelTestFixture {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    lateinit var getSomOrderFilterUseCase: GetSomOrderFilterUseCase

    protected lateinit var somFilterViewModel: SomFilterViewModel

    companion object {
        val mockDate = "14 Okt 2020 - 24 Okt 2020"
        val mockIdFilter = "Siap Dikirim"
        val SOM_FILTER_SUCCESS_RESPONSE = "json/som_get_order_filter_success_response.json"
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        somFilterViewModel = spyk(
            SomFilterViewModel(
                coroutineTestRule.dispatchers,
                getSomOrderFilterUseCase
            )
        )
    }

    protected fun LiveData<*>.verifyErrorEquals(expected: Fail) {
        val expectedResult = expected.throwable::class.java
        val actualResult = (value as Fail).throwable::class.java
        TestCase.assertEquals(expectedResult, actualResult)
    }

    protected fun List<SomFilterUiModel>.getRequestCancelFilter(): SomFilterChipsUiModel? {
        return find { it.nameFilter == FILTER_TYPE_ORDER }?.somFilterData
                ?.find { it.id == DeeplinkMapperOrder.FILTER_CANCELLATION_REQUEST }
    }

    protected fun getMockSomFilterList(): List<BaseSomFilter> {
        val mockResponseSomFilterList = TestHelper.createSuccessResponse<SomFilterResponse>(
            SOM_FILTER_SUCCESS_RESPONSE
        )
        return GetSomFilterMapper.mapToSomFilterVisitable(mockResponseSomFilterList).apply {
            filterIsInstance<SomFilterDateUiModel>().first().date = mockDate
        }
    }
}