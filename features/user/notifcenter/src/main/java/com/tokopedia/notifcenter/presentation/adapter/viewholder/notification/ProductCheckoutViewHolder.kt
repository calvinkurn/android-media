package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.mapper.MultipleProductCardMapper
import com.tokopedia.notifcenter.data.state.SourceMultipleProductView
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.MultipleProductCardAdapter
import com.tokopedia.notifcenter.presentation.adapter.typefactory.product.MultipleProductCardFactoryImpl
import com.tokopedia.notifcenter.presentation.adapter.viewholder.base.BaseProductCampaignViewHolder
import com.tokopedia.notifcenter.util.ProductCardSnapHelper
import com.tokopedia.notifcenter.widget.CampaignRedView
import com.tokopedia.unifycomponents.UnifyButton

class ProductCheckoutViewHolder(
        itemView: View,
        listener: NotificationItemListener
) : BaseProductCampaignViewHolder(itemView, listener) {

    private val cardContainer: RelativeLayout = itemView.findViewById(R.id.container_product_card)
    private val productCampaign: CampaignRedView = itemView.findViewById(R.id.cl_campaign)
    private val lstProduct: RecyclerView = itemView.findViewById(R.id.lst_products)
    private val btnCheckout: UnifyButton = itemView.findViewById(R.id.btn_checkout)
    private val campaignTag: ImageView = itemView.findViewById(R.id.img_campaign)
    private val btnAtc: UnifyButton = itemView.findViewById(R.id.btn_atc)
    private val btnReminder: UnifyButton = itemView.findViewById(R.id.btn_reminder)

    private var multiProductAdapter: MultipleProductCardAdapter? = null
    private val context by lazy { itemView.context }

    override fun bindProductView(element: NotificationItemViewBean) {
        val product = element.getAtcProduct() ?: return
        snapMultiProductItem()
        onProductCheckoutClick(element)
        productCardItemView(element)

        with(product) {
            if (product.shop?.freeShippingIcon != null &&
                product.shop.freeShippingIcon.isNotEmpty()) {
                campaignTag.loadImage(product.shop.freeShippingIcon)
                campaignTag.show()
            }
            productCampaign.setCampaign(campaign)
        }
    }

    private fun snapMultiProductItem() {
        if (lstProduct.onFlingListener == null) {
            ProductCardSnapHelper().attachToRecyclerView(lstProduct)
        }
    }

    private fun productCardItemView(element: NotificationItemViewBean) {
        if (element.products.size > SINGLE_PRODUCT) {
            cardContainer.hide()
            lstProduct.show()
            listener.getAnalytic().trackMultiProductListImpression(
                    userId = element.userInfo.userId,
                    productNumber = element.indexId,
                    notification = element
            )
            val factory = MultipleProductCardFactoryImpl(
                    sourceView = SourceMultipleProductView.NotificationCenter,
                    listener = listener
            )
            if (multiProductAdapter == null) {
                multiProductAdapter = MultipleProductCardAdapter(
                        multipleProductCardFactory = factory,
                        isResizable = true
                )
            }
            lstProduct.adapter = multiProductAdapter
            multiProductAdapter?.removeAllItem()
            multiProductAdapter?.insertData(
                    MultipleProductCardMapper.map(element)
            )
        } else if (element.products.size == SINGLE_PRODUCT) {
            cardContainer.show()
            lstProduct.hide()
            listener.getAnalytic().trackProductListImpression(
                    userId = element.userInfo.userId,
                    notification = element
            )
            showRemindButtonIfEmptyStock(element.products[0].stock, element.products[0].hasReminder)
        }
    }

    private fun showRemindButtonIfEmptyStock(stock: Int, hasReminder: Boolean) {
        if(stock < SINGLE_PRODUCT) {
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

    override fun trackProduct(element: NotificationItemViewBean) {
        if (element.totalProduct == SINGLE_PRODUCT) {
            listener.getAnalytic().trackSingleProductCheckoutCardClick(
                    notification = element
            )
        } else if (element.totalProduct > SINGLE_PRODUCT) {
            listener.getAnalytic().trackMultiProductCheckoutCardClick(
                    notification = element
            )
        }
    }

    private fun onProductCheckoutClick(element: NotificationItemViewBean) {
        val product = element.getAtcProduct() ?: return
        btnCheckout.setOnClickListener {
            notificationItemMarkedClick(element)
            listener.addProductToCart(product) {
                // goto cart page
                routeCartPage()

                // tracker
                listener.getAnalytic().trackAtcOnSingleProductClick(
                        notification = element,
                        cartId = it.cartId
                )
            }
        }

        btnAtc.setOnClickListener {
            notificationItemMarkedClick(element)

            listener.addProductToCart(product) {
                // show toaster
                val message = it.message.first()
                listener.onSuccessAddToCart(message)

                // tracker
                trackAddToCartClicked(element, product, it)
            }
        }

        btnReminder.setOnClickListener {
            notificationItemMarkedClick(element)
            listener.onItemStockHandlerClick(element)
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
            element: NotificationItemViewBean,
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
        private const val SINGLE_PRODUCT = 1

        //button checkout type
        private const val TYPE_BUY_BUTTON = 0
        private const val TYPE_REMINDER_BUTTON = 1
        private const val TYPE_OUT_OF_STOCK_BUTTON = 2

        @LayoutRes val LAYOUT = R.layout.item_notification_product_checkout
    }

}