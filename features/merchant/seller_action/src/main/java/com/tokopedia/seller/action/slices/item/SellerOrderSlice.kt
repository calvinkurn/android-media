package com.tokopedia.seller.action.slices.item

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.slice.Slice
import androidx.slice.builders.ListBuilder
import androidx.slice.builders.header
import androidx.slice.builders.list
import androidx.slice.builders.row
import com.tokopedia.seller.action.R

class SellerOrderSlice(context: Context,
                       sliceUri: Uri): SellerSlice(context, sliceUri) {

    //TODO: pass actual list of data from som request

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun getSlice(): Slice =
            list(context, sliceUri, ListBuilder.INFINITY) {
                header {
                    title = context.getString(R.string.seller_action_order_title)
                }
                listOf("a", "b").forEach {
                    row {
                        title = it
                    }
                }
            }
}