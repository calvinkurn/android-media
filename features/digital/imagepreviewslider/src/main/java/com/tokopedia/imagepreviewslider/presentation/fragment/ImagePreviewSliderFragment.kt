package com.tokopedia.imagepreviewslider.presentation.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.list.adapter.TouchImageAdapter
import com.tokopedia.design.list.decoration.SpaceItemDecoration
import com.tokopedia.imagepreviewslider.R
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.imagepreviewslider.presentation.adapter.ImagePreviewSliderAdapter
import com.tokopedia.imagepreviewslider.presentation.adapter.TouchImageListenerAdapter
import kotlinx.android.synthetic.main.fragment_image_preview_slider.*

/**
 * @author by resakemal on 02/05/19
 */

class ImagePreviewSliderFragment : BaseDaggerFragment() {

    var title = ""
    var imageUrls: java.util.ArrayList<String> = arrayListOf()
    var imageThumbnailUrls: java.util.ArrayList<String> = arrayListOf()
    var imagePosition: Int = 0

    var overlayState = true
    var allowToggleOverlay = true

    var imageSliderLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            title = it.getString(ARG_TITLE, "")
            imageUrls = it.getStringArrayList(ARG_IMAGE_URLS)!!
            imageThumbnailUrls = it.getStringArrayList(ARG_IMAGE_THUMBNAIL_URLS)!!
            imagePosition = it.getInt(ARG_IMAGE_POSITION, 0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_image_preview_slider, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    interface ImagePreviewListener {
        fun onImageClicked(position: Int)
    }

    fun initView() {
        rv_image_list.setHasFixedSize(true)

        setupToolbar()
        setupMainImage()
        setupImageSlider()
        updateImagePosition(imagePosition)
    }

    fun setupToolbar() {
        (activity as ImagePreviewSliderActivity).setSupportActionBar(toolbar)
        (activity as ImagePreviewSliderActivity).supportActionBar?.title = title
        (activity as ImagePreviewSliderActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as ImagePreviewSliderActivity).supportActionBar?.setBackgroundDrawable(
                ColorDrawable(Color.argb(180,0,0,0)))
    }

    fun setupMainImage() {
        val mainImageAdapter = TouchImageListenerAdapter(context!!, imageUrls)
        mainImageAdapter.SetonImageStateChangeListener(object : TouchImageAdapter.OnImageStateChange {
            override fun OnStateDefault() {
                view_pager.SetAllowPageSwitching(true)
                allowToggleOverlay = true
                // Handle picture zoom when overlay is hidden to prevent overlay flickering
                if (overlayState) {
                    toggleOverlay(true)
                } else {
                    overlayState = true
                }

            }

            override fun OnStateZoom() {
                view_pager.SetAllowPageSwitching(false)
                allowToggleOverlay = false
                toggleOverlay(false)
            }

        })
        mainImageAdapter.setOnImageClickListener(object : TouchImageListenerAdapter.ImageClickListener {
            override fun onImageClicked(position: Int) {
                if (allowToggleOverlay) {
                    overlayState = !overlayState
                    toggleOverlay(overlayState)
                }
            }
        })

        view_pager.adapter = mainImageAdapter
        view_pager.currentItem = imagePosition
        view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener(){
            override fun onPageSelected(position: Int) {
                (rv_image_list.adapter as ImagePreviewSliderAdapter).setSelectedImage(position)
                updateImagePosition(position)
            }
        })
    }

    fun setupImageSlider() {
        imageSliderLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_image_list.layoutManager = imageSliderLayoutManager

        val imageSliderListener = object : ImagePreviewListener {
            override fun onImageClicked(position: Int) {
                view_pager.currentItem = position
                updateImagePosition(position)
            }
        }
        val imageSliderAdapter = ImagePreviewSliderAdapter(imageUrls, imagePosition, imageSliderListener)
        rv_image_list.adapter = imageSliderAdapter

        val dividerItemDecoration = SpaceItemDecoration(IMAGE_SLIDER_DIVIDER_SIZE, LinearLayoutManager.HORIZONTAL)
        rv_image_list.addItemDecoration(dividerItemDecoration)
    }

    fun updateImagePosition(position: Int) {
        imagePosition = position
        image_index.text = getString(R.string.image_preview_index, imagePosition + 1, imageUrls.size)
        imageSliderLayoutManager?.smoothScrollToPosition(rv_image_list, RecyclerView.State(), position)
    }

    fun toggleOverlay(value: Boolean) {
        if (value) {
            (activity as ImagePreviewSliderActivity).supportActionBar?.show()
            image_preview_footer.visibility = View.VISIBLE
        } else {
            (activity as ImagePreviewSliderActivity).supportActionBar?.hide()
            image_preview_footer.visibility = View.GONE
        }
    }

    override fun initInjector() {}

    override fun getScreenName(): String = ""

    companion object {
        const val ARG_TITLE = "arg_title"
        const val ARG_IMAGE_URLS = "arg_images"
        const val ARG_IMAGE_THUMBNAIL_URLS = "arg_images_thumbnail"
        const val ARG_IMAGE_POSITION = "arg_image_position"
        const val IMAGE_SLIDER_DIVIDER_SIZE = 32

        fun createInstance (title: String,
                            imageUrls: ArrayList<String>,
                            imageThumbnailUrls: ArrayList<String>,
                            imagePosition: Int): ImagePreviewSliderFragment =
                ImagePreviewSliderFragment().also {
                    it.arguments = Bundle().apply {
                        putString(ARG_TITLE, title)
                        putStringArrayList(ARG_IMAGE_URLS, imageUrls)
                        putStringArrayList(ARG_IMAGE_THUMBNAIL_URLS, imageThumbnailUrls)
                        putInt(ARG_IMAGE_POSITION, imagePosition)
                    }
                }
    }
}