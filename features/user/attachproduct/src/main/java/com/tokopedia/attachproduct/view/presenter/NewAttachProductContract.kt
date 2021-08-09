package com.tokopedia.attachproduct.view.presenter

import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.attachproduct.view.uimodel.NewAttachProductItemUiModel
import java.util.*
//
///**
// * Created by Hendri on 19/02/18.
// */
//
interface NewAttachProductContract {
    interface View {
        fun addProductToList(productNews: List<NewAttachProductItemUiModel>, hasNextPage: Boolean)
        fun hideAllLoadingIndicator()
        fun showErrorMessage(throwable: Throwable)
        fun updateButtonBasedOnChecked(checkedCount: Int)
        fun setShopName(shopName: String)
    }

    interface Activity {
        val isSeller: Boolean
        val shopId: String
        fun finishActivityWithResult(products: ArrayList<ResultProduct>)
        fun goToAddProduct(shopId: String)
        fun setShopName(shopName: String)
    }

    interface Presenter {
        fun loadProductData(query: String, shopId: String, page: Int, warehouseId: String)
        fun updateCheckedList(productNews: List<NewAttachProductItemUiModel>, onUpdated: (Int) -> Unit)
        fun resetCheckedList()
        fun completeSelection(onFinish: (ArrayList<ResultProduct>) -> Unit)
        fun attachView(view: View?)
        fun attachActivityContract(activityContract: Activity?)
        fun detachView()
    }
}