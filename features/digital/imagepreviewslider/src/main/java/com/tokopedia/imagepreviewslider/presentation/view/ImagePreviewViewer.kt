package com.tokopedia.imagepreviewslider.presentation.view

import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stfalcon.imageviewer.StfalconImageViewer
import com.tokopedia.design.list.adapter.SpaceItemDecoration
import com.tokopedia.imagepreviewslider.R
import com.tokopedia.imagepreviewslider.presentation.adapter.ImagePreviewSliderAdapter
import com.tokopedia.imagepreviewslider.presentation.listener.ImageSliderListener
import com.tokopedia.imagepreviewslider.presentation.util.ReflectionPosition
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.view_image_overlay.view.*

/**
 * @author by jessica on 2019-12-16
 */

class ImagePreviewViewer {

    private lateinit var viewer: StfalconImageViewer<String>

    private lateinit var imageSliderListener: ImageSliderListener

    private lateinit var rvImageListFlight: RecyclerView

    private lateinit var imagePreviewSliderAdapter: ImagePreviewSliderAdapter

    private lateinit var imageFlightPreviewLayoutManager: LinearLayoutManager

    private lateinit var overlayView: ImageOverlayView

    companion object {
        @JvmStatic
        private var instance: ImagePreviewViewer? = null

        @Synchronized
        private fun createInstance() {
            if (instance == null) {
                instance = ImagePreviewViewer()
            }
        }

        internal fun getInstance(): ImagePreviewViewer {
            if (instance == null) createInstance()
            return instance!!
        }
    }
    fun startImageFlightPreviewViewer(title: String = "", imageViewTransitionFrom: ImageView?, imageList: List<String>?, context: Context?, index: Int) {
        imageSliderListener = object : ImageSliderListener {
            override fun onImageClicked(position: Int) {
                ReflectionPosition(viewer, position)
            }
        }
        setupOverlayView(title, imageList, context, index)
        startViewer(imageList, imageViewTransitionFrom, context, index)
    }

    private fun setupOverlayView(title: String = "", imageList: List<String>? = null, context: Context?, index: Int) {

        overlayView = ImageOverlayView(context).apply {

            val overlayBackButton = btn_arrow_back
            overlayBackButton.setOnClickListener {
                viewer.dismiss()
            }

            val overlayTitle = tv_title_overlay
            overlayTitle.text = title

            rvImageListFlight = rv_image_list_flight
            rvImageListFlight.apply {
                setHasFixedSize(true)
                imageFlightPreviewLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                layoutManager = imageFlightPreviewLayoutManager
                imagePreviewSliderAdapter = ImagePreviewSliderAdapter(imageList?.toMutableList() ?: mutableListOf(),
                        index, imageSliderListener)
                adapter = imagePreviewSliderAdapter

                val dividerItemDecoration = SpaceItemDecoration(20, LinearLayoutManager.HORIZONTAL)
                addItemDecoration(dividerItemDecoration)
            }
            (rvImageListFlight.adapter as ImagePreviewSliderAdapter).setSelectedImage(index)
            updateImageIndexPosition(index, imageList)
        }
    }

    private fun startViewer(imageList: List<String>?, imageViewTransitionFrom: ImageView?, context: Context?, index: Int) {

        viewer = StfalconImageViewer.Builder<String>(context, imageList, ::loadTheImage)
                .withBackgroundColor(Color.BLACK)
                .withStartPosition(index)
                .withTransitionFrom(imageViewTransitionFrom)
                .withImageChangeListener {
                    overlayView.updateImageIndexPosition(it, imageList)
                    imageFlightPreviewLayoutManager.smoothScrollToPosition(rvImageListFlight, RecyclerView.State(), it)
                    imagePreviewSliderAdapter.setSelectedImage(it)
                }
                .withOverlayView(overlayView)
                .show()
    }

    private fun loadTheImage(imageView: ImageView?, imageList: String?) {
        try {
            imageView?.let {
                it.loadImage(imageList ?: "", com.tokopedia.design.R.drawable.ic_loading_image)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}