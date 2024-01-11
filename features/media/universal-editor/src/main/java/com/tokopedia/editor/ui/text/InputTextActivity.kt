package com.tokopedia.editor.ui.text

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResult
import androidx.activity.viewModels
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.editor.analytics.input.text.InputTextAnalytics
import com.tokopedia.editor.di.ModuleInjector
import com.tokopedia.editor.ui.EditorFragmentProvider
import com.tokopedia.editor.ui.EditorFragmentProviderImpl
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.component.NavToolbarComponent
import com.tokopedia.picker.common.component.ToolbarTheme
import javax.inject.Inject
import com.tokopedia.editor.R as editorR

class InputTextActivity : BaseActivity(), NavToolbarComponent.Listener {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analytics: InputTextAnalytics

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

        initState()
        initFragment()
        initView()
        initObserver()
    }

    override fun onStop() {
        super.onStop()
        finishActivity()
    }

    override fun onCloseClicked() {
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    override fun onContinueClicked() {
        analytics.editSaveClick(
            textDetail = viewModel.getTextDetail()
        )
        finishActivity()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finishActivity()
    }

    private fun initState() {
        intent?.getParcelableExtra<InputTextModel>(EXTRA_INPUT_TEXT_MODEL)?.let {
            var selectedColor = it.textColor

            it.backgroundColor?.let { backgroundColor ->
                viewModel.updateBackgroundState(Pair(it.textColor, backgroundColor))
                selectedColor = backgroundColor
            }

            viewModel.updateText(it.text)
            viewModel.updateSelectedColor(selectedColor)
            viewModel.updateFontStyle(it.fontDetail)
            viewModel.updateAlignment(it.textAlign)
        }
    }

    private fun initView() {
        setupToolbar()
    }

    private fun initObserver() {
        viewModel.isActivitySave.observe(this) {
            if (it) {
                finishActivity()
            }
        }
    }

    private fun setupToolbar() {
        toolbar.onToolbarThemeChanged(ToolbarTheme.Transparent)
        toolbar.setBackVisibility(false)
        toolbar.setTitleVisibility(false)

        toolbar.showContinueButtonAs(true)
        toolbar.setContinueTitle(getString(editorR.string.universal_editor_tools_action_button))
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

    private fun finishActivity() {
        hideSoftKeyboard()
        Handler().postDelayed({
            val resultData = viewModel.getTextDetail()

            val intent = Intent()

            // check if text only contain whitespace
            if (resultData.text.trim().isNotEmpty()) {
                intent.putExtra(INPUT_TEXT_RESULT, resultData)
            }

            setResult(0, intent)
            finish()
        }, HIDE_KEYBOARD_DELAY)
    }

    private fun hideSoftKeyboard() {
        (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            this.currentFocus?.windowToken, 0
        )
    }

    companion object {
        private const val INPUT_TEXT_RESULT = "input_text_result"
        private const val EXTRA_INPUT_TEXT_MODEL = "extra_input_text_model"
        private const val HIDE_KEYBOARD_DELAY = 300L

        fun create(context: Context, model: InputTextModel): Intent {
            return Intent(context, InputTextActivity::class.java).also {
                it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                it.putExtra(EXTRA_INPUT_TEXT_MODEL, model)
            }
        }

        fun result(result: ActivityResult): InputTextModel {
            return result.data?.getParcelableExtra(INPUT_TEXT_RESULT) ?: InputTextModel()
        }
    }
}
