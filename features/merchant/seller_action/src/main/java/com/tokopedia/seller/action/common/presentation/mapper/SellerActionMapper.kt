package com.tokopedia.seller.action.common.presentation.mapper

import android.content.Context
import android.net.Uri
import com.tokopedia.seller.action.common.presentation.model.SellerActionStatus
import com.tokopedia.seller.action.common.presentation.model.SellerSuccessItem
import com.tokopedia.seller.action.common.presentation.slices.SellerFailureSlice
import com.tokopedia.seller.action.common.presentation.slices.SellerLoadingSlice
import com.tokopedia.seller.action.common.presentation.slices.SellerNotLoginSlice
import com.tokopedia.seller.action.common.presentation.slices.SellerSlice

abstract class SellerActionMapper<T : SellerSuccessItem>(protected val context: Context,
                                                         protected val sliceUri: Uri,
                                                         private val titleText: String? = null) {

    abstract fun <T : SellerSuccessItem> getSuccessSlice(itemList: List<T>): SellerSlice

    fun getSlice(status: SellerActionStatus?): SellerSlice? {
        return when(status) {
            is SellerActionStatus.Success -> getSuccessSlice(status.itemList)
            is SellerActionStatus.Fail -> getFailureSlice()
            is SellerActionStatus.Loading -> getLoadingSlice()
            is SellerActionStatus.NotLogin -> getNotLoginSlice()
            else -> null
        }
    }

    protected fun getLoadingSlice(): SellerSlice {
        return SellerLoadingSlice(context, sliceUri, titleText)
    }

    protected fun getNotLoginSlice(): SellerSlice {
        return SellerNotLoginSlice(context, sliceUri, titleText)
    }

    protected fun getFailureSlice(): SellerSlice {
        return SellerFailureSlice(context, sliceUri, titleText)
    }

}