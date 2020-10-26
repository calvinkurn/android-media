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

class SellerFailureSlice(context: Context,
                         sliceUri: Uri): SellerSlice(context, sliceUri) {

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun getSlice(): Slice =
            list(context, sliceUri, ListBuilder.INFINITY) {
                header {
                    title = ""
                }
                row {
                    setTitleItem(IconCompat.createWithResource(context, R.drawable.ic_seller_action_error), ListBuilder.LARGE_IMAGE)
                    title = context.getString(R.string.seller_action_error_title)
                    subtitle = context.getString(R.string.seller_action_error_desc)
                    primaryAction = createActivityAction()
                }
                setIsError(true)
            }
}