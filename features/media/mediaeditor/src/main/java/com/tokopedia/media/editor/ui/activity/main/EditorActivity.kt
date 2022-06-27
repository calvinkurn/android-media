package com.tokopedia.media.editor.ui.activity.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.media.editor.di.EditorInjector
import com.tokopedia.media.editor.ui.activity.BaseEditorActivity
import com.tokopedia.picker.common.EXTRA_EDITOR_PARAM
import com.tokopedia.picker.common.EditorParam
import javax.inject.Inject

class EditorActivity : BaseEditorActivity() {

    @Inject lateinit var fragmentFactory: FragmentFactory
    lateinit var viewModel: EditorViewModel

    private var param = EditorParam()

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(CACHE_PARAM_INTENT_DATA, param)
    }

    override fun getNewFragment(): Fragment {
        return fragmentProvider().editorFragment()
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this
        ).get(EditorViewModel::class.java)
    }

    override fun initBundle(savedInstanceState: Bundle?) {
        val data = savedInstanceState
            ?.getParcelable(CACHE_PARAM_INTENT_DATA)
            ?: intent?.getParcelableExtra<EditorParam>(EXTRA_EDITOR_PARAM)?.also {
                param = it
            }

        data?.let {
            viewModel.setEditorParam(it)
        }
    }

    override fun initInjector() {
        EditorInjector
            .get(applicationContext)
            .inject(this)
    }

    companion object {
        private const val CACHE_PARAM_INTENT_DATA = "intent_data.param_editor"
    }

}