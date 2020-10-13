package com.tokopedia.seller.action.common.presentation.presenter

import android.net.Uri
import androidx.lifecycle.LiveData
import com.tokopedia.seller.action.order.domain.model.Order
import com.tokopedia.seller.action.order.domain.model.SellerActionOrderType
import com.tokopedia.seller.action.review.domain.model.InboxReviewList
import com.tokopedia.usecase.coroutines.Result

interface SliceSellerActionPresenter {

    fun getOrderList(sliceUri: Uri, @SellerActionOrderType orderType: String): LiveData<Result<Pair<Uri, List<Order>>>>
    fun getShopReviewList(sliceUri: Uri, stars: Int): LiveData<Result<Pair<Uri, List<InboxReviewList>>>>

}