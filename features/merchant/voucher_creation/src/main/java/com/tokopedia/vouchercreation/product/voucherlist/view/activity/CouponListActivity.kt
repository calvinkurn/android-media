package com.tokopedia.vouchercreation.product.voucherlist.view.activity

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.product.create.domain.entity.Coupon
import com.tokopedia.vouchercreation.product.create.view.fragment.ProductCouponPreviewFragment
import com.tokopedia.vouchercreation.product.voucherlist.view.fragment.CouponListFragment

class CouponListActivity: BaseSimpleActivity() {

    private val couponListFragment = CouponListFragment()
    override fun getLayoutRes() = R.layout.activity_mvc_coupon_list

    override fun getNewFragment() = couponListFragment

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        couponListFragment.setOnEditCouponMenuSelected { coupon ->
            displayCouponPreviewFragment(coupon)
        }

        return super.onCreateView(name, context, attrs)
    }

    private fun displayCouponPreviewFragment(coupon : Coupon) {
        val couponPreviewFragment = ProductCouponPreviewFragment.newInstance(coupon, ProductCouponPreviewFragment.Mode.UPDATE)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.parent_view, couponPreviewFragment)
            .commitAllowingStateLoss()
    }


}