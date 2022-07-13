package com.tokopedia.media.editor.ui.activity.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.media.editor.di.EditorInjector
import com.tokopedia.media.editor.base.BaseEditorActivity
import com.tokopedia.media.editor.ui.activity.detail.DetailEditorActivity
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
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

//        data?.let {
//            viewModel.setEditorParam(it)
//        }

        // sample data
        val editorParam = EditorParam()
        editorParam.withRemoveBackground()

        viewModel.setEditorParam(editorParam)
        viewModel.initStateList(
            arrayListOf(
                "https://images.unsplash.com/photo-1579353977828-2a4eab540b9a?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8c2FtcGxlfGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=800&q=60",
                "https://images.unsplash.com/photo-1561336313-0bd5e0b27ec8?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8c2FtcGxlfGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=800&q=60",
                "https://images.unsplash.com/photo-1531361171768-37170e369163?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTF8fHNhbXBsZXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=800&q=60"
            )
        )
    }

    override fun initInjector() {
        EditorInjector
            .get(applicationContext)
            .inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == DetailEditorActivity.EDITOR_RESULT_CODE){
            val asd = data?.getParcelableExtra<EditorDetailUiModel>(DetailEditorActivity.EDITOR_RESULT_PARAM)
            asd?.let {
                viewModel.addEditState(it.originalUrl, it)
            }
        }
    }

    companion object {
        private const val CACHE_PARAM_INTENT_DATA = "intent_data.param_editor"
    }

}