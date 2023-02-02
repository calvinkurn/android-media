package com.tokopedia.tokofood.postpurchase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.TokoFoodOrderDetailMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.TokoFoodOrderStatusMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.*
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewmodel.TokoFoodOrderTrackingViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
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
    protected lateinit var savedStateHandle: SavedStateHandle

    @RelaxedMockK
    protected lateinit var getTokoFoodOrderDetailUseCase: Lazy<GetTokoFoodOrderDetailUseCase>

    @RelaxedMockK
    protected lateinit var getTokoFoodOrderStatusUseCase: Lazy<GetTokoFoodOrderStatusUseCase>

    @RelaxedMockK
    protected lateinit var getDriverPhoneNumberUseCase: Lazy<GetDriverPhoneNumberUseCase>

    @RelaxedMockK
    protected lateinit var getTokoChatUnreadChatCountUseCase: Lazy<GetUnreadChatCountUseCase>

    @RelaxedMockK
    protected lateinit var getTokoChatConfigGroupBookingUseCase: TokoChatConfigGroupBookingUseCase

    @RelaxedMockK
    protected lateinit var userSession: UserSessionInterface

    protected lateinit var tokoFoodOrderDetailMapper: TokoFoodOrderDetailMapper

    protected lateinit var tokoFoodOrderStatusMapper: TokoFoodOrderStatusMapper

    protected lateinit var viewModel: TokoFoodOrderTrackingViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        tokoFoodOrderDetailMapper = TokoFoodOrderDetailMapper(mockk(relaxed = true), mockk(relaxed = true))
        tokoFoodOrderStatusMapper = TokoFoodOrderStatusMapper()
        getTokoChatConfigGroupBookingUseCase = TokoChatConfigGroupBookingUseCase(mockk(relaxed = true))
        viewModel = TokoFoodOrderTrackingViewModel(
            userSession,
            savedStateHandle,
            CoroutineTestDispatchersProvider,
            getTokoFoodOrderDetailUseCase,
            getTokoFoodOrderStatusUseCase,
            getDriverPhoneNumberUseCase,
            getTokoChatUnreadChatCountUseCase
        ) { getTokoChatConfigGroupBookingUseCase }
    }

    @After
    fun finish() {
        unmockkAll()
    }

    companion object {
        const val ORDER_ID_DUMMY = "52af8a53-86cc-40b7-bb98-cc3adde8e32a"
        const val CHANNEL_ID = "1234567"
        const val GOFOOD_ORDER_NUMBER = "f-52af8a53-86cc-40b7-bb98-cc3adde8e32a"

        const val ORDER_TRACKING_OTW_DESTINATION = "json/ordertracking/order_tracking_otw_destination.json"
        const val ORDER_TRACKING_CANCELLED = "json/ordertracking/orderdetailcancelled.json"
        const val ORDER_TRACKING_SUCCESS = "json/ordertracking/orderdetailsuccess.json"
    }
}
