package com.tokopedia.product.addedit.preview.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_DRAFT_ID
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_FROM_NOTIF_EDIT_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_FROM_NOTIF_SUCCESS
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_ID
import com.tokopedia.product.addedit.preview.presentation.fragment.AddEditProductPreviewFragment
import com.tokopedia.product.addedit.tracking.ProductAddNotifTracking
import com.tokopedia.product.addedit.tracking.ProductEditNotifTracking
import com.tokopedia.user.session.UserSession


class AddEditProductPreviewActivity : BaseSimpleActivity() {

    private var productId = ""
    private var draftId = ""

    override fun getNewFragment(): Fragment? {
        return AddEditProductPreviewFragment.createInstance("773109803", draftId)
    }

    override fun getLayoutRes() = R.layout.activity_add_edit_product_preview

    override fun onCreate(savedInstanceState: Bundle?) {

        intent.getStringExtra(EXTRA_PRODUCT_ID)?.run { productId = this }
        intent.getStringExtra(EXTRA_DRAFT_ID)?.run { draftId = this }

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

    override fun onBackPressed() {
        super.onBackPressed()
        val f = fragment
        if (f != null && f is AddEditProductPreviewFragment) {
            f.onBackPressed()
        }
    }
}
