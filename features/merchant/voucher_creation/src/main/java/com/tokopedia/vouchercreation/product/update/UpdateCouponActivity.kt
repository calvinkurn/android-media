package com.tokopedia.vouchercreation.product.update

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.utils.FragmentRouter
import com.tokopedia.vouchercreation.product.create.domain.entity.Coupon
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponProduct
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.view.fragment.CouponSettingFragment
import com.tokopedia.vouchercreation.product.create.view.fragment.CreateCouponDetailFragment
import com.tokopedia.vouchercreation.product.create.view.fragment.ProductCouponPreviewFragment
import com.tokopedia.vouchercreation.product.list.view.activity.ProductListActivity
import javax.inject.Inject

class UpdateCouponActivity : AppCompatActivity() {

    @Inject
    lateinit var router: FragmentRouter

    companion object {
        private const val BUNDLE_KEY_COUPON= "coupon"
        @JvmStatic
        fun start(context: Context, coupon: Coupon) {
            val starter = Intent(context, UpdateCouponActivity::class.java)
                .putExtra(BUNDLE_KEY_COUPON, coupon)
            context.startActivity(starter)
        }
    }
    private val coupon by lazy { intent.extras?.getParcelable(BUNDLE_KEY_COUPON) as? Coupon }

    private val couponPreviewFragment by lazy {
        ProductCouponPreviewFragment.newInstance(
            ::navigateToCouponInformationPage,
            ::navigateToCouponSettingPage,
            ::navigateToProductListPage,
            {},
            ::onUpdateCouponSuccess,
            {},
            coupon,
            ProductCouponPreviewFragment.Mode.UPDATE
        )
    }

    private val couponSettingFragment = CouponSettingFragment.newInstance(::saveCouponSettingsData)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        setContentView(R.layout.activity_mvc_coupon_list)
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
        router.replace(supportFragmentManager, R.id.parent_view, couponPreviewFragment)
    }


    private fun onUpdateCouponSuccess() {
        showToaster(getString(R.string.coupon_updated))
        finish()
    }


    private fun showToaster(text: String) {
        if (text.isEmpty()) return
        val view = findViewById<FrameLayout>(R.id.parent_view)
        Toaster.build(view, text).show()
    }


    private fun navigateToCouponInformationPage() {
        router.replaceAndAddToBackstack(supportFragmentManager, R.id.parent_view, setupCreateCouponDetailFragment())
    }

    private fun navigateToCouponSettingPage() {
        router.replaceAndAddToBackstack(supportFragmentManager, R.id.parent_view, couponSettingFragment)
    }

    private fun navigateToProductListPage(coupon: Coupon) {
        startActivity(Intent(this, ProductListActivity::class.java))
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
}