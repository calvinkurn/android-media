package com.tokopedia.product.manage.item.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.price.ProductEditWeightLogisticFragment
import com.tokopedia.product.manage.item.price.model.ProductLogistic
import com.tokopedia.product.manage.item.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_IS_FREE_RETURN
import com.tokopedia.product.manage.item.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_LOGISTIC

class ProductEditWeightLogisticActivity : BaseSimpleActivity(){

    companion object {
        fun createIntent(context: Context, productLogistic: ProductLogistic?, isFreeReturn: Boolean): Intent {
            return Intent(context, ProductEditWeightLogisticActivity::class.java)
                    .putExtra(EXTRA_LOGISTIC, productLogistic)
                    .putExtra(EXTRA_IS_FREE_RETURN, isFreeReturn)
        }
    }

    override fun getNewFragment(): Fragment = ProductEditWeightLogisticFragment.createInstance()

    override fun getLayoutRes() = R.layout.activity_product_edit_with_menu
}
