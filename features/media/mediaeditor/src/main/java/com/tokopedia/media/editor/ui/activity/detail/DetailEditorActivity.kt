package com.tokopedia.media.editor.ui.activity.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.media.editor.di.EditorInjector
import com.tokopedia.media.editor.ui.activity.BaseEditorActivity
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import javax.inject.Inject

class DetailEditorActivity : BaseEditorActivity() {

    @Inject lateinit var fragmentFactory: FragmentFactory
    lateinit var viewModel: DetailEditorViewModel

    private var editorIntent = EditorDetailUiModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(CACHE_EDITOR_INTENT_DATA, editorIntent)
    }

    override fun getNewFragment(): Fragment {
        return fragmentProvider().editorDetailFragment()
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this
        ).get(DetailEditorViewModel::class.java)
    }

    override fun initBundle(savedInstanceState: Bundle?) {
        val data = savedInstanceState
            ?.getParcelable(CACHE_EDITOR_INTENT_DATA)
            ?: intent?.getParcelableExtra<EditorDetailUiModel>(PARAM_EDITOR_DETAIL)?.also {
                editorIntent = it
            }

        if (data == null) finish()
        viewModel.setIntentOnUiModel(data!!)
    }

    override fun initInjector() {
        EditorInjector
            .get(applicationContext)
            .inject(this)
    }

    companion object {
        private const val CACHE_EDITOR_INTENT_DATA = "intent_data.editor_detail"

        private const val PARAM_EDITOR_DETAIL = "param.editor_detail"

        fun start(context: Context, detailUiModel: EditorDetailUiModel) {
            val intent = Intent(context, DetailEditorActivity::class.java).apply {
                putExtra(PARAM_EDITOR_DETAIL, detailUiModel)
            }

            context.startActivity(intent)
        }
    }

}