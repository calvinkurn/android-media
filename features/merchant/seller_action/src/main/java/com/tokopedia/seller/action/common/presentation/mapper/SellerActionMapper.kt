package com.tokopedia.seller.action.common.presentation.mapper

import android.content.Context
import android.net.Uri
import com.tokopedia.seller.action.common.presentation.model.SellerSuccessItem
import com.tokopedia.seller.action.common.presentation.slices.SellerFailureSlice
import com.tokopedia.seller.action.common.presentation.slices.SellerLoadingSlice
import com.tokopedia.seller.action.common.presentation.slices.SellerNotLoginSlice
import com.tokopedia.seller.action.common.presentation.slices.SellerSlice
import com.tokopedia.seller.action.common.presentation.model.SellerActionStatus

abstract class SellerActionMapper<T : SellerSuccessItem>(protected val context: Context,
                                                         protected val sliceUri: Uri) {

    abstract fun <T : SellerSuccessItem> getSuccessSlice(itemList: List<T>): SellerSlice

    fun getSlice(status: SellerActionStatus): SellerSlice {
        return when(status) {
            is SellerActionStatus.Success -> getSuccessSlice(status.itemList)
            is SellerActionStatus.Fail -> getFailureSlice()
            is SellerActionStatus.Loading -> getLoadingSlice()
            is SellerActionStatus.NotLogin -> getNotLoginSlice()
        }
    }

    protected fun getLoadingSlice(): SellerSlice {
        return SellerLoadingSlice(context, sliceUri)
    }

    protected fun getNotLoginSlice(): SellerSlice {
        return SellerNotLoginSlice(context, sliceUri)
    }

    protected fun getFailureSlice(): SellerSlice {
        return SellerFailureSlice(context, sliceUri)
    }

}