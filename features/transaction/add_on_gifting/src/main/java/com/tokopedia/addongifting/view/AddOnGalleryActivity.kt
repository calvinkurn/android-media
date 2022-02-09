package com.tokopedia.addongifting.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.addongifting.R
import com.tokopedia.addongifting.databinding.GalleryActivityBinding
import com.tokopedia.image_gallery.ImageGalleryItem
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show

class AddOnGalleryActivity : BaseSimpleActivity() {

    private var viewBinding: GalleryActivityBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = GalleryActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding?.root)
        renderImageGallery()
    }

    private fun renderImageGallery() {
        val imageGalleryItems = arrayListOf<ImageGalleryItem>()
        intent.extras?.getStringArrayList("ADD_ON_IMAGES")?.forEach {
            imageGalleryItems.add(ImageGalleryItem(null, it))
        }
        if (imageGalleryItems.isNotEmpty()) {
            viewBinding?.imageGalleryAddOn?.apply {
                isHiddenOverlay = true
                setImages(imageGalleryItems)
            }
            viewBinding?.imageGalleryAddOn?.show()
        } else {
            viewBinding?.imageGalleryAddOn?.gone()
        }

        viewBinding?.buttonClose?.setOnClickListener {
            finish()
        }
    }

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int {
        return R.layout.gallery_activity
    }

}