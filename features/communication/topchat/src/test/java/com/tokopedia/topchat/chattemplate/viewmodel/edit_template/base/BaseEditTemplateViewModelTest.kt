package com.tokopedia.topchat.chattemplate.viewmodel.edit_template.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chattemplate.domain.usecase.CreateTemplateUseCase
import com.tokopedia.topchat.chattemplate.domain.usecase.DeleteTemplateUseCase
import com.tokopedia.topchat.chattemplate.domain.usecase.UpdateTemplateUseCase
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class BaseEditTemplateViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var updateTemplateUseCase: UpdateTemplateUseCase

    @RelaxedMockK
    lateinit var createTemplateUseCase: CreateTemplateUseCase

    @RelaxedMockK
    lateinit var deleteTemplateUseCase: DeleteTemplateUseCase

    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider
    protected lateinit var viewModel: EditTemplateViewModel
    protected val expectedThrowable = Throwable("Oops!")

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewModel = EditTemplateViewModel(
            updateTemplateUseCase,
            createTemplateUseCase,
            deleteTemplateUseCase,
            dispatchers
        )
    }
}
