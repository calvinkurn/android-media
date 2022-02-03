package com.tokopedia.vouchercreation.product.create.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.NumberConstant
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.utils.FragmentRouter
import com.tokopedia.vouchercreation.product.create.domain.entity.Coupon
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponProduct
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.view.fragment.CouponSettingFragment
import com.tokopedia.vouchercreation.product.create.view.fragment.CreateCouponDetailFragment
import com.tokopedia.vouchercreation.product.create.view.fragment.ProductCouponPreviewFragment
import com.tokopedia.vouchercreation.product.list.view.activity.ProductListActivity
import com.tokopedia.vouchercreation.product.voucherlist.view.activity.CouponListActivity
import java.util.*
import javax.inject.Inject

class CreateCouponProductActivity : AppCompatActivity() {

    companion object {
        private const val PRODUCT_ID_SEGMENT_INDEX = 1
        const val BUNDLE_KEY_COUPON = "coupon"
        const val REQUEST_CODE_CREATE_COUPON = 100
        private const val APP_LINK = "create-voucher-product"
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var router: FragmentRouter

    private val couponPreviewFragment = ProductCouponPreviewFragment.newInstance(
        ::navigateToCouponInformationPage,
        ::navigateToCouponSettingPage,
        ::navigateToProductListPage,
        ::onCreateCouponSuccess,
        {},
        {},
        null,
        ProductCouponPreviewFragment.Mode.CREATE
    )

    private val couponSettingFragment = CouponSettingFragment.newInstance(::saveCouponSettingsData)

    private val productId: String? by lazy { getProductIdDataFromApplink() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        setContentView(R.layout.activity_mvc_create_coupon)
        router.replace(supportFragmentManager, R.id.parent_view, couponPreviewFragment)
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
        router.replaceAndAddToBackstack(supportFragmentManager, R.id.parent_view, setupCreateCouponDetailFragment())
    }

    private fun navigateToCouponSettingPage() {
        router.replaceAndAddToBackstack(supportFragmentManager, R.id.parent_view, couponSettingFragment)
    }

    private fun navigateToProductListPage() {
        startActivity(Intent(this, ProductListActivity::class.java))
    }


    private fun onCreateCouponSuccess(coupon: Coupon) {
        redirectPage(coupon)
    }


    private fun saveCouponSettingsData(couponSettings: CouponSettings) {
        couponSettingFragment.setCouponSettings(couponSettings)

        //Stub the coupon preview data for testing purpose
        val startDate = Calendar.getInstance().apply { set(2022, 1, 5, 20, 30, 0) }
        val endDate = Calendar.getInstance().apply {  set(2022, 1, 10, 23, 59, 0) }
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
        router.popFragment(supportFragmentManager)
        couponPreviewFragment.setCouponSettingsData(couponSettings)
    }

    private fun setupCreateCouponDetailFragment(): CreateCouponDetailFragment {
        val couponInformationData = couponPreviewFragment.getCouponInformationData()
        val couponInfoFragment = CreateCouponDetailFragment(couponInformationData)
        couponInfoFragment.setOnCouponSaved { coupon ->
            router.popFragment(supportFragmentManager)
            couponPreviewFragment.setCouponInformationData(coupon)
        }
        return couponInfoFragment
    }


    private fun redirectPage(coupon: Coupon) {
        if (isLaunchedFromAppLink()) {
            CouponListActivity.start(this, coupon)
            finish()
        } else {
            notifyCreateCouponSuccessToCouponListPage(coupon)
        }
    }

    private fun notifyCreateCouponSuccessToCouponListPage(coupon: Coupon) {
        val intent = Intent()

        val bundle = Bundle()
        bundle.putParcelable(BUNDLE_KEY_COUPON, coupon)

        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun isLaunchedFromAppLink(): Boolean {
        val appLinkData = RouteManager.getIntent(this, intent.data.toString()).data
        val pathSegments = appLinkData?.pathSegments.orEmpty()
        val path = pathSegments.getOrNull(NumberConstant.ZERO)
        return path == APP_LINK
    }
}