package com.tokopedia.sellerorder.filter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.applink.order.DeeplinkMapperOrder
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_ORDER
import com.tokopedia.sellerorder.filter.domain.usecase.GetSomOrderFilterUseCase
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.sellerorder.filter.presentation.viewmodel.SomFilterViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import java.lang.reflect.Field

abstract class SomFilterViewModelTestFixture {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider

    @RelaxedMockK
    lateinit var getSomOrderFilterUseCase: GetSomOrderFilterUseCase

    protected lateinit var somFilterViewModel: SomFilterViewModel

    lateinit var somFilterUiModelField: Field
    lateinit var requestCancelFilterModel: SomFilterUiModel

    companion object {
        val mockDate = "14 Okt 2020 - 24 Okt 2020"
        val mockIdFilter = "Siap Dikirim"
        val SOM_FILTER_SUCCESS_RESPONSE = "json/som_get_order_filter_success_response.json"
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        somFilterViewModel = SomFilterViewModel(dispatcher, getSomOrderFilterUseCase)

        somFilterUiModelField = SomFilterViewModel::class.java.getDeclaredField("somFilterUiModel").apply {
            isAccessible = true
        }

        requestCancelFilterModel = SomFilterUiModel(
                nameFilter = FILTER_TYPE_ORDER,
                canSelectMany = true,
                isDividerVisible = true,
                somFilterData = listOf(
                        SomFilterChipsUiModel(
                                id = DeeplinkMapperOrder.FILTER_CANCELLATION_REQUEST,
                                idFilter = FILTER_TYPE_ORDER,
                                isSelected = false
                        )
                ))
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
}