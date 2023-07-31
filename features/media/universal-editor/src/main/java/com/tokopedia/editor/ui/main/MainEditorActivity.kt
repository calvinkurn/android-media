package com.tokopedia.editor.ui.main

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
import com.tokopedia.editor.ui.main.component.PagerContainerUiComponent
import com.tokopedia.picker.common.EXTRA_UNIVERSAL_EDITOR_PARAM
import com.tokopedia.picker.common.UniversalEditorParam
import com.tokopedia.picker.common.basecomponent.uiComponent
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
open class MainEditorActivity : AppCompatActivity() {

    @Inject lateinit var fragmentFactory: FragmentFactory
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val pagerContainer by uiComponent {
        PagerContainerUiComponent(it, fragmentProvider(), this)
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

    private fun setDataParam() {
        val param = intent?.getParcelableExtra<UniversalEditorParam>(
            EXTRA_UNIVERSAL_EDITOR_PARAM
        ) ?: error("Please provide the universal media editor parameter.")

        viewModel.setParam(param)
    }

    private fun initObserver() {
        lifecycleScope.launchWhenCreated {
            viewModel.param.collect { param ->
                if (param == null) return@collect

                dismissIfNeeded(param.paths)
                pagerContainer.setupView(param)
            }
        }
    }

    private fun dismissIfNeeded(paths: List<String>) {
        val shouldMediaPathExist = paths.isNotEmpty()
        if (shouldMediaPathExist) return

        error("You have to at least add 1 media file using filePaths(listOf()) in builder.")
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
