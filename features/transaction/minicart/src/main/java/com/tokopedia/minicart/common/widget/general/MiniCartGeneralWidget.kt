package com.tokopedia.minicart.common.widget.general

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import android.widget.Toast
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
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.subpage.globalerror.GlobalErrorBottomSheet
import com.tokopedia.minicart.cartlist.subpage.globalerror.GlobalErrorBottomSheetActionListener
import com.tokopedia.minicart.chatlist.MiniCartChatListBottomSheetV2
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.domain.data.MiniCartCheckoutData
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.GlobalEvent
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.minicart.common.widget.di.DaggerMiniCartWidgetComponent
import com.tokopedia.minicart.common.widget.shoppingsummary.ShoppingSummaryBottomSheet
import com.tokopedia.minicart.databinding.WidgetMiniCartBinding
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.currency.CurrencyFormatUtil
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class MiniCartGeneralWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var miniCartChatListBottomSheet: MiniCartChatListBottomSheetV2

    @Inject
    lateinit var shoppingSummaryBottomSheet: ShoppingSummaryBottomSheet

    @Inject
    lateinit var globalErrorBottomSheet: GlobalErrorBottomSheet

    private var progressDialog: AlertDialog? = null
    private var miniCartWidgetListener: MiniCartWidgetListener? = null
    private var miniCartChevronClickListener: OnClickListener? = null

    private var viewModel: MiniCartGeneralViewModel? = null
    private val binding: WidgetMiniCartBinding

    init {
        binding = WidgetMiniCartBinding.inflate(LayoutInflater.from(context))
        val application = (context as? Activity)?.application
        initializeInjector(application)
    }

    private fun initializeInjector(baseAppComponent: Application?) {
        if (baseAppComponent is BaseMainApplication) {
            DaggerMiniCartWidgetComponent.builder()
                .baseAppComponent(baseAppComponent.baseAppComponent)
                .build()
                .inject(this)
        }
    }

    private fun initializeView(fragment: Fragment) {
        // Init Total Amount Button
        miniCartChevronClickListener = OnClickListener {
            showSimplifiedSummaryBottomSheet(fragment)
        }
        binding.miniCartTotalAmount.apply {
            enableAmountChevron(true)
            amountChevronView.setOnClickListener(miniCartChevronClickListener)
            amountCtaView.setOnClickListener {
                showProgressLoading()
                viewModel?.goToCheckout(GlobalEvent.OBSERVER_MINI_CART_GENERAL_WIDGET)
            }
        }
        binding.imageChevronUnavailable.setOnClickListener(miniCartChevronClickListener)
        // Init Chat Button
        val chatIcon = getIconUnifyDrawable(
            context, IconUnify.CHAT,
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        )
        binding.miniCartTotalAmount.setAdditionalButton(chatIcon)
        binding.miniCartTotalAmount.totalAmountAdditionalButton.setOnClickListener {
            showMiniCartChatListBottomSheet(fragment)
        }
        binding.chatIcon.setImageDrawable(chatIcon)
        // Init Progress Dialog
        context?.let {
            progressDialog = AlertDialog.Builder(it)
                .setView(R.layout.mini_cart_progress_dialog_view)
                .setCancelable(true)
                .create()
        }

        validateTotalAmountView()
    }

    private fun validateTotalAmountView() {
        binding.miniCartTotalAmount.enableAmountChevron(true)
        binding.miniCartTotalAmount.amountChevronView.setOnClickListener(
            miniCartChevronClickListener
        )
    }

    private fun initializeListener(listener: MiniCartWidgetListener) {
        this.miniCartWidgetListener = listener
    }

    private fun initializeViewModel(fragment: Fragment) {
        viewModel =
            ViewModelProvider(fragment, viewModelFactory).get(MiniCartGeneralViewModel::class.java)
        viewModel?.initializeGlobalState()
        observeGlobalStateEvent(fragment)
        observeMiniCartWidgetUiModel(fragment)
    }

    private fun observeGlobalStateEvent(fragment: Fragment) {
        viewModel?.globalEvent?.observe(fragment.viewLifecycleOwner) { globalEvent ->
            when (globalEvent.state) {
                GlobalEvent.STATE_FAILED_LOAD_MINI_CART_SIMPLIFIED_SUMMARY_BOTTOM_SHEET -> {
                    onFailedToLoadMiniCartBottomSheet(globalEvent, fragment)
                }
                GlobalEvent.STATE_FAILED_LOAD_MINI_CART_CHAT_BOTTOM_SHEET -> {
                    onFailedToLoadMiniCartBottomSheet(globalEvent, fragment)
                }
                GlobalEvent.STATE_SUCCESS_TO_CHECKOUT -> {
                    if (globalEvent.observer == GlobalEvent.OBSERVER_MINI_CART_GENERAL_WIDGET) {
                        context?.let {
                            hideProgressLoading()
                            onSuccessGoToCheckout(it)
                        }
                    }
                }
                GlobalEvent.STATE_FAILED_TO_CHECKOUT -> {
                    if (globalEvent.observer == GlobalEvent.OBSERVER_MINI_CART_GENERAL_WIDGET) {
                        onFailedGoToCheckout(globalEvent, fragment)
                    }
                }
            }
        }
    }

    private fun onFailedToLoadMiniCartBottomSheet(globalEvent: GlobalEvent, fragment: Fragment) {
        // TODO: Dismiss simplified summary bottom sheet
        // miniCartSimplifiedSummaryBottomSheet.dismiss()
        miniCartChatListBottomSheet.dismiss()
        if (globalEvent.data != null && globalEvent.data is MiniCartData) {
            val outOfService = (globalEvent.data as MiniCartData).data.outOfService
            if (outOfService.id.isNotBlank() && outOfService.id != "0") {
                fragment.context?.let {
                    globalErrorBottomSheet.show(
                        fragment.parentFragmentManager,
                        it,
                        GlobalError.SERVER_ERROR,
                        outOfService,
                        object : GlobalErrorBottomSheetActionListener {
                            override fun onGoToHome() {
                                RouteManager.route(context, ApplinkConst.HOME)
                            }

                            override fun onRefreshErrorPage() {
                                showSimplifiedSummaryBottomSheet(fragment)
                            }
                        })
                }
            } else {
                showGlobalErrorNoConnection(fragment)
            }
        } else {
            showGlobalErrorNoConnection(fragment)
        }
    }

    private fun showGlobalErrorNoConnection(fragment: Fragment) {
        fragment.context?.let {
            globalErrorBottomSheet.show(
                fragment.parentFragmentManager,
                it,
                GlobalError.NO_CONNECTION,
                null,
                object : GlobalErrorBottomSheetActionListener {
                    override fun onGoToHome() {
                        // No-op
                    }

                    override fun onRefreshErrorPage() {
                        showSimplifiedSummaryBottomSheet(fragment)
                    }
                })
        }
    }

    private fun observeMiniCartWidgetUiModel(fragment: Fragment) {
        viewModel?.miniCartSimplifiedData?.observe(fragment.viewLifecycleOwner) {
            renderWidget(it)
        }
    }

    private fun renderWidget(miniCartSimplifiedData: MiniCartSimplifiedData) {
        if (miniCartSimplifiedData.miniCartWidgetData.containsOnlyUnavailableItems) {
            renderUnavailableWidget(miniCartSimplifiedData)
        } else {
            renderAvailableWidget(miniCartSimplifiedData)
        }
        setTotalAmountLoading(false)
        setAmountViewLayoutParams()
    }

    private fun renderUnavailableWidget(miniCartSimplifiedData: MiniCartSimplifiedData) {
        binding.miniCartTotalAmount.apply {
            setLabelTitle("")
            setAmount("")
            val ctaText = context.getString(R.string.mini_cart_widget_cta_text_default)
            setCtaText(ctaText)
            amountCtaView.isEnabled = false
            amountCtaView.layoutParams.width =
                resources.getDimensionPixelSize(R.dimen.mini_cart_button_buy_width)
            amountCtaView.requestLayout()
        }
        binding.textCannotProcess.apply {
            text = context.getString(R.string.mini_cart_label_cannot_process)
            show()
        }
        binding.textCannotProcessQuantity.apply {
            text = context.getString(
                R.string.mini_cart_cannot_process_quantity,
                miniCartSimplifiedData.miniCartWidgetData.unavailableItemsCount
            )
            show()
        }
        binding.imageChevronUnavailable.show()
    }

    private fun renderAvailableWidget(miniCartSimplifiedData: MiniCartSimplifiedData) {
        binding.miniCartTotalAmount.apply {
            setLabelTitle(context.getString(R.string.mini_cart_widget_label_see_cart))
            setAmount(
                CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    miniCartSimplifiedData.miniCartWidgetData.totalProductPrice,
                    false
                ).removeDecimalSuffix()
            )
            val ctaText = context.getString(R.string.mini_cart_widget_cta_text_default)
            setCtaText("$ctaText (${miniCartSimplifiedData.miniCartWidgetData.totalProductCount})")
            amountCtaView.isEnabled = true
            amountCtaView.layoutParams.width =
                resources.getDimensionPixelSize(R.dimen.mini_cart_button_buy_width)
            amountCtaView.requestLayout()
        }
        binding.textCannotProcess.gone()
        binding.textCannotProcessQuantity.gone()
        binding.imageChevronUnavailable.gone()
    }

    private fun setTotalAmountLoading(isLoading: Boolean) {
        if (isLoading) {
            if (!binding.miniCartTotalAmount.isTotalAmountLoading) {
                binding.miniCartTotalAmount.isTotalAmountLoading = true
            }
        } else {
            if (binding.miniCartTotalAmount.isTotalAmountLoading) {
                binding.miniCartTotalAmount.isTotalAmountLoading = false
            }
        }
        validateTotalAmountView()
    }

    private fun setAmountViewLayoutParams() {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.weight = 0f
        params.setMargins(0, 0, 4.toPx(), 0)
        binding.miniCartTotalAmount.amountView.layoutParams = params
    }

    private fun onSuccessGoToCheckout(context: Context) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.CHECKOUT)
        context.startActivity(intent)
    }

    private fun onFailedGoToCheckout(globalEvent: GlobalEvent, fragment: Fragment) {
        hideProgressLoading()
        setTotalAmountLoading(true)
        fragment.context?.let {
            val rootView = binding.root
            handleFailedGoToCheckout(rootView, context, fragment.parentFragmentManager, globalEvent)
        }
    }

    private fun handleFailedGoToCheckout(
        view: View?,
        context: Context,
        fragmentManager: FragmentManager,
        globalEvent: GlobalEvent
    ) {
        val data = globalEvent.data
        if (data != null && data is MiniCartCheckoutData) {
            // Goes here if failed and get response from BE
            handleFailedGoToCheckoutWithData(view, data, fragmentManager, context, globalEvent)
        } else {
            // Goes here if failed and get no response from BE
            handleFailedGoToCheckoutWithThrowable(view, globalEvent, fragmentManager, context)
        }
    }

    private fun handleFailedGoToCheckoutWithData(
        view: View?,
        miniCartCheckoutData: MiniCartCheckoutData,
        fragmentManager: FragmentManager,
        context: Context,
        globalEvent: GlobalEvent
    ) {
        if (miniCartCheckoutData.outOfService.id.isNotBlank() && miniCartCheckoutData.outOfService.id != "0") {
            // Prioritize to show out of service data
            globalErrorBottomSheet.show(
                fragmentManager,
                context,
                GlobalError.SERVER_ERROR,
                miniCartCheckoutData.outOfService,
                object : GlobalErrorBottomSheetActionListener {
                    override fun onGoToHome() {
                        RouteManager.route(context, ApplinkConst.HOME)
                    }

                    override fun onRefreshErrorPage() {
                        showProgressLoading()
                        viewModel?.goToCheckout(globalEvent.observer)
                    }
                })
        } else {
            // Reload data
            if (globalEvent.observer == GlobalEvent.OBSERVER_MINI_CART_GENERAL_WIDGET) {
                viewModel?.getLatestWidgetState()
            }

            // Show toaster error if have no out of service data
            val ctaText = context.getString(R.string.mini_cart_cta_ok)
            val errorMessage = miniCartCheckoutData.errorMessage
            if (miniCartCheckoutData.toasterAction.showCta) {
                showToaster(view, errorMessage, Toaster.TYPE_ERROR, ctaText, true)
            } else {
                showToaster(view, errorMessage, Toaster.TYPE_ERROR)
            }
        }
    }

    private fun handleFailedGoToCheckoutWithThrowable(
        view: View?,
        globalEvent: GlobalEvent,
        fragmentManager: FragmentManager,
        context: Context
    ) {
        val throwable = globalEvent.throwable
        if (throwable != null) {
            when (throwable) {
                is UnknownHostException -> {
                    globalErrorBottomSheet.show(
                        fragmentManager,
                        context,
                        GlobalError.NO_CONNECTION,
                        null,
                        object : GlobalErrorBottomSheetActionListener {
                            override fun onGoToHome() {
                                // No-op
                            }

                            override fun onRefreshErrorPage() {
                                showProgressLoading()
                                viewModel?.goToCheckout(globalEvent.observer)
                            }
                        })
                }
                is SocketTimeoutException -> {
                    val message =
                        context.getString(R.string.mini_cart_message_error_checkout_timeout)
                    val ctaText = context.getString(R.string.mini_cart_cta_ok)
                    showToaster(view, message, Toaster.TYPE_ERROR, ctaText, true)
                }
                else -> {
                    val message =
                        context.getString(R.string.mini_cart_message_error_checkout_failed)
                    val ctaText = context.getString(R.string.mini_cart_cta_ok)
                    showToaster(view, message, Toaster.TYPE_ERROR, ctaText, true)
                }
            }
        }
    }

    private fun showToaster(
        view: View?,
        message: String,
        type: Int,
        ctaText: String = "",
        isShowCta: Boolean = false,
        onClickListener: OnClickListener? = null
    ) {
        if (message.isBlank()) return
        val toasterViewRoot = view ?: binding.root
        Toaster.toasterCustomBottomHeight = toasterViewRoot.resources?.getDimensionPixelSize(
            com.tokopedia.abstraction.R.dimen.dp_72
        ) ?: 0
        if (isShowCta && ctaText.isNotBlank()) {
            var tmpCtaClickListener = OnClickListener { }
            if (onClickListener != null) {
                tmpCtaClickListener = onClickListener
            }
            Toaster.build(
                toasterViewRoot,
                message,
                Toaster.LENGTH_LONG,
                type,
                ctaText,
                tmpCtaClickListener
            ).show()
        } else {
            Toaster.build(toasterViewRoot, message, Toaster.LENGTH_LONG, type).show()
        }
    }

    private fun showProgressLoading() {
        if (progressDialog?.isShowing == false) {
            progressDialog?.show()
        }
    }

    private fun hideProgressLoading() {
        if (progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
    }

    /**
     * Function to initialize the widget
     */
    fun initialize(
        shopIds: List<String>, fragment: Fragment, listener: MiniCartWidgetListener,
        isShopDirectPurchase: Boolean = true, source: MiniCartSource
    ) {
        removeAllViews()

        if (viewModel == null) {
            initializeView(fragment)
            initializeListener(listener)
            initializeViewModel(fragment)
            viewModel?.isShopDirectPurchase = isShopDirectPurchase
            viewModel?.currentSource = source
            viewModel?.initializeShopIds(shopIds)
        }
        updateData()

        addView(binding.root)
    }

    /**
     * Function to trigger update mini cart data
     * This will trigger view model to fetch latest data from backend and update the UI
     */
    fun updateData() {
        setTotalAmountLoading(true)
        viewModel?.getLatestWidgetState()
    }

    /**
     * Function to trigger update mini cart data
     * This will trigger widget to update the UI with provided data
     */
    fun updateData(miniCartSimplifiedData: MiniCartSimplifiedData) {
        setTotalAmountLoading(true)
        viewModel?.updateMiniCartSimplifiedData(miniCartSimplifiedData)
    }

    /**
     * Function to show mini cart chat bottom sheet
     */
    fun showMiniCartChatListBottomSheet(fragment: Fragment) {
        viewModel?.let {
            miniCartChatListBottomSheet.show(
                fragment.context,
                fragment.parentFragmentManager,
                fragment.viewLifecycleOwner,
                it
            )
        }
    }

    /**
     * Function to show simplified summary bottom sheet
     */
    fun showSimplifiedSummaryBottomSheet(fragment: Fragment) {
        viewModel?.miniCartSimplifiedData?.value?.shoppingSummaryBottomSheetData?.let {
            shoppingSummaryBottomSheet.show(it, fragment.parentFragmentManager, context)
        }
    }
}