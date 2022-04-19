package com.tokopedia.talk.feature.sellersettings.template.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.talk.feature.sellersettings.template.domain.usecase.*
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class TalkTemplateViewModelTestFixture {

    @RelaxedMockK
    lateinit var arrangeTemplateUseCase: ArrangeTemplateUseCase

    @RelaxedMockK
    lateinit var enableTemplateUseCase: EnableTemplateUseCase

    @RelaxedMockK
    lateinit var getAllTemplatesUseCase: GetAllTemplatesUseCase

    @RelaxedMockK
    lateinit var updateSpecificTemplateUseCase: UpdateSpecificTemplateUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: TalkTemplateViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TalkTemplateViewModel(arrangeTemplateUseCase, enableTemplateUseCase, getAllTemplatesUseCase, CoroutineTestDispatchersProvider)
    }

}