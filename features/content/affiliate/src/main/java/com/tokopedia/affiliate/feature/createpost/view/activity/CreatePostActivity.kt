package com.tokopedia.affiliate.feature.createpost.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.createpost.TYPE_AFFILIATE
import com.tokopedia.affiliate.feature.createpost.TYPE_CONTENT_SHOP
import com.tokopedia.affiliate.feature.createpost.view.fragment.AffiliateCreatePostFragment
import com.tokopedia.affiliate.feature.createpost.view.fragment.ContentCreatePostFragment
import com.tokopedia.applink.ApplinkConst

class CreatePostActivity : BaseSimpleActivity() {

    companion object {
        const val PARAM_PRODUCT_ID = "product_id"
        const val PARAM_AD_ID = "ad_id"
        const val PARAM_POST_ID = "post_id"
        const val PARAM_TYPE = "author_type"

        fun getInstanceAffiliate(context: Context, productId: String, adId: String): Intent {
            val intent = Intent(context, CreatePostActivity::class.java)
            intent.putExtra(PARAM_PRODUCT_ID, productId)
            intent.putExtra(PARAM_AD_ID, adId)
            return intent
        }
    }

    object DeepLinkIntents {
        @DeepLink(ApplinkConst.AFFILIATE_CREATE_POST)
        @JvmStatic
        fun getInstanceAffiliate(context: Context, bundle: Bundle): Intent {
            val intent = Intent(context, CreatePostActivity::class.java)
            intent.putExtras(bundle)
            intent.putExtra(PARAM_TYPE, TYPE_AFFILIATE)
            return intent
        }

        @DeepLink(ApplinkConst.CONTENT_CREATE_POST)
        @JvmStatic
        fun getInstanceContent(context: Context, bundle: Bundle): Intent {
            val intent = Intent(context, CreatePostActivity::class.java)
            intent.putExtras(bundle)
            intent.putExtra(PARAM_TYPE, TYPE_CONTENT_SHOP)
            return intent
        }

        @DeepLink(ApplinkConst.AFFILIATE_DRAFT_POST)
        @JvmStatic
        fun getInstanceDraftAffiliate(context: Context, bundle: Bundle): Intent {
            return getInstanceAffiliate(context, bundle)
        }

        @DeepLink(ApplinkConst.CONTENT_DRAFT_POST)
        @JvmStatic
        fun getInstanceDraftContent(context: Context, bundle: Bundle): Intent {
            return getInstanceContent(context, bundle)
        }
    }

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return when(intent?.extras?.get(PARAM_TYPE)) {
            TYPE_AFFILIATE -> AffiliateCreatePostFragment.createInstance(bundle)
            TYPE_CONTENT_SHOP -> ContentCreatePostFragment.createInstance(bundle)
            else -> {
                finish()
                return null
            }
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_create_post
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        setContentView(layoutRes)
    }
}
