package com.tokopedia.product.addedit.preview.presentation.activity

import android.content.Context
import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.BUNDLE_DRAFT_ID
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.BUNDLE_IS_PRODUCT_DUPLICATE
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.BUNDLE_PRODUCT_ID
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_DRAFT_ID
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_FROM_NOTIF_EDIT_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_FROM_NOTIF_SUCCESS
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_FROM_UPLOADING
import com.tokopedia.product.addedit.tracking.ProductAddNotifTracking
import com.tokopedia.product.addedit.tracking.ProductEditNotifTracking
import com.tokopedia.user.session.UserSession


class AddEditProductPreviewActivity : BaseSimpleActivity() {

    companion object {
        fun createInstance(context: Context?, draftId: String? = null): Intent {
            val intent = Intent(context, AddEditProductPreviewActivity::class.java)
            draftId?.let {
                intent.putExtra(EXTRA_DRAFT_ID, draftId)
            }
            return intent
        }

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

    override fun getNewFragment(): Fragment? = null

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

        updateActivityToolbar()
        setupNavController()
    }

    private fun setupNavController() {
        // passing data into fragment
        val bundle = Bundle().apply {
            putString(BUNDLE_PRODUCT_ID, productId)
            putString(BUNDLE_DRAFT_ID, draftId)
            putBoolean(BUNDLE_IS_PRODUCT_DUPLICATE, isDuplicate)
        }

        val navController = findNavController(com.tokopedia.product.addedit.R.id.parent_view)
        val listener = AppBarConfiguration.OnNavigateUpListener {
            navController.navigateUp()
        }

        val appBarConfiguration = AppBarConfiguration.Builder().setFallbackOnNavigateUpListener(listener).build()
        navController.setGraph(com.tokopedia.product.addedit.R.navigation.product_add_edit_navigation, bundle)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, _, _ ->
            updateActivityToolbar()
        }
    }

    private fun updateActivityToolbar() {
        findViewById<androidx.appcompat.widget.Toolbar>(com.tokopedia.product.addedit.R.id.toolbar)?.let {
            setSupportActionBar(it)
            // set to dark mode color support
            val color = androidx.core.content.ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N700)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                it.navigationIcon?.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_IN)
            }else{
                it.navigationIcon?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            }
        }
    }
}
