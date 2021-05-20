package com.tokopedia.minicart.common.widget

import android.app.Application
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.MiniCartListBottomSheet
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.common.widget.di.DaggerMiniCartWidgetComponent
import com.tokopedia.minicart.common.widget.uimodel.MiniCartWidgetUiModel
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
    private var fragmentManager: FragmentManager? = null
    private var totalAmount: TotalAmount? = null
    private var miniCartWidgetListener: MiniCartWidgetListener? = null

    lateinit var viewModel: MiniCartWidgetViewModel

    init {
        view = inflate(context, R.layout.widget_mini_cart, this)
    }

    /*
    * Function to initialize the widget if being used on activity
    * */
    fun initialize(activity: FragmentActivity, listener: MiniCartWidgetListener) {
        val application = activity.application
        initializeInjector(application)
        initializeView(activity)

        viewModel = ViewModelProvider(activity, viewModelFactory).get(MiniCartWidgetViewModel::class.java)
        miniCartWidgetListener = listener
        fragmentManager = activity.supportFragmentManager
        viewModel.miniCartWidgetUiModel.observe(activity, {
            renderWidget(it)
        })
    }

    private fun initializeView(activity: FragmentActivity) {
        totalAmount = view?.findViewById(R.id.mini_cart_total_amount)
        totalAmount?.let {
            it.enableAmountChevron(true)
            it.amountChevronView.setOnClickListener {
                fragmentManager?.let {
                    miniCartListBottomSheet.show(it, activity)
                }
            }
        }
        totalAmount?.isTotalAmountLoading = true
    }

    private fun initializeView(fragment: Fragment) {
        totalAmount = view?.findViewById(R.id.mini_cart_total_amount)
        totalAmount?.let {
            it.enableAmountChevron(true)
            it.amountChevronView.setOnClickListener {
                fragmentManager?.let {
                    miniCartListBottomSheet.show(it, fragment)
                }
            }
        }
        totalAmount?.isTotalAmountLoading = true
    }

    /*
    * Function to initialize the widget if being used on fragment
    * */
    fun initialize(fragment: Fragment, listener: MiniCartWidgetListener) {
        fragment.activity?.let {
            val application = it.application
            initializeInjector(application)
        }
        initializeView(fragment)

        viewModel = ViewModelProvider(fragment, viewModelFactory).get(MiniCartWidgetViewModel::class.java)
        miniCartWidgetListener = listener
        fragmentManager = fragment.parentFragmentManager
        viewModel.miniCartWidgetUiModel.observe(fragment, {
            renderWidget(it)
        })
    }

    /*
    * Function to trigger update mini cart data
    * This will trigger view model to fetch latest data from backend and update the UI
    * */
    fun updateData() {
        viewModel.getLatestState()
    }

    /*
    * Function to trigger update mini cart data
    * This will trigger widget to update the UI with provided data
    * */
    fun updateData(miniCartWidgetData: MiniCartWidgetData) {
        renderWidget(MiniCartWidgetUiModel(
                state = MiniCartWidgetUiModel.STATE_NORMAL,
                totalProductError = miniCartWidgetData.totalProductError,
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
        totalAmount?.isTotalAmountLoading = false
        totalAmount?.apply {
            setLabelTitle(context.getString(R.string.mini_cart_widget_label_total_price))
            setAmount(CurrencyFormatUtil.convertPriceValueToIdrFormat(miniCartWidgetUiModel.totalProductPrice, false))
            setCtaText(String.format(context.getString(R.string.mini_cart_widget_label_buy), miniCartWidgetUiModel.totalProductCount))
        }
        totalAmount?.isTotalAmountLoading = false
    }

}