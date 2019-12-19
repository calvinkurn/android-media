package com.tokopedia.product.manage.item.name.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.manage.item.name.view.fragment.ProductEditNameFragment
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.name.view.model.ProductName
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_IS_EDITABLE_NAME
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_NAME

class ProductEditNameActivity : BaseSimpleActivity(){

    companion object {
        fun createIntent(context: Context, productName: ProductName?, isEditableName: Boolean): Intent {
            return Intent(context, ProductEditNameActivity::class.java)
                    .putExtra(EXTRA_NAME, productName)
                    .putExtra(EXTRA_IS_EDITABLE_NAME, isEditableName)
        }
    }

    override fun getNewFragment(): Fragment = ProductEditNameFragment.createInstance()

    override fun getLayoutRes() = R.layout.activity_product_edit_with_menu
}
