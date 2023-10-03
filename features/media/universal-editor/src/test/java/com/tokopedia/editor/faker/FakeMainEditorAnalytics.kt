package com.tokopedia.editor.faker

import com.tokopedia.editor.analytics.main.editor.MainEditorAnalytics

class FakeMainEditorAnalytics: MainEditorAnalytics {
    override fun toolTextClick() {}

    override fun backPageClick() {}

    override fun finishPageClick(hasText: Boolean, isMute: Boolean, isCropped: Boolean) {}

    override fun toolAdjustCropClick() {}
}
