package com.tokopedia.editor.analytics.input.text

import com.tokopedia.editor.ui.model.InputTextModel

interface InputTextAnalytics {
    fun editSaveClick(
        textDetail: InputTextModel
    ) // analytics on detail save
}
