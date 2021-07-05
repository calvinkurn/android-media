package com.tokopedia.seller.action.common.interfaces

import android.net.Uri
import com.tokopedia.seller.action.common.presentation.slices.SellerSlice
import com.tokopedia.seller.action.order.domain.model.Order
import kotlin.collections.HashMap

class SellerActionContract {

    interface View: BaseSellerActionView<Presenter> {
        fun onSuccessGetOrderList(sliceUri: Uri, orderList: List<Order>)
        fun onErrorGetOrderList(sliceUri: Uri)
    }

    interface Presenter: BaseSellerActionPresenter {
        fun getOrderList(sliceUri: Uri, date: String?, sliceHashMap: HashMap<Uri, SellerSlice?>)
    }
}