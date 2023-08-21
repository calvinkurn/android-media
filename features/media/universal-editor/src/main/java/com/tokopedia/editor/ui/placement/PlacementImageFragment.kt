package com.tokopedia.editor.ui.placement

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
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

    override fun initObserver() {
        viewModel.imagePath.observe(viewLifecycleOwner) {
            viewBinding?.cropArea?.let { ucropRef ->
                ucropRef.getCropImageView()?.let { gestureCropImage ->

                    val outputPath = FileUtil.getTokopediaInternalDirectory(CACHE_FOLDER).path + RESULT_FILE_NAME
                    gestureCropImage.setImageUri(
                        Uri.fromFile(File(it)),
                        Uri.parse(outputPath)
                    )

                    gestureCropImage.setTransformImageListener(object :
                        TransformImageView.TransformImageListener {
                        override fun onLoadComplete() {
                            gestureCropImage.post {
                                ucropRef.getOverlayView()?.setTargetAspectRatio(9/16f)

                                Handler().postDelayed({
                                    viewModel.placementModel.value?.let { model ->
                                        implementPreviousState(model)
                                    }
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

    override fun initView() {}

    fun captureImage(onFinish: (placementModel: ImagePlacementModel) -> Unit) {
        viewBinding?.cropArea?.getCropImageView()?.let {
            it.customCrop { placementModel ->
                onFinish(placementModel)
            }
        }
    }

    private fun implementPreviousState(placementModel: ImagePlacementModel) {
        viewBinding?.let {
            val cropImageView = it.cropArea.getCropImageView() ?: return

            cropImageView.zoomInImage(placementModel.scale)
            cropImageView.postRotate(placementModel.angle)

            val currentImageMatrix = cropImageView.imageMatrix.values()
            val currentTranslateX = currentImageMatrix[INDEX_CORD_X]
            val currentTranslateY = currentImageMatrix[INDEX_CORD_Y]

            Handler().postDelayed({
                // waiting zoom & rotate process is done then move the image
                // need reset image matrix value before implement previous state
                cropImageView.postTranslate(-currentTranslateX, -currentTranslateY)
                cropImageView.postTranslate(placementModel.translateX, placementModel.translateY)
            },PREV_STATE_DELAY)
        }
    }

    companion object {
        private const val RESULT_FILE_NAME = "/stories_editor.png"
        private const val CACHE_FOLDER = "Tokopedia"

        private const val INDEX_CORD_X = 2
        private const val INDEX_CORD_Y = 5

        private const val PREV_STATE_DELAY = 500L
    }
}
