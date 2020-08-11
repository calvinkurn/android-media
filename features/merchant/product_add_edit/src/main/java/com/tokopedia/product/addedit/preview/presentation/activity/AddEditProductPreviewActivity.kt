package com.tokopedia.product.addedit.preview.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_DRAFT_ID
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_FROM_NOTIF_EDIT_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_FROM_NOTIF_SUCCESS
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_FROM_UPLOADING
import com.tokopedia.product.addedit.preview.presentation.fragment.AddEditProductPreviewFragment
import com.tokopedia.product.addedit.tracking.ProductAddNotifTracking
import com.tokopedia.product.addedit.tracking.ProductEditNotifTracking
import com.tokopedia.user.session.UserSession


class AddEditProductPreviewActivity : BaseSimpleActivity() {

    companion object {
        fun createInstance(context: Context?): Intent = Intent(context,
                AddEditProductPreviewActivity::class.java)

        fun createInstance(context: Context?, draftId: String, isFromSuccessNotif: Boolean?,
                           isFromNotifEditMode: Boolean?): Intent {
            val intent = Intent(context, AddEditProductPreviewActivity::class.java)
            isFromSuccessNotif?.run {
                intent.apply {
                    putExtra(EXTRA_DRAFT_ID, draftId)
                    putExtra(EXTRA_FROM_NOTIF_SUCCESS, isFromSuccessNotif)
                    putExtra(EXTRA_FROM_NOTIF_EDIT_PRODUCT, isFromNotifEditMode)
                    putExtra(EXTRA_FROM_UPLOADING, true)
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

    override fun getLayoutRes() = com.tokopedia.product.addedit.R.layout.activity_add_edit_product_preview

    override fun getParentViewResourceID(): Int = com.tokopedia.product.addedit.R.id.parent_view

    override fun onCreate(savedInstanceState: Bundle?) {
        // get draftId from failed notif
        draftId = intent.getStringExtra(EXTRA_DRAFT_ID) ?: ""
        // get data from applink
        intent.data?.run {
            val uri = toString()
            val params = UriUtil.uriQueryParamsToMap(uri)
            if (params.isNotEmpty()) {
                val mode = params[ApplinkConstInternalMechant.QUERY_PARAM_MODE].orEmpty()
                val id = params[ApplinkConstInternalMechant.QUERY_PARAM_ID].orEmpty()
                when (mode) {
                    ApplinkConstInternalMechant.MODE_EDIT_PRODUCT -> productId = id
                    ApplinkConstInternalMechant.MODE_EDIT_DRAFT -> draftId = id
                    ApplinkConstInternalMechant.MODE_DUPLICATE_PRODUCT -> {
                        productId = id
                        isDuplicate = true
                    }
                }
            }
        }
        val fromUpload = intent.getBooleanExtra(EXTRA_FROM_UPLOADING, false)
        if(fromUpload) {
            val isNotifSuccess = intent.getBooleanExtra(EXTRA_FROM_NOTIF_SUCCESS, false)
            val isNotifEditProduct = intent.getBooleanExtra(EXTRA_FROM_NOTIF_EDIT_PRODUCT, false)
            if (!isNotifSuccess && !isNotifEditProduct) {
                ProductAddNotifTracking.clickFailed(UserSession(this).shopId)
            } else if (!isNotifSuccess && isNotifEditProduct) {
                ProductEditNotifTracking.clickFailed(UserSession(this).shopId)
            }
        }
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
        }
    }

    override fun onBackPressed() {
        onBackPressedHitTracking()
        DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(com.tokopedia.product.addedit.R.string.label_title_on_dialog))
            setPrimaryCTAText(getString(R.string.label_cta_primary_button_on_dialog))
            setSecondaryCTAText(getString(R.string.label_cta_secondary_button_on_dialog))
            if((isEditing()  || dataBackPressedLoss()) && !isDrafting()) {
                setDescription(getString(R.string.label_description_on_dialog_edit))
                setSecondaryCTAClickListener {
                    setResult(Activity.RESULT_CANCELED)
                    super.onBackPressed()
                }
                setPrimaryCTAClickListener {
                    this.dismiss()
                }
            } else {
                setDescription(getString(R.string.label_description_on_dialog))
                setSecondaryCTAClickListener {
                    setResult(Activity.RESULT_CANCELED)
                    saveProductToDraft()
                    moveToManageProduct()
                    onCtaYesPressedHitTracking()
                }
                setPrimaryCTAClickListener {
                    this.dismiss()
                    onCtaNoPressedHitTracking()
                }
            }
        }.show()
    }

    private fun moveToManageProduct() {
        val intent = RouteManager.getIntent(this, ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST)
        startActivity(intent)
        finish()
    }

    private fun isEditing(): Boolean {
        val f = fragment
        if (f != null && f is AddEditProductPreviewFragment) {
            return f.isEditing()
        }
        return false
    }

    private fun isDrafting(): Boolean {
        val f = fragment
        if (f != null && f is AddEditProductPreviewFragment) {
            return f.isDrafting()
        }
        return false
    }

    private fun saveProductToDraft() {
        val f = fragment
        if (f != null && f is AddEditProductPreviewFragment) {
            f.saveProductDraft()
        }
    }

    private fun dataBackPressedLoss(): Boolean {
        val f = fragment
        if (f != null && f is AddEditProductPreviewFragment) {
            return f.dataBackPressedLoss()
        }
        return false
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
        if (f != null && f is AddEditProductPreviewFragment) {
            f.onBackPressed()
        }
    }
}
