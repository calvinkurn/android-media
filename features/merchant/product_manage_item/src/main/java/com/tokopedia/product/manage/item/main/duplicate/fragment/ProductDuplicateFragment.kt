package com.tokopedia.product.manage.item.view.fragment

import android.os.Bundle
import com.tokopedia.product.manage.item.common.di.component.ProductComponent
import com.tokopedia.product.manage.item.common.model.edit.ProductViewModel
import com.tokopedia.product.manage.item.common.util.ProductStatus
import com.tokopedia.product.manage.item.di.component.DaggerProductEditComponent
import com.tokopedia.product.manage.item.di.module.ProductEditModule
import com.tokopedia.product.manage.item.view.presenter.ProductEditView

/**
 * Created by zulfikarrahman on 4/27/17.
 */

class ProductDuplicateFragment : ProductEditFragment(){

    override var statusUpload: Int = ProductStatus.ADD

    override fun onSuccessLoadProduct(model: ProductViewModel?) {
        model?.setProductId(null)
        model?.setProductNameEditable(true)
        super.onSuccessLoadProduct(model)
    }

    companion object {
        fun createInstance(productId: String): ProductDuplicateFragment {
            val fragment = ProductDuplicateFragment()
            val args = Bundle()
            args.putString(EDIT_PRODUCT_ID, productId)
            fragment.arguments = args
            return fragment
        }
    }

}
