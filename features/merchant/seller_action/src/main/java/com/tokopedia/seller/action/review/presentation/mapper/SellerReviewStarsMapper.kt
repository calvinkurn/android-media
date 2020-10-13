package com.tokopedia.seller.action.review.presentation.mapper

import android.content.Context
import android.net.Uri
import com.tokopedia.seller.action.common.presentation.mapper.SellerActionMapper
import com.tokopedia.seller.action.common.presentation.model.SellerSuccessItem
import com.tokopedia.seller.action.common.presentation.slices.SellerSlice
import com.tokopedia.seller.action.review.domain.model.InboxReviewList
import com.tokopedia.seller.action.review.presentation.slice.SellerReviewSlice

class SellerReviewStarsMapper(context: Context,
                              sliceUri: Uri): SellerActionMapper<InboxReviewList>(context, sliceUri) {

    override fun <T : SellerSuccessItem> getSuccessSlice(itemList: List<T>): SellerSlice {
        return SellerReviewSlice(context, sliceUri, itemList.filterIsInstance(InboxReviewList::class.java))
    }
}