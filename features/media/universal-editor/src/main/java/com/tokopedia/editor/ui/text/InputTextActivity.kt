package com.tokopedia.editor.ui.text

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.editor.R as editorR
import com.tokopedia.editor.di.ModuleInjector
import com.tokopedia.editor.ui.EditorFragmentProvider
import com.tokopedia.editor.ui.EditorFragmentProviderImpl
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.component.NavToolbarComponent
import com.tokopedia.picker.common.component.ToolbarTheme
import javax.inject.Inject

class InputTextActivity : BaseActivity(), NavToolbarComponent.Listener {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: InputTextViewModel by viewModels { viewModelFactory }

    private val toolbar by uiComponent {
        NavToolbarComponent(
            listener = this,
            parent = it,
            useArrowIcon = true
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)
        setContentView(editorR.layout.activity_input_text)

        initFragment()
        initView()
        initObserver()
    }

    override fun onCloseClicked() {
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    override fun onContinueClicked() {
        val resultData = viewModel.getTextDetail()

        val intent = Intent()
        intent.putExtra(INPUT_TEXT_RESULT, resultData)

        setResult(0, intent)
        finish()
    }

    private fun initView() {
        setupToolbar()
    }

    private fun initObserver() {
        viewModel.textValue.observe(this) {
            toolbar.showContinueButtonAs(it.isNotEmpty())
        }
    }

    private fun setupToolbar() {
        toolbar.onToolbarThemeChanged(ToolbarTheme.Transparent)
        toolbar.showContinueButtonAs(true)
        toolbar.setTitle(getString(editorR.string.universal_editor_nav_bar_add_text))
    }

    private fun initFragment() {
        val fragment = fragmentProvider().inputTextFragment()

        supportFragmentManager.beginTransaction()
            .replace(editorR.id.fragment_view, fragment, "Tag")
            .commit()
    }

    private fun fragmentProvider(): EditorFragmentProvider {
        return EditorFragmentProviderImpl(
            supportFragmentManager,
            applicationContext.classLoader
        )
    }

    private fun initInjector() {
        ModuleInjector
            .get(this)
            .inject(this)
    }

    private fun showExitConfirmationDialog() {
        DialogUnify(this, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(editorR.string.universal_editor_input_tool_confirmation_title))
            setDescription(getString(editorR.string.universal_editor_input_tool_confirmation_desc))

            dialogPrimaryCTA.apply {
                text = getString(editorR.string.universal_editor_input_tool_confirmation_primary_cta)
                setOnClickListener {
                    // TODO: implement function
                }
            }

            dialogSecondaryLongCTA.apply {
                text = getString(editorR.string.universal_editor_input_tool_confirmation_secondary_cta)
                setOnClickListener {
                    // TODO: implement function
                }
            }

            show()
        }
    }

    companion object {
        const val INPUT_TEXT_RESULT = "input_text_result"

        fun create(context: Context): Intent {
            return Intent(context, InputTextActivity::class.java)
        }
    }
}
