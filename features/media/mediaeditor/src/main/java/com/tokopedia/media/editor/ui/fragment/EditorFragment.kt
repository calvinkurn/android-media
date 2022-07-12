package com.tokopedia.media.editor.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
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

class EditorFragment @Inject constructor() : BaseEditorFragment()
    , ToolsUiComponent.Listener
    , DrawerUiComponent.Listener {

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

    private fun backState(){
        val targetEditorUiModel = viewModel.getEditState(activeImageUrl)
        targetEditorUiModel?.let {
            val imageEditStateCount = it.editList.size
            if(it.backValue >= imageEditStateCount) return@let

            it.backValue++
            viewBinding?.imgMainPreview?.loadImage(it.getImageUrl()){
                this.centerCrop()
            }

            viewBinding?.btnUndo?.text = renderUndoText(it)
            viewBinding?.btnRedo?.text = renderRedoText(it)
        }
    }

    private fun forwardState() {
        val targetEditorUiModel = viewModel.getEditState(activeImageUrl)
        targetEditorUiModel?.let {
            if(it.backValue == 0) return@let

            it.backValue--
            viewBinding?.imgMainPreview?.loadImage(it.getImageUrl()){
                this.centerCrop()
            }

            viewBinding?.btnUndo?.text = renderUndoText(it)
            viewBinding?.btnRedo?.text = renderRedoText(it)
        }
    }

    private fun renderUndoText(editList: EditorUiModel): String{
        return if(editList.editList.isNotEmpty()) "Undo (${editList.editList.size - editList.backValue})" else "-"
    }

    private fun renderRedoText(editList: EditorUiModel): String{
        return if(editList.backValue != 0) "Redo (${editList.backValue})" else "-"
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

                editorUiModel.editList.forEach { item ->
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

    override fun onThumbnailDrawerClicked(originalUrl: String, resultUrl: String?, clickedIndex: Int) {
        activeImageUrl = originalUrl

        val editList = viewModel.getEditState(originalUrl)
        viewBinding?.btnUndo?.text = renderUndoText(editList!!)

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

    private fun observeUpdateIndex(){
        viewModel.updatedIndexItem.observe(viewLifecycleOwner){
            thumbnailDrawerComponent.refreshItem(it, viewModel.editStateList.values.toList())
        }
    }

    override fun getScreenName() = SCREEN_NAME

    companion object {
        private const val SCREEN_NAME = "Main Editor"
    }

}