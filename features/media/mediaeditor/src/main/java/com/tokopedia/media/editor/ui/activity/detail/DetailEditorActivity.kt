package com.tokopedia.media.editor.ui.activity.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.media.editor.base.BaseEditorActivity
import com.tokopedia.media.editor.di.EditorInjector
import com.tokopedia.media.editor.ui.fragment.DetailEditorFragment
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorUiModel
import com.tokopedia.media.editor.utils.getToolEditorText
import com.tokopedia.picker.common.EditorParam
import com.tokopedia.picker.common.cache.PickerCacheManager
import javax.inject.Inject
import com.tokopedia.media.editor.R as editorR

class DetailEditorActivity : BaseEditorActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var pickerParam: PickerCacheManager

    private val viewModel: DetailEditorViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(DetailEditorViewModel::class.java)
    }

    private var editorIntent = EditorDetailUiModel()
    private var editorParam = EditorParam()
    private var editorModel = EditorUiModel()

    override fun onHeaderActionClick() {
        if (editorIntent.isToolAddLogo()) {
            (fragment as DetailEditorFragment).showAddLogoUploadTips(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(CACHE_EDITOR_INTENT_DATA, editorIntent)
        outState.putParcelable(CACHE_EDITOR_INTENT_MODEL, editorModel)
        outState.putParcelable(CACHE_EDITOR_PARAM, editorParam)
    }

    override fun getNewFragment(): Fragment {
        return fragmentProvider().editorDetailFragment()
    }

    override fun initViewModel() {}

    override fun initBundle(savedInstanceState: Bundle?) {
        (savedInstanceState?.getParcelable(CACHE_EDITOR_INTENT_MODEL)
            ?: intent?.getParcelableExtra<EditorUiModel>(PARAM_EDITOR_MODEL))?.also {
            editorModel = it
            viewModel.setIntentUiModel(it)
        }

        (savedInstanceState?.getParcelable(CACHE_EDITOR_INTENT_DATA)
            ?: intent?.getParcelableExtra<EditorDetailUiModel>(PARAM_EDITOR_DETAIL))?.also {
            editorIntent = it
            viewModel.setIntentDetailUiModel(it)
        }

        (savedInstanceState?.getParcelable(CACHE_EDITOR_PARAM)
            ?: intent?.getParcelableExtra<EditorParam>(PARAM_EDITOR))?.also {
            editorParam = it
            viewModel.setEditorParam(it)
        }

        setHeader(
            getString(getToolEditorText(editorIntent.editorToolType)),
            rightIcon = if (editorIntent.isToolAddLogo()) IconUnify.INFORMATION else null
        )
    }

    override fun onStop() {
        super.onStop()
        pickerParam.disposeSubPicker()
    }

    override fun initInjector() {
        EditorInjector
            .get(applicationContext)
            .inject(this)
    }

    override fun onBackPressed() {
        val fragment = (fragment as DetailEditorFragment)
        if (fragment.isShowDialogConfirmation()) {
            showBackDialogConfirmation(onPrimaryClick = {
                fragment.saveAndExit()
            }, onSecondaryClick = {
                super.onBackPressed()
            })
        } else {
            super.onBackPressed()
        }
    }

    override fun onBackClicked() {}

    private fun showBackDialogConfirmation(
        onPrimaryClick: () -> Unit,
        onSecondaryClick: () -> Unit
    ) {
        DialogUnify(this, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(editorR.string.editor_detail_activity_dialog_title))
            setDescription(getString(editorR.string.editor_detail_activity_dialog_desc))

            dialogPrimaryCTA.apply {
                text = getString(editorR.string.editor_detail_activity_dialog_primary_button_text)
                setOnClickListener {
                    onPrimaryClick()
                }
            }

            dialogSecondaryLongCTA.apply {
                text = getString(editorR.string.editor_detail_activity_dialog_secondary_button_text)
                setOnClickListener {
                    onSecondaryClick()
                }
            }

            show()
        }
    }

    companion object {
        private const val CACHE_EDITOR_INTENT_DATA = "intent_data.editor_detail"
        private const val CACHE_EDITOR_INTENT_MODEL = "intent_data.editor_model"
        private const val CACHE_EDITOR_PARAM = "intent_data.editor_detail"

        const val PARAM_EDITOR_DETAIL = "param.editor_detail"
        const val PARAM_EDITOR_MODEL = "param.editor.model"
        const val PARAM_EDITOR = "param.editor"

        const val EDITOR_RESULT_PARAM = "intent_data.editor_result"
        const val EDITOR_RESULT_CODE = 798
    }

}
