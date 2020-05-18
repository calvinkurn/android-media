package com.tokopedia.prereleaseinspector

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.view_inspector_config_item.view.*

class ViewInspectorConfigItem : FrameLayout {
    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    fun init() {
        inflate(context, R.layout.view_inspector_config_item, this)
    }

    fun setup(hint: String, text: String, listener: View.OnClickListener) {
        viewInspectorConfigHint.setText(hint)
        viewInspectorConfigInput.setText(text)
        viewInspectorConfigApply.setOnClickListener(listener)
    }

    fun setHint(hint: String) {
        viewInspectorConfigHint.setText(hint)
    }

    fun getText() : String {
        return viewInspectorConfigInput.text.toString()
    }

    fun setText(text: String) {
        viewInspectorConfigInput.setText(text)
    }

    fun setApplyListener(listener: View.OnClickListener) {
        viewInspectorConfigApply.setOnClickListener(listener)
    }
}