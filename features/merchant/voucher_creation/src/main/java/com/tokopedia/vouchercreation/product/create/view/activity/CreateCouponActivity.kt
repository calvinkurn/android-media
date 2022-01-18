package com.tokopedia.vouchercreation.product.create.view.activity

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.product.create.view.fragment.CreateCouponDetailFragment

class CreateCouponActivity : BaseSimpleActivity() {

    override fun getLayoutRes() = R.layout.activity_mvc_create_coupon

    override fun getParentViewResourceID() = R.id.parent_view

    override fun getNewFragment() = CreateCouponDetailFragment()
}