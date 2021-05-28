package com.tokopedia.product.detail.ui.base

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.product.detail.ui.di.DaggerProductDetailTestComponent
import com.tokopedia.product.detail.view.fragment.DynamicProductDetailFragment

/**
 * Created by Yehezkiel on 08/04/21
 */
class ProductDetailFragmentMock : DynamicProductDetailFragment() {

    override fun initInjector() {
        activity?.let {
            DaggerProductDetailTestComponent.builder()
                    .baseAppComponent((it.application as BaseMainApplication).baseAppComponent)
                    .build()
        }
    }
}