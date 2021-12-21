package com.tokopedia.topchat.chattemplate.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chattemplate.domain.usecase.GetTemplateUseCaseNew
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class BaseChatTemplateViewModel {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    //UseCases
    @RelaxedMockK
    lateinit var getTemplateUseCase: GetTemplateUseCaseNew

    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    protected lateinit var viewModel: ChatTemplateViewModel

    protected val expectedThrowable = Throwable("Oops!")

    @Before
    open fun before() {
        MockKAnnotations.init(this)
        viewModel = ChatTemplateViewModel(
            getTemplateUseCase,
            dispatchers
        )
    }
}