package com.tokopedia.vouchercreation.product.create.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.vouchercreation.R
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
        replaceFragment(couponPreviewFragment, TAG_FRAGMENT_COUPON_PREVIEW)
        setupViews()
    }

    private fun setupViews() {
        couponPreviewFragment.setOnNavigateToCouponInformationPageListener {
            //TODO : Replace with your fragment
        }
        couponPreviewFragment.setOnNavigateToCouponSettingsPageListener {
            addOrReplaceFragment(couponSettingFragment, TAG_FRAGMENT_COUPON_SETTINGS)
        }
        couponPreviewFragment.setOnNavigateToProductListPageListener {
            //TODO : Replace with your fragment
        }
    }


    private fun addOrReplaceFragment(fragment: Fragment, tag: String) {
        if (!isFragmentAlreadyAdded(TAG_FRAGMENT_COUPON_SETTINGS)) {
            replaceFragment(fragment, tag)
        } else {
            addFragment(fragment, tag)
        }
    }

    private fun isFragmentAlreadyAdded(tag: String): Boolean {
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        return fragment != null
    }

    private fun replaceFragment(fragment: Fragment, tag : String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, fragment, tag)
            .addToBackStack(tag)
            .commitAllowingStateLoss()
    }

    private fun addFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.frameLayout, fragment, tag)
            .addToBackStack(tag)
            .commitAllowingStateLoss()
    }

}