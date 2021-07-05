package com.tokopedia.seller.action.order.presentation.slice

import android.app.PendingIntent
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.IconCompat
import androidx.slice.Slice
import androidx.slice.builders.*
import androidx.slice.builders.ListBuilder.ICON_IMAGE
import androidx.slice.builders.ListBuilder.SMALL_IMAGE
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.seller.action.R
import com.tokopedia.seller.action.SellerActionActivity
import com.tokopedia.seller.action.common.const.SellerActionFeatureName
import com.tokopedia.seller.action.common.presentation.slices.SellerSuccessSlice
import com.tokopedia.seller.action.common.utils.SellerActionUtils.convertToOrderDateTitle
import com.tokopedia.seller.action.common.utils.SellerActionUtils.getBitmap
import com.tokopedia.seller.action.order.domain.model.Order
import com.tokopedia.seller.action.order.domain.model.SellerActionOrderCode

class SellerOrderSlice(context: Context,
                       sliceUri: Uri,
                       private val date: String? = null,
                       private val orderList: List<Order>)
    : SellerSuccessSlice<Order>(orderList, context, sliceUri, date.convertToOrderDateTitle(context)) {

    companion object {
        private const val MAX_PRODUCT_NAME_LENGTH = 25
        private const val TRUNCATED_PRODUCT_NAME_LENGTH = 22
        private const val MAX_ORDER_LIST_SIZE = 3

        private const val LIST_REQUEST_CODE = 9232

        private val DETAIL_REQUEST_CODES = arrayOf(9233, 9234, 9235)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun getSuccessSlice(): Slice =
            list(context, sliceUri, ListBuilder.INFINITY) {
                header {
                    title = date.convertToOrderDateTitle(context)
                    primaryAction = createActivityAction()
                }
                var numberOfOrder = 0
                for ((orderId, status, orderStatusId, deadlineText, orderDate, buyerName, listOrderProduct) in orderList) {
                    if (numberOfOrder >= MAX_ORDER_LIST_SIZE) {
                        break
                    }
                    row {
                        val pendingIntent = SellerActionActivity.createOrderDetailIntent(context, orderId).let { intent ->
                            PendingIntent.getActivity(context, DETAIL_REQUEST_CODES.getOrNull(numberOfOrder).orZero(), intent, 0)
                        }
                        pendingIntent?.let { intent ->
                            primaryAction = createOrderDetailPrimaryAction(
                                    intent,
                                    listOrderProduct.firstOrNull()?.pictureUrl.orEmpty(),
                                    buyerName)
                        }
                        setTitleItem(IconCompat.createWithBitmap(listOrderProduct.firstOrNull()?.pictureUrl.orEmpty().getBitmap(context)), SMALL_IMAGE)
                        title = listOrderProduct.let { productList ->
                            val displayedName = productList.firstOrNull()?.productName.orEmpty()
                            if (productList.size <= 1) {
                                displayedName
                            } else {
                                with(displayedName) {
                                    val truncatedName =
                                            if (length > MAX_PRODUCT_NAME_LENGTH) {
                                                displayedName.take(TRUNCATED_PRODUCT_NAME_LENGTH) + context.getString(R.string.seller_action_triple_dot)
                                            } else {
                                                displayedName
                                            }
                                    context.getString(R.string.seller_action_order_success_item_multiple_title, truncatedName, productList.size - 1).parseAsHtml()
                                }
                            }
                        }
                        subtitle = (orderStatusId.mapToStatus() ?: status).let { status ->
                            context.getString(R.string.seller_action_order_success_item_desc, status, deadlineText)
                        }
                    }
                    numberOfOrder++
                }
            }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun createActivityAction(): SliceAction {
        SellerActionActivity.createActionIntent(context, SellerActionFeatureName.ALL_ORDER).let { intent ->
            PendingIntent.getActivity(context, LIST_REQUEST_CODE, intent, 0)
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
                    IconCompat.createWithBitmap(imageUri.getBitmap(context)),
                    ICON_IMAGE,
                    title
            )

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