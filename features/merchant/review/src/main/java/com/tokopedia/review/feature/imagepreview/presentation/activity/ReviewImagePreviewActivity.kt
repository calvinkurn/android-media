package com.tokopedia.review.feature.imagepreview.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.review.common.util.OnBackPressedListener
import com.tokopedia.review.feature.imagepreview.presentation.fragment.ReviewImagePreviewFragment

class ReviewImagePreviewActivity : BaseSimpleActivity() {

    companion object {
        const val EXTRA_CACHE_MANAGER_ID = "cacheId"
        const val EXTRA_IS_FROM_GALLERY = "isFromGallery"
        fun getIntent(
            context: Context,
            cacheManagerId: String,
            isFromGallery: Boolean = false
        ): Intent {
            return Intent(context, ReviewImagePreviewActivity::class.java).apply {
                putExtra(EXTRA_CACHE_MANAGER_ID, cacheManagerId)
                putExtra(EXTRA_IS_FROM_GALLERY, isFromGallery)
            }
        }
    }

    private var cacheManagerId = ""
    private var isFromGallery = false
    private var fragment: ReviewImagePreviewFragment? = null

    override fun getNewFragment(): Fragment? {
        fragment = ReviewImagePreviewFragment.newInstance(cacheManagerId, isFromGallery)
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        cacheManagerId = intent.getStringExtra(EXTRA_CACHE_MANAGER_ID) ?: ""
        isFromGallery = intent.getBooleanExtra(EXTRA_IS_FROM_GALLERY, false)
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