package com.tokopedia.seller.action.order.domain.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.tokopedia.seller.action.order.domain.model.Order
import com.tokopedia.usecase.coroutines.Result

interface SliceMainOrderListRepository {

    fun getOrderList(sliceUri: Uri): LiveData<Result<Pair<Uri, List<Order>>>>

}