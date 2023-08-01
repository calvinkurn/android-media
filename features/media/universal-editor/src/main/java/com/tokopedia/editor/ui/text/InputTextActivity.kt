package com.tokopedia.editor.ui.text

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.editor.R
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.component.NavToolbarComponent
import com.tokopedia.picker.common.component.ToolbarTheme

class InputTextActivity : BaseActivity(), NavToolbarComponent.Listener {

    private val toolbar by uiComponent {
        NavToolbarComponent(
            listener = this,
            parent = it,
            useArrowIcon = true
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_text)
        initView()
    }

    override fun onCloseClicked() {
        // TODO save editorModel (current)
    }

    override fun onContinueClicked() {
        // TODO save editorModel (new)
    }

    private fun initView() {
        setupToolbar()
    }

    private fun setupToolbar() {
        toolbar.onToolbarThemeChanged(ToolbarTheme.Transparent)
        toolbar.showContinueButtonAs(true)
        toolbar.setTitle(getString(R.string.universal_editor_nav_bar_add_text))
    }

    companion object {
        fun create(context: Context): Intent {
            return Intent(context, InputTextActivity::class.java)
        }
    }
}
