package com.tokopedia.editor.ui.placement

import android.graphics.Matrix
import android.net.Uri
import android.os.Handler
import androidx.core.graphics.values
import androidx.fragment.app.activityViewModels
import com.tokopedia.editor.R
import com.tokopedia.editor.base.BaseEditorFragment
import com.tokopedia.editor.databinding.FragmentPlacementBinding
import com.tokopedia.editor.ui.model.ImagePlacementModel
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.view.binding.viewBinding
import com.yalantis.ucrop.view.TransformImageView
import java.io.File
import java.lang.Exception
import javax.inject.Inject

class PlacementImageFragment @Inject constructor() : BaseEditorFragment(R.layout.fragment_placement) {

    private val viewModel: PlacementImageViewModel by activityViewModels()
    private val viewBinding: FragmentPlacementBinding? by viewBinding()

    private var scale = 0f
    private var translateX = 0f
    private var translateY = 0f

    override fun initObserver() {
        viewModel.imagePath.observe(viewLifecycleOwner) {
            viewBinding?.cropArea?.let { ucropRef ->
                ucropRef.getCropImageView()?.let { gestureCropImage ->
                    gestureCropImage.setImageUri(
                        imageUri = Uri.fromFile(File(it)),
                        outputUri = Uri.parse(getOutputPath())
                    )

                    gestureCropImage.setTransformImageListener(object :
                        TransformImageView.TransformImageListener {
                        override fun onLoadComplete() {
                            gestureCropImage.post {
                                ucropRef.getOverlayView()?.setTargetAspectRatio(9/16f)

                                Handler().postDelayed({
                                    scale = gestureCropImage.currentScale
                                    gestureCropImage.imageMatrix.values().let {
                                        translateX = it[2]
                                        translateY = it[5]
                                    }

                                    implementPreviousState(viewModel.placementModel.value)
                                }, PREV_STATE_DELAY)
                            }
                        }

                        override fun onLoadFailure(e: Exception) {}

                        override fun onRotate(currentAngle: Float) {}

                        override fun onScale(currentScale: Float) {}
                    })
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
                val currentTranslateX = currentImageMatrix[INDEX_CORD_X]
                val currentTranslateY = currentImageMatrix[INDEX_CORD_Y]

                Handler().postDelayed({
                    // waiting zoom & rotate process is done then move the image
                    // need reset image matrix value before implement previous state
                    cropImageView.postTranslate(-currentTranslateX, -currentTranslateY)
                    cropImageView.postTranslate(previousModel.translateX, previousModel.translateY)

                    viewModel.initialImageMatrix = it.cropArea.getCropImageView()?.imageMatrix?.values()
                },PREV_STATE_DELAY)
            } ?: run{
                viewModel.initialImageMatrix = it.cropArea.getCropImageView()?.imageMatrix?.values()
            }
        }
    }

    private fun getOutputPath(): String {
        return (activity as PlacementImageActivity).getEditorCacheFolderPath() + FileUtil.generateUniqueFileName()
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
            val currentTranslateX = currentImageMatrix[INDEX_CORD_X]
            val currentTranslateY = currentImageMatrix[INDEX_CORD_Y]

            gestureCropImage.postTranslate(-currentTranslateX, -currentTranslateY)
            gestureCropImage.postTranslate(translateX, translateY)
        }
    }

    companion object {
        private const val INDEX_CORD_X = 2
        private const val INDEX_CORD_Y = 5

        private const val PREV_STATE_DELAY = 500L
    }
}
