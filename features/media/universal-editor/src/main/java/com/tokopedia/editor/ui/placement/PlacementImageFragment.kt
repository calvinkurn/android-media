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
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.view.binding.viewBinding
import com.yalantis.ucrop.view.TransformImageView
import java.io.File
import java.lang.Exception
import javax.inject.Inject

class PlacementImageFragment @Inject constructor() : BaseEditorFragment(R.layout.fragment_placement) {

    private val viewModel: PlacementImageViewModel by activityViewModels()
    private val viewBinding: FragmentPlacementBinding? by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initObserver() {}

    override fun initView() {
        viewBinding?.cropArea?.let { ucropRef ->
            ucropRef.getCropImageView()?.let { gestureCropImage ->

                val outputPath = FileUtil.getTokopediaInternalDirectory(CACHE_FOLDER).path + RESULT_FILE_NAME
                gestureCropImage.setImageUri(
                    Uri.fromFile(File(viewModel.imagePath)),
                    Uri.parse(outputPath)
                )

                gestureCropImage.setTransformImageListener(object :
                    TransformImageView.TransformImageListener {
                    override fun onLoadComplete() {
                        gestureCropImage.post {
                            ucropRef.getOverlayView()?.setTargetAspectRatio(9/16f)
                        }
                    }

                    override fun onLoadFailure(e: Exception) {}

                    override fun onRotate(currentAngle: Float) {}

                    override fun onScale(currentScale: Float) {}
                })
            }
        }
    }

    fun captureImage(onFinish: (path: String?) -> Unit) {
        viewBinding?.cropArea?.getCropImageView()?.let {
            it.customCrop { filePath ->
                onFinish(filePath)
            }
        }
    }

    companion object {
        private const val RESULT_FILE_NAME = "/stories_editor.png"
        private const val CACHE_FOLDER = "Tokopedia"
    }
}
