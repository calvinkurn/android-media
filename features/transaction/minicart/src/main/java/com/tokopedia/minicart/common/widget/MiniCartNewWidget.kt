package com.tokopedia.minicart.common.widget

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.minicart.R
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.di.DaggerMiniCartWidgetComponent
import com.tokopedia.minicart.databinding.WidgetMiniCartBinding
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.currency.CurrencyFormatUtil
import javax.inject.Inject

class MiniCartNewWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analytics: MiniCartAnalytics

    var binding: WidgetMiniCartBinding? = null

    private var miniCartWidgetListener: MiniCartWidgetListener? = null
    private var progressDialog: AlertDialog? = null
    private var miniCartChevronClickListener: OnClickListener? = null
    private var coachMark: CoachMark2? = null

    private var viewModel: MiniCartViewModel? = null

    init {
        binding = WidgetMiniCartBinding.inflate(LayoutInflater.from(context), this, true)
        binding?.miniCartContainer?.setOnClickListener {
            // prevent click event from passing through
        }
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

    fun initialize(
        config: MiniCartNewWidgetConfig,
        viewModelStoreOwner: ViewModelStoreOwner,
        lifecycleOwner: LifecycleOwner,
        listener: MiniCartWidgetListener
    ) {
        initializeView(config)
        initializeListener(listener)
        initializeViewModel(viewModelStoreOwner, lifecycleOwner)
    }

    private fun initializeView(config: MiniCartNewWidgetConfig) {
        binding?.miniCartTotalAmount?.apply {
            topContentView.visibility = if (config.showTopShadow) View.VISIBLE else View.GONE
        }
    }

    private fun initializeListener(listener: MiniCartWidgetListener) {
        miniCartWidgetListener = listener
    }

    private fun initializeViewModel(owner: ViewModelStoreOwner, lifecycleOwner: LifecycleOwner) {
        viewModel = ViewModelProvider(owner, viewModelFactory)[MiniCartViewModel::class.java]
        viewModel?.initializeGlobalState()
//        observeGlobalEvent(fragment)
        observeMiniCartWidgetUiModel(lifecycleOwner)
    }

    private fun observeMiniCartWidgetUiModel(lifecycleOwner: LifecycleOwner) {
        viewModel?.miniCartSimplifiedData?.observe(lifecycleOwner) {
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
        validateAmountCtaLabel(miniCartSimplifiedData)
    }

    private fun renderUnavailableWidget(miniCartSimplifiedData: MiniCartSimplifiedData) {
        binding?.miniCartTotalAmount?.apply {
            setLabelTitle("")
            setAmount("")
            val ctaText = viewModel?.miniCartABTestData?.value?.buttonBuyWording
                ?: context.getString(R.string.mini_cart_widget_cta_text_default)
            setCtaText(ctaText)
            amountCtaView.isEnabled = false
            amountCtaView.layoutParams.width = resources.getDimensionPixelSize(R.dimen.mini_cart_button_buy_width)
            amountCtaView.requestLayout()
        }
        binding?.textCannotProcess?.apply {
            text = context.getString(R.string.mini_cart_label_cannot_process)
            show()
        }
        binding?.textCannotProcessQuantity?.apply {
            text = context.getString(R.string.mini_cart_cannot_process_quantity, miniCartSimplifiedData.miniCartWidgetData.unavailableItemsCount)
            show()
        }
        binding?.imageChevronUnavailable?.show()
    }

    private fun renderAvailableWidget(miniCartSimplifiedData: MiniCartSimplifiedData) {
        binding?.miniCartTotalAmount?.apply {
            setLabelTitle(context.getString(R.string.mini_cart_widget_label_see_cart))
            setAmount(CurrencyFormatUtil.convertPriceValueToIdrFormat(miniCartSimplifiedData.miniCartWidgetData.totalProductPrice, false).removeDecimalSuffix())
            val ctaText = viewModel?.miniCartABTestData?.value?.buttonBuyWording
                ?: context.getString(R.string.mini_cart_widget_cta_text_default)
            setCtaText("$ctaText (${miniCartSimplifiedData.miniCartWidgetData.totalProductCount})")
            amountCtaView.isEnabled = true
            amountCtaView.layoutParams.width = resources.getDimensionPixelSize(R.dimen.mini_cart_button_buy_width)
            amountCtaView.requestLayout()
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
            }
        }
        validateTotalAmountView()
    }

    private fun validateTotalAmountView() {
//        binding?.miniCartTotalAmount?.context?.let { context ->
        binding?.miniCartTotalAmount?.enableAmountChevron(true)
        binding?.miniCartTotalAmount?.labelTitleView?.setOnClickListener(miniCartChevronClickListener)
        binding?.miniCartTotalAmount?.amountView?.setOnClickListener(miniCartChevronClickListener)
        binding?.miniCartTotalAmount?.amountChevronView?.setOnClickListener(miniCartChevronClickListener)
//        }
    }

    private fun setAmountViewLayoutParams() {
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp.weight = 0f
        lp.setMargins(0, 0, 4.toPx(), 0)
        binding?.miniCartTotalAmount?.amountView?.layoutParams = lp
    }

    private fun validateAmountCtaLabel(miniCartSimplifiedData: MiniCartSimplifiedData) {
        if (viewModel?.miniCartABTestData?.value?.isOCCFlow == true) {
            // Change button from `Beli Langsung` to `Beli` if ellipsis
            binding?.miniCartTotalAmount?.post {
                val ellipsis = binding?.miniCartTotalAmount?.amountCtaView?.layout?.getEllipsisCount(0) ?: 0
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
    fun updateData(shopIds: List<String>) {
        setTotalAmountLoading(true)
        viewModel?.getLatestWidgetState(shopIds)
    }

    companion object {
        private const val COACH_MARK_TAG = "coachmark_tokonow"

        private const val MINICART_PAGE_SOURCE = "minicart - tokonow"
    }

    data class MiniCartNewWidgetConfig(
        val showTopShadow: Boolean = true
    )
}
