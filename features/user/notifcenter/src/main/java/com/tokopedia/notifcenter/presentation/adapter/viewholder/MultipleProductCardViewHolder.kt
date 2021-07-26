package com.tokopedia.notifcenter.presentation.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.NotificationUpdateAnalytics.Companion.LABEL_BOTTOM_SHEET_LOCATION
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.state.SourceMultipleProductView
import com.tokopedia.notifcenter.data.viewbean.MultipleProductCardViewBean
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.widget.CampaignRedView
import com.tokopedia.notifcenter.widget.ProductVariantLayout
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.notifcenter.data.mapper.MultipleProductCardMapper as Mapper

class MultipleProductCardViewHolder(
        itemView: View,
        private val sourceView: SourceMultipleProductView,
        val listener: NotificationItemListener
) : AbstractViewHolder<MultipleProductCardViewBean>(itemView) {

    private val thumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)
    private val productName: TextView = itemView.findViewById(R.id.tv_product_name)
    private val productPrice: TextView = itemView.findViewById(R.id.tv_product_price)
    private val productVariant: ProductVariantLayout = itemView.findViewById(R.id.pvl_variant)
    private val productCampaign: CampaignRedView = itemView.findViewById(R.id.cl_campaign)
    private val productContainer: ConstraintLayout = itemView.findViewById(R.id.cl_product)
    private val btnCheckout: UnifyButton = itemView.findViewById(R.id.btn_checkout)
    private val campaignTag: ImageView = itemView.findViewById(R.id.img_campaign)
    private val btnAtc: UnifyButton = itemView.findViewById(R.id.btn_atc)
    private val btnReminder: UnifyButton = itemView.findViewById(R.id.btn_reminder)

    private val context by lazy { itemView.context }

    override fun bind(element: MultipleProductCardViewBean?) {
        if (element == null) return
        val product = element.product
        impressionTracker(element)
        productCheckoutClicked(element)
        isCampaignActive(product)

        with(product) {
            ImageHandler.loadImage2(thumbnail, imageUrl, R.drawable.ic_notifcenter_loading_toped)
            productCampaign.setCampaign(campaign)
            productVariant.setupVariant(variant)
            productPrice.text = priceFormat
            productName.text = name
            showRemindButtonIfEmptyStock(this.stock, this.hasReminder)
        }
    }

    private fun showRemindButtonIfEmptyStock(stock: Int, hasReminder: Boolean) {
        if(stock < MINIMUM_STOCK) {
            showReminderButton(hasReminder)
        } else {
            hideReminderButton()
        }
    }

    private fun showReminderButton(hasReminder: Boolean) {
        btnReminder.show()
        btnAtc.hide()
        btnCheckout.hide()
        if(hasReminder) {
            setDeleteReminderButton()
        } else {
            setReminderButton()
        }
    }

    private fun hideReminderButton() {
        btnReminder.hide()
        btnAtc.show()
        btnCheckout.show()
    }

    private fun impressionTracker(element: MultipleProductCardViewBean) {
        when (sourceView) {
            is SourceMultipleProductView.NotificationCenter -> {
                listener.getAnalytic().trackMultiProductListImpression(
                        userId = element.userInfo.userId,
                        productNumber = element.indexId,
                        notification = element
                )
            }
            is SourceMultipleProductView.BottomSheetDetail -> {
                listener.getAnalytic().trackMultiProductListBottomSheetImpression(
                        userId = element.userInfo.userId,
                        shopId = element.userInfo.shopId,
                        productNumber = element.indexId,
                        notificationId = element.notificationId,
                        product = element.product
                )
            }
        }
    }

    private fun isCampaignActive(product: ProductData) {
        if (product.shop?.freeShippingIcon != null && product.shop.freeShippingIcon.isNotEmpty()) {
            campaignTag.loadImage(product.shop.freeShippingIcon)
            campaignTag.show()
        }
    }

    private fun productCheckoutClicked(element: MultipleProductCardViewBean) {
        val notification = Mapper.map(element)
        productContainer.setOnClickListener {
            listener.itemClicked(notification, adapterPosition)
            when (sourceView) {
                is SourceMultipleProductView.NotificationCenter -> {
                    listener.getAnalytic().trackMultiProductCheckoutCardClick(
                            notification = element
                    )
                }
                is SourceMultipleProductView.BottomSheetDetail -> {
                    listener.getAnalytic().trackMultiProductCheckoutCardClick(
                            eventLocation = LABEL_BOTTOM_SHEET_LOCATION,
                            productNumber = adapterPosition,
                            notification = element
                    )
                }
            }
            RouteManager.route(
                    itemView.context,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    element.product.productId
            )
        }

        btnCheckout.setOnClickListener {
            listener.itemClicked(notification, adapterPosition)

            listener.addProductToCart(element.product) {
                // goto cart page
                routeCartPage()

                when (sourceView) {
                    is SourceMultipleProductView.NotificationCenter -> {
                        listener.getAnalytic().trackAtcOnMultiProductClick(
                                notification = element,
                                productNumber = adapterPosition,
                                cartId = it.cartId
                        )
                    }
                    is SourceMultipleProductView.BottomSheetDetail -> {
                        listener.getAnalytic().trackAtcOnMultiProductClick(
                                eventLocation = LABEL_BOTTOM_SHEET_LOCATION,
                                notification = element,
                                productNumber = adapterPosition,
                                cartId = it.cartId
                        )
                    }
                }
            }

            // goto cart page
            routeCartPage()
        }

        btnAtc.setOnClickListener {
            listener.itemClicked(notification, adapterPosition)

            listener.addProductToCart(element.product) {
                // show toaster
                val message = it.message.first()
                listener.onSuccessAddToCart(message)

                // tracker
                trackAddToCartClicked(element, element.product, it)
            }
        }

        btnReminder.setOnClickListener {
            listener.itemClicked(notification, adapterPosition)
            listener.onItemMultipleStockHandlerClick(notification)
        }
    }

    private fun setReminderButton() {
        btnReminder.text = context?.getString(R.string.notifcenter_btn_reminder)
        btnReminder.buttonType = UnifyButton.Type.MAIN
        btnReminder.buttonVariant = UnifyButton.Variant.FILLED
    }

    private fun setDeleteReminderButton() {
        btnReminder.buttonType = UnifyButton.Type.ALTERNATE
        btnReminder.buttonVariant = UnifyButton.Variant.GHOST
        btnReminder.text = context?.getString(R.string.notifcenter_btn_delete_reminder)
    }

    private fun trackAddToCartClicked(
            element: MultipleProductCardViewBean,
            product: ProductData,
            data: DataModel) {
        listener.getAnalytic().trackAtcOnClick(
                templateKey = element.templateKey,
                notificationId = element.notificationId,
                product = product,
                atc = data
        )
    }

    private fun routeCartPage() {
        RouteManager.route(context, ApplinkConstInternalMarketplace.CART)
    }

    companion object {
        private const val MINIMUM_STOCK = 1
        @LayoutRes
        val LAYOUT = R.layout.item_notification_product_card
    }

}