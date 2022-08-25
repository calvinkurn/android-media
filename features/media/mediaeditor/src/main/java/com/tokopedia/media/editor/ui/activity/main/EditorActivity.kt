package com.tokopedia.media.editor.ui.activity.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.media.editor.R as editorR
import com.tokopedia.media.editor.di.EditorInjector
import com.tokopedia.media.editor.base.BaseEditorActivity
import com.tokopedia.media.editor.ui.activity.detail.DetailEditorActivity
import com.tokopedia.media.editor.ui.fragment.EditorFragment
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.utils.deleteRecursive
import com.tokopedia.media.editor.utils.getEditorSaveFolderDir
import com.tokopedia.picker.common.EXTRA_EDITOR_PARAM
import com.tokopedia.picker.common.EditorParam
import com.tokopedia.picker.common.ImageRatioType
import java.io.File
import javax.inject.Inject

class EditorActivity : BaseEditorActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory
    lateinit var viewModel: EditorViewModel

    private var param = EditorParam()

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)

        setHeader(
            getString(editorR.string.editor_main_header_title_text),
            getString(editorR.string.editor_main_header_action_text)
        )
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

        // sample data
        val editorParam = EditorParam()
        editorParam.withRemoveBackground()
        editorParam.withWatermark()
        editorParam.autoCropRatio = ImageRatioType.RATIO_2_1

        viewModel.setEditorParam(editorParam)

        // temporary, will remove later
        viewModel.initStateList(
            arrayListOf(
                "/storage/emulated/0/Pictures/iN76Hq7.jpeg",
                "/storage/emulated/0/Pictures/aaditya-ailawadhi-D6pgxi3gwNQ-unsplash 1.png",
                "/storage/emulated/0/Pictures/Screen Shot 2021-07-12 at 15.23.45.png",
                "/storage/emulated/0/Pictures/pirate.mp4"
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
        if (resultCode == DetailEditorActivity.EDITOR_RESULT_CODE) {
            val asd =
                data?.getParcelableExtra<EditorDetailUiModel>(DetailEditorActivity.EDITOR_RESULT_PARAM)
            asd?.let {
                viewModel.addEditState(it.originalUrl, it)
            }
        }
    }

    override fun onBackPressed() {
        val fragment = (fragment as EditorFragment)
        if (fragment.isShowDialogConfirmation()){
            showBackDialogConfirmation()
        } else {
            super.onBackPressed()
        }
    }

    private fun showBackDialogConfirmation(){
        var dialog = DialogUnify(this, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(editorR.string.editor_activity_dialog_title))
        dialog.setDescription(getString(editorR.string.editor_activity_dialog_desc))

        dialog.dialogPrimaryCTA.apply {
            text = getString(editorR.string.editor_activity_dialog_primary_button_text)
            setOnClickListener {
                super.onBackPressed()
            }
        }

        dialog.dialogSecondaryLongCTA.apply {
            text = getString(editorR.string.editor_activity_dialog_secondary_button_text)
            setOnClickListener {
                dialog.hide()
            }
        }

        dialog.show()
    }

    override fun onDestroy() {
        val editorCacheFolder = File(getEditorSaveFolderDir(this))
        deleteRecursive(editorCacheFolder)
        super.onDestroy()
    }

    companion object {
        private const val CACHE_PARAM_INTENT_DATA = "intent_data.param_editor"
    }

}