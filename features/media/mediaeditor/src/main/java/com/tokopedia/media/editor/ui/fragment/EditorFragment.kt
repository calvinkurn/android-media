package com.tokopedia.media.editor.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
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
import com.tokopedia.media.editor.utils.writeBitmapToStorage
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.listener.MediaListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.picker.common.ImageRatioType
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject
import kotlin.math.round

class EditorFragment @Inject constructor() : BaseEditorFragment(), ToolsUiComponent.Listener,
    DrawerUiComponent.Listener {

    private val viewBinding: FragmentMainEditorBinding? by viewBinding()
    private val viewModel: EditorViewModel by activityViewModels()

    private val editorToolComponent by uiComponent { ToolsUiComponent(it, this) }
    private val thumbnailDrawerComponent by uiComponent { DrawerUiComponent(it, this) }

    private var activeImageUrl: String = ""

    private val isAutoCrop: ImageRatioType? get() = viewModel.editorParam.value?.autoCropRatio

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

    // always triggered for initial phase, since active index default value is 0
    override fun onThumbnailDrawerClicked(
        originalUrl: String,
        resultUrl: String?,
        clickedIndex: Int
    ) {
        activeImageUrl = originalUrl

        val editorUiModel = viewModel.getEditState(originalUrl)

        viewBinding?.imgMainPreview?.loadImage(editorUiModel?.getImageUrl()) {
            this.listener(onSuccess = { bitmap, _ ->
                if(isAutoCrop != null && editorUiModel?.editList?.size == 0){
                    cropImage(bitmap, editorUiModel)
                }

                editorUiModel?.let {
                    renderUndoText(it)
                    renderRedoText(it)

                    renderToolsIconActiveState(it)
                }
            })
        }
    }

    override fun getScreenName() = SCREEN_NAME

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

    private fun cropImage(sourceBitmap: Bitmap?, editorDetailUiModel: EditorUiModel){
        sourceBitmap?.let { it ->
            var scaledBitmap: Bitmap? = null

            val bitmapWidth = sourceBitmap.width
            val bitmapHeight = sourceBitmap.height

            val ratioWidth = isAutoCrop?.getRatioX()?.toFloat() ?: 1f
            val ratioHeight = isAutoCrop?.getRatioY()?.toFloat() ?: 1f
            val autoCropRatio = ratioHeight / ratioWidth

            var newWidth = bitmapWidth
            var newHeight = (bitmapWidth * autoCropRatio).toInt()

            var topMargin = 0
            var leftMargin = 0

            if(newHeight <= bitmapHeight && newWidth <= bitmapWidth){
                leftMargin = (bitmapWidth - newWidth) / 2
                topMargin = (bitmapHeight - newHeight) / 2
            } else if(newHeight > bitmapHeight){
                val diffValue = newHeight - bitmapHeight
                val scaledTarget = 1f + (diffValue.toFloat() / bitmapHeight)

                scaledBitmap = Bitmap.createScaledBitmap(sourceBitmap,
                    (bitmapWidth*scaledTarget).toInt(),
                    newHeight,
                    false
                )

                leftMargin = ((scaledBitmap?.width ?: 0) - newWidth) / 2
                topMargin = ((scaledBitmap?.height ?: 0) - newHeight) / 2
            }

            val bitmapResult = Bitmap.createBitmap((scaledBitmap ?: it), leftMargin, topMargin, newWidth, newHeight)
            val savedFile = writeBitmapToStorage(
                requireContext(),
                bitmapResult
            )

            val newEditorDetailUiModel = EditorDetailUiModel(
                originalUrl = activeImageUrl,
                editorToolType = EditorToolType.CROP,
                resultUrl = savedFile?.path ?: "",
            )
            newEditorDetailUiModel.cropRotateValue.apply {
                imageWidth = newWidth
                imageHeight = newHeight
                isCrop = true
                isAutoCrop = true
            }

            editorDetailUiModel.editList.add(newEditorDetailUiModel)

            viewBinding?.imgMainPreview?.post {
                viewBinding?.imgMainPreview?.loadImage(savedFile?.path)

                viewBinding?.mainEditorFragmentLayout?.let { editorFragmentContainer ->
                    Toaster.build(
                        editorFragmentContainer,
                        getString(editorR.string.editor_auto_crop_format, ratioWidth.toInt(), ratioHeight.toInt()),
                        Toaster.LENGTH_LONG,
                        Toaster.TYPE_NORMAL
                    ).show()
                }
            }
        }
    }

    private fun observeEditorParam() {
        viewModel.editorParam.observe(viewLifecycleOwner) {
            editorToolComponent.setupView(it.editorToolsList)
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

    companion object {
        private const val SCREEN_NAME = "Main Editor"
        private const val TOAST_UNDO = 0
        private const val TOAST_REDO = 1
    }

}