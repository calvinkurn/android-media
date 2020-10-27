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
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.seller.action.R

class SellerFailureSlice(context: Context,
                         sliceUri: Uri,
                         private val titleText: String? = null): SellerSlice(context, sliceUri) {

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun getSlice(): Slice =
            list(context, sliceUri, ListBuilder.INFINITY) {
                header {
                    title = titleText.orEmpty()
                }
                row {
                    setTitleItem(IconCompat.createWithResource(context, R.drawable.ic_seller_action_error), ListBuilder.LARGE_IMAGE)
                    title = context.getString(R.string.seller_action_error_title).parseAsHtml()
                    subtitle = context.getString(R.string.seller_action_error_desc_short).parseAsHtml()
                    primaryAction = createActivityAction()
                }
                setIsError(true)
            }
}