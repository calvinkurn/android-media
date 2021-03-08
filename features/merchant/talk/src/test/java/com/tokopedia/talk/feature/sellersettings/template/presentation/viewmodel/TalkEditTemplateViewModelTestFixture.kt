package com.tokopedia.talk.feature.sellersettings.template.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.talk.feature.sellersettings.template.domain.usecase.*
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class TalkEditTemplateViewModelTestFixture {

    @RelaxedMockK
    lateinit var addTemplateUseCase: AddTemplateUseCase

    @RelaxedMockK
    lateinit var deleteSpecificTemplateUseCase: DeleteSpecificTemplateUseCase

    @RelaxedMockK
    lateinit var updateSpecificTemplateUseCase: UpdateSpecificTemplateUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: TalkEditTemplateViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TalkEditTemplateViewModel(addTemplateUseCase, deleteSpecificTemplateUseCase, updateSpecificTemplateUseCase, CoroutineTestDispatchersProvider)
    }

}