package com.tokopedia.product_ar.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product_ar.di.DaggerProductArComponent
import com.tokopedia.product_ar.di.ProductArComponent
import com.tokopedia.product_ar.di.ProductArModule

class ProductArActivity : BaseSimpleActivity(), HasComponent<ProductArComponent> {

    companion object {
        const val SHOP_ID_EXTRA = "shopId"
    }

    private var productId: String = ""
    private var shopId: String = ""

    override fun getNewFragment(): Fragment = ProductArFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent.data

        uri?.let {
            productId = it.lastPathSegment.toString()
            shopId = intent.getStringExtra(SHOP_ID_EXTRA) ?: ""
        }
    }

    override fun getComponent(): ProductArComponent {
        val baseComponent = (applicationContext as BaseMainApplication).baseAppComponent
        return DaggerProductArComponent.builder()
                .baseAppComponent(baseComponent)
                .productArModule(ProductArModule(productId, shopId))
                .build()
    }
}