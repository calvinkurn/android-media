package com.tokopedia.seller.action.slices.item

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.IconCompat
import androidx.slice.Slice
import androidx.slice.builders.*
import androidx.slice.builders.ListBuilder.ICON_IMAGE
import com.bumptech.glide.Glide
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.seller.action.R
import com.tokopedia.seller.action.data.model.SellerActionOrderList

class SellerOrderSlice(context: Context,
                       sliceUri: Uri,
                       private val orderList: List<SellerActionOrderList.Data.OrderList.Order>): SellerSlice(context, sliceUri) {

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun getSlice(): Slice =
            list(context, sliceUri, ListBuilder.INFINITY) {
                header {
                    title = context.getString(R.string.seller_action_order_title)
                }
                orderList.forEach {
                    row {
                        // Todo: Create applink for som detail
                        val pendingIntent =
                                RouteManager.getIntent(context, ApplinkConstInternalSellerapp.SELLER_HOME_SOM_ALL).apply {
                                    putExtra("order_id", it.orderId)
                                }.let { intent ->
                                    PendingIntent.getActivity(
                                            context,
                                            0,
                                            intent,
                                            0
                                    )
                                }
                        primaryAction = createPrimaryAction(
                                pendingIntent,
                                it.listOrderProduct.firstOrNull()?.pictureUrl.orEmpty(),
                                it.buyerName)
                        title = it.buyerName
                        subtitle = it.status
                    }
                }
            }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun createPrimaryAction(pendingIntent: PendingIntent, imageUri: String, title: String): SliceAction =
            SliceAction.create(
                    pendingIntent,
                    IconCompat.createWithBitmap(imageUri.getBitmap()),
                    ICON_IMAGE,
                    title
            )

    private fun String.getBitmap(): Bitmap? =
            Glide.with(context)
                    .asBitmap()
                    .load(this)
                    .submit()
                    .get()
}