package com.tokopedia.vouchercreation.product.create.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.product.create.view.fragment.CouponSettingFragment
import com.tokopedia.vouchercreation.product.create.view.fragment.CreateCouponDetailFragment
import com.tokopedia.vouchercreation.product.create.view.fragment.ProductCouponPreviewFragment
import com.tokopedia.vouchercreation.product.voucherlist.view.fragment.CouponListFragment
import java.util.*

class CreateCouponProductActivity : AppCompatActivity() {

    private val couponPreviewFragment = ProductCouponPreviewFragment.newInstance(
        ::navigateToCouponInformationPage,
        ::navigateToCouponSettingPage,
        ::navigateToProductListPage,
        ::onCreateCouponSuccess,
        ::onUpdateCouponSuccess,
        null,
        ProductCouponPreviewFragment.Mode.CREATE
    )

    private val couponSettingFragment = CouponSettingFragment()
    private val couponListFragment = CouponListFragment.newInstance(
        ::navigateToCreateCouponPage,
        ::navigateToEditCouponPage,
        ::navigateToDuplicateCouponPage
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
        setContentView(R.layout.activity_mvc_create_coupon)
        replace(couponPreviewFragment)
        setupViews()
        println(productId)
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
        val fragment = ProductCouponPreviewFragment.newInstance(
            ::navigateToCouponInformationPage,
            ::navigateToCouponSettingPage,
            ::navigateToProductListPage,
            ::onCreateCouponSuccess,
            ::onUpdateCouponSuccess,
            null,
            ProductCouponPreviewFragment.Mode.CREATE
        )
        replaceAndAddToBackstack(fragment, TAG_FRAGMENT_COUPON_PREVIEW)
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

    private fun onCreateCouponSuccess() {
        replace(couponListFragment)
        showToaster("Berhasil dibuat")
    }

    private fun onUpdateCouponSuccess() {
        popFragment()
    }

    private fun setupViews() {
        couponSettingFragment.setOnCouponSaved { couponSettings ->
            popFragment()

            this.couponSettings = couponSettings
            couponPreviewFragment.setCouponSettingsData(couponSettings)

            //Stub the coupon preview data for testing purpose
            val startDate = Calendar.getInstance().apply { set(2022, 0, 29, 22, 30, 0) }
            val endDate = Calendar.getInstance().apply {  set(2022, 0, 30, 22, 0, 0) }
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
        }


        /*productListFragment.setOnProductsSelected { products ->
            popFragment()

            //TODO : @Deyo please map your products data to List<CouponProduct>
            //couponPreviewFragment.setCouponProductsData(products)
        }*/
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

    private fun showToaster(text: String) {
        if (text.isEmpty()) return
        Toaster.build(findViewById(R.id.parent_view), text).show()
    }
}