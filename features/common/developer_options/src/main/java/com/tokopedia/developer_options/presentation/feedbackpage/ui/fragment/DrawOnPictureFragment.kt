package com.tokopedia.developer_options.presentation.feedbackpage.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.feedbackpage.ui.activity.DrawOnPictureActivity.Companion.EXTRA_IMAGE_URI
import kotlinx.android.synthetic.main.fragment_draw_on_picture.*

/**
 * @author by furqan on 29/09/2020
 */
class DrawOnPictureFragment : Fragment() {

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

        if (::imageUri.isInitialized) {
            dopFeedbackForm.setImageURI(imageUri)
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