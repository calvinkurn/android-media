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
import com.tokopedia.minicart.cartlist.GlobalErrorBottomSheet
import com.tokopedia.minicart.cartlist.MiniCartListBottomSheet
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.common.widget.di.DaggerMiniCartWidgetComponent
import com.tokopedia.minicart.common.widget.uimodel.MiniCartWidgetUiModel
import com.tokopedia.minicart.common.widget.viewmodel.MiniCartWidgetViewModel
import com.tokopedia.totalamount.TotalAmount
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil
import javax.inject.Inject

class MiniCartWidget @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

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

    lateinit var viewModel: MiniCartWidgetViewModel

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
        viewModel.globalEvent.observe(fragment.viewLifecycleOwner, {
            when (it.state) {
                GlobalEvent.STATE_FAILED_LOAD_MINI_CART_LIST_BOTTOM_SHEET -> {
                    miniCartListBottomSheet.dismiss()
                    fragment.context?.let {
                        globalErrorBottomSheet.show(fragment.parentFragmentManager, it, GlobalErrorBottomSheet.TYPE_FAILED_TO_LOAD) {
                            showMiniCartListBottomSheet(fragment)
                        }
                    }
                }
                GlobalEvent.STATE_SUCCESS_UPDATE_CART_FOR_CHECKOUT -> {

                }
                GlobalEvent.STATE_FAILED_UPDATE_CART_FOR_CHECKOUT -> {
                    fragment.context?.let {
                        globalErrorBottomSheet.show(fragment.parentFragmentManager, it, GlobalErrorBottomSheet.TYPE_FAILED_TO_LOAD) {

                        }
                    }
                }
            }
        })
    }

    private fun observeMiniCartWidgetUiModel(fragment: Fragment) {
        viewModel.miniCartWidgetUiModel.observe(fragment.viewLifecycleOwner, {
            renderWidget(it)
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
                viewModel.updateCart(true)
            }
        }
        setTotalAmountLoading(true)
        setTotalAmountChatIcon()
    }

    private fun showMiniCartListBottomSheet(fragment: Fragment) {
        miniCartListBottomSheet.show(fragment.context, fragment.parentFragmentManager, fragment.viewLifecycleOwner, viewModel, ::onMiniCartBottomSheetDismissed)
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
                val shopId = viewModel.currentShopIds.value?.firstOrNull() ?: "0"
                val intent = RouteManager.getIntent(
                        context, ApplinkConst.TOPCHAT_ROOM_ASKSELLER, shopId
                )
                context.startActivity(intent)
            }
            this.chatIcon?.setImageDrawable(chatIcon)
        }
    }

}