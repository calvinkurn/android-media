package com.tokopedia.product.manage.item.category.view.fragment

import android.os.Bundle
import com.tokopedia.core.common.category.di.module.CategoryPickerModule
import com.tokopedia.product.manage.item.common.di.component.ProductComponent
import com.tokopedia.product.manage.item.category.di.ProductEditCategoryCatalogModule
import com.tokopedia.product.manage.item.catalog.view.model.ProductCatalog
import com.tokopedia.product.manage.item.category.di.DaggerProductEditCategoryCatalogComponent
import com.tokopedia.product.manage.item.category.view.model.ProductCategory
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_CATALOG
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_CATEGORY
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_CATEGORY_LOCKED
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_NAME


class ProductEditCategoryFragment : BaseProductEditCategoryFragment() {

    override fun initInjector() {
        DaggerProductEditCategoryCatalogComponent.builder()
                .productComponent(getComponent(ProductComponent::class.java))
                .categoryPickerModule(CategoryPickerModule(requireActivity().applicationContext))
                .productEditCategoryCatalogModule(ProductEditCategoryCatalogModule())
                .build()
                .inject(this)
        presenter.attachView(this)
    }

    companion object {
        fun createInstance(productName: String, category: ProductCategory?, catalog: ProductCatalog?, isCategoryLocked : Boolean) =
                ProductEditCategoryFragment().apply {
                    arguments = Bundle().apply {
                        putString(EXTRA_NAME, productName)
                        putBoolean(EXTRA_CATEGORY_LOCKED, isCategoryLocked)
                        putParcelable(EXTRA_CATALOG, catalog ?: ProductCatalog())
                        putParcelable(EXTRA_CATEGORY, category ?: ProductCategory())
                }
        }
    }

}
