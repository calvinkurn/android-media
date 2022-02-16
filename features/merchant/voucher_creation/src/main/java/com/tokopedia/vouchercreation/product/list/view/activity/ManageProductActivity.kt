package com.tokopedia.vouchercreation.product.list.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.product.list.view.fragment.ManageProductFragment
import com.tokopedia.vouchercreation.product.list.view.fragment.ManageProductFragment.Companion.BUNDLE_KEY_COUPON_SETTINGS
import com.tokopedia.vouchercreation.product.list.view.fragment.ManageProductFragment.Companion.BUNDLE_KEY_IS_EDITING
import com.tokopedia.vouchercreation.product.list.view.fragment.ManageProductFragment.Companion.BUNDLE_KEY_MAX_PRODUCT_LIMIT
import com.tokopedia.vouchercreation.product.list.view.fragment.ManageProductFragment.Companion.BUNDLE_KEY_SELECTED_PRODUCTS
import com.tokopedia.vouchercreation.product.list.view.fragment.ManageProductFragment.Companion.BUNDLE_KEY_SELECTED_PRODUCT_IDS

class ManageProductActivity : BaseSimpleActivity() {

    companion object {
        private const val IS_EDITING_DEFAULT_VALUE = true
        private const val MAX_PRODUCT_LIMIT_DEFAULT_VALUE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun getNewFragment(): Fragment {
        return ManageProductFragment.createInstance(
                isEditing = intent.getBooleanExtra(BUNDLE_KEY_IS_EDITING, IS_EDITING_DEFAULT_VALUE),
                maxProductLimit = intent.getIntExtra(BUNDLE_KEY_MAX_PRODUCT_LIMIT, MAX_PRODUCT_LIMIT_DEFAULT_VALUE),
                couponSettings = intent.getParcelableExtra(BUNDLE_KEY_COUPON_SETTINGS),
                selectedProducts = intent.getParcelableArrayListExtra(BUNDLE_KEY_SELECTED_PRODUCTS),
                selectedProductIds = intent.getParcelableArrayListExtra(BUNDLE_KEY_SELECTED_PRODUCT_IDS)
        )
    }

    private fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }
}