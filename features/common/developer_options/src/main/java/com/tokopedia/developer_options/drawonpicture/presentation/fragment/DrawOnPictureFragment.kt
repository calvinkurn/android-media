package com.tokopedia.developer_options.drawonpicture.presentation.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.drawonpicture.di.DrawOnPictureComponent
import com.tokopedia.developer_options.drawonpicture.presentation.activity.DrawOnPictureActivity.Companion.EXTRA_IMAGE_URI
import com.tokopedia.developer_options.drawonpicture.presentation.adapter.BrushColorAdapter
import com.tokopedia.developer_options.drawonpicture.presentation.adapter.viewholder.BrushColorViewHolder
import com.tokopedia.developer_options.drawonpicture.presentation.viewmodel.DrawOnPictureViewModel
import com.tokopedia.developer_options.drawonpicture.widgets.DrawOnPictureView
import kotlinx.android.synthetic.main.fragment_draw_on_picture.*
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
        dopFeedbackForm.changeBrushColor(color)
        adapter.setSelectedColor(dopFeedbackForm.getCurrentBrushColor())
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(DrawOnPictureComponent::class.java).inject(this)
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
        updateStrokeSizeText()

        seekbarDopBrushSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar, p1: Int, p2: Boolean) {
                // minimum size is 5
                dopFeedbackForm.changeStrokeWidth(p0.progress + 5F)
                updateStrokeSizeText()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        dopFeedbackForm.listener = this
        if (::imageUri.isInitialized) {
            dopFeedbackForm.setImageURI(imageUri)
        }

        btnDoPBack.setOnClickListener {
            activity?.finish()
        }
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

    private fun updateStrokeSizeText() {
        tvBrushSize.text = dopFeedbackForm.getStrokeWidth().toInt().toString()
    }

    private fun setupBrushColor() {
        adapter = BrushColorAdapter(this, dopFeedbackForm.getCurrentBrushColor(),
                resources.getStringArray(R.array.brush_color_options).toList())
        rvBrushColors.setHasFixedSize(true)
        rvBrushColors.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        rvBrushColors.adapter = adapter
    }

    private fun showPencilOptions() {
        seekbarDopBrushSize.visibility = View.VISIBLE
        rvBrushColors.visibility = View.VISIBLE
    }

    private fun hidePencilOptions() {
        seekbarDopBrushSize.visibility = View.GONE
        rvBrushColors.visibility = View.GONE
    }

    companion object {
        fun getInstance(imageUri: Uri?): DrawOnPictureFragment =
                DrawOnPictureFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(EXTRA_IMAGE_URI, imageUri)
                    }
                }
    }

}