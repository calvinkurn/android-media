package com.tokopedia.product.edit.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.edit.price.ProductEditNameFragment
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.price.model.ProductName
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_IS_EDITABLE_NAME
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_NAME

class ProductEditNameActivity : BaseSimpleActivity(){

    companion object {
        fun createIntent(context: Context, productName: ProductName, isEditableName: Boolean): Intent {
            return Intent(context, ProductEditNameActivity::class.java)
                    .putExtra(EXTRA_NAME, productName)
                    .putExtra(EXTRA_IS_EDITABLE_NAME, isEditableName)
        }
    }

    override fun getNewFragment(): Fragment{
        return ProductEditNameFragment.createInstance()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_product_edit_with_menu
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
