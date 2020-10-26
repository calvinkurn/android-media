package com.tokopedia.seller.action.common.presentation.slices

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.IconCompat
import androidx.slice.Slice
import androidx.slice.builders.ListBuilder
import androidx.slice.builders.header
import androidx.slice.builders.list
import androidx.slice.builders.row
import com.tokopedia.seller.action.R
import com.tokopedia.seller.action.common.presentation.model.SellerSuccessItem

abstract class SellerSuccessSlice<T : SellerSuccessItem>(private val itemList: List<T>,
                                                         context: Context,
                                                         sliceUri: Uri): SellerSlice(context, sliceUri) {

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    abstract fun getSuccessSlice(): Slice

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun getSlice(): Slice {
        return if (itemList.isEmpty()) {
            getEmptySuccessSlice()
        } else {
            getSuccessSlice()
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun getEmptySuccessSlice(): Slice {
        return list(context, sliceUri, ListBuilder.INFINITY) {
            header {
                title = ""
            }
            row {
                setTitleItem(IconCompat.createWithResource(context, R.drawable.ic_seller_action_empty), ListBuilder.LARGE_IMAGE)
                title = context.getString(R.string.seller_action_order_empty_title)
                subtitle = context.getString(R.string.seller_action_order_empty_desc)
                primaryAction = createActivityAction()
            }
        }
    }

}