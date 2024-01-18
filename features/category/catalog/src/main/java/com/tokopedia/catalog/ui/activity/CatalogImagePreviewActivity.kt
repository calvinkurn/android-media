package com.tokopedia.catalog.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.catalog.R
import com.tokopedia.catalog.analytics.CatalogReimagineDetailAnalytics
import com.tokopedia.catalog.analytics.CatalogTrackerConstant
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.EVENT_CLICK_CLOSE_ON_IMAGE_REVIEW
import com.tokopedia.catalog.analytics.CatalogTrackerConstant.TRACKER_ID_CLICK_CLOSE_ON_IMAGE_REVIEW
import com.tokopedia.catalogcommon.uimodel.BuyerReviewUiModel
import com.tokopedia.image_gallery.ImageGallery
import com.tokopedia.image_gallery.ImageGalleryItem
import com.tokopedia.kotlin.extensions.view.ZERO

class CatalogImagePreviewActivity : BaseSimpleActivity() {

    companion object {
        const val ARG_PARAM_IMAGE_LIST = "image_list"
        const val ARG_PARAM_DEFAULT_INDEX = "default_index"
        const val ARG_PARAM_TRACKER_MODEL = "tracker_model"
        const val ARG_PARAM_CATALOG_ID = "catalog_id"

        @JvmStatic
        fun createIntent(
            context: Context,
            imageList: List<String>,
            defaultIndex: Int
        ): Intent {
            val intent = Intent(context, CatalogImagePreviewActivity::class.java)
            intent.putStringArrayListExtra(ARG_PARAM_IMAGE_LIST, ArrayList(imageList))
            intent.putExtra(ARG_PARAM_DEFAULT_INDEX, defaultIndex)
            return intent
        }

        @JvmStatic
        fun setTrackerDataIntent(
            intent: Intent?,
            catalogId: String,
            buyerReviewItem: BuyerReviewUiModel.ItemBuyerReviewUiModel
        ) {
            intent?.putExtra(ARG_PARAM_TRACKER_MODEL, buyerReviewItem)
            intent?.putExtra(ARG_PARAM_CATALOG_ID, catalogId)
        }
    }

    private fun setupViews(imageList: List<ImageGalleryItem>, defaultIndex: Int) {
        val imageGallery: ImageGallery = findViewById(R.id.imageGalleryUnify)
        val closeButton: View = findViewById(R.id.closeButton)
        imageGallery.setImages(ArrayList(imageList), defaultIndex)
        closeButton.setOnClickListener {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog_image_preview)
        val imageList = intent.getStringArrayListExtra(ARG_PARAM_IMAGE_LIST).orEmpty().map {
            ImageGalleryItem(null, it)
        }
        val defaultIndex = intent.getIntExtra(ARG_PARAM_DEFAULT_INDEX, Int.ZERO)
        setupViews(imageList, defaultIndex)
    }

    override fun onDestroy() {
        val reviewUiModel = intent.getParcelableExtra<BuyerReviewUiModel.ItemBuyerReviewUiModel>(ARG_PARAM_TRACKER_MODEL)
        val catalogId = intent.getStringExtra(ARG_PARAM_CATALOG_ID)
        CatalogReimagineDetailAnalytics.sendEventPG(
            action = EVENT_CLICK_CLOSE_ON_IMAGE_REVIEW,
            category = CatalogTrackerConstant.EVENT_CATEGORY_CATALOG_PAGE,
            labels = "${reviewUiModel?.catalogName} $catalogId - feedback_id:${reviewUiModel?.reviewId}",
            trackerId = TRACKER_ID_CLICK_CLOSE_ON_IMAGE_REVIEW
        )
        super.onDestroy()
    }

    override fun getNewFragment(): Fragment? {
        return null
    }
}
