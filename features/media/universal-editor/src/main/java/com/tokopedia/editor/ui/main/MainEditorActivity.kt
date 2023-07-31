package com.tokopedia.editor.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.editor.databinding.ActivityMainEditorBinding
import com.tokopedia.editor.di.ModuleInjector
import com.tokopedia.editor.ui.EditorFragmentProvider
import com.tokopedia.editor.ui.EditorFragmentProviderImpl
import com.tokopedia.editor.ui.main.component.NavigationToolUiComponent
import com.tokopedia.editor.ui.main.component.PagerContainerUiComponent
import com.tokopedia.picker.common.EXTRA_UNIVERSAL_EDITOR_PARAM
import com.tokopedia.picker.common.RESULT_UNIVERSAL_EDITOR
import com.tokopedia.picker.common.UniversalEditorParam
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.component.NavToolbarComponent
import com.tokopedia.picker.common.component.ToolbarTheme
import javax.inject.Inject

/**
 * A parent container of Universal Editor.
 *
 * This activity will manage all the ecosystem of Universal Editor, which will handling the
 * scope and area all editor tools. The universal editor module adopting a single activity,
 * which only contain a single entry point.
 *
 * To access the universal editor, please refer to use a built-in intent in [UniversalEditor] nor
 * you could access this page with this applink:
 *
 * @applink tokopedia-android-internal://global/universal-editor
 */
open class MainEditorActivity : AppCompatActivity(), NavToolbarComponent.Listener {

    @Inject lateinit var fragmentFactory: FragmentFactory
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val toolbar by uiComponent {
        NavToolbarComponent(
            listener = this,
            parent = it,
            useArrowIcon = true
        )
    }

    private val pagerContainer by uiComponent {
        PagerContainerUiComponent(
            parent = it,
            fragment = fragmentProvider(),
            activity = this
        )
    }

    private val navigationTool by uiComponent {
        NavigationToolUiComponent(
            parent = it
        )
    }

    private val viewModel: MainEditorViewModel by viewModels { viewModelFactory }

    private lateinit var binding: ActivityMainEditorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)

        binding = ActivityMainEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDataParam()
        initObserver()
    }

    override fun onCloseClicked() {
        finish()
    }

    override fun onContinueClicked() {
        val intent = Intent()
        intent.putExtra(RESULT_UNIVERSAL_EDITOR, "")
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun setDataParam() {
        val param = intent?.getParcelableExtra<UniversalEditorParam>(
            EXTRA_UNIVERSAL_EDITOR_PARAM
        ) ?: error("Please provide the universal media editor parameter.")

        viewModel.setAction(MainEditorEvent.SetParam(param))
    }

    private fun initObserver() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                initView(it.param)

                // Navigation tool
                navigationTool.setupView(it.tools)
            }
        }
    }

    private fun initView(param: UniversalEditorParam) {
        pagerContainer.setupView(param)
        setupToolbar(param)
    }

    private fun setupToolbar(param: UniversalEditorParam) {
        toolbar.onToolbarThemeChanged(ToolbarTheme.Transparent)
        toolbar.setTitle(getString(param.headerTitle))
        toolbar.setContinueTitle(getString(param.proceedButtonText))
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
}
