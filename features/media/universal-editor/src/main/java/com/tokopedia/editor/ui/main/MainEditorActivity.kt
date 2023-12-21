@file:SuppressLint("DeprecatedMethod")
@file:Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")

package com.tokopedia.editor.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.editor.R
import com.tokopedia.editor.databinding.ActivityMainEditorBinding
import com.tokopedia.editor.di.ModuleInjector
import com.tokopedia.editor.ui.EditorFragmentProvider
import com.tokopedia.editor.ui.EditorFragmentProviderImpl
import com.tokopedia.editor.ui.component.AudioStateUiComponent
import com.tokopedia.editor.ui.component.GlobalLoaderUiComponent
import com.tokopedia.editor.ui.component.NavigationToolUiComponent
import com.tokopedia.editor.ui.component.PagerContainerUiComponent
import com.tokopedia.editor.ui.dialog.ConfirmationDialog
import com.tokopedia.editor.ui.main.uimodel.InputTextParam
import com.tokopedia.editor.ui.main.uimodel.MainEditorEffect
import com.tokopedia.editor.ui.main.uimodel.MainEditorEvent
import com.tokopedia.editor.ui.model.ImagePlacementModel
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.editor.ui.placement.PlacementImageActivity
import com.tokopedia.editor.ui.text.InputTextActivity
import com.tokopedia.editor.ui.widget.DynamicTextCanvasLayout
import com.tokopedia.editor.util.delegate.ScalableCanvasViewDelegate
import com.tokopedia.editor.util.delegate.ScalableCanvasViewDelegateImpl
import com.tokopedia.editor.util.lib.SafeNativeLoader
import com.tokopedia.picker.common.EXTRA_UNIVERSAL_EDITOR_PARAM
import com.tokopedia.picker.common.PickerResult
import com.tokopedia.picker.common.RESULT_UNIVERSAL_EDITOR
import com.tokopedia.picker.common.UniversalEditorParam
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.component.NavToolbarComponent
import com.tokopedia.picker.common.component.ToolbarTheme
import com.tokopedia.picker.common.types.ToolType
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.LENGTH_SHORT
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import javax.inject.Inject

/**
 * A parent container of Universal Editor.
 *
 * This activity will manage all the ecosystem of Universal Editor, which will handling the
 * scope and area all editor tools. The universal editor module adopting a single activity,
 * which only contain a single entry point.
 *
 * To access the universal editor, please refer to use a built-in intent for this Editor nor
 * you could access this page with this applink:
 *
 * @applink tokopedia-android-internal://global/universal-editor
 */
open class MainEditorActivity : AppCompatActivity(),
    NavToolbarComponent.Listener,
    DynamicTextCanvasLayout.Listener,
    ScalableCanvasViewDelegate by ScalableCanvasViewDelegateImpl() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var confirmationDialog: ConfirmationDialog

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

    private val globalLoader by uiComponent { GlobalLoaderUiComponent(it) }
    private val audioMuteState by uiComponent { AudioStateUiComponent(it) }

    private val inputTextIntent = registerForActivityResult(StartActivityForResult()) {
        val result = InputTextActivity.result(it)
        viewModel.onEvent(MainEditorEvent.InputTextResult(result))
    }

    private val placementIntent = registerForActivityResult(StartActivityForResult()) {
        val result = PlacementImageActivity.result(it)
        viewModel.onEvent(MainEditorEvent.PlacementImageResult(result))
    }

    private val viewModel: MainEditorViewModel by viewModels { viewModelFactory }
    private var binding: ActivityMainEditorBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        SafeNativeLoader.load(this)
        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)
        binding = ActivityMainEditorBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setDataParam()
        initObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)

        if (newBase != null) {
            SafeNativeLoader.load(newBase)
        }
    }

    override fun onCloseClicked() {
        viewModel.onEvent(MainEditorEvent.ClickHeaderCloseButton())
    }

    override fun onContinueClicked() {
        exportFinalResult()
    }

    override fun onTextViewClick(text: View, model: InputTextModel?) {
        if (model == null) return

        binding?.container?.setTextVisibility(text.id, false)
        viewModel.onEvent(MainEditorEvent.EditInputTextPage(text.id, model))
    }

    override fun startViewDrag() {
        //TODO: Animator will be used
    }

    override fun endViewDrag() {
        //TODO: Animator will be used
    }

    override fun onBackPressed() {
        viewModel.onEvent(MainEditorEvent.ClickHeaderCloseButton())
    }

    private fun setDataParam() {
        val param = intent?.getParcelableExtra<UniversalEditorParam>(
            EXTRA_UNIVERSAL_EDITOR_PARAM
        ) ?: error("Please provide the universal media editor parameter.")
        require(param.paths.isNotEmpty()) { "Please add at least 1 media file to edit." }
        require(param.pageSource.isUnknown().not()) { "We unable to find the page source." }

        viewModel.onEvent(MainEditorEvent.SetupView(param))
    }

    private fun initObserver() {
        scalableCanvasRegister(this, binding?.canvasContainer)

        lifecycleScope.launchWhenCreated {
            viewModel.mainEditorState.collect(::initView)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.inputTextState.collect(::addOrEditTextOnLayout)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.uiEffect.collect(::onEffectHandler)
        }
    }

    private fun initView(model: MainEditorUiModel) {
        if (isPageInitialize) return

        // pre view render configuration
        setupToolbar(model.param)
        pagerContainer.setupView(model.param)
        navigationTool.setupView(model.tools)

        // listeners
        binding?.container?.setListener(this)

        isPageInitialize = true
    }

    private fun onEffectHandler(effect: MainEditorEffect) {
        when (effect) {
            is MainEditorEffect.ParentToolbarVisibility -> {
                toolbar.setVisibility(effect.visible)
                navigationTool.setVisibility(effect.visible)
            }
            is MainEditorEffect.CloseMainEditorPage -> {
                viewModel.onEvent(MainEditorEvent.DisposeRemainingTasks)
                finish()
            }
            is MainEditorEffect.ShowCloseDialogConfirmation -> {
                confirmationDialog.show(this@MainEditorActivity) {
                    viewModel.onEvent(MainEditorEvent.ClickHeaderCloseButton(true))
                }
            }
            is MainEditorEffect.UpdateTextAddedState -> {
                val hasTextAdded = binding?.container?.hasTextAdded() ?: return
                viewModel.onEvent(MainEditorEvent.HasTextAdded(hasTextAdded))
            }
            is MainEditorEffect.RemoveAudioState -> {
                navigationTool.setRemoveAudioUiState(effect.isRemoved)
                audioMuteState.onShowOrHideAudioState(effect.isRemoved)
            }
            is MainEditorEffect.OpenPlacementPage -> navigateToPlacementImagePage(effect.sourcePath, effect.model)
            is MainEditorEffect.UpdatePagerSourcePath -> pagerContainer.updateView(effect.newSourcePath)
            is MainEditorEffect.FinishEditorPage -> navigateBackToPickerAndFinishIntent(effect.filePath)
            is MainEditorEffect.ShowToastErrorMessage -> onShowToastErrorMessage(effect.message)
            is MainEditorEffect.OpenInputText -> navigateToInputTextTool(effect.model)
            is MainEditorEffect.ShowLoading -> globalLoader.showLoading()
            is MainEditorEffect.HideLoading -> globalLoader.hideLoading()
        }
    }

    private fun onToolClicked(@ToolType type: Int) {
        when (type) {
            ToolType.TEXT -> viewModel.onEvent(MainEditorEvent.AddInputTextPage)
            ToolType.PLACEMENT -> viewModel.onEvent(MainEditorEvent.PlacementImagePage)
            ToolType.AUDIO_MUTE -> viewModel.onEvent(MainEditorEvent.ManageVideoAudio)
            else -> Unit
        }
    }

    private fun navigateToInputTextTool(model: InputTextModel) {
        val intent = InputTextActivity.create(this, model)
        inputTextIntent.launch(intent)
        overridePendingTransition(0, 0)
    }

    private fun navigateToPlacementImagePage(sourcePath: String, model: ImagePlacementModel?) {
        val intent = PlacementImageActivity.create(this, sourcePath, model)
        placementIntent.launch(intent)
    }

    private fun navigateBackToPickerAndFinishIntent(filePath: String) {
        val result = PickerResult(
            originalPaths = listOf(viewModel.filePath),
            editedPaths = listOf(filePath)
        )

        val intent = Intent()
        intent.putExtra(RESULT_UNIVERSAL_EDITOR, result)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun addOrEditTextOnLayout(state: InputTextParam) {
        val (viewId, model) = state
        if (model == null) return

        binding?.container?.addOrEditText(viewId, model)
        binding?.container?.setTextVisibility(viewId, true)
        viewModel.onEvent(MainEditorEvent.ResetActiveInputText)
    }

    private fun onShowToastErrorMessage(message: String) {
        if (message.isEmpty()) return

        val parent = binding?.container ?: return
        val actionText = getString(R.string.universal_editor_retry)

        val toaster = Toaster.build(parent, message, LENGTH_SHORT, TYPE_ERROR, actionText) {
            exportFinalResult()
        }

        toaster.show()
    }

    private fun exportFinalResult() {
        // grid and deletion view are added as Views. thus, we have to ensure
        // both view are removed before convert it to bitmap.
        binding?.container?.viewsCleanUp()

        val imageBitmap = pagerContainer.getImageBitmap()
        val bitmap = binding?.container?.exportAsBitmap() ?: return

        viewModel.onEvent(MainEditorEvent.ExportMedia(bitmap, imageBitmap))
    }

    private fun setupToolbar(param: UniversalEditorParam) {
        toolbar.onToolbarThemeChanged(ToolbarTheme.Transparent)
        toolbar.showContinueButtonAs(true)
        toolbar.setTitle(defaultHeaderTitle(param))
        toolbar.setContinueTitle(defaultActionButtonText(param))
    }

    private fun defaultHeaderTitle(param: UniversalEditorParam): String {
        if (param.custom.headerTitle.isNotEmpty()) return param.custom.headerTitle
        return getString(R.string.universal_editor_toolbar_title)
    }

    private fun defaultActionButtonText(param: UniversalEditorParam): String {
        if (param.custom.headerActionButton.isNotEmpty()) return param.custom.headerActionButton
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
