package com.tokopedia.product.manage.item.main.draft.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.product.manage.item.common.util.AddEditPageType
import com.tokopedia.product.manage.item.common.util.ProductStatus
import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel

class ProductDraftAddFragment : ProductDraftEditFragment() {

    override var addEditPageType: AddEditPageType = AddEditPageType.DRAFT_ADD
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