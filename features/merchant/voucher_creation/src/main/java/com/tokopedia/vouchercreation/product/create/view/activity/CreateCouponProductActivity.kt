package com.tokopedia.vouchercreation.product.create.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.product.create.domain.entity.Coupon
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponProduct
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.view.bottomsheet.BroadcastCouponBottomSheet
import com.tokopedia.vouchercreation.product.create.view.fragment.CouponDetailFragment
import com.tokopedia.vouchercreation.product.create.view.fragment.CouponSettingFragment
import com.tokopedia.vouchercreation.product.create.view.fragment.CreateCouponDetailFragment
import com.tokopedia.vouchercreation.product.create.view.fragment.ProductCouponPreviewFragment
import com.tokopedia.vouchercreation.product.voucherlist.view.fragment.CouponListFragment
import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherCreationStep
import java.util.*
import javax.inject.Inject

class CreateCouponProductActivity : AppCompatActivity() {

    @Inject
    lateinit var userSession: UserSessionInterface

    private val couponPreviewFragment = ProductCouponPreviewFragment.newInstance(
        ::navigateToCouponInformationPage,
        ::navigateToCouponSettingPage,
        ::navigateToProductListPage,
        ::onCreateCouponSuccess,
        ::onUpdateCouponSuccess,
        null,
        ProductCouponPreviewFragment.Mode.CREATE
    )

    private val couponSettingFragment = CouponSettingFragment.newInstance(::saveCouponSettingsData)
    private val couponListFragment = CouponListFragment.newInstance(
        ::navigateToCreateCouponPage,
        ::navigateToEditCouponPage,
        ::navigateToDuplicateCouponPage,
        ::navigateToCouponDetail
    )

    private var couponSettings : CouponSettings? = null
    private val productId: String? by lazy { getProductIdDataFromApplink() }

    companion object {
        private const val TAG_FRAGMENT_COUPON_INFORMATION = "coupon_information"
        private const val TAG_FRAGMENT_COUPON_SETTINGS = "coupon_settings"
        private const val TAG_FRAGMENT_COUPON_PREVIEW = "coupon_preview"
        private const val TAG_FRAGMENT_PRODUCT_LIST = "product_list"
        private const val PRODUCT_ID_SEGMENT_INDEX = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        setContentView(R.layout.activity_mvc_create_coupon)
        replace(couponListFragment)
        println(productId)
    }

    private fun setupDependencyInjection() {
        DaggerVoucherCreationComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun getProductIdDataFromApplink(): String? {
        val applinkData = RouteManager.getIntent(this, intent.data.toString()).data
        val pathSegments = applinkData?.pathSegments.orEmpty()
        return pathSegments.getOrNull(PRODUCT_ID_SEGMENT_INDEX)
    }

    private fun navigateToCouponInformationPage() {
        replaceAndAddToBackstack(setupCreateCouponDetailFragment(), TAG_FRAGMENT_COUPON_INFORMATION)
    }

    private fun navigateToCouponSettingPage() {
        couponSettingFragment.setCouponSettings(couponSettings)
        replaceAndAddToBackstack(couponSettingFragment, TAG_FRAGMENT_COUPON_SETTINGS)
    }

    private fun navigateToProductListPage() {
        //TODO : @Deyo Navigate to product list fragment
    }

    private fun navigateToCreateCouponPage() {
        replaceAndAddToBackstack(couponPreviewFragment, TAG_FRAGMENT_COUPON_PREVIEW)
    }

    private fun navigateToEditCouponPage(coupon : Coupon) {
        val fragment = ProductCouponPreviewFragment.newInstance(
            ::navigateToCouponInformationPage,
            ::navigateToCouponSettingPage,
            ::navigateToProductListPage,
            ::onCreateCouponSuccess,
            ::onUpdateCouponSuccess,
            coupon,
            ProductCouponPreviewFragment.Mode.UPDATE
        )
        replaceAndAddToBackstack(fragment, TAG_FRAGMENT_COUPON_PREVIEW)
    }

    private fun navigateToDuplicateCouponPage(coupon : Coupon) {
        val fragment = ProductCouponPreviewFragment.newInstance(
            ::navigateToCouponInformationPage,
            ::navigateToCouponSettingPage,
            ::navigateToProductListPage,
            ::onCreateCouponSuccess,
            ::onUpdateCouponSuccess,
            coupon,
            ProductCouponPreviewFragment.Mode.DUPLICATE
        )
        replaceAndAddToBackstack(fragment, TAG_FRAGMENT_COUPON_PREVIEW)
    }

    private fun navigateToCouponDetail(couponId : Long) {
        val fragment = CouponDetailFragment.newInstance(couponId)
        replaceAndAddToBackstack(fragment, TAG_FRAGMENT_COUPON_PREVIEW)
    }

    private fun onCreateCouponSuccess(coupon: Coupon) {
        replace(couponListFragment)
        showBroadCastVoucherBottomSheet(coupon)
    }

    private fun onUpdateCouponSuccess() {
        popFragment()
    }

    private fun saveCouponSettingsData(couponSettings: CouponSettings) {
        this.couponSettings = couponSettings
        couponPreviewFragment.setCouponSettingsData(couponSettings)

        //Stub the coupon preview data for testing purpose
        val startDate = Calendar.getInstance().apply { set(2022, 1, 1, 20, 30, 0) }
        val endDate = Calendar.getInstance().apply {  set(2022, 1, 5, 23, 59, 0) }
        val period = CouponInformation.Period(startDate.time, endDate.time)
        couponPreviewFragment.setCouponInformationData(
            CouponInformation(
                CouponInformation.Target.PUBLIC,
                "Kupon Kopi Kenangan",
                "KOPKEN",
                period
            )
        )

        //Stub the products data for testing purpose
        couponPreviewFragment.setCouponProductsData(
            listOf(
                CouponProduct(
                    "2147956088",
                    18000,
                    5.0F,
                    "https://images.tokopedia.net/img/VqbcmM/2021/4/15/16087191-6556-40b5-9150-36944b73f85e.jpg",
                    19
                ),
                CouponProduct(
                    "15455652",
                    18000,
                    4.7F,
                    "https://images.tokopedia.net/img/VqbcmM/2021/4/15/16087191-6556-40b5-9150-36944b73f85e.jpg",
                    1000
                ),
                CouponProduct(
                    "15429644",
                    18000,
                    5.0F,
                    "https://images.tokopedia.net/img/VqbcmM/2021/4/15/16087191-6556-40b5-9150-36944b73f85e.jpg",
                    2100
                ),
                CouponProduct(
                    "15409031",
                    25000,
                    4.0F,
                    "https://images.tokopedia.net/img/VqbcmM/2021/4/15/16087191-6556-40b5-9150-36944b73f85e.jpg",
                    31000
                )
            )
        )
        popFragment()
    }

    private fun setupCreateCouponDetailFragment(): CreateCouponDetailFragment {
        val couponInformationData = couponPreviewFragment.getCouponInformationData()
        val couponInfoFragment = CreateCouponDetailFragment(couponInformationData)
        couponInfoFragment.setOnCouponSaved { coupon ->
            popFragment()
            couponPreviewFragment.setCouponInformationData(coupon)
        }
        return couponInfoFragment
    }

    private fun replace(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.parent_view, fragment)
            .commitAllowingStateLoss()
    }

    private fun replaceAndAddToBackstack(fragment: Fragment, tag : String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.parent_view, fragment, tag)
            .addToBackStack(tag)
            .commitAllowingStateLoss()
    }

    private fun popFragment() {
        supportFragmentManager.popBackStack()
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
}