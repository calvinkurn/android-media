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
import com.tokopedia.vouchercreation.product.create.domain.entity.*
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
        const val BUNDLE_KEY_TARGET_BUYER = "targetBuyer"
        const val BUNDLE_KEY_COUPON = "coupon"
        const val BUNDLE_KEY_COUPON_SETTINGS = "couponSettings"
        const val REQUEST_CODE_CREATE_COUPON = 100
        const val TARGET_ALL_USER = 0
        private const val REQUEST_CODE_ADD_PRODUCT = 101
        private const val EMPTY_STRING = ""
        private const val APP_LINK = "create-voucher-product"
        private const val COUPON_START_DATE_OFFSET_IN_HOUR = 3
        private const val COUPON_END_DATE_OFFSET_IN_DAYS = 30
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
        couponPreviewFragment.setCouponInformationData(populateDefaultCouponStartEndDate())
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

    private fun navigateToProductListPage(coupon: Coupon) {
        val couponSettings = coupon.settings

        val targetBuyer = 0 //0 for all user
        val benefitType = when {
            couponSettings.type == CouponType.FREE_SHIPPING -> "idr"
            couponSettings.type == CouponType.CASHBACK && couponSettings.discountType == DiscountType.NOMINAL -> "idr"
            couponSettings.type == CouponType.CASHBACK && couponSettings.discountType == DiscountType.PERCENTAGE -> "percent"
            else -> "idr"
        }

        val couponType = when (couponSettings.type) {
            CouponType.NONE -> EMPTY_STRING
            CouponType.CASHBACK -> "cashback"
            CouponType.FREE_SHIPPING -> "shipping"
        }

        val benefitIdr = couponSettings.discountAmount
        val benefitMax = couponSettings.maxDiscount
        val benefitPercent = couponSettings.discountPercentage
        val minPurchase = couponSettings.minimumPurchase

        val addProductIntent = Intent(this, ProductListActivity::class.java).apply {
            putExtras(Bundle().apply {
                putInt(BUNDLE_KEY_TARGET_BUYER, TARGET_ALL_USER)
                putParcelable(BUNDLE_KEY_COUPON_SETTINGS, couponSettings)
            })
        }
        startActivityForResult(addProductIntent, REQUEST_CODE_ADD_PRODUCT)
    }


    private fun onCreateCouponSuccess(coupon: Coupon) {
        redirectPage(coupon)
    }


    private fun saveCouponSettingsData(couponSettings: CouponSettings) {
        couponSettingFragment.setCouponSettings(couponSettings)

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


    private fun populateDefaultCouponStartEndDate(): CouponInformation {
        val startDate = getCouponDefaultStartDate()
        val endDate = getCouponDefaultEndDate()
        return CouponInformation(
            CouponInformation.Target.NOT_SELECTED,
            EMPTY_STRING,
            EMPTY_STRING,
            CouponInformation.Period(startDate, endDate)
        )
    }

    private fun getCouponDefaultStartDate() : Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, COUPON_START_DATE_OFFSET_IN_HOUR)
        return calendar.time
    }

    private fun getCouponDefaultEndDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, COUPON_END_DATE_OFFSET_IN_DAYS)
        return calendar.time
    }

}