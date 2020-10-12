package com.tokopedia.seller.action.order.presentation.presenter

import android.net.Uri
import androidx.lifecycle.LiveData
import com.tokopedia.seller.action.order.domain.model.Order
import com.tokopedia.usecase.coroutines.Result

interface SliceSellerActionPresenter {

    fun getOrderList(sliceUri: Uri): LiveData<Result<Pair<Uri, List<Order>>>>

}