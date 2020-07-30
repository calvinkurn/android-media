package com.tokopedia.product.manage.item.main.add.view.fragment

import android.os.Bundle
import com.tokopedia.product.manage.item.common.di.component.ProductComponent
import com.tokopedia.product.manage.item.common.util.ProductStatus
import com.tokopedia.product.manage.item.catalog.view.model.ProductCatalog
import com.tokopedia.product.manage.item.category.view.model.ProductCategory
import com.tokopedia.product.manage.item.common.util.AddEditPageType
import com.tokopedia.product.manage.item.main.add.di.DaggerProductAddComponent
import com.tokopedia.product.manage.item.main.add.di.ProductAddModule
import com.tokopedia.product.manage.item.main.add.view.listener.ProductAddView
import com.tokopedia.product.manage.item.main.add.view.presenter.ProductAddPresenterImpl
import com.tokopedia.product.manage.item.name.view.model.ProductName
import com.tokopedia.product.manage.item.utils.convertImageListResult
import com.tokopedia.product.manage.item.utils.isFilledAny
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment
import com.tokopedia.product.manage.item.main.base.view.model.ProductAddViewModel
import java.util.ArrayList

class ProductAddFragment : BaseProductAddEditFragment<ProductAddPresenterImpl<ProductAddView>, ProductAddView>() {
    override var addEditPageType: AddEditPageType = AddEditPageType.ADD
    override var statusUpload: Int = ProductStatus.ADD

    override fun initInjector() {
        DaggerProductAddComponent
                .builder()
                .productAddModule(ProductAddModule())
                .productComponent(getComponent(ProductComponent::class.java))
                .build()
                .inject(this)
        presenter.attachView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
        if(currentProductAddViewModel == null){
            currentProductAddViewModel = ProductAddViewModel()
        }
        currentProductAddViewModel?.let {
            it.productCatalog = arguments?.get(EXTRA_CATALOG) as ProductCatalog?
            it.productCategory = arguments?.get(EXTRA_CATEGORY) as ProductCategory?
            it.productName = arguments?.get(EXTRA_NAME) as ProductName?
            it.productPictureList = arguments?.getStringArrayList(EXTRA_IMAGES)?.convertImageListResult()
        }

    }

    companion object {
        fun createInstance(productCatalog: ProductCatalog?, productCategory: ProductCategory?, productName: ProductName?, productImages: ArrayList<String>?) : ProductAddFragment {
            val productAddFragment = ProductAddFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_CATALOG, productCatalog)
            bundle.putParcelable(EXTRA_NAME, productName)
            bundle.putParcelable(EXTRA_CATEGORY, productCategory)
            bundle.putStringArrayList(EXTRA_IMAGES, productImages)
            productAddFragment.arguments =bundle
            return productAddFragment
        }
    }

    override fun showDialogSaveDraftOnBack(): Boolean {
        // check if this fragment has any data
        // will compare will the default value and the current value
        // if there is the difference, then assume that the data has been added.
        // will be overriden when not adding product
        return currentProductAddViewModel?.isFilledAny()?:false
    }
}