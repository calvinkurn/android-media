package com.tokopedia.topchat.chattemplate.view.viewmodel

import com.tokopedia.usecase.coroutines.Result

data class ArrangeResultModel(
    var to: Int,
    var from: Int,
    var templateResult: Result<GetTemplateUiModel>
)