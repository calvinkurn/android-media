package com.tokopedia.vouchercreation.product.duplicate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.vouchercreation.R
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
import java.util.*
import javax.inject.Inject

class DuplicateCouponActivity : AppCompatActivity() {
    @Inject
    lateinit var router: FragmentRouter

    private val couponSettingFragment = CouponSettingFragment.newInstance(::saveCouponSettingsData)
    private val coupon by lazy { intent.extras?.getSerializable(BUNDLE_KEY_COUPON) as? Coupon }
    private val couponPreviewFragment by lazy {
        ProductCouponPreviewFragment.newInstance(
            ::navigateToCouponInformationPage,
            ::navigateToCouponSettingPage,
            ::navigateToProductListPage,
            {},
            {},
            ::onDuplicateCouponSuccess,
            coupon,
            ProductCouponPreviewFragment.Mode.DUPLICATE
        )
    }

    companion object {
        private const val BUNDLE_KEY_COUPON = "coupon"

        @JvmStatic
        fun start(context: Context, coupon: Coupon) {
            val starter = Intent(context, DuplicateCouponActivity::class.java)
                .putExtra(BUNDLE_KEY_COUPON, coupon)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        setContentView(R.layout.activity_mvc_coupon_list)
        showToaster(getString(R.string.coupon_duplicated))
        displayPage()
    }

    private fun setupDependencyInjection() {
        DaggerVoucherCreationComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun displayPage() {
        couponSettingFragment.setCouponSettings(coupon?.settings)
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
            couponSettingFragment
        )
    }

    private fun navigateToProductListPage() {
        startActivity(Intent(this, ProductListActivity::class.java))
    }


    private fun onDuplicateCouponSuccess() {
        finish()
    }

    private fun showToaster(text: String) {
        if (text.isEmpty()) return
        val view = findViewById<FrameLayout>(R.id.parent_view)
        Toaster.build(view, text).show()
    }

    private fun saveCouponSettingsData(couponSettings: CouponSettings) {
        couponSettingFragment.setCouponSettings(couponSettings)

        //Stub the coupon preview data for testing purpose
        val startDate = Calendar.getInstance().apply { set(2022, 1, 5, 20, 30, 0) }
        val endDate = Calendar.getInstance().apply { set(2022, 1, 10, 23, 59, 0) }
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
}