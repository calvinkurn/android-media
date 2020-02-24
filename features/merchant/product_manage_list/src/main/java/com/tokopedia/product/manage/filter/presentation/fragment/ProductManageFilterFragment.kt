package com.tokopedia.product.manage.filter.presentation.fragment

import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.common.di.DaggerProductManageComponent
import com.tokopedia.product.manage.common.di.ProductManageModule
import com.tokopedia.product.manage.filter.di.DaggerProductManageFilterComponent
import com.tokopedia.product.manage.filter.di.ProductManageFilterComponent
import com.tokopedia.product.manage.filter.di.ProductManageFilterModule
import com.tokopedia.product.manage.filter.presentation.viewmodel.ProductManageFilterViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ProductManageFilterFragment : BaseDaggerFragment(), HasComponent<ProductManageFilterComponent>{

    companion object {
        fun createInstance() {
            ProductManageFilterFragment()
        }
    }

    @Inject
    lateinit var productManageFilterViewModel: ProductManageFilterViewModel

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun getComponent(): ProductManageFilterComponent? {
        return activity?.run {
            DaggerProductManageFilterComponent
                    .builder()
                    .productManageFilterModule(ProductManageFilterModule())
                    .productManageComponent(ProductManageInstance.getComponent(application))
                    .build()
        }
    }

    private fun observeProductListMeta() {
        productManageFilterViewModel.productListMetaData.observe(this, Observer {
            when(it) {
                is Success -> {

                }
                is Fail -> {
                    showErrorMessage()
                }
            }
        })
    }

    private fun showErrorMessage() {

    }
}