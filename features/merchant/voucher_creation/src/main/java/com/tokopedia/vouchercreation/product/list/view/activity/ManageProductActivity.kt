package com.tokopedia.vouchercreation.product.list.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.product.create.view.activity.CreateCouponProductActivity.Companion.BUNDLE_KEY_MAX_PRODUCT_LIMIT
import com.tokopedia.vouchercreation.product.create.view.activity.CreateCouponProductActivity.Companion.BUNDLE_KEY_SELECTED_PRODUCT_IDS
import com.tokopedia.vouchercreation.product.list.view.fragment.ManageProductFragment

class ManageProductActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun getNewFragment(): Fragment {
        return ManageProductFragment.createInstance(
                maxProductLimit = intent.getIntExtra(BUNDLE_KEY_MAX_PRODUCT_LIMIT, 0),
                selectedProductIds = intent.getParcelableArrayListExtra(BUNDLE_KEY_SELECTED_PRODUCT_IDS)?: arrayListOf()
        )
    }

    private fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }
}