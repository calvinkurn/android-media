package com.tokopedia.seller.action.slices.item

import android.app.PendingIntent
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.IconCompat
import androidx.slice.Slice
import androidx.slice.builders.ListBuilder
import androidx.slice.builders.SliceAction
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.seller.action.R

abstract class SellerSlice(val context: Context, protected val sliceUri: Uri) {

    abstract fun getSlice(): Slice

    protected fun refresh() {
        context.contentResolver.notifyChange(sliceUri, null)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    protected fun createActivityAction(): SliceAction {
        val intent = RouteManager.getIntent(context, ApplinkConst.SELLER_ORDER_DETAIL)
        // TODO: Add deeplink intent to go to seller detail
        return SliceAction.create(
                PendingIntent.getActivity(context, 0, intent, 0),
                IconCompat.createWithResource(context, R.drawable.ic_sellerapp_launcher),
                ListBuilder.SMALL_IMAGE,
                context.getString(R.string.seller_action_open_app)
        )
    }
}