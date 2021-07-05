package com.tokopedia.gallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.gallery.customview.BottomSheetImageReviewSlider
import java.util.*

/**
 * For navigate: use ApplinkConstInternalMarketplace.IMAGE_REVIEW_GALLERY
 */
class ImageReviewGalleryActivity : BaseSimpleActivity() {

    var bottomSheetImageReviewSlider: BottomSheetImageReviewSlider? = null
        private set
    var productId: String = ""
        private set
    var defaultPosition: Int = 0
        private set
    var imageUrlList: ArrayList<String>? = null
        private set

    val isImageListPreloaded: Boolean
        get() = imageUrlList != null && !imageUrlList!!.isEmpty()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromIntent()
        bindView()
    }

    private fun getDataFromIntent() {
        val uri = intent.data
        if (uri != null) {
            val segments = uri.pathSegments
            productId = segments[1]
        } else {
            productId = intent.getStringExtra(EXTRA_PRODUCT_ID) ?: ""
        }
        defaultPosition = intent.getIntExtra(EXTRA_DEFAULT_POSITION, 0)
        imageUrlList = intent.getStringArrayListExtra(EXTRA_IMAGE_URL_LIST)
    }

    private fun bindView() {
        bottomSheetImageReviewSlider = findViewById(R.id.bottomSheetImageSlider)
    }

    override fun onBackPressed() {
        if (isImageListPreloaded || !bottomSheetImageReviewSlider!!.onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun getNewFragment(): Fragment {
        return ImageReviewGalleryFragment.createInstance()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_review_gallery
    }

    override fun getParentViewResourceID(): Int {
        return R.id.activity_review_gallery_parent_view
    }

    companion object {

        private val EXTRA_PRODUCT_ID = "product_id"
        private val EXTRA_IMAGE_URL_LIST = "EXTRA_IMAGE_URL_LIST"
        private val EXTRA_DEFAULT_POSITION = "EXTRA_DEFAULT_POSITION"

        fun moveTo(activity: Activity?, productId: String) {
            if (activity != null) {
                val intent = Intent(activity, ImageReviewGalleryActivity::class.java)
                intent.putExtra(EXTRA_PRODUCT_ID, productId)
                activity.startActivity(intent)
            }
        }

        fun moveTo(context: Context?, imageUrlList: ArrayList<String>, defaultPosition: Int?) {
            if (context != null) {
                val intent = Intent(context, ImageReviewGalleryActivity::class.java)
                intent.putStringArrayListExtra(EXTRA_IMAGE_URL_LIST, imageUrlList)
                intent.putExtra(EXTRA_DEFAULT_POSITION, defaultPosition)
                context.startActivity(intent)
            }
        }
    }
}
