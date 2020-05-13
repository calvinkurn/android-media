package com.tokopedia.notifcenter.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.StockHandlerAnalytics
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.mapper.ProductHighlightMapper.mapToProductData
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.data.viewbean.ProductHighlightViewBean
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.listener.ProductStockListener
import com.tokopedia.notifcenter.presentation.activity.NotificationActivity
import com.tokopedia.notifcenter.presentation.adapter.ProductHighlightAdapter
import com.tokopedia.notifcenter.presentation.viewmodel.ProductStockHandlerViewModel
import com.tokopedia.notifcenter.util.dialogWindow
import com.tokopedia.notifcenter.util.viewModelProvider
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.unifycomponents.Toaster.toasterLength
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.dialog_product_stock_handler.*
import kotlinx.android.synthetic.main.item_empty_state.*
import kotlinx.android.synthetic.main.item_notification_product_reminder.*
import javax.inject.Inject

class ProductStockHandlerDialog(
        private val element: NotificationItemViewBean,
        private val listener: NotificationItemListener
): BottomSheetUnify(), ProductStockListener {

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

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val contentView = View.inflate(
                requireContext(),
                R.layout.dialog_product_stock_handler,
                null
        )
        setChild(contentView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        analytics.productCardImpression(element, userSession.userId)
        renderView()
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
            val successMessage = getString(R.string.product_reminder_success)
            val actionText = getString(R.string.notifcenter_btn_title_ok)
            onSuccessListener(successMessage, actionText)
            btnReminder?.isEnabled = false
        })

        viewModel.addToCart.observe(viewLifecycleOwner, Observer {
            analytics.addToCardClicked(element, userSession.userId, it.data.cartId)

            val actionText = getString(R.string.notifcenter_title_view)
            onSuccessListener(it.data.message.first(), actionText) {
                RouteManager.route(context, ApplinkConstInternalMarketplace.CART)
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            showToastErrorMessage(it)
        })
    }

    private fun renderView() {
        // delegate view before rendering a content view
        lstProduct?.adapter = adapter

        // content view of bottomSheet container and empty state
        setContainerContentView()

        element.getAtcProduct()?.let { product ->
            // set product card data
            productCard?.setOnClickListener { onProductCardClicked(product) }
            btnReminder?.setOnClickListener { onProductStockReminderClicked(product) }
            buttonReminderValidation(product.typeButton)
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

    override fun onAddToCartProduct(element: ProductHighlightViewBean) {
        viewModel.addProductToCart(mapToProductData(element))
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
            listener.addProductToCheckout(element.userInfo, element)
        }
    }

    private fun onSuccessListener(title: String, actionText: String, onClick: () -> Unit = {}) {
        dialogWindow()?.let {
            val onActionClick = View.OnClickListener { onClick() }
            Toaster.make(it, title, Toaster.LENGTH_LONG, TYPE_NORMAL, actionText, onActionClick)
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
                btnReminder?.text = context?.getString(R.string.notifcenter_btn_reminder)
                btnReminder?.buttonType = UnifyButton.Type.MAIN
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

    companion object {
        private const val TYPE_BUY_BUTTON = 0
        private const val TYPE_REMINDER_BUTTON = 1

        private const val SINGLE_PRODUCT_STOCK = 1
    }
}