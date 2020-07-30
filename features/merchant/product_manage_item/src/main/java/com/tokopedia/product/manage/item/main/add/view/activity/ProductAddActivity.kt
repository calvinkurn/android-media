package com.tokopedia.product.manage.item.main.add.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.catalog.view.model.ProductCatalog
import com.tokopedia.product.manage.item.category.view.model.ProductCategory
import com.tokopedia.product.manage.item.common.util.AddEditPageType
import com.tokopedia.product.manage.item.name.view.model.ProductName
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditActivity
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_CATALOG
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_CATEGORY
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_IMAGES
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_NAME
import com.tokopedia.product.manage.item.main.add.view.fragment.ProductAddFragment

open class ProductAddActivity : BaseProductAddEditActivity(){
    override var addEditPageType: AddEditPageType = AddEditPageType.ADD

    override fun getCancelMessageRes() =  R.string.product_draft_dialog_cancel_message

    override fun needDeleteCacheOnBack() = true

    override fun getNewFragment(): Fragment {
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
