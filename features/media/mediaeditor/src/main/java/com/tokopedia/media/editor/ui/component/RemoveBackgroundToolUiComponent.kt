package com.tokopedia.media.editor.ui.component

import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.R
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.unifycomponents.UnifyButton

class RemoveBackgroundToolUiComponent constructor(
    viewGroup: ViewGroup,
    private val listener: Listener
) : UiComponent(viewGroup, R.id.uc_tool_removebg) {

    private val btnRemoveBg: UnifyButton = findViewById(R.id.btn_remove_bg)

    init {
        btnRemoveBg.setOnClickListener {
            listener.onRemoveBackgroundClicked()
        }
    }

    fun setupView() {
        container().show()
    }

    interface Listener {
        fun onRemoveBackgroundClicked()
    }

}