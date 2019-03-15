package com.tokopedia.gallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.gallery.customview.BottomSheetImageReviewSlider
import com.tokopedia.gallery.customview.GalleryItemDecoration
import com.tokopedia.gallery.domain.GetImageReviewUseCase
import com.tokopedia.gallery.presenter.ReviewGalleryPresenter
import com.tokopedia.gallery.presenter.ReviewGalleryPresenterImpl
import com.tokopedia.gallery.tracking.ImageReviewGalleryTracking
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.graphql.domain.GraphqlUseCase

import java.util.ArrayList

/**
 * For navigate: use ApplinkConstInternalMarketplace.IMAGE_REVIEW_GALLERY
 */
class ImageReviewGalleryActivity : BaseSimpleActivity() {

    var bottomSheetImageReviewSlider: BottomSheetImageReviewSlider? = null
        private set
    var productId: Int = 0
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
            productId = segments[1].toInt()
        } else {
            productId = intent.getIntExtra(EXTRA_PRODUCT_ID, 0)
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

    companion object {

        private val EXTRA_PRODUCT_ID = "product_id"
        private val EXTRA_IMAGE_URL_LIST = "EXTRA_IMAGE_URL_LIST"
        private val EXTRA_DEFAULT_POSITION = "EXTRA_DEFAULT_POSITION"

        fun moveTo(activity: Activity?, productId: Int) {
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
