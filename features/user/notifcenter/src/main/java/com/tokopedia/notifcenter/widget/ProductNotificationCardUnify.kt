package com.tokopedia.notifcenter.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.UnifyButton

class ProductNotificationCardUnify(
        context: Context,
        attrs: AttributeSet?
) : CardUnify(context, attrs) {

    private var thumbnail: ImageView? = null
    private var productName: TextView? = null
    private var productPrice: TextView? = null
    private var productVariant: ProductVariantLayout? = null
    private var productCampaign: CampaignRedView? = null
    private var productContainer: ConstraintLayout? = null
    private var campaignTag: ImageView? = null
    private var btnCheckout: UnifyButton? = null
    private var btnAtc: UnifyButton? = null
    private var btnReminder: UnifyButton? = null
    private var listener: NotificationItemListener? = null

    init {
        initView()
    }

    private fun initView() {
        View.inflate(context, R.layout.partial_single_product_notification, this)?.apply {
            initViewBinding(this)
        }
    }

    private fun initViewBinding(view: View) {
        thumbnail = view.findViewById(R.id.iv_thumbnail)
        productName = view.findViewById(R.id.tv_product_name)
        productPrice = view.findViewById(R.id.tv_product_price)
        productVariant = view.findViewById(R.id.pvl_variant)
        productCampaign = view.findViewById(R.id.cl_campaign)
        productContainer = view.findViewById(R.id.cl_product)
        btnCheckout = view.findViewById(R.id.btn_checkout)
        campaignTag = view.findViewById(R.id.img_campaign)
        btnAtc = view.findViewById(R.id.btn_atc)
        btnReminder = view.findViewById(R.id.tv_reminder)
    }

    fun bindProductData(
            notification: NotificationUiModel?,
            product: ProductData?,
            listener: NotificationItemListener?,
            adapterPosition: Int
    ) {
        if (product != null) {
            show()
            initField(listener)
            bindProductImage(product)
            bindProductCampaign(product)
            bindProductVariant(product)
            bindProductPrice(product)
            bindProductName(product)
            bindCampaignTag(product)
            bindProductClick(product)
            bindBuyClick(product)
            bindAtcClick(product)
            bindReminder(product, notification, adapterPosition)
        } else {
            hide()
        }
    }

    fun bindReminderState(product: ProductData) {
        btnReminder?.post {
            btnReminder?.isLoading = product.loadingBumpReminder
        }
    }

    private fun bindReminder(
            product: ProductData,
            notification: NotificationUiModel?,
            adapterPosition: Int
    ) {
        if (product.hasEmptyStock() && notification != null) {
            btnReminder?.show()
            bindReminderState(product)
            btnReminder?.setOnClickListener {
                if (!product.loadingBumpReminder) {
                    product.loadingBumpReminder = true
                    bindReminderState(product)
                    listener?.bumpReminder(product, notification, adapterPosition)
                }
            }
        } else {
            btnReminder?.hide()
        }
    }

    fun goToPdp(product: ProductData?) {
        if (product == null) return
        RouteManager.route(
                context,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                product.productId.toString()
        )
    }

    private fun initField(listener: NotificationItemListener?) {
        this.listener = listener
    }

    private fun bindProductImage(product: ProductData) {
        ImageHandler.loadImage2(
                thumbnail, product.imageUrl, R.drawable.ic_notifcenter_loading_toped
        )
    }

    private fun bindProductCampaign(product: ProductData) {
        productCampaign?.setCampaign(product.campaign)
    }

    private fun bindProductVariant(product: ProductData) {
        productVariant?.setupVariant(product.variant)
    }

    private fun bindProductPrice(product: ProductData) {
        productPrice?.text = product.priceFmt
    }

    private fun bindProductName(product: ProductData) {
        productName?.text = product.name
    }

    private fun bindCampaignTag(product: ProductData) {
        if (product.shop.freeShippingIcon.isNotEmpty()) {
            campaignTag?.loadImage(product.shop.freeShippingIcon)
            campaignTag?.show()
        } else {
            campaignTag?.hide()
        }
    }

    private fun bindProductClick(product: ProductData) {
        setOnClickListener {
            goToPdp(product)
        }
    }

    private fun bindBuyClick(product: ProductData) {
        if (product.hasEmptyStock()) {
            btnCheckout?.hide()
        } else {
            btnCheckout?.show()
            btnCheckout?.setOnClickListener {
                listener?.buyProduct(product)
            }
        }
    }

    private fun bindAtcClick(product: ProductData) {
        if (product.hasEmptyStock()) {
            btnAtc?.hide()
        } else {
            btnAtc?.show()
            btnAtc?.setOnClickListener {
                listener?.addProductToCart(product)
            }
        }
    }
}