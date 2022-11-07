package com.tokopedia.notifications.factory.ui

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.notifications.R
import com.tokopedia.notifications.analytics.ProductAnalytics
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.CarouselUtilities
import com.tokopedia.notifications.factory.BaseNotificationContract
import com.tokopedia.notifications.model.ActionButton
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.ProductInfo
import com.tokopedia.notifications.utils.onlyOne
import com.tokopedia.user.session.UserSession
import com.tokopedia.notifications.common.CMConstant.PreDefineActionType.ATC as TYPE_ATC
import com.tokopedia.notifications.common.CMConstant.PreDefineActionType.OCC as TYPE_OCC
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

    val collapsedView by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            RemoteViews(packageName, R.layout.cm_layout_collapsed)
        else RemoteViews(packageName, R.layout.cm_layout_collapsed_pre_dark_mode)
    }
    val expandedView by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            RemoteViews(packageName, R.layout.cm_layout_product_expand)
        else RemoteViews(packageName, R.layout.cm_layout_product_expand_pre_dark_mode) }

    val product: ProductInfo = model.productInfoList[model.carouselIndex]
    val productImage: Bitmap? = loadImage(product.productImage)

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
        productImage?.let{
            view.setImageViewBitmap(R.id.iv_icon_collapsed, it)
        } ?: run {
            view.setImageViewBitmap(R.id.iv_icon_collapsed, base.defaultIcon())
        }
        // set title and message of collapsed
        view.setTextViewText(R.id.tv_collapse_title, spanStr(model.title))
        view.setTextViewText(R.id.tv_collapsed_message, spanStr(model.message))

        // set intent of collapsedView
        view.setOnClickPendingIntent(R.id.collapseMainView, contract.collapsedIntent())
    }

    override fun setExpandedViewData(view: RemoteViews) {
        setCollapseViewData(view)

        // hide collapsed icon
        view.setViewVisibility(R.id.iv_icon_collapsed, View.GONE)

        // set notification icon
        productImage?.let {
            view.setImageViewBitmap(R.id.iv_productImage, it)
        } ?: view.setImageViewBitmap(R.id.iv_productImage, base.defaultIcon())

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

        // carousel visibility of arrow button
        carouselButtonVisibility(view)

        productV2Card(view)
    }

    private fun productV2Card(view: RemoteViews) {
        // set pending intent of product image and content
        view.setOnClickPendingIntent(R.id.iv_productImage, contract.productDetailIntent(product))
        view.setOnClickPendingIntent(R.id.ll_content, contract.productDetailIntent(product))
        view.setOnClickPendingIntent(R.id.status_bar_latest_event_content, contract.productDetailIntent(product))

        // action button
        when {
            product.productButtons.isNullOrEmpty() -> {
                view.setViewVisibility(R.id.btn_icon, View.GONE)
                view.setViewVisibility(R.id.btn_text, View.GONE)
            }
            product.productButtons.onlyOne() -> {
                view.setViewVisibility(R.id.btn_icon, View.GONE)
                val actionButton = product.productButtons.first()
                setButtonField(R.id.btn_text, view, actionButton)
            }
            else -> {
                view.setViewVisibility(R.id.btn_icon, View.GONE)
                var availableButtonSlot = 2
                product.productButtons.forEach { button->
                    if(availableButtonSlot > 0) {
                        if (button.type == TYPE_ATC){
                            view.setViewVisibility(R.id.btn_icon, View.VISIBLE)
                            setButtonField(R.id.btn_icon, view, button)
                        }else {
                            view.setViewVisibility(R.id.btn_text, View.VISIBLE)
                            setButtonField(R.id.btn_text, view, button)
                        }
                        availableButtonSlot--
                    }
                }
            }
        }

        freeOngkirIcon(view) // free ongkir icon
        starReview(view) // star review

        // tracker
        ProductAnalytics.impression(userSession.userId, model, product)
        ProductAnalytics.impressionExpanded(userSession.userId, model, product)

        // override carousel visibility button
        if (model.productInfoList.onlyOne()) {
            view.setViewVisibility(R.id.ivArrowLeft, View.GONE)
            view.setViewVisibility(R.id.ivArrowRight, View.GONE)
        }
    }

    private fun setButtonField(resId: Int, view: RemoteViews, actionButton: ActionButton) {
        val pendingIntent = contract.actionButtonIntent(actionButton)
        view.setOnClickPendingIntent(resId, pendingIntent)
        view.setTextViewText(resId, actionButton.text)

        // force to remove the title of button icon
        if (resId == R.id.btn_icon) {
            view.setTextViewText(resId, "")
        }
    }

    private fun starReview(view: RemoteViews) {
        if (product.reviewScore == 0.0) return

        view.setViewVisibility(R.id.widget_review, View.VISIBLE)
        view.setTextViewText(R.id.txt_review, product.reviewScore.toString())
        view.setTextViewText(R.id.txt_count_review, "(${product.reviewNumber ?: "0"})")

        loadImage(product.reviewIcon)?.let {
            view.setImageViewBitmap(R.id.img_star, it)
        } ?: base.loadResourceAsBitmap(R.drawable.cm_ic_star_review) {
            view.setImageViewBitmap(R.id.img_star, it)
        }
    }

    private fun freeOngkirIcon(view: RemoteViews) {
        if (product.freeOngkirIcon.isNullOrEmpty()) {
            view.setViewVisibility(R.id.img_campaign, View.GONE)
        } else {
            view.setViewVisibility(R.id.img_campaign, View.VISIBLE)
            loadImage(product.freeOngkirIcon)?.let {
                view.setImageViewBitmap(R.id.img_campaign, it)
            }
        }
    }

    private fun productStock(view: RemoteViews) {
        product.stockAvailable?.let {
            if (it.isNotEmpty() && it != "0") {
                view.setViewVisibility(R.id.tv_stock, View.VISIBLE)
                view.setTextViewText(R.id.tv_stock, spanStr(product.stockMessage))
            }
        }
    }

    private fun discountPriceTag(view: RemoteViews) {
        with(product) {
            val isActualPriceAndPercentOn = productActualPrice.isNullOrEmpty() ||
                    productPriceDroppedPercentage.isNullOrEmpty() ||
                    productActualPrice == productCurrentPrice
            if (isActualPriceAndPercentOn) {
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

    interface ProductContract {
        fun collapsedIntent(): PendingIntent
        fun actionButtonIntent(actionButton: ActionButton): PendingIntent
        fun productDetailIntent(product: ProductInfo): PendingIntent
        fun rightCarouselButton(remoteView: RemoteViews)
        fun leftCarouselButton(remoteView: RemoteViews)
    }

}