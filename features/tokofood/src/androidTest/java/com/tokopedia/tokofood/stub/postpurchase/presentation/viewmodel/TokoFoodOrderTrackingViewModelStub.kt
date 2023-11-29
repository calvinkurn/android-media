package com.tokopedia.tokofood.stub.postpurchase.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokochat.config.domain.TokoChatCounterUseCase
import com.tokopedia.tokochat.config.domain.TokoChatGroupBookingUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetDriverPhoneNumberUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetTokoFoodOrderDetailUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetTokoFoodOrderStatusUseCase
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewmodel.TokoFoodOrderTrackingViewModel
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class TokoFoodOrderTrackingViewModelStub @Inject constructor(
    userSession: UserSessionInterface,
    savedStateHandle: SavedStateHandle,
    coroutineDispatchers: CoroutineDispatchers,
    getTokoFoodOrderDetailUseCase: Lazy<GetTokoFoodOrderDetailUseCase>,
    getTokoFoodOrderStatusUseCase: Lazy<GetTokoFoodOrderStatusUseCase>,
    getDriverPhoneNumberUseCase: Lazy<GetDriverPhoneNumberUseCase>,
    getTokoChatGroupBookingUseCase: Lazy<TokoChatGroupBookingUseCase>,
    getTokoChatCounterUseCase: Lazy<TokoChatCounterUseCase>
) : TokoFoodOrderTrackingViewModel(
    userSession,
    savedStateHandle,
    coroutineDispatchers,
    getTokoFoodOrderDetailUseCase,
    getTokoFoodOrderStatusUseCase,
    getDriverPhoneNumberUseCase,
    getTokoChatGroupBookingUseCase,
    getTokoChatCounterUseCase
)
