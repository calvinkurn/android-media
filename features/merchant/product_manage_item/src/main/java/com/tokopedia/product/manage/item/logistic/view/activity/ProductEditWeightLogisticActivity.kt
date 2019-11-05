package com.tokopedia.product.manage.item.logistic.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.logistic.view.fragment.ProductEditWeightLogisticFragment
import com.tokopedia.product.manage.item.logistic.view.model.ProductLogistic
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_IS_FREE_RETURN
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_LOGISTIC

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
