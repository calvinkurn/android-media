package com.tokopedia.notifcenter.presentation.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.StockHandlerAnalytics
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.data.viewbean.ProductHighlightViewBean
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.presentation.activity.NotificationActivity
import com.tokopedia.notifcenter.presentation.adapter.ProductHighlightAdapter
import com.tokopedia.notifcenter.presentation.viewmodel.ProductStockHandlerViewModel
import com.tokopedia.notifcenter.util.dialogWindow
import com.tokopedia.notifcenter.util.viewModelProvider
import com.tokopedia.notifcenter.widget.CampaignRedView
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.unifycomponents.Toaster.toasterLength
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ProductStockHandlerDialog(
        private val element: NotificationItemViewBean,
        private val userSession: UserSessionInterface,
        private val listener: NotificationItemListener
): BottomSheetUnify() {

    private lateinit var dialogContainerView: LinearLayout
    private lateinit var productCard: CardUnify
    private lateinit var txtTitle: AppCompatTextView
    private lateinit var txtDescription: AppCompatTextView
    private lateinit var thumbnail: ImageView
    private lateinit var productName: TextView
    private lateinit var productPrice: TextView
    private lateinit var productCampaign: CampaignRedView
    private lateinit var campaignTag: ImageView
    private lateinit var btnReminder: UnifyButton

    // empty state
    private lateinit var emptyContainer: RelativeLayout
    private lateinit var txtEmptyMessage: TextView
    private lateinit var lstProduct: RecyclerView

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ProductStockHandlerViewModel

    private val productHighlightList = arrayListOf<ProductHighlightViewBean>()

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        ProductHighlightAdapter(productHighlightList, ::onAtcClicked)
    }

    private val analytics by lazy(LazyThreadSafetyMode.NONE) {
        StockHandlerAnalytics()
    }

    private val containerView: View by lazy(LazyThreadSafetyMode.NONE) {
        View.inflate(requireContext(), R.layout.dialog_product_stock_handler, null)
    }

    private fun initView() {
        dialogContainerView = containerView.findViewById(R.id.dialogContainer)
        productCard = containerView.findViewById(R.id.productCard)
        txtTitle = containerView.findViewById(R.id.txtTitle)
        txtDescription = containerView.findViewById(R.id.txtDescription)
        thumbnail = containerView.findViewById(R.id.imgThumbnail)
        productName = containerView.findViewById(R.id.txtProductName)
        productPrice = containerView.findViewById(R.id.txtProductPrice)
        productCampaign = containerView.findViewById(R.id.txtProductCampaign)
        campaignTag = containerView.findViewById(R.id.viewCampaignTag)
        btnReminder = containerView.findViewById(R.id.btnReminder)

        // empty state
        emptyContainer = containerView.findViewById(R.id.empty_container)
        txtEmptyMessage = containerView.findViewById(R.id.txt_message)

        lstProduct = containerView.findViewById(R.id.lstProduct)
        lstProduct.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        viewModel = viewModelProvider(viewModelFactory)

        initView()
        setChild(containerView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        analytics.productCardImpression(element, userSession.userId)
        renderView()
        initObservable()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.cleared()
        dismissAllowingStateLoss()
    }

    private fun initObservable() {
        viewModel.productHighlight.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                emptyContainer.show()
            } else {
                productHighlightList.clear()
                productHighlightList.addAll(it)
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.productStockReminder.observe(viewLifecycleOwner, Observer {
            val successMessage = getString(R.string.product_reminder_success)
            val actionText = getString(R.string.notifcenter_btn_title_ok)
            onSuccessListener(successMessage, actionText)
            btnReminder.isEnabled = false
        })

        viewModel.addToCart.observe(viewLifecycleOwner, Observer {
            listener.onSuccessAddToCart(it.data.message.first())
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            showToastErrorMessage(it)
        })
    }

    private fun renderView() {
        // content view of bottomSheet container and empty state
        setContainerContentView()

        element.getAtcProduct()?.let { product ->
            // set product card data
            buttonReminderValidation(product.typeButton)
            setDataProductCard(product)
            onClickListener(product)

            // get product highlight
            if (product.shop?.id != null) {
                viewModel.getHighlightProduct(product.shop.id.toString())
            }
        }
    }

    private fun setContainerContentView() {
        txtTitle.text = element.title
        txtDescription.text = element.body
        txtEmptyMessage.text = getString(R.string.product_highlight_empty)
    }

    private fun setDataProductCard(product: ProductData) {
        ImageHandler.loadImage2(
                thumbnail,
                product.imageUrl,
                R.drawable.ic_notifcenter_loading_toped
        )

        productName.text = product.name
        productPrice.text = product.priceFormat
        productCampaign.setCampaign(product.campaign)

        setCampaignTag(product)
    }

    private fun setCampaignTag(product: ProductData) {
        if (product.shop?.freeShippingIcon != null &&
            product.shop.freeShippingIcon.isNotEmpty()) {
            campaignTag.loadImage(product.shop.freeShippingIcon)
            campaignTag.show()
        }
    }

    private fun onAtcClicked() {
        viewModel.addProductToCart(element.getAtcProduct())
    }

    private fun onClickListener(product: ProductData) {
        productCard.setOnClickListener {
            analytics.productCardClicked(element, userSession.userId)
            RouteManager.route(
                    context,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    product.productId
            )
        }

        btnReminder.setOnClickListener {
            analytics.stockReminderClicked(element, userSession.userId)
            if (product.stock < SINGLE_PRODUCT_STOCK) {
                viewModel.setProductReminder(product.productId, element.notificationId)
            } else {
                listener.addProductToCheckout(element.userInfo, element)
            }
        }
    }

    private fun onSuccessListener(title: String, actionText: String, onClick: () -> Unit = {}) {
        dialogWindow()?.let {
            val onActionClick = View.OnClickListener { onClick() }
            Toaster.make(it, title, toasterLength, TYPE_NORMAL, actionText, onActionClick)
        }
    }

    private fun showToastErrorMessage(message: String) {
        dialogWindow()?.let {
            Toaster.make(it, message, toasterLength, TYPE_ERROR)
        }
    }

    private fun buttonReminderValidation(type: Int) {
        when(type) {
            TYPE_BUY_BUTTON -> {
                btnReminder.text = context?.getString(R.string.notifcenter_btn_buy)
                btnReminder.buttonType = UnifyButton.Type.TRANSACTION
            }
            TYPE_REMINDER_BUTTON -> {
                btnReminder.text = context?.getString(R.string.notifcenter_btn_reminder)
                btnReminder.buttonType = UnifyButton.Type.MAIN
            }
        }
    }

    private fun initInjector() {
        (activity as NotificationActivity)
                .notificationComponent
                .inject(this)
    }

    companion object {
        private const val TYPE_BUY_BUTTON = 0
        private const val TYPE_REMINDER_BUTTON = 1

        private const val SINGLE_PRODUCT_STOCK = 1
    }
}