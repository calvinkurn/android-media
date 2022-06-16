package com.tokopedia.vouchercreation.product.detail.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.utils.FragmentRouter
import com.tokopedia.vouchercreation.product.detail.view.fragment.CouponDetailFragment
import javax.inject.Inject

class VoucherProductDetailActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentRouter: FragmentRouter

    companion object {
        private const val COUPON_ID_SEGMENT_INDEX = 1
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
        val couponId = getCouponIdFromAppLink()
        val fragment = CouponDetailFragment.newInstance(couponId.orZero())
        fragmentRouter.replace(supportFragmentManager, R.id.parent_view, fragment)
    }

    private fun getCouponIdFromAppLink(): Long? {
        val appLinkData = RouteManager.getIntent(this, intent.data.toString()).data
        val pathSegments = appLinkData?.pathSegments.orEmpty()
        return pathSegments.getOrNull(COUPON_ID_SEGMENT_INDEX)?.toLong()
    }

}