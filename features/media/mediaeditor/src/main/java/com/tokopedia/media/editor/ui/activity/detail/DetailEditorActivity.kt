package com.tokopedia.media.editor.ui.activity.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.media.editor.R as editorR
import com.tokopedia.media.editor.di.EditorInjector
import com.tokopedia.media.editor.base.BaseEditorActivity
import com.tokopedia.media.editor.ui.fragment.DetailEditorFragment
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.utils.getToolEditorText
import com.tokopedia.picker.common.EditorParam
import javax.inject.Inject

class DetailEditorActivity : BaseEditorActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var fragmentFactory: FragmentFactory

    lateinit var viewModel: DetailEditorViewModel

    private var editorIntent = EditorDetailUiModel()
    private var editorParam = EditorParam()

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)

        setHeader(
            getString(getToolEditorText(editorIntent.editorToolType))
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(CACHE_EDITOR_INTENT_DATA, editorIntent)
        outState.putParcelable(CACHE_EDITOR_PARAM, editorParam)
    }

    override fun getNewFragment(): Fragment {
        return fragmentProvider().editorDetailFragment()
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(DetailEditorViewModel::class.java)
    }

    override fun initBundle(savedInstanceState: Bundle?) {
        (savedInstanceState?.getParcelable(CACHE_EDITOR_INTENT_DATA)
            ?: intent?.getParcelableExtra<EditorDetailUiModel>(PARAM_EDITOR_DETAIL))?.also {
            editorIntent = it
            viewModel.setIntentDetailUiModel(it)
        }

        (savedInstanceState?.getParcelable(CACHE_EDITOR_INTENT_DATA)
            ?: intent?.getParcelableExtra<EditorParam>(PARAM_EDITOR))?.also {
            editorParam = it
            viewModel.setEditorParam(it)
        }
    }

    override fun initInjector() {
        EditorInjector
            .get(applicationContext)
            .inject(this)
    }

    override fun onBackPressed() {
        val fragment = (fragment as DetailEditorFragment)
        if (fragment.isShowDialogConfirmation()){
            showBackDialogConfirmation(onPrimaryClick = {
                fragment.saveAndExit()
            }, onSecondaryClick = {
                super.onBackPressed()
            })
        } else {
            super.onBackPressed()
        }
    }

    private fun showBackDialogConfirmation(onPrimaryClick: () -> Unit, onSecondaryClick: () -> Unit){
        var dialog = DialogUnify(this, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(editorR.string.editor_detail_activity_dialog_title))
        dialog.setDescription(getString(editorR.string.editor_detail_activity_dialog_desc))

        dialog.dialogPrimaryCTA.apply {
            text = getString(editorR.string.editor_detail_activity_dialog_primary_button_text)
            setOnClickListener {
                onPrimaryClick()
            }
        }

        dialog.dialogSecondaryLongCTA.apply {
            text = getString(editorR.string.editor_detail_activity_dialog_secondary_button_text)
            setOnClickListener {
                onSecondaryClick()
            }
        }

        dialog.show()
    }

    companion object {
        private const val CACHE_EDITOR_INTENT_DATA = "intent_data.editor_detail"
        private const val CACHE_EDITOR_PARAM = "intent_data.editor_detail"

        const val PARAM_EDITOR_DETAIL = "param.editor_detail"
        const val PARAM_EDITOR = "param.editor"

        const val EDITOR_RESULT_PARAM = "intent_data.editor_result"
        const val EDITOR_RESULT_CODE = 798
    }

}