package com.tokopedia.notifcenter.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.StockHandlerAnalytics
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.mapper.ProductHighlightMapper.mapToProductData
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.data.viewbean.ProductHighlightViewBean
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.listener.ProductStockListener
import com.tokopedia.notifcenter.presentation.BaseBottomSheet
import com.tokopedia.notifcenter.presentation.activity.NotificationActivity
import com.tokopedia.notifcenter.presentation.adapter.ProductHighlightAdapter
import com.tokopedia.notifcenter.presentation.viewmodel.ProductStockHandlerViewModel
import com.tokopedia.notifcenter.util.dialogWindow
import com.tokopedia.notifcenter.util.viewModelProvider
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.dialog_product_stock_handler.*
import kotlinx.android.synthetic.main.item_empty_state.*
import kotlinx.android.synthetic.main.item_notification_product_reminder.*
import javax.inject.Inject

class ProductStockHandlerDialog(
        private val element: NotificationItemViewBean,
        private val listener: NotificationItemListener
): BaseBottomSheet(), ProductStockListener {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var userSession: UserSessionInterface

    private lateinit var viewModel: ProductStockHandlerViewModel

    private val productHighlightList = arrayListOf<ProductHighlightViewBean>()

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        ProductHighlightAdapter(productHighlightList, this)
    }

    private val analytics by lazy(LazyThreadSafetyMode.NONE) {
        StockHandlerAnalytics()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        viewModel = viewModelProvider(viewModelFactory)
    }

    override fun contentView(): Int {
        return R.layout.dialog_product_stock_handler
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        analytics.productCardImpression(element, userSession.userId)
        initObservable()
    }

    private fun initObservable() {
        viewModel.productHighlight.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                emptyContainer?.show()
            } else {
                productHighlightList.clear()
                productHighlightList.addAll(it)
                adapter.notifyDataSetChanged()

                // send tracker
                productHighlightList.forEach { product ->
                    val shopId = element.getAtcProduct()?.shop?.id.toString()
                    swipeProductListTracker(product.id.toString(), shopId)
                }
            }
        })

        viewModel.productStockReminder.observe(viewLifecycleOwner, Observer {
            val successMessage = getString(R.string.title_success_bump_reminder)
            val actionText = getString(R.string.notifcenter_btn_title_ok)
            onSuccessListener(successMessage, actionText)
        })

        viewModel.addToCart.observe(viewLifecycleOwner, Observer {
            val productData = it.first
            val atcResponse = it.second

            // showing the success toaster
            val actionText = getString(R.string.notifcenter_title_view)
            onSuccessListener(atcResponse.data.message.first(), actionText) {
                RouteManager.route(context, ApplinkConstInternalMarketplace.CART)
            }

            // send tracker
            analytics.addToCardClicked(
                    element.notificationId,
                    productData,
                    userSession.userId,
                    atcResponse.data.cartId
            )
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            showToastErrorMessage(it)
        })

        viewModel.deleteReminder.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> {
                    val successMessage = getString(R.string.title_success_delete_reminder)
                    onSuccessToastMessage(successMessage)
                }
                is Fail -> {
                    setReminderButton()
                    showToastErrorMessage(ErrorHandler.getErrorMessage(context, it.throwable))
                }
            }
        })
    }

    override fun renderView() {
        // delegate view before rendering a content view
        lstProduct?.adapter = adapter

        // content view of bottomSheet container and empty state
        setContainerContentView()

        element.getAtcProduct()?.let { product ->
            // set product card data
            productCard?.setOnClickListener { onProductCardClicked(product) }
            val typeButton = if(product.hasReminder) {
                TYPE_DELETE_REMINDER_BUTTON
            } else {
                product.typeButton
            }
            buttonReminderValidation(typeButton)
            setDataProductCard(product)

            // get product highlight
            if (product.shop?.id != null) {
                viewModel.getHighlightProduct(product.shop.id.toString())
            }
        }
    }

    private fun setContainerContentView() {
        txtTitle?.text = element.title
        txtDescription?.text = element.body
        txtEmptyMessage?.text = getString(R.string.product_highlight_empty)
    }

    private fun setDataProductCard(product: ProductData) {
        ImageHandler.loadImage2(
                imgThumbnail,
                product.imageUrl,
                R.drawable.ic_notifcenter_loading_toped
        )

        txtProductName?.text = product.name
        txtProductPrice?.text = product.priceFormat
        txtProductCampaign?.setCampaign(product.campaign)

        setCampaignTag(product)
    }

    private fun setCampaignTag(product: ProductData) {
        if (product.shop?.freeShippingIcon != null &&
            product.shop.freeShippingIcon.isNotEmpty()) {
            viewCampaignTag?.loadImage(product.shop.freeShippingIcon)
            viewCampaignTag?.show()
        }
    }

    override fun onAddToCartProduct(data: ProductHighlightViewBean) {
        if (data.isStockEmpty) {
            val message = getString(R.string.notifcenter_out_of_stock)
            showToastErrorMessage(message)
        } else {
            viewModel.addProductToCart(
                    userSession.userId,
                    mapToProductData(data)
            )
        }
    }

    override fun productStockListCardImpression(data: ProductHighlightViewBean, index: Int) {
        analytics.productStockListCardImpression(
                element.notificationId,
                mapToProductData(data),
                userSession.userId,
                element.getAtcProduct()?.shop?.id.toString(),
                index
        )
    }

    override fun productStockListCardClicked(data: ProductHighlightViewBean, index: Int) {
        analytics.productStockListCardClicked(
                element.notificationId,
                mapToProductData(data),
                userSession.userId,
                element.getAtcProduct()?.shop?.id.toString(),
                index
        )
    }

    private fun onProductCardClicked(product: ProductData) {
        analytics.productCardClicked(element, userSession.userId)
        RouteManager.route(
                context,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                product.productId
        )
    }

    private fun onProductStockReminderClicked(product: ProductData) {
        analytics.stockReminderClicked(element, userSession.userId)
        if (product.stock < SINGLE_PRODUCT_STOCK) {
            viewModel.setProductReminder(product.productId, element.notificationId)
        } else {
            viewModel.addProductToCart(
                    userSession.userId,
                    product
            )
        }
    }

    private fun onSuccessListener(title: String, actionText: String, onClick: () -> Unit = {}) {
        dialogWindow()?.let {
            val onActionClick = View.OnClickListener { onClick() }
            Toaster.make(it, title, Toaster.LENGTH_LONG, TYPE_NORMAL, actionText, onActionClick)
        }
    }

    private fun onSuccessToastMessage(message: String) {
        dialogWindow()?.let {
            Toaster.make(it, message, Toaster.LENGTH_LONG, TYPE_NORMAL)
        }
    }

    private fun showToastErrorMessage(message: String) {
        dialogWindow()?.let {
            Toaster.make(it, message, Toaster.LENGTH_LONG, TYPE_ERROR)
        }
    }

    private fun buttonReminderValidation(type: Int) {
        when(type) {
            TYPE_BUY_BUTTON -> {
                btnReminder?.text = context?.getString(R.string.notifcenter_btn_buy)
                btnReminder?.buttonType = UnifyButton.Type.TRANSACTION
            }
            TYPE_REMINDER_BUTTON -> {
                setReminderButton()
            }
            TYPE_DELETE_REMINDER_BUTTON -> {
                setDeleteReminderButton()
            }
        }
    }

    private fun swipeProductListTracker(productId: String, shopId: String) {
        analytics.swipeRestockProductList(
                notificationId = element.notificationId,
                productId = productId,
                userId = userSession.userId,
                shopId = shopId
        )
    }

    private fun initInjector() {
        (activity as NotificationActivity)
                .notificationComponent
                .inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.cleared()
        dismissAllowingStateLoss()
    }

    private fun setReminderButton() {
        btnReminder?.text = context?.getString(R.string.notifcenter_btn_reminder)
        btnReminder?.buttonType = UnifyButton.Type.MAIN
        btnReminder?.buttonVariant = UnifyButton.Variant.FILLED
        element.getAtcProduct()?.let {product ->
            btnReminder?.setOnClickListener {
                setDeleteReminderButton()
                onProductStockReminderClicked(product)
            }
        }
    }

    private fun setDeleteReminderButton() {
        btnReminder?.buttonType = UnifyButton.Type.ALTERNATE
        btnReminder?.buttonVariant = UnifyButton.Variant.GHOST
        btnReminder?.text = context?.getString(R.string.notifcenter_btn_delete_reminder)
        btnReminder?.setOnClickListener {
            setReminderButton()
            element.getAtcProduct()?.productId?.let {
                viewModel.deleteReminder(it, element.notificationId)
            }
        }
    }

    companion object {
        private const val TYPE_BUY_BUTTON = 0
        private const val TYPE_REMINDER_BUTTON = 1
        private const val TYPE_DELETE_REMINDER_BUTTON = 2

        private const val SINGLE_PRODUCT_STOCK = 1
    }
}