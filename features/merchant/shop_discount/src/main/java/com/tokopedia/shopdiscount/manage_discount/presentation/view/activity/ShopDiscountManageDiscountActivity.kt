package com.tokopedia.shopdiscount.manage_discount.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.manage_discount.presentation.view.fragment.ShopDiscountManageDiscountFragment

class ShopDiscountManageDiscountActivity : BaseSimpleActivity() {

    private var requestId: String = ""
    private var status: Int = -1
    private var mode: String = ""

    companion object {
        private const val REQUEST_ID_PARAM = "REQUEST_ID_PARAM"
        private const val STATUS_PARAM = "STATUS_PARAM"
        private const val MODE_PARAM = "MODE_PARAM"

        @JvmStatic
        fun start(context: Context, requestId : String, discountStatus : Int, mode : String) {
            val starter = Intent(context, ShopDiscountManageDiscountActivity::class.java)
                .putExtra(REQUEST_ID_PARAM, requestId)
                .putExtra(STATUS_PARAM, discountStatus)
                .putExtra(MODE_PARAM, mode)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getIntentData()
        super.onCreate(savedInstanceState)
    }

    private fun getIntentData() {
        requestId = intent.extras?.getString(REQUEST_ID_PARAM).orEmpty()
        status = intent.extras?.getInt(STATUS_PARAM).orZero()
        mode = intent.extras?.getString(MODE_PARAM).orEmpty()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_manage_discount
    }

    override fun getNewFragment(): Fragment = ShopDiscountManageDiscountFragment.createInstance(
        requestId,
        status,
        mode
    )

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

}