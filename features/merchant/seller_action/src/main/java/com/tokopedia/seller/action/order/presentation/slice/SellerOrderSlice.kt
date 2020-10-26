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
import com.tokopedia.kotlin.extensions.convertToDate
import com.tokopedia.seller.action.R
import com.tokopedia.seller.action.SellerActionActivity
import com.tokopedia.seller.action.common.const.SellerActionConst
import com.tokopedia.seller.action.common.const.SellerActionFeatureName
import com.tokopedia.seller.action.common.presentation.slices.SellerSuccessSlice
import com.tokopedia.seller.action.common.utils.SellerActionUtils.isOrderDateToday
import com.tokopedia.seller.action.order.domain.model.Order
import com.tokopedia.seller.action.order.domain.model.SellerActionOrderCode
import java.util.*

class SellerOrderSlice(context: Context,
                       sliceUri: Uri,
                       private val date: String? = null,
                       private val orderList: List<Order>): SellerSuccessSlice<Order>(orderList, context, sliceUri) {

    companion object {
        private const val MAX_PRODUCT_NAME_LENGTH = 15
        private const val TRUNCATED_PRODUCT_NAME_LENGTH = 12
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun getSuccessSlice(): Slice =
            list(context, sliceUri, ListBuilder.INFINITY) {
                header {
                    title =
                            if (date == null || date.isOrderDateToday()) {
                                context.getString(R.string.seller_action_order_success_title_today)
                            }
                            else {
                                val formattedDate = date.convertToDate(SellerActionConst.SLICE_DATE_FORMAT, Locale("in", "ID"))
                                context.getString(R.string.seller_action_order_success_title_specific_date, formattedDate)
                            }
                }
                orderList.forEach {
                    row {
                        val pendingIntent = SellerActionActivity.createOrderDetailIntent(context, it.orderId).let { intent ->
                            PendingIntent.getActivity(context, 0, intent, 0)
                        }
                        pendingIntent?.let { intent ->
                            primaryAction = createOrderDetailPrimaryAction(
                                    intent,
                                    it.listOrderProduct.firstOrNull()?.pictureUrl.orEmpty(),
                                    it.buyerName)
                        }
                        setTitleItem(IconCompat.createWithBitmap(it.listOrderProduct.firstOrNull()?.pictureUrl.orEmpty().getBitmap()), SMALL_IMAGE)
                        title = it.listOrderProduct.let { productList ->
                            val displayedName = productList.firstOrNull()?.productName.orEmpty()
                            if (productList.size <= 1) {
                                displayedName
                            } else {
                                with(displayedName) {
                                    val truncatedName =
                                            if (length > MAX_PRODUCT_NAME_LENGTH) {
                                                displayedName.take(TRUNCATED_PRODUCT_NAME_LENGTH).let {
                                                    it + context.getString(R.string.seller_action_triple_dot)
                                                }
                                            } else {
                                                displayedName
                                            }
                                    context.getString(R.string.seller_action_order_success_item_multiple_title, truncatedName, productList.size - 1)
                                }
                            }
                        }
                        subtitle = (it.orderStatusId.mapToStatus() ?: it.status).let { status ->
                            context.getString(R.string.seller_action_order_success_item_desc, status, it.deadlineText)
                        }
                    }
                }
            }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun createActivityAction(): SliceAction {
        SellerActionActivity.createActionIntent(context, SellerActionFeatureName.ALL_ORDER).let { intent ->
            PendingIntent.getActivity(context, 0, intent, 0)
        }.let { pendingIntent ->
            return SliceAction.create(
                    pendingIntent,
                    IconCompat.createWithResource(context, R.drawable.ic_sellerapp_slice),
                    ICON_IMAGE,
                    context.getString(R.string.seller_action_open_app)
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun createOrderDetailPrimaryAction(pendingIntent: PendingIntent, imageUri: String, title: String): SliceAction =
            SliceAction.create(
                    pendingIntent,
                    IconCompat.createWithBitmap(imageUri.getBitmap()),
                    ICON_IMAGE,
                    title
            )

    private fun String.getBitmap(): Bitmap? =
            Glide.with(context)
                    .asBitmap()
                    .placeholder(R.drawable.ic_sellerapp_slice)
                    .load(this)
                    .submit()
                    .get()

    /**
     * Map order code to its writing manually
     */
    private fun Int.mapToStatus(): String? {
        return when(this) {
            SellerActionOrderCode.STATUS_CODE_ORDER_CREATED -> context.getString(R.string.seller_action_order_status_new_order)
            SellerActionOrderCode.STATUS_CODE_ORDER_CONFIRMED, SellerActionOrderCode.STATUS_CODE_ORDER_CONFIRMED_2 -> context.getString(R.string.seller_action_order_status_ready_to_ship)
            else -> null
        }
    }
}