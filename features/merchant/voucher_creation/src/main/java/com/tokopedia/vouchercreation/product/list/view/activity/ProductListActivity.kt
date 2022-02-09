package com.tokopedia.vouchercreation.product.list.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.view.activity.CreateCouponProductActivity.Companion.BUNDLE_KEY_COUPON_SETTINGS
import com.tokopedia.vouchercreation.product.create.view.activity.CreateCouponProductActivity.Companion.BUNDLE_KEY_TARGET_BUYER
import com.tokopedia.vouchercreation.product.create.view.activity.CreateCouponProductActivity.Companion.TARGET_ALL_USER
import com.tokopedia.vouchercreation.product.list.view.fragment.AddProductFragment

class ProductListActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun getNewFragment(): Fragment {
        return AddProductFragment.createInstance(
                targetBuyer = intent.getIntExtra(BUNDLE_KEY_TARGET_BUYER, TARGET_ALL_USER),
                couponSettings = intent.getParcelableExtra(BUNDLE_KEY_COUPON_SETTINGS)
        )
    }

    private fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }
}