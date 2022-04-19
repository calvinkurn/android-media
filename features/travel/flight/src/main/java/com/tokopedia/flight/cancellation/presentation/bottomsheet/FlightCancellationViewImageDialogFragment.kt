package com.tokopedia.flight.cancellation.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.tokopedia.flight.R
import kotlinx.android.synthetic.main.dialog_fragment_flight_cancellation_view_image.*
import java.io.File

/**
 * @author by furqan on 26/04/2021
 */
class FlightCancellationViewImageDialogFragment : DialogFragment() {

    private var filePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        filePath = arguments?.getString(EXTRA_FILE_PATH) ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_fragment_flight_cancellation_view_image, container, false)
        view.setBackgroundResource(android.R.color.transparent)
        view.requestLayout()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        showImage()

    }

    private fun showImage() {
        context?.let {
            Glide.with(it)
                    .asBitmap()
                    .load(File(filePath))
                    .into(flight_cancellation_image_view)
        }
    }

    companion object {

        private const val EXTRA_FILE_PATH = "EXTRA_FILE_PATH"

        fun newInstance(filePath: String): FlightCancellationViewImageDialogFragment =
                FlightCancellationViewImageDialogFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_FILE_PATH, filePath)
                    }
                }

    }

}