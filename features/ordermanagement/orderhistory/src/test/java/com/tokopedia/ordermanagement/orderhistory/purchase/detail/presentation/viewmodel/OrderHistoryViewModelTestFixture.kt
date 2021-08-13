package com.tokopedia.ordermanagement.orderhistory.purchase.detail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.domain.usecase.OrderHistoryUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class OrderHistoryViewModelTestFixture {

    @RelaxedMockK
    lateinit var orderHistoryUseCase: OrderHistoryUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: OrderHistoryViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = OrderHistoryViewModel(orderHistoryUseCase, userSession, CoroutineTestDispatchersProvider)
    }

}