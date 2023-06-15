package com.tokopedia.media.editor.ui.activity.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.media.editor.analytics.editorhome.EditorHomeAnalytics
import com.tokopedia.media.editor.base.BaseEditorActivity
import com.tokopedia.media.editor.di.EditorInjector
import com.tokopedia.media.editor.ui.activity.detail.DetailEditorActivity
import com.tokopedia.media.editor.ui.fragment.EditorFragment
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.utils.*
import com.tokopedia.picker.common.*
import com.tokopedia.picker.common.cache.EditorCacheManager
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.utils.file.cleaner.InternalStorageCleaner
import javax.inject.Inject
import com.tokopedia.media.editor.R as editorR

class EditorActivity : BaseEditorActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var pickerParam: PickerCacheManager

    @Inject
    lateinit var editorParam: EditorCacheManager

    @Inject
    lateinit var editorHomeAnalytics: EditorHomeAnalytics

    private val viewModel: EditorViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(EditorViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)

        setHeader(
            getString(editorR.string.editor_main_header_title_text),
            getString(editorR.string.editor_main_header_action_text)
        )

        InternalStorageCleaner.cleanUpInternalStorageIfNeeded(
            this,
            getEditorSaveFolderPath()
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(CACHE_PARAM_INTENT_DATA, editorParam.get())
    }

    override fun getNewFragment(): Fragment {
        return fragmentProvider().editorFragment()
    }

    override fun initViewModel() {}

    override fun initBundle(savedInstanceState: Bundle?) {
        intent?.getParcelableExtra<EditorParam>(EXTRA_EDITOR_PARAM)?.also {
            editorParam.set(it)
            viewModel.setEditorParam(it)
        }

        intent?.getParcelableExtra<EditorImageSource>(EXTRA_INTENT_EDITOR)?.also {
            viewModel.initStateList(it.originalPaths)
        }

        intent?.getParcelableExtra<PickerParam>(EXTRA_PICKER_PARAM)?.also {
            pickerParam.set(it)
        }
    }

    override fun initInjector() {
        EditorInjector
            .get(applicationContext)
            .inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == DetailEditorActivity.EDITOR_RESULT_CODE) {
            val editorDetailResultData =
                data?.getParcelableExtra<EditorDetailUiModel>(DetailEditorActivity.EDITOR_RESULT_PARAM)
            editorDetailResultData?.let {
                viewModel.addEditState(it.originalUrl, it)
            }
        }
    }

    override fun onBackPressed() {
        val fragment = (fragment as EditorFragment)
        if (fragment.isShowDialogConfirmation()) {
            showBackDialogConfirmation()
        } else {
            super.onBackPressed()
        }
    }

    override fun onHeaderActionClick() {
        finishPage()
    }

    override fun onBackClicked() {
        editorHomeAnalytics.clickBackButton()
    }

    private fun finishPage() {
        val listImageEditState = viewModel.editStateList.values.toList()
        viewModel.finishPage(
            listImageEditState
        ) { imageResultList ->
            imageResultList?.let {
                val result = EditorResult(
                    originalPaths = listImageEditState.map { it.getOriginalUrl() },
                    editedImages = imageResultList
                )

                editorHomeAnalytics.clickUpload()

                val intent = Intent()
                intent.putExtra(RESULT_INTENT_EDITOR, result)
                setResult(Activity.RESULT_OK, intent)
            }

            finish()
        }
    }

    private fun showBackDialogConfirmation() {
        DialogUnify(this, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(editorR.string.editor_activity_dialog_title))
            setDescription(getString(editorR.string.editor_activity_dialog_desc))

            dialogPrimaryCTA.apply {
                text = getString(editorR.string.editor_activity_dialog_primary_button_text)
                setOnClickListener {
                    super.onBackPressed()
                }
            }

            dialogSecondaryLongCTA.apply {
                text = getString(editorR.string.editor_activity_dialog_secondary_button_text)
                setOnClickListener {
                    hide()
                }
            }

            show()
        }
    }

    companion object {
        private const val CACHE_PARAM_INTENT_DATA = "intent_data.param_editor"
        private const val PERMISSION_REQUEST_CODE = 197
    }

}
