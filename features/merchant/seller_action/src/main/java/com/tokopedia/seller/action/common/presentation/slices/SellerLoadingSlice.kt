package com.tokopedia.seller.action.common.presentation.slices

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.slice.Slice
import androidx.slice.builders.ListBuilder
import androidx.slice.builders.header
import androidx.slice.builders.list
import com.tokopedia.seller.action.R

class SellerLoadingSlice(context: Context,
                         sliceUri: Uri): SellerSlice(context, sliceUri) {

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun getSlice(): Slice =
            list(context, sliceUri, ListBuilder.INFINITY) {
                header {
                    title = context.getString(R.string.seller_action_loading)
                }
            }

}