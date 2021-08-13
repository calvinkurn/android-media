package com.tokopedia.feedback_form.drawonpicture.presentation.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewTreeObserver
import android.widget.SeekBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.feedback_form.drawonpicture.di.DrawOnPictureComponent
import com.tokopedia.feedback_form.drawonpicture.widgets.DrawOnPictureView
import com.tokopedia.feedback_form.R
import com.tokopedia.feedback_form.drawonpicture.presentation.Utils
import com.tokopedia.feedback_form.drawonpicture.presentation.activity.DrawOnPictureActivity.Companion.EXTRA_IMAGE_URI
import com.tokopedia.feedback_form.drawonpicture.presentation.adapter.BrushColorAdapter
import com.tokopedia.feedback_form.drawonpicture.presentation.adapter.viewholder.BrushColorViewHolder
import com.tokopedia.feedback_form.drawonpicture.presentation.viewmodel.DrawOnPictureViewModel
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.file.PublicFolderUtil
import com.tokopedia.utils.image.ImageProcessingUtil
import kotlinx.android.synthetic.main.fragment_draw_on_picture.*
import java.io.File
import javax.inject.Inject

/**
 * @author by furqan on 29/09/2020
 */
class DrawOnPictureFragment : BaseDaggerFragment(),
        DrawOnPictureView.Listener,
        BrushColorViewHolder.Listener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: DrawOnPictureViewModel

    private lateinit var imageUri: Uri
    private lateinit var adapter: BrushColorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = viewModelProvider.get(DrawOnPictureViewModel::class.java)
        }

        arguments?.getParcelable<Uri>(EXTRA_IMAGE_URI)?.let {
            imageUri = it
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_draw_on_picture, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        containerDOP.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                containerDOP.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val imageSize = ImageProcessingUtil.getWidthAndHeight(requireContext(), imageUri)
                val heightRatio: Float = containerDOP.height.toFloat() / imageSize.second.toFloat()
                val widthRatio: Float = containerDOP.width.toFloat() / imageSize.first.toFloat()
                val layoutParams = dopFeedbackForm.layoutParams
                if (heightRatio < widthRatio) {
                    layoutParams.height = MATCH_PARENT
                } else {
                    layoutParams.width = MATCH_PARENT
                }

                dopFeedbackForm.requestLayout()
            }
        })

        initViews()
        observeViewModel()
    }

    override fun onActionDraw() {
        viewModel.startDrawing()
    }

    override fun onActionUp() {
        setupUndoButton()
        viewModel.stopDrawing()
    }

    override fun onItemClicked(color: String) {
        Utils.changeShapeColor(bgDoPencil.background, color)
        dopFeedbackForm.changeBrushColor(color)
        adapter.setSelectedColor(dopFeedbackForm.getCurrentBrushColor())
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(DrawOnPictureComponent::class.java).inject(this)
    }

    fun saveNewImage() {
        showLoadingDialog()

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(File(imageUri.path).absolutePath, options)

        val editedBitmap = dopFeedbackForm.getBitmap()
        val saveBitmap = if (options.outWidth > 0 && options.outHeight > 0)
            Bitmap.createScaledBitmap(editedBitmap, options.outWidth, options.outHeight, false)
        else editedBitmap

        val newFileAndUri = PublicFolderUtil.putImageToPublicFolder(requireActivity(), saveBitmap, FileUtil.generateUniqueFileNameDateFormat(Math.random().toInt()))
        newFileAndUri.first?.let {
            sendNewPathResult(it.absolutePath, imageUri.path)
        }
    }


    private fun observeViewModel() {
        viewModel.showPencilOptions.observe(viewLifecycleOwner, Observer {
            if (it) {
                showPencilOptions()
            } else {
                hidePencilOptions()
            }
        })
    }

    private fun initViews() {
        setupUndoButton()
        setupBrushColor()
        setupBrushSize()

        dopFeedbackForm.listener = this
        if (::imageUri.isInitialized) {
            dopFeedbackForm.setImageURI(imageUri)
        }
        Utils.changeShapeColor(bgDoPencil.background, dopFeedbackForm.getCurrentBrushColor())

        btnDoPUndo.setOnClickListener {
            dopFeedbackForm.undoChange()
            setupUndoButton()
        }
    }

    private fun setupUndoButton() {
        if (dopFeedbackForm.canUndo()) {
            btnDoPUndo.visibility = View.VISIBLE
        } else {
            btnDoPUndo.visibility = View.GONE
        }
    }

    private fun setupBrushColor() {
        adapter = BrushColorAdapter(this, dopFeedbackForm.getCurrentBrushColor(),
                resources.getStringArray(R.array.brush_color_options).toList())
        rvBrushColors.setHasFixedSize(true)
        rvBrushColors.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        rvBrushColors.adapter = adapter
    }

    private fun setupBrushSize() {
        seekbarDopBrushSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar, p1: Int, p2: Boolean) {
                // minimum size is 5
                dopFeedbackForm.changeStrokeWidth(p0.progress + 5F)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    private fun showPencilOptions() {
        seekbarDopBrushSize.visibility = View.VISIBLE
        rvBrushColors.visibility = View.VISIBLE
    }

    private fun hidePencilOptions() {
        seekbarDopBrushSize.visibility = View.GONE
        rvBrushColors.visibility = View.GONE
    }

    private fun showLoadingDialog() {
        ProgressDialog.show(requireContext(), "", "Menyimpan gambar ...", true, true)
    }

    private fun sendNewPathResult(path: String, oldPath: String?) {
        val intent = Intent()
        intent.putExtra(EXTRA_DRAW_IMAGE_URI, Uri.parse(path))
        intent.putExtra(EXTRA_DRAW_IMAGE_URI_OLD, oldPath)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    companion object {

        const val EXTRA_DRAW_IMAGE_URI = "EXTRA_DRAW_IMAGE_URI"
        const val EXTRA_DRAW_IMAGE_URI_OLD = "EXTRA_DRAW_IMAGE_URI_OLD"

        fun getInstance(imageUri: Uri?): DrawOnPictureFragment =
                DrawOnPictureFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(EXTRA_IMAGE_URI, imageUri)
                    }
                }
    }

}