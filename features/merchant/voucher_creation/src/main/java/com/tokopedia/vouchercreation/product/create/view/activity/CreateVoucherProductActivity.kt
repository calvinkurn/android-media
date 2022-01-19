package com.tokopedia.vouchercreation.product.create.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponProduct
import com.tokopedia.vouchercreation.product.create.view.fragment.CouponSettingFragment
import com.tokopedia.vouchercreation.product.create.view.fragment.ProductCouponPreviewFragment
import java.util.*

class CreateVoucherProductActivity : AppCompatActivity() {

    private val couponPreviewFragment by lazy { ProductCouponPreviewFragment.newInstance() }
    private val couponSettingFragment = CouponSettingFragment()

    companion object {
        private const val TAG_FRAGMENT_COUPON_INFORMATION = "coupon_information"
        private const val TAG_FRAGMENT_COUPON_SETTINGS = "coupon_settings"
        private const val TAG_FRAGMENT_COUPON_PREVIEW = "coupon_preview"
        private const val TAG_FRAGMENT_PRODUCT_LIST = "product_list"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvc_create_voucher_product)
        displayCouponPreviewFragment()
        setupViews()
    }

    private fun setupViews() {
        couponPreviewFragment.setOnNavigateToCouponInformationPageListener {
            //TODO : @Faisal Replace with your coupon information fragment
        }
        couponPreviewFragment.setOnNavigateToCouponSettingsPageListener {
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

        couponSettingFragment.setOnCouponSaved { coupon ->
            popFragment()
            couponPreviewFragment.setCouponSettingsData(coupon)

            //TODO : Please replace with your real implementation of CouponInformation instance
            val startDate = Calendar.getInstance().apply { set(2022, 0, 17, 8, 30, 0) }
            val endDate = Calendar.getInstance().apply {  set(2022, 0, 17, 22, 0, 0) }
            val period = CouponInformation.Period(startDate.time, endDate.time)
            couponPreviewFragment.setCouponInformationData(CouponInformation(CouponInformation.Target.PUBLIC, "Kupon Kopi Kenangan", "KOPKEN", period))

            //TODO : Please replace with your real implementation of CouponProduct instance
            couponPreviewFragment.setCouponProductsData(listOf(CouponProduct(1), CouponProduct(2)))
        }

        /*couponInformationFragment.setOnCouponSaved { products ->
            popFragment()

            //TODO : @Deyo please map your products data to List<CouponProduct>
            //couponPreviewFragment.setCouponProductsData(products)
        }*/
    }


    private fun displayCouponPreviewFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, couponPreviewFragment)
            .commitAllowingStateLoss()
    }

    private fun replaceFragment(fragment: Fragment, tag : String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, fragment, tag)
            .addToBackStack(tag)
            .commitAllowingStateLoss()
    }

    private fun popFragment() {
        supportFragmentManager.popBackStack()
    }

}