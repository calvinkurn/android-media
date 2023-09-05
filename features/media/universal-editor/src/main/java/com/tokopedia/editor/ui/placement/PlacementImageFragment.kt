package com.tokopedia.editor.ui.placement

import android.net.Uri
import android.os.Handler
import androidx.core.graphics.values
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.editor.R
import com.tokopedia.editor.base.BaseEditorFragment
import com.tokopedia.editor.databinding.FragmentPlacementBinding
import com.tokopedia.editor.ui.widget.crop.StoryEditorUCropLayout
import com.tokopedia.editor.ui.model.ImagePlacementModel
import com.tokopedia.editor.util.getEditorCacheFolderPath
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class PlacementImageFragment @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseEditorFragment(R.layout.fragment_placement) {

    private val viewModel: PlacementImageViewModel by activityViewModels()
    private val viewBinding: FragmentPlacementBinding? by viewBinding()

    private var scale = 0f
    private var translateX = 0f
    private var translateY = 0f

    private var loaderDialog: LoaderDialog? = null

    override fun initObserver() {
        viewModel.imagePath.observe(viewLifecycleOwner) {
            viewBinding?.cropArea?.let { ucropRef ->
                ucropRef.getCropImageView()?.let { gestureCropImage ->

                    viewModel.updateLoadingState(true)

                    gestureCropImage.setImageUri(
                        imageUri = Uri.fromFile(File(it)),
                        outputUri = Uri.parse(getOutputPath())
                    )

                    ucropRef.listener = object: StoryEditorUCropLayout.Listener {
                        override fun onFinish() {
                            gestureCropImage.post {
                                ucropRef.getOverlayView()?.setTargetAspectRatio(IMAGE_RATIO)

                                Handler().postDelayed({
                                    scale = gestureCropImage.currentScale
                                    gestureCropImage.imageMatrix.values().let {
                                        translateX = it[INDEX_TRANSLATE_X]
                                        translateY = it[INDEX_TRANSLATE_Y]
                                    }

                                    implementPreviousState(viewModel.placementModel.value)
                                }, PREV_STATE_DELAY)
                            }
                        }
                    }
                }
            }
        }

        viewModel.isLoadingShow.observe(viewLifecycleOwner) {
            if (it) {
                showLoadingDialog()
            } else {
                hideLoadingDialog()
            }
        }
    }

    override fun initView() {
        initListener()
    }

    fun captureImage() {
        viewBinding?.cropArea?.getCropImageView()?.let {
            lifecycleScope.launch(dispatchers.io) {
                it.customCrop { (bitmap, matrix, outputUri) ->
                    val translateX = matrix[INDEX_TRANSLATE_X]
                    val translateY = matrix[INDEX_TRANSLATE_Y]

                    viewModel.savePlacementBitmap(
                        outputUri,
                        bitmap,
                        translateX = translateX,
                        translateY = translateY,
                        scale = it.currentScale,
                        angle = it.currentAngle
                    )
                }
            }
        }
    }

    fun getCurrentMatrixValue(): FloatArray? {
        return viewBinding?.cropArea?.getCropImageView()?.imageMatrix?.values()
    }

    private fun implementPreviousState(placementModel: ImagePlacementModel?) {
        viewBinding?.let {
            placementModel?.let { previousModel ->
                val cropImageView = it.cropArea.getCropImageView() ?: return

                cropImageView.zoomInImage(previousModel.scale)
                cropImageView.postRotate(previousModel.angle)

                val currentImageMatrix = cropImageView.imageMatrix.values()
                val currentTranslateX = currentImageMatrix[INDEX_TRANSLATE_X]
                val currentTranslateY = currentImageMatrix[INDEX_TRANSLATE_Y]

                Handler().postDelayed({
                    // waiting zoom & rotate process is done then move the image
                    // need reset image matrix value before implement previous state
                    cropImageView.postTranslate(-currentTranslateX, -currentTranslateY)
                    cropImageView.postTranslate(previousModel.translateX, previousModel.translateY)

                    viewModel.initialImageMatrix = it.cropArea.getCropImageView()?.imageMatrix?.values()

                    viewModel.updateLoadingState(false)
                }, PREV_STATE_DELAY)
            } ?: run{
                viewModel.initialImageMatrix = it.cropArea.getCropImageView()?.imageMatrix?.values()

                viewModel.updateLoadingState(false)
            }
        }
    }

    private fun getOutputPath(): String {
        return getEditorCacheFolderPath() + FileUtil.generateUniqueFileName() + IMAGE_EXTENSION
    }

    private fun initListener() {
        viewBinding?.let {
            it.resetCta.setOnClickListener {
                resetUCrop()
            }
        }
    }

    private fun resetUCrop() {
        viewBinding?.cropArea?.getCropImageView()?.let { gestureCropImage ->
            gestureCropImage.postRotate(-gestureCropImage.currentAngle)
            gestureCropImage.zoomOutImage(scale)

            val currentImageMatrix = gestureCropImage.imageMatrix.values()
            val currentTranslateX = currentImageMatrix[INDEX_TRANSLATE_X]
            val currentTranslateY = currentImageMatrix[INDEX_TRANSLATE_Y]

            gestureCropImage.postTranslate(-currentTranslateX, -currentTranslateY)
            gestureCropImage.postTranslate(translateX, translateY)
        }
    }

    private fun showLoadingDialog() {
        context?.let {
            loaderDialog = LoaderDialog(it)
            loaderDialog?.setLoadingText("")
            loaderDialog?.show()
        }
    }

    private fun hideLoadingDialog() {
        loaderDialog?.dismiss()
        loaderDialog = null
    }

    companion object {
        private const val INDEX_TRANSLATE_X = 2
        private const val INDEX_TRANSLATE_Y = 5

        private const val IMAGE_RATIO = 9/16f

        private const val PREV_STATE_DELAY = 500L

        private const val IMAGE_EXTENSION = ".png"
    }
}
