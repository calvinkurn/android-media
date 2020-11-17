package com.tokopedia.seller.action.common.presentation.slices

import android.app.PendingIntent
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.IconCompat
import androidx.slice.Slice
import androidx.slice.builders.ListBuilder
import androidx.slice.builders.SliceAction
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.seller.action.R
import com.tokopedia.seller.action.common.const.SellerActionConst

abstract class SellerSlice(protected val context: Context, protected val sliceUri: Uri) {

    abstract fun getSlice(): Slice

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    open fun createActivityAction(): SliceAction {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalSellerapp.SELLER_HOME)
        return SliceAction.create(
                PendingIntent.getActivity(context, SellerActionConst.MAIN_REQUEST_CODE, intent, 0),
                IconCompat.createWithResource(context, R.drawable.ic_sellerapp_slice),
                ListBuilder.SMALL_IMAGE,
                context.getString(R.string.seller_action_open_app)
        )
    }
}