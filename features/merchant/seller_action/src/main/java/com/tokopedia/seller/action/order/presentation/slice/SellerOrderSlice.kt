package com.tokopedia.seller.action.order.presentation.slice

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
import androidx.slice.builders.ListBuilder.SMALL_IMAGE
import com.bumptech.glide.Glide
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.seller.action.R
import com.tokopedia.seller.action.order.domain.model.Order
import com.tokopedia.seller.action.common.presentation.slices.SellerSuccessSlice

class SellerOrderSlice(context: Context,
                       sliceUri: Uri,
                       private val orderList: List<Order>): SellerSuccessSlice<Order>(orderList, context, sliceUri) {

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun getSuccessSlice(): Slice =
            list(context, sliceUri, ListBuilder.INFINITY) {
                header {
                    title = context.getString(R.string.seller_action_order_title)
                }
                orderList.forEach {
                    row {
                        val pendingIntent =
                                RouteManager.getIntent(context, ApplinkConstInternalOrder.ORDER_DETAIL, it.orderId)?.let { intent ->
                                    PendingIntent.getActivity(
                                            context,
                                            0,
                                            intent,
                                            0
                                    )
                                }
                        pendingIntent?.let { intent ->
                            primaryAction = createPrimaryAction(
                                    intent,
                                    it.listOrderProduct.firstOrNull()?.pictureUrl.orEmpty(),
                                    it.buyerName)
                        }
                        setTitleItem(IconCompat.createWithBitmap(it.listOrderProduct.firstOrNull()?.pictureUrl.orEmpty().getBitmap()), SMALL_IMAGE)
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