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
import com.tokopedia.vouchercreation.product.preview.CouponPreviewFragment
import com.tokopedia.vouchercreation.product.list.view.activity.AddProductActivity
import com.tokopedia.vouchercreation.product.list.view.activity.ManageProductActivity
import com.tokopedia.vouchercreation.product.list.view.fragment.ManageProductFragment
import com.tokopedia.vouchercreation.product.list.view.model.ProductUiModel
import com.tokopedia.vouchercreation.product.voucherlist.view.activity.CouponListActivity
import java.util.*
import javax.inject.Inject

class CreateCouponProductActivity : AppCompatActivity() {

    companion object {
        private const val PRODUCT_ID_SEGMENT_INDEX = 1
        const val BUNDLE_KEY_MAX_PRODUCT_LIMIT = "maxProductLimit"
        const val BUNDLE_KEY_SELECTED_PRODUCTS = "selectedProducts"
        const val BUNDLE_KEY_COUPON = "coupon"
        const val BUNDLE_KEY_COUPON_SETTINGS = "couponSettings"
        const val REQUEST_CODE_CREATE_COUPON = 100
        const val REQUEST_CODE_ADD_PRODUCT = 101
        const val REQUEST_CODE_MANAGE_PRODUCT = 102
        private const val EMPTY_STRING = ""
        private const val APP_LINK = "create-voucher-product"
        private const val COUPON_START_DATE_OFFSET_IN_HOUR = 3
        private const val COUPON_END_DATE_OFFSET_IN_DAYS = 30
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var router: FragmentRouter

    private val couponPreviewFragment = CouponPreviewFragment.newInstance(
        ::navigateToCouponInformationPage,
        ::navigateToCouponSettingPage,
        ::navigateToAddProductPage,
        ::navigateToManageProductPage,
        ::onCreateCouponSuccess,
        {},
        {},
        CouponPreviewFragment.COUPON_ID_NOT_YET_CREATED,
        CouponPreviewFragment.Mode.CREATE
    )

    private val productId: String? by lazy { getProductIdDataFromApplink() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        setContentView(R.layout.activity_mvc_create_coupon)
        router.replace(supportFragmentManager, R.id.parent_view, couponPreviewFragment)
        println(productId) //TODO: Auto select product based on productId
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
        router.replaceAndAddToBackstack(supportFragmentManager, R.id.parent_view, buildCouponSettingFragmentInstance())
    }

    private fun navigateToAddProductPage(coupon: Coupon) {
        val couponSettings = coupon.settings
        val maxProductLimit = couponPreviewFragment.getMaxAllowedProduct()
        val addProductIntent = Intent(this, AddProductActivity::class.java).apply {
            putExtras(Bundle().apply {
                putInt(BUNDLE_KEY_MAX_PRODUCT_LIMIT, maxProductLimit)
                putParcelable(BUNDLE_KEY_COUPON_SETTINGS, couponSettings)
                val selectedProducts = arrayListOf<ProductUiModel>()
                selectedProducts.addAll(couponPreviewFragment.getSelectedProducts())
                putParcelableArrayList(BUNDLE_KEY_SELECTED_PRODUCTS, selectedProducts)
            })
        }
        startActivityForResult(addProductIntent, REQUEST_CODE_ADD_PRODUCT)
    }

    private fun navigateToManageProductPage(coupon: Coupon) {
        val couponSettings = coupon.settings
        val maxProductLimit = couponPreviewFragment.getMaxAllowedProduct()
        val manageProductIntent = Intent(this, ManageProductActivity::class.java).apply {
            putExtras(Bundle().apply {
                putInt(BUNDLE_KEY_MAX_PRODUCT_LIMIT, maxProductLimit)
                putParcelable(BUNDLE_KEY_COUPON_SETTINGS, couponSettings)
                val selectedProducts = ArrayList<ProductUiModel>()
                selectedProducts.addAll(couponPreviewFragment.getSelectedProducts())
                putParcelableArrayList(BUNDLE_KEY_SELECTED_PRODUCTS, selectedProducts)
            })
        }
        startActivityForResult(manageProductIntent, REQUEST_CODE_MANAGE_PRODUCT)
    }

    private fun onCreateCouponSuccess(coupon: Coupon) {
        redirectPage(coupon)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADD_PRODUCT) {
                val selectedProducts = data?.getParcelableArrayListExtra<ProductUiModel>(BUNDLE_KEY_SELECTED_PRODUCTS)?.toList() ?: listOf()
                couponPreviewFragment.addProducts(selectedProducts)
            } else if (requestCode == REQUEST_CODE_MANAGE_PRODUCT) {
                val selectedProducts = data?.getParcelableArrayListExtra<ProductUiModel>(BUNDLE_KEY_SELECTED_PRODUCTS)?.toList() ?: listOf()
                couponPreviewFragment.setProducts(selectedProducts)
            }
        }
    }
}