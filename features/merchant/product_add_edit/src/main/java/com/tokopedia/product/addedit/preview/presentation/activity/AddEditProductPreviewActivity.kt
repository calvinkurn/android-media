package com.tokopedia.product.addedit.preview.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_DRAFT_ID
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_FROM_NOTIF_EDIT_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_FROM_NOTIF_SUCCESS
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_DUPLICATE
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_ID
import com.tokopedia.product.addedit.preview.presentation.fragment.AddEditProductPreviewFragment
import com.tokopedia.product.addedit.tracking.ProductAddNotifTracking
import com.tokopedia.product.addedit.tracking.ProductEditNotifTracking
import com.tokopedia.user.session.UserSession


class AddEditProductPreviewActivity : BaseSimpleActivity() {

    companion object {
        fun createInstance(context: Context?): Intent = Intent(context,
                AddEditProductPreviewActivity::class.java)

        fun createInstance(context: Context?, isFromSuccessNotif: Boolean?,
                           isFromNotifEditMode: Boolean?): Intent {
            val intent = Intent(context,
                    AddEditProductPreviewActivity::class.java)
            isFromSuccessNotif?.run {
                intent.apply {
                    putExtra(EXTRA_FROM_NOTIF_SUCCESS, isFromSuccessNotif)
                    putExtra(EXTRA_FROM_NOTIF_EDIT_PRODUCT, isFromNotifEditMode)
                }
            }
            return intent
        }
    }

    private var productId = ""
    private var draftId = ""
    private var isDuplicate = false

    override fun getNewFragment(): Fragment? {
        return AddEditProductPreviewFragment.createInstance(productId, draftId, isDuplicate)
    }

    override fun getLayoutRes() = R.layout.activity_add_edit_product_preview

    override fun onCreate(savedInstanceState: Bundle?) {
        with (intent) {
            getStringExtra(EXTRA_PRODUCT_ID)?.run { productId = this }
            getStringExtra(EXTRA_DRAFT_ID)?.run { draftId = this }
            isDuplicate = getBooleanExtra(EXTRA_IS_DUPLICATE, false)
        }

        if (intent.hasExtra(EXTRA_FROM_NOTIF_SUCCESS) &&
                intent.hasExtra(EXTRA_FROM_NOTIF_EDIT_PRODUCT) &&
                intent.getBooleanExtra(EXTRA_FROM_NOTIF_SUCCESS, false)) {
            val isEdit = intent.getBooleanExtra(EXTRA_FROM_NOTIF_EDIT_PRODUCT, false)
            if (isEdit) {
                ProductEditNotifTracking.clickFailed(UserSession(this).shopId)
            } else {
                ProductAddNotifTracking.clickFailed(UserSession(this).shopId)
            }
        }

        super.onCreate(savedInstanceState)
    }

    override fun onBackPressed() {
        onBackPressedHitTracking()
        DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(R.string.label_title_on_dialog))
            setDescription(getString(R.string.label_description_on_dialog))
            setPrimaryCTAText(getString(R.string.label_cta_primary_button_on_dialog))
            setSecondaryCTAText(getString(R.string.label_cta_secondary_button_on_dialog))
            setSecondaryCTAClickListener {
                moveToManageProduct()
                onCtaYesPressedHitTracking()
            }
            setPrimaryCTAClickListener {
                super.onBackPressed()
                onCtaNoPressedHitTracking()
            }
        }.show()
    }

    private fun moveToManageProduct() {
        val intent = RouteManager.getIntent(this, ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST)
        startActivity(intent)
        finish()
    }

    private fun onCtaYesPressedHitTracking() {
        val f = fragment
        if (f != null && f is AddEditProductPreviewFragment) {
            f.onCtaYesPressed()
        }
    }

    private fun onCtaNoPressedHitTracking() {
        val f = fragment
        if (f != null && f is AddEditProductPreviewFragment) {
            f.onCtaNoPressed()
        }
    }

    private fun onBackPressedHitTracking() {
        val f = fragment
        if (f!= null && f is AddEditProductPreviewFragment) {
            f.onBackPressed()
        }
    }
}
