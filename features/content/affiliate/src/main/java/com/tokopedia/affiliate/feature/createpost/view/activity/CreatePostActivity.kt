package com.tokopedia.affiliate.feature.createpost.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.createpost.TYPE_AFFILIATE
import com.tokopedia.affiliate.feature.createpost.TYPE_CONTENT_SHOP
import com.tokopedia.affiliate.feature.createpost.view.fragment.AffiliateCreatePostFragment
import com.tokopedia.affiliate.feature.createpost.view.fragment.BaseCreatePostFragment
import com.tokopedia.affiliate.feature.createpost.view.fragment.ContentCreatePostFragment
import com.tokopedia.affiliate.feature.createpost.view.listener.CreatePostActivityListener
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.HeaderViewModel
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.design.component.Dialog
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.kotlin.extensions.view.showWithCondition
import kotlinx.android.synthetic.main.activity_create_post.*

class CreatePostActivity : BaseSimpleActivity(), CreatePostActivityListener, BaseCreatePostFragment.OnCreatePostCallBack {
    private var postId: String? = null

    override fun invalidatePostMenu(isPostEnabled: Boolean) {
        if (isPostEnabled){
            action_post.setTextColor(ContextCompat.getColor(this, R.color.green_500))
        } else {
            action_post.setTextColor(ContextCompat.getColor(this, R.color.grey_500))
        }
    }

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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent
        inflateFragment()
    }

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        val uri = intent.data
        if (uri != null && uri.scheme == DeeplinkConstant.SCHEME_INTERNAL){
            val segmentUri = uri.pathSegments
            intent.putExtra(PARAM_POST_ID, segmentUri[segmentUri.size - 2])
            intent.putExtra(PARAM_TYPE, segmentUri[0])
        }

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
        backBtn.setOnClickListener {
            onBackPressed()
        }
        action_post.setOnClickListener {
            val fragment = supportFragmentManager
                    .findFragmentByTag("TAG_FRAGMENT") as? BaseCreatePostFragment ?:
            return@setOnClickListener
            fragment.saveDraftAndSubmit()
        }
    }

    override fun updateHeader(header: HeaderViewModel) {
        avatar.loadImageCircle(header.avatar)
        avatar.showWithCondition(header.avatar.isNotBlank())
        badge.loadImage(header.badge)
        badge.showWithCondition(header.badge.isNotBlank())
        name.text = header.title
    }

    override fun onBackPressed() {
        val dialog = Dialog(this, Dialog.Type.PROMINANCE)
        dialog.setTitle(getString(R.string.af_leave_warning))
        dialog.setDesc(getString(R.string.af_leave_warning_desc))
        dialog.setBtnOk(getString(R.string.af_leave_title))
        dialog.setBtnCancel(getString(R.string.af_continue))
        dialog.setOnOkClickListener{
            (supportFragmentManager.findFragmentByTag("TAG_FRAGMENT") as? AffiliateCreatePostFragment)?.let {
                it.clearCache()
            }
            dialog.dismiss()
            super.onBackPressed()
        }
        dialog.setOnCancelClickListener{
            dialog.dismiss()
        }
        dialog.setCancelable(true)
        dialog.show()
    }
}
