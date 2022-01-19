package com.tokopedia.vouchercreation.product.create.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponProduct
import com.tokopedia.vouchercreation.product.create.view.fragment.CouponSettingFragment
import com.tokopedia.vouchercreation.product.create.view.fragment.ProductCouponPreviewFragment

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
            //TODO : Replace with your fragment
        }
        couponPreviewFragment.setOnNavigateToCouponSettingsPageListener {
            replaceFragment(couponSettingFragment, TAG_FRAGMENT_COUPON_SETTINGS)
        }
        couponPreviewFragment.setOnNavigateToProductListPageListener {
            //TODO : Replace with your fragment
        }
        couponSettingFragment.setOnCouponSaved { coupon ->
            popFragment()
            couponPreviewFragment.setCouponSettingsData(coupon)
            couponPreviewFragment.setCouponInformationData(CouponInformation("Khusus", "Kupon Kopi Kenangan", "KOPKEN", "3 Jan 2022 - 4 Feb 2022"))
            couponPreviewFragment.setCouponProductsData(listOf(CouponProduct(1), CouponProduct(2)))
        }
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