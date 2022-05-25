package com.tokopedia.tokofood.postpurchase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.TokoFoodOrderDetailMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.TokoFoodOrderStatusMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetDriverPhoneNumberUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetTokoFoodOrderDetailUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetTokoFoodOrderStatusUseCase
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewmodel.TokoFoodOrderTrackingViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.After
import org.junit.Before
import org.junit.Rule

@FlowPreview
@ExperimentalCoroutinesApi
abstract class TokoFoodOrderTrackingViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    protected lateinit var getTokoFoodOrderDetailUseCase: Lazy<GetTokoFoodOrderDetailUseCase>

    @RelaxedMockK
    protected lateinit var getTokoFoodOrderStatusUseCase: Lazy<GetTokoFoodOrderStatusUseCase>

    @RelaxedMockK
    protected lateinit var getDriverPhoneNumberUseCase: Lazy<GetDriverPhoneNumberUseCase>

    protected lateinit var tokoFoodOrderDetailMapper: TokoFoodOrderDetailMapper

    protected lateinit var tokoFoodOrderStatusMapper: TokoFoodOrderStatusMapper

    protected lateinit var viewModel: TokoFoodOrderTrackingViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        tokoFoodOrderDetailMapper = TokoFoodOrderDetailMapper(mockk(relaxed = true), mockk(relaxed = true))
        tokoFoodOrderStatusMapper = TokoFoodOrderStatusMapper()
        viewModel = TokoFoodOrderTrackingViewModel(
            CoroutineTestDispatchersProvider,
            getTokoFoodOrderDetailUseCase,
            getTokoFoodOrderStatusUseCase,
            getDriverPhoneNumberUseCase
        )
    }

    @After
    fun finish() {
        unmockkAll()
    }

    companion object {
        const val ORDER_ID_DUMMY = "52af8a53-86cc-40b7-bb98-cc3adde8e32a"

        const val ORDER_TRACKING_CREATED = "json/order_tracking_created.json"
        const val ORDER_TRACKING_MERCHANT_ACCEPTED = "json/order_tracking_merchant_accepted.json"
        const val ORDER_TRACKING_SEARCHING_DRIVER = "json/order_tracking_searching_driver.json"
        const val ORDER_TRACKING_PICKUP_REQUESTED = "json/order_tracking_pickup_requested.json"
        const val ORDER_TRACKING_OTW_DESTINATION = "json/order_tracking_otw_destination.json"
        const val ORDER_TRACKING_CANCELLED = "json/orderdetailcancelled.json"
        const val ORDER_TRACKING_SUCCESS = "json/orderdetailsuccess.json"
    }
}