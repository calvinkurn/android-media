package com.tokopedia.oldcatalog.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.oldcatalog.model.raw.CatalogImage
import com.tokopedia.oldcatalog.ui.fragment.CatalogGalleryFragment
import com.tokopedia.kotlin.extensions.view.hide

class CatalogGalleryActivity : BaseSimpleActivity() {
    companion object {
        private const val ARG_CATALOG_NAME = "ARG_CATALOG_NAME"
        private const val ARG_CATALOG_ID = "ARG_CATALOG_ID"
        private const val ARG_EXTRA_IMAGES = "ARG_EXTRA_IMAGES"
        private const val ARG_EXTRA_CURRENT_IMAGE = "ARG_EXTRA_CURRENT_IMAGE"
        private const val ARG_SHOW_BOTTOM_GALLERY = "ARG_SHOW_BOTTOM_GALLERY"
        private const val ARG_SHOW_BOTTOM_NUMBERING = "ARG_SHOW_BOTTOM_NUMBERING"
        private const val ARG_REVIEW_ID = "ARG_REVIEW_ID"

        fun newIntent(context: Context?, catalogName : String,catalogId : String, currentItem: Int,
                      catalogImages: ArrayList<CatalogImage>,
                      showBottomGallery : Boolean = false,
                      showNumbering : Boolean = false,
                      reviewId : String = "" ): Intent {
            val intent = Intent(context, CatalogGalleryActivity::class.java)
            intent.putExtra(ARG_CATALOG_NAME, catalogName)
            intent.putExtra(ARG_CATALOG_ID, catalogId)
            intent.putExtra(ARG_EXTRA_IMAGES, catalogImages)
            intent.putExtra(ARG_EXTRA_CURRENT_IMAGE, currentItem)
            intent.putExtra(ARG_SHOW_BOTTOM_GALLERY,showBottomGallery)
            intent.putExtra(ARG_SHOW_BOTTOM_NUMBERING,showNumbering)
            intent.putExtra(ARG_REVIEW_ID,reviewId)
            return intent
        }
    }

    override fun getNewFragment(): Fragment {
        val catalogName: String = intent.getStringExtra(ARG_CATALOG_NAME) ?: ""
        val catalogId: String = intent.getStringExtra(ARG_CATALOG_ID) ?: ""
        val currentItem: Int = intent.getIntExtra(ARG_EXTRA_CURRENT_IMAGE, 0)
        val catalogImage: ArrayList<CatalogImage> = intent.getParcelableArrayListExtra(ARG_EXTRA_IMAGES) ?: ArrayList()
        val showBottomGallery : Boolean = intent.getBooleanExtra(ARG_SHOW_BOTTOM_GALLERY,false)
        val showBottomNumbering : Boolean = intent.getBooleanExtra(ARG_SHOW_BOTTOM_NUMBERING,false)
        val reviewId : String = intent.getStringExtra(ARG_REVIEW_ID) ?: ""
        return CatalogGalleryFragment.newInstance(catalogName,catalogId,currentItem, catalogImage,
            showBottomGallery,
            showBottomNumbering, reviewId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.hide()
    }
}
