package com.tokopedia.vouchercreation.product.list.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.product.create.view.activity.CreateCouponProductActivity
import com.tokopedia.vouchercreation.product.list.view.fragment.ManageProductFragment
import com.tokopedia.vouchercreation.product.list.view.fragment.ManageProductFragment.Companion.BUNDLE_KEY_COUPON_SETTINGS
import com.tokopedia.vouchercreation.product.list.view.fragment.ManageProductFragment.Companion.BUNDLE_KEY_IS_EDITING
import com.tokopedia.vouchercreation.product.list.view.fragment.ManageProductFragment.Companion.BUNDLE_KEY_IS_VIEWING
import com.tokopedia.vouchercreation.product.list.view.fragment.ManageProductFragment.Companion.BUNDLE_KEY_MAX_PRODUCT_LIMIT
import com.tokopedia.vouchercreation.product.list.view.fragment.ManageProductFragment.Companion.BUNDLE_KEY_SELECTED_PRODUCTS
import com.tokopedia.vouchercreation.product.list.view.fragment.ManageProductFragment.Companion.BUNDLE_KEY_SELECTED_PRODUCT_IDS
import com.tokopedia.vouchercreation.product.list.view.model.ProductUiModel

class ManageProductActivity : BaseSimpleActivity() {

    interface OnBackPressedListener {
        fun onBackPressed()
    }

    companion object {
        private const val IS_VIEWING_DEFAULT_VALUE = false
        private const val IS_EDITING_DEFAULT_VALUE = false
        private const val MAX_PRODUCT_LIMIT_DEFAULT_VALUE = 0
    }

    private var fragment: ManageProductFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun getNewFragment(): Fragment? {
        fragment = ManageProductFragment.createInstance(
                isViewing = intent.getBooleanExtra(BUNDLE_KEY_IS_VIEWING, IS_VIEWING_DEFAULT_VALUE),
                isEditing = intent.getBooleanExtra(BUNDLE_KEY_IS_EDITING, IS_EDITING_DEFAULT_VALUE),
                maxProductLimit = intent.getIntExtra(BUNDLE_KEY_MAX_PRODUCT_LIMIT, MAX_PRODUCT_LIMIT_DEFAULT_VALUE),
                couponSettings = intent.getParcelableExtra(BUNDLE_KEY_COUPON_SETTINGS),
                selectedProducts = intent.getParcelableArrayListExtra(BUNDLE_KEY_SELECTED_PRODUCTS),
                selectedProductIds = intent.getParcelableArrayListExtra(BUNDLE_KEY_SELECTED_PRODUCT_IDS)
        )
        return fragment
    }

    private fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onBackPressed() {
        (fragment as? OnBackPressedListener)?.onBackPressed()
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == CreateCouponProductActivity.REQUEST_CODE_ADD_PRODUCT) {
                val selectedProducts = data?.getParcelableArrayListExtra<ProductUiModel>(CreateCouponProductActivity.BUNDLE_KEY_SELECTED_PRODUCTS)?.toList() ?: listOf()
                fragment?.addProducts(selectedProducts)
            }
        }
    }
}