package com.tokopedia.topchat.chattemplate.view.uimodel

import com.tokopedia.usecase.coroutines.Result

data class ArrangeResultModel(
    var to: Int,
    var from: Int,
    var templateResult: Result<GetTemplateResultModel>
)