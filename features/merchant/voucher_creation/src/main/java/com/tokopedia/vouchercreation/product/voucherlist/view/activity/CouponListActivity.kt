package com.tokopedia.vouchercreation.product.voucherlist.view.activity

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.product.voucherlist.view.fragment.CouponListFragment

class CouponListActivity: BaseSimpleActivity() {

    override fun getLayoutRes() = R.layout.activity_mvc_coupon_list

    override fun getNewFragment() = CouponListFragment()

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }


}