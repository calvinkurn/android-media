package com.tokopedia.product.edit.view.fragment

import android.os.Bundle
import com.tokopedia.product.edit.common.di.component.ProductComponent
import com.tokopedia.product.edit.common.util.ProductStatus
import com.tokopedia.product.edit.di.component.DaggerProductAddComponent
import com.tokopedia.product.edit.di.module.ProductAddModule
import com.tokopedia.product.edit.price.model.ProductCatalog
import com.tokopedia.product.edit.price.model.ProductCategory
import com.tokopedia.product.edit.price.model.ProductName
import com.tokopedia.product.edit.utils.convertImageListResult
import com.tokopedia.product.edit.utils.isFilledAny
import com.tokopedia.product.edit.view.listener.ProductAddView
import com.tokopedia.product.edit.view.model.ProductAddViewModel
import com.tokopedia.product.edit.view.presenter.ProductAddPresenterImpl
import java.util.ArrayList

class ProductAddFragment : BaseProductAddEditFragment<ProductAddPresenterImpl<ProductAddView>, ProductAddView>() {
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
        currentProductAddViewModel?.productCatalog = arguments?.get(EXTRA_CATALOG) as ProductCatalog?
        currentProductAddViewModel?.productCategory = arguments?.get(EXTRA_CATEGORY) as ProductCategory?
        currentProductAddViewModel?.productName = arguments?.get(EXTRA_NAME) as ProductName?
        currentProductAddViewModel?.productPictureList = arguments?.getStringArrayList(EXTRA_IMAGES)?.convertImageListResult()
    }

    companion object {
        fun createInstance(productCatalog: ProductCatalog?, productCategory: ProductCategory?, productName: ProductName?, productImages: ArrayList<String>?) : ProductAddFragment{
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