package com.tokopedia.editor.ui.text

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.editor.R
import com.tokopedia.editor.di.ModuleInjector
import com.tokopedia.editor.ui.EditorFragmentProvider
import com.tokopedia.editor.ui.EditorFragmentProviderImpl
import com.tokopedia.editor.ui.main.MainEditorViewModel
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
        setContentView(R.layout.activity_input_text)

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

    private fun initObserver() {}

    private fun setupToolbar() {
        toolbar.onToolbarThemeChanged(ToolbarTheme.Transparent)
        toolbar.setBackVisibility(false)
        toolbar.setTitleVisibility(false)

        toolbar.showContinueButtonAs(true)
    }

    private fun initFragment() {
        val fragment = fragmentProvider().inputTextFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_view, fragment, "Tag")
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

    companion object {
        const val INPUT_TEXT_RESULT = "input_text_result"

        fun create(context: Context): Intent {
            return Intent(context, InputTextActivity::class.java)
        }
    }
}
