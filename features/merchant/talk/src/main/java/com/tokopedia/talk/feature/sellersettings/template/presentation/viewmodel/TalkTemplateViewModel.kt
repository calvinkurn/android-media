package com.tokopedia.talk.feature.sellersettings.template.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.talk.feature.sellersettings.template.domain.usecase.*

class TalkTemplateViewModel(
        private val addTemplateUseCase: AddTemplateUseCase,
        private val arrangeTemplateUseCase: ArrangeTemplateUseCase,
        private val deleteSpecificTemplateUseCase: DeleteSpecificTemplateUseCase,
        private val enableTemplateUseCase: EnableTemplateUseCase,
        private val getAllTemplatesUseCase: GetAllTemplatesUseCase,
        private val updateSpecificTemplateUseCase: UpdateSpecificTemplateUseCase,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io)  {


}