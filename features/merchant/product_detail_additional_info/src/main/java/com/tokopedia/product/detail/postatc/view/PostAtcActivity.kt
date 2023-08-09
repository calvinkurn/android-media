package com.tokopedia.product.detail.postatc.view

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.PostAtcHelper.PARAM_POST_ATC
import com.tokopedia.product.detail.common.postatc.PostAtc
import com.tokopedia.product.detail.common.showImmediately

/**
 * RouteManager.route(context, ApplinkConst.POST_ATC. productId)
 */
class PostAtcActivity : BaseSimpleActivity() {

    companion object {
        private const val PATH_INDEX_PRODUCT_ID = 1
        private const val PARAM_LAYOUT_ID = "layoutID"
        private const val PARAM_CART_ID = "cartID"
        private const val PARAM_PAGE_SOURCE = "pageSource"
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
        val postAtc = generatePostAtc(extras)

        showImmediately(supportFragmentManager, PostAtcBottomSheet.TAG) {
            PostAtcBottomSheet.instance(productId, postAtc)
        }
    }

    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    private fun generatePostAtc(extras: Bundle): PostAtc {
        val postAtc = extras.getParcelable(PARAM_POST_ATC) ?: PostAtc()

        val pageSource = extras.getString(PARAM_PAGE_SOURCE)?.let {
            PostAtc.Source.Custom(it)
        }

        return postAtc.copy(
            layoutId = extras.getString(PARAM_LAYOUT_ID, postAtc.layoutId),
            cartId = extras.getString(PARAM_CART_ID, postAtc.cartId),
            pageSource = pageSource ?: postAtc.pageSource
        )
    }
}
