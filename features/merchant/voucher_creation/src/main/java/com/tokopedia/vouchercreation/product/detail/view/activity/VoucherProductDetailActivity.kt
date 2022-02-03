package com.tokopedia.vouchercreation.product.detail.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.utils.FragmentRouter
import com.tokopedia.vouchercreation.product.create.view.fragment.CouponDetailFragment
import javax.inject.Inject

class VoucherProductDetailActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentRouter: FragmentRouter

    companion object {
        private const val ZERO: Long = 0
        private const val COUPON_ID = "couponId"

        @JvmStatic
        fun start(context: Context, couponId: Long) {
            val starter = Intent(context, VoucherProductDetailActivity::class.java)
                .putExtra(COUPON_ID, couponId)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        setContentView(R.layout.activity_mvc_coupon_list)
        displayCouponDetail()
    }

    private fun setupDependencyInjection() {
        DaggerVoucherCreationComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun displayCouponDetail() {
        val couponId = intent.extras?.getLong(COUPON_ID, ZERO).orZero()
        val fragment = CouponDetailFragment.newInstance(couponId)
        fragmentRouter.replace(supportFragmentManager, R.id.parent_view, fragment)
    }


}