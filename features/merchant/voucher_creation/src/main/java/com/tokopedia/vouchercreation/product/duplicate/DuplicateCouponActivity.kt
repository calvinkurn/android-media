package com.tokopedia.vouchercreation.product.duplicate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.utils.FragmentRouter
import com.tokopedia.vouchercreation.product.create.data.response.ProductId
import com.tokopedia.vouchercreation.product.create.domain.entity.Coupon
import com.tokopedia.vouchercreation.product.create.view.fragment.CouponSettingFragment
import com.tokopedia.vouchercreation.product.create.view.fragment.CreateCouponDetailFragment
import com.tokopedia.vouchercreation.product.list.view.activity.ManageProductActivity
import com.tokopedia.vouchercreation.product.list.view.fragment.ManageProductFragment
import com.tokopedia.vouchercreation.product.preview.CouponPreviewFragment
import javax.inject.Inject

class DuplicateCouponActivity : AppCompatActivity() {

    companion object {
        private const val BUNDLE_KEY_COUPON_ID = "coupon-id"
        const val BUNDLE_KEY_MAX_PRODUCT_LIMIT = "maxProductLimit"
        const val BUNDLE_KEY_SELECTED_PRODUCT_IDS = "selectedProductIds"
        const val BUNDLE_KEY_COUPON_SETTINGS = "couponSettings"

        @JvmStatic
        fun start(context: Context, couponId : Long) {
            val starter = Intent(context, DuplicateCouponActivity::class.java)
                .putExtra(BUNDLE_KEY_COUPON_ID, couponId)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var router: FragmentRouter

    private val couponId by lazy { intent.extras?.getLong(BUNDLE_KEY_COUPON_ID).orZero() }

    private val couponPreviewFragment by lazy {
        CouponPreviewFragment.newInstance(
            ::navigateToCouponInformationPage,
            ::navigateToCouponSettingPage,
            ::navigateToManageProductPage,
            {},
            {},
            {},
            ::onDuplicateCouponSuccess,
            couponId,
            CouponPreviewFragment.Mode.DUPLICATE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        setContentView(R.layout.activity_mvc_coupon_list)
        displayPage()
        couponPreviewFragment.setOnCouponDuplicated {
            showToaster(getString(R.string.coupon_duplicated))
        }
    }

    private fun setupDependencyInjection() {
        DaggerVoucherCreationComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun displayPage() {
        replace(couponPreviewFragment)
    }

    private fun replace(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.parent_view, fragment)
            .commit()
    }


    private fun navigateToCouponInformationPage() {
        router.replaceAndAddToBackstack(
            supportFragmentManager,
            R.id.parent_view,
            setupCreateCouponDetailFragment()
        )
    }

    private fun navigateToCouponSettingPage() {
        router.replaceAndAddToBackstack(
            supportFragmentManager,
            R.id.parent_view,
            buildCouponSettingFragmentInstance()
        )
    }

    private fun navigateToManageProductPage(coupon: Coupon) {
        val couponSettings = coupon.settings
        val maxProductLimit = couponPreviewFragment.getMaxAllowedProduct()
        val manageProductIntent = Intent(this, ManageProductActivity::class.java).apply {
            putExtras(Bundle().apply {
                putBoolean(ManageProductFragment.BUNDLE_KEY_IS_VIEWING, true)
                putInt(BUNDLE_KEY_MAX_PRODUCT_LIMIT, maxProductLimit)
                putParcelable(BUNDLE_KEY_COUPON_SETTINGS, couponSettings)
                val selectedProductIds = ArrayList<ProductId>()
                selectedProductIds.addAll(couponPreviewFragment.getSelectedProductIds())
                putParcelableArrayList(BUNDLE_KEY_SELECTED_PRODUCT_IDS, selectedProductIds)
            })
        }
        startActivity(manageProductIntent)
    }

    private fun onDuplicateCouponSuccess() {
        finish()
    }

    private fun showToaster(text: String) {
        if (text.isEmpty()) return
        val view = findViewById<FrameLayout>(R.id.parent_view)
        Toaster.build(view, text).show()
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
    private fun buildCouponSettingFragmentInstance(): CouponSettingFragment {
        val couponSettingsData = couponPreviewFragment.getCouponSettingsData()
        val fragment = CouponSettingFragment.newInstance(couponSettingsData)
        fragment.setOnCouponSaved {
            router.popFragment(supportFragmentManager)
            couponPreviewFragment.setCouponSettingsData(it)
        }
        return fragment
    }
}