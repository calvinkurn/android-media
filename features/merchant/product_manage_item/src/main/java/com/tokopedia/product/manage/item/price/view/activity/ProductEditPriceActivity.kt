package com.tokopedia.product.manage.item.price.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.price.view.fragment.ProductEditPriceFragment
import com.tokopedia.product.manage.item.price.model.ProductPrice
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_HAS_VARIANT
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_IS_GOLD_MERCHANT
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_IS_OFFICIAL_STORE
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_PRICE

class ProductEditPriceActivity : BaseSimpleActivity(){

    companion object {
        fun createIntent(context: Context, productPrice: ProductPrice?, isOfficialStore: Boolean, hasVariant: Boolean, isGoldMerchant: Boolean): Intent {
            return Intent(context, ProductEditPriceActivity::class.java)
                    .putExtra(EXTRA_PRICE, productPrice)
                    .putExtra(EXTRA_IS_OFFICIAL_STORE, isOfficialStore)
                    .putExtra(EXTRA_HAS_VARIANT, hasVariant)
                    .putExtra(EXTRA_IS_GOLD_MERCHANT, isGoldMerchant)
        }
    }

    override fun getNewFragment(): Fragment = ProductEditPriceFragment.createInstance()

    override fun getLayoutRes() = R.layout.activity_product_edit_with_menu
}
