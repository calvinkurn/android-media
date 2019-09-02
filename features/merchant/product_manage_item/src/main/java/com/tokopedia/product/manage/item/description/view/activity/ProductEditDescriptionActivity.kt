package com.tokopedia.product.manage.item.description.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.description.view.fragment.ProductEditDescriptionFragment
import com.tokopedia.product.manage.item.description.view.model.ProductDescription
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_DESCRIPTION
import com.tokopedia.product.manage.item.video.view.fragment.ProductAddVideoFragment.Companion.EXTRA_KEYWORD

class ProductEditDescriptionActivity : BaseSimpleActivity(){

    companion object {
        fun createIntent(context: Context, productDescription: ProductDescription?, keyword: String?): Intent {
            return Intent(context, ProductEditDescriptionActivity::class.java)
                    .putExtra(EXTRA_DESCRIPTION, productDescription)
                    .putExtra(EXTRA_KEYWORD, keyword)
        }
    }

    override fun getNewFragment(): Fragment = ProductEditDescriptionFragment.createInstance()

    override fun getLayoutRes() = R.layout.activity_product_edit_with_menu
}
