package com.tokopedia.review.feature.reviewreminder.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.review.feature.reviewreminder.domain.ProductrevGetReminderCounterUseCase
import com.tokopedia.review.feature.reviewreminder.domain.ProductrevGetReminderListUseCase
import com.tokopedia.review.feature.reviewreminder.domain.ProductrevGetReminderTemplateUseCase
import com.tokopedia.review.feature.reviewreminder.domain.ProductrevSendReminderUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class ReminderMessageViewModelTestFixture {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var productrevGetReminderCounterUseCase: ProductrevGetReminderCounterUseCase

    @RelaxedMockK
    lateinit var productrevGetReminderTemplateUseCase: ProductrevGetReminderTemplateUseCase

    @RelaxedMockK
    lateinit var productrevGetReminderListUseCase: ProductrevGetReminderListUseCase

    @RelaxedMockK
    lateinit var productrevSendReminderUseCase: ProductrevSendReminderUseCase

    protected lateinit var viewModel: ReminderMessageViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ReminderMessageViewModel(
                CoroutineTestDispatchersProvider,
                productrevGetReminderCounterUseCase,
                productrevGetReminderTemplateUseCase,
                productrevGetReminderListUseCase,
                productrevSendReminderUseCase
        )
    }
}