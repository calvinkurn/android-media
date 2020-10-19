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
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.seller.action.R
import com.tokopedia.seller.action.common.presentation.slices.SellerSuccessSlice
import com.tokopedia.seller.action.order.domain.model.Order

class SellerOrderSlice(context: Context,
                       sliceUri: Uri,
                       private val orderList: List<Order>): SellerSuccessSlice<Order>(orderList, context, sliceUri) {

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun getSuccessSlice(): Slice =
            list(context, sliceUri, ListBuilder.INFINITY) {
                header {
                    title = context.getString(R.string.seller_action_order_title)
                    primaryAction = createHeaderPrimaryAction()
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
                seeMoreRow {
                    title = context.getString(R.string.seller_action_order_see_all)
                    primaryAction = createSeeMorePrimaryAction()
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

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun createSeeMorePrimaryAction(): SliceAction{
        RouteManager.getIntent(context, ApplinkConstInternalSellerapp.SELLER_HOME_SOM_ALL).let { intent ->
            PendingIntent.getActivity(context, 0, intent, 0).let { pendingIntent ->
                return SliceAction.create(
                        pendingIntent,
                        IconCompat.createWithResource(context, R.drawable.ic_sellerapp_slice),
                        ListBuilder.ICON_IMAGE,
                        context.getString(R.string.seller_action_order_title)
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun createHeaderPrimaryAction(): SliceAction{
        RouteManager.getIntent(context, ApplinkConstInternalSellerapp.SELLER_HOME_SOM_ALL).let { intent ->
            PendingIntent.getActivity(context, 0, intent, 0).let { pendingIntent ->
                return SliceAction.create(
                        pendingIntent,
                        IconCompat.createWithResource(context, R.drawable.ic_sellerapp_slice),
                        ListBuilder.ICON_IMAGE,
                        context.getString(R.string.seller_action_order_title)
                )
            }
        }
    }

    private fun String.getBitmap(): Bitmap? =
            Glide.with(context)
                    .asBitmap()
                    .load(this)
                    .submit()
                    .get()
}