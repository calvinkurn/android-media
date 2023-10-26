package com.tokopedia.editor.analytics.main.editor

interface MainEditorAnalytics {
    fun toolTextClick()

    fun toolAdjustCropClick()

    fun finishPageClick(
        hasText: Boolean,
        isMute: Boolean,
        isCropped: Boolean
    )

    fun backPageClick() // page back on main editor
}
