package com.tokopedia.seller.action.slices.item

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.slice.Slice
import androidx.slice.builders.ListBuilder
import androidx.slice.builders.list
import androidx.slice.builders.row
import com.tokopedia.seller.action.R

class SellerUnknownSlice(context: Context,
                         sliceUri: Uri): SellerSlice(context, sliceUri) {

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun getSlice(): Slice =
            list(context, sliceUri, ListBuilder.INFINITY) {
                row {
                    title = context.getString(R.string.seller_action_not_found)
                    primaryAction = createActivityAction()
                }
                setIsError(true)
            }
}