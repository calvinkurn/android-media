package com.tokopedia.media.editor.ui.component

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.adapter.AddTextToolAdapter
import com.tokopedia.picker.common.basecomponent.UiComponent

class AddTextToolUiComponent constructor(
    viewGroup: ViewGroup,
    private val listener: Listener
) : UiComponent(viewGroup, R.id.uc_tool_add_text) {

    private val mRv = findViewById<RecyclerView>(R.id.add_text_rv)

    fun setupView() {
        container().show()

        mRv.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        mRv.adapter = AddTextToolAdapter(listener)
    }

    interface Listener {
        fun onChangePosition()
        fun onTemplateSave()
        fun onAddFreeText()
        fun onAddSingleBackgroundText()
    }
}
