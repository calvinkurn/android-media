package com.tokopedia.catalog.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.catalog.model.raw.CatalogImage
import com.tokopedia.catalog.ui.fragment.CatalogGalleryFragment
import com.tokopedia.kotlin.extensions.view.hide

class CatalogGalleryActivity : BaseSimpleActivity() {
    companion object {
        private const val ARG_CATALOG_ID = "ARG_CATALOG_ID"
        private const val ARG_EXTRA_IMAGES = "ARG_EXTRA_IMAGES"
        private const val ARG_EXTRA_CURRENT_IMAGE = "ARG_EXTRA_CURRENT_IMAGE"

        fun newIntent(context: Context?, catalogId : String, currentItem: Int, catalogImages: ArrayList<CatalogImage>): Intent {
            val intent = Intent(context, CatalogGalleryActivity::class.java)
            intent.putExtra(ARG_CATALOG_ID, catalogId)
            intent.putExtra(ARG_EXTRA_IMAGES, catalogImages)
            intent.putExtra(ARG_EXTRA_CURRENT_IMAGE, currentItem)
            return intent
        }
    }

    override fun getNewFragment(): Fragment? {
        val catalogId: String = intent.getStringExtra(ARG_CATALOG_ID)
        val currentItem: Int = intent.getIntExtra(ARG_EXTRA_CURRENT_IMAGE, 0)
        val catalogImage: ArrayList<CatalogImage> = intent.getParcelableArrayListExtra<CatalogImage>(ARG_EXTRA_IMAGES)
        return CatalogGalleryFragment.newInstance(catalogId,currentItem, catalogImage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.hide()
    }
}