package com.tokopedia.topchat.chattemplate.viewmodel.chat_template.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chattemplate.domain.usecase.GetTemplateUseCase
import com.tokopedia.topchat.chattemplate.domain.usecase.RearrangeTemplateUseCase
import com.tokopedia.topchat.chattemplate.domain.usecase.ToggleTemplateUseCase
import com.tokopedia.topchat.chattemplate.view.viewmodel.ChatTemplateViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class BaseChatTemplateViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // UseCases
    @RelaxedMockK
    lateinit var getTemplateUseCase: GetTemplateUseCase

    @RelaxedMockK
    lateinit var toggleTemplateUseCase: ToggleTemplateUseCase

    @RelaxedMockK
    lateinit var rearrangeTemplateUseCase: RearrangeTemplateUseCase

    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    protected lateinit var viewModel: ChatTemplateViewModel

    protected val expectedThrowable = Throwable("Oops!")

    @Before
    open fun before() {
        MockKAnnotations.init(this)
        viewModel = ChatTemplateViewModel(
            getTemplateUseCase,
            toggleTemplateUseCase,
            rearrangeTemplateUseCase,
            dispatchers
        )
    }
}
