package com.tokopedia.product.edit.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.price.ProductEditDescriptionFragment
import com.tokopedia.product.edit.price.model.ProductDescription
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_DESCRIPTION
import com.tokopedia.product.edit.view.fragment.ProductAddVideoFragment.Companion.EXTRA_KEYWORD

class ProductEditDescriptionActivity : BaseSimpleActivity(){

    companion object {
        fun createIntent(context: Context, productDescription: ProductDescription?, keyword: String?): Intent {
            return Intent(context, ProductEditDescriptionActivity::class.java)
                    .putExtra(EXTRA_DESCRIPTION, productDescription)
                    .putExtra(EXTRA_KEYWORD, keyword)
        }
    }

    override fun getNewFragment(): Fragment{
        return ProductEditDescriptionFragment.createInstance()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_product_edit_with_menu
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
