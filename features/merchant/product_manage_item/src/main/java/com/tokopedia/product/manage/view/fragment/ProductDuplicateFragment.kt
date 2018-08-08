package com.tokopedia.product.edit.view.fragment

import android.os.Bundle
import com.tokopedia.product.edit.common.di.component.ProductComponent
import com.tokopedia.product.edit.common.model.edit.ProductViewModel
import com.tokopedia.product.edit.common.util.ProductStatus
import com.tokopedia.product.edit.di.component.DaggerProductEditComponent
import com.tokopedia.product.edit.di.module.ProductEditModule
import com.tokopedia.product.edit.view.presenter.ProductEditView

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
