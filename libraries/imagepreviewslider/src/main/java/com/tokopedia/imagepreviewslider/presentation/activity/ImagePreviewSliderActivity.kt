package com.tokopedia.imagepreviewslider.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.imagepreviewslider.presentation.fragment.ImagePreviewSliderFragment


/**
 * @author by resakemal on 02/05/19
 */

class ImagePreviewSliderActivity: BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun getNewFragment(): Fragment = ImagePreviewSliderFragment.createInstance(
            intent.getStringExtra(ImagePreviewSliderFragment.ARG_TITLE) ?: "",
            intent.getStringArrayListExtra(ImagePreviewSliderFragment.ARG_IMAGE_URLS) as ArrayList<String>,
            intent.getStringArrayListExtra(ImagePreviewSliderFragment.ARG_IMAGE_THUMBNAIL_URLS) as ArrayList<String>,
            intent.getIntExtra(ImagePreviewSliderFragment.ARG_IMAGE_POSITION, 0))

    companion object {
        fun getCallingIntent(context: Context,title: String = "", imageUrls: List<String>,
                             imageThumbnailUrls: List<String>, imagePosition: Int = 0): Intent =
                Intent(context, ImagePreviewSliderActivity::class.java)
                        .putExtra(ImagePreviewSliderFragment.ARG_TITLE, title)
                        .putExtra(ImagePreviewSliderFragment.ARG_IMAGE_URLS, ArrayList(imageUrls))
                        .putExtra(ImagePreviewSliderFragment.ARG_IMAGE_THUMBNAIL_URLS, ArrayList(imageThumbnailUrls))
                        .putExtra(ImagePreviewSliderFragment.ARG_IMAGE_POSITION, imagePosition)
    }

}