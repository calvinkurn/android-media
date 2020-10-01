package com.tokopedia.tradein.view.viewcontrollers.fragment

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.common_tradein.utils.TradeInUtils
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tradein.R
import com.tokopedia.tradein.TradeInAnalytics
import com.tokopedia.tradein.di.DaggerTradeInComponent
import com.tokopedia.tradein.di.TradeInComponent
import com.tokopedia.tradein.view.viewcontrollers.bottomsheet.TradeInImeiHelpBottomSheet.Companion.newInstance
import com.tokopedia.tradein.viewmodel.TradeInHomeViewModel
import com.tokopedia.tradein.viewmodel.TradeInInitialPriceViewModel
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.tradein_initial_price_fragment.*
import java.util.*
import javax.inject.Inject

class TradeInInitialPriceFragment : BaseViewModelFragment<TradeInInitialPriceViewModel>() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var tradeInAnalytics: TradeInAnalytics
    private lateinit var tradeInInitialPriceViewModel: TradeInInitialPriceViewModel
    private lateinit var tradeinHomeViewModel: TradeInHomeViewModel

    override fun initInject() {
        getComponent().inject(this)
    }

    private fun getComponent(): TradeInComponent =
            DaggerTradeInComponent
                    .builder()
                    .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                    .build()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { observer ->
            val viewModelProvider = ViewModelProviders.of(observer, viewModelProvider)
            tradeinHomeViewModel = viewModelProvider.get(TradeInHomeViewModel::class.java)
        }
        setUpObservers()
        init()
    }

    private fun init() {
        arguments?.apply {
            setMaxPrice(getString(EXTRA_MAX_PRICE, "-"))
            if (getBoolean(EXTRA_IS_ELIGIBLE, false)) {
                isElligible()
            } else {
                notElligible(getString(EXTRA_NOT_ELIGIBLE_MESSAGE, ""))
            }
        }
        tradeInInitialPriceViewModel.checkAndroid10(getTradeInDeviceId())
        tradeinHomeViewModel.tradeInParams.apply {
            product_name.text = productName
            product_price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(newPrice, true)
            ImageHandler.loadImageWithoutPlaceholder(product_image, productImage)
        }
        no_imei_value.text = getTradeInDeviceId()
        model_value.text = StringBuilder().append(Build.MANUFACTURER).append(" ").append(Build.MODEL).toString()
        typography_imei_help.setOnClickListener {
            val tradeInImeiHelpBottomSheet = newInstance()
            tradeInImeiHelpBottomSheet.show(childFragmentManager, "")
            tradeInAnalytics.clickInitialPriceImeiBottomSheet()
        }
        iv_back.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun setUpObservers() {
        tradeInInitialPriceViewModel.imeiStateLiveData.observe(viewLifecycleOwner, Observer { showImei: Boolean ->
            if (showImei) {
                handleImei()
            } else {
                imei_view.hide()
                btn_continue.setOnClickListener {
                    tradeinHomeViewModel.onInitialPriceClick(null)
                }
            }
        })
    }

    private fun getTradeInDeviceId(): String? {
        return TradeInUtils.getDeviceId(context)
    }

    private fun handleImei() {
        imei_view.show()
        no_imei.hide()
        no_imei_value.hide()
        edit_text_imei.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                tradeInAnalytics.clickInitialPriceInputImei()
        }
        btn_continue.setOnClickListener {
            when {
                edit_text_imei.text.length == 15 -> {
                    tradeinHomeViewModel.onInitialPriceClick(edit_text_imei.text.toString())
                }
                edit_text_imei.text.isEmpty() -> {
                    tradeInAnalytics.clickInitialPriceImeiNoInput()
                    typography_imei_description.text = getString(R.string.enter_the_imei_number_text)
                    typography_imei_description.setTextColor(MethodChecker.getColor(context, R.color.tradein_hint_red))
                }
                else -> {
                    tradeInAnalytics.clickInitialPriceImeiWrongInput()
                    typography_imei_description.text = getString(R.string.wrong_imei_string)
                    typography_imei_description.setTextColor(MethodChecker.getColor(context, R.color.tradein_hint_red))
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tradein_initial_price_fragment, container, false)
    }

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelProvider
    }

    override fun getViewModelType(): Class<TradeInInitialPriceViewModel> {
        return TradeInInitialPriceViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        tradeInInitialPriceViewModel = viewModel as TradeInInitialPriceViewModel
    }

    fun notElligible(notEligibleText: String) {
        progress_bar_layout.hide()
        btn_continue.isEnabled = false
        save_upto.hide()
        phone_valid_ticker.show()
        phone_valid_ticker.setTextDescription(notEligibleText)
    }

    private fun setMaxPrice(maxPrice: String) {
        max_price_value.text = maxPrice
        val spannableString = SpannableString(getString(R.string.tradein_save_upto, maxPrice))
        val start = 18
        spannableString.setSpan(StyleSpan(Typeface.BOLD), start, start + maxPrice.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        save_upto.text = spannableString
    }

    private fun isElligible() {
        progress_bar_layout.hide()
        btn_continue.isEnabled = true
    }

    fun setWrongImei(error: String) {
        if (error == getString(R.string.tradein_laku6_imei_error))
            typography_imei_description.text = getString(R.string.wrong_imei_string)
        else
            typography_imei_description.text = error
        typography_imei_description.setTextColor(MethodChecker.getColor(context, R.color.tradein_hint_red))
    }

    companion object {
        private const val EXTRA_MAX_PRICE = "EXTRA_MAX_PRICE"
        private const val EXTRA_IS_ELIGIBLE = "EXTRA_IS_ELIGIBLE"
        private const val EXTRA_NOT_ELIGIBLE_MESSAGE = "EXTRA_NOT_ELIGIBLE_MESSAGE"

        fun getFragmentInstance(maxPrice: String, isEligible: Boolean, notEligibleMessage: String): Fragment {
            return TradeInInitialPriceFragment().also {
                it.arguments = Bundle().apply {
                    putString(EXTRA_MAX_PRICE, maxPrice)
                    putString(EXTRA_NOT_ELIGIBLE_MESSAGE, notEligibleMessage)
                    putBoolean(EXTRA_IS_ELIGIBLE, isEligible)
                }
            }
        }
    }
}