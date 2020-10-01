package com.tokopedia.developer_options.drawonpicture.presentation.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.drawonpicture.di.DrawOnPictureComponent
import com.tokopedia.developer_options.drawonpicture.presentation.activity.DrawOnPictureActivity.Companion.EXTRA_IMAGE_URI
import com.tokopedia.developer_options.drawonpicture.widgets.DrawOnPictureView
import kotlinx.android.synthetic.main.fragment_draw_on_picture.*

/**
 * @author by furqan on 29/09/2020
 */
class DrawOnPictureFragment : BaseDaggerFragment(), DrawOnPictureView.Listener {

    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getParcelable<Uri>(EXTRA_IMAGE_URI)?.let {
            imageUri = it
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_draw_on_picture, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    override fun onActionDraw() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActionUp() {
        setupUndoButton()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(DrawOnPictureComponent::class.java).inject(this)
    }

    private fun initViews() {
        setupUndoButton()

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

    companion object {
        fun getInstance(imageUri: Uri?): DrawOnPictureFragment =
                DrawOnPictureFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(EXTRA_IMAGE_URI, imageUri)
                    }
                }
    }

}