package com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.domain.usecase.DiscussionSetSmartReplySettingsUseCase
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.domain.usecase.DiscussionSetSmartReplyTemplateUseCase
import javax.inject.Inject

class TalkSmartReplyDetailViewModel @Inject constructor(
        private val discussionSetSmartReplyTemplateUseCase: DiscussionSetSmartReplyTemplateUseCase,
        private val discussionSetSmartReplySettingsUseCase: DiscussionSetSmartReplySettingsUseCase,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io) {


}