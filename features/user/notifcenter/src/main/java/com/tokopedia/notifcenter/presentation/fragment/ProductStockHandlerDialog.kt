package com.tokopedia.notifcenter.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.StockHandlerAnalytics
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
    private val adapter by lazy { ProductHighlightAdapter(productHighlightList) }

    private val containerView: View by lazy(LazyThreadSafetyMode.NONE) {
        View.inflate(requireContext(), R.layout.dialog_product_stock_handler, null)
    }

    private val analytics by lazy(LazyThreadSafetyMode.NONE) {
        StockHandlerAnalytics()
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
        renderView(element)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        analytics.productCardImpression(element, userSession.userId)
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
            successReminderProductStock()
        })
    }

    private fun renderView(element: NotificationItemViewBean) {
        txtEmptyMessage.text = "Belum ada produk"

        txtTitle.text = element.title
        txtDescription.text = element.body

        element.getAtcProduct()?.let { product ->
            buttonReminderValidation(product.typeButton)

            if (product.shop?.id != null) {
                viewModel.getHighlightProduct(product.shop.id.toString())
            }

            ImageHandler.loadImage2(
                    thumbnail,
                    product.imageUrl,
                    R.drawable.ic_notifcenter_loading_toped
            )

            productName.text = product.name
            productPrice.text = product.priceFormat
            productCampaign.setCampaign(product.campaign)

            if (product.shop?.freeShippingIcon != null &&
                    product.shop.freeShippingIcon.isNotEmpty()) {
                campaignTag.loadImage(product.shop.freeShippingIcon)
                campaignTag.show()
            }

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
    }

    private fun successReminderProductStock() {
        btnReminder.isEnabled = false
        dialogWindow()?.let {
            Toaster.make(
                    it,
                    "SUKSES",
                    Toaster.toasterLength,
                    Toaster.TYPE_NORMAL,
                    "LIHAT",
                    View.OnClickListener { }
            )
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