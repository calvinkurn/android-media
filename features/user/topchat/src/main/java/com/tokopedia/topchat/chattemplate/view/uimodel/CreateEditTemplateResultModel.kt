package com.tokopedia.topchat.chattemplate.view.uimodel

import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateUiModel

data class CreateEditTemplateResultModel (
    var editTemplateUiModel: EditTemplateUiModel,
    var index: Int,
    var text: String
)