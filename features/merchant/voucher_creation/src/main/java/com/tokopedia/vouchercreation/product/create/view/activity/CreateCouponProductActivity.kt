package com.tokopedia.vouchercreation.product.create.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponProduct
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.view.fragment.CouponSettingFragment
import com.tokopedia.vouchercreation.product.create.view.fragment.CreateCouponDetailFragment
import com.tokopedia.vouchercreation.product.create.view.fragment.ProductCouponPreviewFragment

class CreateCouponProductActivity : AppCompatActivity() {

    private val couponPreviewFragment = ProductCouponPreviewFragment()
    private val couponSettingFragment = CouponSettingFragment()
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
        displayCouponPreviewFragment()
        setupViews()
        println(productId)
    }

    private fun getProductIdDataFromApplink(): String? {
        val applinkData = RouteManager.getIntent(this, intent.data.toString()).data
        val pathSegments = applinkData?.pathSegments.orEmpty()
        return pathSegments.getOrNull(PRODUCT_ID_SEGMENT_INDEX)
    }

    private fun setupViews() {
        couponPreviewFragment.setOnNavigateToCouponInformationPageListener {
            //TODO : @Faisal Replace with your coupon information fragment
        }
        couponPreviewFragment.setOnNavigateToCouponSettingsPageListener {
            couponSettingFragment.setCouponSettings(couponSettings)
            replaceFragment(couponSettingFragment, TAG_FRAGMENT_COUPON_SETTINGS)
        }
        couponPreviewFragment.setOnNavigateToProductListPageListener {
            //TODO : @Deyo Replace with your product list fragment
        }

       /* couponInformationFragment.setOnCouponSaved { coupon ->
            popFragment()

            //TODO : @Faisal please map your coupon information data to CouponInformation model

            couponPreviewFragment.setCouponInformationData(couponInformation)
        }*/

        couponSettingFragment.setOnCouponSaved { couponSettings ->
            popFragment()

            this.couponSettings = couponSettings
            couponPreviewFragment.setCouponSettingsData(couponSettings)

            //Stub the coupon preview data for testing purpose
            val startDate = Calendar.getInstance().apply { set(2022, 0, 20, 22, 30, 0) }
            val endDate = Calendar.getInstance().apply {  set(2022, 0, 25, 22, 0, 0) }
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
                        1,
                        18000,
                        5,
                        "https://images.tokopedia.net/img/VqbcmM/2021/4/15/16087191-6556-40b5-9150-36944b73f85e.jpg"
                    ),
                    CouponProduct(
                        2,
                        25000,
                        4,
                        "https://images.tokopedia.net/img/VqbcmM/2021/4/15/16087191-6556-40b5-9150-36944b73f85e.jpg"
                    )
                )
            )
        }

        /*couponProducts.setOnCouponSaved { products ->
            popFragment()

            //TODO : @Deyo please map your products data to List<CouponProduct>
            //couponPreviewFragment.setCouponProductsData(products)
        }*/
    }

    private fun setupCreateCouponDetailFragment(): CreateCouponDetailFragment {
        val couponInfoFragment = CreateCouponDetailFragment()
        couponInfoFragment.setOnCouponSaved { coupon ->
            popFragment()
            couponPreviewFragment.setCouponInformationData(coupon)
        }
        return couponInfoFragment
    }

    private fun displayCouponPreviewFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.parent_view, couponPreviewFragment)
            .commitAllowingStateLoss()
    }

    private fun replaceFragment(fragment: Fragment, tag : String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.parent_view, fragment, tag)
            .addToBackStack(tag)
            .commitAllowingStateLoss()
    }

    private fun popFragment() {
        supportFragmentManager.popBackStack()
    }
}