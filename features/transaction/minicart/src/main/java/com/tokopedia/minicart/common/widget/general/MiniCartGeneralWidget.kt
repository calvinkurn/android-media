package com.tokopedia.minicart.common.widget.general

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.subpage.globalerror.GlobalErrorBottomSheet
import com.tokopedia.minicart.cartlist.subpage.globalerror.GlobalErrorBottomSheetActionListener
import com.tokopedia.minicart.chatlist.MiniCartChatListBottomSheetV2
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.GlobalEvent
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.minicart.common.widget.di.DaggerMiniCartWidgetComponent
import com.tokopedia.minicart.common.widget.shoppingsummary.ShoppingSummaryBottomSheet
import com.tokopedia.minicart.databinding.WidgetMiniCartBinding
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
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

    private var miniCartWidgetListener: MiniCartWidgetListener? = null

    private var viewModel: MiniCartGeneralViewModel? = null
    private val binding: WidgetMiniCartBinding

    init {
        binding = WidgetMiniCartBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)

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
        binding.miniCartTotalAmount.apply {
            enableAmountChevron(true)
            amountChevronView.setOnClickListener {
                showSimplifiedSummaryBottomSheet(fragment)
            }
            amountCtaView.setOnClickListener {
                RouteManager.route(context, ApplinkConstInternalMarketplace.CART)
            }
        }
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
                GlobalEvent.STATE_FAILED_LOAD_MINI_CART_LIST_BOTTOM_SHEET -> {
                    onFailedToLoadMiniCartBottomSheet(globalEvent, fragment)
                }
            }
        }
    }

    private fun onFailedToLoadMiniCartBottomSheet(globalEvent: GlobalEvent, fragment: Fragment) {
        shoppingSummaryBottomSheet.dismiss()
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
                                // No-op
                            }

                            override fun onRefreshErrorPage() {
                                showMiniCartChatListBottomSheet(fragment)
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
                        showMiniCartChatListBottomSheet(fragment)
                    }
                })
        }
    }

    private fun observeMiniCartWidgetUiModel(fragment: Fragment) {
        viewModel?.miniCartSimplifiedData?.observe(fragment.viewLifecycleOwner) {
            renderWidget(it)
            miniCartWidgetListener?.onCartItemsUpdated(it)
        }
    }

    private fun renderWidget(miniCartSimplifiedData: MiniCartSimplifiedData) {
        if (miniCartSimplifiedData.miniCartWidgetData.isShopActive) {
            renderAvailableWidget(miniCartSimplifiedData)
        } else {
            renderUnavailableWidget(miniCartSimplifiedData)
        }
        setTotalAmountLoading(false)
        setAmountViewLayoutParams()
    }

    private fun renderUnavailableWidget(miniCartSimplifiedData: MiniCartSimplifiedData) {
        binding.miniCartTotalAmount.apply {
            val labelText = miniCartSimplifiedData.miniCartWidgetData.headlineWording
            if (labelText.isNotBlank()) {
                labelTitleView.setWeight(Typography.BOLD)
                labelTitleView.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_RN500))
                setLabelTitle(labelText)
            } else {
                labelTitleView.gone()
            }
            val labelAmountText = miniCartSimplifiedData.miniCartWidgetData.totalProductPriceWording
            if (labelAmountText.isNotBlank()) {
                amountView.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN400))
                setAmount(labelAmountText)
            } else {
                amountView.gone()
            }
            totalAmountAdditionalButton.layoutParams.width = 0
            totalAmountAdditionalButton.requestLayout()
            val ctaText = context.getString(R.string.mini_cart_widget_label_see_cart)
            setCtaText(ctaText)
            enableAmountChevron(false)
        }
    }

    private fun renderAvailableWidget(miniCartSimplifiedData: MiniCartSimplifiedData) {
        binding.miniCartTotalAmount.apply {
            val labelText = miniCartSimplifiedData.miniCartWidgetData.headlineWording.ifBlank {
                context.getString(R.string.mini_cart_widget_label_purchase_summary)
            }
            setLabelTitle(labelText)
            val totalAmountText = miniCartSimplifiedData.miniCartWidgetData.totalProductPriceWording.ifBlank {
                CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    miniCartSimplifiedData.miniCartWidgetData.totalProductPrice,
                    false
                ).removeDecimalSuffix()
            }
            setAmount(totalAmountText)
            val ctaText = context.getString(R.string.mini_cart_widget_label_see_cart)
            setCtaText(ctaText)
            enableAmountChevron(true)
        }
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
    }

    private fun setAmountViewLayoutParams() {
        binding.miniCartTotalAmount.amountCtaView.layoutParams.width =
            resources.getDimensionPixelSize(R.dimen.mini_cart_button_buy_width)
        binding.miniCartTotalAmount.amountCtaView.requestLayout()

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.weight = 0f
        params.setMargins(0, 0, 4.toPx(), 0)
        binding.miniCartTotalAmount.amountView.layoutParams = params
    }

    /**
     * Function to initialize the widget
     */
    fun initialize(
        shopIds: List<String>, fragment: Fragment, listener: MiniCartWidgetListener,
        isShopDirectPurchase: Boolean = true, source: MiniCartSource
    ) {
        if (viewModel == null) {
            initializeView(fragment)
            initializeListener(listener)
            initializeViewModel(fragment)
            viewModel?.isShopDirectPurchase = isShopDirectPurchase
            viewModel?.currentSource = source
            viewModel?.initializeShopIds(shopIds)
        }
        updateData()
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
    private fun showSimplifiedSummaryBottomSheet(fragment: Fragment) {
        viewModel?.miniCartSimplifiedData?.value?.shoppingSummaryBottomSheetData?.let {
            shoppingSummaryBottomSheet.show(it, fragment.parentFragmentManager, context)
        }
    }
}