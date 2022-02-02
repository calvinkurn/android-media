package com.tokopedia.vouchercreation.product.detail.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.product.create.view.fragment.CouponDetailFragment

class VoucherProductDetailActivity: AppCompatActivity() {

    companion object {
        private const val COUPON_ID = "couponId"
        @JvmStatic
        fun start(context: Context, couponId : Long) {
            val starter = Intent(context, VoucherProductDetailActivity::class.java)
                .putExtra(COUPON_ID, couponId)
            context.startActivity(starter)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvc_voucher_product_detail)
        displayCouponDetail()
    }

    private fun displayCouponDetail() {
        val couponId = intent.extras?.getLong(COUPON_ID, 0).orZero()
        val fragment = CouponDetailFragment.newInstance(couponId)
        replace(fragment)
    }

    private fun replace(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.parent_view, fragment)
            .commit()
    }
}