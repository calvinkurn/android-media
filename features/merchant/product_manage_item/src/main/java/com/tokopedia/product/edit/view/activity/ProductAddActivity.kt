package com.tokopedia.product.edit.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.price.model.ProductCatalog
import com.tokopedia.product.edit.price.model.ProductCategory
import com.tokopedia.product.edit.price.model.ProductName
import com.tokopedia.product.edit.util.ProductEditModuleRouter
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_CATALOG
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_CATEGORY
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_IMAGES
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_NAME
import com.tokopedia.product.edit.view.fragment.ProductAddFragment
import com.tokopedia.product.edit.view.presenter.ProductAddImagePresenter
import com.tokopedia.product.edit.view.presenter.ProductAddImageView
import permissions.dispatcher.*

open class ProductAddActivity : BaseProductAddEditActivity(){
    override fun getCancelMessageRes() =  R.string.product_draft_dialog_cancel_message

    override fun needDeleteCacheOnBack() = true

    override fun getNewFragment(): Fragment? {
        val productCatalog = intent.getParcelableExtra<ProductCatalog>(EXTRA_CATALOG)
        val productCategory = intent.getParcelableExtra<ProductCategory>(EXTRA_CATEGORY)
        val productName = intent.getParcelableExtra<ProductName>(EXTRA_NAME)
        val productImages = intent.getStringArrayListExtra(EXTRA_IMAGES)
        return ProductAddFragment.createInstance(productCatalog, productCategory, productName, productImages)
    }


    companion object {
        fun createInstance(context: Context?, productCatalog: ProductCatalog, productCategory: ProductCategory,
                           productName: ProductName, productImages: ArrayList<String>): Intent {
            val intent = Intent(context, ProductAddActivity::class.java)
            intent.putExtra(EXTRA_NAME, productName)
            intent.putExtra(EXTRA_CATALOG, productCatalog)
            intent.putExtra(EXTRA_CATEGORY, productCategory)
            intent.putStringArrayListExtra(EXTRA_IMAGES, productImages)
            return intent
        }
    }
}
