package com.tokopedia.product.edit.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.price.BaseProductEditFragment.Companion.EXTRA_HAS_VARIANT
import com.tokopedia.product.edit.price.BaseProductEditFragment.Companion.EXTRA_OFFICIAL_STORE
import com.tokopedia.product.edit.price.BaseProductEditFragment.Companion.EXTRA_PRICE
import com.tokopedia.product.edit.price.ProductEditPriceFragment
import com.tokopedia.product.edit.price.model.ProductPrice

class ProductEditPriceActivity : BaseSimpleActivity(){

    companion object {
        fun createIntent(context: Context, productPrice: ProductPrice, officialStore: Boolean, hasVariant: Boolean): Intent {
            return Intent(context, ProductEditPriceActivity::class.java)
                    .putExtra(EXTRA_PRICE, productPrice)
                    .putExtra(EXTRA_OFFICIAL_STORE, officialStore)
                    .putExtra(EXTRA_HAS_VARIANT, hasVariant)
        }
    }

    override fun getNewFragment(): Fragment{
        return ProductEditPriceFragment.createInstance()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_product_edit_with_menu
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
