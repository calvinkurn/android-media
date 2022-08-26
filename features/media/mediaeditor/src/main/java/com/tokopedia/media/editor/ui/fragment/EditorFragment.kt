package com.tokopedia.media.editor.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.media.editor.R as editorR
import com.tokopedia.media.editor.base.BaseEditorFragment
import com.tokopedia.media.editor.databinding.FragmentMainEditorBinding
import com.tokopedia.media.editor.ui.activity.detail.DetailEditorActivity
import com.tokopedia.media.editor.ui.activity.main.EditorViewModel
import com.tokopedia.media.editor.ui.component.DrawerUiComponent
import com.tokopedia.media.editor.ui.component.ToolsUiComponent
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorUiModel
import com.tokopedia.media.editor.utils.getToolEditorText
import com.tokopedia.media.loader.loadImageWithEmptyTarget
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.picker.common.ImageRatioType
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class EditorFragment @Inject constructor() : BaseEditorFragment(), ToolsUiComponent.Listener,
    DrawerUiComponent.Listener {

    private val viewBinding: FragmentMainEditorBinding? by viewBinding()
    private val viewModel: EditorViewModel by activityViewModels()

    private val editorToolComponent by uiComponent { ToolsUiComponent(it, this) }
    private val thumbnailDrawerComponent by uiComponent { DrawerUiComponent(it, this) }

    private var activeImageUrl: String = ""

    private val isAutoCrop: ImageRatioType? get() = viewModel.editorParam.value?.autoCropRatio

    private var loader: LoaderDialog? = null

    fun isShowDialogConfirmation(): Boolean {
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

    private fun startAutoCrop() {
        loader = LoaderDialog(requireContext()).apply {
            setLoadingText("")
            customView = View.inflate(
                requireContext(),
                editorR.layout.fragment_main_crop_loader_layout,
                null
            ) as LinearLayout
            show()
        }

        cropAll(viewModel.editStateList.values.toList(), 0)
    }

    private fun cropAll(listData: List<EditorUiModel>, currentProcess: Int) {
        if (currentProcess >= listData.size) {
            showAutoCropToaster()

            viewBinding?.viewPager?.let {
                val stateList = viewModel.editStateList.values.toList()
                it.setAdapter(stateList)
                it.setOnPageChanged { position, isVideo ->
                    thumbnailDrawerComponent.clickIndex(position)

                    editorToolComponent.container().apply {
                        if (isVideo) hide() else visible()
                    }
                }
            }

            return
        }

        val data = listData[currentProcess]
        if (isAutoCrop != null && data.editList.size == 0 && !data.isVideo) {
            loadImageWithEmptyTarget(requireContext(),
                data.getImageUrl(),
                properties = {},
                mediaTarget = MediaBitmapEmptyTarget(
                    onReady = { bitmap ->
                        cropImage(bitmap, data)
                        thumbnailDrawerComponent.refreshItem(
                            currentProcess,
                            viewModel.editStateList.values.toList()
                        )
                        cropAll(listData, currentProcess + 1)
                    },
                    onCleared = {}
                ))
        } else {
            cropAll(listData, currentProcess + 1)
        }
    }

    private fun cropImage(sourceBitmap: Bitmap?, editorDetailUiModel: EditorUiModel) {
        sourceBitmap?.let { it ->
            val bitmapWidth = sourceBitmap.width
            val bitmapHeight = sourceBitmap.height

            val ratioWidth = isAutoCrop?.getRatioX()?.toFloat() ?: 1f
            val ratioHeight = isAutoCrop?.getRatioY()?.toFloat() ?: 1f
            val autoCropRatio = ratioHeight / ratioWidth

            var newWidth = bitmapWidth
            var newHeight = (bitmapWidth * autoCropRatio).toInt()

            var topMargin = 0
            var leftMargin = 0

            var scaledTarget = 1f

            if (newHeight <= bitmapHeight && newWidth <= bitmapWidth) {
                leftMargin = (bitmapWidth - newWidth) / 2
                topMargin = (bitmapHeight - newHeight) / 2
            } else if (newHeight > bitmapHeight) {
                scaledTarget = bitmapHeight.toFloat() / newHeight

                // new value after rescale small
                newWidth = (newWidth * scaledTarget).toInt()
                newHeight = (newHeight * scaledTarget).toInt()

                leftMargin = (bitmapWidth - newWidth) / 2
                topMargin = (bitmapHeight - newHeight) / 2
            }

            val bitmapResult = Bitmap.createBitmap(it, leftMargin, topMargin, newWidth, newHeight)
            val savedFile = viewModel.saveImageCache(
                requireContext(),
                bitmapResult
            )

            val newEditorDetailUiModel = EditorDetailUiModel(
                originalUrl = editorDetailUiModel.getOriginalUrl(),
                editorToolType = EditorToolType.CROP,
                resultUrl = savedFile?.path ?: "",
            )
            newEditorDetailUiModel.cropRotateValue.apply {
                offsetX = leftMargin
                offsetY = topMargin
                imageWidth = newWidth
                imageHeight = newHeight
                scaleX = 1f
                scaleY = 1f
                isCrop = true
                isAutoCrop = true
            }

            editorDetailUiModel.editList.add(newEditorDetailUiModel)
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
                        paramData.isContrastExecuteFirst = false
                    } else if (item.editorToolType == EditorToolType.BRIGHTNESS) {
                        paramData.isContrastExecuteFirst = true
                    }
                }

                val intent = Intent(it, DetailEditorActivity::class.java).apply {
                    putExtra(DetailEditorActivity.PARAM_EDITOR_DETAIL, paramData)
                    putExtra(DetailEditorActivity.PARAM_EDITOR, viewModel.editorParam.value)
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

        viewModel.getEditState(originalUrl)?.let { editorUiModel ->
            viewBinding?.viewPager?.currentItem = clickedIndex

            renderUndoText(editorUiModel)
            renderRedoText(editorUiModel)
            renderToolsIconActiveState(editorUiModel)
        }
    }

    override fun getScreenName() = SCREEN_NAME

    private fun backState() {
        val targetEditorUiModel = viewModel.getEditState(activeImageUrl)
        targetEditorUiModel?.let {
            val imageEditStateCount = it.editList.size
            if (it.backValue >= imageEditStateCount) return@let

            it.backValue++
            viewBinding?.viewPager?.updateImage(
                thumbnailDrawerComponent.getCurrentIndex(),
                targetEditorUiModel.getImageUrl()
            )

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
            viewBinding?.viewPager?.updateImage(
                thumbnailDrawerComponent.getCurrentIndex(),
                targetEditorUiModel.getImageUrl()
            )

            renderUndoText(it)
            renderRedoText(it)

            renderToolsIconActiveState(it)
            updateDrawerSelectionItemIcon()

            val redoIndex = (it.editList.size - 1) - it.backValue
            renderStateChangeToast(TOAST_REDO, it.editList[redoIndex].editorToolType)
        }
    }

    private fun renderUndoText(editList: EditorUiModel) {
        viewBinding?.btnUndo?.showWithCondition(
            editList.editList.isNotEmpty()
                    && editList.editList.size > editList.backValue
        )
    }

    private fun renderRedoText(editList: EditorUiModel) {
        viewBinding?.btnRedo?.showWithCondition(editList.backValue != 0)
    }

    private fun renderToolsIconActiveState(editorUiModel: EditorUiModel) {
        editorToolComponent.setupActiveTools(editorUiModel.editList, editorUiModel.backValue)
    }

    private fun updateDrawerSelectionItemIcon() {
        viewModel.updatedIndexItem.value?.let { updatedIndex ->
            thumbnailDrawerComponent.refreshItem(
                updatedIndex,
                viewModel.editStateList.values.toList()
            )
        }
    }

    private fun renderStateChangeToast(toastKey: Int, @EditorToolType editorToolType: Int) {
        val sourceInt = getToolEditorText(editorToolType)

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
            }, UNDO_REDO_NOTIFY_TIME)
        }
    }

    private fun observeEditorParam() {
        viewModel.editorParam.observe(viewLifecycleOwner) {
            editorToolComponent.setupView(it.editorToolsList)
            thumbnailDrawerComponent.setupRecyclerView(viewModel.editStateList.values.toList())
            startAutoCrop()
        }
    }

    private fun observeUpdateIndex() {
        viewModel.updatedIndexItem.observe(viewLifecycleOwner) {
            val editList = viewModel.editStateList.values.toList()
            thumbnailDrawerComponent.refreshItem(it, editList)

            val editorUiModel = viewModel.getEditState(activeImageUrl)
            if (editorUiModel != null) {
                renderUndoText(editorUiModel)
                renderRedoText(editorUiModel)

                renderToolsIconActiveState(editorUiModel)

                viewBinding?.viewPager?.updateImage(it, editorUiModel.getImageUrl())
            }
        }
    }

    private fun showAutoCropToaster() {
        loader?.dismiss()

        viewBinding?.mainEditorFragmentLayout?.let { editorFragmentContainer ->
            val ratioWidth = isAutoCrop?.getRatioX()?.toFloat() ?: 1f
            val ratioHeight = isAutoCrop?.getRatioY()?.toFloat() ?: 1f

            Toaster.build(
                editorFragmentContainer,
                getString(
                    editorR.string.editor_auto_crop_format,
                    ratioWidth.toInt(),
                    ratioHeight.toInt()
                ),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL
            ).show()
        }
    }

    companion object {
        private const val SCREEN_NAME = "Main Editor"
        private const val TOAST_UNDO = 0
        private const val TOAST_REDO = 1
        private const val UNDO_REDO_NOTIFY_TIME = 1500L
    }

}