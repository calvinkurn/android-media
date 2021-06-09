package com.tokopedia.createpost.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.createpost.*
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.view.fragment.AffiliateCreatePostFragment
import com.tokopedia.createpost.view.fragment.BaseCreatePostFragment
import com.tokopedia.createpost.view.fragment.ContentCreatePostFragment
import com.tokopedia.createpost.view.listener.CreatePostActivityListener
import com.tokopedia.createpost.view.viewmodel.HeaderViewModel
import com.tokopedia.design.component.Dialog
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.kotlin.extensions.view.showWithCondition
import kotlinx.android.synthetic.main.activity_create_post.*


const val PARAM_PRODUCT_ID = "product_id"
const val PARAM_AD_ID = "ad_id"
const val PARAM_POST_ID = "post_id"
const val PARAM_TYPE = "author_type"

class CreatePostActivity : BaseSimpleActivity(), CreatePostActivityListener {

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent
        inflateFragment()
    }

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        intent.data?.let { uri ->
            val segmentUri = uri.pathSegments
            if (uri.lastPathSegment == TYPE_EDIT && segmentUri.size == 3) {
                intent.putExtra(PARAM_POST_ID, segmentUri[segmentUri.size - 2])
                intent.putExtra(PARAM_TYPE, segmentUri[0])
            }

            if (segmentUri[0] == TYPE_CREATE_POST && segmentUri.size == 3) {
                intent.putExtra(PARAM_PRODUCT_ID, segmentUri[1])
                intent.putExtra(PARAM_AD_ID, segmentUri[2])
                uri.getQueryParameter(TOKEN)?.let {
                    intent.putExtra(TOKEN, uri.getQueryParameter(TOKEN))
                }
            }
            if (segmentUri[0] == TYPE_DRAFT) {
                intent.putExtra(DRAFT_ID, uri.lastPathSegment)
            }

            if (intent.getStringExtra(PARAM_TYPE) == null) {
                if (uri.host?.contains(TYPE_CONTENT, false) == true) {
                    intent.putExtra(PARAM_TYPE, TYPE_CONTENT_SHOP)
                } else {
                    intent.putExtra(PARAM_TYPE, TYPE_AFFILIATE)
                }
            }

            if (intent.extras != null) {
                bundle.putAll(intent.extras)
            }
        }

        return when (intent.extras?.get(PARAM_TYPE)) {
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

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        setContentView(layoutRes)
        backBtn.setOnClickListener {
            onBackPressed()
        }
        action_post.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentByTag("TAG_FRAGMENT") as? BaseCreatePostFragment
                    ?: return@setOnClickListener
            fragment.saveDraftAndSubmit()
        }
        shareTo.apply {
            setCompoundDrawablesWithIntrinsicBounds(null, null, MethodChecker.getDrawable(this@CreatePostActivity, R.drawable.ic_dropdown_spinner_gray), null)
            setOnClickListener { openShareBottomSheetDialog() }
        }
    }


    override fun updateHeader(header: HeaderViewModel) {
        avatar.loadImageCircle(header.avatar)
        avatar.showWithCondition(header.avatar.isNotBlank())
        badge.loadImage(header.badge)
        badge.showWithCondition(header.badge.isNotBlank())
        name.text = header.title
    }

    override fun updateShareHeader(text: String) {
        shareTo.text = text
    }

    override fun invalidatePostMenu(isPostEnabled: Boolean) {
        if (isPostEnabled) {
            action_post.setTextColor(ContextCompat.getColor(this, com.tokopedia.design.R.color.green_500))
        } else {
            action_post.setTextColor(ContextCompat.getColor(this, com.tokopedia.design.R.color.grey_500))
        }
    }

    override fun onBackPressed() {
        val dialog = Dialog(this, Dialog.Type.PROMINANCE)
        dialog.setTitle(getString(R.string.cp_leave_warning))
        dialog.setDesc(getString(R.string.cp_leave_warning_desc))
        dialog.setBtnOk(getString(R.string.cp_leave_title))
        dialog.setBtnCancel(getString(com.tokopedia.imagepicker.common.R.string.ip_continue))
        dialog.setOnOkClickListener {
            KeyboardHandler.hideSoftKeyboard(this)
            (fragment as? AffiliateCreatePostFragment)?.clearCache()
            dialog.dismiss()
            finish()
        }
        dialog.setOnCancelClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(true)
        dialog.show()
    }

    private fun openShareBottomSheetDialog() {
        (fragment as? BaseCreatePostFragment)?.openShareBottomSheetDialog()
    }
}
