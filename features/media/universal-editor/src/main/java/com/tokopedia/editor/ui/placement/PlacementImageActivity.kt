package com.tokopedia.editor.ui.placement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.editor.R
import com.tokopedia.editor.di.ModuleInjector
import com.tokopedia.editor.ui.EditorFragmentProvider
import com.tokopedia.editor.ui.EditorFragmentProviderImpl
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.component.NavToolbarComponent
import com.tokopedia.picker.common.component.ToolbarTheme
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

        initFragment()
        initView()
    }

    override fun onCloseClicked() {}

    override fun onContinueClicked() {
        (supportFragmentManager.findFragmentById(R.id.fragment_view) as PlacementImageFragment).captureImage {
            // TODO: send result
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
        const val INPUT_PLACEMENT_RESULT = "input_placement_result"

        fun create(context: Context): Intent {
            return Intent(context, PlacementImageActivity::class.java)
        }
    }
}
