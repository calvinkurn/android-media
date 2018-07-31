package com.tokopedia.product.edit.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.price.BaseProductEditFragment.Companion.EXTRA_LOGISTIC
import com.tokopedia.product.edit.price.ProductEditWeightLogisticFragment
import com.tokopedia.product.edit.price.model.ProductLogistic

class ProductEditWeightLogisticActivity : BaseSimpleActivity(){

    companion object {
        fun createIntent(context: Context, productLogistic: ProductLogistic): Intent {
            return Intent(context, ProductEditWeightLogisticActivity::class.java).putExtra(EXTRA_LOGISTIC, productLogistic)
        }
    }

    override fun getNewFragment(): Fragment{
        return ProductEditWeightLogisticFragment.createInstance()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_product_edit_with_menu
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
