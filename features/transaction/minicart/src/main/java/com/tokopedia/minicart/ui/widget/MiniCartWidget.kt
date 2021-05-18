package com.tokopedia.minicart.ui.widget

import android.app.Application
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.minicart.R
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.ui.widget.di.DaggerMiniCartWidgetComponent
import com.tokopedia.minicart.ui.widget.uimodel.MiniCartWidgetUiModel
import com.tokopedia.totalamount.TotalAmount
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.utils.currency.CurrencyFormatUtil
import javax.inject.Inject

class MiniCartWidget @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var totalAmount: TotalAmount? = null
    private var miniCartWidgetListener: MiniCartWidgetListener? = null

    lateinit var viewModel: MiniCartWidgetViewModel

    init {
        val v = inflate(context, R.layout.widget_mini_cart, this)
        totalAmount = v.findViewById(R.id.mini_cart_total_amount)
        totalAmount?.let {
            it.enableAmountChevron(true)
            it.amountChevronView.setOnClickListener {
                // Todo : open mini cart bottomsheet or summary transaction bottomsheet
            }
        }

        totalAmount?.isTotalAmountLoading = true
    }

    fun setup(activity: AppCompatActivity, listener: MiniCartWidgetListener) {
        val application = activity.application
        inject(application)

        viewModel = ViewModelProvider(activity, viewModelFactory).get(MiniCartWidgetViewModel::class.java)
        miniCartWidgetListener = listener
        viewModel.miniCartWidgetUiModel.observe(activity, {
            renderWidget(it)
        })
    }

    fun setup(fragment: Fragment, listener: MiniCartWidgetListener) {
        fragment.activity?.let {
            val application = it.application
            inject(application)
        }

        viewModel = ViewModelProvider(fragment, viewModelFactory).get(MiniCartWidgetViewModel::class.java)
        miniCartWidgetListener = listener
        viewModel.miniCartWidgetUiModel.observe(fragment, {
            renderWidget(it)
        })
    }

    private fun inject(baseAppComponent: Application?) {
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
            setLabelTitle("Total Harga")
            setAmount(CurrencyFormatUtil.convertPriceValueToIdrFormat(miniCartWidgetUiModel.totalProductPrice, false))
            setCtaText("Beli (${miniCartWidgetUiModel.totalProductCount})")
        }
        totalAmount?.isTotalAmountLoading = false
    }

    fun updateData() {
        viewModel.getLatestState()
    }

    fun updateData(miniCartWidgetData: MiniCartWidgetData) {
        renderWidget(MiniCartWidgetUiModel(
                state = MiniCartWidgetUiModel.STATE_NORMAL,
                totalProductError = miniCartWidgetData.totalProductError,
                totalProductPrice = miniCartWidgetData.totalProductPrice,
                totalProductCount = miniCartWidgetData.totalProductCount
        ))
    }

}