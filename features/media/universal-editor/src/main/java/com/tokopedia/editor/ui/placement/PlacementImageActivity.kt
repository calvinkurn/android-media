package com.tokopedia.editor.ui.placement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.viewModels
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.editor.R
import com.tokopedia.editor.di.ModuleInjector
import com.tokopedia.editor.ui.EditorFragmentProvider
import com.tokopedia.editor.ui.EditorFragmentProviderImpl
import com.tokopedia.editor.ui.model.ImagePlacementModel
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.editor.ui.text.InputTextActivity
import com.tokopedia.editor.util.getEditorCacheFolderPath
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.component.NavToolbarComponent
import com.tokopedia.picker.common.component.ToolbarTheme
import com.tokopedia.utils.file.cleaner.InternalStorageCleaner
import javax.inject.Inject

class PlacementImageActivity : BaseActivity(), NavToolbarComponent.Listener {
    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: PlacementImageViewModel by viewModels { viewModelFactory }

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
        setContentView(R.layout.activity_placement)

        initBundle(savedInstanceState)
        initFragment()
        initView()
        initObserver()

        // clear cache file if more than 1 day old
        InternalStorageCleaner.cleanUpInternalStorageIfNeeded(
            this,
            getEditorCacheFolderPath()
        )
    }

    override fun onCloseClicked() {
        onBackPressed()
    }

    override fun onContinueClicked() {
        getFragment().let {
            viewModel.updateLoadingState(true)
            it.captureImage()
        }
    }

    override fun onBackPressed() {
        onPageExit()
    }

    private fun initBundle(savedInstanceState: Bundle?) {
        (savedInstanceState?.getString(PLACEMENT_PARAM_KEY)
            ?: intent?.getStringExtra(PLACEMENT_PARAM_KEY))?.also {
            viewModel.setImagePath(it)
        }

        (savedInstanceState?.getParcelable<ImagePlacementModel>(PLACEMENT_MODEL_KEY)
            ?: intent?.getParcelableExtra(PLACEMENT_MODEL_KEY))?.also {
            viewModel.setPlacementModel(it)
        }
    }

    private fun initInjector() {
        ModuleInjector
            .get(this)
            .inject(this)
    }

    private fun initFragment() {
        val fragment = fragmentProvider().placementImageFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_view, fragment, "Tag")
            .commit()
    }

    private fun initObserver() {
        viewModel.placementModelResult.observe(this) {
            it?.let { resultModel ->
                viewModel.updateLoadingState(false)
                setResult(
                    RESULT_OK,
                    createIntentResult(resultModel)
                )
                finish()
            }
        }
    }

    private fun fragmentProvider(): EditorFragmentProvider {
        return EditorFragmentProviderImpl(
            supportFragmentManager,
            applicationContext.classLoader
        )
    }

    private fun setupToolbar() {
        toolbar.onToolbarThemeChanged(ToolbarTheme.Transparent)
        toolbar.showContinueButtonAs(true)
        toolbar.setTitle(getString(R.string.universal_editor_nav_bar_add_text))
    }

    private fun initView() {
        setupToolbar()
    }

    private fun onPageExit() {
        if (viewModel.isShowExitConfirmation(getFragment().getCurrentMatrixValue())) {
            DialogUnify(this, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply dialog@{
                setTitle(getString(R.string.universal_editor_input_tool_confirmation_title))
                setDescription(getString(R.string.universal_editor_input_tool_confirmation_desc))

                dialogPrimaryCTA.apply {
                    text = getString(R.string.universal_editor_input_tool_confirmation_primary_cta)
                    setOnClickListener {
                        onContinueClicked()
                    }
                }

                dialogSecondaryLongCTA.apply {
                    text =
                        getString(R.string.universal_editor_input_tool_confirmation_secondary_cta)
                    setOnClickListener {
                        finish()
                    }
                }

                show()
            }
        } else {
            finish()
        }
    }

    private fun getFragment(): PlacementImageFragment {
        fragmentProvider()
        return supportFragmentManager.findFragmentById(R.id.fragment_view) as PlacementImageFragment
    }

    companion object {
        const val PLACEMENT_RESULT_KEY = "input_placement_result"

        const val PLACEMENT_PARAM_KEY = "placement_param_key"
        const val PLACEMENT_MODEL_KEY = "placement_model_key"

        fun create(context: Context, imagePath: String, previousState: ImagePlacementModel?): Intent {
            val intent = Intent(context, PlacementImageActivity::class.java)

            intent.putExtra(PLACEMENT_PARAM_KEY, imagePath)
            intent.putExtra(PLACEMENT_MODEL_KEY, previousState)

            return intent
        }

        fun result(result: ActivityResult): ImagePlacementModel? {
            return result.data?.getParcelableExtra(PLACEMENT_RESULT_KEY)
        }

        fun createIntentResult(placementModel: ImagePlacementModel?): Intent {
            val intent = Intent()
            intent.putExtra(PLACEMENT_RESULT_KEY, placementModel)

            return intent
        }
    }
}
