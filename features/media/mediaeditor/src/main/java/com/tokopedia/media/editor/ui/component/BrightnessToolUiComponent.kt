package com.tokopedia.media.editor.ui.component

import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.EditText
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.R
import com.tokopedia.picker.common.basecomponent.UiComponent

class BrightnessToolUiComponent constructor(
    viewGroup: ViewGroup,
    private val listener: Listener
) : UiComponent(viewGroup, R.id.uc_tool_brightness) {

    private val edtBrightnessValue: EditText = findViewById(R.id.edt_brightness)

    fun setupView() {
        container().show()

        edtBrightnessValue.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val value = s.toString().toInt()
                listener.onBrightnessValueChanged(if (value > 0) value else 0)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

        })
    }

    interface Listener {
        fun onBrightnessValueChanged(value: Int)
    }

}