package com.tokopedia.editor.ui.placement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.editor.R
import com.tokopedia.editor.di.ModuleInjector
import com.tokopedia.editor.ui.EditorFragmentProvider
import com.tokopedia.editor.ui.EditorFragmentProviderImpl
import com.tokopedia.editor.ui.model.ImagePlacementModel
import com.tokopedia.picker.common.UniversalEditorParam
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.component.NavToolbarComponent
import com.tokopedia.picker.common.component.ToolbarTheme
import com.tokopedia.utils.file.FileUtil
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

        // clear cache file if more than 1 day old
        InternalStorageCleaner.cleanUpInternalStorageIfNeeded(
            this,
            getEditorCacheFolderPath()
        )
    }

    override fun onCloseClicked() {}

    override fun onContinueClicked() {
        (supportFragmentManager.findFragmentById(R.id.fragment_view) as PlacementImageFragment).let {
            it.captureImage { placementModel ->
                val intent = Intent()
                intent.putExtra(PLACEMENT_RESULT_KEY, placementModel)

                setResult(0, intent)
                finish()
            }
        }
    }

    fun getEditorCacheFolderPath(): String {
        return FileUtil.getTokopediaInternalDirectory(CACHE_FOLDER).path
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

    companion object {
        private const val CACHE_FOLDER = "Tokopedia/editor-stories"

        const val PLACEMENT_RESULT_KEY = "input_placement_result"

        const val PLACEMENT_PARAM_KEY = "placement_param_key"
        const val PLACEMENT_MODEL_KEY = "placement_model_key"

        fun create(context: Context): Intent {
            return Intent(context, PlacementImageActivity::class.java)
        }
    }
}
