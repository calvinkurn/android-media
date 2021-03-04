package com.tokopedia.notifcenter.presentation.fragment.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.collection.ArrayMap
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.data.state.Resource
import com.tokopedia.notifcenter.data.state.Status
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.viewholder.ViewHolderState
import com.tokopedia.notifcenter.widget.ProductNotificationCardUnify
import com.tokopedia.unifycomponents.toPx

class NotificationProductLongerContentBottomSheet : NotificationLongerContentBottomSheet() {

    private var listener: NotificationItemListener? = null
    private var products: ArrayMap<String, ProductNotificationCardUnify?> = ArrayMap()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as? NotificationItemListener
    }

    override fun initCtaButton() {
        cta?.hide()
    }

    fun handleEventReminderState(
            data: Resource<Any>,
            viewHolderState: ViewHolderState?,
            isBumpReminder: Boolean
    ) {
        when (data.status) {
            Status.SUCCESS -> {
                if (isBumpReminder) {
                    showMessage(R.string.title_success_bump_reminder)
                } else {
                    showMessage(R.string.title_success_delete_reminder)
                }
            }
            Status.ERROR -> {
                data.throwable?.let { error ->
                    showErrorMessage(error)
                }
            }
            else -> {
            }
        }
        val payload = viewHolderState?.payload
        if (payload is ProductData) {
            val index = notification?.productData?.indexOfFirst {
                it.productId == payload.productId
            } ?: return
            val product = notification?.productData?.get(index)
            product?.update(payload)
            val productView = products[product?.productId]
            productView?.bumpReminderState(product)
        }
    }

    override fun onInitContentView() {
        notification?.productData?.forEachIndexed { index, product ->
            val view = createProductView()
            val productView: ProductNotificationCardUnify? = view?.findViewById(R.id.pc_single)
            bind(productView, product, index)
            contentContainer?.addView(view)
            products[product.productId] = productView
        }
    }

    private fun bind(
            productView: ProductNotificationCardUnify?,
            product: ProductData,
            index: Int
    ) {
        productView?.bindProductData(notification, product, listener, index)
    }

    private fun createProductView(): View? {
        return View.inflate(
                context,
                R.layout.partial_notifcenter_single_product_card,
                null
        ).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                setMargins(12.toPx(), 2.toPx(), 12.toPx(), 0)
            }
        }
    }

    companion object {
        fun create(notification: NotificationUiModel): NotificationProductLongerContentBottomSheet {
            val notificationString = CommonUtils.toJson(notification)
            val bundle = Bundle().apply {
                putString(KEY_NOTIFICATION_PAYLOAD, notificationString)
            }
            return NotificationProductLongerContentBottomSheet().apply {
                arguments = bundle
            }
        }
    }
}