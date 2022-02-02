package com.tokopedia.vouchercreation.product.voucherlist.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.product.create.domain.entity.Coupon
import com.tokopedia.vouchercreation.product.create.view.activity.CreateCouponProductActivity
import com.tokopedia.vouchercreation.product.create.view.bottomsheet.BroadcastCouponBottomSheet
import com.tokopedia.vouchercreation.product.detail.view.activity.VoucherProductDetailActivity
import com.tokopedia.vouchercreation.product.duplicate.DuplicateCouponActivity
import com.tokopedia.vouchercreation.product.update.UpdateCouponActivity
import com.tokopedia.vouchercreation.product.voucherlist.view.fragment.CouponListFragment
import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherCreationStep
import javax.inject.Inject

class CouponListActivity: BaseSimpleActivity() {

    @Inject
    lateinit var userSession: UserSessionInterface

    companion object {
         const val BUNDLE_KEY_COUPON = "coupon"
        @JvmStatic
        fun start(context: Context, coupon: Coupon?) {
            val starter = Intent(context, CouponListActivity::class.java).apply {
                putExtra(BUNDLE_KEY_COUPON, coupon)
            }
            context.startActivity(starter)
        }
    }

    override fun getLayoutRes() = R.layout.activity_mvc_coupon_list
    override fun getNewFragment() = couponListFragment

    //private val coupon by lazy { intent.extras?.getSerializable(BUNDLE_KEY_COUPON) as? Coupon }

    private val couponListFragment = CouponListFragment.newInstance(
        ::navigateToCreateCouponPage,
        ::navigateToEditCouponPage,
        ::navigateToDuplicateCouponPage,
        ::navigateToCouponDetail
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        //displayCreateCouponSuccess()
    }

    private fun setupDependencyInjection() {
        DaggerVoucherCreationComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    /*private fun displayCreateCouponSuccess() {
        if (coupon != null) {
            showBroadCastVoucherBottomSheet(coupon ?: return)
        }
    }*/

    private fun navigateToCreateCouponPage() {
        startActivity(Intent(this, CreateCouponProductActivity::class.java))
    }

    private fun navigateToEditCouponPage(coupon: Coupon) {
        UpdateCouponActivity.start(this, coupon)
    }

    private fun navigateToDuplicateCouponPage(coupon: Coupon) {
        showToaster(getString(R.string.coupon_duplicated))
        DuplicateCouponActivity.start(this, coupon)
    }


    private fun navigateToCouponDetail(couponId: Long) {
        VoucherProductDetailActivity.start(this, couponId)
    }

    private fun showBroadCastVoucherBottomSheet(coupon: Coupon) {
        val bottomSheet = BroadcastCouponBottomSheet.newInstance(coupon.id, coupon.information)
        bottomSheet.setCloseClickListener {
            VoucherCreationTracking.sendCreateVoucherClickTracking(
                step = VoucherCreationStep.REVIEW,
                action = VoucherCreationAnalyticConstant.EventAction.Click.VOUCHER_SUCCESS_CLICK_BACK_BUTTON,
                userId = userSession.userId
            )
            bottomSheet.dismiss()
        }
        bottomSheet.show(supportFragmentManager)
    }

    private fun showToaster(text: String) {
        if (text.isEmpty()) return
        val view = findViewById<FrameLayout>(R.id.parent_view)
        Toaster.build(view, text).show()
    }

}