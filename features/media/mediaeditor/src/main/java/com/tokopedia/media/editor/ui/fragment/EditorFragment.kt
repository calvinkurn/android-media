package com.tokopedia.media.editor.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.editor.R
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
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class EditorFragment @Inject constructor() : BaseEditorFragment(), ToolsUiComponent.Listener,
    DrawerUiComponent.Listener {

    private val viewBinding: FragmentMainEditorBinding? by viewBinding()
    private val viewModel: EditorViewModel by activityViewModels()

    private val editorToolComponent by uiComponent { ToolsUiComponent(it, this) }
    private val thumbnailDrawerComponent by uiComponent { DrawerUiComponent(it, this) }

    private var activeImageUrl: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_main_editor,
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
            viewBinding?.imgMainPreview?.loadImage(it.getImageUrl()) {
                this.centerCrop()
            }

            renderUndoText(it)
            renderRedoText(it)

            editorToolComponent.setupActiveTools(it.editList, it.backValue)
            viewModel.updatedIndexItem.value?.let { updatedIndex ->
                thumbnailDrawerComponent.refreshItem(updatedIndex, viewModel.editStateList.values.toList())
            }
        }
    }

    private fun forwardState() {
        val targetEditorUiModel = viewModel.getEditState(activeImageUrl)
        targetEditorUiModel?.let {
            if (it.backValue == 0) return@let

            it.backValue--
            viewBinding?.imgMainPreview?.loadImage(it.getImageUrl()) {
                this.centerCrop()
            }

            renderUndoText(it)
            renderRedoText(it)

            editorToolComponent.setupActiveTools(it.editList, it.backValue)
            viewModel.updatedIndexItem.value?.let { updatedIndex ->
                thumbnailDrawerComponent.refreshItem(updatedIndex, viewModel.editStateList.values.toList())
            }
        }
    }

    private fun renderUndoText(editList: EditorUiModel) {
        viewBinding?.btnUndo?.showWithCondition(editList.editList.isNotEmpty()
                && editList.editList.size > editList.backValue)
    }

    private fun renderRedoText(editList: EditorUiModel) {
        viewBinding?.btnRedo?.showWithCondition(editList.backValue != 0)
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

                // limit for remove background
                val isBackgroundRemoveIncluded = stateLimit > editorUiModel.removeBackgroundStartState

                editorUiModel.editList.forEachIndexed { index, item ->
                    // if index is more than undo/redo state and not remove background skip
                    if (index > stateLimit && !isBackgroundRemoveIncluded) {
                        return@forEachIndexed
                    }

                    // if item is removed background and index is less than remove background start state
                    if ( index < editorUiModel.removeBackgroundStartState && isBackgroundRemoveIncluded ) {
                        return@forEachIndexed
                    }

                    paramData.brightnessValue = item.brightnessValue
                    paramData.contrastValue = item.contrastValue
                    paramData.watermarkMode = item.watermarkMode
                    paramData.rotateValue = item.rotateValue
                    paramData.removeBackgroundUrl = item.removeBackgroundUrl
                    paramData.cropBound = item.cropBound
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
        renderUndoText(editList!!)

        viewBinding?.imgMainPreview?.loadImage(editList.getImageUrl()) {
            centerCrop()
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

                editorToolComponent.setupActiveTools(editorUiModel.editList)
            }
        }
    }

    override fun getScreenName() = SCREEN_NAME

    companion object {
        private const val SCREEN_NAME = "Main Editor"
    }

}