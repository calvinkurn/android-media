package com.tokopedia.vouchercreation.product.update

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.utils.FragmentRouter
import com.tokopedia.vouchercreation.product.create.data.response.ProductId
import com.tokopedia.vouchercreation.product.create.domain.entity.Coupon
import com.tokopedia.vouchercreation.product.create.view.activity.CreateCouponProductActivity
import com.tokopedia.vouchercreation.product.create.view.fragment.CouponSettingFragment
import com.tokopedia.vouchercreation.product.create.view.fragment.CreateCouponDetailFragment
import com.tokopedia.vouchercreation.product.list.view.activity.AddProductActivity
import com.tokopedia.vouchercreation.product.list.view.activity.ManageProductActivity
import com.tokopedia.vouchercreation.product.list.view.model.ProductUiModel
import com.tokopedia.vouchercreation.product.preview.CouponPreviewFragment
import javax.inject.Inject

class UpdateCouponActivity : AppCompatActivity() {

    @Inject
    lateinit var router: FragmentRouter

    companion object {
        const val BUNDLE_KEY_COUPON_ID = "coupon-id"
        const val BUNDLE_KEY_MAX_PRODUCT_LIMIT = "maxProductLimit"
        const val BUNDLE_KEY_SELECTED_PRODUCTS = "selectedProducts"
        const val BUNDLE_KEY_SELECTED_PRODUCT_IDS = "selectedProductIds"
        const val BUNDLE_KEY_COUPON_SETTINGS = "couponSettings"
        const val REQUEST_CODE_UPDATE_COUPON = 200
        const val REQUEST_CODE_ADD_PRODUCT = 101
        const val REQUEST_CODE_MANAGE_PRODUCT = 102
    }

    private val couponId by lazy { intent.extras?.getLong(BUNDLE_KEY_COUPON_ID).orZero() }

    private val couponPreviewFragment by lazy {
        CouponPreviewFragment.newInstance(
                ::navigateToCouponInformationPage,
                ::navigateToCouponSettingPage,
                ::navigateToAddProductPage,
                ::navigateToManageProductPage,
                {},
                ::onUpdateCouponSuccess,
                {},
                couponId,
                CouponPreviewFragment.Mode.UPDATE
        )
    }


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
        router.replace(supportFragmentManager, R.id.parent_view, couponPreviewFragment)
    }


    private fun onUpdateCouponSuccess() {
        notifyUpdateCouponSuccessToCouponListPage()
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
                val selectedProductIds = couponPreviewFragment.getSelectedProductIds()
                val selectedProducts = arrayListOf<ProductUiModel>()
                selectedProducts.addAll(couponPreviewFragment.getSelectedProducts(selectedProductIds))
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
                val selectedProductIds = ArrayList<ProductId>()
                selectedProductIds.addAll(couponPreviewFragment.getSelectedProductIds())
                putParcelableArrayList(BUNDLE_KEY_SELECTED_PRODUCT_IDS, selectedProductIds)
                val selectedProducts = arrayListOf<ProductUiModel>()
                selectedProducts.addAll(couponPreviewFragment.getSelectedProducts())
                putParcelableArrayList(BUNDLE_KEY_SELECTED_PRODUCTS, selectedProducts)
            })
        }
        startActivityForResult(manageProductIntent, CreateCouponProductActivity.REQUEST_CODE_MANAGE_PRODUCT)
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

    private fun notifyUpdateCouponSuccessToCouponListPage() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == CreateCouponProductActivity.REQUEST_CODE_ADD_PRODUCT) {
                val selectedProducts = data?.getParcelableArrayListExtra<ProductUiModel>(BUNDLE_KEY_SELECTED_PRODUCTS)?.toList() ?: listOf()
                couponPreviewFragment.addProducts(selectedProducts)
            } else if (requestCode == CreateCouponProductActivity.REQUEST_CODE_MANAGE_PRODUCT) {
                val selectedProducts = data?.getParcelableArrayListExtra<ProductUiModel>(BUNDLE_KEY_SELECTED_PRODUCTS)?.toList() ?: listOf()
                couponPreviewFragment.setProducts(selectedProducts)
            }
        }
    }
}