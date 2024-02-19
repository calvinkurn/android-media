package com.tokopedia.minicart.v2

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.subpage.globalerror.GlobalErrorBottomSheet
import com.tokopedia.minicart.cartlist.subpage.globalerror.GlobalErrorBottomSheetActionListener
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartCheckoutData
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.di.DaggerMiniCartWidgetComponent
import com.tokopedia.minicart.databinding.WidgetMiniCartV2Binding
import com.tokopedia.minicart.domain.GetMiniCartParam
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.abstraction.R as abstractionR
import com.tokopedia.globalerror.R as globalerrorR

class MiniCartV2Widget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analytics: MiniCartAnalytics

    @Inject
    lateinit var globalErrorBottomSheet: GlobalErrorBottomSheet

    var binding: WidgetMiniCartV2Binding? = null

    private var config: MiniCartV2WidgetConfig = MiniCartV2WidgetConfig()
    private var miniCartWidgetListener: MiniCartV2WidgetListener? = null

    private var progressDialog: AlertDialog? = null

    private var viewModel: MiniCartV2ViewModel? = null

    init {
        binding = WidgetMiniCartV2Binding.inflate(LayoutInflater.from(context), this)
        setOnClickListener {
            // prevent click event from passing through
        }
        initializeInjector()
    }

    private fun initializeInjector() {
        val baseAppComponent = context?.applicationContext
        if (baseAppComponent is BaseMainApplication) {
            DaggerMiniCartWidgetComponent.builder()
                .baseAppComponent(baseAppComponent.baseAppComponent)
                .build()
                .inject(this)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initializeViewModel()
    }

    fun initialize(
        config: MiniCartV2WidgetConfig,
        listener: MiniCartV2WidgetListener
    ) {
        this.config = config
        initializeView(config)
        initializeListener(listener)
        initializeViewModel()
    }

    private fun initializeView(config: MiniCartV2WidgetConfig) {
        binding?.miniCartTotalAmount?.apply {
            topContentView.visibility = if (config.showTopShadow) View.VISIBLE else View.GONE
            amountCtaView.setOnClickListener {
                if (config.overridePrimaryButtonAction) {
                    miniCartWidgetListener?.onPrimaryButtonClickListener()
                } else {
                    sendEventClickBuy()
                    showProgressLoading()
                    viewModel?.goToCheckout()
                }
            }
            if (config.additionalButton != null) {
                totalAmountAdditionalButton.setOnClickListener {
                    miniCartWidgetListener?.onAdditionalButtonClickListener()
                }
            }
        }
        initializeProgressDialog(context)
    }

    private fun initializeProgressDialog(context: Context?) {
        context?.let {
            progressDialog = AlertDialog.Builder(it)
                .setView(R.layout.mini_cart_progress_dialog_view)
                .setCancelable(true)
                .create()
        }
    }

    private fun initializeListener(listener: MiniCartV2WidgetListener) {
        miniCartWidgetListener = listener
    }

    private fun initializeViewModel() {
        if (viewModel != null) return
        val lifecycleOwner = findViewTreeLifecycleOwner() ?: return
        val viewModelStoreOwner = findViewTreeViewModelStoreOwner() ?: return
        viewModel = ViewModelProvider(
            viewModelStoreOwner,
            viewModelFactory
        )[MiniCartV2ViewModel::class.java]
        observeGlobalEvent(lifecycleOwner)
        observeMiniCartLoadingState(lifecycleOwner)
        observeMiniCartWidgetUiModel(lifecycleOwner)
    }

    private fun observeGlobalEvent(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel?.globalEvent?.collect {
                    when (it) {
                        is MiniCartV2GlobalEvent.FailToLoadMiniCart -> {
                            miniCartWidgetListener?.onFailedToLoadMiniCartWidget()
                        }

                        is MiniCartV2GlobalEvent.SuccessGoToCheckout -> {
                            context?.let { context ->
                                hideProgressLoading()
                                onSuccessGoToCheckout(context)
                            }
                        }

                        is MiniCartV2GlobalEvent.FailGoToCheckout -> {
                            onFailedGoToCheckout(it)
                        }
                    }
                }
            }
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

    private fun sendEventClickBuy() {
        val products = viewModel?.miniCartSimplifiedData?.value?.miniCartItems?.values?.toList()
            ?: emptyList()
        val isOCCFlow = viewModel?.miniCartABTestData?.value?.isOCCFlow ?: false
        analytics.eventClickBuy(config.page, products, isOCCFlow)
    }

    private fun onSuccessGoToCheckout(context: Context) {
        val intent = if (viewModel?.miniCartABTestData?.value?.isOCCFlow == true) {
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT)
        } else {
            val pageSource = when (config.page) {
                MiniCartAnalytics.Page.HOME_PAGE -> "$MINICART_PAGE_SOURCE - homepage"
                MiniCartAnalytics.Page.SEARCH_PAGE -> "$MINICART_PAGE_SOURCE - search result"
                MiniCartAnalytics.Page.CATEGORY_PAGE -> "$MINICART_PAGE_SOURCE category page"
                MiniCartAnalytics.Page.DISCOVERY_PAGE -> "$MINICART_PAGE_SOURCE discovery page"
                MiniCartAnalytics.Page.RECOMMENDATION_INFINITE -> "$MINICART_PAGE_SOURCE recommendation infinite page"
                else -> ""
            }
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.CHECKOUT)
                .putExtra(CheckoutConstant.EXTRA_CHECKOUT_PAGE_SOURCE, pageSource)
        }

        context.startActivity(intent)
    }

    private fun onFailedGoToCheckout(globalEvent: MiniCartV2GlobalEvent.FailGoToCheckout) {
        hideProgressLoading()
        context?.let { ctx ->
            miniCartWidgetListener?.getFragmentManager()?.let { fm ->
                handleFailedGoToCheckout(binding?.root, ctx, fm, globalEvent)
            }
        }
    }

    private fun handleFailedGoToCheckout(
        view: View?,
        context: Context,
        fragmentManager: FragmentManager,
        globalEvent: MiniCartV2GlobalEvent.FailGoToCheckout
    ) {
        val data = globalEvent.data
        if (data != null) {
            // Goes here if failed and get response from BE
            handleFailedGoToCheckoutWithData(view, data, fragmentManager, context)
        } else {
            // Goes here if failed and get no response from BE
            handleFailedGoToCheckoutWithThrowable(view, globalEvent, fragmentManager, context)
        }
    }

    private fun handleFailedGoToCheckoutWithData(
        view: View?,
        miniCartCheckoutData: MiniCartCheckoutData,
        fragmentManager: FragmentManager,
        context: Context
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
                        viewModel?.goToCheckout()
                    }
                }
            )
            val isOCCFlow = viewModel?.miniCartABTestData?.value?.isOCCFlow ?: false
            analytics.eventClickBuyThenGetBottomSheetError(
                miniCartCheckoutData.outOfService.description,
                isOCCFlow
            )
        } else {
            // Reload data
            miniCartWidgetListener?.onFailedToGoToCheckoutPage()

            // Show toaster error if have no out of service data
            val ctaText = context.getString(R.string.mini_cart_cta_ok)
            val errorMessage = miniCartCheckoutData.errorMessage
            if (miniCartCheckoutData.toasterAction.showCta) {
                showToaster(view, errorMessage, Toaster.TYPE_ERROR, ctaText) {
                    analytics.eventClickAtcToasterErrorCta(errorMessage, ctaText)
                }
            } else {
                showToaster(view, errorMessage, Toaster.TYPE_ERROR, isShowCta = false)
            }
            val isOCCFlow = viewModel?.miniCartABTestData?.value?.isOCCFlow ?: false
            analytics.eventClickBuyThenGetToasterError(errorMessage, isOCCFlow)
        }
    }

    private fun handleFailedGoToCheckoutWithThrowable(
        view: View?,
        globalEvent: MiniCartV2GlobalEvent.FailGoToCheckout,
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
                                viewModel?.goToCheckout()
                            }
                        }
                    )
                    val isOCCFlow = viewModel?.miniCartABTestData?.value?.isOCCFlow ?: false
                    analytics.eventClickBuyThenGetBottomSheetError(
                        context.getString(globalerrorR.string.noConnectionTitle),
                        isOCCFlow
                    )
                }

                is SocketTimeoutException -> {
                    val message =
                        context.getString(R.string.mini_cart_message_error_checkout_timeout)
                    val ctaText = context.getString(R.string.mini_cart_cta_ok)
                    showToaster(view, message, Toaster.TYPE_ERROR, ctaText) {
                        analytics.eventClickAtcToasterErrorCta(message, ctaText)
                    }
                    val isOCCFlow = viewModel?.miniCartABTestData?.value?.isOCCFlow ?: false
                    analytics.eventClickBuyThenGetToasterError(message, isOCCFlow)
                }

                else -> {
                    val message =
                        context.getString(R.string.mini_cart_message_error_checkout_failed)
                    val ctaText = context.getString(R.string.mini_cart_cta_ok)
                    showToaster(view, message, Toaster.TYPE_ERROR, ctaText) {
                        analytics.eventClickAtcToasterErrorCta(message, ctaText)
                    }
                    val isOCCFlow = viewModel?.miniCartABTestData?.value?.isOCCFlow ?: false
                    analytics.eventClickBuyThenGetToasterError(message, isOCCFlow)
                }
            }
        }
    }

    private fun showToaster(
        view: View? = null,
        message: String,
        type: Int,
        ctaText: String = "Oke",
        isShowCta: Boolean = true,
        onClickListener: View.OnClickListener? = null
    ) {
        if (message.isBlank()) return
        var toasterViewRoot = view
        if (toasterViewRoot == null) toasterViewRoot = this.binding?.root
        toasterViewRoot?.let {
            Toaster.toasterCustomBottomHeight = it.resources?.getDimensionPixelSize(
                abstractionR.dimen.dp_72
            ) ?: 0
            if (isShowCta && ctaText.isNotBlank()) {
                var tmpCtaClickListener = View.OnClickListener { }
                if (onClickListener != null) {
                    tmpCtaClickListener = onClickListener
                }
                Toaster.build(it, message, Toaster.LENGTH_LONG, type, ctaText, tmpCtaClickListener)
                    .show()
            } else {
                Toaster.build(it, message, Toaster.LENGTH_LONG, type).show()
            }
        }
    }

    private fun observeMiniCartLoadingState(lifecycleOwner: LifecycleOwner) {
        viewModel?.miniCartLoadingState?.observe(lifecycleOwner) {
            setTotalAmountLoading(it)
        }
    }

    private fun observeMiniCartWidgetUiModel(lifecycleOwner: LifecycleOwner) {
        viewModel?.miniCartSimplifiedData?.observe(lifecycleOwner) {
            renderWidget(it)
            miniCartWidgetListener?.onCartItemsUpdated(it)
        }
    }

    private fun renderWidget(miniCartSimplifiedData: MiniCartSimplifiedData) {
        if (!miniCartSimplifiedData.isShowMiniCartWidget) {
            renderEmptyWidget(miniCartSimplifiedData)
        } else if (miniCartSimplifiedData.miniCartWidgetData.containsOnlyUnavailableItems) {
            renderUnavailableWidget(miniCartSimplifiedData)
        } else {
            renderAvailableWidget(miniCartSimplifiedData)
            binding?.miniCartTotalAmount?.apply {
                setAdditionalButton(config.additionalButton)
            }
        }
        setAmountViewLayoutParams()
        validateAmountCtaLabel(miniCartSimplifiedData)
    }

    private fun renderEmptyWidget(miniCartSimplifiedData: MiniCartSimplifiedData) {
        binding?.miniCartTotalAmount?.apply {
            setLabelTitle(
                miniCartSimplifiedData.miniCartWidgetData.headlineWording.ifBlank {
                    context.getString(
                        R.string.mini_cart_widget_label_total_price
                    )
                }
            )
            setAmount("Rp-")
            setAmountSuffix("")
            val overridePrimaryButtonWording = config.overridePrimaryButtonWording
            if (overridePrimaryButtonWording.isNullOrBlank()) {
                val ctaText = viewModel?.miniCartABTestData?.value?.buttonBuyWording
                    ?: context.getString(R.string.mini_cart_widget_cta_text_default)
                setCtaText(ctaText)
            } else {
                setCtaText(overridePrimaryButtonWording)
            }
            amountCtaView.isEnabled = false
            amountCtaView.layoutParams.width =
                resources.getDimensionPixelSize(R.dimen.mini_cart_button_buy_width)
            amountCtaView.requestLayout()
            setAdditionalButton(null)
        }
        binding?.textCannotProcess?.gone()
        binding?.textCannotProcessQuantity?.gone()
        binding?.imageChevronUnavailable?.gone()
    }

    private fun renderUnavailableWidget(miniCartSimplifiedData: MiniCartSimplifiedData) {
        binding?.miniCartTotalAmount?.apply {
            setLabelTitle("")
            setAmount("")
            setAmountSuffix("")
            val overridePrimaryButtonWording = config.overridePrimaryButtonWording
            if (overridePrimaryButtonWording.isNullOrBlank()) {
                val ctaText = viewModel?.miniCartABTestData?.value?.buttonBuyWording
                    ?: context.getString(R.string.mini_cart_widget_cta_text_default)
                setCtaText(ctaText)
            } else {
                setCtaText(overridePrimaryButtonWording)
            }
            amountCtaView.isEnabled = false
            amountCtaView.layoutParams.width =
                resources.getDimensionPixelSize(R.dimen.mini_cart_button_buy_width)
            amountCtaView.requestLayout()
            setAdditionalButton(null)
        }
        binding?.textCannotProcess?.apply {
            text = context.getString(R.string.mini_cart_label_cannot_process)
            show()
        }
        binding?.textCannotProcessQuantity?.apply {
            text = context.getString(
                R.string.mini_cart_cannot_process_quantity,
                miniCartSimplifiedData.miniCartWidgetData.unavailableItemsCount
            )
            show()
        }
        binding?.imageChevronUnavailable?.show()
        binding?.textCannotProcess?.setOnClickListener {
            miniCartWidgetListener?.onChevronClickListener()
        }
        binding?.textCannotProcessQuantity?.setOnClickListener {
            miniCartWidgetListener?.onChevronClickListener()
        }
        binding?.imageChevronUnavailable?.setOnClickListener {
            miniCartWidgetListener?.onChevronClickListener()
        }
    }

    private fun renderAvailableWidget(miniCartSimplifiedData: MiniCartSimplifiedData) {
        binding?.miniCartTotalAmount?.apply {
            setLabelTitle(
                miniCartSimplifiedData.miniCartWidgetData.headlineWording.ifBlank {
                    context.getString(
                        R.string.mini_cart_widget_label_total_price
                    )
                }
            )
            setAmount(
                CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    miniCartSimplifiedData.miniCartWidgetData.totalProductPrice,
                    false
                ).removeDecimalSuffix()
            )
            if (config.showOriginalTotalPrice && miniCartSimplifiedData.miniCartWidgetData.totalProductOriginalPrice > 0) {
                val originalPriceStr = String.format(
                    CROSSED_TEXT_FORMAT,
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        miniCartSimplifiedData.miniCartWidgetData.totalProductOriginalPrice,
                        false
                    ).removeDecimalSuffix()
                )
                setAmountSuffix(originalPriceStr.parseAsHtml())
            } else {
                setAmountSuffix("")
            }
            val overridePrimaryButtonWording = config.overridePrimaryButtonWording
            if (overridePrimaryButtonWording.isNullOrBlank()) {
                val ctaText = viewModel?.miniCartABTestData?.value?.buttonBuyWording
                    ?: context.getString(R.string.mini_cart_widget_cta_text_default)
                setCtaText("$ctaText (${miniCartSimplifiedData.miniCartWidgetData.totalProductCount})")
            } else {
                setCtaText(overridePrimaryButtonWording)
            }
            amountCtaView.isEnabled = true
            amountCtaView.layoutParams.width =
                resources.getDimensionPixelSize(R.dimen.mini_cart_button_buy_width)
            amountCtaView.requestLayout()
            setAdditionalButton(config.additionalButton)
        }
        binding?.textCannotProcess?.gone()
        binding?.textCannotProcessQuantity?.gone()
        binding?.imageChevronUnavailable?.gone()
    }

    private fun setTotalAmountLoading(isLoading: Boolean) {
        if (isLoading) {
            if (binding?.miniCartTotalAmount?.isTotalAmountLoading == false) {
                binding?.miniCartTotalAmount?.isTotalAmountLoading = true
            }
        } else {
            if (binding?.miniCartTotalAmount?.isTotalAmountLoading == true) {
                binding?.miniCartTotalAmount?.isTotalAmountLoading = false
                binding?.miniCartTotalAmount?.amountCtaView?.visibility = View.VISIBLE
            }
            validateTotalAmountView()
        }
    }

    private fun validateTotalAmountView() {
        if (config.showChevron) {
            binding?.miniCartTotalAmount?.enableAmountChevron(true)
            binding?.miniCartTotalAmount?.labelTitleView?.setOnClickListener {
                miniCartWidgetListener?.onChevronClickListener()
            }
            binding?.miniCartTotalAmount?.amountView?.setOnClickListener {
                miniCartWidgetListener?.onChevronClickListener()
            }
            binding?.miniCartTotalAmount?.amountChevronView?.setOnClickListener {
                miniCartWidgetListener?.onChevronClickListener()
            }
        } else {
            binding?.miniCartTotalAmount?.enableAmountChevron(false)
        }
    }

    private fun setAmountViewLayoutParams() {
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.weight = 0f
        lp.setMargins(0, 0, 4.toPx(), 0)
        binding?.miniCartTotalAmount?.amountView?.layoutParams = lp
    }

    private fun validateAmountCtaLabel(miniCartSimplifiedData: MiniCartSimplifiedData) {
        if (viewModel?.miniCartABTestData?.value?.isOCCFlow == true && config.overridePrimaryButtonWording.isNullOrBlank()) {
            // Change button from `Beli Langsung` to `Beli` if ellipsis
            binding?.miniCartTotalAmount?.post {
                val ellipsis =
                    binding?.miniCartTotalAmount?.amountCtaView?.layout?.getEllipsisCount(0) ?: 0
                if (ellipsis > 0) {
                    val ctaText = context.getString(R.string.mini_cart_widget_cta_text_default)
                    if (miniCartSimplifiedData.miniCartWidgetData.containsOnlyUnavailableItems) {
                        binding?.miniCartTotalAmount?.setCtaText(ctaText)
                    } else {
                        binding?.miniCartTotalAmount?.setCtaText("$ctaText (${miniCartSimplifiedData.miniCartWidgetData.totalProductCount})")
                    }
                }
            }
        }
    }

    /*
    * Function to trigger update mini cart data
    * This will trigger view model to fetch latest data from backend and update the UI
    * */
    fun refresh(param: GetMiniCartParam) {
        viewModel?.getLatestWidgetState(param)
    }

    /*
    * Function to trigger update mini cart data
    * This will trigger widget to update the UI with provided data
    * */
    fun refresh(miniCartSimplifiedData: MiniCartSimplifiedData) {
        viewModel?.updateMiniCartLoadingState(false)
        viewModel?.setMiniCartABTestData(
            miniCartSimplifiedData.miniCartWidgetData.isOCCFlow,
            miniCartSimplifiedData.miniCartWidgetData.buttonBuyWording
        )
        viewModel?.updateMiniCartSimplifiedData(miniCartSimplifiedData)
    }

    fun showLoading() {
        viewModel?.updateMiniCartLoadingState(true)
    }

    fun dismissLoading() {
        viewModel?.updateMiniCartLoadingState(false)
    }

    companion object {
        private const val MINICART_PAGE_SOURCE = "minicart - tokonow"

        private const val CROSSED_TEXT_FORMAT = "<del>%s</del>"
    }

    data class MiniCartV2WidgetConfig(
        val showTopShadow: Boolean = true,
        val showChevron: Boolean = true,
        val showOriginalTotalPrice: Boolean = false,
        val overridePrimaryButtonAction: Boolean = false,
        val overridePrimaryButtonWording: String? = null,
        val additionalButton: Drawable? = null,
        val page: MiniCartAnalytics.Page = MiniCartAnalytics.Page.HOME_PAGE
    )
}
