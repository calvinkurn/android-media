package com.tokopedia.vouchercreation.product.voucherlist.view.activity

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.product.voucherlist.view.fragment.CouponListFragment

class CouponListActivity: BaseSimpleActivity() {

    override fun getLayoutRes() = R.layout.activity_mvc_coupon_list

    override fun getNewFragment() = CouponListFragment()

}