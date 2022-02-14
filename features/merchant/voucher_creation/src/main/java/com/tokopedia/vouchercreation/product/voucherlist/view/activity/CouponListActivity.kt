package com.tokopedia.vouchercreation.product.voucherlist.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
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
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.PAGE_MODE_ACTIVE
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.PAGE_MODE_SEGMENT_INDEX
import com.tokopedia.vouchercreation.product.voucherlist.view.fragment.CouponListFragment
import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherCreationStep
import javax.inject.Inject

class CouponListActivity : BaseSimpleActivity() {

    companion object {
        private const val INTENT_KEY_COUPON = "coupon"
        @JvmStatic
        fun start(context: Context, coupon: Coupon) {
            val bundle = Bundle()
            bundle.putParcelable(INTENT_KEY_COUPON, coupon)
            val starter = Intent(context, CouponListActivity::class.java)
            starter.putExtras(bundle)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var userSession: UserSessionInterface
    private val coupon by lazy { intent.extras?.getParcelable<Coupon>(INTENT_KEY_COUPON) }

    override fun getLayoutRes() = R.layout.activity_mvc_coupon_list

    override fun getNewFragment() = CouponListFragment.newInstance(
        getPageModeDataFromApplink(),
        ::navigateToCreateCouponPage,
        ::navigateToUpdateCouponPage,
        ::navigateToDuplicateCouponPage,
        ::navigateToCouponDetail
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        if (coupon != null) {
            showBroadCastVoucherBottomSheet(coupon ?: return)
        }
    }

    private fun getPageModeDataFromApplink(): String {
        val applinkData = RouteManager.getIntent(this, intent.data.toString()).data
        val pathSegments = applinkData?.pathSegments.orEmpty()
        return pathSegments.getOrNull(PAGE_MODE_SEGMENT_INDEX) ?: PAGE_MODE_ACTIVE
    }

    private fun setupDependencyInjection() {
        DaggerVoucherCreationComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun navigateToCreateCouponPage() {
        val intent = Intent(this, CreateCouponProductActivity::class.java)
        startActivityForResult(intent, CreateCouponProductActivity.REQUEST_CODE_CREATE_COUPON)
    }

    private fun navigateToUpdateCouponPage(couponId: Long) {
        val intent = Intent(this, UpdateCouponActivity::class.java).apply {
            putExtra(UpdateCouponActivity.BUNDLE_KEY_COUPON_ID, couponId)
        }
        startActivityForResult(intent, UpdateCouponActivity.REQUEST_CODE_UPDATE_COUPON)
    }

    private fun navigateToDuplicateCouponPage(couponId: Long) {
        DuplicateCouponActivity.start(this, couponId)
    }

    private fun navigateToCouponDetail(couponId: Long) {
        VoucherProductDetailActivity.start(this, couponId)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            CreateCouponProductActivity.REQUEST_CODE_CREATE_COUPON ->{
                handleCreateCouponResult(resultCode, data)
            }
            UpdateCouponActivity.REQUEST_CODE_UPDATE_COUPON -> {
                handleUpdateCouponResult(resultCode)
            }
        }
    }

    private fun handleCreateCouponResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val coupon = data?.getParcelableExtra<Coupon>(CreateCouponProductActivity.BUNDLE_KEY_COUPON)
                ?: return
            showBroadCastVoucherBottomSheet(coupon)
            newFragment.loadInitialData()
        }
    }

    private fun handleUpdateCouponResult(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK) {
            showToaster(getString(R.string.coupon_updated))
            newFragment.loadInitialData()
        }
    }

    private fun showBroadCastVoucherBottomSheet(coupon: Coupon) {
        val bottomSheet = BroadcastCouponBottomSheet.newInstance(coupon.id, coupon)
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