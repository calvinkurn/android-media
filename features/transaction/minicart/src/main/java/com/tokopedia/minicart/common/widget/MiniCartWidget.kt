package com.tokopedia.minicart.common.widget

import android.app.Application
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.MiniCartListBottomSheet
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.common.widget.di.DaggerMiniCartWidgetComponent
import com.tokopedia.minicart.common.widget.uimodel.MiniCartWidgetUiModel
import com.tokopedia.minicart.common.widget.viewmodel.MiniCartWidgetViewModel
import com.tokopedia.totalamount.TotalAmount
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.utils.currency.CurrencyFormatUtil
import javax.inject.Inject

class MiniCartWidget @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var miniCartListBottomSheet: MiniCartListBottomSheet

    private var view: View? = null
    private var totalAmount: TotalAmount? = null
    private var ivTest: ImageView? = null
    private var miniCartWidgetListener: MiniCartWidgetListener? = null

    lateinit var viewModel: MiniCartWidgetViewModel

    init {
        view = inflate(context, R.layout.widget_mini_cart, this)
        ivTest = view?.findViewById(R.id.iv_test)
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
        viewModel.miniCartWidgetUiModel.observe(fragment.viewLifecycleOwner, {
            renderWidget(it)
        })
        viewModel.globalEvent.observe(fragment.viewLifecycleOwner, {
            when (it.state) {
                GlobalEvent.STATE_SUCCESS_UPDATE_CART_FOR_CHECKOUT -> {

                }
                GlobalEvent.STATE_FAILED_UPDATE_CART_FOR_CHECKOUT -> {

                }
            }
        })
    }

    private fun initializeView(fragment: Fragment) {
        totalAmount = view?.findViewById(R.id.mini_cart_total_amount)
        totalAmount?.let {
            it.enableAmountChevron(true)
            it.amountChevronView.setOnClickListener {
                miniCartListBottomSheet.show(fragment.context, fragment.parentFragmentManager, fragment.viewLifecycleOwner, viewModel, ::onMiniCartBottomSheetDismissed)
            }
        }
        totalAmount?.context?.let { context ->
            val chatIcon = getIconUnifyDrawable(context, IconUnify.CHAT, ContextCompat.getColor(context, R.color.Unify_G500))
            totalAmount?.setAdditionalButton(chatIcon)
            totalAmount?.totalAmountAdditionalButton?.setOnClickListener {
                val shopId = viewModel.currentShopIds.value?.firstOrNull() ?: "0"
                val intent = RouteManager.getIntent(
                        context, ApplinkConst.TOPCHAT_ROOM_ASKSELLER, shopId
                )
                context.startActivity(intent)
            }
            ivTest?.setImageDrawable(chatIcon)
        }
        if (totalAmount?.isTotalAmountLoading == false) {
            totalAmount?.isTotalAmountLoading = true
        }
    }

    private fun onMiniCartBottomSheetDismissed() {
        miniCartWidgetListener?.onCartItemsUpdated(MiniCartSimplifiedData())
    }

    /*
    * Function to trigger update mini cart data
    * This will trigger view model to fetch latest data from backend and update the UI
    * */
    fun updateData(shopIds: List<String>) {
        viewModel.getLatestWidgetState(shopIds)
    }

    /*
    * Function to trigger update mini cart data
    * This will trigger widget to update the UI with provided data
    * */
    fun updateData(miniCartWidgetData: MiniCartWidgetData) {
        renderWidget(MiniCartWidgetUiModel(
                state = MiniCartWidgetUiModel.STATE_NORMAL,
                totalProductPrice = miniCartWidgetData.totalProductPrice,
                totalProductCount = miniCartWidgetData.totalProductCount
        ))
    }

    private fun initializeInjector(baseAppComponent: Application?) {
        if (baseAppComponent is BaseMainApplication) {
            DaggerMiniCartWidgetComponent.builder()
                    .baseAppComponent(baseAppComponent.baseAppComponent)
                    .build()
                    .inject(this)
        }
    }

    private fun renderWidget(miniCartWidgetUiModel: MiniCartWidgetUiModel) {
        totalAmount?.apply {
            setLabelTitle(context.getString(R.string.mini_cart_widget_label_total_price))
            setAmount(CurrencyFormatUtil.convertPriceValueToIdrFormat(miniCartWidgetUiModel.totalProductPrice, false))
            setCtaText(String.format(context.getString(R.string.mini_cart_widget_label_buy), miniCartWidgetUiModel.totalProductCount))
        }
        totalAmount?.isTotalAmountLoading = false
    }

}