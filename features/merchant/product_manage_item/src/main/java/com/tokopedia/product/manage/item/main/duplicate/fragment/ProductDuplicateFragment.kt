package com.tokopedia.product.manage.item.main.duplicate.fragment

import android.os.Bundle
import com.tokopedia.product.manage.item.common.util.ProductStatus
import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel
import com.tokopedia.product.manage.item.main.edit.view.fragment.ProductEditFragment

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
