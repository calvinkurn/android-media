package com.tokopedia.navigation.presentation.adapter.viewholder.transaction

import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.model.TransactionItemNotification
import com.tokopedia.navigation.domain.pojo.ProductData
import com.tokopedia.navigation.presentation.view.listener.NotificationTransactionItemListener
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateItemListener
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel
import com.tokopedia.navigation.widget.CampaignLayout
import com.tokopedia.navigation.widget.ProductVariantLayout

class WishListNotificationViewHolder(itemView: View, listener: NotificationTransactionItemListener)
    : NotificationTransactionItemViewHolder(itemView, listener) {

    private val thumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)
    private val productName: TextView = itemView.findViewById(R.id.tv_product_name)
    private val productPrice: TextView = itemView.findViewById(R.id.tv_product_price)
    private val productContainer: ConstraintLayout = itemView.findViewById(R.id.cl_product)
    private val productCampaign: CampaignLayout = itemView.findViewById(R.id.cl_campaign)
    private val productVariant: ProductVariantLayout = itemView.findViewById(R.id.pvl_variant)
    private val btnCart: ImageView = itemView.findViewById(R.id.iv_cart)

    override fun bindNotificationPayload(element: TransactionItemNotification) {
        val product = element.getAtcProduct() ?: return
        val atcDrawable = ContextCompat.getDrawable(itemView.context, R.drawable.ic_add_to_cart)

        with(product) {
            productName.text = name
            productPrice.text = priceFormat
            btnCart.setImageDrawable(atcDrawable)
            productCampaign.setupCampaign(campaign)
            productVariant.setupVariant(variant)
            ImageHandler.loadImage2(thumbnail, imageUrl, R.drawable.ic_loading_toped_new)
        }

        assignClickListenerAtc(element)
        assignProductClickListener(element)
        //getAnalytic().saveProductCardImpression(element, adapterPosition)
    }

    private fun assignClickListenerAtc(element: TransactionItemNotification) {
        val product = element.getAtcProduct() ?: return
        btnCart.setOnClickListener {
            listener.getAnalytic().trackAtcOnClick(product, DataModel())
            listener.addProductToCart(product, onSuccessAddToCart())
            listener.itemClicked(element, adapterPosition)
            element.isRead = true
        }
    }

    private fun onSuccessAddToCart(): () -> Unit {
        return {
            val checkDrawable = ContextCompat.getDrawable(itemView.context, R.drawable.ic_add_to_cart_check_grey)
            btnCart.setImageDrawable(checkDrawable)
        }
    }

    override fun bindOnNotificationClick(element: TransactionItemNotification) {
        val product = element.getAtcProduct() ?: return
        container.setOnClickListener(getItemClickListener(product, element))
    }

    private fun assignProductClickListener(element: TransactionItemNotification) {
        val product = element.getAtcProduct() ?: return
        productContainer.setOnClickListener(getItemClickListener(product, element))
    }

    private fun getItemClickListener(product: ProductData, element: TransactionItemNotification): View.OnClickListener {
        return View.OnClickListener {
            listener.itemClicked(element, adapterPosition)
            element.isRead = true
            listener.getAnalytic().trackAtcToPdpClick(product)
            RouteManager.route(
                    itemView.context,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    product.productId
            )
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_notification_update_wishlist
    }

}