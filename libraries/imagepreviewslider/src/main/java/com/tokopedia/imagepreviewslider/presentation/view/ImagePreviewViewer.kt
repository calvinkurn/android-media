package com.tokopedia.imagepreviewslider.presentation.view

import android.content.Context
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stfalcon.imageviewer.StfalconImageViewer
import com.tokopedia.design.list.adapter.SpaceItemDecoration
import com.tokopedia.imagepreviewslider.presentation.adapter.ImagePreviewSliderAdapter
import com.tokopedia.imagepreviewslider.presentation.listener.ImageSliderListener
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.view_image_overlay.view.*

/**
 * @author by jessica on 2019-12-16
 */

class ImagePreviewViewer {

    private lateinit var viewer: StfalconImageViewer<String>

    private lateinit var imageSliderListener: ImageSliderListener

    private lateinit var rvImageList: RecyclerView

    private lateinit var imagePreviewSliderAdapter: ImagePreviewSliderAdapter

    private lateinit var imagePreviewLayoutManager: LinearLayoutManager

    private lateinit var overlayView: ImageOverlayView


    companion object {
        @JvmStatic
        val instance by lazy { ImagePreviewViewer() }
    }

    fun startImagePreviewViewer(title: String = "", imageViewTransitionFrom: ImageView?, imageList: List<String>?, context: Context?, index: Int) {
        imageSliderListener = object : ImageSliderListener {
            override fun onImageClicked(position: Int) {
                viewer.setCurrentPosition(position)
            }
        }
        setupOverlayView(title, imageList, context, index)
        startViewer(imageList, imageViewTransitionFrom, context, index)
    }

    private fun setupOverlayView(title: String = "", imageList: List<String>? = null, context: Context?, index: Int) {

        overlayView = ImageOverlayView(context).apply {

            val overlayBackButton = btn_arrow_back
            overlayBackButton.setBackgroundResource(com.tokopedia.resources.common.R.drawable.ic_system_action_back_grayscale_24)
            overlayBackButton.setOnClickListener {
                viewer.close()
            }

            val overlayTitle = tv_title_overlay
            overlayTitle.text = title

            rvImageList = rv_image_list
            rvImageList.apply {
                setHasFixedSize(true)
                imagePreviewLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                layoutManager = imagePreviewLayoutManager
                imagePreviewSliderAdapter = ImagePreviewSliderAdapter(imageList?.toMutableList()
                        ?: mutableListOf(),
                        index, imageSliderListener)
                adapter = imagePreviewSliderAdapter

                val dividerItemDecoration = SpaceItemDecoration(20, LinearLayoutManager.HORIZONTAL)
                addItemDecoration(dividerItemDecoration)
            }
            (rvImageList.adapter as ImagePreviewSliderAdapter).setSelectedImage(index)
            updateImageIndexPosition(index, imageList)
        }
    }

    private fun startViewer(imageList: List<String>?, imageViewTransitionFrom: ImageView?, context: Context?, index: Int) {
        context?.let {
            viewer = StfalconImageViewer.Builder<String>(context, imageList, ::loadImages)
                    .withBackgroundColor(androidx.core.content.ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Static_Black_96))
                    .withStartPosition(index)
                    .withTransitionFrom(imageViewTransitionFrom)
                    .withImageChangeListener {
                        overlayView.updateImageIndexPosition(it, imageList)
                        imagePreviewLayoutManager.smoothScrollToPosition(rvImageList, RecyclerView.State(), it)
                        imagePreviewSliderAdapter.setSelectedImage(it)
                    }
                    .withOverlayView(overlayView)
                    .show()
        }
    }

    private fun loadImages(imageView: ImageView?, imageList: String?) {
        imageView?.let {
            it.loadImage(imageList ?: "", com.tokopedia.design.R.drawable.ic_loading_image)
        }
    }
}