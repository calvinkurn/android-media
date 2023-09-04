package com.tokopedia.editor.ui.placement

import android.net.Uri
import android.os.Handler
import androidx.core.graphics.values
import androidx.fragment.app.activityViewModels
import com.tokopedia.editor.R
import com.tokopedia.editor.base.BaseEditorFragment
import com.tokopedia.editor.databinding.FragmentPlacementBinding
import com.tokopedia.editor.ui.components.custom.crop.StoryEditorUCropLayout
import com.tokopedia.editor.ui.model.ImagePlacementModel
import com.tokopedia.editor.util.getEditorCacheFolderPath
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.view.binding.viewBinding
import java.io.File
import javax.inject.Inject

class PlacementImageFragment @Inject constructor() : BaseEditorFragment(R.layout.fragment_placement) {

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
                    showLoadingDialog()
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
    }

    override fun initView() {
        initListener()
    }

    fun captureImage(onFinish: (placementModel: ImagePlacementModel) -> Unit) {
        viewBinding?.cropArea?.getCropImageView()?.let {
            it.customCrop { placementModel ->
                onFinish(placementModel)
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

                    hideLoadingDialog()
                }, PREV_STATE_DELAY)
            } ?: run{
                viewModel.initialImageMatrix = it.cropArea.getCropImageView()?.imageMatrix?.values()

                hideLoadingDialog()
            }
        }
    }

    private fun getOutputPath(): String {
        return getEditorCacheFolderPath() + FileUtil.generateUniqueFileName()
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
        loaderDialog?.show() ?: run {
            context?.let {
                loaderDialog = LoaderDialog(it)
                loaderDialog?.setLoadingText("")
                loaderDialog?.show()
            }
        }
    }

    private fun hideLoadingDialog() {
        loaderDialog?.dismiss()
    }

    companion object {
        private const val INDEX_TRANSLATE_X = 2
        private const val INDEX_TRANSLATE_Y = 5

        private const val IMAGE_RATIO = 9/16f

        private const val PREV_STATE_DELAY = 500L
    }
}
