package com.tokopedia.review.feature.gallery.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.review.common.util.OnBackPressedListener
import com.tokopedia.review.feature.gallery.presentation.fragment.ReviewGalleryFragment

class ReviewGalleryActivity : BaseSimpleActivity() {

    companion object {
        const val EXTRA_CACHE_MANAGER_ID = "cacheId"
        fun getIntent(context: Context, cacheManagerId: String): Intent {
            return Intent(context, ReviewGalleryActivity::class.java).putExtra(EXTRA_CACHE_MANAGER_ID, cacheManagerId)
        }
    }

    private var cacheManagerId = ""
    private var fragment: ReviewGalleryFragment? = null

    override fun getNewFragment(): Fragment? {
        fragment = ReviewGalleryFragment.newInstance(cacheManagerId)
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        cacheManagerId = intent.getStringExtra(EXTRA_CACHE_MANAGER_ID) ?: ""
        super.onCreate(savedInstanceState)
        hideToolbar()
    }

    override fun onBackPressed() {
        (fragment as? OnBackPressedListener)?.onBackPressed()
        super.onBackPressed()
    }

    private fun hideToolbar() {
        toolbar?.hide()
    }
}