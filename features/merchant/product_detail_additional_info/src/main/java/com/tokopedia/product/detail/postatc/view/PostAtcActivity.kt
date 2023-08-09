package com.tokopedia.product.detail.postatc.view

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.PostAtcHelper.PARAM_CART_ID
import com.tokopedia.product.detail.common.PostAtcHelper.PARAM_DESELECTED_ADDONS_IDS
import com.tokopedia.product.detail.common.PostAtcHelper.PARAM_IS_FULFILLMENT
import com.tokopedia.product.detail.common.PostAtcHelper.PARAM_LAYOUT_ID
import com.tokopedia.product.detail.common.PostAtcHelper.PARAM_PAGE_SOURCE
import com.tokopedia.product.detail.common.PostAtcHelper.PARAM_QUANTITY
import com.tokopedia.product.detail.common.PostAtcHelper.PARAM_SELECTED_ADDONS_IDS
import com.tokopedia.product.detail.common.PostAtcHelper.PARAM_WAREHOUSE_ID
import com.tokopedia.product.detail.common.showImmediately

/**
 * RouteManager.route(context, ApplinkConst.POST_ATC. productId)
 */
class PostAtcActivity : BaseSimpleActivity() {

    companion object {

        /**
         * Mandatory Parameters
         */
        private const val PATH_INDEX_PRODUCT_ID = 1
    }

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int {
        return R.layout.activity_bottom_sheet_base
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adjustOrientation()

        val pathSegments = intent.data?.pathSegments ?: emptyList()
        val productId = pathSegments.getOrNull(PATH_INDEX_PRODUCT_ID) ?: return

        val extras = intent.extras ?: return
        val cartId = extras.getString(PARAM_CART_ID, "")
        val isFulfillment = extras.getBoolean(PARAM_IS_FULFILLMENT, false)
        val layoutId = extras.getString(PARAM_LAYOUT_ID, "")
        val pageSource = extras.getString(PARAM_PAGE_SOURCE, "")
        val selectedAddonsIds = extras.getStringArrayList(PARAM_SELECTED_ADDONS_IDS) ?: emptyList()
        val deselectedAddonsIds = extras.getStringArrayList(PARAM_DESELECTED_ADDONS_IDS) ?: emptyList()
        val warehouseId = extras.getString(PARAM_WAREHOUSE_ID, "")
        val quantity = extras.getInt(PARAM_QUANTITY, 0)

        showImmediately(supportFragmentManager, PostAtcBottomSheet.TAG) {
            PostAtcBottomSheet.instance(
                productId,
                cartId,
                isFulfillment,
                layoutId,
                pageSource,
                selectedAddonsIds,
                deselectedAddonsIds,
                warehouseId,
                quantity
            )
        }
    }

    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}
