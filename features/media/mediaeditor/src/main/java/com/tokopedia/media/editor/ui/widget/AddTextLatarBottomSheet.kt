package com.tokopedia.media.editor.ui.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.media.editor.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class AddTextLatarBottomSheet(): BottomSheetUnify() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.inflate(R.layout.add_text_tips_bottomsheet, null)?.apply {
            setChild(this)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}
