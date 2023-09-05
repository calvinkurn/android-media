package com.tokopedia.editor.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.editor.R
import com.tokopedia.editor.di.ModuleInjector
import com.tokopedia.editor.ui.EditorFragmentProvider
import com.tokopedia.editor.ui.EditorFragmentProviderImpl
import com.tokopedia.editor.ui.main.component.NavigationToolUiComponent
import com.tokopedia.editor.ui.main.component.PagerContainerUiComponent
import com.tokopedia.editor.ui.model.ImagePlacementModel
import com.tokopedia.editor.ui.main.uimodel.MainEditorEffect
import com.tokopedia.editor.ui.main.uimodel.MainEditorEvent
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.editor.ui.placement.PlacementImageActivity
import com.tokopedia.editor.ui.text.InputTextActivity
import com.tokopedia.picker.common.EXTRA_UNIVERSAL_EDITOR_PARAM
import com.tokopedia.picker.common.RESULT_UNIVERSAL_EDITOR
import com.tokopedia.picker.common.UniversalEditorParam
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.component.NavToolbarComponent
import com.tokopedia.picker.common.component.ToolbarTheme
import com.tokopedia.picker.common.types.ToolType
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

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var isPageInitialize = false

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
            parent = it,
            listener = ::onToolClicked
        )
    }

    private val inputTextIntent = registerForActivityResult(StartActivityForResult()) {
        val result = InputTextActivity.result(it)
        viewModel.onEvent(MainEditorEvent.InputTextResult(result))
    }

    private val placementIntent = registerForActivityResult(StartActivityForResult()) {
        val result = PlacementImageActivity.result(it)
        viewModel.onEvent(MainEditorEvent.PlacementImageResult(result))

//        if (result?.path?.isNotEmpty() == true) {
//            viewModel.updatePlacement(result)
//        }
    }

    private val viewModel: MainEditorViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_editor)

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

    @Suppress("DEPRECATION")
    private fun setDataParam() {
        val param = intent?.getParcelableExtra<UniversalEditorParam>(EXTRA_UNIVERSAL_EDITOR_PARAM)
            ?: error("Please provide the universal media editor parameter.")

        viewModel.onEvent(MainEditorEvent.SetupView(param))
    }

    private fun initObserver() {
        lifecycleScope.launchWhenCreated {
            viewModel.mainEditorState.collect(::initView)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.uiEffect.collect {
                when (it) {
                    is MainEditorEffect.OpenInputText -> {
                        navigateToInputTextTool(it.model)
                    }
                    is MainEditorEffect.ParentToolbarVisibility -> {
                        toolbar.setVisibility(it.visible)
                        navigationTool.setVisibility(it.visible)
                    }
                    is MainEditorEffect.OpenPlacementPage -> {
                        navigateToPlacementImagePage(it.sourcePath, it.model)
                    }
                    is MainEditorEffect.UpdatePagerSourcePath -> {
                        pagerContainer.updateView(it.newSourcePath)
                    }
                }
            }
        }
    }

    private fun initView(model: MainEditorUiModel) {
        if (isPageInitialize) return
        setupToolbar(model.param)
        pagerContainer.setupView(model.param)
        navigationTool.setupView(model.tools)
        isPageInitialize = true
    }

    private fun onToolClicked(@ToolType type: Int) {
        when (type) {
            ToolType.TEXT -> viewModel.onEvent(MainEditorEvent.AddInputTextPage)
            ToolType.PLACEMENT -> viewModel.onEvent(MainEditorEvent.PlacementImagePage)
            ToolType.AUDIO_MUTE -> {}
            else -> Unit
        }
    }

    private fun navigateToInputTextTool(model: InputTextModel) {
        val intent = InputTextActivity.create(this, model)
        inputTextIntent.launch(intent)
        overridePendingTransition(0,0)
    }

    private fun navigateToPlacementImagePage(sourcePath: String, model: ImagePlacementModel?) {
        val intent = PlacementImageActivity.create(this, sourcePath, model)
        placementIntent.launch(intent)
    }

    private fun setupToolbar(param: UniversalEditorParam) {
        toolbar.onToolbarThemeChanged(ToolbarTheme.Transparent)
        toolbar.showContinueButtonAs(true)
        toolbar.setTitle(defaultHeaderTitle(param))
        toolbar.setContinueTitle(defaultActionButtonText(param))
    }

    private fun defaultHeaderTitle(param: UniversalEditorParam): String {
        if (param.headerTitle.isNotEmpty()) return param.headerTitle
        return getString(R.string.universal_editor_toolbar_title)
    }

    private fun defaultActionButtonText(param: UniversalEditorParam): String {
        if (param.proceedButtonText.isNotEmpty()) return param.proceedButtonText
        return getString(R.string.universal_editor_toolbar_action_button)
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
