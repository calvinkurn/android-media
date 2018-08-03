package com.tokopedia.product.edit.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.price.ProductEditWeightLogisticFragment
import com.tokopedia.product.edit.price.model.ProductLogistic
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_IS_FREE_RETURN
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_LOGISTIC

class ProductEditWeightLogisticActivity : BaseSimpleActivity(){

    companion object {
        fun createIntent(context: Context, productLogistic: ProductLogistic?, isFreeReturn: Boolean): Intent {
            return Intent(context, ProductEditWeightLogisticActivity::class.java)
                    .putExtra(EXTRA_LOGISTIC, productLogistic)
                    .putExtra(EXTRA_IS_FREE_RETURN, isFreeReturn)
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
