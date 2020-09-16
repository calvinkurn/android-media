package com.tokopedia.notifications.factory.ui

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import com.tokopedia.notifications.R
import com.tokopedia.notifications.analytics.ProductAnalytics
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.CarouselUtilities
import com.tokopedia.notifications.factory.BaseNotificationContract
import com.tokopedia.notifications.model.ActionButton
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.ProductInfo
import com.tokopedia.user.session.UserSession
import com.tokopedia.notifications.common.CMNotificationUtils.getSpannedTextFromStr as spanStr
import com.tokopedia.notifications.common.CarouselUtilities.loadImageFromStorage as loadImage


internal open class ProductWidget(
        private val context: Context,
        private val contract: ProductContract,
        private val base: BaseNotificationContract,
        private val model: BaseNotificationModel
) : FactoryWidget {

    private val userSession by lazy { UserSession(context) }
    private val packageName = context.applicationContext.packageName

    val product: ProductInfo = model.productInfoList[model.carouselIndex]
    val productImage: Bitmap? = loadImage(product.productImage)

    val collapsedView by lazy {
        RemoteViews(packageName, R.layout.cm_layout_collapsed)
    }

    val expandedView by lazy {
        RemoteViews(packageName, R.layout.cm_layout_product_expand)
    }

    init {
        if (!model.isUpdateExisting) {
            CarouselUtilities.downloadProductImages(
                    context.applicationContext,
                    model.productInfoList
            )
        }
    }

    override fun setCollapseViewData(view: RemoteViews) {
        // set notification icon
        if (model.icon.isNullOrEmpty()) {
            view.setImageViewBitmap(R.id.iv_icon_collapsed, base.defaultIcon())
        } else {
            view.setImageViewBitmap(R.id.iv_icon_collapsed, base.getBitmap(model.icon))
        }

        // set title and message of collapsed
        view.setTextViewText(R.id.tv_collapse_title, spanStr(model.title))
        view.setTextViewText(R.id.tv_collapsed_message, spanStr(model.message))

        // set intent of collapsedView
        view.setOnClickPendingIntent(R.id.collapseMainView, contract.collapsedIntent())
    }

    override fun setExpandedViewData(view: RemoteViews) {
        setCollapseViewData(view)

        // set notification icon
        productImage?.let {
            view.setImageViewBitmap(R.id.iv_productImage, it)
        }?: view.setImageViewBitmap(R.id.iv_productImage, base.defaultIcon())

        // set common product card of expanded
        view.setTextViewText(R.id.tv_productTitle, spanStr(product.productTitle))
        view.setTextViewText(R.id.tv_oldPrice, spanStr(product.productActualPrice))
        view.setTextViewText(R.id.tv_currentPrice, spanStr(product.productCurrentPrice))

        // product stock
        productStock(view)

        // discount price tag
        discountPriceTag(view)

        // carousel button
        contract.rightCarouselButton(view)
        contract.leftCarouselButton(view)
        carouselButtonVisibility(view)

        // handling legacy of product card
        when (model.notificationProductType) {
            CMConstant.NotificationProductType.V2 -> productV2Card(view)
            else -> productV1Card(view)
        }
    }

    private fun productV1Card(view: RemoteViews) {
        view.setOnClickPendingIntent(R.id.ll_expandedProductView, contract.productDetailIntent(product))
        view.setTextViewText(R.id.tv_productButton, spanStr(product.productButtonMessage))
    }

    private fun productV2Card(view: RemoteViews) {
        val actionButton = product.actionButton[model.carouselIndex]

        // set pending intent of product image and content
        view.setOnClickPendingIntent(R.id.iv_productImage, contract.productDetailIntent(product))
        view.setOnClickPendingIntent(R.id.ll_content, contract.productDetailIntent(product))

        // action button
        if (product.actionButton.size == 1) {
            val pendingIntent = contract.actionButtonIntent(actionButton)
            view.setTextViewText(R.id.tv_productButton, actionButton.text)
            view.setOnClickPendingIntent(R.id.tv_productButton, pendingIntent)
        } else if (product.actionButton.size > 1) {
            view.setViewVisibility(R.id.tv_productButton, View.GONE)
            multipleActionButton(view)
        }

        freeOngkirIcon(view) // free ongkir icon
        starReview(view) // star review

        // tracker
        ProductAnalytics.impression(userSession.userId, model, product)
        ProductAnalytics.impressionExpanded(userSession.userId, model, product)
    }

    private fun multipleActionButton(view: RemoteViews) {
        view.setViewVisibility(R.id.view_button, View.VISIBLE)
        product.actionButton.forEach {
            when (it.type) {
                CMConstant.PreDefineActionType.ATC -> mainActionButton(view, it)
                CMConstant.PreDefineActionType.OCC -> secondActionButton(view, it)
            }
        }
    }

    private fun starReview(view: RemoteViews) {
        if (product.reviewScore == null) return
        if (product.reviewScore?.isEmpty() == true) return

        view.setViewVisibility(R.id.widget_review, View.VISIBLE)
        view.setTextViewText(R.id.txt_review, product.reviewScore)
        view.setTextViewText(R.id.txt_count_review, "(${product.reviewScore})")

        base.loadResourceAsBitmap(R.drawable.cm_ic_star_review) {
            view.setImageViewBitmap(R.id.img_star, it)
        }
    }

    private fun freeOngkirIcon(view: RemoteViews) {
        if (product.freeOngkirIcon.isNullOrEmpty()) {
            view.setViewVisibility(R.id.img_campaign, View.GONE)
        } else {
            view.setViewVisibility(R.id.img_campaign, View.VISIBLE)
            CarouselUtilities.loadImageFromStorage(product.freeOngkirIcon)?.let {
                view.setImageViewBitmap(R.id.img_campaign, it)
            }
        }
    }

    private fun productStock(view: RemoteViews) {
        if (product.productMessage.isEmpty()) {
            view.setViewVisibility(R.id.tv_stock, View.GONE)
        } else {
            view.setTextViewText(R.id.tv_stock, spanStr(product.productMessage))
        }
    }

    private fun discountPriceTag(view: RemoteViews) {
        with(product) {
            if (productActualPrice == null || productPriceDroppedPercentage == null) {
                view.setViewVisibility(R.id.ll_oldPriceAndDiscount, View.GONE)
            } else {
                val strikePrice = "<strike>${productActualPrice}</strike>"
                view.setTextViewText(R.id.tv_oldPrice, spanStr(strikePrice))
                view.setTextViewText(R.id.tv_discountPercent, spanStr(productPriceDroppedPercentage))
            }
        }
    }

    private fun carouselButtonVisibility(view: RemoteViews) {
        when {
            model.productInfoList.size == 1 -> {
                view.setViewVisibility(R.id.ivArrowLeft, View.INVISIBLE)
                view.setViewVisibility(R.id.ivArrowRight, View.INVISIBLE)
            }
            model.carouselIndex == 0 -> {
                view.setViewVisibility(R.id.ivArrowLeft, View.INVISIBLE)
                view.setViewVisibility(R.id.ivArrowRight, View.VISIBLE)
            }
            model.carouselIndex == (model.productInfoList.size - 1) -> {
                view.setViewVisibility(R.id.ivArrowLeft, View.VISIBLE)
                view.setViewVisibility(R.id.ivArrowRight, View.INVISIBLE)
            }
            else -> {
                view.setViewVisibility(R.id.ivArrowLeft, View.VISIBLE)
                view.setViewVisibility(R.id.ivArrowRight, View.VISIBLE)
            }
        }
    }

    private fun mainActionButton(view: RemoteViews, button: ActionButton) {
        view.setInt(R.id.btn_first, SET_BACKGROUND_RESOURCE, R.color.deep_orange_500)
        view.setTextColor(R.id.btn_first, ContextCompat.getColor(context, R.color.white))
        view.setTextViewText(R.id.btn_first, button.text)
    }

    private fun secondActionButton(view: RemoteViews, button: ActionButton) {
        view.setInt(R.id.btn_second, SET_BACKGROUND_RESOURCE, R.color.white)
        view.setTextColor(R.id.btn_second, ContextCompat.getColor(context, R.color.deep_orange_500))
        view.setTextViewText(R.id.btn_second, button.text)
    }

    interface ProductContract {
        fun collapsedIntent(): PendingIntent
        fun actionButtonIntent(actionButton: ActionButton): PendingIntent
        fun productDetailIntent(product: ProductInfo): PendingIntent
        fun rightCarouselButton(remoteView: RemoteViews)
        fun leftCarouselButton(remoteView: RemoteViews)
    }

    companion object {
        private const val SET_BACKGROUND_RESOURCE = "setBackgroundResource"
    }

}