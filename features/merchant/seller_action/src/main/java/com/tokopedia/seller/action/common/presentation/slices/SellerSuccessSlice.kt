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
import com.tokopedia.seller.action.common.utils.SellerActionUtils.getBitmap
import com.tokopedia.seller.action.order.const.SellerActionOrderIconUrl

abstract class SellerSuccessSlice<T : SellerSuccessItem>(private val itemList: List<T>,
                                                         context: Context,
                                                         sliceUri: Uri,
                                                         private val titleText: String? = null): SellerSlice(context, sliceUri) {

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
                title = titleText.orEmpty()
            }
            row {
                setTitleItem(IconCompat.createWithBitmap(SellerActionOrderIconUrl.Empty.getBitmap(context)), ListBuilder.SMALL_IMAGE)
                title = context.getString(R.string.seller_action_order_empty_title)
                subtitle = context.getString(R.string.seller_action_order_empty_desc_short)
                primaryAction = createActivityAction()
            }
        }
    }

}