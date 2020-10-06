package com.tokopedia.seller.action.slices

import android.content.Context
import android.net.Uri
import androidx.slice.Slice

abstract class SellerSlice(val context: Context, private val sliceUri: Uri) {

    abstract fun getSlice(): Slice

    protected fun refresh() {
        context.contentResolver.notifyChange(sliceUri, null)
    }

//    protected fun createActivityAction(): SliceAction {
//        val intent = RouteManager.getIntent(context, ApplinkConst.SELLER_ORDER_DETAIL)
//        // TODO: Add deeplink intent to go to seller detail
//        return SliceAction.create(
//                PendingIntent.getActivity(context, 0, intent, 0),
//
//        )
//    }
}