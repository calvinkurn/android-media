package com.tokopedia.media.editor.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visibleWithCondition
import com.tokopedia.media.editor.R as editorR
import com.tokopedia.media.editor.base.BaseEditorFragment
import com.tokopedia.media.editor.databinding.FragmentMainEditorBinding
import com.tokopedia.media.editor.ui.activity.detail.DetailEditorActivity
import com.tokopedia.media.editor.ui.activity.main.EditorViewModel
import com.tokopedia.media.editor.ui.component.DrawerUiComponent
import com.tokopedia.media.editor.ui.component.ToolsUiComponent
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorUiModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class EditorFragment @Inject constructor() : BaseEditorFragment(), ToolsUiComponent.Listener,
    DrawerUiComponent.Listener {

    private val viewBinding: FragmentMainEditorBinding? by viewBinding()
    private val viewModel: EditorViewModel by activityViewModels()

    private val editorToolComponent by uiComponent { ToolsUiComponent(it, this) }
    private val thumbnailDrawerComponent by uiComponent { DrawerUiComponent(it, this) }

    private var activeImageUrl: String = ""

    fun isShowDialogConfirmation(): Boolean{
        return viewModel.getEditState(activeImageUrl)?.editList?.isNotEmpty() ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            editorR.layout.fragment_main_editor,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding?.btnUndo?.setOnClickListener {
            backState()
        }

        viewBinding?.btnRedo?.setOnClickListener {
            forwardState()
        }
    }

    private fun backState() {
        val targetEditorUiModel = viewModel.getEditState(activeImageUrl)
        targetEditorUiModel?.let {
            val imageEditStateCount = it.editList.size
            if (it.backValue >= imageEditStateCount) return@let

            it.backValue++
            viewBinding?.imgMainPreview?.loadImage(it.getImageUrl())

            renderUndoText(it)
            renderRedoText(it)

            renderToolsIconActiveState(it)
            updateDrawerSelectionItemIcon()

            val undoIndex = (it.editList.size - it.backValue)
            renderStateChangeToast(TOAST_UNDO, it.editList[undoIndex].editorToolType)
        }
    }

    private fun forwardState() {
        val targetEditorUiModel = viewModel.getEditState(activeImageUrl)
        targetEditorUiModel?.let {
            if (it.backValue == 0) return@let

            it.backValue--
            viewBinding?.imgMainPreview?.loadImage(it.getImageUrl())

            renderUndoText(it)
            renderRedoText(it)

            renderToolsIconActiveState(it)
            updateDrawerSelectionItemIcon()

            val redoIndex = (it.editList.size - 1) - it.backValue
            renderStateChangeToast(TOAST_REDO, it.editList[redoIndex].editorToolType)
        }
    }

    private fun renderUndoText(editList: EditorUiModel) {
        viewBinding?.btnUndo?.showWithCondition(editList.editList.isNotEmpty()
                && editList.editList.size > editList.backValue)
    }

    private fun renderRedoText(editList: EditorUiModel) {
        viewBinding?.btnRedo?.showWithCondition(editList.backValue != 0)
    }

    private fun renderToolsIconActiveState(editorUiModel: EditorUiModel){
        editorToolComponent.setupActiveTools(editorUiModel.editList, editorUiModel.backValue)
    }

    private fun updateDrawerSelectionItemIcon(){
        viewModel.updatedIndexItem.value?.let { updatedIndex ->
            thumbnailDrawerComponent.refreshItem(updatedIndex, viewModel.editStateList.values.toList())
        }
    }

    private fun renderStateChangeToast(toastKey: Int, @EditorToolType editorToolType: Int) {
        val sourceInt = when(editorToolType){
            EditorToolType.BRIGHTNESS -> editorR.string.editor_tool_brightness
            EditorToolType.CONTRAST -> editorR.string.editor_tool_contrast
            EditorToolType.WATERMARK -> editorR.string.editor_tool_watermark
            EditorToolType.ROTATE -> editorR.string.editor_tool_rotate
            EditorToolType.CROP -> editorR.string.editor_tool_crop
            else -> editorR.string.editor_tool_remove_background
        }

        val toastEditText = getString(sourceInt)
        val toastStateChangeText = if (toastKey == TOAST_UNDO)
            getString(editorR.string.editor_undo_state_format, toastEditText)
        else
            getString(editorR.string.editor_redo_state_format, toastEditText)


        viewBinding?.undoRedoToast?.let {
            it.text = toastStateChangeText
            it.show()

            Handler().removeCallbacksAndMessages(null)
            Handler().postDelayed({
                it.hide()
            }, 1500)
        }
    }

    override fun initObserver() {
        observeEditorParam()
        observeUpdateIndex()
    }

    override fun onEditorToolClicked(type: Int) {
        activity?.let {
            val clickedItem = viewModel.getEditState(activeImageUrl)
            clickedItem?.let { editorUiModel ->
                val paramData = EditorDetailUiModel(
                    originalUrl = editorUiModel.getOriginalUrl(),
                    editorToolType = type
                )

                // limit state according to undo
                val stateLimit = (editorUiModel.editList.size - 1) - editorUiModel.backValue

                editorUiModel.editList.forEachIndexed { index, item ->
                    // stop loop if
                    // 1. index is more than undo/redo state
                    if (index > stateLimit) {
                        return@forEachIndexed
                    }

                    // skip state if (AND), when state limit greater than removeBG index then dont bring previous value
                    // 1. index is lower than state limit
                    // 2. state limit greater than remove index
                    if (editorUiModel.removeBackgroundStartState in index until stateLimit) {
                        return@forEachIndexed
                    }

                    paramData.brightnessValue = item.brightnessValue
                    paramData.contrastValue = item.contrastValue
                    paramData.watermarkMode = item.watermarkMode
                    paramData.removeBackgroundUrl = item.removeBackgroundUrl
                    paramData.cropRotateValue = item.cropRotateValue

                    // need to store brightness / contrast implement sequence (result will be diff)
                    // if contrast is latest filter then isContrastExecuteFirst = false
                    if (item.editorToolType == EditorToolType.CONTRAST) {
                        paramData.isContrastExecuteFirst = 0
                    } else if (item.editorToolType == EditorToolType.BRIGHTNESS) {
                        paramData.isContrastExecuteFirst = 1
                    }
                }

                val intent = Intent(it, DetailEditorActivity::class.java).apply {
                    putExtra(DetailEditorActivity.PARAM_EDITOR_DETAIL, paramData)
                }

                startActivityForResult(intent, DetailEditorActivity.EDITOR_RESULT_CODE)
            }
        }
    }

    override fun onThumbnailDrawerClicked(
        originalUrl: String,
        resultUrl: String?,
        clickedIndex: Int
    ) {
        activeImageUrl = originalUrl

        val editList = viewModel.getEditState(originalUrl)

        viewBinding?.imgMainPreview?.loadImage(editList?.getImageUrl())

        viewBinding?.imgMainPreview?.post {
            editList?.let {
                renderUndoText(it)
                renderRedoText(it)

                renderToolsIconActiveState(it)
            }
        }
    }

    private fun observeEditorParam() {
        viewModel.editorParam.observe(viewLifecycleOwner) {
            editorToolComponent.setupView(it.editorTools)
            thumbnailDrawerComponent.setupRecyclerView(viewModel.editStateList.values.toList())
        }
    }

    private fun observeUpdateIndex() {
        viewModel.updatedIndexItem.observe(viewLifecycleOwner) {
            val editList =  viewModel.editStateList.values.toList()
            thumbnailDrawerComponent.refreshItem(it, editList)

            val editorUiModel = viewModel.getEditState(activeImageUrl)
            if (editorUiModel != null) {
                renderUndoText(editorUiModel)
                renderRedoText(editorUiModel)

                renderToolsIconActiveState(editorUiModel)
            }
        }
    }

    override fun getScreenName() = SCREEN_NAME

    companion object {
        private const val SCREEN_NAME = "Main Editor"
        private const val TOAST_UNDO = 0
        private const val TOAST_REDO = 1
    }

}