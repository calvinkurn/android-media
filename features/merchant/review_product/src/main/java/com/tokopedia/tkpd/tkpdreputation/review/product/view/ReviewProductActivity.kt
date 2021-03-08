package com.tokopedia.tkpd.tkpdreputation.review.product.view

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent

/**
 *
 */
class ReviewProductActivity : BaseSimpleActivity(), HasComponent<BaseAppComponent> {

    override fun getNewFragment(): Fragment? {
        val uri = intent.data
        val productId = if (uri != null) {
            val segments = uri.pathSegments
            segments.getOrNull(segments.size - 2) ?: "0"
        } else {
            intent?.extras?.getString(ReviewProductFragment.EXTRA_PRODUCT_ID) ?: ""
        }
        return ReviewProductFragment.getInstance(productId)
    }

    override fun getComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }

    override fun getParentViewResourceID(): Int {
        return com.tokopedia.abstraction.R.id.parent_view
    }

    override fun getLayoutRes(): Int {
        return com.tokopedia.abstraction.R.layout.activity_base_simple
    }

}