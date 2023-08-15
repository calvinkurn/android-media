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
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.media.editor.analytics.editorhome.EditorHomeAnalytics
import com.tokopedia.media.editor.analytics.getToolEditorText
import com.tokopedia.media.editor.base.BaseEditorFragment
import com.tokopedia.media.editor.data.FeatureToggleManager
import com.tokopedia.media.editor.databinding.FragmentMainEditorBinding
import com.tokopedia.media.editor.ui.activity.detail.DetailEditorActivity
import com.tokopedia.media.editor.ui.activity.main.EditorViewModel
import com.tokopedia.media.editor.ui.component.DrawerUiComponent
import com.tokopedia.media.editor.ui.component.ToolsUiComponent
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorUiModel
import com.tokopedia.media.editor.ui.widget.EditorViewPager
import com.tokopedia.media.editor.utils.cropCenterImage
import com.tokopedia.media.editor.utils.getImageSize
import com.tokopedia.media.editor.utils.showErrorLoadToaster
import com.tokopedia.media.loader.loadImageWithEmptyTarget
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.picker.common.ImageRatioType
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.picker.common.utils.isVideoFormat
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject
import com.tokopedia.media.editor.R as editorR

class EditorFragment @Inject constructor(
    private val editorHomeAnalytics: EditorHomeAnalytics,
    private val featureToggleManager: FeatureToggleManager
) : BaseEditorFragment(), ToolsUiComponent.Listener,
    DrawerUiComponent.Listener {

    private val viewBinding: FragmentMainEditorBinding? by viewBinding()
    private val viewModel: EditorViewModel by activityViewModels()

    private val editorToolComponent by uiComponent { ToolsUiComponent(it, this) }
    private val thumbnailDrawerComponent by uiComponent { DrawerUiComponent(it, this) }

    private var activeImageUrl: String = ""
    private var loader: LoaderDialog? = null
    private var autoCropStartTime: Long = 0

    private var toaster: Snackbar? = null

    fun isShowDialogConfirmation(): Boolean {
        return viewModel.getEditState(activeImageUrl)?.editList?.isNotEmpty() ?: false
    }

    override fun onPause() {
        viewBinding?.viewPager?.releaseImage()
        super.onPause()
    }

    override fun onResume() {
        viewBinding?.viewPager?.reloadImage()
        super.onResume()
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

        autoCropStartTime = System.nanoTime()
        iterateCrop(viewModel.editStateList.values.toList(), 0)
    }

    private fun iterateCrop(listData: List<EditorUiModel>, currentProcess: Int) {
        if (currentProcess >= listData.size) {
            showAutoCropToaster()

            viewBinding?.viewPager?.let {
                val stateList = viewModel.editStateList.values.toList()
                it.setAdapter(stateList)
                setPagerPageChangeListener(it)
            }

            val autoCropTotalTime = (System.nanoTime() - autoCropStartTime) / NANO_DIVIDER
            editorHomeAnalytics.autoCropProcessTime(autoCropTotalTime, listData.filter {
                !it.isVideo
            }.size)

            return
        }

        val data = listData[currentProcess]
        data.isAutoCropped = true
        if (data.editList.size == 0 && !data.isVideo) {
            val filePath = data.getOriginalUrl()

            getImageSize(filePath).let {
                val (width, height) = it
                if (viewModel.isMemoryOverflow(width, height)) {
                    loader?.dismiss()
                    activity?.finish()
                    return
                }
            }

            loadImageWithEmptyTarget(requireContext(),
                filePath,
                properties = {
                    useCache(IS_USING_CACHE)
                    setCacheStrategy(CACHE_STRATEGY)
                    listener(
                        onError = {
                            it?.let { exception ->
                                loader?.dismiss()
                                viewBinding?.mainEditorFragmentLayout?.let { view ->
                                    showErrorLoadToaster(view, exception.message ?: "")
                                }

                            }
                        }
                    )
                },
                mediaTarget = MediaBitmapEmptyTarget(
                    onReady = { bitmap ->
                        imageCrop(bitmap, data.getOriginalUrl())
                        thumbnailDrawerComponent.refreshItem(
                            currentProcess,
                            viewModel.editStateList.values.toList()
                        )
                        iterateCrop(listData, currentProcess + 1)
                    },
                    onCleared = {}
                ))
        } else {
            iterateCrop(listData, currentProcess + 1)
        }
    }

    override fun initObserver() {
        observeEditorParam()
        observeUpdateIndex()
    }

    private fun imageCrop(bitmap: Bitmap, originalPath: String) {
        val cropRatio = viewModel.editorParam.value?.autoCropRatio() ?: ImageRatioType.RATIO_1_1
        val imageRatio = bitmap.width.toFloat() / bitmap.height

        cropCenterImage(bitmap, cropRatio)?.let { cropRotateData ->
            viewModel.cropImage(bitmap, cropRotateData)?.let { croppedBitmap ->
                viewModel.saveToCache(
                    croppedBitmap,
                    sourcePath = originalPath
                )?.let {
                    croppedBitmap.recycle()

                    val newEditorDetailUiModel = EditorDetailUiModel(
                        originalUrl = originalPath,
                        editorToolType = EditorToolType.CROP,
                        resultUrl = it.absolutePath,
                        originalRatio = imageRatio
                    )

                    newEditorDetailUiModel.cropRotateValue = cropRotateData
                    viewModel.addEditState(originalPath, newEditorDetailUiModel, false)
                }
            }
        }
    }

    private fun editorClickTracker(editorType: Int) {
        when (editorType) {
            EditorToolType.BRIGHTNESS -> editorHomeAnalytics.clickBrightness()
            EditorToolType.CONTRAST -> editorHomeAnalytics.clickContrast()
            EditorToolType.ROTATE -> editorHomeAnalytics.clickRotate()
            EditorToolType.CROP -> editorHomeAnalytics.clickCrop()
            EditorToolType.REMOVE_BACKGROUND -> editorHomeAnalytics.clickRemoveBackground()
            EditorToolType.WATERMARK -> editorHomeAnalytics.clickWatermark()
            EditorToolType.ADD_LOGO -> editorHomeAnalytics.clickToolAddLogo()
            EditorToolType.ADD_TEXT -> editorHomeAnalytics.clickToolAddText()
        }
    }

    override fun onEditorToolClicked(type: Int) {
        activity?.let {
            editorClickTracker(type)
            val clickedItem = viewModel.getEditState(activeImageUrl)
            clickedItem?.let { editorUiModel ->
                val paramData = EditorDetailUiModel(
                    originalUrl = editorUiModel.getOriginalUrl(),
                    editorToolType = type
                )

                editorUiModel.getFilteredStateList().forEach { item ->
                    paramData.brightnessValue = item.brightnessValue
                    paramData.contrastValue = item.contrastValue
                    paramData.watermarkMode = item.watermarkMode
                    paramData.removeBackgroundUrl = item.removeBackgroundUrl
                    paramData.cropRotateValue = item.cropRotateValue
                    paramData.addLogoValue = item.addLogoValue
                    paramData.addTextValue = item.addTextValue

                    // need to store brightness / contrast implement sequence (result will be diff)
                    // if contrast is latest filter then isContrastExecuteFirst = false
                    if (item.editorToolType == EditorToolType.CONTRAST) {
                        paramData.isContrastExecuteFirst = false
                    } else if (item.editorToolType == EditorToolType.BRIGHTNESS) {
                        paramData.isContrastExecuteFirst = true
                    }
                }

                paramData.resultUrl = editorUiModel.getImageUrl()
                paramData.originalRatio = editorUiModel.originalRatio

                val intent = Intent(it, DetailEditorActivity::class.java).apply {
                    putExtra(DetailEditorActivity.PARAM_EDITOR_DETAIL, paramData)
                    putExtra(DetailEditorActivity.PARAM_EDITOR_MODEL, editorUiModel)
                    putExtra(DetailEditorActivity.PARAM_EDITOR, viewModel.editorParam.value)
                }

                toaster?.dismiss()
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
            updateToolsContainerVisibility(editorUiModel.getOriginalUrl())
            viewBinding?.viewPager?.currentItem = clickedIndex

            viewBinding?.viewPager?.post {
                renderUndoButton(editorUiModel)
                renderRedoButton(editorUiModel)
                renderToolsIconActiveState(editorUiModel)
            }
        }
    }

    override fun getScreenName() = SCREEN_NAME

    private fun backState() {
        viewModel.undoState(activeImageUrl)?.apply {
            viewBinding?.viewPager?.updateImage(
                thumbnailDrawerComponent.getCurrentIndex(),
                this.getImageUrl(),
                overlayImageUrl = this.getOverlayLogoValue()?.overlayLogoUrl ?: "",
                overlaySecondaryImageUrl = this.getOverlayTextValue()?.textImagePath ?: ""
            )

            renderUndoButton(this)
            renderRedoButton(this)

            renderToolsIconActiveState(this)
            updateDrawerSelectionItemIcon()

            renderStateChangeToast(
                TOAST_UNDO,
                this.editList[this.getUndoStartIndex()].editorToolType
            )
        }
    }

    private fun forwardState() {
        viewModel.redoState(activeImageUrl)?.apply {
            viewBinding?.viewPager?.updateImage(
                thumbnailDrawerComponent.getCurrentIndex(),
                this.getImageUrl(),
                overlayImageUrl = this.getOverlayLogoValue()?.overlayLogoUrl ?: "",
                overlaySecondaryImageUrl = this.getOverlayTextValue()?.textImagePath ?: ""
            )

            renderUndoButton(this)
            renderRedoButton(this)

            renderToolsIconActiveState(this)
            updateDrawerSelectionItemIcon()

            renderStateChangeToast(
                TOAST_REDO,
                this.editList[this.getRedoStartIndex()].editorToolType
            )
        }
    }

    private fun renderUndoButton(editList: EditorUiModel) {
        viewBinding?.btnUndo?.showWithCondition(editList.isShowUndoButton())
    }

    private fun renderRedoButton(editList: EditorUiModel) {
        viewBinding?.btnRedo?.showWithCondition(editList.isShowRedoButton())
    }

    private fun renderToolsIconActiveState(editorUiModel: EditorUiModel) {
        editorToolComponent.setupActiveTools(editorUiModel)
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
            // show/hide add logo base on rollence
            if (!viewModel.isShopAvailable()) {
                it.editorToolsList().apply {
                    val removeIndex = find { toolId -> toolId == EditorToolType.ADD_LOGO }
                    remove(removeIndex)
                }
            }

            // show/hide add text base on rollence
            if (!featureToggleManager.isAddTextEnable()) {
                it.editorToolsList().apply {
                    val removeIndex = find { toolId -> toolId == EditorToolType.ADD_TEXT }
                    remove(removeIndex)
                }
            }

            editorToolComponent.setupView(it.editorToolsList())
            thumbnailDrawerComponent.setupView(viewModel.editStateList.values.toList())

            if (it.autoCropRatio() != null) {
                startAutoCrop()
            } else {
                viewBinding?.viewPager?.apply {
                    setAdapter(viewModel.editStateList.values.toList())
                    setPagerPageChangeListener(this)
                }
            }
        }
    }

    private fun setPagerPageChangeListener(viewPager: EditorViewPager) {
        viewPager.setOnPageChanged { position, _ ->
            thumbnailDrawerComponent.clickIndex(position)
        }
    }

    private fun observeUpdateIndex() {
        viewModel.updatedIndexItem.observe(viewLifecycleOwner) {
            val editList = viewModel.editStateList.values.toList()
            thumbnailDrawerComponent.refreshItem(it, editList)

            val editorUiModel = viewModel.getEditState(activeImageUrl)
            if (editorUiModel != null) {
                renderUndoButton(editorUiModel)
                renderRedoButton(editorUiModel)

                renderToolsIconActiveState(editorUiModel)

                viewBinding?.viewPager?.updateImage(
                    it,
                    editorUiModel.getImageUrl(),
                    overlayImageUrl = editorUiModel.getOverlayLogoValue()?.overlayLogoUrl ?: "",
                    overlaySecondaryImageUrl = editorUiModel.getOverlayTextValue()?.textImagePath ?: ""
                )
            }
        }
    }

    private fun showAutoCropToaster() {
        loader?.dismiss()

        viewBinding?.mainEditorFragmentLayout?.let { editorFragmentContainer ->
            val autoCropRatio = viewModel.editorParam.value?.autoCropRatio()
            val ratioWidth = autoCropRatio?.getRatioX()?.toFloat() ?: 1f
            val ratioHeight = autoCropRatio?.getRatioY()?.toFloat() ?: 1f

            toaster = Toaster.build(
                editorFragmentContainer,
                getString(
                    editorR.string.editor_auto_crop_format,
                    ratioWidth.toInt(),
                    ratioHeight.toInt()
                ),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL
            )
            toaster?.show()
        }
    }

    private fun updateToolsContainerVisibility(filePath: String) {
        val isVideo = isVideoFormat(filePath)
        editorToolComponent.container().apply {
            if (isVideo) {
                hide()
            } else {
                visible()
            }
        }
    }

    companion object {
        private const val SCREEN_NAME = "Main Editor"
        private const val TOAST_UNDO = 0
        private const val TOAST_REDO = 1
        private const val UNDO_REDO_NOTIFY_TIME = 1500L
        private const val NANO_DIVIDER = 1000000

        const val IS_USING_CACHE = false
        val CACHE_STRATEGY = MediaCacheStrategy.NONE
    }

}
