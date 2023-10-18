package com.tokopedia.minicart.common.simplified

import android.app.Application
import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.AttrRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.minicart.R
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.promo.domain.data.ValidateUseMvcData
import com.tokopedia.minicart.common.promo.widget.PromoProgressBarWidget
import com.tokopedia.minicart.common.widget.di.DaggerMiniCartWidgetComponent
import com.tokopedia.minicart.databinding.WidgetMiniCartSimplifiedBinding
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MiniCartSimplifiedWidget : BaseCustomView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analytics: MiniCartAnalytics

    private var viewModel: MiniCartSimplifiedViewModel? = null

    private var widgetListener: MiniCartSimplifiedWidgetListener? = null

    private var binding: WidgetMiniCartSimplifiedBinding =
        WidgetMiniCartSimplifiedBinding.inflate(LayoutInflater.from(context), this, true)
    private var promoProgressBar: PromoProgressBarWidget = PromoProgressBarWidget(context)

    private var animationDebounceJob: Job? = null
    private var lastFailedValidateMoveToCartMessage: String = ""
    private var lastFailedValidateMoveToCart: Long = FAILED_VALIDATE_TIME_DEFAULT
    private var lastFailedValidateErrorMessage: String = ""
    private var lastFailedValidateError: Long = FAILED_VALIDATE_TIME_DEFAULT

    /*
    * Function to initialize the widget
    * */
    fun initialize(
        shopIds: List<String>,
        promoId: String,
        promoCode: String,
        businessUnit: String,
        currentSite: String,
        source: MiniCartSource,
        fragment: Fragment,
        pageSource: MiniCartAnalytics.Page,
        listener: MiniCartSimplifiedWidgetListener
    ) {
        if (viewModel == null) {
            val application = fragment.activity?.application
            initializeInjector(application)
            initializeView()
            initializeListener(listener)
            initializeViewModel(fragment)
        }
        viewModel?.currentShopIds = shopIds
        viewModel?.currentPromoId = promoId
        viewModel?.currentPromoCode = promoCode
        viewModel?.currentBusinessUnit = businessUnit
        viewModel?.currentSite = currentSite
        viewModel?.currentPageSource = pageSource
        viewModel?.source = source
    }

    private fun initializeInjector(baseAppComponent: Application?) {
        if (baseAppComponent is BaseMainApplication) {
            DaggerMiniCartWidgetComponent.builder()
                .baseAppComponent(baseAppComponent.baseAppComponent)
                .build()
                .inject(this)
        }
    }

    private fun initializeView() {
        setTotalAmountLoading(true)
        binding.miniCartSimplifiedTotalAmount.apply {
            setLabelTitle(context.getString(R.string.mini_cart_widget_label_total_price))
            setCustomTopContent(promoProgressBar)
            customContentCardView.cardType = CardUnify.TYPE_CLEAR
            amountCtaView.setOnClickListener {
                setTotalAmountLoading(true)
                viewModel?.moveToCart()
                sendEventClickCheckCart()
            }
        }
    }

    private fun initializeListener(listener: MiniCartSimplifiedWidgetListener) {
        widgetListener = listener
    }

    private fun initializeViewModel(fragment: Fragment) {
        viewModel = ViewModelProvider(fragment, viewModelFactory).get(MiniCartSimplifiedViewModel::class.java)
        observeMiniCartWidgetUiModel(fragment)
        observeValidateUseMvcData(fragment)
        observeMiniCartEvent(fragment)
    }

    private fun observeMiniCartEvent(fragment: Fragment) {
        viewModel?.miniCartSimplifiedState?.observe(fragment.viewLifecycleOwner) { state ->
            when (state.state) {
                MiniCartSimplifiedState.STATE_FAILED_VALIDATE_USE -> {
                    onFailedValidateUseMvc(fragment, state)
                }
                MiniCartSimplifiedState.STATE_FAILED_VALIDATE_USE_MOVE_TO_CART -> {
                    onFailedValidateUseMvcMoveToCart(fragment, state)
                }
                MiniCartSimplifiedState.STATE_FAILED_MINICART -> {
                    onFailedGetMiniCart(fragment, state)
                }
                MiniCartSimplifiedState.STATE_MOVE_TO_CART -> {
                    onMoveToCart()
                }
                else -> {
                    /* no-op */
                }
            }
        }
    }

    private fun onFailedValidateUseMvc(fragment: Fragment, state: MiniCartSimplifiedState) {
        setTotalAmountLoading(false)
        promoProgressBar.visibility = GONE
        binding.miniCartSimplifiedTotalAmount.hideTopContent()
        val throwable = state.throwable
        val currentTime = SystemClock.elapsedRealtime()
        if (throwable is MessageErrorException && throwable.message == lastFailedValidateErrorMessage) {
            // show toaster when first time error or over time limit after last error
            if (lastFailedValidateError == FAILED_VALIDATE_TIME_DEFAULT || currentTime - lastFailedValidateError >= FAILED_VALIDATE_TIME_LIMIT) {
                lastFailedValidateError = currentTime
                lastFailedValidateErrorMessage = throwable.message ?: ""
                showToasterError(fragment, throwable)
            }
        } else {
            if (throwable is MessageErrorException) {
                lastFailedValidateError = currentTime
                lastFailedValidateErrorMessage = throwable.message ?: ""
            } else if (throwable != null) {
                lastFailedValidateError = FAILED_VALIDATE_TIME_DEFAULT
                lastFailedValidateErrorMessage = ""
            }
            showToasterError(fragment, throwable)
        }
    }

    private fun onFailedValidateUseMvcMoveToCart(fragment: Fragment, state: MiniCartSimplifiedState) {
        setTotalAmountLoading(false)
        promoProgressBar.visibility = GONE
        binding.miniCartSimplifiedTotalAmount.hideTopContent()
        val throwable = state.throwable
        val currentTime = SystemClock.elapsedRealtime()
        if (throwable is MessageErrorException && throwable.message == lastFailedValidateMoveToCartMessage) {
            // move to cart if last error time is below time limit, if not, then should show toaster
            if (lastFailedValidateMoveToCart > FAILED_VALIDATE_TIME_DEFAULT && currentTime - lastFailedValidateMoveToCart < FAILED_VALIDATE_TIME_LIMIT) {
                widgetListener?.onClickCheckCart(RouteManager.getIntent(context, ApplinkConstInternalMarketplace.CART), false)
                lastFailedValidateMoveToCart = FAILED_VALIDATE_TIME_DEFAULT
                lastFailedValidateError = FAILED_VALIDATE_TIME_DEFAULT
            } else {
                lastFailedValidateMoveToCart = currentTime
                lastFailedValidateMoveToCartMessage = throwable.message ?: ""
                showToasterError(fragment, throwable)
            }
        } else {
            if (throwable is MessageErrorException) {
                lastFailedValidateMoveToCart = currentTime
                lastFailedValidateMoveToCartMessage = throwable.message ?: ""
            } else if (throwable != null) {
                lastFailedValidateMoveToCart = FAILED_VALIDATE_TIME_DEFAULT
                lastFailedValidateMoveToCartMessage = ""
            }
            showToasterError(fragment, throwable)
        }
    }

    private fun onFailedGetMiniCart(fragment: Fragment, state: MiniCartSimplifiedState) {
        setTotalAmountLoading(false)
        showToasterError(fragment, state.throwable)
    }

    private fun onMoveToCart() {
        setTotalAmountLoading(false)
        widgetListener?.onClickCheckCart(RouteManager.getIntent(context, ApplinkConstInternalMarketplace.CART), true)
        lastFailedValidateMoveToCart = FAILED_VALIDATE_TIME_DEFAULT
        lastFailedValidateError = FAILED_VALIDATE_TIME_DEFAULT
    }

    private fun observeMiniCartWidgetUiModel(fragment: Fragment) {
        viewModel?.miniCartSimplifiedData?.observe(fragment.viewLifecycleOwner) {
            renderWidget(it)
            widgetListener?.onCartItemsUpdated(it)
        }
    }

    private fun observeValidateUseMvcData(fragment: Fragment) {
        viewModel?.validateUseMvcData?.observe(fragment.viewLifecycleOwner) {
            renderPromoWidget(it)
            setTotalAmountLoading(false)
            sendEventMvcProgressBarImpression(isClickCheckCart = false)
            lastFailedValidateMoveToCart = FAILED_VALIDATE_TIME_DEFAULT
            lastFailedValidateError = FAILED_VALIDATE_TIME_DEFAULT
        }
    }

    private fun renderWidget(data: MiniCartSimplifiedData) {
        binding.miniCartSimplifiedTotalAmount.apply {
            setAmount(CurrencyFormatUtil.convertPriceValueToIdrFormat(data.miniCartWidgetData.totalProductPrice, false).removeDecimalSuffix())
            setCtaText("${context.getString(R.string.mini_cart_widget_label_see_cart)} (${data.miniCartWidgetData.totalProductCount})")
            amountCtaView.isEnabled = data.miniCartWidgetData.totalProductCount > 0
        }
        setTotalAmountLoading(false)
    }

    private fun renderPromoWidget(data: ValidateUseMvcData) {
        if (promoProgressBar.visibility != View.VISIBLE) {
            promoProgressBar.visibility = View.VISIBLE
            binding.miniCartSimplifiedTotalAmount.showTopContent()
        }
        promoProgressBar.updateProgress(data.progressPercentage)
        promoProgressBar.updatePromoText(data.message)
    }

    /*
    * Function to trigger update mini cart data
    * This will trigger view model to fetch latest data from backend and update the UI
    * */
    fun updateData() {
        setTotalAmountLoading(true)
        viewModel?.getLatestWidgetState()
    }

    private fun setTotalAmountLoading(isLoading: Boolean) {
        if (isLoading) {
            if (!binding.miniCartSimplifiedTotalAmount.isTotalAmountLoading) {
                binding.miniCartSimplifiedTotalAmount.isTotalAmountLoading = true
            }
        } else {
            if (binding.miniCartSimplifiedTotalAmount.isTotalAmountLoading) {
                binding.miniCartSimplifiedTotalAmount.isTotalAmountLoading = false
            }
        }
    }

    private fun showToasterError(fragment: Fragment, throwable: Throwable?) {
        if (throwable == null) {
            return
        }
        fragment.viewLifecycleOwner.lifecycleScope.launch {
            delay(TOASTER_ERROR_DELAY)
            if (fragment.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
                fragment.view?.let {
                    Toaster.toasterCustomBottomHeight = TOASTER_BOTTOM_HEIGHT.toPx()
                    Toaster.build(
                        it,
                        ErrorHandler.getErrorMessage(
                            it.context,
                            throwable,
                            ErrorHandler.Builder().withErrorCode(false).build()
                        ),
                        Toaster.LENGTH_SHORT,
                        Toaster.TYPE_ERROR,
                        actionText = context.getString(R.string.mini_cart_cta_ok)
                    ).show()
                }
            }
        }
    }

    fun setScrollAnimation(recyclerView: RecyclerView, coroutineScope: CoroutineScope) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (promoProgressBar.visibility == View.VISIBLE) {
                    binding.miniCartSimplifiedTotalAmount.addTopContentTranslationY(dy.toFloat())
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (promoProgressBar.visibility == View.VISIBLE) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        animationDebounceJob = coroutineScope.launch(Dispatchers.Main) {
                            delay(ANIMATION_DEBOUNCE_DELAY)
                            binding.miniCartSimplifiedTotalAmount.showTopContent()
                        }
                    } else {
                        animationDebounceJob?.cancel()
                    }
                } else {
                    animationDebounceJob?.cancel()
                }
            }
        })
    }

    private fun sendEventClickCheckCart() {
        val miniCartSimplifiedData = viewModel?.miniCartSimplifiedData?.value ?: return
        val validateUseMvcData = viewModel?.validateUseMvcData?.value ?: return
        analytics.eventClickCheckCart(
            basketSize = miniCartSimplifiedData.miniCartWidgetData.totalProductPrice.toString(),
            isFulfilled = validateUseMvcData.progressPercentage >= 100,
            shopId = viewModel?.currentShopIds?.joinToString() ?: "",
            pageSource = viewModel?.currentPageSource,
            businessUnit = viewModel?.currentBusinessUnit ?: "",
            currentSite = viewModel?.currentSite ?: "",
            trackerId = null
        )
        sendEventMvcProgressBarImpression(isClickCheckCart = true)
    }

    private fun sendEventMvcProgressBarImpression(isClickCheckCart: Boolean) {
        if (isClickCheckCart || viewModel?.isFirstValidate == true) {
            val miniCartSimplifiedData = viewModel?.miniCartSimplifiedData?.value ?: return
            val validateUseMvcData = viewModel?.validateUseMvcData?.value ?: return
            analytics.eventMvcProgressBarImpression(
                basketSize = miniCartSimplifiedData.miniCartWidgetData.totalProductPrice.toString(),
                promoPercentage = validateUseMvcData.progressPercentage.toString(),
                shopId = viewModel?.currentShopIds?.joinToString() ?: "",
                businessUnit = viewModel?.currentBusinessUnit ?: "",
                currentSite = viewModel?.currentSite ?: ""
            )
            viewModel?.isFirstValidate = false
        }
    }

    companion object {
        private const val ANIMATION_DEBOUNCE_DELAY = 2000L
        private const val TOASTER_BOTTOM_HEIGHT = 100

        private const val FAILED_VALIDATE_TIME_LIMIT = 10 * 60 * 1000
        private const val FAILED_VALIDATE_TIME_DEFAULT = -1L

        private const val TOASTER_ERROR_DELAY = 750L
    }
}
