package com.tokopedia.shopdiscount.manage_discount.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.manage_discount.presentation.view.fragment.ShopDiscountManageFragment

class ShopDiscountManageActivity : BaseSimpleActivity() {

    private var requestId: String = ""
    private var status: Int = -1
    private var mode: String = ""
    private var selectedProductVariantId: String = ""

    companion object {
        const val REQUEST_ID_PARAM = "REQUEST_ID_PARAM"
        const val STATUS_PARAM = "STATUS_PARAM"
        const val MODE_PARAM = "MODE_PARAM"
        const val SELECTED_PRODUCT_VARIANT_ID_PARAM = "SELECTED_PRODUCT_VARIANT_ID_PARAM"

        @JvmStatic
        fun start(context: Context, requestId : String, discountStatus : Int, mode : String) {
            val starter = Intent(context, ShopDiscountManageActivity::class.java)
                .putExtra(REQUEST_ID_PARAM, requestId)
                .putExtra(STATUS_PARAM, discountStatus)
                .putExtra(MODE_PARAM, mode)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getIntentData()
        super.onCreate(savedInstanceState)
        window?.decorView?.setBackgroundColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Background))
    }

    override fun onBackPressed() {
        (fragment as? ShopDiscountManageFragment)?.onBackPressed()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_manage_discount
    }

    override fun getNewFragment(): Fragment = ShopDiscountManageFragment.createInstance(
        requestId,
        status,
        mode,
        selectedProductVariantId
    )

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    private fun getIntentData() {
        intent.extras?.let{
            requestId = it.getString(REQUEST_ID_PARAM).orEmpty()
            status = it.getInt(STATUS_PARAM).orZero()
            mode = it.getString(MODE_PARAM).orEmpty()
            selectedProductVariantId = it.getString(SELECTED_PRODUCT_VARIANT_ID_PARAM).orEmpty()
        }
    }
}