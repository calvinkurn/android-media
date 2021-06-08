package com.tokopedia.minicart.common.widget

import android.app.Application
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.GlobalErrorBottomSheet
import com.tokopedia.minicart.cartlist.GlobalErrorBottomSheetActionListener
import com.tokopedia.minicart.cartlist.MiniCartListBottomSheet
import com.tokopedia.minicart.cartlist.MiniCartListBottomSheetListener
import com.tokopedia.minicart.common.data.response.updatecart.Data
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.common.widget.di.DaggerMiniCartWidgetComponent
import com.tokopedia.minicart.common.widget.viewmodel.MiniCartWidgetViewModel
import com.tokopedia.totalamount.TotalAmount
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.currency.CurrencyFormatUtil
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class MiniCartWidget @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr), MiniCartListBottomSheetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var miniCartListBottomSheet: MiniCartListBottomSheet

    @Inject
    lateinit var globalErrorBottomSheet: GlobalErrorBottomSheet

    private var view: View? = null
    private var totalAmount: TotalAmount? = null
    private var chatIcon: ImageUnify? = null
    private var miniCartWidgetListener: MiniCartWidgetListener? = null
    private var progressDialog: AlertDialog? = null

    private var viewModel: MiniCartWidgetViewModel? = null

    init {
        view = inflate(context, R.layout.widget_mini_cart, this)
        chatIcon = view?.findViewById(R.id.chat_icon)
    }

    /*
    * Function to initialize the widget
    * */
    fun initialize(shopIds: List<String>, fragment: Fragment, listener: MiniCartWidgetListener, autoInitializeData: Boolean = true) {
        val application = fragment.activity?.application
        initializeInjector(application)
        initializeView(fragment)
        initializeListener(listener)
        initializeViewModel(fragment)
        if (autoInitializeData) {
            updateData(shopIds)
        }
    }

    private fun initializeListener(listener: MiniCartWidgetListener) {
        miniCartWidgetListener = listener
    }

    private fun initializeViewModel(fragment: Fragment) {
        viewModel = ViewModelProvider(fragment, viewModelFactory).get(MiniCartWidgetViewModel::class.java)
        observeGlobalEvent(fragment)
        observeMiniCartWidgetUiModel(fragment)
    }

    private fun observeGlobalEvent(fragment: Fragment) {
        viewModel?.globalEvent?.observe(fragment.viewLifecycleOwner, {
            when (it.state) {
                GlobalEvent.STATE_FAILED_LOAD_MINI_CART_LIST_BOTTOM_SHEET -> {
                    onFailedToLoadMiniCartBottomSheet(fragment)
                }
                GlobalEvent.STATE_SUCCESS_UPDATE_CART_FOR_CHECKOUT -> {
                    if (it.observer == GlobalEvent.OBSERVER_MINI_CART_WIDGET) {
                        context?.let {
                            hideProgressLoading()
                            onSuccessUpdateCartForCheckout(it)
                        }
                    }
                }
                GlobalEvent.STATE_FAILED_UPDATE_CART_FOR_CHECKOUT -> {
                    if (it.observer == GlobalEvent.OBSERVER_MINI_CART_WIDGET) {
                        hideProgressLoading()
                        onFailedUpdateCartForCheckout(it, fragment)
                    }
                }
            }
        })
    }

    private fun onFailedUpdateCartForCheckout(globalEvent: GlobalEvent, fragment: Fragment) {
        setTotalAmountLoading(true)
        viewModel?.getLatestWidgetState()
        fragment.context?.let { context ->
            handleFailedUpdateCartForCheckout(view, context, fragment.parentFragmentManager, globalEvent)
        }
    }

    private fun handleFailedUpdateCartForCheckout(view: View?, context: Context, fragmentManager: FragmentManager, globalEvent: GlobalEvent) {
        val data = globalEvent.data
        if (data != null) {
            // Goes here if failed but get response from BE
            handleFailedUpdateCartWithOutOfService(view, data, fragmentManager, context)
        } else {
            // Goes here if failed and get no response from BE
            handleFailedUpdateCartWithThrowable(view, globalEvent, fragmentManager, context)
        }
    }

    private fun handleFailedUpdateCartWithOutOfService(view: View?, data: Any?, fragmentManager: FragmentManager, context: Context) {
        if (data is Data) {
            if (data.outOfService.id.isNotBlank() && data.outOfService.id != "0") {
                // Prioritize to show out of service data
                globalErrorBottomSheet.show(fragmentManager, context, GlobalError.SERVER_ERROR, data.outOfService, object : GlobalErrorBottomSheetActionListener {
                    override fun onGoToHome() {
                        RouteManager.route(context, ApplinkConst.HOME)
                    }

                    override fun onRefreshErrorPage() {
                        showProgressLoading()
                        viewModel?.updateCart(true, GlobalEvent.OBSERVER_MINI_CART_WIDGET)
                    }
                })
            } else {
                // Show toaster error if have no out of service data
                var ctaText = "Oke"
                if (data.toasterAction.showCta) {
                    ctaText = data.toasterAction.text
                }
                showToaster(view, data.error, Toaster.TYPE_ERROR, ctaText)
            }
        }
    }

    private fun handleFailedUpdateCartWithThrowable(view: View?, globalEvent: GlobalEvent, fragmentManager: FragmentManager, context: Context) {
        val throwable = globalEvent.throwable
        if (throwable != null) {
            when (throwable) {
                is UnknownHostException -> {
                    globalErrorBottomSheet.show(fragmentManager, context, GlobalError.NO_CONNECTION, null, object : GlobalErrorBottomSheetActionListener {
                        override fun onGoToHome() {
                            // No-op
                        }

                        override fun onRefreshErrorPage() {
                            showProgressLoading()
                            viewModel?.updateCart(true, GlobalEvent.OBSERVER_MINI_CART_WIDGET)
                        }
                    })
                }
                is SocketTimeoutException -> {
                    val message = context.getString(R.string.mini_cart_message_error_checkout_timeout)
                    showToaster(view, message, Toaster.TYPE_ERROR)
                }
                else -> {
                    val message = context.getString(R.string.mini_cart_message_error_checkout_failed)
                    showToaster(view, message, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun onSuccessUpdateCartForCheckout(context: Context) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.CHECKOUT)
        intent.putExtra("EXTRA_IS_ONE_CLICK_SHIPMENT", true)
        context.startActivity(intent)
    }

    private fun onFailedToLoadMiniCartBottomSheet(fragment: Fragment) {
        miniCartListBottomSheet.dismiss()
        fragment.context?.let {
            globalErrorBottomSheet.show(fragment.parentFragmentManager, it, GlobalError.NO_CONNECTION, null, object : GlobalErrorBottomSheetActionListener {
                override fun onGoToHome() {
                    // No-op
                }

                override fun onRefreshErrorPage() {
                    showMiniCartListBottomSheet(fragment)
                }
            })
        }
    }

    private fun observeMiniCartWidgetUiModel(fragment: Fragment) {
        viewModel?.miniCartSimplifiedData?.observe(fragment.viewLifecycleOwner, {
            renderWidget(it.miniCartWidgetData)
        })
    }

    private fun initializeView(fragment: Fragment) {
        totalAmount = view?.findViewById(R.id.mini_cart_total_amount)
        totalAmount?.let {
            it.enableAmountChevron(true)
            it.amountChevronView.setOnClickListener {
                showMiniCartListBottomSheet(fragment)
            }
            it.amountCtaView.setOnClickListener {
                showProgressLoading()
                viewModel?.updateCart(true, GlobalEvent.OBSERVER_MINI_CART_WIDGET)
            }
        }
        setTotalAmountLoading(true)
        setTotalAmountChatIcon()
        initializeProgressDialog(fragment.context)
    }

    private fun initializeProgressDialog(context: Context?) {
        context?.let {
            progressDialog = AlertDialog.Builder(it)
                    .setView(R.layout.mini_cart_progress_dialog_view)
                    .setCancelable(true)
                    .create()
        }
    }

    private fun showMiniCartListBottomSheet(fragment: Fragment) {
        viewModel?.let {
            miniCartListBottomSheet.show(fragment.context, fragment.parentFragmentManager, fragment.viewLifecycleOwner, it, this)
        }
    }

    override fun showToaster(view: View?, message: String, type: Int, ctaText: String, onClickListener: OnClickListener?) {
        if (message.isBlank()) return

        view?.let {
            Toaster.toasterCustomBottomHeight = view.resources?.getDimensionPixelSize(R.dimen.dp_72)
                    ?: 0
            if (ctaText.isNotBlank()) {
                var tmpCtaClickListener = OnClickListener { }
                if (onClickListener != null) {
                    tmpCtaClickListener = onClickListener
                }
                Toaster.build(it, message, Toaster.LENGTH_LONG, type, ctaText, tmpCtaClickListener).show()
            }
        }
    }

    override fun showProgressLoading() {
        if (progressDialog?.isShowing == false) {
            progressDialog?.show()
        }
    }

    override fun hideProgressLoading() {
        if (progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
    }

    /*
    * Function to trigger update mini cart data
    * This will trigger view model to fetch latest data from backend and update the UI
    * */
    fun updateData(shopIds: List<String>) {
        viewModel?.getLatestWidgetState(shopIds)
    }

    /*
    * Function to trigger update mini cart data
    * This will trigger widget to update the UI with provided data
    * */
    fun updateData(miniCartWidgetData: MiniCartWidgetData) {
        renderWidget(miniCartWidgetData)
    }

    private fun initializeInjector(baseAppComponent: Application?) {
        if (baseAppComponent is BaseMainApplication) {
            DaggerMiniCartWidgetComponent.builder()
                    .baseAppComponent(baseAppComponent.baseAppComponent)
                    .build()
                    .inject(this)
        }
    }

    private fun renderWidget(miniCartWidgetData: MiniCartWidgetData) {
        totalAmount?.apply {
            setLabelTitle(context.getString(R.string.mini_cart_widget_label_total_price))
            setAmount(CurrencyFormatUtil.convertPriceValueToIdrFormat(miniCartWidgetData.totalProductPrice, false))
            setCtaText(String.format(context.getString(R.string.mini_cart_widget_label_buy), miniCartWidgetData.totalProductCount))
        }
        setTotalAmountLoading(false)
    }

    private fun setTotalAmountLoading(isLoading: Boolean) {
        if (isLoading) {
            if (totalAmount?.isTotalAmountLoading == false) {
                totalAmount?.isTotalAmountLoading = true
            }
        } else {
            if (totalAmount?.isTotalAmountLoading == true) {
                totalAmount?.isTotalAmountLoading = false
            }
        }
        setTotalAmountChatIcon()
    }

    private fun setTotalAmountChatIcon() {
        totalAmount?.context?.let { context ->
            val chatIcon = getIconUnifyDrawable(context, IconUnify.CHAT, ContextCompat.getColor(context, R.color.Unify_G500))
            totalAmount?.setAdditionalButton(chatIcon)
            totalAmount?.totalAmountAdditionalButton?.setOnClickListener {
                val shopId = viewModel?.currentShopIds?.value?.firstOrNull() ?: "0"
                val intent = RouteManager.getIntent(
                        context, ApplinkConst.TOPCHAT_ROOM_ASKSELLER, shopId
                )
                context.startActivity(intent)
            }
            this.chatIcon?.setImageDrawable(chatIcon)
        }
    }

    override fun onMiniCartListBottomSheetDismissed() {
        viewModel?.getLatestMiniCartData()?.let {
            miniCartWidgetListener?.onCartItemsUpdated(it)
        }
    }

    override fun onBottomSheetSuccessUpdateCartForCheckout() {
        context?.let {
            onSuccessUpdateCartForCheckout(it)
        }
    }

    override fun onBottomSheetFailedUpdateCartForCheckout(toasterAnchorView: View, fragmentManager: FragmentManager, globalEvent: GlobalEvent) {
        context?.let {
            handleFailedUpdateCartForCheckout(toasterAnchorView, it, fragmentManager, globalEvent)
        }
    }

}