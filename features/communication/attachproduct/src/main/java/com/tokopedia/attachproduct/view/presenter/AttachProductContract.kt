package com.tokopedia.attachproduct.view.presenter

import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.attachproduct.view.uimodel.AttachProductItemUiModel
import java.util.*

interface AttachProductContract {
    interface View {
        fun addProductToList(products: List<AttachProductItemUiModel>, hasNextPage: Boolean)
        fun hideAllLoadingIndicator()
        fun showErrorMessage(throwable: Throwable)
        fun updateButtonBasedOnChecked(checkedCount: Int)
    }

    interface Activity {
        fun finishActivityWithResult(products: ArrayList<ResultProduct>)
        fun goToAddProduct(shopId: String)
    }
}
