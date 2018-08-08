package com.tokopedia.product.manage.item.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.product.manage.item.common.model.edit.ProductViewModel
import com.tokopedia.product.manage.item.common.util.ProductStatus

class ProductDraftAddFragment : ProductDraftEditFragment() {

    override var statusUpload = ProductStatus.ADD

    override fun onSuccessLoadProduct(model: ProductViewModel?) {
        // bug fix when instagram draft from old version that does not have name editable attribute.
        model?.setProductNameEditable(true)
        super.onSuccessLoadProduct(model)
    }

    companion object {
        fun createInstance(draftProductId: Long): Fragment {
            val fragment = ProductDraftAddFragment()
            val args = Bundle()
            args.putLong(DRAFT_PRODUCT_ID, draftProductId)
            fragment.arguments = args
            return fragment
        }
    }

}