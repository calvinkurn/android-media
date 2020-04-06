package com.tokopedia.notifcenter.presentation.fragment

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.StockHandlerAnalytics
import com.tokopedia.notifcenter.data.entity.ProductStockReminder
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.domain.ProductStockReminderUseCase
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.presentation.BaseBottomSheetDialog
import com.tokopedia.notifcenter.widget.CampaignRedView
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.abstraction.common.utils.GraphqlHelper.loadRawString as raw
import com.tokopedia.notifcenter.domain.ProductStockReminderUseCase.Companion.params as stockReminderParams

typealias UseCase = GraphqlUseCase<ProductStockReminder>

class ProductStockReminderDialog(
        private val userSession: UserSessionInterface,
        fragmentManager: FragmentManager,
        private val context: Context,
        private val listener: NotificationItemListener
): BaseBottomSheetDialog<NotificationItemViewBean>(context, fragmentManager) {

    private val productCard = container?.findViewById<CardUnify>(R.id.productCard)

    private val txtTitle = container?.findViewById<Typography>(R.id.txtTitle)
    private val txtDescription = container?.findViewById<Typography>(R.id.txtDescription)

    private val thumbnail = container?.findViewById<ImageView>(R.id.imgThumbnail)
    private val productName = container?.findViewById<TextView>(R.id.txtProductName)
    private val productPrice = container?.findViewById<TextView>(R.id.txtProductPrice)
    private val productCampaign = container?.findViewById<CampaignRedView>(R.id.txtProductCampaign)
    private val campaignTag = container?.findViewById<ImageView>(R.id.viewCampaignTag)
    private val btnReminder = container?.findViewById<UnifyButton>(R.id.btnReminder)

    private val useCase by lazy {
        val repository = GraphqlInteractor.getInstance().graphqlRepository
        val graphUseCase = UseCase(repository)
        val graphQuery = raw(context.resources, R.raw.mutation_product_stock_reminder)
        ProductStockReminderUseCase(graphQuery, graphUseCase)
    }

    private val analytics by lazy {
        StockHandlerAnalytics()
    }

    override fun resourceId(): Int {
        return R.layout.dialog_product_stock_handler
    }

    override fun show(element: NotificationItemViewBean) {
        analytics.productCardImpression(element, userSession.userId)

        txtTitle?.text = element.title
        txtDescription?.text = element.body

        element.getAtcProduct()?.let { product ->
            buttonReminderValidation(product.typeButton)

            ImageHandler.loadImage2(
                    thumbnail,
                    product.imageUrl,
                    R.drawable.ic_notifcenter_loading_toped
            )

            productName?.text = product.name
            productPrice?.text = product.priceFormat
            productCampaign?.setCampaign(product.campaign)

            if (product.shop?.freeShippingIcon != null &&
                    product.shop.freeShippingIcon.isNotEmpty()) {
                campaignTag?.loadImage(product.shop.freeShippingIcon)
                campaignTag?.show()
            }

            productCard?.setOnClickListener {
                analytics.productCardClicked(element, userSession.userId)
                RouteManager.route(
                        context,
                        ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                        product.productId
                )
            }

            btnReminder?.setOnClickListener {
                analytics.stockReminderClicked(element, userSession.userId)
                if (product.stock < SINGLE_PRODUCT_STOCK) {
                    setReminder(product.productId, element.notificationId)
                } else {
                    listener.addProductToCheckout(element.userInfo, element)
                }
            }
        }
    }

    private fun buttonReminderValidation(type: Int) {
        when(type) {
            TYPE_BUY_BUTTON -> {
                btnReminder?.text = context.getString(R.string.notifcenter_btn_buy)
                btnReminder?.buttonType = UnifyButton.Type.TRANSACTION
            }
            TYPE_REMINDER_BUTTON -> {
                btnReminder?.text = context.getString(R.string.notifcenter_btn_reminder)
                btnReminder?.buttonType = UnifyButton.Type.MAIN
            }
            TYPE_OUT_OF_STOCK_BUTTON -> {
                btnReminder?.text = context.getString(R.string.notifcenter_btn_out_of_stock)
                btnReminder?.isEnabled = false
            }
        }
    }

    private fun setReminder(productId: String, notificationId: String) {
        val params = stockReminderParams(notificationId, productId)
        useCase.get(params, {
            isSuccessProductReminder()
        }, {})
    }

    private fun isSuccessProductReminder() {
        btnReminder?.isEnabled = false
        listener.onSuccessReminderStock()
    }

    companion object {
        private const val TYPE_BUY_BUTTON = 0
        private const val TYPE_REMINDER_BUTTON = 1
        private const val TYPE_OUT_OF_STOCK_BUTTON = 2

        private const val SINGLE_PRODUCT_STOCK = 1
    }

}