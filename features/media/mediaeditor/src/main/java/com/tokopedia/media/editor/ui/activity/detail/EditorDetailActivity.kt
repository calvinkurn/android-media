package com.tokopedia.media.editor.ui.activity.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.media.editor.di.EditorInjector
import com.tokopedia.media.editor.ui.activity.BaseEditorActivity
import com.tokopedia.media.editor.ui.param.EditorParam
import com.tokopedia.picker.common.types.EditorToolType
import javax.inject.Inject

class EditorDetailActivity : BaseEditorActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @EditorToolType
    private var editorToolType: Int = EditorToolType.NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        initBundle(savedInstanceState)
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment {
        return fragmentProvider().editorDetailFragment()
    }

    private fun initBundle(bundle: Bundle?) {
        val param: EditorParam? = bundle?.getParcelable(PARAM_EDITOR_DETAIL)
        if (param == null) finish()

        editorToolType = param?.editorToolType?: EditorToolType.NONE
    }

    private fun initInjector() {
        EditorInjector
            .get(applicationContext)
            .inject(this)
    }

    companion object {
        private const val PARAM_EDITOR_DETAIL = "param.editor_detail"

        fun start(context: Context, param: EditorParam) {
            val intent = Intent(context, EditorDetailActivity::class.java).apply {
                putExtra(PARAM_EDITOR_DETAIL, param)
            }
            context.startActivity(intent)
        }
    }

}